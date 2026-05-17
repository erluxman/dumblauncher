# Naming & Positioning — Should "FocusLauncher" Become Something Else?

> Date: 2026-05-17
> Trigger: as the spec expands from "disciplinary launcher" (~135 features) to "OS for life" (~250+ planned features spanning launcher + tracking + social + finance + creator economy), the working name "FocusLauncher" describes ~10% of what we're building.
> Status: open question, not a decision.

---

## The naming problem

**"FocusLauncher"** describes one feature: a launcher that helps you focus.

The actual product is:
- A launcher (10% of the scope)
- A discipline track system (TRACK-001 through TRACK-004)
- A life tracker (fitness + meditation + finance + location + relationships)
- A social network for builders (40 social features)
- A correlation engine + AI life coach
- A creator marketplace
- A safety net (SAFETY-001 crisis detection)
- A year-end artifact factory (Built Wrapped)
- Eventually: hardware (NFC puck), couples mode, family mode, B2B coach/therapist dashboards

**A category-defining product needs a category-defining name.** "FocusLauncher" reads like a 2018 productivity utility on the Play Store, not a movement.

The rule from `docs/12-app-landscape.md` Part 5 holds: every successful "life OS" wins on **narrow + beautiful**, then expands. The name should be *general enough to grow into*, *specific enough to mean something*, and *short enough to chant*.

---

## Five candidate names

### 1. **Built** ⭐ (recommended)

> "I'm Built." / "Built for the year." / "Built since 2026."

**For:**
- Already implicit in the verification badge (`SOCIAL-011` "Built" Badge). Aligns with the social identity layer we've already specified.
- The verb "to build" is exactly what the app rewards. Building a body. Building a project. Building a life.
- Identity claim, not feature description. "I'm Built" reads like "I'm sober" or "I run." Becomes a self-concept.
- Resonant with the existing "Builder vs Consumer" mode toggle (`PSYCH-004`) and "Top Builders" tab (`SOCIAL-017`).
- Single syllable, four letters, hard consonants. Memorable. Tattooable.
- Past tense — implies the work is done, the user has been forged. Aspirational.

**Against:**
- "Built" is a common English adjective — discoverability + SEO hard. Need to dominate brand search.
- Domain availability uncertain (built.app / getbuilt.com / built.so — must check).
- Could sound macho / fitness-bro in the wrong context. Brand voice has to actively counter that with breadth (mind, money, mood, relationships all visible).

**Tagline candidates:**
- "Built. Day by day."
- "The OS for a life worth building."
- "You're not your phone. You're what you build."

### 2. **Lifeline**

> "Your lifeline, in one place."

**For:**
- Strong life-OS positioning. Implies both *line of life* (timeline) and *life-saving rope* (the safety angle).
- Visualizable: the entire app is conceptually one infinite scrollable timeline of your life.
- Plays well with our planned "Digital Twin Timeline" feature (`LIFE-*`).

**Against:**
- Crowded — multiple existing apps and products named Lifeline (911 service, indie game, crisis hotlines).
- Heavy / serious. Doesn't have the swagger of "Built."
- SEO competes with telecom and crisis services.

### 3. **Mirror**

> "The mirror you can't lie to."

**For:**
- Captures the *honest reflection* core thesis (PSYCH-005 Mirror Widget, SOCIAL-032 Brutal Mirror Filter, the whole "see yourself truly" pitch).
- Two syllables, memorable, ancient word.
- Implies passive observation, not active prescription. Lower threat than "Coach."

**Against:**
- Crowded — Mirror (the fitness mirror by Lululemon), Mirror Trading, Mirror publishing platform.
- Static metaphor — doesn't capture the building/improving arc.
- Slightly creepy connotations (surveillance, vanity).

### 4. **Compound**

> "Compound your habits. Compound your life."

**For:**
- Atomic Habits / Bezos / Buffett math is the implicit thesis. 1% better/day = 37x in a year.
- Mathematical, serious, builder-adjacent.
- Plays well with the "Compounding Curve Visualizer" feature.
- Unique-ish in the productivity space.

**Against:**
- Chemical / pharmacy / military-base connotations split attention.
- Sounds like a fintech app (Compound Finance is a DeFi protocol).
- Less identity-claimable than "Built" — "I use Compound" isn't a self-concept.

### 5. **Anchor**

> "Anchor your day."

**For:**
- Anti-drift framing. The whole product is about not drifting through life.
- Stable, simple, classic.
- Pairs with "anchor habits" — a real psychology term.

**Against:**
- Anchor by Spotify (podcasts) is the dominant brand association.
- Heavy / sailor-adjacent imagery limits design language.
- Less aspirational than "Built."

---

## Recommendation: **Built**

Of the five, "Built" has:
- The best identity claim ("I'm Built")
- The strongest alignment with existing specced features (verification badge, Builder/Consumer mode, Top Builders tab)
- The shortest, most chantable form
- The most flexible scope (build a body, build wealth, build a life, build a relationship)
- The least overlap with established competitor brands

**Risk acceptance:** the SEO + discoverability cost is real but solvable with category-defining marketing. "Built" should win brand search via paid + content + community, not via dictionary luck.

---

## Naming sub-elements (assuming Built is chosen)

| Element | Current | Proposed |
|---|---|---|
| Product | FocusLauncher | **Built** |
| Verification badge | "Built" Badge (SOCIAL-011) | "Built ✓" Badge (keep as-is — it's already perfect) |
| The "thriving" behavior state | Thriving (PROD-003) | "On Track" / "Built today" |
| Annual artifact | Built Wrapped (SOCIAL-010) | **Built Wrapped** (keep as-is) |
| Public profile URL | focuslauncher.app/risky | built.app/risky or built.app/@risky |
| Companion hardware (NFC puck) | unnamed | "Anchor" (sub-product brand) |
| The track system | Track System (TRACK-001) | **Climb** ("I'm on Climb 7 of the Sleep track") |
| The verification badge year-2 tier | unnamed | "Built. Year II" |
| Founder mode / Year-10 retention | Graduate Mode (LIFECYCLE-001) | "Built for Life" tier |

---

## What this changes practically

If we adopt **Built**:

1. **Update `docs/00-vision.md`** to reframe product around "what you build" not "what you avoid."
2. **Rename `docs/09-features-master.json` project field** `"FocusLauncher"` → `"Built"`.
3. **Marketing copy direction:** every page header is in past-tense achievement form. "Built today." "Built this year." "Still Built after 247 days."
4. **Visual identity:** typography-first, no cute mascots. Bold sans serif, lots of negative space. Bauhaus / Swiss design influence. Color: muted greens for thriving + slate grey for default + sharp red only for critical safety. No purple, no gradient soup.
5. **Hardware product line** can be branded **Anchor** as a sub-brand. "Built × Anchor." The NFC puck, the desk timer, the focus speaker — all Anchors.
6. **Sub-feature brands** can have their own micro-identity:
   - **Climb** = the track / leveling system
   - **Anchor** = hardware product line
   - **Wrapped** = annual recap (Built Wrapped)
   - **Vault** = the encrypted journal/data layer
   - **Mantra** = the voice unlock ritual (already named)
   - **Lobby** = the wait-before-open screen (already named)

---

## Before deciding — checks to run

These are gates before committing to a rename. Listing so we don't move blindly:

1. **Domain check.** `built.app`, `built.so`, `getbuilt.com`, `tryBuilt.com`, `Built.me`. At least one must be available or buyable.
2. **Play Store search.** Are there active apps with "Built" as a primary name?
3. **Trademark search.** "Built" is generic in English — USPTO will require a specific class (mobile software, life-tracking). Need to confirm no conflicting registered marks in class 9 (software) / 42 (SaaS).
4. **Brand search hygiene.** What does Google show when you search "Built app"? If it's already crowded with house-builders / construction firms, expect to spend $$ on SEO.
5. **Social handles.** @built / @builtapp / @getbuilt on Twitter, IG, TikTok. At least one must be available.
6. **Negative associations check.** "Built" + Reddit / Twitter — any disaster brands?

I'd run all six before signing off. Cheap to check (a few hours), expensive to undo.

---

## Open question

Should the rename happen **before** MVP launch (cleaner, but delays) or **after** initial traction (validated, but rebrand churn)?

My weak preference: **before**, but only if domain + trademark gates pass cleanly. The product is still pre-code; the cost of changing the name now is essentially zero. The cost of changing it after 50k downloads is six figures of marketing budget + community confusion.

Pinning the discussion here so we don't lose it. Decision tracked in `docs/04-decisions-needed.md`.
