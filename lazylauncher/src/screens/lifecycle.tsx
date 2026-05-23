import { Phone, StatusBar, HomeIndicator, TopBadge } from "../components/Phone";
import { GhostButton, HollowButton, PrimaryButton, Dashed } from "../components/primitives";

/* 7.1 — Uninstall attempt: 72hr gate + future-self video */
export function UninstallGate() {
  return (
    <Phone label="7.1   Uninstall — Gate">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 flex items-baseline justify-between">
        <div className="label-caps text-[10px] text-danger">UNINSTALL · DAY 1 OF 3</div>
        <div className="text-[11px] text-fg-mute">72h cooldown · cannot skip</div>
      </div>

      <div className="absolute inset-x-0 top-[160px] px-8">
        <div className="text-[26px] leading-[34px] font-light text-fg tracking-heading">
          you’re trying to<br />leave.
        </div>
        <p className="mt-4 text-[13px] text-fg-dim leading-[20px]">
          before you do, your past self has a 30-second message for you.
          watch it through. there is no skip.
        </p>
      </div>

      <div className="absolute inset-x-0 top-[348px] px-8">
        <div className="relative h-[200px] rounded-[18px] border border-edge bg-[#0c0c0c] overflow-hidden">
          <div className="absolute inset-0 flex items-center justify-center">
            <span className="text-fg-dim text-[42px]">▶</span>
          </div>
          <div className="absolute top-3 left-3 right-3 flex items-baseline justify-between">
            <span className="label-caps text-[9px] text-fg-mute">FUTURE-SELF · APR 14</span>
            <span className="label-caps text-[9px] text-accent">0:14 / 0:30</span>
          </div>
          <div className="absolute bottom-3 left-3 right-3 h-[2px] bg-edge rounded-full overflow-hidden">
            <div className="h-full w-[47%] bg-accent" />
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[580px] px-8">
        <div className="bg-surface rounded-[14px] p-5">
          <div className="label-caps text-[9px] text-fg-mute">YOUR REASON FOR INSTALLING</div>
          <p className="mt-2 text-[12.5px] text-fg leading-[19px] italic">
            "my daughter said put the phone down twice this week. i don’t want to be
            the dad who watches reels at dinner."
          </p>
          <div className="mt-3 text-[10.5px] text-fg-mute">
            captured Feb 3, 2026 · 84 days ago
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 space-y-2">
        <PrimaryButton>change my mind · stay</PrimaryButton>
        <HollowButton className="!h-11 !text-[12px] !text-fg-mute">continue uninstall · 47:59 left</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 7.2 — Uninstall: group vote */
export function UninstallVote() {
  const votes = [
    ["Anuj (sponsor)", "voted NO", "danger"],
    ["Sara",           "voted NO", "danger"],
    ["Bijou",          "voted NO", "danger"],
    ["John",           "pending",  "mute"],
    ["Pawan",          "voted YES", "ok"],
  ];
  return (
    <Phone label="7.2   Uninstall — Group Vote">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 flex items-baseline justify-between">
        <div className="label-caps text-[10px] text-danger">UNINSTALL · GROUP VOTE</div>
        <div className="text-[11px] text-fg-mute">SOCIAL-002</div>
      </div>

      <div className="absolute inset-x-0 top-[160px] px-8">
        <div className="text-[24px] leading-[32px] font-light text-fg tracking-heading">
          your group must approve.<br /><span className="text-fg-dim">majority of 5.</span>
        </div>
        <p className="mt-3 text-[12.5px] text-fg-dim leading-[19px]">
          your accountability group has 24 hours to vote. you cannot uninstall
          until 3 of 5 say yes.
        </p>
      </div>

      <div className="absolute inset-x-0 top-[336px] px-8">
        <div className="bg-surface rounded-[14px] p-5">
          <div className="flex items-baseline justify-between">
            <div className="label-caps text-[9px] text-fg-mute">CURRENT TALLY</div>
            <div className="text-[10px] text-fg-mute font-mono">closes in 18h 22m</div>
          </div>
          <div className="mt-3 flex items-baseline gap-3">
            <div>
              <div className="text-[36px] font-light text-success leading-none tabular-nums">1</div>
              <div className="text-[10px] text-fg-mute label-caps mt-1">YES</div>
            </div>
            <div className="text-[24px] text-fg-mute font-light leading-none">/</div>
            <div>
              <div className="text-[36px] font-light text-danger leading-none tabular-nums">3</div>
              <div className="text-[10px] text-fg-mute label-caps mt-1">NO</div>
            </div>
            <div className="text-[24px] text-fg-mute font-light leading-none">/</div>
            <div>
              <div className="text-[36px] font-light text-fg-dim leading-none tabular-nums">1</div>
              <div className="text-[10px] text-fg-mute label-caps mt-1">PENDING</div>
            </div>
          </div>
          <div className="mt-4 text-[11px] text-fg-mute">need 2 more YES votes to uninstall.</div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[580px] px-8 space-y-[6px]">
        {votes.map(([name, status, tone], i) => (
          <div key={i} className="bg-ink border border-edge rounded-[10px] px-4 py-2 flex items-center justify-between">
            <span className="text-[13px] text-fg">{name}</span>
            <span className={`text-[11px] label-caps ${tone === "danger" ? "text-danger" : tone === "ok" ? "text-success" : "text-fg-mute"}`}>{status}</span>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>← back · keep going</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 7.3 — Wrapped (year in review) */
export function Wrapped() {
  return (
    <Phone label="7.3   Built Wrapped">
      <StatusBar />
      <TopBadge level={6} form={78} points={1247} />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        BUILT WRAPPED · 2026 · SOCIAL-010
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[40px] leading-[1] font-bold text-fg tracking-display">your year,<br />in receipts.</div>
      </div>

      <div className="absolute inset-x-0 top-[270px] px-8 grid grid-cols-2 gap-3">
        {[
          ["FOCUSED", "412h", "+89h vs 2025"],
          ["SAVED",   "$2,140", "subscription hunter"],
          ["READ",    "23 books", "↑ 9"],
          ["WORKOUTS", "184", "↑ 41"],
          ["TOP DISTRACTION", "Twitter", "37h all year"],
          ["WORST DAY", "Mar 14", "−47 pts · surrender"],
        ].map(([l, v, s]) => (
          <div key={l} className="bg-surface rounded-[14px] p-4">
            <div className="label-caps text-[9px] text-fg-mute">{l}</div>
            <div className="mt-1 text-[22px] font-light text-fg tracking-display tabular-nums">{v}</div>
            <div className="text-[10px] text-fg-dim mt-[2px]">{s}</div>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 top-[564px] px-8">
        <div className="bg-surface rounded-[14px] p-5">
          <div className="label-caps text-[9px] text-fg-mute">PATTERN OF THE YEAR</div>
          <div className="mt-2 text-[14px] text-fg leading-[20px] italic">
            "you fail Twitter most on Sundays between 2 and 4pm, especially after
            Sara’s notifications. fixed it in May."
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 space-y-2">
        <PrimaryButton>share my card</PrimaryButton>
        <HollowButton className="!h-11 !text-[12px]">see all chapters · 12 of 12</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 7.4 — Public Builder Profile */
export function BuilderProfile() {
  const ach = [
    ["365-day streak", "first habit"],
    ["3 shipped projects", "verified"],
    ["12 hashtags", "completed"],
    ["sponsor of 2", "verified"],
  ];
  return (
    <Phone label="7.4   Public Builder Profile">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 flex items-baseline justify-between">
        <div className="label-caps text-[10px] text-accent">PUBLIC · built.app/erluxman</div>
        <div className="text-[10px] text-fg-mute">SOCIAL-009 / 011</div>
      </div>

      <div className="absolute inset-x-0 top-[136px] px-8">
        <div className="flex items-end gap-3">
          <div className="w-16 h-16 rounded-full border border-edge bg-surface flex items-center justify-center text-[22px] font-bold text-fg">e</div>
          <div className="pb-1">
            <div className="text-[22px] font-semibold text-fg leading-tight tracking-heading">erluxman</div>
            <div className="text-[11px] text-fg-dim">verified · top 5% builder · L6</div>
          </div>
          <div className="ml-auto w-7 h-7 rounded-full border-2 border-accent text-accent text-[14px] flex items-center justify-center pb-[2px]">✓</div>
        </div>

        <div className="mt-5 text-[12.5px] text-fg-dim leading-[19px]">
          <span className="label-caps text-[9px] text-fg-mute mr-2">ANTI-BIO</span>
          haven’t opened Instagram in 287 days. haven’t skipped a Tuesday workout in 41 weeks.
          haven’t had a coffee after 2pm in 6 months.
        </div>
      </div>

      <div className="absolute inset-x-0 top-[324px] px-8 grid grid-cols-3 gap-2">
        {[["412h","FOCUS Y1"], ["1247","POINTS"], ["78%","FORM"]].map(([v, l]) => (
          <div key={l} className="bg-surface rounded-[12px] p-3">
            <div className="text-[18px] font-light text-fg tracking-display tabular-nums">{v}</div>
            <div className="text-[9px] text-fg-mute label-caps mt-1">{l}</div>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 top-[428px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-3">ACHIEVEMENTS · 4 OF 18</div>
        <div className="space-y-[6px]">
          {ach.map(([a, m]) => (
            <div key={a} className="bg-surface rounded-[12px] px-4 py-2 flex items-baseline justify-between">
              <span className="text-[12.5px] text-fg">{a}</span>
              <span className="text-[10px] text-fg-mute">{m}</span>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[652px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">RECEIPT WALL · 18 RECENT</div>
        <div className="grid grid-cols-9 gap-[3px]">
          {Array.from({ length: 27 }).map((_, i) => (
            <div key={i} className={`h-4 rounded-[2px] ${[1,4,5,8,11,12,15,18,20,22,25].includes(i) ? "bg-accent" : "bg-surface2"}`} />
          ))}
        </div>
        <div className="mt-2 text-[10px] text-fg-mute">phone-down within 3 min · 11 / 27 sessions</div>
      </div>

      <HomeIndicator />
    </Phone>
  );
}
