# The Life-Tracking App Landscape — Market Map

> Date: 2026-05-17
> Sources: 4 parallel research agents, ~400 apps surveyed across body / mind / finance / productivity / life-domains / quantified-self / life-OS / annual-reports.
> Caveat: agents worked from training knowledge through Jan 2026. Pricing and exact positioning shift constantly — re-verify before quoting externally.

This document maps what already exists, names the patterns that survive, identifies white-space gaps, and lays out the strategic implications for FocusLauncher as it expands from "launcher" toward "OS for life."

---

## Part 1 — The 15 categories

### 🏃 BODY

| Sub-category | Top players | What makes the winner win |
|---|---|---|
| Fitness / GPS / steps | Strava, Garmin Connect, Apple Fitness+, Nike Run Club, Komoot, AllTrails, Zwift, Peloton, MapMyRun, Adidas Running, Pacer | Strava = social moat (segments). Garmin = data depth. Komoot/AllTrails = route DB. |
| Strength / lifting | Hevy, Strong, Fitbod, JEFIT, Caliber, Future, Centr, Ladder, FitNotes, Boostcamp, Liftin', Alpha Progression | Hevy stole #1 from Strong on UX + social feed. Fitbod = AI workouts. Future = $199/mo human coach premium. |
| Sleep | Oura, Whoop, Sleep Cycle, AutoSleep, Pillow, Sleep as Android, Rise Science, SleepScore, Eight Sleep | Hardware lock-in dominates. AutoSleep is the iOS-software-only cult. |
| Nutrition | MyFitnessPal, Cronometer, MacroFactor, Lose It!, Cal AI, Yazio, Lifesum, Noom, Foodvisor, Carb Manager, Zero, Levels, Zoe | MFP = database moat. MacroFactor = adaptive math. Cal AI = TikTok-famous photo-counting. Levels = CGM frontier. |
| Cycle / fertility | Flo (300M users), Clue, Natural Cycles (FDA-cleared), Ovia, Stardust, Apple Health Cycle, Glow, Kindara, Wild.AI, Oova | Flo = scale. Natural Cycles = FDA-cleared birth control. Wild.AI = cycle-training integration (rare). |
| Symptom / chronic | **Bearable**, Migraine Buddy, Cara Care, MySugr, Visible, Guava, mySymptoms, Flaredown, CareClinic | **Bearable = the QS swiss-army knife — most flexible correlation engine in existence.** |
| Weight / body comp | Happy Scale, Libra, Withings, RENPHO, Eufy Life, Lumen, InBody | Withings = hardware ecosystem. RENPHO = cheap smart-scale default. |
| HRV / recovery | Whoop, Oura, Elite HRV, HRV4Training, Welltory, Garmin Body Battery, Polar Nightly Recharge, Athlytic, Training Today, Morpheus | Athlytic = "Whoop-score using only Apple Watch" (interesting wedge). Welltory = HRV via phone camera. |
| Hydration | WaterMinder, Plant Nanny, Hydro Coach, Waterllama, HidrateSpark | Gamified mascots; uniformly poor solving "I forgot to log." |
| Super-apps / aggregators | Apple Health, Health Connect, Google Fit, Samsung Health, Huawei Health, Gyroscope, Bevel, Welltory, Heads Up Health | **OS-bundled defaults eat third-party aggregators alive.** |

### 🧠 MIND

| Sub-category | Top players | Killer feature |
|---|---|---|
| Mood / emotion | Daylio, How We Feel (Yale RULER, free), Bearable, Stoic, Pixels, MoodFit, eMoods, Moodnotes, MoodKit, Reflectly | Daylio = icon-tap, no typing. How We Feel = nonprofit, free forever. Pixels = year-grid visualization. |
| Journaling | Day One, Journey, Diarium (Windows-first), Stoic, Five Minute Journal, Grid Diary, Penzu, Daybook, Mello | Day One = polish + E2EE + "on this day." |
| Meditation | Headspace, Calm, Waking Up (Sam Harris), Insight Timer (200k free), Balance, Ten Percent Happier, Healthy Minds (free + RCT), Smiling Mind, Medito (open-source), Oak | Calm = Sleep Stories celebs. Insight Timer = 200k free. Healthy Minds = research-backed + free. |
| Therapy / CBT | BetterHelp, Talkspace, Sanvello, Woebot, Wysa, Youper, MindShift CBT (free, Anxiety Canada), CBT-i Coach (VA, free), PTSD Coach (VA, free), Bloom, Cerebral | AI chatbots feel half-baked. BetterHelp has data scandals. Middle ground (journal-your-therapist-uses) is empty. |
| Anxiety | Rootd (panic button), Dare (Barry McDonagh method), MindShift CBT, Welltory, Nerva (hypnotherapy for IBS), Calm Harm, Happify, PanicShield | Rootd = panic SOS button. Nerva = hypnotherapy for gut-brain. |
| Gratitude | Gratitude, Presently (open-source Android), Stoic, ThinkUp (your own voice affirmations), I Am, 365 Gratitude | ThinkUp = play your own voice affirmations (weird, sticky). |
| Reading | Goodreads (Amazon-owned, stale), StoryGraph, Hardcover, Bookly, Readwise, Readwise Reader, Kindle, Audible, Libby, Literal, Fable, Basmo | StoryGraph = "the Goodreads we deserved." Readwise = spaced-rep on highlights. |
| Learning | Duolingo (extreme streak gamification), Anki (open-source, ugly, godtier), Brilliant, Memrise, Babbel, Lingvist, Drops, Pimsleur, Speak (GPT conversation), Coursera, MasterClass, Skillshare, Quizlet, RemNote | Duolingo = streak weaponization. Anki = unbeatable powertool, looks like Windows 98. |
| Dreams / lucid | Awoken, Lucidity, Dreamboard, Capture Dreams, DreamKeeper, Elsewhere (AI), DreamApp (AI), Lucid | **Entire category abandoned since ~2014.** AI interpretation just starting (Elsewhere, DreamApp). |
| Brain training | Lumosity (FTC-fined for overclaims), Elevate, Peak, CogniFit, Cambridge Brain Sciences, NeuroNation, BrainHQ (RCT-validated), Impulse | **Credibility crisis in whole category — Lumosity settled $2M with FTC.** BrainHQ is the only one with real RCT evidence. |
| Notes / second brain | Notion, Obsidian (local-first cult), Roam Research (pioneer, eclipsed), Logseq, Bear, Apple Notes, Mem, Evernote (collapsed), Craft, Capacities, Tana, Reflect, Heptabase, Scrintal, Supernotes, Standard Notes, Workflowy | Obsidian = local-first cult. Notion = mass-market. Roam = pioneer, eclipsed by cheaper/free competitors. |

### 💰 FINANCE

| Sub-category | Top players | Wedge |
|---|---|---|
| Budgeting | YNAB (methodology cult, no free), Monarch Money (post-Mint default), Copilot (best iOS design), Rocket Money (does it for you), Lunch Money (indie), Actual Budget (open-source self-host), EveryDollar (Ramsey), Goodbudget (envelope, no bank link), PocketGuard, Simplifi, Tiller (sheets-based), Quicken Classic, Wallet, Money Lover, Spendee | Monarch = couples-first. Copilot = polish. YNAB = cult methodology. Lunch Money + Actual = privacy. |
| Net worth / investments | Empower (free retirement tool), Kubera ($150/yr, alt assets + death-switch), Sharesight (tax + dividends), Snowball, Delta, Stock Events, Morningstar Investor ($250/yr), Magnifi (AI), SigFig | Empower's free retirement = moat. Kubera = everything-incl-illiquid. Sharesight = tax/dividends. |
| Subscription hunters | Rocket Money (Truebill), Bobby (manual, no bank link), Hiatus, Subby, Trim | Rocket Money takes 30-60% of savings. Bobby = privacy default. |
| Receipts / cashback | Fetch Rewards, Rakuten, Ibotta, Capital One Shopping, Honey (PayPal), Receipt Hog, Upside, Drop, Shopkick | Fetch = teen/college base via gift-card points. |
| Couples finance | Honeydue, Zeta, Monarch (couples-first), Goodbudget (shared envelopes), Splitwise, Ivella | **Surprisingly thin category. Honeydue + Zeta dominate by default.** |
| Crypto portfolio | CoinStats (300+ exchanges), Delta, CoinTracker (tax), Koinly (tax), Zerion (DeFi), DeBank, Zapper, Rotki (privacy/open-source), CoinGecko Portfolio | CoinTracker/Koinly = tax. Zerion/DeBank = DeFi. Rotki = privacy. |
| Bill negotiation | Rocket Money, Trim, BillShark, DoNotPay (AI fee disputes), Prism, BillTracker | All take % of savings. |
| Side-hustle / 1099 | Stride (free), Keeper Tax (AI deductions), Hurdlr, MileIQ (Microsoft), Everlance, TripLog, QuickBooks Self-Employed, FreshBooks, Bonsai, FlyFin, Found | Stride = free leader. MileIQ = mileage default. |
| Neobank / aggregators | Revolut, Wise, N26, Monzo (best in-app budgeting "pots"), Starling, Chime, Cash App, Curve, Emma | Monzo's "pots" set the in-app budgeting bar. |

### ⏱️ PRODUCTIVITY / FOCUS

| Sub-category | Top players | Wedge |
|---|---|---|
| Habit trackers | Habitica (RPG/social — the only one!), Streaks (Apple Design Award), Productive, Way of Life (Seinfeld chain), Habitify, HabitNow, Loop Habit Tracker (open-source), Strides, Done, TickTick Habits, Fabulous (coached), HabitKit (pixel-grid), Stoic, Daylio | Habitica = only social habit app at scale. Streaks = polish. Loop = open-source. |
| Todos | Todoist (NLP + integrations), TickTick (habits + pomodoro), Things 3 (Apple Design Award, one-time), Microsoft To Do (free), Apple Reminders, Google Tasks, OmniFocus (power-user GTD), Notion, Obsidian Tasks, Amazing Marvin, Sorted³, Akiflow, Any.do, Superlist, 2Do | Todoist = NLP + integrations. Things = design. OmniFocus = GTD power-user lock-in. |
| Time tracking | Toggl Track (free tier generous), RescueTime (passive auto-track), Timery (iOS+Toggl), Hours, Clockify (free), Harvest, Timing (Mac auto-track), Tyme, TimeCamp, Hubstaff (surveillance), Memory.ai (AI timesheets), ATracker, aTimeLogger, Session | RescueTime = passive default. Toggl = manual default. |
| Focus / blocking | Forest (plant trees), Freedom, Cold Turkey (un-uninstallable), AppBlock, Opal, **one sec** (mandatory breath), **Brick** (NFC hardware), Jomo, ScreenZen, Clearspace, Refocus, Block Site, StayFocusd, LeechBlock, SelfControl (free Mac), Focus, Flipd, Roots, Bear Focus Timer, Focus To-Do, Tide, Centered, Endel (focus sounds), Brain.fm | **Direct competitor zone for FocusLauncher.** |
| Calendar / planning | Fantastical (Apple polish), Google Calendar, Notion Calendar (was Cron), Sunsama (daily ritual, $20/mo), Motion (AI autopilot, $34/mo), Reclaim, Akiflow, Calendly, Cal.com (open-source), Vimcal, Amie, Morgen, TimeTree, Microsoft Outlook | Sunsama = ritual. Motion = AI autopilot. Fantastical = Apple-power-user. |
| **Phone restriction launchers** | Minimalist Phone, Olauncher, Niagara Launcher, Before Launcher, Indistract, Blank Spaces, Dumbify, Light Phone, Brick, unpluq, Pareto Launcher, Lessphone, Ratio, Vesper, Dumbphone Launcher, Punkt MP02, Mudita Pure | **The competitive set. Deep dive below.** |
| Goals / OKR | Strides, Goalify, GoalsOnTrack (2009-era UX), Lifetick, **Beeminder** (charges you when you fail), **Stickk** (commitment contracts), Notion templates, Tability, Weekdone | **Mostly bland category.** Beeminder + Stickk are quirky niche. |
| Year-in-review | (See life-OS category) | **Always single domain** (Spotify, Letterboxd, Strava). Multi-domain doesn't exist. |

### 🌍 LIFE DOMAINS

| Sub-category | Top players | Wedge |
|---|---|---|
| Personal CRM | Monica (open-source self-host), Dex (LinkedIn sync), **Clay** ($20/mo ambient auto-import + AI), Cloze (auto-pull email/calendar), UpHabit, Notable, Garden, Circles, Hippo, Covve | Clay + Cloze win by hiding data entry. Monica = self-host nerds. **Manual entry kills 90% of users.** |
| Location / travel | **Polarsteps** (auto-track + printed travel book), Visited, Been, Arc (iOS, on-device), Dawarich (self-hosted Google Timeline), Owntracks (MQTT), Swarm/Foursquare, FogOfWorld (literal map fog-of-war), Wanderlog, TripIt, Day One Maps | **Polarsteps wins on physical artifact** (printed book). FogOfWorld = gamified literal fog-of-war. |
| Photo journal / memories | **Day One** (Automattic-owned), Diarium (Windows-first), Timehop (declining as APIs close), **One Second Everyday / 1SE** (annual film), Hindsight, Journey, Momento, Daylio, Five Minute Journal | 1SE wins on "the output is the product" — 1-sec/day stitched film. Day One = polish + on-this-day. |
| Pets | 11pets, Pawprint, Tractive (GPS collar), Fi (Apple-watch-grade dog collar), Whistle, PetDesk (vet-pushed), Pupford (training) | Hardware-tied wins (Fi, Tractive). |
| Plants | **Planta** ($40/yr Swedish polish), Greg (social), PictureThis (ID), Blossom (ID), Vera, Garden Tags, From Seed To Spoon, Smart Plant Home | Planta = polish. Greg = plant social. Care reminders = killer feature. |
| Films / music / TV / games / restaurants | **Letterboxd** (films, the masterclass), Trakt + TV Time (TV), Last.fm + Stats.fm (music), Backloggd + HowLongToBeat (games), Exophase + PlayNite (cross-platform games), setlist.fm + Glaze (concerts), **Beli** (restaurants — Letterboxd-for-meals attempt) | **Letterboxd is the masterclass: Strava for films. Social + lists + annual report.** Scrobbling kills manual logging. |
| Substances / sobriety | I Am Sober (largest community), Reframe (CBT-based), **Sunnyside** (moderation, not abstinence — clever wedge), Less (Apple Design Award), Sober Time, Nomo (multi-clocks for porn/gambling/etc.), Quit Genius, Smoke Free, EasyQuit, Try Dry (UK Alcohol Change) | Streak counter is unmatched UX. Sunnyside's "mindful drinking" framing = real wedge. |
| Sex / intimacy | Emjoy (audio-led), Ferly, Coral (couples quizzes), Lover, **Paired** (couples Q&A, most popular), Honi, Pillow, Kindu (anon yes/no/maybe), Bloom (porn moderation), Brainbuddy | **Paired (relationship Q&A) outperforms direct intimacy trackers.** Category struggles with retention — logging feels clinical. |
| Driving / mileage | **MileIQ** (Microsoft, tax default), Hurdlr, Drivvo, Fuelio, Stride (free), Everlance, TripLog, Carly/Torque (OBD-II), Carfax Car Care, My Cars, Road Trippers | Tax deduction = sticky. |
| Weather / sun | dminder (vitamin D synthesis), Apple "Time in Daylight," AirCare/Plume (pollution exposure), QSun/SunSafe, Mercury Weather (trip history) | **Lifetime "weather you lived" is essentially un-built.** |
| Niche QS | **Bearable** (anything + correlation), PoopMD (ML on photos), Bowelle (IBS), Cara Care, MyBladder, TroveSkin/MDacne (acne photo-progression), Migraine Buddy, Hair Diary, Sneeze tracker, TickTalk (Tourette), Cronometer (micronutrients), InsideTracker (blood biomarkers), Heads Up Health (labs + lifestyle) | Bearable's correlation engine = best in QS world. |
| Mortality | **WeCroak** ($2, 5 daily death pushes), Final Roll (weeks-of-life-remaining), Memento Mori widgets, Life Calendar (Wait But Why 90yr grid), Stoic (woven in) | WeCroak inspired by Bhutanese proverb. Most have churn — death isn't daily-engagement-friendly. |
| Year-in-review | Spotify Wrapped (genre-defining), Apple Music Replay, Stats.fm Wrapped, Letterboxd YIR, Strava Year in Sport, Pocket Casts YIR, Duolingo YIR, 1SE annual film, Polarsteps travel book, Day One book exports, Beli, GitHub Skyline, Snapchat/Instagram "Year on…", Reddit Recap | **Always single-domain.** Multi-domain "your year in life" doesn't exist. |

### 🌐 LIFE OS / ALL-IN-ONE — the category that matters most

**The graveyard is full. Study every failure.**

| App | Status | Why it lives or dies |
|---|---|---|
| **Exist.io** | 🟢 Alive, tiny | Aggregates 30+ services, surfaces correlations ("you sleep worse on rainy days"). Survives as labor of love. Never crossed chasm — utilitarian UX, non-QS people don't get it. |
| **Gyroscope** | 🔴 Effectively dead by 2023 | Stunning design. Killed by: pricing whiplash ($0 → $78/mo), creator-driven w/ no team, scope creep into coaching/supplements. **Cautionary tale #1.** |
| **Bevel** | 🟡 Quiet / acquired | Beautiful, narrowly positioned around PoC health. |
| **Welltory** | 🟢 Alive | Real wedge: HRV without hardware (phone camera PPG). |
| **Day One** | 🟢 Successful by staying narrow | Acquired by Automattic 2021. Journal-only, didn't pretend to be a dashboard. |
| **Reflect / Mem / Tana / Capacities** | 🟢 Alive, niche | "Thinking OS," not "life OS." |
| **Sunsama** | 🟢 Growing | Work-adjacent but adopted as life-planner by power users. |
| **Stoic** | 🟢 Profitable indie | Smart wedge: philosophy hook. |
| **Open** | 🟢 Alive | "Wellness OS" hybrid (meditation + breath + journal). |
| **Bend** | 🟢 Alive | Stays narrow → survives. |
| **Routinery** | 🟢 Alive | Time-blocked routines as habit OS. |
| **Apple Health** | 🟢 The 800-lb gorilla | OS-bundled. Eats third-party aggregators. |
| **Notion / Obsidian personal dashboards** | 🟢 DIY dominant | Where life-OS power users actually end up. |
| **Fabriq** | 🔴 Faded | Tried to blend PRM + habits. Manual entry tax killed it. |

### Why life-OS keeps failing — the rules of the graveyard

1. **Data-entry tax.** Anything not auto-tracked decays in weeks.
2. **No artifact / no output.** Polarsteps makes a book. 1SE makes a video. Life-OS dashboards make… another dashboard. Nothing to share, nothing to look back on.
3. **Scope creep.** Every life-OS app eventually adds coaching, supplements, AI chat, courses — drifting from "mirror" to "self-help product." Gyroscope textbook.
4. **Platform competition.** Apple Health + Health Connect ate the floor.
5. **The user is a developer.** Anyone who actually wants this is technical enough to roll their own in Notion/Obsidian/Airtable.
6. **Pricing whiplash.** Intimate data → asymmetric churn. Once they leave, they don't come back.
7. **Privacy paradox.** Rich data → scary trust requirement. Most users don't trust a Series-A startup with their whole life.

### What's working in this category

- **Narrow + beautiful** (Streaks, Bend, Day One, Stoic) — outlive every "everything app."
- **Aggregator + correlations** (Exist.io, Bearable) — small but loyal.
- **Output as product** (1SE, Polarsteps books, Spotify Wrapped) — the artifact is the moat.

---

## Part 2 — The five patterns that survive

Across 400+ apps, only these patterns reliably win:

1. **Streak weaponization** (Duolingo, Daylio, I Am Sober, Stoic, Snapchat fire emoji)
2. **Hardware lock-in** (Oura, Whoop, Garmin, Withings, Levels, Eight Sleep, Tractive, Fi, Brick, unpluq)
3. **Scrobbling / auto-ingestion** (Last.fm, Trakt, Stats.fm, Strava from devices, Arc, RescueTime, Letterboxd-from-Plex)
4. **Artifact as product, not dashboard** (Spotify Wrapped, Letterboxd posters, Polarsteps printed books, 1SE annual film, GitHub Skyline, Strava Year in Sport)
5. **Narrow + beautiful** (Streaks, Bend, Day One, Stoic) — outlive every "everything app"

---

## Part 3 — White-space gaps the market is begging for

### Cross-domain integration gaps
1. **"Spotify Wrapped" for life** — multi-domain annual artifact (films + books + travel + people + photos + mood + money + restaurants). The data exists across silos, nobody stitches it.
2. **"Spotify Wrapped" for money** — nobody owns shareable year-in-money recap.
3. **"Spotify Wrapped" for focus / phone usage** — RescueTime tries weakly. Open lane.
4. **Mental ↔ physical correlation across siloed apps** — mood trackers and body trackers don't talk. The app that surfaces "your sleep tanked → mood tanked → workouts tanked" is the strongest "life OS" pitch on the market.
5. **Money + time + life unified** — "the year I spent $X and Y hours on hobby Z."
6. **Lifetime "people-met" graph** — who you've ever shared a calendar event or photo with, when, where, how often. Adjacent to PRM but ambient.
7. **Dream content ↔ waking life correlation** — Bearable does symptoms. No one does dreams.

### Single-domain holes
8. **Lifetime weather lived** — Letterboxd-for-weather doesn't exist.
9. **Lifetime restaurants / dishes** — Beli is closest, no real winner.
10. **Recovery score for non-athletes** — Whoop/Oura pitch performance; knowledge worker version using Apple Watch is unsolved (Athlytic is closest, niche).
11. **Cycle + training integration** — Wild.AI is alone.
12. **Men's hormonal/longevity tracking** — Bryan-Johnson-Blueprint-style consumer app.
13. **Therapy adjuncts** — between Wysa (AI) and BetterHelp (humans), nothing for "journal/mood your therapist actually uses between sessions."
14. **Couples / family mental health** — Paired exists for prompts, not mood.
15. **Local-first + E2EE mental health** — BetterHelp got FTC-fined $7.8M for sharing data with Facebook. Massive trust gap.
16. **Men's mental health UX** — everything in the space is feminine-coded. Stoic + Waking Up are exceptions.
17. **Couples finance with good UX** — Honeydue/Zeta default by lack of competition.
18. **Privacy-first PFM** — Plaid-dependent apps dominate; Lunch Money + Actual Budget are niche.
19. **Goal tracking that isn't "habits with extra fields"** — Beeminder is unique but ugly. Open lane.
20. **Medication tracking** — Medisafe/CareClinic are functional but ugly.
21. **Hydration UX** — everyone fails "I forgot to log half my drinks."
22. **Lucid dreaming** — category abandoned since ~2014.

### iOS reality check
23. **iOS cannot have a real launcher.** Apple won't open the API. iOS apps in this space are all Screen Time wrappers (Opal, Jomo, ScreenZen, one sec, Clearspace). Hardware fills the void (Brick, unpluq). **FocusLauncher is fundamentally an Android product.**

---

## Part 4 — FocusLauncher's competitive set

| Competitor | Position | Pricing | Threat |
|---|---|---|---|
| **Niagara Launcher** | Polished freemium incumbent. Alphabet-edge minimal launcher. | ~$10/yr | 🟢 High — best polish |
| **Olauncher** | Open-source, free, text-list | Free | 🟡 Medium — captures nerds |
| **Minimalist Phone** | Aggressive hiding + greyscale | Freemium | 🟡 Medium |
| **Before Launcher** | Notification-zen | Freemium | 🟡 Medium |
| **Indistract / Lessphone / Ratio / Vesper / Pareto / Dumbphone Launcher** | Long tail of similar Android launchers | Free / freemium | 🟢 Low — none have moats |
| **Light Phone (II/III)** | Actual hardware dumb phone | $300–800 | 🟡 Niche adjacent — purist customers |
| **Punkt MP02 / Mudita Pure** | Hardware dumb phones | $349–370 | 🟡 Same — premium niche |
| **Brick** (hardware NFC) | Tap-to-lock-apps NFC tag | $59 hardware | 🟡 Aligned with our `HARDWARE-001` |
| **unpluq** (hardware NFC) | Tag + app blocker | $59 + sub | 🟡 Same mechanic |
| **Opal / Jomo / ScreenZen / one sec / Clearspace** | iOS Screen Time wrappers | $30-60/yr | 🟢 The iOS competitor set we'd face as companion |

**Critical insight:** Every existing launcher is **static** — greyscale, hide app icons, ask for an intention before opening. **None have a behavioral feedback loop. None tie restriction to positive habit reinforcement. None integrate finance / fitness / life data. None produce a year-in-review artifact. None have couples / accountability modes. None have a creator economy.**

That gap is **exactly** the seam where FocusLauncher becomes a category-killer instead of competitor #15.

---

## Part 5 — Strategic synthesis for OS-for-Life

### What NOT to do (graveyard rules)
- Don't build manual entry as primary UX. (Killed Fabriq, hurt Gyroscope.)
- Don't compete with Apple Health on "all your health in one place." OS-bundled wins.
- Don't launch broad. Every survivor started obsessively narrow.
- Don't whiplash on pricing. Once intimate-data users leave they never come back. (Killed Gyroscope.)
- Don't make a dashboard your output. Make an artifact.

### What MUST be true
- **Launcher is the trojan horse.** Phone restriction is where we already have a unique product. Use it to earn the right to collect everything else.
- **Auto-ingest everything.** HealthConnect, Plaid, Strava, Spotify, Letterboxd, Photos, Calendar. No manual entry except journals + mood.
- **Build the artifact.** Year in Review (Wrapped-grade, multi-domain) is the single highest-leverage feature.
- **Stay narrow at first; let users pull you wide.** Phone discipline → discipline tracking → mood pairing → finance pairing → location pairing → eventually OS for life. Each step earns the next.
- **Android-first.** iOS is fundamentally a worse product for this; treat iOS as companion-only.
- **Privacy as moat.** Local-first by default, E2EE cloud. The combo of phone + health + finance + mood + location is uniquely catastrophic if leaked — uniquely defensible if you're the only place safe enough.

### Three-legged stool
**Launcher + integrations + artifact.** No competitor has all three.
- Niagara has launcher only.
- Bearable has integrations only.
- Polarsteps has artifact only.
- Spotify Wrapped has artifact only (single-domain).
- Exist.io has integrations only (no artifact, no launcher).

We can have all three.

---

## Appendix — file structure

This research generated three follow-up artifacts:
- `docs/12-app-landscape.md` — this document.
- `docs/13-naming-and-positioning.md` — rebrand consideration (FocusLauncher → ?).
- New features added to `docs/09-features-master.json` — `LIFE-*`, `FIN-*`, `FIT-*`, `MIND-*`, `LOC-*`, `INTEG-*` categories.
