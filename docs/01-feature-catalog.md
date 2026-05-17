# FocusLauncher — Feature Catalog

## Scoring Guide

Each feature scored 1-5 on three axes:

- **Impact**: How much does this change user behavior? (5 = life-changing)
- **Effort**: How hard to build? (5 = months of work, 1 = weekend project)
- **Feasibility**: Can Android actually do this? (5 = fully possible, 1 = impossible/needs root)

**Priority Score** = Impact × Feasibility ÷ Effort (higher = build first)

---

## Category A: Restriction & Friction Mechanics

| # | Feature | Description | Impact | Effort | Feasibility | Priority |
|---|---------|-------------|--------|--------|-------------|----------|
| A1 | Cognitive Tax | Puzzles before opening distraction apps. Scales with opens/day. 1st=swipe, 5th=multiplication, 10th=haiku | 4 | 2 | 5 | 10.0 |
| A2 | The Lobby | Mandatory wait screen before app opens. 15s productive, 90s distractions. No skip. | 5 | 1 | 5 | 25.0 |
| A3 | Progressive Dimming | Screen brightness drops 2%/min in distraction apps. Near-black at 25min. | 4 | 2 | 4 | 8.0 |
| A4 | The Slow Zone | Distraction apps render at 0.5x animation speed. Scrolling feels like molasses. | 3 | 3 | 3 | 3.0 |
| A5 | One App At A Time | No multitasking. 3-second close animation before next app opens. | 4 | 2 | 4 | 8.0 |
| A6 | Reverse Home Screen | Home shows ONLY calendar + current task + timer. No app grid. | 5 | 2 | 5 | 12.5 |
| A7 | App Roulette Lock | Pick 3 apps to restrict. System randomly picks ONE to be available each day. | 3 | 2 | 5 | 7.5 |
| A8 | Random Language Mode | After time limit, UI switches to unknown language. Navigate by memory or give up. | 3 | 3 | 3 | 3.0 |
| A9 | Reverse Notifications | No push notifications. Manual "check-in" at scheduled windows only. | 5 | 3 | 4 | 6.7 |
| A10 | Gravity Feed | Social feeds load bottom-to-top. Breaks scroll muscle memory. | 3 | 4 | 2 | 1.5 |
| A11 | Memory Wipe | Force-close app AND clear scroll position/algorithm state after time limit. | 4 | 3 | 2 | 2.7 |
| A12 | The Decay Algorithm | Each reopen: more pixelated, slower, uglier UI. By 5th open looks like 2004 phone. | 4 | 3 | 3 | 4.0 |
| A13 | Choice Overload Weapon | Want blocked app? Presented with 47 alternative activities in chaotic grid. Decision fatigue. | 3 | 2 | 5 | 7.5 |
| A14 | Variable Ratio Punishment | Restrictions are RANDOM. Sometimes app works. Sometimes locked. Unpredictability breaks habits. | 4 | 1 | 5 | 20.0 |
| A15 | Intent Declaration | Must state intention + timer before opening distraction. Don't finish stated goal? App locked for day. | 4 | 2 | 5 | 10.0 |
| A16 | The Replacement Engine | Intercept app open, offer alternative: "Read 2 pages? 10 pushups? Write 3 sentences?" Accept = earn 5min. | 4 | 3 | 5 | 6.7 |
| A17 | Escalating Lockout | Open blocked app → locked out of EVERYTHING. 5min→30min→2hrs escalation. | 4 | 2 | 5 | 10.0 |
| A18 | Gatekeeper Tasks | Complete boring task (math, Duolingo lesson) before accessing addictive apps. | 4 | 2 | 5 | 10.0 |
| A19 | Time Debt | Overspent today? Double penalty locked out tomorrow. Debt compounds. | 4 | 2 | 5 | 10.0 |
| A20 | The Paradox Lock | Must write 50-word essay on why you SHOULDN'T open app. Good essay = stays locked. Bad = also locked. | 3 | 3 | 5 | 5.0 |
| A21 | Shrinking Circle Timer | Visual timer on home screen. Green→yellow→red→tombstone. Earn back time with challenges. | 4 | 2 | 5 | 10.0 |
| A22 | Context-Aware Locks | GPS+time+calendar. At gym=only music. At desk 9-5=only work. In bed after 10=only kindle. | 5 | 3 | 4 | 6.7 |
| A23 | Dream Mode | After 10pm, phone fades to: tomorrow's schedule, quote, breathing exercise. Apps disappear. | 4 | 2 | 5 | 10.0 |
| A24 | Weather-Gated Apps | Beautiful day? Entertainment locked. "72° and sunny. Go outside." | 3 | 2 | 5 | 7.5 |
| A25 | Sound Diet | Track audio input hours. After threshold: forced silence. No media playback. | 3 | 3 | 3 | 3.0 |
| A26 | The Boredom Preservatory | 2-min intervals of NOTHING. No apps available. Blank screen. Train boredom tolerance. | 4 | 1 | 5 | 20.0 |
| A27 | Anti-Notification Cannon | FLOODS 50 fake notifications when distraction app pings. Associate sound with annoyance. | 2 | 2 | 4 | 4.0 |

---

## Category B: Psychological & Identity Mechanics

| # | Feature | Description | Impact | Effort | Feasibility | Priority |
|---|---------|-------------|--------|--------|-------------|----------|
| B1 | The Real Clock | Status bar shows "estimated hours alive remaining" instead of time. Deducts in real-time. | 4 | 2 | 4 | 8.0 |
| B2 | Session Receipts | After closing ANY app, full-screen receipt: "23 min = cook a meal, 2km run, 40 Japanese chars" | 4 | 2 | 5 | 10.0 |
| B3 | Opportunity Cost Ticker | Permanent overlay: "This session = $34.50 at your hourly rate" | 4 | 2 | 5 | 10.0 |
| B4 | Legacy Counter | Lifetime hours per category on home screen. "Entertainment: 4,247hrs. Creation: 312hrs. Ratio: 13.6:1" | 5 | 2 | 5 | 12.5 |
| B5 | The Contract | Write values on paper, photo upload. AI cross-references behavior daily. | 4 | 3 | 5 | 6.7 |
| B6 | Two Modes: Builder/Consumer | Toggle between "Builder" (unlimited work apps) and "Consumer" (daily budget). Identity split. | 5 | 2 | 5 | 12.5 |
| B7 | Identity Voting | Every app open framed as vote: "This is a vote for: [Consumer] or [Creator]?" | 4 | 1 | 5 | 20.0 |
| B8 | The Alter Ego | "Distracted Self" as named character with separate stats. Dissociation as tool. | 3 | 2 | 5 | 7.5 |
| B9 | Skill Tree Phone | Fresh install = calls only. Real-world tasks unlock features. 7-day gym streak = Spotify. | 5 | 4 | 4 | 5.0 |
| B10 | The Mirror Widget | Front camera as home wallpaper. See YOUR face every unlock. Reduces mindless behavior. | 4 | 1 | 5 | 20.0 |
| B11 | Future Self Video | Record "don't do it" video during strong moment. Plays before every relapse. Can't skip. | 5 | 2 | 5 | 12.5 |
| B12 | The Regret Simulator | AI-generated "future memory" before app open: "It's 11:47pm. You feel empty." | 4 | 3 | 5 | 6.7 |
| B13 | The Last Day Test | "If you die tomorrow, would your last 30 min be this?" Single button confirm. | 3 | 1 | 5 | 15.0 |
| B14 | Deathbed Simulator | Monthly projection: "At current rate: 11.3 YEARS on social media by end of life." | 4 | 2 | 5 | 10.0 |
| B15 | The Stoic Interceptor | Contextual philosophy quotes as mandatory splash screens before distraction apps. | 3 | 2 | 5 | 7.5 |
| B16 | Peak-End Manipulation | Distraction sessions always END on worst note (boring content, ugly UI, slow fade). | 3 | 3 | 3 | 3.0 |
| B17 | Anchoring Attack | Before your stats, show "most disciplined user: 12min total." Then your 4hrs feels massive. | 3 | 1 | 5 | 15.0 |
| B18 | Future Self Messenger | Write letters that deliver at random future weak moments. Past-you coaching present-you. | 4 | 2 | 5 | 10.0 |
| B19 | The Aging Filter | Profile pic ages via AI for every minute in distraction apps. See yourself 10yrs older. | 3 | 4 | 4 | 3.0 |
| B20 | Earned Pixels | Home screen starts 1-bit grayscale. Focused hours earn color/resolution back. | 4 | 3 | 5 | 6.7 |
| B21 | The Empty Room | Home screen = 3D empty room. Tasks completed = furniture/plants grow. Zen garden via discipline. | 4 | 4 | 5 | 5.0 |
| B22 | App Tombstones | Killed apps show gravestones: "Here lies Candy Crush. Age: 47 plays this month." | 3 | 1 | 5 | 15.0 |
| B23 | App Ghosts | Deleted apps leave transparent untappable icons for 30 days. Visible graveyard. | 3 | 2 | 5 | 7.5 |

---

## Category C: Social & Accountability Mechanics

| # | Feature | Description | Impact | Effort | Feasibility | Priority |
|---|---------|-------------|--------|--------|-------------|----------|
| C1 | Shame Notification | Fail → silent notification to friend: "Risky opened TikTok 8th time today 😬" | 5 | 3 | 5 | 8.3 |
| C2 | The Warden | Designated person gets every fail. Can grant/deny app access remotely. | 5 | 3 | 5 | 8.3 |
| C3 | Betting on Streaks | Friends bet on your streak. You break? They profit. "$4.23 from your failure." | 4 | 4 | 4 | 4.0 |
| C4 | Partner Live Feed | Real-time updates to partner. They react 👏/😂. Pattern-based preemptive texts. | 4 | 3 | 5 | 6.7 |
| C5 | Confession Journal | "Why did I open this?" for every fail. Compiled daily report. Optional email to friend. | 4 | 2 | 5 | 10.0 |
| C6 | Public Commitment Wall | Post commitment publicly. Uninstall triggers auto-post: "erluxman quit after 12 days 🏳️" | 4 | 3 | 5 | 6.7 |
| C7 | The Sponsor System | AA-style human sponsor. Gets notified on uninstall attempt. Calls you. | 5 | 3 | 4 | 6.7 |
| C8 | Team Streaks | Squad of 5. All must stay installed. You quit = everyone's streak dies. | 5 | 3 | 5 | 8.3 |
| C9 | Crowd-Sourced Willpower | About to break? Anonymous post to community: "Someone about to relapse. Send strength." | 4 | 3 | 5 | 6.7 |
| C10 | The Disappointment API | One person you respect gets ONE notification/week: your worst stat. | 5 | 2 | 5 | 12.5 |
| C11 | Shame Cards | Shareable fail stats as cards. "Hall of Shame" leaderboard. | 3 | 2 | 5 | 7.5 |
| C12 | Proximity Unlock | Apps only work near specific people (Bluetooth). Social media only when alone. | 3 | 3 | 4 | 4.0 |
| C13 | Silent Co-Presence | 25min sessions with other users. No talking. Just mutual presence + focus. | 3 | 4 | 4 | 3.0 |
| C14 | Nearby Focus Friends | See who's nearby also fighting. Wave for walking buddy. No algorithms. | 3 | 4 | 4 | 3.0 |
| C15 | Group Uninstall Approval | Can't uninstall without group members voting to allow it. Social lock. | 5 | 3 | 4 | 6.7 |
| C16 | The Confession Call | After 30min in distraction, auto-dials random contact. Explain yourself or hang up in shame. | 3 | 2 | 4 | 6.0 |

---

## Category D: Gamification & Rewards

| # | Feature | Description | Impact | Effort | Feasibility | Priority |
|---|---------|-------------|--------|--------|-------------|----------|
| D1 | Boss Battles | Daily boss with health bar. Distractions feed it. 24hr clean = defeat. Always returns. | 3 | 3 | 5 | 5.0 |
| D2 | Streak Funeral | Breaking streak = animated funeral pyre. Watch days burn away. | 3 | 2 | 5 | 7.5 |
| D3 | Compound Time Bank | Every avoided minute banks with compound interest. Monthly "earnings" report. | 4 | 2 | 5 | 10.0 |
| D4 | Phone RPG | Apps = dungeons. Real tasks = quests for XP. Earn app access through gameplay. | 3 | 4 | 5 | 3.75 |
| D5 | The Applause | Put phone DOWN after short use → speaker plays applause. Pavlov for quitting. | 4 | 1 | 5 | 20.0 |
| D6 | Micro-Trivia | 5-second trivia on unlock. Fill dopamine slot with knowledge. Streak tracked. | 3 | 2 | 5 | 7.5 |
| D7 | Kindness Tracker | Track acts of kindness. Running total on home screen. Identity shift to "doer." | 3 | 2 | 5 | 7.5 |
| D8 | Escape Velocity | You = rocket. Distractions = gravity. Visual metaphor for daily progress. | 3 | 3 | 5 | 5.0 |
| D9 | Leaderboard Rankings | Compared against community. "Rank: 847/10,000" — nobody wants to be bottom half. | 4 | 3 | 5 | 6.7 |

---

## Category E: Data & Awareness Mechanics

| # | Feature | Description | Impact | Effort | Feasibility | Priority |
|---|---------|-------------|--------|--------|-------------|----------|
| E1 | Harsh Unlock Summary | AI on unlock: "4.3hrs Instagram. What did you accomplish?" | 4 | 2 | 5 | 10.0 |
| E2 | Pattern Detection | "38 of 47 failures between 2-4pm. Always after Sarah's notification." | 5 | 3 | 5 | 8.3 |
| E3 | The Witness | AI watches 2 weeks silently. Delivers ONE devastating insight about your deepest pattern. | 5 | 4 | 5 | 6.25 |
| E4 | Addiction Invoice | Itemized bill of every digital hit. Running total. | 4 | 2 | 5 | 10.0 |
| E5 | Comparative Ranking | "Slightly better than people killing their careers faster than you." | 2 | 1 | 5 | 10.0 |
| E6 | Lifespan Projection | "4.7 years of life on home screen by 2034 at current rate." | 4 | 2 | 5 | 10.0 |
| E7 | Intervention Screen | Full-screen after 14 unlocks: "This is attempt #14. We're worried. Phone is not your friend." | 4 | 1 | 5 | 20.0 |
| E8 | Self-Audit | Weekly: "I said I use Reddit for news." Reality: "67 opens for same 3 headlines." | 4 | 3 | 5 | 6.7 |
| E9 | Algorithm Exposer | Overlay showing WHY content was shown: "You lingered on similar posts 3x this week." | 3 | 5 | 2 | 1.2 |
| E10 | Attention Auction | Apps "bid" for your time with disclosed value. "YouTube offering: 1 fact per 4min ads. Accept?" | 3 | 4 | 3 | 2.25 |
| E11 | Data Mirror | Shows what data each app collected TODAY. "47 face scans, 12 location pings." Disgust as deterrent. | 3 | 4 | 2 | 1.5 |
| E12 | Manipulation Heatmap | Red zones on app UIs showing dark pattern locations. Thermal vision for exploitation. | 3 | 5 | 2 | 1.2 |
| E13 | Phantom Vibration Counter | Detects phone checks without notification. "34 phantom checks today. Nervous system hijacked." | 4 | 2 | 5 | 10.0 |
| E14 | Time Capsule Comparison | Monthly snapshots. "34% worse than yourself 6 months ago." Compete against best self. | 4 | 2 | 5 | 10.0 |
| E15 | Cringe Archive | Front camera screenshots during mindless scrolling. Weekly dead-eyed montage. | 3 | 2 | 4 | 6.0 |
| E16 | Weekly Lie Detector | Cross-reference stated goals vs actual behavior. Expose self-deception. | 4 | 3 | 5 | 6.7 |

---

## Category F: Biometric & Hardware Integration

| # | Feature | Description | Impact | Effort | Feasibility | Priority |
|---|---------|-------------|--------|--------|-------------|----------|
| F1 | Heartrate Gate | Elevated HR (dopamine anticipation) via watch = phone locked. Only opens when calm. | 4 | 3 | 3 | 4.0 |
| F2 | Breath to Unlock | 4-7-8 breathing verified by mic. 16 seconds minimum every unlock. | 4 | 2 | 4 | 8.0 |
| F3 | Posture Lock | Accelerometer detects toilet/lying posture. Blocks entertainment in those positions. | 3 | 2 | 4 | 6.0 |
| F4 | Physical NFC Key | Distraction apps only unlock when phone touches NFC tag on desk. Forces physical journey. | 4 | 2 | 5 | 10.0 |
| F5 | Somatic Feedback | Watch vibrates with increasing intensity as time limits approach. Body learns boundaries. | 4 | 2 | 3 | 6.0 |
| F6 | Sleep Debt Collector | Slept 5hrs? Phone restricts based on cognitive impairment level. "60% capacity. Netflix denied." | 4 | 3 | 3 | 4.0 |
| F7 | Pupil Dilation Detection | Front camera monitors pupil size. Dilated = dopamine = screen goes black mid-scroll. | 3 | 5 | 1 | 0.6 |
| F8 | Hydration Bouncer | Must log water intake to earn screen time. Smart bottle integration. | 2 | 3 | 3 | 2.0 |
| F9 | Cortisol Calendar | Tracks stress patterns. Preemptively locks comfort-scroll apps during high-stress times. | 4 | 3 | 3 | 4.0 |

---

## Category G: Financial Stakes

| # | Feature | Description | Impact | Effort | Feasibility | Priority |
|---|---------|-------------|--------|--------|-------------|----------|
| G1 | Money Stake | Deposit money. Fail commitment? Donated to cause you hate. Complete = refund + bonus. | 5 | 4 | 4 | 5.0 |
| G2 | Streak Insurance | "47-day streak worth $94 in rewards. Uninstall = forfeit all." | 4 | 3 | 5 | 6.7 |
| G3 | The Bond System | Friends pool money. Anyone uninstalls before date = loses share to survivors. | 4 | 4 | 4 | 4.0 |
| G4 | Micro-Investment Lock | Focused hours auto-invest $0.50 to brokerage. Uninstall = stops passive income. | 4 | 4 | 3 | 3.0 |
| G5 | Friend Betting | Friends bet on your streak. You break = they profit financially. | 4 | 4 | 3 | 3.0 |

---

## Category H: Absurdist & Experimental

| # | Feature | Description | Impact | Effort | Feasibility | Priority |
|---|---------|-------------|--------|--------|-------------|----------|
| H1 | Kazoo Mode | Speaker plays kazoo in restricted apps. Can't mute. Public embarrassment. | 2 | 1 | 5 | 10.0 |
| H2 | The Narrator | TTS narrates actions: "He opened Twitter. Again. The 14th time." Out loud. | 3 | 2 | 5 | 7.5 |
| H3 | Reverse Scrolling Day | Random days, scroll directions invert. Muscle memory destroyed. | 2 | 3 | 3 | 2.0 |
| H4 | The Shrinking App | Icon gets 5% smaller each open. After 20 opens = 1 pixel. | 3 | 2 | 4 | 6.0 |
| H5 | Notification Archaeology | Notifications "buried." Must "excavate" at scheduled times. Some are decoys. | 3 | 3 | 4 | 4.0 |
| H6 | The Uncanny Valley | AI subtly distorts faces in feeds by 2-3%. Subconscious discomfort. | 2 | 5 | 1 | 0.4 |
| H7 | Time Dilation UI | Clock runs 3x faster in distraction apps. Feel like you've been there forever. | 3 | 1 | 5 | 15.0 |
| H8 | Voice Authenticity Gate | Must say aloud: "I choose distraction over goals." Designed to be unsayable. | 4 | 2 | 4 | 8.0 |
| H9 | Motor Skill Gauntlet | Uninstall requires beating impossible phone game. Rage state = can't complete. | 3 | 2 | 4 | 6.0 |
| H10 | Addiction Confession Podcast | Weekly AI audio summary of worst patterns. Auto-plays Monday morning. | 3 | 3 | 5 | 5.0 |

---

## Summary Stats

- **Total features cataloged**: 100
- **Categories**: 8
- **Top Priority (≥15.0)**: A2, A14, A26, B7, B10, B13, B17, B22, D5, E7, H7
- **High Priority (10.0-14.9)**: A1, A15, A17, A18, A19, A21, A23, B2, B3, B4, B6, B11, C10, D3, E1, E4, E6, E13, E14, F4, H1
