import { useEffect, useState } from "react";
import { UnlockMood, UnlockTyped, UnlockSadSelf, UnlockHighlight } from "./screens/unlock";
import { HomeMorning, HomeDay, HomeEvening, HomeNight } from "./screens/home";
import { TasksList, TaskCreate, TaskCompletion, TaskSurrender } from "./screens/tasks";
import { Level, Form, Review, ReviewDetail } from "./screens/life";
import { Feed, SearchMenu, LobbyIntercept, ReceiptAfterClose } from "./screens/system";
import { OnboardWelcome, OnboardMantra, OnboardFutureSelf, OnboardVIP, OnboardBaseline } from "./screens/onboarding";
import { UninstallGate, UninstallVote, Wrapped, BuilderProfile } from "./screens/lifecycle";
import { SettingsRoot, SettingsTransparency, CrisisSoften, Tombstones } from "./screens/settings";
import { PhoneFuneral, Boredom, LifeCoach, IdentityVote } from "./screens/extras";
import { MoneyStake, DualStreak, SponsorSetup, GroupCreate } from "./screens/stakes";
import { FocusMap, LiveFocusRooms, GroupSprint, FocusDuel } from "./screens/social-live";
import { CouplesPair, FamilyParent, NFCSetup, MantraListening } from "./screens/pairs";
import { TrackMarketplace, AntiInfluencer, HashtagTracks, TopBuilders } from "./screens/discovery";
import { SleepDash, MoneyDash, WorkoutDash, ReadingDash } from "./screens/domains";
import { PromiseKept, VaultStories, ConfessionBooth, PreCommit } from "./screens/sharing";
import { C } from "./tokens";
import Doc from "./Doc";
import FeaturesDoc from "./FeaturesDoc";
import MarketingDoc from "./MarketingDoc";

type Row = {
  num: string;
  title: string;
  desc: string;
  frames: { id: string; node: JSX.Element; caption: string }[];
};

const ROWS: Row[] = [
  {
    num: "01",
    title: "The Unlock Interview",
    desc: "One small thing, every unlock. Most features are never browsed — they're asked the moment you arrive.",
    frames: [
      { id: "u1", node: <UnlockMood />,      caption: "Tap one of three. +1 pt. ~3 sec. Skip 3 in a row → Sad Self surfaces." },
      { id: "u2", node: <UnlockTyped />,     caption: "Quick journal. +2 pts past 10 chars. Mood, ideas, meals — same shape." },
      { id: "u3", node: <UnlockSadSelf />,   caption: "Show-beat. No input. Loss aversion on time, with a real receipt." },
      { id: "u4", node: <UnlockHighlight />, caption: "Spaced repetition of your own highlights. Reading-as-thinking." },
    ],
  },
  {
    num: "02",
    title: "Home",
    desc: "Four modes for four hours of the day. The home is never the same place twice — it becomes the moment you're in.",
    frames: [
      { id: "h1", node: <HomeMorning />, caption: "Mantra first. Declare one thing. No app icons until the first todo earns them." },
      { id: "h2", node: <HomeDay />,     caption: "Active task pinned. Every todo has a time, payout, and an app it unlocks." },
      { id: "h3", node: <HomeEvening />, caption: "A daily ledger. Wins, losses, net. Tomorrow captured before sleep." },
      { id: "h4", node: <HomeNight />,   caption: "Apps disappear. Breath, alarm, kindle. Friction by the clock." },
    ],
  },
  {
    num: "03",
    title: "Tasks",
    desc: "The point economy. Creating a todo costs 3. Completing earns 5. Habits cost 50. Projects cost 30. Surrender is public.",
    frames: [
      { id: "t1", node: <TasksList />,      caption: "Today's todos with time estimate, payout, and the app each unlocks." },
      { id: "t2", node: <TaskCreate />,     caption: "Create modal. The cost is explicit, the unlock is explicit, the refund is explicit." },
      { id: "t3", node: <TaskCompletion />, caption: "Payout, balance, streak, form — and the app you just earned." },
      { id: "t4", node: <TaskSurrender />,  caption: "Friction before quitting. Two taps, with the forfeit and the social cost shown." },
    ],
  },
  {
    num: "04",
    title: "Life Layer",
    desc: "Level, Form, Review. The slow metrics that shape the fast ones. Sentence-only — no dashboards.",
    frames: [
      { id: "l1", node: <Level />,        caption: "L1 → L10. Each level adds daily commitment. L10 is graduate mode." },
      { id: "l2", node: <Form />,         caption: "F ∈ [0,1] from completion, streak, misses. It changes how the app treats you." },
      { id: "l3", node: <Review />,       caption: "A sentence ledger across every domain. Each line taps deeper." },
      { id: "l4", node: <ReviewDetail />, caption: "One sentence expanded. Same shape for finance, fitness, mood, people." },
    ],
  },
  {
    num: "05",
    title: "Social & Intercepts",
    desc: "Feed for the people you joined a group with. Search as the menu. Intercepts you never navigate to — they find you.",
    frames: [
      { id: "s1", node: <Feed />,               caption: "Reverse-chrono, no algorithm. Initials over avatars. Public surrender is brutal." },
      { id: "s2", node: <SearchMenu />,         caption: "There is no menu. Type to find anything. Categories below for the curious." },
      { id: "s3", node: <LobbyIntercept />,     caption: "Wait timer + intent declaration. Cannot skip. Wait shortens as form rises." },
      { id: "s4", node: <ReceiptAfterClose />,  caption: "Every distraction ends with a receipt. The cost is shown in time, money, and things undone." },
    ],
  },
  {
    num: "06",
    title: "Onboarding",
    desc: "A 20-minute first-run ritual. The 'why' is captured verbatim and resurfaces forever. Restrictions don't activate until baseline is observed.",
    frames: [
      { id: "o1", node: <OnboardWelcome />,    caption: "ONBOARD-002. The pain is captured raw. Resurfaces at every uninstall attempt." },
      { id: "o2", node: <OnboardMantra />,     caption: "MANTRA-001. Three reps before phone unlocks. Whisper / type fallback for public." },
      { id: "o3", node: <OnboardFutureSelf />, caption: "PSYCH-006. 30-sec recording. Plays back at every relapse, full-screen, no skip." },
      { id: "o4", node: <OnboardVIP />,        caption: "CALLS-001 + SOCIAL-008. Sponsor + VIPs in one step. 24-hr cooldown on edits." },
      { id: "o5", node: <OnboardBaseline />,   caption: "TRACK-004. 72-hour observation. No restrictions yet — only the unlock interview is live." },
    ],
  },
  {
    num: "07",
    title: "Lifecycle & Identity",
    desc: "Leaving, becoming, and being seen. The uninstall flow is the moat. Wrapped is the year-end content event. The Builder Profile is the public face.",
    frames: [
      { id: "lc1", node: <UninstallGate />,    caption: "UNINSTALL-002/004. 72-hour cooldown + future-self video. Daily check-ins required." },
      { id: "lc2", node: <UninstallVote />,    caption: "SOCIAL-002. Your group decides if you leave. 3-of-5 majority. 24-hour window." },
      { id: "lc3", node: <Wrapped />,          caption: "SOCIAL-010 + LIFE-006. Year in six stats. The Spotify-Wrapped moment of the year." },
      { id: "lc4", node: <BuilderProfile />,   caption: "SOCIAL-009 + 011 + 012 + 013. Public profile · anti-bio · receipt wall · verification." },
    ],
  },
  {
    num: "08",
    title: "Settings, Trust, Safety",
    desc: "Hidden behind passphrase + long-press. Every psychological technique is listed and toggleable. Crisis detection silently softens the app — never the user.",
    frames: [
      { id: "st1", node: <SettingsRoot />,         caption: "Accessed via long-press dock + passphrase. Cooldowns on every edit." },
      { id: "st2", node: <SettingsTransparency />, caption: "ETHICS-001. Every dark pattern is listed. Each has a switch. Default-on, opt-out trivially." },
      { id: "st3", node: <CrisisSoften />,         caption: "SAFETY-001. Triggered by pattern detection. Sad Self goes silent. Helplines surfaced. Private." },
      { id: "st4", node: <Tombstones />,           caption: "PSYCH-014. Apps you killed. Bringing one back is expensive and requires sponsor confirmation." },
    ],
  },
  {
    num: "09",
    title: "Ceremonies & Specialty Surfaces",
    desc: "Things you'll do once a quarter. A phone funeral for an app. Sitting in boredom on purpose. A two-minute conversation with your data.",
    frames: [
      { id: "ex1", node: <PhoneFuneral />, caption: "SOCIAL-040. 30 days zero opens of an app triggers the community ritual. Group attends." },
      { id: "ex2", node: <Boredom />,      caption: "RESTRICT-005. Two minutes of nothing. Earn +2 pts. Trains boredom tolerance." },
      { id: "ex3", node: <LifeCoach />,    caption: "LIFE-011. On-device LLM coach reads your full data. Stoic / CBT / friend personalities." },
      { id: "ex4", node: <IdentityVote />, caption: "PSYCH-001. Every distraction-app open is a vote. Cumulative tallies on profile." },
    ],
  },
  {
    num: "10",
    title: "Stakes & Social Bonds",
    desc: "When points stop being enough. Real money in escrow. Public streaks with a friend. A sponsor who gets called. A group that gets to vote on your fate.",
    frames: [
      { id: "sk1", node: <MoneyStake />,    caption: "FINANCE-001. Real escrow. Fail → cause you hate. Complete → refund + 10% bonus." },
      { id: "sk2", node: <DualStreak />,    caption: "SOCIAL-025. Shared fire 🔥 with one friend. One miss is forgiven. Two ends the streak." },
      { id: "sk3", node: <SponsorSetup />,  caption: "SOCIAL-008. One trusted person. Triggers are explicit. Sponsor can call, not scold." },
      { id: "sk4", node: <GroupCreate />,   caption: "SOCIAL-001. Max 5. Shared streak, shared feed, uninstall by vote. Weekly review." },
    ],
  },
  {
    num: "11",
    title: "Live Social",
    desc: "Friends in deep work, live. Audio rooms with verified focus. Sprints. Head-to-head duels. The opposite of a follower count.",
    frames: [
      { id: "ls1", node: <FocusMap />,       caption: "SOCIAL-027. City-level location share, opt-in. Filled dots = focused right now." },
      { id: "ls2", node: <LiveFocusRooms />, caption: "SOCIAL-028. Audio rooms, mic muted by default, only people in focus state can join." },
      { id: "ls3", node: <GroupSprint />,    caption: "SOCIAL-007. 5 people, 2 hours, one task each. All finish = bonus. One quits = penalty for all." },
      { id: "ls4", node: <FocusDuel />,      caption: "SOCIAL-034. Head-to-head against one friend. Winner takes the loser's 20 pts." },
    ],
  },
  {
    num: "12",
    title: "Pairs & Hardware",
    desc: "Pair with the people closest to you, or with a piece of plastic on your desk. Each pair changes how the app behaves around you.",
    frames: [
      { id: "pr1", node: <CouplesPair />,       caption: "COUPLES-001/002. Shared streaks, time saved, proud pings. Phone Sabbath together." },
      { id: "pr2", node: <FamilyParent />,      caption: "FAMILY-001. Parent dashboard. Approve / deny / talk first. Weekly digest shown to both." },
      { id: "pr3", node: <NFCSetup />,          caption: "HARDWARE-001. Pair an NFC desk tag. Distraction apps unlock only when you're touching it." },
      { id: "pr4", node: <MantraListening />,   caption: "MANTRA-001. Live listening state. Whisper mode and type fallback for public." },
    ],
  },
  {
    num: "13",
    title: "Discovery & Creator Economy",
    desc: "Surfaces that don't have follower counts. Tracks you can clone from verified creators. Leaderboards measured in output, not attention.",
    frames: [
      { id: "ds1", node: <TrackMarketplace />, caption: "CREATOR-001. Browse curated tracks, clone, follow the cloners' progress. 70/30 revenue split." },
      { id: "ds2", node: <AntiInfluencer />,   caption: "SOCIAL-018. Verified creators must publish real Lazy Launcher data. No edits." },
      { id: "ds3", node: <HashtagTracks />,    caption: "SOCIAL-019. Public joinable tracks like #buildinpublic. Leaderboards within each tag." },
      { id: "ds4", node: <TopBuilders />,      caption: "SOCIAL-017. Today's top builders by output × form × consistency. Nothing to like." },
    ],
  },
  {
    num: "14",
    title: "Domain Detail Views",
    desc: "The Review surface taps deeper here. Same pattern — sentences first, with one optional dashboard view per domain. Captured passively where possible.",
    frames: [
      { id: "dd1", node: <SleepDash />,    caption: "SLEEP-001/002. Avg, debt, consistency, and cross-correlation against scroll, caffeine, mood." },
      { id: "dd2", node: <MoneyDash />,    caption: "FIN-001..006. Spend, savings rate, by-category, and Subscription Hunter ROI." },
      { id: "dd3", node: <WorkoutDash />,  caption: "FIT-002/003/004/005. PR wall, sport-specific clocks, recovery score." },
      { id: "dd4", node: <ReadingDash />,  caption: "READ-001/002. Books finished, current read, daily highlight resurfacing." },
    ],
  },
  {
    num: "15",
    title: "Self-Knowledge & Public Honesty",
    desc: "What you tell the people who matter most, and what you tell the room. Identity-grade surfaces — the long-arc metrics and the brave shares.",
    frames: [
      { id: "sh1", node: <PromiseKept />,      caption: "IDENT-002. The single longest-leading-indicator metric we measure. Self-trust." },
      { id: "sh2", node: <VaultStories />,     caption: "SOCIAL-024. Real numbers, encrypted, to three trusted people. Today's vault expires in 24h." },
      { id: "sh3", node: <ConfessionBooth />,  caption: "SOCIAL-023. Anonymous post. Identity stripped on upload. Only stickers come back." },
      { id: "sh4", node: <PreCommit />,        caption: "SOCIAL-033. Tomorrow's three. Auto-graded at 10pm. Public stamp." },
    ],
  },
];

export default function App() {
  const [focused, setFocused] = useState<string | null>(null);

  // Route to a print-friendly document based on ?doc=…
  if (typeof window !== "undefined") {
    const param = new URLSearchParams(window.location.search).get("doc");
    if (param === "features")  return <FeaturesDoc />;
    if (param === "marketing") return <MarketingDoc />;
    if (param === "spec" || param === "1") return <Doc />;
  }

  useEffect(() => {
    const onKey = (e: KeyboardEvent) => {
      if (e.key === "Escape") setFocused(null);
    };
    document.addEventListener("keydown", onKey);
    return () => document.removeEventListener("keydown", onKey);
  }, []);

  const allFrames = ROWS.flatMap((r) => r.frames);
  const focusedFrame = focused ? allFrames.find((f) => f.id === focused) : null;

  return (
    <div className="min-h-screen bg-page">
      {/* Cover */}
      <header className="px-12 pt-16 pb-10 max-w-[2000px] mx-auto">
        <h1 className="text-[56px] leading-[60px] font-bold text-fg tracking-display">
          Lazy Launcher
        </h1>
        <p className="mt-5 text-[18px] text-fg-dim max-w-[820px] leading-[26px]">
          A minimal Android launcher that asks one tiny thing every time you unlock —
          and quietly builds the habits you wish you had. Every feature is event-driven.
          Nothing on the home screen is decoration.
        </p>
        <p className="mt-7 label-caps text-[12px] text-accent">
          EVENT-DRIVEN   ·   TYPE-FIRST   ·   POINT ECONOMY   ·   AMBIENT CAPTURE
        </p>
      </header>

      {/* Rows */}
      <main className="px-12 max-w-[2000px] mx-auto space-y-24 pb-32">
        {ROWS.map((row) => (
          <section key={row.num}>
            <div className="mb-10">
              <div className="label-caps text-[11px] text-accent">{row.num}</div>
              <h2 className="mt-2 text-[28px] font-bold text-fg tracking-heading">{row.title}</h2>
              <p className="mt-2 text-[14px] text-fg-dim max-w-[700px] leading-[22px]">{row.desc}</p>
            </div>
            <div className="flex flex-wrap gap-x-16 gap-y-20">
              {row.frames.map((f) => (
                <div key={f.id} className="flex flex-col">
                  <button
                    onClick={() => setFocused(f.id)}
                    className="cursor-zoom-in transition-transform hover:-translate-y-1 focus:outline-none"
                    aria-label={`Focus ${f.id}`}
                  >
                    {f.node}
                  </button>
                  <p className="mt-5 w-[412px] text-[11px] text-fg-dim leading-[16px]">{f.caption}</p>
                </div>
              ))}
            </div>
          </section>
        ))}

        {/* Design tokens */}
        <section>
          <div className="label-caps text-[11px] text-accent">06</div>
          <h2 className="mt-2 text-[28px] font-bold text-fg tracking-heading">Design tokens</h2>
          <p className="mt-2 text-[14px] text-fg-dim max-w-[700px] leading-[22px]">
            A monochrome system with one warm accent. Inter throughout — Light for displays,
            Regular for body, Semi Bold for caps labels. Type-led, icon-light.
          </p>
          <div className="mt-10 grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-6">
            {(Object.entries(C) as [keyof typeof C, string][]).map(([name, hex]) => (
              <div key={name} className="flex items-center gap-3">
                <div className="w-12 h-12 rounded-xl border border-edge" style={{ background: hex }} />
                <div>
                  <div className="text-[12px] font-medium text-fg">{name}</div>
                  <div className="text-[11px] text-fg-mute font-mono">{hex}</div>
                </div>
              </div>
            ))}
          </div>
        </section>
      </main>

      {/* Focus overlay */}
      {focusedFrame && (
        <div
          className="fixed inset-0 z-50 bg-page/80 backdrop-blur-sm flex items-center justify-center px-8 py-8"
          onClick={() => setFocused(null)}
        >
          <div onClick={(e) => e.stopPropagation()} className="relative">
            {focusedFrame.node}
            <button
              onClick={() => setFocused(null)}
              className="absolute -top-10 right-0 text-[11px] text-fg-dim hover:text-fg label-caps"
            >
              esc · close
            </button>
            <p className="mt-5 w-[412px] text-[11px] text-fg-dim leading-[16px]">{focusedFrame.caption}</p>
          </div>
        </div>
      )}
    </div>
  );
}
