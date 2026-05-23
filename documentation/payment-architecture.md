# Payment architecture

How money flows through FocusLauncher. Two channels (native on-device + web)
with a Firebase-driven flag deciding which one the user sees.

## Goals

1. Take payments without standing up our own payment processor.
2. Avoid the Play Store 30% cut when we can — but only when policy lets us.
3. Keep the on-device flow shippable as soon as possible; web is the
   fallback while native is pending.
4. Make the channel choice flippable from the backend, no app update needed.

## Channels

### Native — Google Play Billing (`PAY-001`)

On-device in-app purchases handled by the Google Play Billing Library.
Payment goes through the user's Google Pay / Play Store account.
This is the canonical channel for **consumable** in-app items and
**subscription** features inside the launcher itself (per Play policy).

- Library: `com.android.billingclient:billing-ktx`
- Products: defined in the Google Play Console
- Purchase token verification: server-side via Firebase Cloud Functions
- Refund handling: Play Store policy + our 72hr-cool-down uninstall flow
- Receipts: persisted to Firestore under `users/{uid}/purchases/{orderId}`

### Web — Chrome homepage checkout (`PAY-002`, `PLATFORM-002`)

A static web app (the Chrome homepage we'll ship as part of `PLATFORM-002`)
takes payment for things Play Store policy doesn't require us to route
through Billing — currently only **physical merchandise** (`MERCH-001`)
and **B2B subscriptions** (`B2B-001`, `B2B-002`).

- Hosting: Firebase Hosting
- Processor: Google Pay for Web (`google-pay-button-element`) so user
  identity matches their Android device; Stripe as the merchant backend
  with Google Pay as the payment method
- Identity link: web checkout requires the user to sign in with the same
  Google account; Cloud Function verifies and writes the purchase under
  the matching `users/{uid}/purchases/{orderId}` document
- Used when: native flow is disabled by the remote flag OR Play policy
  forbids the product

## The remote router (`PAY-003`)

Single Firebase Firestore document `/config/payments` shaped as:

```json
{
  "nativeOnDevice": true,
  "webFallbackUrl": "https://built.app/pay",
  "updatedAt": <serverTimestamp>
}
```

`nativeOnDevice = true` means: show the in-app Billing flow when the user
taps "buy". `false` means: open `webFallbackUrl` in Custom Tabs with the
purchase intent encoded.

The app reads this on cold start (and at most once per hour while warm),
caches the result in DataStore, and falls back to the local feature flag
`PAYMENTS_NATIVE_ON_DEVICE` if Firestore is unreachable.

## Local feature flag

`PAYMENTS_NATIVE_ON_DEVICE` exists in `assets/featureflags.json` for two
reasons:

1. **Offline default.** When the user has no network or Firebase isn't
   initialized (no `google-services.json`), the launcher needs a sensible
   answer. The default is `false` — open the web checkout — because that
   path works without Play Billing setup.

2. **Dev override.** Devs can force a value from the in-app settings
   screen. The Firebase remote value still wins on next sync.

```kotlin
// Effective value the app uses:
val nativeOnDevice =
    firebaseConfig.nativeOnDevice ?: flags[PAYMENTS_NATIVE_ON_DEVICE] ?: false
```

## What gets sold

| Product | Channel | Reason |
|---|---|---|
| Premium subscription (Stage 2/3 features) | Native (Play Billing) | Play policy requires it for digital features |
| Money Stake escrow (`FINANCE-001`) | TBD — see below | Escrow + refund logic is gnarly; Play Billing supports it but only via subscriptions/consumables, not custom holds |
| Print Shop physical goods (`MERCH-001`) | Web | Physical product — Play policy says we MUST use external |
| Therapist / Coach Dashboard (`B2B-001`) | Web | B2B SaaS — not consumed in the Android app |
| Team Focus Dashboard (`B2B-002`) | Web | Same |
| Mentor Subscriptions (`CREATOR-002`) | Native | Digital feature inside the app |
| Track Marketplace (`CREATOR-001`) | Native for purchase, web for creator payout | Buyers in-app, creators paid via Stripe Connect |

## Open decisions

- **Money Stake (FINANCE-001).** Play Billing technically supports
  consumable purchases that can be refunded, but the "donate forfeit to
  cause you hate" flow probably has to go through web + Stripe Connect.
  Gambling-law review pending per `challenges.md` §4.
- **Tax-aware income tracker (FIN-010).** If we hold escrow, we may be
  considered a payment processor — needs legal sign-off.
- **Couples joint finance (FIN-011).** Whichever way payments flow, the
  shared-account model touches both partners — needs scoping.

## Why not Stripe directly on Android?

Play Store policy: any **digital** good consumed inside an Android app
that's available on Google Play **must** go through Play Billing. Using
Stripe-on-Android for a feature unlock would risk app removal.

Web checkout (Stripe + Google Pay as the payment method) is fine because
the purchase isn't consumed in the Android app — it unlocks a server-side
flag that the app reads.

## Sequence

### Native purchase (on-device flag = true)

```
User taps "Buy" in a paywall surface
  → PaymentRouter.checkout(productId)
  → PaymentRouter sees nativeOnDevice=true
  → BillingClient.launchBillingFlow(productId)
  → Google Play UI handles consent + Google Pay
  → onPurchasesUpdated callback writes to Firestore via Cloud Function
  → Cloud Function verifies purchase token, sets users/{uid}/entitlements/{productId}=true
  → App observes Firestore entitlement → unlocks feature
```

### Web purchase (on-device flag = false)

```
User taps "Buy" in a paywall surface
  → PaymentRouter.checkout(productId)
  → PaymentRouter sees nativeOnDevice=false
  → Build deep-link: webFallbackUrl + "?p=" + productId + "&uid=" + uid + "&sig=" + hmac
  → Open in CustomTabsIntent
  → Web app: Google Pay button collects payment via Stripe
  → Stripe webhook hits Cloud Function
  → Cloud Function verifies + writes users/{uid}/entitlements/{productId}=true
  → User returns to app (Custom Tab dismissed); app polls Firestore once
  → Entitlement appears → feature unlocked
```

## Code shape

```
app/src/main/java/.../payment/
  ├── Entitlements.kt           // Flow<Set<String>> of unlocked product IDs
  ├── PaymentRouter.kt          // checkout(productId): routes native or web
  ├── BillingClientWrapper.kt   // Play Billing integration (PAY-001)
  └── WebCheckoutLauncher.kt    // CustomTabsIntent + URL builder (PAY-002)

functions/                       // Firebase Cloud Functions (TypeScript)
  ├── verifyPlayPurchase.ts      // Play Billing webhook
  ├── verifyStripeWebhook.ts     // Stripe webhook
  └── grantEntitlement.ts        // common writer

web/                             // Static site for the Chrome homepage (PLATFORM-002)
  ├── index.html                 // homepage + dashboard preview
  └── checkout.html              // Stripe + Google Pay
```

## Status

| Component | Status |
|---|---|
| `PAYMENTS_NATIVE_ON_DEVICE` flag in JSON | will land with payment scaffolding |
| `PaymentRouter` skeleton + entitlement Flow | will land with payment scaffolding |
| `BillingClientWrapper` | NOT started — needs Play Console product setup first |
| `WebCheckoutLauncher` | NOT started — needs web app deployment |
| Web app (`PLATFORM-002`) | NOT started |
| Cloud Functions | NOT started — Firebase project not provisioned |
| Firestore `/config/payments` document | NOT created — needs Firebase project |
