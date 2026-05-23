import { Phone, StatusBar, HomeIndicator, TopBadge } from "../components/Phone";
import { HollowButton, PrimaryButton } from "../components/primitives";

/* 5.1 ─ Feed */
export function Feed() {
  const events: [string, string, string, string][] = [
    ["PA", "Pawan", "completed Q2 design review · +5 pts", "2m"],
    ["SD", "Sara",  "surrendered morning run · day 3 ended", "8m"],
    ["BJ", "Bijou", "reached top 5% this week", "1h"],
    ["JD", "John",  "started a new project: pottery", "3h"],
    ["AC", "Anuj",  "12-day streak on read 30m", "5h"],
    ["KP", "Kamala","skipped 4 unlocks · sad self surfaced", "yesterday"],
    ["EV", "Evana", "level up · L3 → L4", "yesterday"],
  ];
  return (
    <Phone label="5.1   Feed">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />

      <div className="absolute inset-x-0 top-[88px] px-8">
        <div className="label-caps text-[10px] text-accent">FEED</div>
        <div className="mt-3 text-[30px] font-light text-fg tracking-heading">3 groups</div>
        <div className="mt-1 text-[12px] text-fg-dim">reverse-chronological · no algorithm</div>
      </div>

      <div className="absolute inset-x-0 top-[222px] px-8 space-y-[10px]">
        {events.map(([ini, name, action, time], i) => (
          <div key={i} className="bg-surface rounded-[12px] px-4 py-3 flex items-start gap-3">
            <div className="w-9 h-9 rounded-full border border-edge flex items-center justify-center text-[11px] text-fg-dim font-medium">{ini}</div>
            <div className="flex-1 min-w-0">
              <div className="text-[13px] text-fg leading-[18px]">
                <span className="font-medium">{name}</span>{" "}
                <span className="text-fg-dim">{action}</span>
              </div>
            </div>
            <div className="text-[11px] text-fg-mute pt-1 shrink-0">{time}</div>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <div className="bg-surface rounded-full h-[52px] grid grid-cols-3 items-center text-[12px] font-medium text-fg-dim">
          <div className="text-center">feed</div>
          <div className="text-center border-l border-edge text-fg">leaderboard</div>
          <div className="text-center border-l border-edge">profile</div>
        </div>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 5.2 ─ Search / Menu */
export function SearchMenu() {
  return (
    <Phone label="5.2   Search / Menu">
      <StatusBar />
      <div className="absolute inset-x-0 top-[80px] px-8">
        <div className="bg-surface rounded-full h-[52px] flex items-center px-5 gap-3">
          <span className="text-fg-mute">⌕</span>
          <span className="text-[15px] text-fg-dim">type to find anything</span>
          <span className="ml-auto text-[10px] text-fg-mute">esc</span>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[168px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">RECENT</div>
        <div className="mt-3 space-y-[6px] text-[14px]">
          {["form", "review · spending", "leaderboard", "fasting", "sad self log"].map((r) => (
            <div key={r} className="text-fg-dim hover:text-fg cursor-default">{r}</div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[348px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">AT THIS HOUR (Sat 14:00)</div>
        <div className="mt-3 space-y-[6px] text-[14px]">
          {[
            ["start focus block", "2h until lunch"],
            ["log workout", "you missed yesterday"],
            ["call Sarah", "18 days since last contact"],
          ].map(([t, m]) => (
            <div key={t} className="flex items-baseline gap-3">
              <span className="text-fg">{t}</span>
              <span className="text-fg-mute text-[11px]">{m}</span>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[546px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">INDEX · everything is here</div>
        <div className="mt-3 grid grid-cols-2 gap-x-6 gap-y-1 text-[12px]">
          {[
            ["TODAY",    "tasks · log · intent · one thing"],
            ["YOU",      "level · form · points · streak"],
            ["DATA",     "review · journal · wrapped · stats"],
            ["PEOPLE",   "feed · leaderboard · pair · sponsor"],
            ["MONEY",    "spending · subs · regret"],
            ["HEALTH",   "sleep · mood · workout · food"],
            ["TOOLS",    "timer · breath · mantra · boredom"],
            ["SETTINGS", "vip · transparency · passphrase"],
          ].map(([cat, items]) => (
            <div key={cat} className="leading-[18px] py-1.5">
              <div className="label-caps text-[9px] text-accent">{cat}</div>
              <div className="text-fg-dim mt-[2px]">{items}</div>
            </div>
          ))}
        </div>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 5.3 ─ Lobby (intercept) */
export function LobbyIntercept() {
  return (
    <Phone label="5.3   Lobby — Intercept">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 flex items-baseline justify-between">
        <div className="label-caps text-[10px] text-danger">LOBBY</div>
        <div className="text-[11px] text-fg-dim">form 73% · −20s wait</div>
      </div>

      <div className="absolute inset-x-0 top-[180px] px-8">
        <div className="text-[26px] leading-[34px] font-light text-fg tracking-heading">
          Twitter<br />
          <span className="text-fg-dim">wants to open.</span>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[358px] px-8">
        <div className="text-[88px] leading-none font-light text-fg tracking-display tabular-nums">0:47</div>
        <div className="mt-3 text-[12px] text-fg-dim">of 1:10 remaining · cannot skip</div>
        <div className="mt-4 h-[3px] bg-surface2 rounded-full overflow-hidden">
          <div className="h-full w-[34%] bg-accent" />
        </div>
      </div>

      <div className="absolute inset-x-0 top-[564px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">DECLARE YOUR INTENT</div>
        <div className="mt-2 h-px bg-fg-mute" />
        <div className="mt-3 text-[15px] text-fg-dim flex items-center">
          check DMs from Anuj
          <span className="ml-1 inline-block w-[2px] h-[16px] bg-accent animate-pulse" />
        </div>
        <div className="mt-3 text-[11px] text-fg-mute">15 min budget · 1 budget used today (47m)</div>
      </div>

      <div className="absolute inset-x-0 top-[748px] px-8 text-[11px] text-fg-mute leading-5">
        if you finish a todo first, this wait is skipped.
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>← back · do something better</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 5.4 ─ Receipt (after closing distraction) */
export function ReceiptAfterClose() {
  return (
    <Phone label="5.4   Receipt — After Close">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 flex items-baseline justify-between">
        <div className="label-caps text-[10px] text-accent">RECEIPT</div>
        <div className="text-[11px] text-fg-dim">just closed Twitter</div>
      </div>

      <div className="absolute inset-x-0 top-[178px] px-8">
        <div className="text-[64px] leading-none font-light text-fg tracking-display tabular-nums">12 min</div>
        <div className="mt-3 text-[13px] text-fg-dim">in Twitter · session 1 of 1 today</div>
      </div>

      <div className="absolute inset-x-0 top-[332px] px-8">
        <div className="bg-surface rounded-[14px] p-5 dashed-row text-[12px] text-fg-dim leading-6">
          <div className="flex justify-between text-fg-dim font-sans">
            <span>cost</span><span className="text-danger tabular-nums">−12 pts</span>
          </div>
          <div className="flex justify-between text-fg-dim font-sans">
            <span>balance</span><span className="text-fg tabular-nums">232</span>
          </div>
          <div className="flex justify-between text-fg-dim font-sans">
            <span>budget left</span><span className="text-fg-dim">3 min</span>
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[500px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">EQUIVALENTS · WHAT 12 MIN BOUGHT</div>
        <div className="mt-3 space-y-[6px] text-[13px] text-fg-dim leading-6">
          <p>· 4 pages of <span className="text-fg">Atomic Habits</span></p>
          <p>· 1 phone call to <span className="text-fg">Sarah</span></p>
          <p>· 40 push-ups, or 2 short workouts</p>
          <p>· $4.50 of your hourly rate</p>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[704px] px-8 text-[11px] text-fg-mute leading-5">
        you shipped <span className="text-fg-dim">0 todos</span> in this session.
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 space-y-2">
        <PrimaryButton>start a 5m todo · earn it back</PrimaryButton>
        <HollowButton className="!h-11 !text-[12px]">continue to home</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}
