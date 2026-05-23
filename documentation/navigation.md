# Navigation map

How every feature is reached from the minimal home, with the `features-by-stage.json`
feature IDs each surface implements.

The minimal home + menu fan out to every shipped screen; every menu row is gated
by a feature flag (`documentation/feature-flags.md`). When a screen implements
multiple spec features, all relevant IDs are listed.

## Top-level surfaces

```
MinimalHomeScreen  (Screen.Home, CORE-001)
  ├── tap "↓ menu" hint        (testTag: dashboard-hint)     ─┐
  ├── tap "settings" dock      (testTag: dock-settings)      ─┤
  ├── tap inline placeholder   on empty "one-thing"          ─┼──>  Screen.Menu
  └── swipe-down anywhere on home                            ─┘
                                                                │
  dock items wired directly to system intents:                  │
  ├── tap "phone" dock         (testTag: dock-phone)   ──>  ACTION_DIAL
  └── tap "msgs" dock          (testTag: dock-msgs)    ──>  ACTION_VIEW sms:
```

## The menu fan-out

```
MinimalMenuScreen  (Screen.Menu — navigation surface, not a spec feature)
  │
  ├── ← back                   (menu-back)                ──>  Screen.Home
  │
  ├── stats                    (menu-stats)               ──>  Screen.Stats
  ├── dashboard                (menu-dashboard)           ──>  Screen.Dashboard (legacy)
  ├── transparency             (menu-transparency)        ──>  Screen.Transparency
  ├── vip contacts             (menu-vip)                 ──>  Screen.Vip
  ├── focus timer              (menu-focus)               ──>  Screen.Focus
  ├── mantra                   (menu-mantra)              ──>  Screen.Mantra
  ├── boredom                  (menu-boredom)             ──>  Screen.Boredom
  ├── breath unlock            (menu-breath)              ──>  Screen.Breath
  ├── future self              (menu-future-self)         ──>  Screen.FutureSelfVideo
  ├── replay onboarding        (menu-onboarding)          ──>  Screen.Onboarding
  ├── export data              (menu-export)              ──>  Screen.Export
  ├── identity                 (menu-identity)            ──>  Screen.Identity
  ├── graduate                 (menu-graduate)            ──>  Screen.Graduate
  ├── wrapped                  (menu-wrapped)             ──>  Screen.Wrapped
  ├── subscriptions            (menu-subscriptions)       ──>  Screen.Subscriptions
  ├── sleep ↔ output           (menu-sleep-correlator)    ──>  Screen.SleepCorrelator
  ├── mood                     (menu-mood)                ──>  Screen.Mood
  ├── park an idea             (menu-ideas)               ──>  Screen.Ideas
  ├── consumption              (menu-consumption)         ──>  Screen.Consumption
  ├── journal                  (menu-journal)             ──>  Screen.Journal
  ├── pr wall                  (menu-pr-wall)             ──>  Screen.PrWall
  ├── travel atlas             (menu-travel)              ──>  Screen.Travel
  ├── compounding              (menu-compound)            ──>  Screen.Compound
  ├── money                    (menu-money)               ──>  Screen.Money
  ├── anti-bio                 (menu-anti-bio)            ──>  Screen.AntiBio
  ├── sleep window             (menu-sleep-window)        ──>  Screen.SleepWindow
  ├── daily logs               (menu-daily-logs)          ──>  Screen.DailyLogs
  ├── highlights               (menu-highlights)          ──>  Screen.Highlights
  ├── tombstones               (menu-tombstones)          ──>  Screen.Tombstones
  ├── future letters           (menu-future-letters)      ──>  Screen.FutureLetters
  ├── week review              (menu-weekly-review)       ──>  Screen.WeeklyReview
  ├── promises                 (menu-promises)            ──>  Screen.Promises
  ├── legacy                   (menu-legacy)              ──>  Screen.Legacy
  ├── time dilation            (menu-dilation)            ──>  Screen.Dilation
  ├── sad self                 (menu-sad-self)            ──>  Screen.SadSelf
  ├── time = money             (menu-time-money)          ──>  Screen.TimeMoney
  ├── estimation               (menu-estimation)          ──>  Screen.Estimation
  ├── stress weather           (menu-stress)              ──>  Screen.Stress
  ├── anchor                   (menu-anchor)              ──>  Screen.Anchor
  ├── energy zones             (menu-energy-zones)        ──>  Screen.EnergyZones
  ├── track status             (menu-track-status)        ──>  Screen.TrackStatus
  ├── reciprocity              (menu-reciprocity)         ──>  Screen.Reciprocity
  ├── groups                   (menu-groups)              ──>  Screen.Groups          (SOCIAL-001 — stub backend)
  ├── backend status           (menu-backend)             ──>  Screen.Backend         (firebase + payment debug)
  ├── dual streak              (menu-dual-streak)         ──>  Screen.DualStreak      (SOCIAL-025)
  ├── disappointment           (menu-disappointment)      ──>  Screen.Disappointment  (SOCIAL-005)
  ├── builder profile          (menu-profile)             ──>  Screen.Profile         (SOCIAL-009 + SOCIAL-011)
  ├── feed                     (menu-feed)                ──>  Screen.Feed            (SOCIAL-016)
  ├── pre-commit               (menu-pre-commit)          ──>  Screen.PreCommit       (SOCIAL-033)
  ├── receipt wall             (menu-receipt-wall)        ──>  Screen.ReceiptWall     (SOCIAL-012)
  ├── feature flags            (menu-feature-flags)       ──>  Screen.FeatureFlags
  └── uninstall                (menu-uninstall)           ──>  Screen.Uninstall
```

## Per-screen feature IDs

For each shipped Compose screen, the spec features it covers (`features-by-stage.json`):

| Screen route | Feature IDs | Notes |
|---|---|---|
| `Screen.Home` | CORE-001, CORE-002, CORE-003, CORE-005, RESTRICT-011 (dream hour), PROD-007 (morning surface), PROD-012 (shutdown surface), PROD-013 (one thing) | Type-only minimal home; rotates by hour-of-day |
| `Screen.Menu` | — | Pure navigation surface |
| `Screen.Stats` | (composite read-only) GAMIFY-006 streaks, SLEEP-001 sleep, LIFE-009 beach days, LIFECYCLE-001 graduation, MIND-001 meditation, READ-001 reading | Sentence-only dashboard |
| `Screen.Dashboard` | (legacy ~50-card home; superset of everything implemented before the minimal pass) | Behind `LEGACY_DASHBOARD` flag |
| `Screen.Onboarding` | ONBOARD-001, ONBOARD-002 | |
| `Screen.Transparency` | ETHICS-001 | Per-technique runtime toggles |
| `Screen.Uninstall` | UNINSTALL-001, UNINSTALL-002, UNINSTALL-004, UNINSTALL-007 | 72hr cool-down + future-self video |
| `Screen.Vip` | CALLS-001, CALLS-005 | |
| `Screen.Focus` | PROD-005 | Pomodoro |
| `Screen.Mantra` | MANTRA-001, MANTRA-003 | Voice/type |
| `Screen.Boredom` | RESTRICT-005 | |
| `Screen.Breath` | HARDWARE-002 | 4·7·8 |
| `Screen.FutureSelfVideo` | PSYCH-006 | |
| `Screen.Export` | BACKUP-002 | json/csv share intent |
| `Screen.Identity` | PSYCH-001 | Builder vs Consumer voting |
| `Screen.Graduate` | LIFECYCLE-001 | Read-only progress |
| `Screen.Wrapped` | SOCIAL-010 | Year-in-review |
| `Screen.Subscriptions` | FIN-003 | Manual entry |
| `Screen.SleepCorrelator` | SLEEP-002 | Pearson r over commits + sleep |
| `Screen.Mood` | PROD-017 | Quick emoji + note |
| `Screen.Ideas` | PROD-009 | Idea parking lot |
| `Screen.Consumption` | SUB-001, SUB-003 | Caffeine + drinks, rolling 24h |
| `Screen.Journal` | PROD-004 | Plain-text entries |
| `Screen.PrWall` | FIT-003 | Manual PR entry |
| `Screen.Travel` | LOC-006 | Year + place |
| `Screen.Compound` | LIFE-010 | Compounding curve |
| `Screen.Money` | FIN-002, FIN-004 | Manual net worth + savings rate |
| `Screen.AntiBio` | SOCIAL-013 | 280-char declaration |
| `Screen.SleepWindow` | SLEEP-003 | Cutoff + wake picker |
| `Screen.DailyLogs` | MIND-001, READ-001, FIT-002, INTEG-008 | One-tap +mins/+commits |
| `Screen.Highlights` | READ-002 | Free-form quotes |
| `Screen.Tombstones` | PSYCH-014 | Killed apps |
| `Screen.FutureLetters` | PSYCH-015 | Scheduled letters |
| `Screen.WeeklyReview` | LIFE-003 | 7-day rollup |
| `Screen.Promises` | IDENT-002 | Kept/broken ratio |
| `Screen.Legacy` | PSYCH-003 | Cumulative builder minutes |
| `Screen.Dilation` | ABSURD-003 | Real min → 3× felt |
| `Screen.SadSelf` | SAD-001, SAD-002 | Voice picker + preview |
| `Screen.TimeMoney` | PSYCH-007, FIN-006 | Opportunity cost + lifetime hours |
| `Screen.Estimation` | PROD-010 | Effort estimation accuracy |
| `Screen.Stress` | MIND-002 | Sleep + unlocks weather |
| `Screen.Anchor` | PSYCH-013 | 12-min/day anchor |
| `Screen.EnergyZones` | PROD-014 | 6 windows × high/med/low |
| `Screen.TrackStatus` | TRACK-001, UNINSTALL-007 | Level, points, misses |
| `Screen.Reciprocity` | PRM-003 | Contacts log + outbound% |
| `Screen.Groups` | SOCIAL-001 | Create / join / leave; stub BackendRepository until Firebase wired |
| `Screen.Backend` | PAY-003 (router preview), FIREBASE_BACKEND meta | Read-only debug: firebase init state, uid, payment channel, cached config, "test checkout" trigger |
| `Screen.DualStreak` | SOCIAL-025 | Pair-uid input + shared 🔥 counter + per-side done-today indicators |
| `Screen.Disappointment` | SOCIAL-005 | Form: to + worstStat; server-side weekly rate-limit (when Firebase is wired) |
| `Screen.Profile` | SOCIAL-009, SOCIAL-011 | Read-only profile preview + verification eligibility check (days + projects + vouches) |
| `Screen.Feed` | SOCIAL-016 | Chronological feed; backed by `BackendRepository.feed` |
| `Screen.PreCommit` | SOCIAL-033 | Post tomorrow's intent as a PRE_COMMIT post that expires end-of-tomorrow |
| `Screen.ReceiptWall` | SOCIAL-012 | Reads from `ReceiptWall.receiptFor(app, sec)` |
| `Screen.FeatureFlags` | — | Build-time gating surface (not in spec) |
| `(no UI yet)` PaymentRouter | PAY-001, PAY-002, PAY-003 | Triggered when a paywall surface calls `PaymentRouter.checkout(productId)` — opens Play Billing or web checkout per `/config/payments.nativeOnDevice` |
| `(no UI yet)` Web companion | PLATFORM-002 | Static site on Firebase Hosting — separate repo target, not a route in the Android app |

## Lobby surface (intercept overlay)

```
LobbyAccessibilityService  ──(intent-launches)──>  LobbyActivity
                                                      │
                                                      └── RESTRICT-001 wait timer
                                                          RESTRICT-002 cognitive tax
                                                          RESTRICT-003 dimming overlay (via DimmingOverlay)
                                                          RESTRICT-006 intent declaration
                                                          PSYCH-008 intervention screen (high-unlock branch)
                                                          PSYCH-010 last day test (planned)
```

The Lobby isn't reachable from the menu — it's interception-driven. Toggling
`LOBBY_INTERCEPT` off in the feature-flags screen disables the accessibility
service registration on next start.

## Feature flags

Every menu row is gated by a `FeatureFlags.*` constant — see
`documentation/feature-flags.md`. Flipping a flag to `false` hides its row
automatically, which is why the menu test asserts the *currently-enabled* row
set rather than a hard-coded count.

## Files

| File | Role |
|---|---|
| `app/src/main/java/com/erluxman/focuslauncher/MainActivity.kt` | `Screen` sealed class + route table |
| `app/src/main/assets/featureflags.json` | Source of truth for flag defaults + metadata |
| `app/src/main/java/com/erluxman/focuslauncher/config/FeatureFlag.kt` | Flag data class + `FlagKey` constants |
| `app/src/main/java/com/erluxman/focuslauncher/config/FeatureFlagsRepository.kt` | Loads JSON, manages DataStore overrides, exposes effective `Flow<Map>` |
| `app/src/main/java/com/erluxman/focuslauncher/ui/home/minimal/MinimalHomeScreen.kt` | The minimal home (4 hour-of-day surfaces) |
| `app/src/main/java/com/erluxman/focuslauncher/ui/home/minimal/MinimalMenuScreen.kt` | The single menu — fan-out to every feature |
| `app/src/main/java/com/erluxman/focuslauncher/ui/home/minimal/MinimalStatsScreen.kt` | Sentence-only read-only dashboard |
| `app/src/main/java/com/erluxman/focuslauncher/ui/flags/FeatureFlagsScreen.kt` | In-app toggle UI |
| `app/src/androidTest/java/com/erluxman/focuslauncher/MinimalMenuTest.kt` | Flow test: every feature reachable from home |
| `app/src/androidTest/java/com/erluxman/focuslauncher/MinimalHomeTest.kt` | Home surface assertions |
| `app/src/androidTest/java/com/erluxman/focuslauncher/MinimalStatsTest.kt` | Stats screen assertions |
| `app/src/androidTest/java/com/erluxman/focuslauncher/FeatureFlagsScreenTest.kt` | Flag toggle assertions |

## Test coverage summary

`MinimalMenuTest` (5 tests):

1. `home_dashboardHintTap_opensMenu` — tap ↓ hint reaches `minimal-menu`.
2. `home_settingsDockTap_opensMenu` — tap settings dock reaches `minimal-menu`.
3. `menu_exposesEveryFeatureRow` — scrolls + asserts every default-on row visible (44 at last update).
4. `menu_tappingTransparency_navigatesToTransparencyScreen` — menu → screen route works.
5. `menu_backReturnsToHome` — back button returns to `minimal-home`.

`MinimalHomeTest` (3 tests):

1. `minimalHome_isVisibleByDefault`
2. `minimalHome_showsExactlyOneSection`
3. `dockSettings_visibleOnHome`

`MinimalStatsTest` (1 test):

1. `statsScreen_rendersFooterAndReturnLink` — stats surface renders return + 3 footer links.

`FeatureFlagsScreenTest` (2 tests):

1. `screen_rendersAndShowsRequiredAndOptionalFlags` — required flag locked-on, default-on/off observable.
2. `toggling_writesOverride_andSurvivesRecompose` — toggling a switch persists to DataStore.

## How to find which feature ID a route covers

```bash
grep -A1 "Screen.YourRoute" documentation/navigation.md
```

When you add a new screen, add a row to the **Per-screen feature IDs** table
above and bump the menu-row list. If your screen covers multiple spec IDs,
list all of them — that's the source of truth for "what's shipped".
