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
  ├── tap "dashboard"        (testTag: menu-dashboard)    ──>  Screen.Dashboard      (legacy 50-card home)
  ├── tap "transparency"     (testTag: menu-transparency) ──>  Screen.Transparency
  ├── tap "vip contacts"     (testTag: menu-vip)          ──>  Screen.Vip
  ├── tap "focus timer"      (testTag: menu-focus)        ──>  Screen.Focus
  ├── tap "mantra"           (testTag: menu-mantra)       ──>  Screen.Mantra
  ├── tap "boredom"          (testTag: menu-boredom)      ──>  Screen.Boredom
  ├── tap "breath unlock"    (testTag: menu-breath)       ──>  Screen.Breath
  ├── tap "future self"      (testTag: menu-future-self)  ──>  Screen.FutureSelfVideo
  ├── tap "replay onboarding" (testTag: menu-onboarding)  ──>  Screen.Onboarding
  └── tap "uninstall"        (testTag: menu-uninstall)    ──>  Screen.Uninstall
```

## Why a menu and not a swipe-down stats sheet

The design spec calls for a stats sheet behind swipe-down. That sheet doesn't
exist yet, and even when it lands it's read-only. The launcher still needs
a *navigational* fan-out — entries to Focus, Mantra, VIP, etc. — that the
stats sheet was never going to provide. The menu fills that role today:
text-only, one tappable line per feature, no cards, same aesthetic as home.

When the stats sheet ships, it can either become a section *inside* the menu
or take over swipe-down with the menu moved to the settings dock tap. Both
options leave the rest of this wiring intact.

## Files

| File | Role |
|---|---|
| `app/src/main/java/com/erluxman/focuslauncher/MainActivity.kt` | `Screen` sealed class + route table |
| `app/src/main/java/com/erluxman/focuslauncher/ui/home/minimal/MinimalHomeScreen.kt` | The minimal home (4 hour-of-day surfaces) |
| `app/src/main/java/com/erluxman/focuslauncher/ui/home/minimal/MinimalMenuScreen.kt` | The single menu — fan-out to every feature |
| `app/src/androidTest/java/com/erluxman/focuslauncher/MinimalMenuTest.kt` | Flow test: every feature reachable from home |
| `app/src/androidTest/java/com/erluxman/focuslauncher/MinimalHomeTest.kt` | Home surface assertions |

## Test coverage

`MinimalMenuTest` (5 tests):

1. `home_dashboardHintTap_opensMenu` — tap ↓ hint reaches `minimal-menu`.
2. `home_settingsDockTap_opensMenu` — tap settings dock reaches `minimal-menu`.
3. `menu_exposesEveryFeatureRow` — scrolls + asserts all 10 feature rows visible.
4. `menu_tappingTransparency_navigatesToTransparencyScreen` — menu → screen route works.
5. `menu_backReturnsToHome` — back button returns to `minimal-home`.

`MinimalHomeTest` (3 tests) — unchanged, still passing:

1. `minimalHome_isVisibleByDefault`
2. `minimalHome_showsExactlyOneSection`
3. `dockSettings_visibleOnHome`

All 8 tests pass on `Resizable_Experimental(AVD) - 16`.
