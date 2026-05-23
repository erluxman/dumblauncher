import { ReactNode } from "react";
import { UnlockMood, UnlockTyped, UnlockSadSelf, UnlockHighlight } from "./screens/unlock";
import { HomeMorning, HomeDay, HomeEvening, HomeNight } from "./screens/home";
import { TasksList, TaskCreate, TaskCompletion, TaskSurrender } from "./screens/tasks";
import { Level, Form, Review, ReviewDetail } from "./screens/life";
import { Feed, SearchMenu, LobbyIntercept, ReceiptAfterClose } from "./screens/system";
import { C } from "./tokens";

function Page({ children, className = "" }: { children: ReactNode; className?: string }) {
  return (
    <section className={`doc-page ${className}`}>
      {children}
    </section>
  );
}

function H1({ children }: { children: ReactNode }) {
  return <h1 className="text-[44px] font-bold leading-[1.05] tracking-display text-fg">{children}</h1>;
}
function H2({ children, accent = false }: { children: ReactNode; accent?: boolean }) {
  return <h2 className={`text-[26px] font-bold leading-tight tracking-heading mb-4 ${accent ? "text-accent" : "text-fg"}`}>{children}</h2>;
}
function H3({ children }: { children: ReactNode }) {
  return <h3 className="text-[15px] font-semibold uppercase tracking-[0.16em] text-fg-mute mt-7 mb-3">{children}</h3>;
}
function P({ children, className = "" }: { children: ReactNode; className?: string }) {
  return <p className={`text-[13px] leading-[20px] text-fg-dim mb-3 ${className}`}>{children}</p>;
}
function MutedRow({ label, value }: { label: string; value: ReactNode }) {
  return (
    <div className="flex items-baseline gap-4 text-[12px] py-1 border-b border-edge/60">
      <span className="w-[160px] text-fg-mute uppercase tracking-wider text-[10px] font-semibold shrink-0">{label}</span>
      <span className="text-fg-dim leading-[18px]">{value}</span>
    </div>
  );
}

/* ── Cover ─────────────────────────────────────────────────── */
function Cover() {
  return (
    <Page className="!justify-between">
      <div>
        <div className="label-caps text-[11px] text-accent">DESIGN SPECIFICATION · v0.1</div>
        <div className="mt-3 text-[12px] text-fg-mute">2026 · erluxman</div>
      </div>
      <div>
        <div className="text-[88px] leading-[1] font-bold tracking-display text-fg">Lazy<br/>Launcher</div>
        <p className="mt-8 text-[17px] text-fg-dim leading-[24px] max-w-[540px]">
          A minimal Android launcher that asks one tiny thing every time you unlock —
          and quietly builds the habits you wish you had.
        </p>
        <p className="mt-12 label-caps text-[11px] text-accent">
          EVENT-DRIVEN  ·  TYPE-FIRST  ·  POINT ECONOMY  ·  AMBIENT CAPTURE
        </p>
      </div>
      <div className="text-[11px] text-fg-mute leading-[18px]">
        20 screens · 5 stages · 250 feature spec
        <br/>
        Stage 1 MVP · Stage 2 V1 · Stage 3 V2 · Stage 4 OS for life
      </div>
    </Page>
  );
}

/* ── Philosophy ────────────────────────────────────────────── */
function Philosophy() {
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">01</div>
      <H1>Philosophy</H1>
      <P className="mt-6 !text-[15px] !leading-[24px] text-fg max-w-[600px]">
        Every other launcher tries to be your home. This one is closer to a doorway you
        pass through. It asks one thing as you enter. It hands you a receipt when you
        leave. The rest of the time, it is quiet.
      </P>

      <H3>Four principles</H3>
      <div className="grid grid-cols-2 gap-6 max-w-[640px]">
        <div>
          <div className="text-[14px] font-semibold text-fg mb-1">Event-driven, not browseable.</div>
          <P className="!mb-0">Most features are never opened from a menu. They appear when their moment is — at the unlock, after a session, when you fail, when the hour turns. The menu exists, but only as a fallback for power users (Search).</P>
        </div>
        <div>
          <div className="text-[14px] font-semibold text-fg mb-1">Type-first, no icons.</div>
          <P className="!mb-0">Apps are text. Tasks are text. Reviews are sentences. Icons are decoration that makes scrolling feel cheap. Text makes it feel deliberate.</P>
        </div>
        <div>
          <div className="text-[14px] font-semibold text-fg mb-1">Friction as a feature.</div>
          <P className="!mb-0">A Lobby waits before a distraction. A passphrase guards Settings. Mantra reps gate the morning unlock. The friction is the point — it converts impulse into intent.</P>
        </div>
        <div>
          <div className="text-[14px] font-semibold text-fg mb-1">Points are receipts, not currency.</div>
          <P className="!mb-0">You see the cost when you create a task and the payout when you complete one. There is no leaderboard race, no streak push notifications. Points record what happened.</P>
        </div>
      </div>

      <H3>What it is not</H3>
      <ul className="text-[13px] text-fg-dim leading-[20px] space-y-1 list-disc pl-5 max-w-[600px]">
        <li>Not a productivity app you open. It opens itself.</li>
        <li>Not a digital-wellness scoreboard. The point of the score is to disappear.</li>
        <li>Not a community feed for likes. The feed exists to make surrender public, not success.</li>
        <li>Not a launcher you can leave. Anti-uninstall (72hr cool-down, future-self video) is core, not a setting.</li>
      </ul>
    </Page>
  );
}

/* ── Unlock Interview ──────────────────────────────────────── */
function UnlockMechanic() {
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">02</div>
      <H1>The Unlock Interview</H1>
      <P className="mt-6 !text-[15px] !leading-[24px] text-fg max-w-[600px]">
        The universal entry point. Every phone unlock plays exactly one beat. Over a week
        the user has ambiently logged mood × 14, water × 20, ideas × 8, meals × 21,
        intent × 7. Nobody browsed a menu. Nobody opened "Mood Tracker." The data accumulated
        through small interruptions the user already paid for.
      </P>

      <H3>Two beat types</H3>
      <div className="grid grid-cols-2 gap-6 max-w-[640px]">
        <div className="bg-surface rounded-[14px] p-5">
          <div className="label-caps text-[10px] text-accent mb-2">ASK</div>
          <ul className="text-[12px] text-fg-dim leading-[20px] list-disc pl-5">
            <li>Mood right now? (😞 / 😐 / 🙂)</li>
            <li>What did you ship in the last 2h?</li>
            <li>Drank water? Last meal? Pages read?</li>
            <li>One idea worth keeping? Grateful for?</li>
            <li>Spent money on? On what?</li>
            <li>Tomorrow's one thing?</li>
          </ul>
        </div>
        <div className="bg-surface rounded-[14px] p-5">
          <div className="label-caps text-[10px] text-danger mb-2">SHOW</div>
          <ul className="text-[12px] text-fg-dim leading-[20px] list-disc pl-5">
            <li>Sad self ("you opened me 14× today")</li>
            <li>A highlight from your past reading</li>
            <li>Future-you video, recorded weeks ago</li>
            <li>Streak status / level progress</li>
            <li>Receipt: time spent today on X</li>
          </ul>
        </div>
      </div>

      <H3>Scheduling — what gets asked when</H3>
      <P className="!mb-2">It is not random. The queue is weighted by:</P>
      <pre className="font-mono text-[11px] text-fg-dim bg-surface rounded-[10px] p-4 leading-[18px] max-w-[600px] whitespace-pre-wrap">{`priority = base_weight
         × (1 + hours_since_last_capture × decay)
         × time_of_day_match
         × form_modulation`}</pre>

      <div className="mt-4 space-y-1 max-w-[640px]">
        <MutedRow label="First unlock of day" value="Morning ritual (intention, mantra, sleep log) — expanded beat" />
        <MutedRow label="After lunch (12–2pm)" value="Meal / mood" />
        <MutedRow label="After closing IG/TikTok" value="Receipt + “what did you actually want?”" />
        <MutedRow label="Random mid-day" value="Idea, water, gratitude" />
        <MutedRow label="Evening (6–10pm)" value="Shutdown ritual — what shipped, tomorrow’s one thing" />
        <MutedRow label="Low form (<40%)" value="Bias toward SHOW beats (sad self, future you, highlight)" />
        <MutedRow label="High form (>80%)" value="Bias toward ASK beats — you're earning trust, capture more" />
      </div>

      <H3>Skip behavior</H3>
      <P>Skip is free but tracked. After 3 consecutive skips of a category, Sad Self surfaces: "you haven’t checked in on your mood in 4 days." Friction without punishment.</P>
    </Page>
  );
}

/* ── Point Economy ─────────────────────────────────────────── */
function PointEconomy() {
  const rows: [string, string, string, string][] = [
    ["Answer unlock (mood)", "0", "+1", "+1"],
    ["Answer unlock (typed, ≥10 chars)", "0", "+2", "+2"],
    ["Create todo", "−3", "+5 on time", "+2"],
    ["Late completion (within 1h)", "−3", "+3", "0 (forgiveness)"],
    ["Skip / surrender todo", "−3", "0", "−3 + feed post"],
    ["Commit habit (recurring)", "−50", "+3 / day", "break-even ~17 days"],
    ["Start project", "−30", "+50 on ship", "+20"],
    ["Spend on distraction apps", "1 pt = 1 min", "—", "depletes balance"],
  ];
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">03</div>
      <H1>Point Economy</H1>
      <P className="mt-6 !text-[15px] !leading-[24px] text-fg max-w-[620px]">
        Points are the bookkeeping for behavior. You earn them by producing,
        you spend them to consume. A balanced day creates 5 todos (−15), completes them (+25),
        nets +10 — which buys 10 minutes of Twitter back to zero. You can only consume
        what you produced.
      </P>

      <H3>Table</H3>
      <div className="bg-surface rounded-[14px] p-5 max-w-[700px]">
        <div className="grid grid-cols-[1fr,80px,140px,1fr] gap-4 text-[10px] label-caps text-fg-mute border-b border-edge pb-2">
          <span>Action</span><span>Cost</span><span>Reward</span><span>Net</span>
        </div>
        {rows.map(([a, c, r, n], i) => (
          <div key={i} className="grid grid-cols-[1fr,80px,140px,1fr] gap-4 text-[12px] py-2 border-b border-edge/60 last:border-0">
            <span className="text-fg">{a}</span>
            <span className="text-danger tabular-nums">{c}</span>
            <span className="text-success tabular-nums">{r}</span>
            <span className="text-fg-dim">{n}</span>
          </div>
        ))}
      </div>

      <H3>Why these numbers</H3>
      <P>Each todo's break-even success rate is 60% (3 cost / 5 reward). A user who reliably ships above that bar nets positive — but overcommitting overloads the balance and triggers Sad Self.</P>
      <P>Habits at −50 / +3 force a 17-day commitment to break even. This filters out impulsive habit-creation; only users with conviction reach the payout.</P>
      <P>Projects at −30 / +50 reward the long arc. You feel the cost weeks before the payout.</P>

      <H3>Forgiveness</H3>
      <P className="max-w-[600px]">
        Late completion (within 1 hour of deadline) refunds the original 3-pt investment.
        Loss aversion still applies (no profit), but the user is not punished for being human.
        Source: optimized-gamification research recommends a small refund window to soften
        missed deadlines and preserve momentum.
      </P>
    </Page>
  );
}

/* ── Form Metric ───────────────────────────────────────────── */
function FormMetric() {
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">04</div>
      <H1>Form — F ∈ [0, 1]</H1>
      <P className="mt-6 !text-[15px] !leading-[24px] text-fg max-w-[620px]">
        A single number that summarizes the user's recent self-trust. Updates daily.
        Replaces the older 5-state Behavior Indicator with a mathematically principled value.
      </P>

      <H3>Formula</H3>
      <pre className="font-mono text-[12px] text-fg bg-surface rounded-[12px] p-5 leading-[22px] max-w-[600px]">{`F = 0.5 · (completed / created) over last 14 days
  + 0.3 · (1 − e^(−streak / 7))
  − 0.2 · (1 − e^(−misses / 7))
clamp(F, 0, 1)`}</pre>

      <H3>What F shapes</H3>
      <div className="grid grid-cols-2 gap-4 max-w-[640px]">
        <div className="bg-surface rounded-[14px] p-5">
          <div className="text-[12px] font-semibold text-danger mb-1">F &lt; 0.40</div>
          <P className="!mb-0 !text-[11.5px]">Lobby wait 90s. Sad Self surfaces more often. Breath gate before distractions. Question difficulty drops to simple yes/no.</P>
        </div>
        <div className="bg-surface rounded-[14px] p-5">
          <div className="text-[12px] font-semibold text-fg-dim mb-1">0.40 ≤ F &lt; 0.60</div>
          <P className="!mb-0 !text-[11.5px]">Standard Lobby wait. Mixed ASK/SHOW beats. Receipts always shown after closing distraction apps.</P>
        </div>
        <div className="bg-surface rounded-[14px] p-5">
          <div className="text-[12px] font-semibold text-fg-dim mb-1">0.60 ≤ F &lt; 0.80</div>
          <P className="!mb-0 !text-[11.5px]">Eligible for level promotion. Lobby wait −20s. Ask-beats outweigh show-beats. Deeper journal prompts.</P>
        </div>
        <div className="bg-surface rounded-[14px] p-5">
          <div className="text-[12px] font-semibold text-success mb-1">F ≥ 0.80</div>
          <P className="!mb-0 !text-[11.5px]">Lobby instant. Sad Self silent. Sophisticated capture beats (voice dumps, dream journal). Eligibility for graduate-mode preview.</P>
        </div>
      </div>

      <H3>Leaderboard math</H3>
      <P className="max-w-[600px]">
        Among friends in a joined group: rank = F × points. A user with 400 pts and 0.41 form ranks
        below a user with 244 pts and 0.73 form. Sustained engagement beats one-off hoarding.
      </P>
    </Page>
  );
}

/* ── Level Ladder ──────────────────────────────────────────── */
function LevelLadder() {
  const ladder: [string, string, string][] = [
    ["L1",  "15m / day",  "starter · 1 todo"],
    ["L2",  "30m / day",  "2 todos · first habit"],
    ["L3",  "1h / day",   "3 todos · 1 habit"],
    ["L4",  "2h / day",   "5 todos · 2 habits · 1 project"],
    ["L5",  "3h / day",   "7 todos · 3 habits"],
    ["L6",  "4h / day",   "first project deadlines"],
    ["L7",  "5h / day",   "team / group commitments"],
    ["L8",  "6h / day",   "deep work blocks · 2h+"],
    ["L9",  "7h / day",   "near-grind · uncommon"],
    ["L10", "15m / day",  "graduate · habits internalized · UI fades"],
  ];
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">05</div>
      <H1>Level Ladder</H1>
      <P className="mt-6 !text-[15px] !leading-[24px] text-fg max-w-[620px]">
        Ten levels of commitment. Promotion requires 14 consecutive days of full completion at
        the current level. Demotion triggers after 7 consecutive fail-days. Level 10 is graduate
        mode — the UI nearly disappears, only a quiet daily check-in remains.
      </P>

      <div className="mt-6 bg-surface rounded-[14px] p-5 max-w-[600px]">
        <div className="grid grid-cols-[60px,1fr,2fr] gap-4 text-[10px] label-caps text-fg-mute border-b border-edge pb-2">
          <span>level</span><span>commitment</span><span>shape</span>
        </div>
        {ladder.map(([lv, t, d], i) => (
          <div key={lv} className="grid grid-cols-[60px,1fr,2fr] gap-4 text-[12px] py-2 border-b border-edge/60 last:border-0">
            <span className="font-mono text-fg">{lv}</span>
            <span className="text-fg-dim tabular-nums">{t}</span>
            <span className="text-fg-dim">{d}</span>
          </div>
        ))}
      </div>

      <H3>Why the inversion at L10</H3>
      <P className="max-w-[600px]">
        Higher levels increase commitment, until L10. By then the habits are internalized;
        the app only needs to check in. The Graduate Mode (LIFECYCLE-001) makes the launcher
        near-invisible — stats-only widget, monthly review, optional uninstall enabled.
      </P>

      <H3>Eligibility</H3>
      <P>Form must be {">"} 0.60 for promotion. A user can be at L4 with poor form and stay there indefinitely without leveling up, even after 14 perfect days. The form gate prevents pure-grind escalation.</P>
    </Page>
  );
}

/* ── Screen Page (reusable) ────────────────────────────────── */
type ScreenSpec = {
  num: string;
  title: string;
  triggered: string;
  shows: string;
  features: string;
  notes?: string;
  Component: () => JSX.Element;
};

function ScreenPage({ spec }: { spec: ScreenSpec }) {
  return (
    <Page className="!flex-row !items-start !justify-start gap-12 !py-12">
      <div className="phone-scaled-65 shrink-0">
        <spec.Component />
      </div>
      <div className="flex-1 pt-2 max-w-[420px]">
        <div className="label-caps text-[10px] text-accent">{spec.num}</div>
        <h2 className="text-[28px] font-bold leading-[1.1] tracking-heading text-fg mt-1">{spec.title}</h2>

        <H3>What it shows</H3>
        <P className="!text-[12.5px] !leading-[19px]">{spec.shows}</P>

        <H3>When it appears</H3>
        <P className="!text-[12.5px] !leading-[19px]">{spec.triggered}</P>

        <H3>Features covered</H3>
        <P className="!text-[12px] !leading-[18px] font-mono">{spec.features}</P>

        {spec.notes && (
          <>
            <H3>Notes</H3>
            <P className="!text-[12px] !leading-[18px]">{spec.notes}</P>
          </>
        )}
      </div>
    </Page>
  );
}

const SCREENS: ScreenSpec[] = [
  { num: "1.1", title: "Unlock — Mood",       Component: UnlockMood,
    triggered: "Played on a phone unlock when no mood ping in the last 8 hours and time-of-day is between 09:00 and 22:00.",
    shows: "A single question with three emoji choices: low / ok / good. Tap one to save and continue to the home screen. Skipping is free, but tracked.",
    features: "PROD-017 Mood Pings · RESTRICT-015 Reverse Notifications",
    notes: "+1 pt for answering. Skip 3 in a row → Sad Self beat surfaces at the next unlock." },
  { num: "1.2", title: "Unlock — Typed Journal", Component: UnlockTyped,
    triggered: "Played 2+ hours into a focus block, or on the first unlock after a deep-work session, when the user has not journaled recently.",
    shows: "A short typed-answer prompt. The same shape is reused for ideas, meals, intent, gratitude — only the question changes.",
    features: "PROD-004 Journal · PROD-009 Ideas · INTEG-010 Voice-to-Structured-Data · MIND-004 Subconscious Dump",
    notes: "+2 pts past 10 characters. The cursor pulses orange. Submission auto-categorizes via on-device LLM." },
  { num: "1.3", title: "Unlock — Sad Self",   Component: UnlockSadSelf,
    triggered: "On unlock when daily distraction-app usage exceeds the user's set ceiling OR they have skipped 3 consecutive ask-beats.",
    shows: "A pointed message from \"sad you\" with a real receipt of today's distraction usage. No input — only tap to continue.",
    features: "SAD-001 Sad Self Notifications · SAD-002 Voice Style · PSYCH-008 Intervention Screen · PSYCH-011 Phantom Checks",
    notes: "Backed off if ignored 5× — surfaces less, but the message gets sharper. Voice style is configurable: gentle / blunt / sarcastic / philosophical." },
  { num: "1.4", title: "Unlock — Highlight",  Component: UnlockHighlight,
    triggered: "On unlock with probability proportional to (days since last show-beat) × time-of-day match. Picks from the user's saved highlights.",
    shows: "A single highlight from a book the user has read, with source and date. A pure show-beat — no decision required.",
    features: "READ-002 Daily Highlight Resurfacing · MIND-007 Reading-as-Thinking · PSYCH-015 Future Self Messenger",
    notes: "Drawn from Kindle/Readwise sync. Same shape can resurface a Future-Self letter the user wrote weeks ago." },

  { num: "2.1", title: "Home — Morning",      Component: HomeMorning,
    triggered: "Active from first unlock after 5:00 AM until 11:00 AM. After the mantra-gate reps, the home transitions to capture today's one thing.",
    shows: "A large clock, the user's mantra (which they must speak/type 3× to fully unlock), an empty One Thing card, and the week-so-far bar chart. Apps are deliberately hidden until the first todo earns them.",
    features: "CORE-001 Home Screen · MANTRA-001 Mantra Gate · PROD-007 Morning Routine · PROD-013 One Thing · PSYCH-001 Identity Voting · GAMIFY-006 Streaks",
    notes: "Mantra reps scale with level. L4+ requires 3; L8+ adds the day's task statement." },
  { num: "2.2", title: "Home — Day",          Component: HomeDay,
    triggered: "Active 11:00 AM through 18:00. Replaced by Evening mode at the user's configured shutdown hour.",
    shows: "The currently-active task pinned at top with its progress bar and app-unlock badge. The next three tasks in the queue. A list of five essential apps. A four-tab dock (tasks · review · search · feed).",
    features: "CORE-002 5-App Dock · PROD-002 Project Tracking · PROD-005 Focus Timer · RESTRICT-010 Context-Aware Locks · PSYCH-004 Builder Mode",
    notes: "App icons are absent by design. The five apps are user-selected with a 24-hour cooldown on changes." },
  { num: "2.3", title: "Home — Evening",      Component: HomeEvening,
    triggered: "Active from the user's shutdown hour (default 18:00) until 22:00. Always plays the shutdown ritual on the first unlock in this window.",
    shows: "Today's ledger — wins, losses, net points, form delta. Capture for tomorrow's one thing. A countdown to dream mode and a wind-down 4-7-8 breath button.",
    features: "PROD-012 Shutdown Ritual · PROD-004 Journal (auto-summary) · LIFE-003 Sunday Life Review (weekly variant) · RESTRICT-011 Dream Mode",
    notes: "Late-tomorrow scheduling can be voice-captured via INTEG-010." },
  { num: "2.4", title: "Home — Night",        Component: HomeNight,
    triggered: "Active from 22:00 until the user's wake alarm. Distraction apps are unreachable; only the alarm and Kindle/audiobook apps respond.",
    shows: "A dim clock, tomorrow's schedule, the 4-7-8 breath circle exercise, and a quiet line confirming that apps are asleep.",
    features: "RESTRICT-011 Dream Mode · SLEEP-003 Sleep Window Guardrails · HARDWARE-002 Breath Gate · MIND-001 Meditation Log",
    notes: "Phone sabbath (COUPLES-002) can extend this mode to a full day." },

  { num: "3.1", title: "Tasks — Today",       Component: TasksList,
    triggered: "Reachable from the Day-mode dock OR the Tasks slash-command in Search. Live task is always pinned at top.",
    shows: "A summary of cost/payout, the week streak, the live task highlighted, and the next-up queue. Three creation actions show their cost explicitly.",
    features: "PROD-001 Todo with App-Unlock · PROD-002 Project Tracking · TRACK-001 Track System · GAMIFY-003 Focus Score Economy",
    notes: "Habits cost 50 pts; projects cost 30 pts; todos cost 3 pts. The balance is shown before each creation." },
  { num: "3.2", title: "Tasks — Create",      Component: TaskCreate,
    triggered: "Modal opened from Tasks list or via the create-todo shortcut.",
    shows: "Name, time estimate, app-unlock picker, and a preview block showing the −3 / +5 / refund-on-late terms. The commit button is the only way to spend.",
    features: "PROD-001 Todo · PROD-010 Effort Estimation Training · PROD-019 Companion Verification (optional)",
    notes: "Estimates feed PROD-010 — actuals are logged and the user's estimation accuracy is tracked over time." },
  { num: "3.3", title: "Tasks — Completion",  Component: TaskCompletion,
    triggered: "On marking a task done. Auto-confirmed for time-bounded tasks; tap-confirmed otherwise.",
    shows: "Payout, balance, streak progress toward next level, form delta, and the unlocked app card if applicable. Two CTAs: open the unlocked app, or pocket the time.",
    features: "PROD-001 Todo · GAMIFY-001 The Applause · PSYCH-002 Session Receipts · GAMIFY-006 Streak System",
    notes: "Pocketing the time is the celebrated choice — the user keeps the points without spending them on distraction." },
  { num: "3.4", title: "Tasks — Surrender",   Component: TaskSurrender,
    triggered: "On tapping \"give up\" on an in-progress task. A two-step confirmation; cannot be skipped.",
    shows: "Cost, streak impact, social visibility. Two CTAs: keep going (primary), surrender (danger).",
    features: "PROD-001 Todo · SOCIAL-003 Shame Notification · SOCIAL-005 Disappointment API · RECOVERY-001 After-Fall Ritual",
    notes: "Surrender posts to the user's group feed. The After-Fall ritual triggers automatically on the next unlock." },

  { num: "4.1", title: "Level",               Component: Level,
    triggered: "Reached via the L4·73%·244 badge on the home screen, OR by typing \"level\" in Search.",
    shows: "Current level, days remaining to promotion, fail-days warning, and the full L1–L10 ladder with the user pinned in place.",
    features: "TRACK-001 Track System · TRACK-002 Level-Up Mechanics · TRACK-003 Multiple Independent Tracks · LIFECYCLE-001 Graduate Mode",
    notes: "Tracks can be set per-domain (Focus, Sleep, Fitness, Learning) and level independently." },
  { num: "4.2", title: "Form",                Component: Form,
    triggered: "Reached via the 73% segment of the home badge.",
    shows: "Current F value, the formula, a 14-day dot history, and a card showing how the current value modulates the rest of the app.",
    features: "PROD-003 Behavior Indicator (modernized) · SAD-005 Adaptive Frequency · RESTRICT-001 Lobby Wait modulation",
    notes: "The formula is intentionally visible — the user understands how the metric works, which is a transparency requirement (ETHICS-001)." },
  { num: "4.3", title: "Review",              Component: Review,
    triggered: "Reachable via the dock's review tab. Sentence-only — no charts unless tapped.",
    shows: "A scrollable sentence ledger across every tracked domain (focus, sleep, money, fitness, mood, reading, people, patterns).",
    features: "LIFE-001 Personal Bloomberg · LIFE-002 Auto-Insight Engine · LIFE-003 Sunday Life Review · SOCIAL-010 Built Wrapped · PSYCH-012 Pattern Detection",
    notes: "Each sentence is tappable; the detail view (4.4) shows the underlying data." },
  { num: "4.4", title: "Review — Detail",     Component: ReviewDetail,
    triggered: "From a tapped sentence on the Review screen — example shown is the spending sentence.",
    shows: "The same one-sentence shape expanded into a quantitative breakdown — categories, comparison vs prior period, and pending review items (e.g. Regret Receipts).",
    features: "FIN-001 Auto-Categorized Spend · FIN-004 Savings Rate · FIN-006 Time-Money · FIN-008 Regret Receipts",
    notes: "Pattern is reused for every other domain — fitness detail, mood detail, etc. use the same shape." },

  { num: "5.1", title: "Feed",                Component: Feed,
    triggered: "Only visible if the user has joined at least one group. Otherwise the dock tab is replaced by a join CTA.",
    shows: "Reverse-chronological group events: completions, surrenders, milestones, level changes, skipped-unlock warnings. Initials, not avatars. Three tabs: feed / leaderboard / profile.",
    features: "SOCIAL-001 Groups · SOCIAL-003 Shame Notification · SOCIAL-016 Chronological Feed · SOCIAL-021 Focus Stories · SOCIAL-025 Dual Streaks · SOCIAL-029 Push Reactions",
    notes: "No algorithm. No like counts. Surrender events are intentionally as prominent as completions." },
  { num: "5.2", title: "Search · Menu",       Component: SearchMenu,
    triggered: "Long-press home OR swipe up from any screen. Replaces the traditional app drawer and the legacy 80-row menu.",
    shows: "Recent items, time-of-day suggestions, and a category index. Type to filter anything — features, friends, apps, journal entries.",
    features: "CORE-003 Hidden App Search · all 250 features (via the index)",
    notes: "The category index makes features discoverable without being browseable. Power users find anything in two keystrokes." },
  { num: "5.3", title: "Lobby — Intercept",   Component: LobbyIntercept,
    triggered: "Played when a distraction app is launched. The user must wait, then declare their intent. Cannot be skipped.",
    shows: "App being opened, large countdown timer, the form-modulated wait length, and an intent declaration prompt. A back button is offered, never a skip.",
    features: "RESTRICT-001 The Lobby · RESTRICT-006 Intent Declaration · RESTRICT-002 Cognitive Tax · PSYCH-010 Last Day Test · RESTRICT-007 Escalating Lockout",
    notes: "Wait length: 15s for productive apps, up to 90s for distractions. Form > 0.80 cuts the wait to instant." },
  { num: "5.4", title: "Receipt — After Close", Component: ReceiptAfterClose,
    triggered: "Played the moment the user closes a distraction app (whether by timeout or manual exit).",
    shows: "Time spent, cost in points, equivalents (books, calls, exercise, money), and the count of todos shipped during the session.",
    features: "PSYCH-002 Session Receipts · PSYCH-007 Opportunity Cost · PSYCH-003 Legacy Counter · FIN-006 Time-Money Conversion",
    notes: "A primary CTA invites the user to start a small todo to earn the time back — converting regret into a next action." },
];

/* ── Feature Index by Category ─────────────────────────────── */
function FeatureIndex() {
  const groups: { title: string; lines: [string, string][] }[] = [
    {
      title: "INTERCEPTS — never browsed, always triggered",
      lines: [
        ["RESTRICT-001", "The Lobby — wait timer before distraction"],
        ["RESTRICT-002", "Cognitive Tax — puzzle gate"],
        ["RESTRICT-003", "Progressive Dimming Overlay"],
        ["RESTRICT-006", "Intent Declaration"],
        ["RESTRICT-007", "Escalating Lockout"],
        ["PSYCH-002",    "Session Receipts"],
        ["PSYCH-008",    "Intervention Screen (14 unlocks)"],
        ["PSYCH-010",    "Last Day Test"],
        ["PSYCH-011",    "Phantom Vibration Counter"],
        ["PSYCH-014",    "App Tombstones"],
        ["SAD-001",      "Sad Self Notifications"],
        ["UNLOCK-004",   "Future Self Video on Uninstall"],
      ],
    },
    {
      title: "UNLOCK INTERVIEW — captured ambiently",
      lines: [
        ["PROD-017",     "Mood Pings"],
        ["PROD-004",     "Journal (auto-generated and typed)"],
        ["PROD-009",     "Idea Parking Lot"],
        ["PROD-013",     "One Thing — daily priority"],
        ["NUT-001",      "Plate-Photo / typed meal log"],
        ["NUT-002",      "Fasting Window auto-detect"],
        ["SUB-001",      "Hangover Calculus (drinks logged)"],
        ["INTEG-010",    "Voice-to-Structured-Data Capture"],
        ["MIND-004",     "Subconscious Dump (60-sec voice)"],
        ["MIND-006",     "Dream Journal"],
        ["IDENT-003",    "Rejection Counter"],
        ["IDENT-004",    "Risks-Taken Log"],
        ["IDENT-005",    "Things-Made Counter"],
      ],
    },
    {
      title: "SHOW-BEATS — surfaced at the right moment",
      lines: [
        ["READ-002",     "Daily Highlight Resurfacing"],
        ["MIND-007",     "Reading-as-Thinking Loop"],
        ["PSYCH-006",    "Future Self Video"],
        ["PSYCH-015",    "Future Self Messenger (letters)"],
        ["PSYCH-009",    "Deathbed Simulator"],
        ["PSYCH-013",    "Anchoring Attack"],
        ["LIFE-009",     "Beach Days Remaining"],
        ["LIFE-012",     "Death Clock Widget"],
      ],
    },
    {
      title: "POINT ECONOMY & GAMIFICATION",
      lines: [
        ["TRACK-001",    "Track System — 10-level scaling"],
        ["TRACK-002",    "Level-Up / Demotion Mechanics"],
        ["TRACK-003",    "Multiple Independent Tracks"],
        ["PROD-001",     "Todo with App-Unlock Rewards"],
        ["GAMIFY-003",   "Focus Score Economy"],
        ["GAMIFY-006",   "Streak System"],
        ["GAMIFY-002",   "Compound Time Bank"],
        ["GAMIFY-007",   "Earned Pixels"],
        ["GRACE-001",    "Grace Days"],
        ["GRACE-002",    "Streak Freeze"],
        ["RECOVERY-001", "After-Fall Ritual"],
      ],
    },
    {
      title: "REVIEW SURFACE — one sentence per domain",
      lines: [
        ["LIFE-001",     "Personal Bloomberg Terminal"],
        ["LIFE-002",     "Auto-Insight Engine"],
        ["LIFE-003",     "Sunday Life Review"],
        ["LIFE-004",     "Quarterly Self-Audit"],
        ["LIFE-005",     "Annual Report (PDF)"],
        ["LIFE-006",     "Built Wrapped — multi-domain"],
        ["LIFE-007",     "Counterfactual Twin"],
        ["LIFE-010",     "Compounding Curve"],
        ["PSYCH-003",    "Legacy Counter"],
        ["PSYCH-012",    "Pattern Detection"],
      ],
    },
    {
      title: "DOMAINS — passive sensors + ambient capture",
      lines: [
        ["INTEG-001",    "HealthConnect — body data"],
        ["INTEG-002",    "Plaid — banking"],
        ["INTEG-003",    "Strava / Garmin / Whoop / Oura"],
        ["INTEG-006",    "Photos library"],
        ["INTEG-007",    "Calendar deep integration"],
        ["INTEG-009",    "Kindle / Audible / Goodreads"],
        ["LOC-001",      "Lifetime Location Heatmap"],
        ["SLEEP-002",    "Sleep ↔ Everything Correlator"],
        ["FIN-001",      "Auto-Categorized Spending"],
        ["FIN-003",      "Subscription Hunter"],
        ["FIT-002",      "Workout Log"],
        ["PRM-001",      "Time-With-People Auto-Detect"],
      ],
    },
    {
      title: "SOCIAL — opt-in, group-scoped",
      lines: [
        ["SOCIAL-001",   "Group Creation"],
        ["SOCIAL-002",   "Group Uninstall Approval"],
        ["SOCIAL-005",   "Disappointment API"],
        ["SOCIAL-008",   "Sponsor System"],
        ["SOCIAL-009",   "Public Builder Profile"],
        ["SOCIAL-010",   "Built Wrapped"],
        ["SOCIAL-011",   "Verification Badge"],
        ["SOCIAL-016",   "Chronological Feed"],
        ["SOCIAL-021",   "Focus Stories"],
        ["SOCIAL-025",   "Dual Streaks"],
        ["SOCIAL-029",   "Push Reactions to Friend Failure"],
        ["SOCIAL-033",   "Public Pre-Commitments"],
        ["COUPLES-001",  "Partner Pair Mode"],
        ["FAMILY-001",   "Parent / Child Pair Mode"],
      ],
    },
    {
      title: "ANTI-UNINSTALL — the moat",
      lines: [
        ["UNINSTALL-001", "Device Admin Registration"],
        ["UNINSTALL-002", "72-hour Cooldown Deactivation"],
        ["UNINSTALL-003", "Nuclear Passphrase (20+ char paper key)"],
        ["UNINSTALL-004", "Future Self Video on Attempt"],
        ["UNINSTALL-005", "Public Shame Post on Uninstall"],
        ["UNINSTALL-006", "Relapse Prediction on Uninstall Screen"],
        ["UNINSTALL-007", "Graduated Freedom"],
        ["SAFETY-001",    "Crisis Detection & Soften Mode"],
      ],
    },
  ];

  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">06</div>
      <H1>Feature Index</H1>
      <P className="mt-6 !text-[14px] !leading-[22px] text-fg max-w-[640px]">
        Mapping the master spec to surfaces. Each feature lives on a screen (1.1–5.4) or
        as an intercept (no surface — only triggered). Full 250-feature list is the
        upstream JSON.
      </P>
      <div className="mt-6 grid grid-cols-2 gap-x-12 gap-y-6">
        {groups.map((g) => (
          <div key={g.title}>
            <div className="label-caps text-[9.5px] text-accent mb-2">{g.title}</div>
            <div className="space-y-[1px]">
              {g.lines.map(([id, desc]) => (
                <div key={id} className="flex items-baseline gap-3 text-[11px] py-[2px]">
                  <span className="font-mono text-fg-dim w-[88px] shrink-0">{id}</span>
                  <span className="text-fg-dim">{desc}</span>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </Page>
  );
}

/* ── Roadmap ───────────────────────────────────────────────── */
function Roadmap() {
  const stages: { stage: string; title: string; window: string; summary: string; key: string[] }[] = [
    {
      stage: "Stage 1",
      title: "MVP — A launcher that hurts to leave",
      window: "4–6 weeks",
      summary: "Bare minimum to dogfood. Launcher + restriction primitives + anti-uninstall + onboarding. Author must dogfood without bypass before exit.",
      key: ["CORE-001..005 home, dock, search, usage, widgets", "RESTRICT-001/002/003 lobby, cognitive tax, dimming", "UNINSTALL-001/002 device admin + 72h cooldown", "ONBOARD-001/002 first-run + Why Are You Here", "ETHICS-001 transparency · A11Y-001 accessibility"],
    },
    {
      stage: "Stage 2",
      title: "V1 — Track engine + full psychology + integrations",
      window: "3–4 months",
      summary: "Track engine, full Sad Self, Mantra Gate, productivity loop, basic integrations, encrypted backup. Beta with 100 testers.",
      key: ["TRACK-001..004 levels + baseline detection", "SAD-001..005 Sad/Proud Self + adaptive frequency", "MANTRA-001/002/003 mantra gate", "PSYCH-001..015 identity voting, receipts, future self", "BACKUP-001/002 encrypted backup + export", "INTEG-001 HealthConnect · SLEEP-001 sleep · FIT-001 activity", "PAY-001..003 Play Billing + web Stripe + router", "PLATFORM-002 Chrome-homepage web app"],
    },
    {
      stage: "Stage 3",
      title: "V2 — Social tier, financial stakes, hardware, life integrations",
      window: "6+ months",
      summary: "The moat. Social accountability, escrow stakes, NFC/breath/posture, couples/family, Plaid + life integrations, Built Wrapped MVP, AI insight engine.",
      key: ["SOCIAL-001..036 groups, feed, streaks, sponsor", "FINANCE-001/002 escrow + streak insurance", "HARDWARE-001/002/004 NFC, breath, posture", "COUPLES-001/002 + FAMILY-001 pairing", "INTEG-002 Plaid · INTEG-006 Photos · INTEG-007 Calendar", "FIN-001..006 spending + net worth + subscriptions", "LOC-001..006 location heatmap, third places, travel", "LIFE-001..010 Bloomberg, auto-insights, Wrapped", "ABSURD-001..004 kazoo, narrator, time dilation"],
    },
    {
      stage: "Stage 4",
      title: "V3 — OS for life",
      window: "12+ months",
      summary: "Marketplace, B2B dashboards, web/desktop companion, WearOS, AI Life Coach, multi-domain Wrapped, full social experimentation surfaces.",
      key: ["CREATOR-001/002 track marketplace + mentor subs", "B2B-001/002 therapist + team dashboards", "PLATFORM-001 full read+write web companion", "HARDWARE-003 WearOS", "LIFECYCLE-001 Graduate Mode", "MIND-003..007 voice EQ, dump, vitals, dream, reading", "NUT-001..004 plate-photo, fasting, trigger-foods", "FIN-007..012 future budget, regret, joint mode, Wrapped Money", "LIFE-005..011 annual report, counterfactual twin, AI life coach", "IDENT-001..005 selfie timelapse, promise ratio, courage logs"],
    },
  ];
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">07</div>
      <H1>Roadmap — Four Stages</H1>
      <P className="mt-6 !text-[14px] !leading-[22px] text-fg max-w-[640px]">
        Stage 1 must work on the author's phone before Stage 2 begins. Each subsequent
        stage assumes the prior one's primitives are in production. The 18 "technically
        impossible" features from the master spec are explicitly excluded.
      </P>
      <div className="mt-8 space-y-5">
        {stages.map((s) => (
          <div key={s.stage} className="bg-surface rounded-[14px] p-5">
            <div className="flex items-baseline justify-between mb-1">
              <span className="label-caps text-[10px] text-accent">{s.stage} · {s.window}</span>
              <span className="text-[10px] text-fg-mute">{s.key.length} key feature groups</span>
            </div>
            <div className="text-[15px] font-semibold text-fg mb-2 tracking-heading">{s.title}</div>
            <P className="!text-[12px] !leading-[18px] !mb-3">{s.summary}</P>
            <div className="grid grid-cols-2 gap-x-6 gap-y-[2px] text-[11px] text-fg-dim font-mono">
              {s.key.map((k) => (
                <div key={k} className="leading-[16px]">· {k}</div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </Page>
  );
}

/* ── Tokens ────────────────────────────────────────────────── */
function Tokens() {
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">08</div>
      <H1>Design Tokens</H1>
      <P className="mt-6 !text-[14px] !leading-[22px] text-fg max-w-[600px]">
        Monochrome system with one warm accent. Inter throughout — Light for displays,
        Regular for body, Semi Bold for caps labels.
      </P>

      <H3>Color</H3>
      <div className="grid grid-cols-3 gap-4 max-w-[640px]">
        {(Object.entries(C) as [keyof typeof C, string][]).map(([name, hex]) => (
          <div key={name} className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-lg border border-edge" style={{ background: hex }} />
            <div>
              <div className="text-[12px] font-medium text-fg">{name}</div>
              <div className="text-[10.5px] text-fg-mute font-mono">{hex}</div>
            </div>
          </div>
        ))}
      </div>

      <H3>Type</H3>
      <div className="space-y-2 max-w-[600px]">
        <div className="flex items-baseline gap-4 border-b border-edge/60 pb-2">
          <span className="text-[40px] font-light text-fg tracking-display">5:42</span>
          <span className="text-[11px] text-fg-mute">Inter Light · display · −3% tracking · for clocks, hero numbers</span>
        </div>
        <div className="flex items-baseline gap-4 border-b border-edge/60 pb-2">
          <span className="text-[28px] font-bold text-fg tracking-heading">Headline</span>
          <span className="text-[11px] text-fg-mute">Inter Bold · heading · −2% tracking</span>
        </div>
        <div className="flex items-baseline gap-4 border-b border-edge/60 pb-2">
          <span className="text-[14px] text-fg">Body — regular weight</span>
          <span className="text-[11px] text-fg-mute">Inter Regular · 13–15 px · 18–22 px line</span>
        </div>
        <div className="flex items-baseline gap-4">
          <span className="label-caps text-[10px] text-fg-mute">LABEL CAPS</span>
          <span className="text-[11px] text-fg-mute">Inter Semi Bold · 9–11 px · +0.16em tracking · UPPERCASE</span>
        </div>
      </div>

      <H3>Spacing scale</H3>
      <div className="flex items-end gap-2">
        {[4, 8, 16, 24, 32, 48].map((n) => (
          <div key={n} className="flex flex-col items-center gap-1">
            <div className="bg-accent" style={{ width: 16, height: n }} />
            <span className="text-[10px] text-fg-mute font-mono">{n}</span>
          </div>
        ))}
      </div>

      <H3>Radius</H3>
      <div className="flex items-center gap-3">
        {[12, 14, 16, 28, 48].map((n) => (
          <div key={n} className="flex flex-col items-center gap-1">
            <div className="w-12 h-12 bg-surface border border-edge" style={{ borderRadius: n }} />
            <span className="text-[10px] text-fg-mute font-mono">{n}</span>
          </div>
        ))}
      </div>
    </Page>
  );
}

/* ── Main Doc ──────────────────────────────────────────────── */
export default function Doc() {
  return (
    <div className="doc-root">
      <Cover />
      <Philosophy />
      <UnlockMechanic />
      <PointEconomy />
      <FormMetric />
      <LevelLadder />
      {SCREENS.map((s) => (
        <ScreenPage key={s.num} spec={s} />
      ))}
      <FeatureIndex />
      <Roadmap />
      <Tokens />
    </div>
  );
}
