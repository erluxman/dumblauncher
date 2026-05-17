# Gap Analysis — Devil's Advocate Audit of `09-features-master.json`

> Date: 2026-05-17
> Auditor's posture: skeptic playing devil's advocate against the 135-feature spec.
> Goal: surface what's incomplete, naive, ethically wobbly, or strategically under-built — *before* we start writing code.

The current spec is impressive in surface area but has predictable blind spots. This document catalogues them and proposes new features to file in `09-features-master.json` (see `GAP-*` entries appended at the end of that file).

---

## A. The "you forgot users are humans" gaps

### A1. No onboarding plan
135 features and zero of them describe the first 30 minutes. That window decides retention. `TRACK-004` references a "3-day baseline observation" but nobody has designed the actual first-run flow: permissions, mantra capture, future-self video recording, baseline observation, first track selection, VIP contact picking, partner invite.
**→ Add feature: `ONBOARD-001` First-Run Sequencer.**

### A2. No "I had a bad day" graceful exit
Sick? Grieving? Travelling? Period pain? Today the app punishes you the same. Need a **Grace Day** (1–2/month, declared in advance, no penalty), otherwise people uninstall during life crises rather than the system absorbing the shock.
**→ Add feature: `GRACE-001` Grace Day System.**

### A3. No streak insurance
A 200-day streak dies on one missed day. Duolingo solved this years ago with earnable streak freezes. Without it, the system is brittle and breeds catastrophic loss aversion.
**→ Add feature: `GRACE-002` Streak Freeze (Earnable).**

### A4. No "graduation" / off-ramp
`UNINSTALL-007` hints at "graduated freedom" but there is no real Year-2, Year-3 model. What does the *successful* user's phone look like? Or is this an infinite-engagement trap dressed as discipline?
**→ Add feature: `LIFECYCLE-001` Graduate Mode.**

### A5. No relapse-recovery framing
The 5-state behavior meter shows "Drowning" but offers no compassionate path back up. `SOCIAL-008` (sponsor) exists but no flow. After a fall, the spec is silent.
**→ Add feature: `RECOVERY-001` After-Fall Ritual.**

### A6. No mental-health red flag detection
3am scrolling × 14 days + zero todos + low motion = likely depressive episode. The app should *soften*, not escalate. Right now Sad Self (`SAD-001`) could harm someone in a bad place. This is also a legal risk.
**→ Add feature: `SAFETY-001` Depression/Crisis Detection & Soften Mode.**

---

## B. The "the data is one-dimensional" gaps

### B1. No "good consumption" category
Kindle, audiobooks, documentaries, Duolingo, language podcasts should be **encouraged**, not lumped with TikTok. `PROD-011` input/output is too binary.
**→ Add feature: `PROD-016` Nourishing Consumption Category.**

### B2. No mood/emotion log paired with usage
We track productivity obsessively, emotional context zero. The useful insight isn't "you scrolled 4hrs" — it's "you scrolled 4hrs every time you felt anxious about a code review." Two taps/day, paired with stats.
**→ Add feature: `PROD-017` Mood Pings (2x daily emotion check).**

### B3. No menstrual / biological cycle awareness
~50% of users have a cycle that radically changes energy and focus. Ignoring this is engineering blindness and an obvious differentiator vs. existing apps.
**→ Add feature: `PROD-018` Cycle / Energy-Window Awareness.**

### B4. Missing context modes
`RESTRICT-010` (geofence) covers gym/desk/bed but not the obvious life states: **commute, travel, sick, vacation, weekend**. Each needs its own rule set or temporary opt-out.
**→ Add feature: `RESTRICT-022` Life-State Mode Set.**

### B5. No "boring app" mode
Why all-or-nothing? Let user open Instagram in **text-only-grayscale-no-Reels** mode for 5 min. Better than 0 or full dopamine.
**→ Add feature: `RESTRICT-023` Lite Mode for Distraction Apps (text/gray only).**

### B6. No content-level blocking inside allowed apps
Twitter for tech threads, not politics. `RESTRICT-020` VPN-based domain filtering doesn't cover this.
**→ Add feature: `RESTRICT-024` Topic-Level Filter (LLM-classified content gating).**

---

## C. The "you forgot who else exists" gaps

### C1. No partner/spouse mode
Couples are a huge segment. Shared screen-time goals, shared time-saved, "we're both off our phones at dinner" tracking, "I'm proud of you" notifications from one specific person.
**→ Add feature: `COUPLES-001` Partner Pair.**

### C2. No parent/kid mode
Most-installed reason for any focus app. Nothing in the spec about supervising a child's phone via parent's app.
**→ Add feature: `FAMILY-001` Parent/Child Pair.**

### C3. No therapist/coach view
A real ADHD coach or behavioral therapist would pay to see anonymized client patterns. Huge B2B angle missed.
**→ Add feature: `B2B-001` Therapist/Coach Dashboard.**

### C4. No team/manager view
"My engineering team is in deep work 9–11" team-level dashboard. Sells to companies.
**→ Add feature: `B2B-002` Team Focus Dashboard.**

### C5. No accessibility considerations
Blind users can't see overlays. Deaf users can't hear kazoo. Motor-impaired users can't do voice mantras. The spec implicitly assumes able-bodied.
**→ Add feature: `A11Y-001` Accessibility Pass (audio/visual/motor alternatives across all features).**

---

## D. The "this feature has a hidden hole" gaps

### D1. Mantra Gate (`MANTRA-001`) embarrasses you in meetings
Whisper mode (`MANTRA-003`) is listed but undersold. It should auto-activate via mic ambient detection.
**→ Update: extend `MANTRA-003` with auto-whisper detection.**

### D2. Sad Self (`SAD-001`) will cause some users to spiral
A depressed user reading "future-you is disappointed" 8x/day is a lawsuit. `SAD-005` backs off on ignore but doesn't detect *harm*. Pair with `SAFETY-001`.

### D3. Variable-ratio punishment (`RESTRICT-004`) is dark-patterning the user "for their own good"
Still a dark pattern. Need an ethics spec / opt-out / transparency note.
**→ Add feature: `ETHICS-001` Manipulation Transparency Page (settings menu showing every psychological technique in use).**

### D4. Future Self Video (`PSYCH-006`) records one moment in time
What if your values change? No update cadence.
**→ Update: add 90-day refresh prompt to `PSYCH-006`.**

### D5. App-unlock-via-todos (`PROD-001`) is gameable
"Brush teeth ✓ unlock TikTok." Verification needs teeth.
**→ Add feature: `PROD-019` Companion Verification (peer-confirms task completion).**

### D6. Anti-uninstall stack has no "lost phone" recovery
Passphrase on paper at home + travelling + bricked phone = stuck.
**→ Add feature: `RECOVERY-002` Account Recovery via Trusted Contacts (2-of-3 vouches).**

### D7. No backup/restore
New phone day = lose 400-day streak, all tracks, all journal. Catastrophic.
**→ Add feature: `BACKUP-001` Encrypted Cloud Backup & Device Migration.**

### D8. No data export
GDPR aside, users want journal as PDF, stats as CSV.
**→ Add feature: `BACKUP-002` Data Export (journal PDF, stats CSV, photo book).**

---

## E. The "you're missing whole product surfaces" gaps

### E1. No "Year in Review" / Wrapped
Spotify proved this is viral. We have ALL the data. Built once a year, drives massive install spikes every January.
**→ Add feature: `SOCIAL-010` Built Wrapped (annual recap).**

### E2. No physical artifact
Print my year-end journal. Mail me a poster of my heatmap. Real anchor. Etsy-tier merch.
**→ Add feature: `MERCH-001` Print Shop (heatmap poster, year journal, NFC desk puck).**

### E3. No marketplace
Tracks designed by Cal Newport / Andrew Huberman / James Clear. Buy/share/clone.
**→ Add feature: `CREATOR-001` Track Marketplace.**

### E4. No public profile / portfolio page
"Look at what I built this year." LinkedIn for makers of their own life.
**→ Add feature: `SOCIAL-009` Public Builder Profile.**

### E5. No proof-of-discipline credential
Verifiable badge an employer/client could check.
**→ Add feature: `SOCIAL-011` "Built" Verification Badge.**

### E6. No companion physical hardware product line
`HARDWARE-001` smart NFC tag exists but a branded "focus puck" you slap on your desk could be a $40 product. Adjacent to `MERCH-001`.

### E7. No web / desktop companion
Phone is the launcher but I work on a laptop. Right now all data dies at the phone boundary.
**→ Add feature: `PLATFORM-001` Web/Desktop Companion (read-only stats + journal + todo view).**

---

## F. The "social tier is a stub" gap

The existing `SOCIAL-001`–`SOCIAL-008` is 8 features. It's clearly a placeholder. **Group voting on uninstall** is a single mechanic — not a social product. There's no feed, no profile, no follows, no DMs, no stories, no presence, no reactions, no discover, no creator economy, no virality loop.

Devil's-advocate verdict: **The spec treats social as "nice to have" when it's the only thing that will get this app from 1k users to 10M users.** A discipline tool is private; a discipline *movement* is social.

See `11-social-features.md` for the full social brainstorm. The highest-leverage social additions are filed in `09-features-master.json` as `SOCIAL-009` through `SOCIAL-040`.

---

## Priority summary — what to file first

If we add nothing else, these 12 entries plug the most embarrassing holes:

1. `ONBOARD-001` First-run sequencer
2. `GRACE-001` Grace Day
3. `GRACE-002` Streak Freeze
4. `SAFETY-001` Crisis detection / soften mode
5. `BACKUP-001` Encrypted backup
6. `BACKUP-002` Data export
7. `PROD-017` Mood Pings
8. `PROD-016` Nourishing Consumption category
9. `COUPLES-001` Partner Pair
10. `SOCIAL-009` Public Builder Profile
11. `SOCIAL-010` Built Wrapped
12. `SOCIAL-011` Built Verification

All twelve are filed in `09-features-master.json` along with the rest.
