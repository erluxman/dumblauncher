# Design — minimal pass

> Successor to `design-baseline.md`. Opinionated synthesis: combines
> *type-only*, *time-of-day*, and *two-surface*. No cards, no shadows,
> no icons. The launcher hides almost all of its surface behind one
> gesture and rotates what it shows by hour of day.

---

## 1. Core principles

1. **No cards.** No surfaces, no rounded corners, no fills, no shadows. Just text on background.
2. **One accent.** Monochrome content + one warm accent reserved for the single most important thing on screen.
3. **Sentences, not metrics.** "12 days of work" not `STREAK 12 / BEST 47`.
4. **One job per hour.** Morning shows the morning routine. Work shows the focus stats. Evening shows the shutdown ritual. Night shows the breathing circle. Never all at once.
5. **One gesture for the dashboard.** Everything quantitative lives behind a swipe-down. Home never feels like a dashboard.
6. **Dock pinned, search invisible until typed.** The launcher function survives. Nothing else visually competes.

---

## 2. Type + colour

| Token | Value |
|---|---|
| Background | `#0B0B0E` (near-black) |
| Foreground | `#EEEEEE` (off-white) |
| Outline | `#6E6E78` (muted gray, only for secondary lines) |
| Accent | `#E5A95C` (warm amber, reserved for the single highlighted line) |
| Font | DM Sans or system sans-serif |
| Display | 32sp, weight 400, line-height 1.4 |
| Body | 20sp, weight 400, line-height 1.6 |
| Caption | 14sp, weight 400, outline color, only at footer |
| Letter-spacing | 0 (no all-caps tracking) |

---

## 3. Layout

- 32dp horizontal page padding (up from 24dp).
- Content vertically *centered* with the dock anchored at the bottom.
- One section visible at a time. Sections never stack.
- Spacing inside a section: 24dp between paragraphs.
- Spacing from section to dock: at least 64dp.

---

## 4. Hour-of-day rotation

| Hours | Section | Why |
|---|---|---|
| 05–09 | **Morning routine** | The day starts as a checklist of identity actions, not as stats. |
| 09–17 | **Work focus** | Today's one thing + how today is going so far. No history. |
| 17–22 | **Shutdown ritual** | What you shipped + tomorrow's one thing. Closes the loop. |
| 22–05 | **Dream Mode** | Already exists. Single breathing circle. Apps disabled. |

These are the existing `MORNING_ROUTINE`, `SHUTDOWN_RITUAL`, `DREAM` features — just promoted from "one card among 50" to "the entire surface for that hour".

---

## 5. Wireframes — the four hour-of-day surfaces

### 5.1 Morning (05–09)

```
                                                              · 06:42
┌──────────────────────────────────────────────────────────────────┐
│                                                                  │
│                                                                  │
│   good morning, risky.                                           │
│                                                                  │
│                                                                  │
│   today's one thing —                                            │
│   ship the design refactor.            ← accent line             │
│                                                                  │
│                                                                  │
│   bed     water     read     walk                                │
│   ●       ●         ○        ○                                   │
│                                                                  │
│                                                                  │
│                                                                  │
│                                                                  │
│                                                                  │
│                                                                  │
├──────────────────────────────────────────────────────────────────┤
│   📞     💬     ⚙                                              │
└──────────────────────────────────────────────────────────────────┘
                          (swipe ↓ for stats)
```

### 5.2 Work (09–17)

```
                                                              · 14:08
┌──────────────────────────────────────────────────────────────────┐
│                                                                  │
│                                                                  │
│   day 12 of work.                                                │
│                                                                  │
│                                                                  │
│   today —                                                        │
│   47 minutes focused, 12 in distraction.                         │
│                                                                  │
│                                                                  │
│   one thing —                                                    │
│   ship the design refactor.            ← accent line             │
│                                                                  │
│                                                                  │
│                                                                  │
│                                                                  │
│                                                                  │
│                                                                  │
├──────────────────────────────────────────────────────────────────┤
│   📞     💬     ⚙                                              │
└──────────────────────────────────────────────────────────────────┘
                          (swipe ↓ for stats)
```

### 5.3 Evening (17–22)

```
                                                              · 19:30
┌──────────────────────────────────────────────────────────────────┐
│                                                                  │
│                                                                  │
│   shutdown.                                                      │
│                                                                  │
│                                                                  │
│   what did you ship today?                                       │
│   ┌────────────────────────────────────────────────────────┐    │
│   │  design refactor — slices 1-6                          │    │
│   └────────────────────────────────────────────────────────┘    │
│                                                                  │
│                                                                  │
│   tomorrow's one thing?                                          │
│   ┌────────────────────────────────────────────────────────┐    │
│   │                                                        │    │
│   └────────────────────────────────────────────────────────┘    │
│                                                                  │
│                                                                  │
├──────────────────────────────────────────────────────────────────┤
│   📞     💬     ⚙                                              │
└──────────────────────────────────────────────────────────────────┘
                          (swipe ↓ for stats)
```

### 5.4 Dream (22–05)

```
┌──────────────────────────────────────────────────────────────────┐
│                                                                  │
│                                                                  │
│                                                                  │
│                                                                  │
│                            ●                                     │
│                                                                  │
│                                                                  │
│                                                                  │
│                                                                  │
│   inhale 4.  hold 7.  exhale 8.                                  │
│                                                                  │
│                                                                  │
│                                                                  │
│                                                                  │
│   tomorrow —                                                     │
│   ship the design refactor.                                      │
│                                                                  │
│                                                                  │
│             (dock hidden in dream mode)                          │
└──────────────────────────────────────────────────────────────────┘
```

---

## 6. The dashboard sheet (swipe ↓ only)

```
                                                              · 14:12
┌──────────────────────────────────────────────────────────────────┐
│   today                                                          │
│                                                                  │
│   12 days of work.                                               │
│   6 h 24 m asleep last night.                                    │
│   recovery: ready.                                               │
│   3 commits.                                                     │
│   10 minutes of meditation.                                      │
│                                                                  │
│                                                                  │
│   this week                                                      │
│                                                                  │
│   78 minutes meditation across 4 days.                           │
│   245 minutes reading across 5 days.                             │
│   2 days at the gym.                                             │
│                                                                  │
│                                                                  │
│   this year                                                      │
│                                                                  │
│   482 days to graduate.                                          │
│   47 beach saturdays left in your life.                          │
│                                                                  │
│                                                                  │
│   transparency · uninstall                                       │
└──────────────────────────────────────────────────────────────────┘
                            (swipe ↑ to return)
```

Sentences only. No numbers without a unit. No cards. The same data, re-typeset.

---

## 7. Banners (the only override surfaces)

These still need to interrupt — but as a single line at the top, not a card.

```
                                                              · 14:08
   ⚠  soften mode — your patterns suggest a pause.
─────────────────────────────────────────────────────────────────────
   day 12 of work.
   ...
```

```
                                                              · 14:08
   🎉  thirty-day streak.                                     ×
─────────────────────────────────────────────────────────────────────
   day 12 of work.
   ...
```

```
                                                              · 14:08
   unlock #14. pause.                                         ×
─────────────────────────────────────────────────────────────────────
   day 12 of work.
   ...
```

A single line at the top with an optional `×`. No surface around it.

---

## 8. Search & dock

- **Dock (always visible except dream):** 5 single-character glyphs at the very bottom — `📞 💬 ⚙ [app1] [app2] [app3]`. Hairline above the dock as the only divider on screen.
- **Search:** invisible until the user starts typing — a hidden `TextField` that captures keystrokes. After the first character, a single line of results appears just above the dock. No search bar shown otherwise.

Pull-to-reveal: a small `↓` glyph at the top center (low-contrast) is the only hint of the dashboard. Swipe-down opens it; swipe-up closes.

---

## 9. Mapping every old surface to a new home

Where each baseline section goes in the minimal version.

| Baseline section | Minimal home |
|---|---|
| WhyHereCard | Dashboard sheet, footer line only |
| OneThingCard | Promoted to the **accent line** on Morning + Work |
| StreakRow | "day 12 of work." on Work surface |
| DomainStreaksRow | Dashboard sheet, "this week" group |
| TrackCard / DomainTracksCard | Dashboard sheet, sentence form |
| CalendarStrip | Work surface, sentence: "next: 11-12 standup" |
| HealthCard / RecoveryCard / StressCard | Dashboard sheet, "today" group |
| MirrorWidget | **Dropped** from home; lives in Onboarding only |
| InsightsCard / TimeMoneyCard | Dashboard sheet, "this year" group |
| SubscriptionsCard / MoneyMirrorCard | Removed from home entirely; a separate "money" screen reachable from dashboard |
| MortalityCard / DeathbedCard | Dashboard sheet, opt-in line "47 beach saturdays left" |
| CaffeineCard / HangoverCard | Removed from home; a "log" screen reachable from dashboard |
| MeditationCard / ReadingCard / WorkoutCard / CommitCard | Same — log screen |
| CompoundCurveCard / WeeklyReviewCard / EstimationCard | Dashboard sheet only |
| PatternCard | Dashboard sheet only |
| IdeaParkingCard / HighlightCard | Removed from home; "notes" screen |
| TimeDilationCard / AnchorCard | Dashboard sheet only |
| SleepWindowCard / AfterFallCard / TimeDebtCard / MoodPingCard | Conditional banner OR dashboard sheet |
| MorningRoutineCard | **Promoted to Morning surface** |
| ShutdownRitualCard | **Promoted to Evening surface** |
| IdentityVoteCard | Dropped from home; reachable from dashboard |
| HourlyHeatmapCard / BaselineProposalCard | Dashboard sheet only |
| ProjectSection / TodoSection | Dashboard sheet, "today" group lists open todos |
| TombstoneSection / NourishingSection | Dropped from home; settings only |
| WidgetSection | **Dropped entirely.** Clock is the timestamp at top. |
| App dock | Survives, more muted |
| Hidden drawer | Survives, invisible until typing |

---

## 10. What changes in the code

We *do not* remove any of the underlying data, math, prefs, or tests. The
minimal pass is purely a **render swap**:

- New file: `ui/home/minimal/MinimalHomeScreen.kt` — the four hour-swap surfaces.
- New file: `ui/home/minimal/StatsSheet.kt` — the dashboard sheet.
- The existing `HomeScreen.kt` is **kept** as `DashboardScreen` (no longer
  the default) so anyone who wants the old dashboard can still reach it
  via the swipe-down gesture or a transparency toggle. All 50 cards keep
  working.
- `MainActivity.kt` routes the default `Screen.Home` to `MinimalHomeScreen`.

Tests don't move; the new file gets its own small sweep.

---

## 11. What we leave for after this lands

- A typography pass — pick a real custom font, not the system one.
- A live theme — `0B0B0E` background today, but consider a per-hour tint
  (cooler at night, warmer at sunset).
- An actual swipe-down gesture (V2 implements the dashboard as a
  bottom-route navigation; the gesture wiring is a follow-up).
- Removing dead `View` widgets from the manifest (WidgetSection etc.).

We pick those up after the minimal home is real.
