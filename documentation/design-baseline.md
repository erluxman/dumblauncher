# Design baseline — what the app looks like today

> Snapshot before the "make it minimal" pass. Every section that currently
> exists is in this doc, even if it only shows up under a specific
> condition. The minimal redesign will be drafted against this.

---

## 0. Map of all surfaces

```
                          ┌─────────────────┐
                          │   Onboarding    │  ← first launch only
                          └────────┬────────┘
                                   ▼
        ┌────────────────────────────────────────────────────┐
        │                       Home                         │
        │  ── default surface, ~50 stacked cards ──          │
        └────┬──────┬─────────┬──────────┬──────────┬────────┘
             │      │         │          │          │
             ▼      ▼         ▼          ▼          ▼
        ┌────────┐┌────┐┌────────────┐┌──────┐┌───────────┐
        │ Lobby  ││ VIP││ Transparency││Mantra││ Boredom    │
        └────────┘└────┘└────────────┘└──────┘└───────────┘
             │
             ▼
        ┌──────────────────┐    ┌───────────────────┐
        │ Future-Self Vid  │    │ Uninstall (72hr)  │
        └──────────────────┘    └───────────────────┘
             │
             ▼
        ┌──────────────────┐    ┌───────────────────┐
        │  Breath Unlock   │    │  Focus Timer      │
        └──────────────────┘    └───────────────────┘

Plus a "Dream Mode" that replaces Home between cutoff and wake hours.
```

---

## 1. Home — the firehose

Vertical LazyColumn, scrolls forever. Every card is opt-out-able via the
Transparency page; many auto-hide when their data is empty.

```
┌─ status bar ──────────────────────────────────────────┐
│                                                       │
│  [ THRIVING ]     [ 47 pts ]   [ 4 passes ]    [⚙]   │  ← header row
│                                                       │
├───────────────────────────────────────────────────────┤
│  ⚠  SOFTEN MODE                                      │  (crisis-only)
│     low motion + late scroll + flat mood detected.    │
├───────────────────────────────────────────────────────┤
│  This is unlock #14 today. Pause.            [×]      │  intervention banner
├───────────────────────────────────────────────────────┤
│  🎉 You hit a 30-day streak.                  [×]    │  celebration banner
├───────────────────────────────────────────────────────┤
│  WHY YOU'RE HERE                                      │
│  "I want to be the person who builds, not the         │
│   one who scrolls. — declared 2026-01-04"             │
├───────────────────────────────────────────────────────┤
│  📨 A letter from past-you is due today.    [open]    │  future-letter banner
├───────────────────────────────────────────────────────┤
│  ONE THING                                            │
│  ┌─────────────────────────────────────────────────┐  │
│  │ Ship the design refactor branch.                │  │
│  └─────────────────────────────────────────────────┘  │
├───────────────────────────────────────────────────────┤
│  DOMAIN STREAKS                                       │
│  ⚙ Work 12d   🏃 Fit 4d   📚 Read 19d   🧘 Med 7d     │
├───────────────────────────────────────────────────────┤
│  TRACK   Level 3 — Deep Work                          │
│  ▓▓▓▓░░░░░ 41% to next promotion                      │
├───────────────────────────────────────────────────────┤
│  PER-DOMAIN TRACKS                                    │
│  Deep Work L3 · Sleep L5 · Fitness L2 · Learning L4   │
├───────────────────────────────────────────────────────┤
│  📅 TODAY'S CALENDAR  (or FOCUS BLOCK ACTIVE)         │
│   9-11   Deep work                                    │
│  11-12   Standup                                      │
│   1- 3   Code review                                  │
├───────────────────────────────────────────────────────┤
│  HEALTH         RECOVERY        STRESS WEATHER        │
│  👟 6,400       READY 78        CLEAR 24              │
│  💤 7h 12m      sleep 84        unlocks 24            │
│                                                       │
├───────────────────────────────────────────────────────┤
│  ┌─────────────────────────────┐                      │
│  │      MIRROR (live cam)      │                      │
│  │  [your face, 180dp tall]    │                      │
│  └─────────────────────────────┘                      │
├───────────────────────────────────────────────────────┤
│  TRACK RECALIBRATED — dropped to L2. Be kind.   [×]   │  conditional
├───────────────────────────────────────────────────────┤
│  GRADUATE TRACK                                       │
│  482d to graduate. Stay at L10.                       │
├───────────────────────────────────────────────────────┤
│  Sad voice:  ( ) gentle  (●) stern  ( ) witty  ( ) drill│
├───────────────────────────────────────────────────────┤
│  ENERGY ZONES                                         │
│  06-09 ●HIGH   09-12 ●HIGH   12-15 ○MED   15-18 ●LOW  │
├───────────────────────────────────────────────────────┤
│  🔥 streak 12d · best 47   |   ⏱ 3 sessions · 87m banked│
├───────────────────────────────────────────────────────┤
│  EARNED PIXELS   42% colour earned                    │
│  ▓▓▓▓░░░░░░                                           │
├───────────────────────────────────────────────────────┤
│  GRACE   2 freezes   |   declare tomorrow as grace?   │
├───────────────────────────────────────────────────────┤
│  INSIGHTS                                             │
│  • Cost today        $12.50      (45m × $25/hr)       │
│  • If you keep this  3 life-years on screen by 80     │
│  • I/O ratio         1.30        creation ÷ scroll    │
│  • Bank in 1 year    1d 14h      compounded           │
│  [ rate ] [ age ]  [Save]                             │
├───────────────────────────────────────────────────────┤
│  TIME COST                                            │
│  $ [        ]   at $25/HR   →   3h 12m                │
├───────────────────────────────────────────────────────┤
│  SUBSCRIPTIONS                  $44.97 / mo           │
│  Spotify   $11   Netflix $16   iCloud $3   …          │
├───────────────────────────────────────────────────────┤
│  MONEY MIRROR        SAVINGS  25%      NET  $42,000   │
│  [income/mo] [exp/mo] [assets] [liabilities]          │
├───────────────────────────────────────────────────────┤
│  MORTALITY  (opt-in)                                  │
│  BEACH SATURDAYS  47           NEXT SUMMER  13        │
│  DAYS REMAINING  14,610                               │
├───────────────────────────────────────────────────────┤
│  DEATHBED  (opt-in)                                   │
│  At today's rate, by 80:  3.4 waking years on screen  │
├───────────────────────────────────────────────────────┤
│  CAFFEINE                                             │
│  IN SYSTEM 142 mg     AT MIDNIGHT  28 mg              │
│  ╲___ sparkline                                       │
│  [Espresso +65mg] [Drip +95mg] [Cold brew +200mg] …   │
├───────────────────────────────────────────────────────┤
│  HANGOVER                                             │
│  BAC 0.020 %    HOURS TO SOBER  1.3h                  │
│  [Beer +1.0u] [Wine +1.0u] [Cocktail +1.0u] [Double]  │
├───────────────────────────────────────────────────────┤
│  MEDITATION   TODAY 10m · LAST 7D 78m · 🔥4d          │
│  Tech: [Breath] [Body scan] [Loving] [Open] [Walk]    │
│  Log: [+5] [+10] [+15] [+20] [+30]                    │
├───────────────────────────────────────────────────────┤
│  READING      TODAY 30m · LAST 7D 4h · 0.6 books      │
│  Log: [+10] [+20] [+30] [+45] [+60]                   │
├───────────────────────────────────────────────────────┤
│  WORKOUT      TODAY 0m · LAST 7D 90m · 🔥2d           │
│  Kind: [Strength] [Run] [Walk] [Cycle] [Yoga] [Climb] │
│  Log: [+15] [+30] [+45] [+60] [+90]                   │
├───────────────────────────────────────────────────────┤
│  OUTPUT       COMMITS TODAY 3   LAST 7D 24            │
│  Log: [+1] [+3] [+5]                                  │
├───────────────────────────────────────────────────────┤
│  PR WALL                                              │
│  Lift:[Squat] Value:[140] kg [+]                      │
│  Squat 140 kg · 2026-04-22                            │
│  Bench 90 kg · 2026-03-11                             │
├───────────────────────────────────────────────────────┤
│  TRAVEL ATLAS                                         │
│  PLACES 14    YEARS 6                                 │
│  2026  Lisbon    2025  Tokyo    2024  Berlin    …     │
├───────────────────────────────────────────────────────┤
│  COMPOUND                                             │
│  BASELINE × 0.5%/DAY → 3y                             │
│  6.2x          ╱╱  (Canvas curve)                     │
│  Rate:  [0.1%] [0.5%] [1%]                            │
│  Horizon: [1y] [3y] [5y]                              │
├───────────────────────────────────────────────────────┤
│  WEEK IN REVIEW                                       │
│  MEDITATION  78m · 4d                                 │
│  READING     245m · 5d                                │
│  WORKOUT     90m · 2d                                 │
├───────────────────────────────────────────────────────┤
│  ESTIMATION                                           │
│  ACCURACY 67%   BIAS -12%   median 0.85× est/actual   │
├───────────────────────────────────────────────────────┤
│  PATTERN     YOUR WEAKEST HOUR TODAY  2 PM            │
│              MINUTES  41                              │
├───────────────────────────────────────────────────────┤
│  PARKED      ┌────────────────────────┐ [+]          │
│              │ park an idea…          │              │
│              └────────────────────────┘              │
│  • Write a post about Sunday-review   [✓] [✗]        │
│  • Refactor InsightsCard math          [✓] [✗]        │
├───────────────────────────────────────────────────────┤
│  TODAY'S HIGHLIGHT                                    │
│  "We are what we repeatedly do…" — Aristotle          │
│  Add: [_____________________________________] [+]    │
├───────────────────────────────────────────────────────┤
│  TIME DILATION                                        │
│  DISTRACTION (REAL) 45m    FELT LIKE (3×)  2h 15m     │
├───────────────────────────────────────────────────────┤
│  ANCHOR     MOST DISCIPLINED 12m   YOU 45m (3.8x)     │
├───────────────────────────────────────────────────────┤
│  SLEEP WINDOW                                         │
│  10 PM → 5 AM           [DREAM ACTIVE]                │
│  CUTOFF [−] 10 PM [+]   WAKE [−] 5 AM [+]             │
├───────────────────────────────────────────────────────┤
│  AFTER FALL — your streak broke.                      │
│  [ ] Reflect   [ ] Recommit smaller    [ ] DM partner │
├───────────────────────────────────────────────────────┤
│  TIME DEBT  +30m   effective target 150m (was 180m)   │
├───────────────────────────────────────────────────────┤
│  MOOD                                                 │
│  😀  😐  😞  😩       (tap one to log)                │
│  Recent: 14:02 😐 — focus block                       │
├───────────────────────────────────────────────────────┤
│  MORNING ROUTINE                                      │
│  [✓] Bed     [✓] Water     [ ] Read     [ ] Walk      │
├───────────────────────────────────────────────────────┤
│  IDENTITY    Builder 14    Consumer 3                 │
│  Vote now:  [ Builder ]   [ Consumer ]                │
├───────────────────────────────────────────────────────┤
│  SHUTDOWN RITUAL                                      │
│  [ ] What did I ship?  [ ] What's tomorrow's One?     │
├───────────────────────────────────────────────────────┤
│  HOURLY HEATMAP                                       │
│  00 01 02 …  09 10 11 12 13 14 15 …  22 23            │
│  ░░░░░░ … ▓▓ ▓▓ ░░ ░░ ▓▓ ▓▓ ▓▓ … ░░ ░░              │
├───────────────────────────────────────────────────────┤
│  BASELINE — based on 3 days, suggest Level 1 ≤ 180m?  │
│  [ Accept ]    [ Skip ]                               │
├───────────────────────────────────────────────────────┤
│  PROJECTS                                             │
│  ┌────────────────────┬────────────────────┐         │
│  │ Built launcher     │ Health rewrite     │         │
│  │ ▓▓▓▓▓░░░ 65%       │ ▓▓░░░░░░ 25%       │         │
│  └────────────────────┴────────────────────┘         │
├───────────────────────────────────────────────────────┤
│  TODOS                                                │
│  [ ] Ship design baseline doc                         │
│  [ ] Reply to Sarah                                   │
│  [ ] Workout                                          │
│  [+ add ]                                             │
├───────────────────────────────────────────────────────┤
│  APP TOMBSTONES   (apps you killed)                   │
│  💀 TikTok · 2026-02-04                               │
│  💀 Twitter · 2026-01-12                              │
├───────────────────────────────────────────────────────┤
│  NOURISHING                                           │
│  Kindle  Audible  Duolingo  Headspace                 │
├───────────────────────────────────────────────────────┤
│  WIDGETS                                              │
│  ┌────┐┌────┐┌────┐┌────┐┌────┐                       │
│  │CLOCK││Quote││Prog││  +  ││  +  │                    │
│  └────┘└────┘└────┘└────┘└────┘                       │
└───────────────────────────────────────────────────────┘
        ↑↑↑ (scroll continues, end of list)

┌─ pinned dock (5 apps) ────────────────────────────────┐
│   📞 Phone   💬 Messages   [app1]   [app2]   [app3]   │
└───────────────────────────────────────────────────────┘
   swipe up → hidden-drawer search bar (type to launch)
```

**Count: ~50 distinct vertical sections**, dock + hidden drawer at the bottom. No tabs, no segmentation. Many cards have inputs (text fields, chips, buttons) — the surface is dense and decision-heavy.

---

## 2. Lobby (overlay before a flagged app opens)

```
┌───────────────────────────────────────────────────────┐
│                                                       │
│                                                       │
│              YOU REQUESTED  TIKTOK                    │
│                                                       │
│                                                       │
│                       0:32                            │
│                                                       │
│              ╲___ progressively dimmed                │
│                                                       │
│                                                       │
│  PUZZLE TODAY: 47 + 31 = [    ]                       │
│                                                       │
│  or speak the mantra 3 times                          │
│  "I am the builder, not the consumer."                │
│                                                       │
│                                                       │
│           [ Wait it out ]   [ Cancel ]                │
└───────────────────────────────────────────────────────┘
```

---

## 3. Onboarding (first launch, ~6 screens)

```
1) Why are you here?         (long text field, saved verbatim)
2) Permissions               (usage, accessibility, device admin, notifications)
3) Pick a mantra             (4 defaults + custom, voice test)
4) Future-self video         (60-90s recording)
5) VIP contacts              (max 10)
6) Day 1-3: baseline obs.    (no restrictions yet, "app watches")
```

---

## 4. Transparency (settings)

```
┌─ TRANSPARENCY ────────────────────────────────────────┐
│  Every behavioural technique we use.                  │
│                                                       │
│  ▶ The Lobby            [●] on  (countdown)           │
│  ▶ Dimming              [●] on  (slot-machine penalty)│
│  ▶ Behavior indicator   [●] on  (moral framing)       │
│  ▶ Hidden drawer        [●] on  (no app grid)         │
│  ▶ Variable ratio       [●] on  (slot-machine penalty)│
│  ▶ Escalating lockout   [●] on                        │
│  ▶ Dream mode           [●] on                        │
│  ▶ Anchoring            [●] on                        │
│                                                       │
│  ─ OPT-IN SURFACES ───────────────────────────────    │
│  ▶ Mortality widgets    [ ] off                       │
│                                                       │
│  ─ ANTI-BIO ──────────────────────────────────────    │
│  [ "I'm not the person who scrolls at 1am."   ]       │
│  [ Save ]                                             │
└───────────────────────────────────────────────────────┘
```

---

## 5. Other screens

| Screen | What you see |
|---|---|
| **VIP** | List of up to 10 numbers, add/remove, 24hr cooldown notice |
| **Uninstall** | "Are you sure? 72hr cooldown" countdown → future-self letter page → future-self video → final confirm |
| **Future-Self Video** | Camera preview, record button, status (saved/ready) |
| **Mantra** | Text input + voice test |
| **Breath** | Single breathing circle, 4-7-8 timing, big start button |
| **Boredom** | Blank screen for 2 minutes (intentionally empty) |
| **Focus Timer** | Pomodoro UI |
| **Dream Mode** | Replaces Home between cutoff/wake. Single line + breathing animation |

---

## 6. Visual language right now

- **Layout:** Material-3 cards everywhere. Surface with 16dp rounded corners. Constant card-on-background pattern.
- **Type:** MaterialTheme.typography (labelLarge/labelSmall headers, headlineSmall/Medium values, bodyMedium/Small descriptions).
- **Colour:** Default Material 3 theme. Accent on primary, outlines for secondary text. Each metric card uses 50% surfaceVariant background.
- **Spacing:** 24dp horizontal page padding, 12dp vertical between cards.
- **Density:** **Very high.** Roughly 50 cards vertically. Numbers shown next to numbers. No section dividers. The home is the dashboard.

---

## 7. What's wrong with this (for the minimal pass)

1. **Too many cards.** A "minimal" launcher should fit on one screen, no scroll.
2. **Too many decisions on first load.** Mood, identity, intention, ratings, sliders, chips, sliders, sliders…
3. **No hierarchy.** A 365-day streak and a single mood-ping log have equal visual weight.
4. **Cards look like a normal app.** Rounded surfaces + headings + body text = the same template every Material app uses.
5. **Quantitative-only.** No silence. No empty space. Numbers next to numbers. The product is supposed to be about "less", but the home looks like a dashboard.

---

## 8. What "minimal" could mean (not committed — for the next round)

Sketch directions to choose from, *not* a decision yet:

- **Monochrome:** kill all surfaces and colour. Only the active "ONE THING" and the streak. Everything else moves behind a single "more" gesture.
- **Single-line per signal:** no cards, no headlines, no labels. Just lines: `12d. 47pts. 6h 12m sleep. 78% recovery.`
- **Type-only:** no shapes, no progress bars, no icons. Stats are sentences. `"You've been on a 12-day work streak. Today's one thing is X."`
- **Two surfaces:** Home = ONE THING + dock + search. Everything else lives behind a swipe-down or "stats" gesture.
- **Time-of-day surface swap:** morning routine in the morning, focus stats during work, shutdown at night, dream at night. Never all at once.

We'll pick one (or hybrid) and tweak.
