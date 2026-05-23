# FocusLauncher (working name: **Built**) — Project Plan

> Source-of-truth synthesis of `../docs/00-vision.md` … `../docs/13-naming-and-positioning.md`.
> Companion artifacts: `features-by-stage.json` (every shipped-or-shippable feature, stage-bucketed) and `challenges.md` (everything that does NOT qualify for the spec list).
> Plan date: 2026-05-17.

---

## 1. Product thesis

A hyper-restrictive Android launcher that turns the phone from a toy into a tool, then expands into a full "OS for a life worth building."

The product wins by stacking three things no competitor has all of:

1. **Launcher** — we OWN the home screen, so we control every app launch.
2. **Auto-ingested life integrations** — HealthConnect, Plaid, Strava, Spotify, Letterboxd, Photos, Calendar.
3. **Year-end artifact** — Built Wrapped, multi-domain, the viral surface no life-OS has shipped.

Discipline is private. A discipline **movement** is social. Both surfaces must coexist.

---

## 2. Core principles (load-bearing)

1. Friction is the product — don't block, make distraction progressively painful.
2. Social > willpower — group accountability beats individual discipline.
3. Identity over rules — every action is a vote for Consumer vs Creator.
4. Uninstall = group decision — you can't quit alone.
5. Compound rewards — good behavior earns compound interest (time, money, status).
6. Honest mirror — show reality without judgment.
7. Boredom is a feature — train tolerance for non-stimulation.
8. Start embarrassingly easy — Level 1 of any Track should feel like "that's it?"
9. Demotion = compassion, not punishment.
10. Manual entry decays in 30 days — voice or auto-ingest only for ongoing data.

---

## 3. Strategic narrative (year-by-year)

| Year | Identity | Surface area |
|------|----------|--------------|
| Y1   | "The launcher that won't let you quit." | Launcher + Tracks + Anti-uninstall + Productivity loop |
| Y2   | "Your phone, now your accountability partner." | + Social, Mantra Gate, Sad Self, Group voting, Couples, Wrapped |
| Y3   | "OS for a life worth building."           | + Finance, fitness, location, life-OS, marketplace, B2B dashboards |

---

## 4. Tech stack (recommended defaults)

| Layer | Choice | Rationale |
|-------|--------|-----------|
| Mobile platform | **Android-only at first** | iOS sandbox makes 80% of the concept impossible. |
| Language / UI | **Kotlin + Jetpack Compose** | Native, deep API access for launcher/admin/accessibility. |
| Local data | **Room (SQLite) + DataStore** | On-device source of truth. |
| Backend | **Firebase (Firestore + Auth + Cloud Functions + Remote Config + Storage)** | Free tier covers MVP, single vendor for auth/data/push/functions/hosting, native Google integration for billing + identity. |
| Push | **FCM** (part of Firebase) | Group features, shame notifications, dual-streak nudges. |
| Payment — digital (in-app) | **Google Play Billing (native) — `PAY-001`** | Required by Play policy for digital features sold inside the Android app. Channel chosen at runtime by the remote-config flag (see `payment-architecture.md`). |
| Payment — physical + B2B | **Web checkout: Stripe + Google Pay button — `PAY-002`** | Hosted on the Chrome-homepage web app (`PLATFORM-002`). For physical goods (Print Shop) and B2B subscriptions where Play Billing isn't required. |
| Payment routing | **Firebase Remote Config / Firestore `/config/payments`** | Single backend boolean `nativeOnDevice` flips on-device vs web checkout without an app update. Local feature flag `PAYMENTS_NATIVE_ON_DEVICE` is the offline default. |
| Web app | **Firebase Hosting** (`PLATFORM-002`) | Chrome homepage — read-only dashboard preview + web checkout surface. |
| AI (on-device) | **Gemma Nano / Phi-3 mini** | Sad Self generation, Auto-Insight Engine, voice-to-structured-data. |
| AI (cloud, optional) | Anthropic API | Premium tier features only; default to on-device for privacy. |
| Distribution | **Play Store (core) + sideload APK (extreme mode)** | Play Store for reach, sideload for nuclear features that Play Store rejects. |
| Companion | **WearOS** (V3), **Chrome-homepage web app** (V3 `PLATFORM-002`) | HR sensors, desk-side read-only stats, and the checkout surface. |

ADR-grade decisions still pending: see `../docs/04-decisions-needed.md` and `challenges.md` §"Open decisions".

---

## 5. Architecture (target shape)

```
┌─────────────────────────────────────────────┐
│                Built / FocusLauncher          │
├─────────────────────────────────────────────┤
│  UI (Compose)                                 │
│  • Home screen (projects, todos, behavior)    │
│  • Lobby / Cognitive Tax / Intercept overlays │
│  • Mantra Gate                                │
│  • Onboarding sequencer                       │
│  • Journal / Wrapped views                    │
├─────────────────────────────────────────────┤
│  Service layer                                │
│  • UsageTracker (UsageStatsManager)           │
│  • AccessibilityService (intercept overlay)   │
│  • NotificationListenerService                │
│  • DeviceAdminReceiver                        │
│  • Foreground monitoring service              │
│  • RuleEngine (which restrictions apply)      │
│  • ContextEngine (GPS, time, calendar)        │
│  • SadSelfEngine (templates → LLM)            │
│  • TrackEngine (levels, promotion, demotion)  │
│  • SafetyEngine (crisis detection)            │
├─────────────────────────────────────────────┤
│  Data layer                                   │
│  • Room DB (projects, todos, journal, snaps)  │
│  • DataStore (prefs, streaks, mantra cfg)     │
│  • EncryptedSharedPrefs (passphrase hash)     │
├─────────────────────────────────────────────┤
│  Integrations                                 │
│  • HealthConnect, Calendar, Photos            │
│  • Plaid / TrueLayer / Tink                   │
│  • Strava, Spotify, Letterboxd, GitHub        │
│  • OAuth manager + token refresh              │
├─────────────────────────────────────────────┤
│  Backend (Firebase)                           │
│  • Auth (Google Sign-In, anonymous)           │
│  • Firestore: groups, votes, entitlements,    │
│    `/config/payments` remote router doc       │
│  • Realtime presence + streak sync            │
│  • FCM push (shame, dual-streak, due-letter)  │
│  • Cloud Functions:                           │
│      - verifyPlayPurchase (Billing webhook)   │
│      - verifyStripeWebhook (web checkout)     │
│      - grantEntitlement (writes uid→product)  │
│      - generateWrapped                        │
│  • Firebase Hosting: Chrome-homepage web app  │
│  • Remote Config: payment channel + kill swit │
├─────────────────────────────────────────────┤
│  Payments                                     │
│  • PaymentRouter (in-app)                     │
│      reads /config/payments.nativeOnDevice    │
│        → true:  BillingClient (Google Play)   │
│        → false: CustomTabs to web checkout    │
│  • Web checkout: Stripe + Google Pay button   │
│  • Entitlements live in Firestore +           │
│    mirrored to local DataStore                │
└─────────────────────────────────────────────┘
```

---

## 6. Stage roadmap

Detailed feature buckets live in [features-by-stage.json](features-by-stage.json). Summary:

### Stage 1 — MVP (4–6 weeks, solo dev)
**Theme: "A launcher that hurts to leave."**
Custom home screen, app dock, hidden drawer, usage tracking, basic widgets, **The Lobby**, Cognitive Tax, dimming overlay, VIP contacts, silent-mode enforcement, todo + project basics, behavior indicator, intervention screen, Device Admin + 72hr cooldown, onboarding sequencer, "why are you here?" capture, manipulation transparency page, accessibility pass.

**Exit criteria:** Author dogfoods for 2 weeks straight without bypassing.

### Stage 2 — V1 (3–4 months)
**Theme: "The Track system unlocks the rest." + the payment plumbing.**

Payment / backend ships here even though the consumer-facing paid features
mostly live in Stage 3, because the routing infrastructure (`PAY-001`,
`PAY-002`, `PAY-003`, `PLATFORM-002`) is what unlocks them. The Firebase
project, Cloud Functions, Hosting target, and `/config/payments` document
all come up in Stage 2 with no products yet for sale.

Track engine (10 levels, promotion/demotion, baseline detection, grace days, streak freezes, after-fall ritual), Mantra Gate + Whisper mode, emergency-pass system, full Sad Self engine + 4 voices + celebration self, Identity Voting, Session Receipts, Legacy Counter, Builder/Consumer mode, Mirror Widget, Future Self Video, Last Day Test, Phantom Vibration counter, Compound Time Bank, productivity heatmap, streak system, applause, journal, focus timer, face-down detection, morning routine, shutdown ritual, One Thing, time-blocking enforcer, Nourishing Consumption, Mood Pings, Crisis Detection / Soften Mode, Encrypted Backup, Data Export, HealthConnect, Sleep Stats, Daily Activity Loop, Dream Mode, context-aware locks, full restriction stack (variable ratio, boredom preservatory, intent declaration, escalating lockout, gatekeeper tasks, time debt, reverse notifications, replacement engine, app roulette, dream mode, one-app-at-a-time), graduated freedom.

**Exit criteria:** 100 beta testers, 30-day D30 retention >40%.

### Stage 3 — V2 (6+ months)
**Theme: "Social is the moat."**
Groups, group uninstall vote, shame notifications, team streaks, Disappointment API, co-working presence, sponsor system, Public Builder Profile, **Built Wrapped**, **Built Verification badge**, dual streaks, push reactions, focus DMs, focus stories, anti-bio, highlight reels, receipt wall, chronological feed, public pre-commitments, quiet brag, vault stories, disappointment inbox, public shame post on uninstall, relapse prediction, money stake (escrow), streak insurance display, NFC physical key, breath-to-unlock, posture lock, Couples Mode, Phone Sabbath Together, Parent/Child Pair, account recovery via trusted contacts, Plaid integration, auto-categorized spending, net worth, subscription hunter, savings rate, time-money conversion, sleep correlator, sleep window guardrails, hangover calculus, caffeine half-life, meditation log, stress weather, location heatmap, time-per-place, third-place tracker, commute tax, outdoor minutes / nature score, travel atlas, fitness PRs / workout log / recovery score / sport-specific clocks, Personal Bloomberg Terminal, Auto-Insight Engine, Sunday Life Review, Beach Days widget, Death Clock widget (opt-in), Compounding Curve, reading stats + highlight resurfacing, voice-to-structured-data capture, Strava/Spotify/Photos/Calendar integrations, kazoo / narrator / time-dilation / voice authenticity (absurdist tier), pattern detection, deathbed simulator, anchoring attack, app tombstones, future-self letters, on-device AI for Sad Self.

**Exit criteria:** 10k users; one viral Wrapped season; first paying B2B pilot.

### Stage 4 — V3 (12+ months)
**Theme: "OS for life."**
Graduate Mode, Therapist / Coach Dashboard, Team Focus Dashboard, Web / Desktop Companion, Print Shop (heatmap, journal, NFC puck), Track Marketplace, Mentor Subscriptions, WearOS companion, Skill Tree Phone, Focus Score Economy, Reps-as-Currency, Counterfactual Twin, Digital Twin Timeline, AI Life Coach, Personal Annual Report, Built Wrapped — Life Edition (multi-domain), Built Wrapped — Money Edition, Confession Booth, Aura Avatar, Brutal Mirror Filter, Focus Aura Filter, Focus Map, Live Focus Rooms, Focus Duels, Time Donation, Late Night Crew, Mantra Mentions, Best Friends List, Public Phone Funeral, Streak Charms, Hashtag Tracks, Anti-Influencer Tab, Today's Top Builders, Topic-Level Filter, Lite Mode, plate-photo nutrition, fasting auto-detect, trigger-food detector, cooking ratio, spend heatmap by place, future-self budget projector, regret receipts, money mirror tile, tax-aware income tracker, couples joint finance, Built Wrapped — Money Edition, sound-fingerprint place ID, AQ/UV exposure ledger, substance log, voice-tone EQ, subconscious dump, cognitive vitals, dream journal + AI symbols, reading-as-thinking loop, vocabulary growth, daily selfie timelapse, Promise Kept Ratio, Rejection Counter, Risks-Taken Log, Things-Made Counter, People-Met Graph, reciprocity score, last-contacted, family time budget, GitHub integration, Letterboxd / Trakt / TV Time, Goodreads / Kindle / Audible, family welfare check, form check video, confession call, sound diet, the paradox lock, choice overload weapon, weather-gated apps, voice-to-todo, idea parking lot, input/output ratio, effort estimation, energy zones, earned pixels.

**Exit criteria:** 100k DAU, B2B subscriptions live, hardware product (Anchor NFC puck) shipped.

---

## 7. The implementation sequence (week-by-week, MVP only)

| Week | Deliverable |
|------|-------------|
| 0 | Decisions in `../docs/04-decisions-needed.md` finalized. Domain/trademark gates for "Built" run (see §"Naming"). Repo + CI scaffold. Compose + Room + Hilt skeleton. |
| 1 | `android.intent.category.HOME` launcher registered. Empty home screen with placeholder widgets. App-drawer hidden, search-only. |
| 2 | UsageStatsManager wired. Foreground app detection. AccessibilityService scaffold (passes Play Store review checklist). NotificationListenerService scaffold. |
| 3 | The Lobby — overlay countdown before distraction app opens. Cognitive Tax v1 (simple math puzzle). Progressive dimming overlay. |
| 4 | Todo + project Room schema. Behavior indicator algorithm. Intervention screen. Calls: VIP + silent mode. Onboarding sequencer + "Why are you here?" capture. |
| 5 | Device Admin registration. In-app 72hr deactivation flow. Manipulation transparency page. Accessibility pass (TalkBack, type-mantra fallback). |
| 6 | Internal alpha. Author dogfoods for 14 days. Bug bash. Polish onboarding ritual. Ship MVP to ~10 trusted testers via APK. |

---

## 8. Onboarding ritual (the first 20 minutes decide everything)

`ONBOARD-001` is the single highest-leverage MVP feature. The flow:

1. "Why are you here?" — type/record the pain (saved verbatim, surfaced at every weak moment).
2. Permission walkthrough: Usage stats, Accessibility, Device Admin, Notifications, Contacts.
3. Pick mantra (suggest 4 defaults + custom). Test voice detection.
4. Record Future Self video (60–90s). Stored locally.
5. Pick VIP contacts (max 10).
6. Optional: invite an accountability partner.
7. 3-day **baseline observation** begins — no restrictions yet. App watches.
8. Day 4: app suggests a Level-1 Track based on your real usage (max 25% delta from baseline).

---

## 9. Decisions still open (must land before code freeze)

Tracked in `../docs/04-decisions-needed.md`; surfaced in `challenges.md` §"Open decisions". Headline ones:

- D1: which 5–8 MVP features make the cut? (Plan above proposes a list.)
- D2: solo-first / group-required / hybrid?
- D3: Play Store / sideload / both?
- D5: Firebase / Supabase / custom backend?
- D6: monetization (freemium / one-time / OSS+donations / subscription)?
- D7: tone (drill sergeant / therapist / witty / monk)?
- D8: aggressiveness on day 1?
- D9: rename to **Built**? Gate on domain + trademark + Play Store search.

---

## 10. Naming & positioning

Working name: **FocusLauncher**. Recommended product name: **Built**. Sub-brands: **Climb** (track system), **Anchor** (hardware line / NFC puck), **Wrapped** (annual recap), **Vault** (encrypted data), **Mantra**, **Lobby**.

Six gates to clear before commit (`../docs/13-naming-and-positioning.md` §"Before deciding"): domain availability, Play Store search, USPTO trademark in class 9/42, brand-search hygiene, social handles, negative-association check.

Default tagline candidates: "Built. Day by day." / "The OS for a life worth building." / "You're not your phone. You're what you build."

---

## 11. Permissions matrix (Play Store risk)

| Permission | Used by | Risk |
|------------|---------|------|
| `android.intent.category.HOME` | Launcher | ✅ Standard |
| `QUERY_ALL_PACKAGES` | App discovery | ⚠️ Justify clearly |
| `PACKAGE_USAGE_STATS` | All restriction logic | ✅ User-granted in Settings |
| `SYSTEM_ALERT_WINDOW` | Overlays (lobby, dimming, receipts) | ⚠️ Special permission |
| `BIND_ACCESSIBILITY_SERVICE` | Intercept, scroll detection | ⚠️ Play Store review — must justify |
| `BIND_DEVICE_ADMIN` | Anti-uninstall | ⚠️ Extra review |
| `READ_PHONE_STATE` + `CALL_PHONE` | VIP / pass system | ✅ |
| `SEND_SMS` | Auto-reply on decline | ⚠️ Sensitive |
| `RECORD_AUDIO` | Mantra Gate, breath unlock, voice capture | ⚠️ Justify |
| `CAMERA` | Mirror Widget, form check | ✅ |
| `ACCESS_FINE_LOCATION` | Context-aware locks, location heatmap | ⚠️ Background-location sensitive |
| `NotificationListenerService` | Notification dismissal + content | ⚠️ |
| `WRITE_SETTINGS` | Brightness / DND control | ✅ |
| HealthConnect scopes | Body/mind data | ✅ permissioned per-scope |
| `BIND_VPN_SERVICE` | Per-app firewall (RESTRICT-020) | ⚠️ Special |
| `com.android.vending.BILLING` | Google Play Billing (`PAY-001`) | ✅ Standard for paid apps |
| Internet access (for Firebase) | Auth, Firestore, FCM, Remote Config | ✅ Standard |

Mitigation: split into **Play Store edition** (compliant) + **sideload extreme APK** (full power). Open-source the core to build trust.

---

## 12. Success metrics

- **D1:** Onboarding completion rate (target ≥70%).
- **D7:** First Track Level-1 promotion rate (target ≥50%).
- **D30:** Retention with at least one active Track (target ≥40%).
- **D90:** Group attachment (% of D30 users in a 2+ person accountability group, target ≥30%).
- **Lifetime:** Time-saved per user vs baseline (target ≥1 hr/day after 6 months).
- **Viral:** Built Wrapped share rate among DAU (target ≥15% in launch month).
- **Anti-uninstall:** % of uninstall attempts aborted during the 72hr cooldown (target ≥80%).

---

## 13. Risks, ethics, legal — see `challenges.md`

Anything that does not fit cleanly in this plan or the feature spec (impossible features, open decisions, ethical hazards, legal exposure, market risks, naming gates, philosophy tensions) is filed in `challenges.md`. Read that file alongside this one before kickoff.
