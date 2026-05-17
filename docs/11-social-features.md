# Social Features — Wild Brainstorm

> Date: 2026-05-17
> Inspiration: Instagram, Snapchat, Strava, Duolingo, AA, Discord, LinkedIn.
> Inversion: every viral surface from social apps — but engagement here means **putting the phone down**, not picking it up.

Risk labels:
- 🟢 safe / clearly good
- 🟡 weird but defensible
- 🔴 unhinged / launch-press only / opt-in extreme mode

The "best of these" are filed in `09-features-master.json` as `SOCIAL-009` through `SOCIAL-040`. This doc keeps the full set (including ones we won't ship) so the brainstorm isn't lost.

---

## 1. Profile & Identity

1. 🟢 **Public Builder Profile** — `focuslauncher.app/risky`. Shows streaks, projects shipped (verifiable from app data), heatmap, "Built" badge if verified. LinkedIn for makers of their own life.  → `SOCIAL-009`
2. 🟢 **"Built" Verification Badge** — earned after 365 days + N shipped projects + 3 community vouches. Tiny checkmark. Real social currency you can't fake. → `SOCIAL-011`
3. 🟡 **Receipt Wall** — every time you put the phone down within 3 min, a tiny tile appears on your profile. Visible *texture* of restraint over time. Skyline of small wins. → `SOCIAL-012`
4. 🟡 **Anti-Bio** — your bio isn't what you do, it's what you've **chosen not to do**. "47 days TikTok-free. Quit Twitter in March. Replaced Reddit with Anki." → `SOCIAL-013`
5. 🟡 **Highlight Reels** — pinned permanent stories on profile of biggest wins (shipped projects, longest streaks). Auto-verified from data. → `SOCIAL-014`
6. 🔴 **Aura Avatar** — in-app avatar visibly degrades or glows based on behavior meter. Drowning = foggier. Thriving = golden ring. Friends see it. → `SOCIAL-015`

## 2. Feed & Discover

7. 🟢 **Chronological Feed Only** — no algorithm. People you actively chose to follow, in order. Sacred space. Brag about this. → `SOCIAL-016`
8. 🟢 **Today's Top Builders** — Explore-tab equivalent, sorted by *output verified by app data*, not by engagement. → `SOCIAL-017`
9. 🟡 **Anti-Influencer Tab** — verified creators publish actual focus rules + real stats. "Follow Tim Ferriss's exact schedule this week." Numbers compete with yours. → `SOCIAL-018`
10. 🟡 **Hashtag Tracks** — `#75hard`, `#buildinpublic`, `#nomorningphone`. Joinable public challenges with thousands of strangers on the same track. → `SOCIAL-019`
11. 🟡 **The Disappointment Inbox** — a feed surface that scrolls through *only* messages from your future selves and accountability buddies being disappointed in you. Pure dark humor. → `SOCIAL-020`
12. 🔴 **"Live in My Phone" Tab** — opt-in TikTok-style feed of auto-edited timelapses of *other* users' actual focus sessions (screen + face cam). Real productivity porn. Algorithmically hidden from you during your own focus blocks.

## 3. Stories / Ephemeral

13. 🟢 **Focus Stories (24hr)** — auto-generated at end of day from data: "Risky shipped 3 todos, 4.5hr focus, 0 TikTok opens." No filters, no fakery. → `SOCIAL-021`
14. 🟢 **Mood Voice Notes** — Clubhouse-style 30-sec audio about today. Less performative than video. Expires in 24hr. → `SOCIAL-022`
15. 🟡 **Confession Booth** — anonymous post of today's failure. Strangers send "I've been there" stickers. ML-curated for genuineness. → `SOCIAL-023`
16. 🟡 **Polls in Stories** — "Should I work on project A or B today?" Friends vote → you're committed to majority pick.
17. 🔴 **Vault Stories (Close Friends Only)** — share your **shameful** stats with 3-5 trusted people. Brutal honesty club. → `SOCIAL-024`

## 4. Streaks With Friends (Snapchat 🔥)

18. 🟢 **Dual Streaks with Friends** — both hit focus goal = `🔥 day 47 with Sarah`. Breaks if *either* misses. Snapchat's retention engine, repurposed. → `SOCIAL-025`
19. 🟡 **Stack Streaks** — squads of 3-5 all hit goal = bigger fire emoji. Group-level pride.
20. 🟡 **Streak Charms** — Snapchat-style auto-generated facts: "You and Sarah have woken up before 7am the same 89 days." → `SOCIAL-026`
21. 🟡 **Streak Reincarnation** — when a dual streak dies, both parties get a 1-week "restart insurance" window.

## 5. Live Presence

22. 🟢 **Focus Map** — Snap Map equivalent. Optional location share showing friends deep-working right now. Coffee shop pin: "3 friends focusing here." Drives real-world co-work. → `SOCIAL-027`
23. 🟢 **Live Focus Rooms** — Discord-style audio rooms. 2–50 people working silently together. Mic muted, camera optional. → `SOCIAL-028`
24. 🟡 **Ghost Lurk** — opt-in invisible mode to check on friends without showing online.
25. 🟡 **Build-in-Public Studio** — your phone screen during deep work auto-streams to a private room your followers can lurk in. Twitch for working.
26. 🔴 **Late Night Crew** — auto-formed group of grinders/insomniacs in your timezone, visible only 11pm–4am. Solidarity for the obsessed.

## 6. Reactions & DMs

27. 🟢 **Push-Reactions to Friend's Failure** — when Sarah opens TikTok the 10th time, your phone buzzes. One-tap reactions: 👀 🚨 💪 ❤️ "I'm here." → `SOCIAL-029`
28. 🟡 **Focus DMs (Queued)** — messages sent during deep work are queued. Recipient sees "Sarah sent during your deep work — delivered at 11:00." → `SOCIAL-030`
29. 🟡 **Apology Templates** — when you fail, system drafts an apology to your accountability partner. They get push: "Risky failed. Here's their note."
30. 🟡 **Pity Stickers** — custom reaction packs: "It's ok get back up", "I'm coming over", "This isn't you."

## 7. Filters & AR

31. 🟡 **Focus Aura Filter** — selfie filter overlays a glow on you based on real data. Gold = thriving. Murky = drowning. Shareable to IG/Snap = recruits new users. → `SOCIAL-031`
32. 🟡 **Mantra Filter** — front camera shows you surrounded by floating text — your mantras, project names, today's One Thing.
33. 🔴 **Brutal Mirror Filter** — AR filter that re-skins your face based on actual data. Disciplined = sharp/young. Drowning = bloated/aged/foggy. Most-shared and most-controversial. → `SOCIAL-032`
34. 🔴 **Build Body Filter** — your in-app avatar's cartoon body literally gets buffer the more you focus, skinnier when you slip.

## 8. Challenges & Duels

35. 🟢 **Public Pre-Commitments** — post tomorrow's goals to feed. App auto-grades pass/fail at end of day. → `SOCIAL-033`
36. 🟡 **Focus Duels** — challenge friend to a 2hr focus block. Higher score wins. Loser owes coffee. → `SOCIAL-034`
37. 🟡 **Side-Quest Dares** — friend dares you mid-day: "20 pushups in next hour, $5 says you don't." Camera proof.
38. 🟡 **Group Sprints with Live Leaderboard** — extend `SOCIAL-007`. Real-time bar chart during the 2hr.
39. 🔴 **The Loser Pays** — squad of 5 each commits $10/week. Lowest scorer pays the other 4. Social-financial roulette.

## 9. Memories & Recaps

40. 🟢 **Memories** — "1 year ago today: 6hrs on TikTok. Today: 18min." Auto comparison cards.
41. 🟢 **Built Wrapped (Year in Review)** — Spotify Wrapped for discipline. "You shipped 4 projects. Saved 412hrs. Top distraction defeated: Twitter, 89 times. Top friend: Sarah, 247-day streak." Designed to be screen-shareable. → `SOCIAL-010`
42. 🟡 **Anniversary Cards** — auto-generated when you + accountability partner hit milestones. Mailable physical postcard.
43. 🟡 **Time Capsule from Past Self** — videos you recorded years ago unlock at milestones (day 100 = unlocks 5-yr-ago you).

## 10. Generosity Mechanics

44. 🟡 **Time Donation** — gift your saved time to a friend's bank. "Sending 1.5hr to Sarah, she's stuck on her novel." Venmo for hours. → `SOCIAL-035`
45. 🟡 **Mantra Mentions** — tag a friend in your mantra; they get "Risky did their mantra and mentioned you." Honor-by-proxy.
46. 🟡 **Cheerleader Bot** — designate one friend who gets pushes *only when you win*. Pure positive channel.
47. 🔴 **The Proxy Goal** — give your goal to a friend's app. They literally see *your* todos on *their* home screen. Distributed conscience.

## 11. Couples & Family

48. 🟢 **Couples Mode** — pair with partner. Shared streak, shared time-saved, shared "phone Sabbath Sunday." → `COUPLES-001`
49. 🟡 **Anniversary Tracker** — relationship has a "focus age" — days both disciplined since pairing.
50. 🟡 **Family Tree** — multi-generational. Kid's app feeds your dashboard. Your dad's app shows you when he's idle 12 hrs (welfare check). → `FAMILY-001`

## 12. Creator Economy

51. 🟢 **Track Marketplace** — buy/clone curated tracks from creators. "Cal Newport's Deep Work Track." Revenue split. → `CREATOR-001`
52. 🟡 **Mentor Subscriptions** — pay a verified Built user $5/mo for full data + DM access. Twitter Subscriptions for discipline experts.
53. 🟡 **Coach Dashboard** — therapists/ADHD coaches see (with consent) anonymized client patterns. B2B SaaS. → `B2B-001`
54. 🔴 **Reverse Influencer Tax** — if a celeb's posted routine doesn't match their actual data, an "🚨 Unverified" badge auto-appears next to their content. Forced honesty for influencers.

## 13. Public Accountability / Shame

55. 🟢 **Quiet Brag Post** — finish your One Thing → "share?" → auto-post. Small win, low friction. → `SOCIAL-036`
56. 🟡 **Disappointment API Channels** — extend `SOCIAL-005`. Add severity per relationship: weekly to mentor, monthly to parent, real-time to spouse.
57. 🟡 **Public Failure Wall** — opt-in. Your fails post publicly with humor. "Risky lost to Twitter today, 47 min in."
58. 🔴 **Snitch Mode** — invite a specific enemy/rival to see *only* your failures. Pure motivation by spite.

## 14. Snapchat-Specific Weird Stuff

59. 🟡 **Best Friends List** — top 8 most-supportive friends auto-curated from reactions/help given. Visible on profile.
60. 🟡 **Streak Lock-Screen Widget** — fire emoji + day count for each dual streak. Glanceable shame/pride.
61. 🟡 **Voice Mantra Duets** — record your mantra; friends record over it; compilation video as a hype anthem.
62. 🔴 **Snap-Style "Ghost" Mode During Distraction** — when you're scrolling Instagram, your friends' app shows you as a literal ghost icon. They know you're gone without being told.

## 15. The Truly Unhinged Ideas (parking lot)

63. 🔴 **Real-Time Bat-Signal on Drown** — when meter hits Drowning, partner's phone buzzes with "rescue?" notification.
64. 🔴 **Reverse Notifications from Age-80 Self** — once a week, app sends a notification *from your future self at age 80*, generated from your data trajectory.
65. 🔴 **Public Phone Funeral** — when you beat a distraction app (30 days zero opens), trigger a community ritual. Friends "attend" funeral. Gravestone on profile.
66. 🔴 **Match-By-Weakness** — Tinder-style match with a stranger who has the *exact same* worst-hour pattern as you.
67. 🔴 **The Bystander Network** — when about to relapse, ping 3 random nearby users (anonymous): "Someone near you is struggling. Send encouragement?"
68. 🔴 **NFC Tag at Bar/Cafe** — bar/cafe partners stick a FocusLauncher tag on tables. Tap to start a verified focus session that posts to feed. Real-world venue partnership.

---

## Top 3 to ship first (highest leverage, lowest weirdness)

1. **Dual Streaks + Push Reactions** (`SOCIAL-025` + `SOCIAL-029`) — Snapchat's entire retention engine, but for the right behavior. Cheap to build, massive network effect.
2. **Built Wrapped** (`SOCIAL-010`) — single most viral surface in modern social. We have all the data. Drives install spikes every January.
3. **Public Builder Profile + Built Verification** (`SOCIAL-009` + `SOCIAL-011`) — gives users *external* social value for their internal discipline. Turns the app into a credential, not a tool. Spreads to LinkedIn and Twitter unprompted.

The rest is flavor. **Brutal Mirror Filter** (`SOCIAL-032`) is the "we made the news" launch feature.

---

## Cross-cutting concerns

These apply to *every* social feature:

- **Privacy default**: everything off / private by default. Opt-in to each surface. Granular controls.
- **Anti-harassment**: blocking, reporting, anti-pile-on. Public Failure Wall is dangerous without it.
- **Anti-fake**: every "achievement" must be auto-verified from app data. No manual claims.
- **Ethics**: see `SAFETY-001` and `ETHICS-001` in `09-features-master.json`. If a user is detected in crisis state, social pressure surfaces should soften or disable.
- **Account portability**: see `BACKUP-001`. Losing a phone shouldn't lose your social graph.
- **Verification economy**: the "Built" badge is the keystone. Don't give it away. Once it means something, everything else has weight.
