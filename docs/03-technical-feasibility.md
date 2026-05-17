# FocusLauncher — Technical Feasibility Analysis

## Android APIs & Permissions Required

### Core Launcher Functionality

| API/Permission | What It Enables | Risk Level |
|---|---|---|
| `android.intent.category.HOME` | Replace default launcher (home screen control) | ✅ Standard |
| `QUERY_ALL_PACKAGES` | See installed apps, launch them | ⚠️ Google scrutinizes |
| `PACKAGE_USAGE_STATS` | Screen time per app, foreground detection | ✅ User grants in settings |
| `SYSTEM_ALERT_WINDOW` | Overlays on top of other apps (for intercepts, receipts) | ⚠️ Special permission |
| `BIND_ACCESSIBILITY_SERVICE` | Detect current app, intercept actions, read screen | ⚠️ Google Play review |
| `BIND_DEVICE_ADMIN` | Prevent uninstall, lock device, enforce policies | ⚠️ Extra Play Store review |
| `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` | Keep service alive in background | ✅ User grants |

### Feature-Specific Requirements

| Feature Category | Required APIs | Feasibility |
|---|---|---|
| App blocking/time limits | UsageStatsManager + Accessibility + Overlay | ✅ Proven (existing apps do this) |
| Home screen replacement | Launcher Intent + custom UI | ✅ Standard launcher pattern |
| Context-aware locks (GPS) | ACCESS_FINE_LOCATION + Geofencing API | ✅ Standard |
| Calendar integration | READ_CALENDAR | ✅ Standard |
| Weather gating | Weather API call (no permission needed) | ✅ Trivial |
| Biometric (heart rate) | Health Connect API or WearOS companion | ⚠️ Requires watch |
| NFC unlock | NFC permission | ✅ Standard |
| Camera (mirror widget) | CAMERA permission | ✅ Standard |
| Audio monitoring (sound diet) | Limited — can track media session, not total audio | ⚠️ Partial |
| Notification control | NotificationListenerService | ⚠️ Sensitive permission |
| Brightness manipulation | WRITE_SETTINGS | ✅ User grants |
| Device admin (anti-uninstall) | DeviceAdminReceiver | ✅ But Play Store scrutiny |

---

## Google Play Store Risks

### High Risk (may cause rejection)

1. **Accessibility Service misuse** — Google rejects apps using accessibility for non-accessibility purposes. Must justify clearly.
2. **Device Admin** — Extra review process. Must disclose prominently. Cannot use for "malicious" purposes.
3. **QUERY_ALL_PACKAGES** — Must declare why. Launcher use case is valid but reviewed.
4. **Overlay abuse** — Drawing over other apps. Must not mislead user.

### Mitigation Strategies

- **Publish on own website / F-Droid** as fallback if Play Store rejects
- **Split into modules**: Core launcher (Play Store safe) + "Extreme Mode" sideload APK
- **Open source** the project — builds trust, allows community auditing
- **Clear disclosure** during onboarding of every permission and why

---

## What's Technically IMPOSSIBLE (without root)

| Idea | Why Impossible | Workaround |
|---|---|---|
| Modify other apps' UI (slow scroll, pixelate) | Sandboxed. Can't touch other app's rendering. | Overlay on top showing visual effects |
| Clear other apps' data/cache | No cross-app data access | Can force-stop via Device Admin |
| Control other apps' notification sounds | NotificationListener can dismiss, not modify | Can override with own sound system |
| Truly prevent uninstall | User always has ADB/factory reset escape | Make it socially/financially painful |
| Read other apps' content (algorithm expose) | Sandboxed | Accessibility can read some visible text |
| Modify system clock display | Can't change status bar clock | Custom persistent notification or widget |
| Force 0.5x animation in other apps | System setting, not per-app | Global animation scale (affects everything) |
| Detect pupil dilation | Front camera resolution/ML not reliable enough | Skip this feature |

---

## What's Technically HARD But Possible

| Idea | Approach | Effort |
|---|---|---|
| Overlay "receipts" after app close | Accessibility detects app switch → show overlay | Medium |
| Context-aware locks | Geofence + time rules + calendar sync | Medium |
| Group sync (streaks, voting) | Backend server (Firebase/Supabase) + push notifications | High |
| AI pattern detection | Collect usage data locally → run simple ML or rule engine | High |
| Financial escrow | Third-party payment API integration (Stripe) | High |
| WearOS companion (heart rate, vibration) | Separate WearOS app with data sync | High |
| Breath verification | Mic input → analyze breathing pattern timing | Medium |
| NFC tag verification | Simple NFC read on specific tag ID | Low |

---

## Recommended Architecture

```
┌─────────────────────────────────────────────┐
│              FocusLauncher App               │
├─────────────────────────────────────────────┤
│  UI Layer (Jetpack Compose)                 │
│  ├── Home Screen (minimal: clock, task)     │
│  ├── App Drawer (hidden, intentional)       │
│  ├── Intercept Screens (lobby, tax, etc)    │
│  └── Settings & Group Management            │
├─────────────────────────────────────────────┤
│  Service Layer                              │
│  ├── UsageTracker (foreground monitoring)   │
│  ├── RuleEngine (when to block/intercept)   │
│  ├── ContextEngine (GPS, time, calendar)    │
│  ├── DeviceAdminReceiver                    │
│  └── NotificationManager                   │
├─────────────────────────────────────────────┤
│  Data Layer                                 │
│  ├── Local DB (Room) — usage stats, rules   │
│  ├── DataStore — preferences, streaks       │
│  └── Remote Sync — groups, leaderboards     │
├─────────────────────────────────────────────┤
│  Backend (Firebase/Supabase)                │
│  ├── Auth                                   │
│  ├── Group management & voting              │
│  ├── Streak sync                            │
│  ├── Push notifications (shame, sponsor)    │
│  └── Financial escrow (if applicable)       │
└─────────────────────────────────────────────┘
```

---

## MVP vs Full Vision

### MVP (4-6 weeks, solo dev)

- Custom launcher home screen (minimal UI)
- App usage tracking
- The Lobby (wait timer before apps)
- Basic time limits with progressive dimming overlay
- Session receipts
- Device Admin anti-uninstall
- Local-only (no backend needed)

### V1 (3-4 months)

- All of MVP +
- Rule engine (context-aware locks)
- Intent declaration
- Multiple restriction modes (cognitive tax, variable ratio)
- Identity voting / Builder-Consumer modes
- Basic group features (invite, shared streaks)
- Backend for group sync

### V2 (6+ months)

- AI pattern detection
- Financial stakes integration
- Group uninstall voting
- WearOS companion
- Community features (leaderboards, shame wall)
- Advanced accountability (sponsor, disappointment API)

---

## Key Technical Decisions Needed

1. **Kotlin + Jetpack Compose** vs **Flutter/KMP** (Android-only vs cross-platform later?)
2. **Firebase** vs **Supabase** vs **custom backend** for group features
3. **Play Store** vs **sideload-only** vs **both** (permission constraints)
4. **Open source** vs **closed source** (trust vs monetization)
5. **Accessibility Service** — use it (powerful but risky) or avoid it (safer but limited)?
