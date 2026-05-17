# FocusLauncher — Anti-Uninstall Mechanisms

## The Core Problem

Android gives users full control over uninstalling apps. No app can truly prevent its own removal. But we CAN:

1. Add massive friction to the uninstall process
2. Make uninstalling feel like a loss (social, financial, identity)
3. Make the app so integrated that removing it breaks things the user cares about

---

## Technical Reality on Android

| Mechanism | What It Does | Android Support |
|---|---|---|
| Device Admin API | Greys out uninstall button until admin is deactivated | ✅ Full support |
| `onDisableRequested()` | Show warning string when user tries to deactivate admin | ✅ Returns custom string |
| Accessibility Service | Can detect uninstall intent in settings | ⚠️ Fragile, Google may reject |
| Profile Owner | Full device control (enterprise/MDM level) | ✅ But requires managed provisioning |
| App pinning | Locks device to single app | ✅ But user can exit with gesture |

**Best approach: Device Admin + in-app deactivation flow with social/financial gates**

---

## Mechanism Catalog

### Tier 1: Technical Friction (Android-native)

| # | Mechanism | Description | Impact | Effort | Feasibility | Priority |
|---|-----------|-------------|--------|--------|-------------|----------|
| U1 | Device Admin Registration | Grey out uninstall. Requires 6-7 tap journey through scary system menus to deactivate. | 5 | 1 | 5 | 25.0 |
| U2 | In-App Deactivation Flow | User must go through YOUR app's UI to begin uninstall process. You control the experience. | 5 | 2 | 5 | 12.5 |
| U3 | 72-Hour Cooldown | In-app countdown with daily "still want to quit?" check-ins. App deactivates admin after 72hrs. | 5 | 2 | 5 | 12.5 |
| U4 | Nuclear Passphrase | 20+ char passphrase set on install, written on paper, stored inconveniently. Required to begin uninstall. | 4 | 1 | 5 | 20.0 |
| U5 | Motor Skill Gauntlet | Complete impossible mini-game to proceed. Rage state = can't do it. Calm = don't want to anymore. | 3 | 2 | 5 | 7.5 |

### Tier 2: Social Locks

| # | Mechanism | Description | Impact | Effort | Feasibility | Priority |
|---|-----------|-------------|--------|--------|-------------|----------|
| U6 | Group Uninstall Vote | Group members must vote to ALLOW your uninstall. Majority required. | 5 | 3 | 5 | 8.3 |
| U7 | Public Shame Post | Uninstall triggers auto-post: "erluxman quit after 12 days 🏳️" to community/social. | 4 | 2 | 5 | 10.0 |
| U8 | Team Streak Kill | You uninstall = everyone in your squad loses their streak. 4 angry texts incoming. | 5 | 3 | 5 | 8.3 |
| U9 | Sponsor Notification | Uninstall attempt notifies your sponsor who calls you. Human intervention. | 5 | 2 | 4 | 10.0 |
| U10 | The Accountability Pact | If you uninstall, your paired friend's restrictions LOOSEN. You're sabotaging them. | 5 | 3 | 5 | 8.3 |
| U11 | Public Commitment Contract | Written commitment visible to all. Uninstall = visible breach of own word. | 4 | 2 | 5 | 10.0 |

### Tier 3: Financial Stakes

| # | Mechanism | Description | Impact | Effort | Feasibility | Priority |
|---|-----------|-------------|--------|--------|-------------|----------|
| U12 | Escrow Deposit | $50-500 deposited. Uninstall before commitment period = donated to hated cause. | 5 | 4 | 4 | 5.0 |
| U13 | Streak Value Display | "Your 47-day streak = $94 in rewards. Uninstall = forfeit." On the uninstall screen. | 4 | 2 | 5 | 10.0 |
| U14 | Group Money Pool | Friends pool money. Quitter loses share to survivors. | 4 | 4 | 4 | 4.0 |
| U15 | Passive Income Loss | "You've earned $127 this quarter via focused-hour micro-investments. Uninstall = stops." | 4 | 4 | 3 | 3.0 |

### Tier 4: Psychological Deterrents

| # | Mechanism | Description | Impact | Effort | Feasibility | Priority |
|---|-----------|-------------|--------|--------|-------------|----------|
| U16 | Exit Interview From Hell | 15 brutally honest questions: "Which app will you binge?" "Write who you're letting down." | 4 | 2 | 5 | 10.0 |
| U17 | Future Self Video | Pre-recorded video of yourself at your strongest. Plays full. Can't skip. | 5 | 1 | 5 | 25.0 |
| U18 | Relapse Prediction | "94% of users who uninstalled at day 12 reinstalled within 9 days. Save yourself the spiral?" | 4 | 2 | 5 | 10.0 |
| U19 | Graduated Freedom | Restrictions ease over time. 30d=lighter, 60d=near-invisible. No REASON to uninstall. | 5 | 2 | 5 | 12.5 |
| U20 | Voice Confession | Must say aloud: "I am choosing distraction. I am too weak to continue." Psychologically unsayable. | 4 | 2 | 4 | 8.0 |

---

## Recommended Implementation Stack

**Layer 1 (Always active):**
- U1: Device Admin — technical barrier
- U2 + U3: In-app flow with 72hr cooldown — time barrier
- U17: Future self video — emotional barrier

**Layer 2 (If user is in a group):**
- U6: Group vote required — social barrier
- U8: Team streak consequences — guilt barrier
- U10: Accountability pact — responsibility barrier

**Layer 3 (If user opted into financial stakes):**
- U12/U13: Money at risk — loss aversion barrier

**The chain:** Device Admin blocks instant uninstall → User must open app → App plays future self video → Requires passphrase → Starts 72hr countdown → Notifies group → Group votes → If approved after 72hrs → Admin deactivated → User can finally uninstall.

**Result:** 5-7 barriers between impulse and action. Impulses last ~20 minutes. This chain takes 72+ hours minimum.

---

## Legal & Ethical Considerations

- All mechanisms must be OPTED INTO during onboarding when user is rational
- User must always have a technical escape hatch (ADB uninstall, factory reset)
- Financial stakes must comply with gambling/escrow regulations per jurisdiction
- Google Play policy: Device Admin apps face extra review. Must clearly disclose.
- GDPR: User data deletion must be possible even if app retention mechanisms exist
