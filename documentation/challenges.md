# Challenges — Everything that does NOT qualify for `project-plan.md` or `features-by-stage.json`

> Synthesized from `../docs/00-vision.md` through `../docs/13-naming-and-positioning.md`.
> Last updated: 2026-05-17.

The project plan and features list capture *what we ship*. This file captures *what gets in the way of shipping it*: impossible features, open decisions, ethical hazards, legal exposure, market risks, platform politics, naming gates, philosophy tensions, accessibility gaps, parking-lot ideas, and the unanswered questions inside individual features.

If something here is resolved, promote it to a feature or a planning section. If it stays here, it's a known constraint we ship around.

---

## 1. Technically impossible features (Android sandbox / OS limits)

From `../docs/05-impossible-features.md` and `IMPOSSIBLE-001..018` in the master JSON. None of these ship — we ship the workarounds, which live in the feature list.

| ID | Wanted | Why impossible | Best workaround (in spec) |
|----|--------|----------------|---------------------------|
| IMPOSSIBLE-001 | Modify other apps' internal UI (slow scroll, pixelate, distort) | Android app sandbox. Cannot inject into another app's rendering pipeline. | RESTRICT-003 (dimming overlay), RESTRICT-021 (bandwidth throttling) |
| IMPOSSIBLE-002 | Truly prevent uninstallation | User always has ADB / factory reset / Safe Mode | UNINSTALL-001…007 chain + SOCIAL-002 voting + FINANCE-001 stake |
| IMPOSSIBLE-003 | Read other apps' internal content/data | Sandbox isolation | Accessibility text reading, OAuth API integrations, VPN DNS, built-in browser |
| IMPOSSIBLE-004 | Replace system clock in status bar | Status bar is system UI | Custom widget on home (we own it), persistent notification, WearOS face |
| IMPOSSIBLE-005 | Force-close foreground apps | `forceStopPackage` is system-only | Accessibility `GLOBAL_ACTION_HOME` + overlay blockade + `clearApplicationUserData` (nuclear) |
| IMPOSSIBLE-006 | Detect pupil dilation / micro-expressions | No IR, unreliable ML, battery-killing | HARDWARE-003 (WearOS HR), typing-pattern proxy, usage-pattern proxy, PROD-017 mood pings |
| IMPOSSIBLE-007 | Modify scroll behavior in other apps (infinite → finite) | Cannot inject into other apps' UI | RESTRICT-020 VPN content cut, RESTRICT-023 Lite Mode, overlay progressive cover |
| IMPOSSIBLE-008 | Silently reinstall self after uninstall | Silent install requires Device Owner / MDM | Companion app detect + group push, Device Owner provisioning ("focus phone") |
| IMPOSSIBLE-009 | Intercept app launch from outside our launcher | No launch interceptor API | We ARE the launcher (covers 80%); Accessibility for the rest |
| IMPOSSIBLE-010 | Selectively control system DND for other apps | Cannot prevent notification *generation* | NotificationListener auto-dismiss + DND-whitelist + batch re-deliver (RESTRICT-015) |
| IMPOSSIBLE-011 | Persist system settings against user override | No API to lock a setting | Monitor + re-apply loop, overlay-based alternatives (grayscale-overlay vs system grayscale) |
| IMPOSSIBLE-012 | Block app installation (prevent bypass tools) | PACKAGE_ADDED is post-install | Detect + shame to group, Device Owner whitelist (extreme), Play Store overlay |
| IMPOSSIBLE-013 | Persistent background monitoring on iOS | iOS hostile to background work | FamilyControls (gated by Apple), widget-only iOS — accept iOS is a worse product |
| IMPOSSIBLE-014 | Replace home screen on iOS | No third-party launchers on iOS | Widget-based experience, Focus Modes, Shortcuts — companion-only on iOS |
| IMPOSSIBLE-015 | Detect exact content consumed (videos, posts) | App sandbox | OAuth API (YouTube/Reddit), VPN DNS/SNI, built-in browser, Accessibility text |
| IMPOSSIBLE-016 | Play audio OVER other apps reliably | Audio focus is exclusive | Notification sound channel, USAGE_ALARM, duck-and-play, media-pause-and-narrate |
| IMPOSSIBLE-017 | Algorithm/feed manipulation exposure | Algorithms are server-side, opaque | Generic dark-pattern education overlays, usage-based inference |
| IMPOSSIBLE-018 | Heartrate / stress gate from phone alone | No passive HR/stress sensors in phones | HARDWARE-003 WearOS, typing/unlock-frequency proxies, PROD-017 mood pings |

---

## 2. Open decisions (must land before MVP code freeze)

From `../docs/04-decisions-needed.md`. Each shapes architecture and feature selection. None of these can stay "TBD" past Week 0.

1. **D1 — MVP feature shortlist.** Plan proposes 20 stage-1 features. Final cut needs confirmation.
2. **D2 — Group philosophy.** Solo-first vs Group-required vs Hybrid. Affects whether backend ships in MVP.
3. **D3 — Distribution.** Play Store (lite) + sideload (full extreme) is recommended, but pick one or both.
4. **D4 — Tech stack.** Kotlin + Compose is recommended; KMP or Flutter still on the table.
5. **D5 — Backend.** Supabase recommended over Firebase or custom. No backend at all is an option for MVP.
6. **D6 — Monetization.** Freemium / one-time / OSS+donations / subscription. Plus: never ads, never sell data.
7. **D7 — Tone.** Drill sergeant / therapist / witty / monk. Bleeds into every copy string.
8. **D8 — Day-1 aggressiveness.** Shock therapy / gradual ramp / user-chosen slider / AI-calibrated. Recommended: gradual via Track-Level-1.
9. **D9 — Final product name.** "FocusLauncher" vs "Built" vs alternatives (see §6).

---

## 3. Ethical & psychological hazards

These are the things that could harm users or get us sued, broken out from `../docs/02-anti-uninstall.md`, `../docs/07-sad-self`, `../docs/10-gap-analysis.md` §A6/D2/D3, and individual feature notes.

### 3.1. Sad Self could hurt depressed users
- `SAD-001` sends future-self-disappointed notifications. A depressed user reading "future-you is disappointed" 8x/day is a potential lawsuit.
- Mitigation in spec: `SAFETY-001` Crisis Detection & Soften Mode (V1) auto-pauses Sad Self and surfaces help when patterns suggest crisis. `SAD-005` already backs off on ignore.
- Open question: what's the *false-positive* tolerance for SAFETY-001? Over-soften and the product breaks for healthy users; under-soften and we miss the people who need it.

### 3.2. We dark-pattern users "for their own good"
- `RESTRICT-004` Variable Ratio Punishment, `PSYCH-013` Anchoring Attack, `ABSURD-004` Voice Authenticity Gate are textbook dark patterns aimed at the *right* outcome.
- Mitigation: `ETHICS-001` Manipulation Transparency Page (MVP) — every technique listed, each individually toggleable.
- Open question: do we *disable* by default and let users opt in, or default on and let them opt out? Behavior change demands defaults; ethics demands consent.

### 3.3. Body-image harm
- `SOCIAL-032` Brutal Mirror Filter visibly ages/bloats your face. Walks the body-image-harm line.
- Marked V3, opt-in, must pair with `SAFETY-001`. Could be the "launch press" feature *or* the "we got pulled from the store" feature. Land legal review before ship.
- Same risk class: `SOCIAL-015` Aura Avatar (foggy/sharp), `FIT-007` Form Check Video, `IDENT-001` Daily Selfie Timelapse.

### 3.4. Sleep deprivation feedback loops
- `SOCIAL-037` Late Night Crew could encourage harmful sleep deprivation by giving grinders a social home at 3am.
- `SLEEP-003` Sleep Window Guardrails + `RESTRICT-011` Dream Mode mitigate, but Late Night Crew specifically opposes them.
- Mark explicitly "must not encourage harm" in product copy; auto-disable when SAFETY-001 fires.

### 3.5. Anti-uninstall is itself an ethical hazard
- The whole point of Tier 1 (Device Admin) + Tier 2 (Social Locks) + Tier 3 (Financial Stakes) is making leaving painful.
- Must always have a **technical escape hatch** (ADB / factory reset) — never claim "you cannot remove this."
- Onboarding must capture consent *while the user is rational*, not at 1am during a relapse.
- All financial-stake features must comply with gambling/escrow law per jurisdiction. Plain refunds (no donation-to-cause-you-hate) might be the only legal route in some places.

### 3.6. The Confession Call (`ABSURD-005`) auto-dials random contact after 30min in a distraction app
- One typo away from a 3am call to your boss. Opt-in extreme mode only, with hard-blocklist of work/family contacts.

---

## 4. Legal & regulatory exposure

| Risk | Source | Mitigation |
|------|--------|------------|
| GDPR right-to-delete vs anti-uninstall retention | `../docs/02-anti-uninstall.md` §"Legal & Ethical" | `BACKUP-002` data export + always-honor delete request even if app retention mechanisms exist |
| HIPAA / health data when storing HealthConnect output | `INTEG-001`, `MIND-001`, `FIT-005` | On-device-only by default; E2EE backup; never share without explicit per-scope consent |
| Financial regulation on escrow (`FINANCE-001`) | `../docs/02-anti-uninstall.md` §"Legal" | Gambling-law compliance per jurisdiction; partner with regulated escrow provider; refunds-only fallback |
| Play Store policy on Device Admin apps | `../docs/03-technical-feasibility.md` §"Risks" | Two-app strategy: Play-Store-compliant core + sideload extreme; open-source for trust |
| Play Store policy on Accessibility Service for non-accessibility use | Same | Justify clearly in listing + design overlay-only fallback path |
| Apple App Store: virtually unbuildable on iOS | `../docs/05-impossible-features.md` §"Key Insight" | Treat iOS as companion-only; do not promise feature parity |
| Minor consent / parental control law for `FAMILY-001` | `../docs/11-social-features.md` §11 | Android Family Link integration; jurisdiction-specific age gates |
| Therapist B2B (`B2B-001`) crosses into clinical software territory | Devil's-advocate audit §C3 | Position as "between-session journal", not "treatment"; medical-device classification check |
| AR face-shaming filter (`SOCIAL-032`) potential cosmetic-claims liability | §3.3 above | Disclaim, soft-touch defaults, locale-specific suppression |

---

## 5. Platform & market risks

From `../docs/03-technical-feasibility.md`, `../docs/12-app-landscape.md`.

### 5.1. Android only
- 80% of the concept is impossible on iOS.
- iOS users get a companion app: widget + lock screen + Focus Mode automations + stats. Marketed as such; do not promise parity.

### 5.2. OEM background-killer wars (Xiaomi, Huawei, Samsung, OnePlus, ...)
- Each kills background services differently. Must handle per-OEM whitelisting flows.
- Foreground Service + Accessibility persistence reduces but doesn't eliminate.

### 5.3. Competitive landscape
From `../docs/12-app-landscape.md` §Part 4. The competitive launcher set is thin and undifferentiated:
- Niagara (polish), Olauncher (free OSS), Minimalist Phone (greyscale aggressor), Before / Light Phone / Brick / unpluq.
- **None have a behavioral feedback loop, social tier, financial stakes, life integrations, or a year-end artifact.** That's our seam.

### 5.4. Life-OS graveyard rules (Gyroscope, Bevel, Fabriq, etc.)
From `../docs/12-app-landscape.md` §Part 5. Things that have killed every life-OS attempt:
1. Manual data entry decays in 30 days. → Voice + auto-ingest only.
2. No artifact / no output. → `LIFE-005`, `LIFE-006`, `SOCIAL-010` Wrapped, `MERCH-001` prints.
3. Scope creep into coaching/courses/supplements. → Stay narrow at first. Launcher → discipline → life-OS. Each step earned.
4. OS-bundled defaults (Apple Health) eat aggregators. → Differentiate via launcher + social, not "all your health in one place."
5. The audience is technical and will roll their own in Notion. → Win on integration breadth + artifact quality, not feature count.
6. Pricing whiplash kills intimate-data products. → Pick a model in D6, commit. Never raise prices on existing users on intimate-data plans.
7. Privacy paradox. Rich data + Series-A startup = trust gap. → Local-first by default, E2EE cloud, open-source the client.

### 5.5. Distribution: Play Store rejection scenarios
- Device Admin → extra review.
- Accessibility Service → reject if not for accessibility purposes.
- VPN-based content blocking (`RESTRICT-020`) → policy grey area.
- Workaround: ship Play-Store-safe lite version + open-source sideload extreme APK + self-host distribution.

### 5.6. Backend operating cost at scale
- Group voting, real-time presence, Wrapped generation, push notifications, AI on-server features (`SAD-004` cloud path, `LIFE-002`).
- Per-user infra cost must clear the free-tier wall before we know monetization works. Cap features at free tier until DAU justifies it.

---

## 6. Naming & branding gates

From `../docs/13-naming-and-positioning.md`. The working name "FocusLauncher" describes ~10% of the actual product surface. Recommended product name: **Built**. Six gates must pass before commit:

| Gate | Status | Notes |
|------|--------|-------|
| Domain check (built.app / built.so / getbuilt.com / tryBuilt.com / Built.me) | Open | At least one must be available or buyable |
| Play Store search for active "Built" apps | Open | Need a clear field |
| USPTO trademark in class 9 (software) / 42 (SaaS) | Open | "Built" is generic English — expect a fight |
| Brand search hygiene (Google SERP for "Built app") | Open | Construction firms / house-builders dominate; expect SEO spend |
| Social handles (@built / @builtapp / @getbuilt on Twitter, IG, TikTok) | Open | At least one must be available |
| Negative-association check (Reddit / Twitter) | Open | Avoid disaster brand collisions |

Backup names: Lifeline, Mirror, Compound, Anchor. Each has its own tradeoffs detailed in `../docs/13`.

If rename happens, sub-brands also change: **Climb** (track system), **Anchor** (hardware line / NFC puck), **Wrapped** (annual recap), **Vault** (encrypted data layer), **Mantra** + **Lobby** keep their existing names.

Cost of changing name *before* MVP: near zero. After 50k downloads: six-figure marketing budget + community confusion. Decide now.

---

## 7. Philosophy tensions inside the spec

Internal contradictions that need an editorial-direction call, not just a tech decision.

1. **"Friction is the product" vs "Don't be annoying after mastery."** `UNINSTALL-007` Graduated Freedom + `LIFECYCLE-001` Graduate Mode counter the early-stage hostility. Must explicitly tune restrictions *down* as Track levels go *up*, or users uninstall at the moment we should be celebrating them.

2. **"Social > willpower" vs "Privacy is the moat."** Most growth comes from social features (`SOCIAL-010` Wrapped, `SOCIAL-025` Dual Streaks, `SOCIAL-009` Profile). But the data we collect is uniquely catastrophic if leaked. Default-private + opt-in-to-share is the only sustainable answer.

3. **"Identity over rules" vs "Variable ratio dark patterns."** `PSYCH-001` Identity Voting frames the user as a moral agent making choices. `RESTRICT-004` Variable Ratio Punishment manipulates them via slot-machine psychology. Both work, but they undercut each other's narrative. `ETHICS-001` Transparency Page is the partial answer.

4. **Sad Self ("you're failing me") vs Compassionate Demotion ("recalibration, not failure").** Both ship. Both serve different moments. Editorial guideline needed: when to use which tone for which user state. Not just a per-feature config.

5. **Anti-influencer ethos vs Creator Marketplace + Mentor Subscriptions.** `SOCIAL-018` Anti-Influencer Tab and `CREATOR-001` Track Marketplace coexist uneasily. The mitigation is *verified data* — creators publish stats that the app proves true. If verification slips, the marketplace becomes the influencer economy we said we were against.

6. **Year-in-review virality vs No-algorithm feed.** `SOCIAL-016` Chronological Feed is a marketing promise. `SOCIAL-017` Top Builders Tab and `SOCIAL-018` Anti-Influencer Tab are *recommendation surfaces*. Need a clear story: ranked-by-output is not "an algorithm" in the manipulative sense.

---

## 8. Per-feature open questions

Questions left dangling in the source docs that don't fit anywhere else.

### Productivity (`../docs/06-productivity-features.md`)
1. Carry-over vs expire for uncompleted todos at midnight?
2. Allow todos *without* app-unlock rewards (pure productivity, no carrot)?
3. Project progress shareable with group by default, or opt-in?
4. Journal: fully auto-generated or require user input to save?
5. Behavior indicator Red/Black state: *force* restrictions or just suggest?
6. Widgets: allow third-party (calendar / weather) or only ours? Spec defaults to ours-only; could lose users who want one weather widget.

### Mantra & Calls (`../docs/08-mantra-gate-and-urgent-calls.md`)
1. Mantra language support — non-Latin scripts? Right-to-left? Tonal-language verification?
2. Mantra change cooldown to prevent setting it to "a"?
3. Accessibility alternative when voice not possible — typing already in spec, but gesture pattern too?
4. "Any call from school location" as VIP rule, not just numbers?
5. Pass economy: rollover or use-it-or-lose-it weekly?
6. Group calls (WhatsApp, FaceTime) — same VIP/pass rules? Technically a different surface.

### Sad Self (`../docs/07-sad-self-and-gradual-commitment.md`)
1. Message generation: templates → on-device AI → cloud AI progression. When to cut over?
2. How long before Sad Self switches OFF for a user who consistently ignores it? Currently SAD-005 says "back off"; spec needs concrete cutoff days.
3. Sad Self's knowledge of YOUR data crosses a creepy line at some point. Slider for "how personal can it get"?

### Anti-uninstall (`../docs/02-anti-uninstall.md`)
1. The "Future Self Video" can't actually be unskippable if the user pulls the battery / boots into Safe Mode. What's the graceful failure?
2. Group uninstall vote: what if the user *should* be allowed to quit (grief, life change, app is bad)? Veto override by sponsor? By time alone (90-day no-vote = auto-approved)?
3. Financial stakes during group vote: who decides the financial penalty applies? Vote outcome or strict 72hr-cooldown clock?

---

## 9. Accessibility & inclusivity gaps still open

From `../docs/10-gap-analysis.md` §C5 and `A11Y-001`. Stage-1 has the pass; but each feature has its own version of the question.

1. Blind users → no overlays visible. → All overlay info must be `contentDescription`-tagged and TalkBack-friendly.
2. Deaf users → can't hear kazoo (`ABSURD-001`), applause (`GAMIFY-001`), narration (`ABSURD-002`). → Vibration / visual equivalents per feature.
3. Motor-impaired users → can't do voice mantra (`MANTRA-001`), can't do impossible-game uninstall puzzle (`UNINSTALL-005`-class). → Type/gesture/switch-control fallback per feature.
4. Cognitive disabilities → 50-word essay (`RESTRICT-017`), brutal-honesty feed (`SOCIAL-020`). → Gentler defaults; per-feature opt-out.
5. Non-English first-language users → mantra (`MANTRA-001`), Sad Self templates (`SAD-001`), AI insight summaries (`LIFE-002`). → i18n strategy before V1.
6. Cultural sensitivity on "shame" mechanics — `SOCIAL-005` Disappointment API, `SOCIAL-023` Confession Booth, `SOCIAL-040` Phone Funeral. Some cultures take shame mechanics very differently. Locale-specific defaults.

---

## 10. Parking lot — ideas explicitly NOT shipping in any current stage

From `../docs/11-social-features.md` §"Truly Unhinged" and assorted notes. Filed here so the brainstorm isn't lost.

- "Live in My Phone" Tab (auto-edited timelapses of others' focus sessions). Possibly inappropriate. Possibly genius. Not now.
- Build-in-Public Studio (your phone screen streams to lurkers). Twitch-for-work. Surveillance vibe. Not now.
- Polls-in-Stories ("Project A or B today?" → majority binds you). Cute but ungameable in a healthy way? Not now.
- Snitch Mode (invite a rival to see only your failures). Spite as motivation. Probably toxic. Not now.
- Voice Mantra Duets (friends record over your mantra; compilation video as a hype anthem). Trivial to build, weird in practice. Not now.
- Snap-Style Ghost Mode During Distraction (friends see you as a ghost icon when you're scrolling). Public shaming by default. Not now.
- Real-Time Bat-Signal on Drown (partner's phone buzzes "rescue?"). Plausible at V3 but heavy. Not now.
- Reverse Notifications from Age-80 Self (generated AI message from future-you). Companion to Sad Self. Skipped — too easy to misfire emotionally.
- Match-By-Weakness (Tinder for "people with your exact worst-hour pattern"). Possibly catastrophic, possibly transformative. Not now.
- The Bystander Network (anonymous nearby strangers get pinged to send encouragement when you're about to relapse). Privacy + safety nightmare. Not now.
- NFC Tag at Bar/Cafe partnerships (tap-to-start verified focus session at participating venues). Real-world business-development work — V4+ at earliest.
- Reverse Influencer Tax (auto "🚨 Unverified" badge on influencers whose routines don't match their data). Forced honesty — could be amazing or a legal mess. Not now.

---

## 11. Stale or under-defined material

Areas where the source docs themselves point at missing work:

- Devil's-advocate audit (`../docs/10-gap-analysis.md`) lists 12 "highest leverage" plug-the-hole features. All 12 are now in the stage 1–2 features. Done.
- The metadata count in `../docs/09-features-master.json` claims 265 features; my count totals 263 (245 stage-buckets + 18 impossibles). Two features may be miscounted or duplicated in the master JSON — minor, but worth a reconciliation pass before sign-off.
- `LIFECYCLE-001` Graduate Mode lacks concrete "what does the home screen actually look like at Year 2" mock. Needs design work, not just spec.
- `LIFE-002` Auto-Insight Engine is the highest-leverage feature in the spec ("mental ↔ physical correlation nobody has built") but is rated difficulty 5 / feasibility 4. On-device LLM quality at V2 timeline is the gating risk — needs a feasibility spike *before* V2 commitment.
- `LIFE-007` Counterfactual Twin requires a believable AI model of "your trajectory under different habits." Genuinely hard ML. Treat as research, not roadmap.
- `RESTRICT-024` Topic-Level Filter (LLM-classified content gating inside other apps) is rated feasibility 2. Probably best done via custom in-app browser for web services; native-app version is research-grade.

---

## 12. Things to revisit at each stage gate

Quarterly review checklist — items here might *move into* the plan once conditions change:

- **iOS companion seriousness.** Right now we ship Android-only. At ~50k DAU, if 20%+ of waitlist is iOS, reconsider FamilyControls-based iOS app.
- **Marketplace / B2B activation.** `CREATOR-001` and `B2B-001`/`B2B-002` only make sense once `SOCIAL-011` "Built" badge has real scarcity (i.e., enough 365-day users exist).
- **Hardware product line.** `HARDWARE-001` NFC tag is a software feature. The branded "Anchor" puck (MERCH-001 / `../docs/13`) is a hardware product — only ship when supply chain + warehousing makes sense.
- **AI feature cloud migration.** Default is on-device for privacy. If accuracy of on-device LLM remains insufficient for `LIFE-002`, `SAD-004`, `LIFE-011`, plan a privacy-preserving cloud path (E2EE inference, federated learning, or paid premium with opt-in cloud).
- **Couples-finance (`FIN-011`) and Joint Family modes (`FAMILY-001`).** Big revenue opportunities but compound the legal exposure. Revisit after lawyer-on-retainer milestone.
- **Premium pricing experimentation.** Tied to D6. Whatever we pick at MVP, don't touch for 12 months on existing users — Gyroscope's pricing whiplash killed it.
