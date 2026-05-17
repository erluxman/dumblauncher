# FocusLauncher — Open Decisions

## Decisions To Make Before Coding

These need YOUR input. Each decision shapes architecture and feature selection.

---

### D1: Which features make the MVP?

Top priority features (scored ≥15.0):

| # | Feature | Priority Score |
|---|---------|---------------|
| A2 | The Lobby (wait timer) | 25.0 |
| U1 | Device Admin (anti-uninstall) | 25.0 |
| U17 | Future Self Video (on uninstall) | 25.0 |
| A14 | Variable Ratio Punishment | 20.0 |
| A26 | Boredom Preservatory | 20.0 |
| B7 | Identity Voting | 20.0 |
| B10 | Mirror Widget (camera wallpaper) | 20.0 |
| D5 | The Applause (reward for stopping) | 20.0 |
| E7 | Intervention Screen (14 unlocks) | 20.0 |
| U4 | Nuclear Passphrase | 20.0 |
| B13 | The Last Day Test | 15.0 |
| B17 | Anchoring Attack | 15.0 |
| B22 | App Tombstones | 15.0 |
| H7 | Time Dilation UI | 15.0 |

**Question:** Pick 5-8 of these for MVP. Which ones define the core experience?

---

### D2: Group features — how central?

Options:
- **A) Solo-first** — App works fully alone. Groups are a bonus feature added later.
- **B) Group-required** — App REQUIRES a group to function. No solo mode. Forces social accountability.
- **C) Hybrid** — Solo mode exists but app constantly nudges toward forming/joining groups.

**Question:** Which philosophy?

---

### D3: Platform & distribution

Options:
- **A) Play Store only** — Limited permissions, wider reach, Google review risk
- **B) Sideload only** — Full power, niche audience, harder distribution
- **C) Play Store (lite) + sideload (full)** — Best of both, two versions to maintain
- **D) Play Store + progressive unlock** — Start safe, user grants escalating permissions over time

**Question:** Which path?

---

### D4: Tech stack

Options:
- **A) Kotlin + Jetpack Compose** — Native Android, best performance, best API access
- **B) Kotlin Multiplatform** — Share logic with potential iOS version later
- **C) Flutter** — Faster UI dev, but launcher APIs need platform channels anyway

**Recommendation:** A (Kotlin + Compose). Launcher is deeply Android-native. Cross-platform adds complexity for no near-term gain.

**Question:** Agree?

---

### D5: Backend for group features

Options:
- **A) Firebase** — Fast to ship, Google ecosystem, free tier generous
- **B) Supabase** — Open source, Postgres, better for complex queries
- **C) Custom (Ktor/Spring)** — Full control, more work
- **D) No backend for MVP** — Local-only, groups come in V1

**Question:** Which one?

---

### D6: Monetization model

Options:
- **A) Freemium** — Basic restrictions free, advanced (AI, groups, financial) paid
- **B) One-time purchase** — Pay once, full access forever
- **C) Open source + donations** — Community-driven
- **D) Subscription** — Monthly/yearly for premium features

**Question:** Which aligns with your vision?

---

### D7: Tone/personality of the app

Options:
- **A) Brutal drill sergeant** — Harsh, confrontational, no mercy
- **B) Calm therapist** — Gentle but firm, empathetic, non-judgmental data
- **C) Witty friend** — Humorous, sarcastic, makes you laugh at yourself
- **D) Silent monk** — Minimal words. Just numbers, timers, and consequences. No personality.

**Question:** Which voice?

---

### D8: How aggressive on first install?

Options:
- **A) Full restriction immediately** — Shock therapy. All features on day 1.
- **B) Gradual ramp-up** — Easy week 1, progressively stricter. Boiling frog.
- **C) User chooses intensity** — Slider from "gentle" to "nuclear" during onboarding.
- **D) AI calibrated** — Watches behavior for 3 days, then recommends restriction level.

**Question:** Which onboarding?

---

### D9: Name & branding

Current: "FocusLauncher"
Alternatives to consider:
- Cage
- Lockdown
- Monk Mode
- The Warden
- Friction
- Dead Zone
- Cold Turkey Launcher
- Ascetic
- The Lobby

**Question:** Final name preference?

---

## Next Steps After Decisions

1. Finalize MVP feature list based on D1
2. Set up project structure based on D4
3. Build home screen + launcher basics
4. Implement top 3-5 restriction mechanics
5. Add Device Admin + anti-uninstall chain
6. Internal testing / dogfooding
7. Group features (if D2 says early)
8. Beta release
