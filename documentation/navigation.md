# Navigation map

How every feature is reached from the minimal home. The home stays clean —
two surfaces (home, menu) are responsible for fanning out to all 11 screens.

## Surface tree

```
MinimalHomeScreen  (Screen.Home)
  │
  ├── tap "↓ menu" hint      (testTag: dashboard-hint)   ─┐
  ├── tap "settings" dock    (testTag: dock-settings)    ─┼──>  MinimalMenuScreen  (Screen.Menu)
  ├── tap inline "(tap to set →)" placeholder            ─┤
  └── swipe-down anywhere on home                        ─┘
                                                              │
  dock items wired directly to system intents:                │
  ├── tap "phone" dock       (testTag: dock-phone)   ──>  ACTION_DIAL
  └── tap "msgs" dock        (testTag: dock-msgs)    ──>  ACTION_VIEW sms:

MinimalMenuScreen  (Screen.Menu)
  │
  ├── tap "← back"           (testTag: menu-back)         ──>  Screen.Home
  ├── tap "stats"            (testTag: menu-stats)        ──>  Screen.Stats          (sentence-only read-only dashboard)
  ├── tap "dashboard"        (testTag: menu-dashboard)    ──>  Screen.Dashboard      (legacy 50-card home)
  ├── tap "transparency"     (testTag: menu-transparency) ──>  Screen.Transparency
  ├── tap "vip contacts"     (testTag: menu-vip)          ──>  Screen.Vip
  ├── tap "focus timer"      (testTag: menu-focus)        ──>  Screen.Focus
  ├── tap "mantra"           (testTag: menu-mantra)       ──>  Screen.Mantra
  ├── tap "boredom"          (testTag: menu-boredom)      ──>  Screen.Boredom
  ├── tap "breath unlock"    (testTag: menu-breath)       ──>  Screen.Breath
  ├── tap "future self"      (testTag: menu-future-self)  ──>  Screen.FutureSelfVideo
  ├── tap "replay onboarding" (testTag: menu-onboarding)  ──>  Screen.Onboarding
  ├── tap "feature flags"    (testTag: menu-feature-flags)──>  Screen.FeatureFlags
  └── tap "uninstall"        (testTag: menu-uninstall)    ──>  Screen.Uninstall

FeatureFlagsScreen  (Screen.FeatureFlags)
  │
  ├── tap "← back"           (testTag: flags-back)         ──>  Screen.Menu
  ├── toggle Switch          (testTag: flag-switch-&lt;KEY&gt;)  ──>  writes DataStore override
  └── tap "reset all overrides" (testTag: flags-reset-all) ──>  clears all overrides

MinimalStatsScreen  (Screen.Stats)
  │
  ├── tap "return"           (testTag: stats-return)       ──>  Screen.Home
  ├── swipe-up anywhere on stats                           ──>  Screen.Home
  ├── tap "transparency"     (testTag: stats-transparency) ──>  Screen.Transparency
  ├── tap "uninstall"        (testTag: stats-uninstall)    ──>  Screen.Uninstall
  └── tap "dashboard"        (testTag: stats-dashboard)    ──>  Screen.Dashboard
```

## Why a menu and not a swipe-down stats sheet

The design spec calls for a stats sheet behind swipe-down. The launcher
still needs a *navigational* fan-out — entries to Focus, Mantra, VIP, etc. —
that the read-only stats sheet was never going to provide. The menu fills
that role today: text-only, one tappable line per feature, no cards, same
aesthetic as home.

The stats sheet (`MinimalStatsScreen`, `Screen.Stats`) ships as a peer
surface reached from the menu's first row. Reaching it via the menu rather
than the swipe-down keeps every other test in place and gives the dock and
swipe-down a single, predictable destination (menu = "everything").

## Files

Every menu row is gated by a `FeatureFlags.*` constant — see
`documentation/feature-flags.md`. Flipping a flag to `false` hides its row
automatically, which is why the test asserts the *currently-enabled* row
set rather than a hard-coded count.

| File | Role |
|---|---|
| `app/src/main/java/com/erluxman/focuslauncher/MainActivity.kt` | `Screen` sealed class + route table |
| `app/src/main/assets/featureflags.json` | Source of truth for flag defaults + metadata |
| `app/src/main/java/com/erluxman/focuslauncher/config/FeatureFlag.kt` | Flag data class + `FlagKey` constants |
| `app/src/main/java/com/erluxman/focuslauncher/config/FeatureFlagsRepository.kt` | Loads JSON, manages DataStore overrides, exposes effective `Flow<Map>` |
| `app/src/main/java/com/erluxman/focuslauncher/ui/flags/FeatureFlagsScreen.kt` | In-app toggle UI |
| `app/src/main/java/com/erluxman/focuslauncher/ui/home/minimal/MinimalHomeScreen.kt` | The minimal home (4 hour-of-day surfaces) |
| `app/src/main/java/com/erluxman/focuslauncher/ui/home/minimal/MinimalMenuScreen.kt` | The single menu — fan-out to every feature |
| `app/src/main/java/com/erluxman/focuslauncher/ui/home/minimal/MinimalStatsScreen.kt` | Sentence-only read-only dashboard (today / this week / this year) |
| `app/src/androidTest/java/com/erluxman/focuslauncher/MinimalMenuTest.kt` | Flow test: every feature reachable from home |
| `app/src/androidTest/java/com/erluxman/focuslauncher/MinimalHomeTest.kt` | Home surface assertions |
| `app/src/androidTest/java/com/erluxman/focuslauncher/MinimalStatsTest.kt` | Stats screen assertions |

## Test coverage

`MinimalMenuTest` (5 tests):

1. `home_dashboardHintTap_opensMenu` — tap ↓ hint reaches `minimal-menu`.
2. `home_settingsDockTap_opensMenu` — tap settings dock reaches `minimal-menu`.
3. `menu_exposesEveryFeatureRow` — scrolls + asserts all 11 feature rows visible (now includes `menu-stats`).
4. `menu_tappingTransparency_navigatesToTransparencyScreen` — menu → screen route works.
5. `menu_backReturnsToHome` — back button returns to `minimal-home`.

`MinimalHomeTest` (3 tests):

1. `minimalHome_isVisibleByDefault`
2. `minimalHome_showsExactlyOneSection`
3. `dockSettings_visibleOnHome`

`MinimalStatsTest` (1 test):

1. `statsScreen_rendersFooterAndReturnLink` — stats surface renders return + 3 footer links.

`FeatureFlagsScreenTest` (2 tests):

1. `screen_rendersAndShowsRequiredAndOptionalFlags` — required flag locked-on, Stage 1 default-on, Stage 3 default-off.
2. `toggling_writesOverride_andSurvivesRecompose` — toggling a switch persists to DataStore.
