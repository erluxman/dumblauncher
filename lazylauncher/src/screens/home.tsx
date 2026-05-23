import { Phone, StatusBar, HomeIndicator, TopBadge } from "../components/Phone";
import { GhostButton, WeekBars, WeekDots, Dashed, LedgerRow } from "../components/primitives";

/* 2.1 ─ Home: Morning */
export function HomeMorning() {
  return (
    <Phone label="2.1   Home — Morning">
      <StatusBar time="5:42" />
      <TopBadge level={4} form={73} points={244} />

      <div className="absolute inset-x-0 top-[88px] px-8">
        <div className="label-caps text-[10px] text-accent">MORNING</div>
        <div className="mt-3 text-[80px] leading-none font-light text-fg tracking-display">5:42</div>
        <div className="mt-4 text-[13px] text-fg-dim">Saturday, May 23 · sunrise 6:12</div>
      </div>

      <div className="absolute inset-x-0 top-[290px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">MANTRA · 3 REPS</div>
        <div className="mt-3 text-[20px] leading-[30px] font-light text-fg">
          "i build things.<br />i don’t scroll them."
        </div>
        <div className="mt-5 flex items-center gap-2">
          {[0, 1, 2].map((i) => (
            <div key={i} className={`w-7 h-[2px] rounded-full ${i === 0 ? "bg-accent" : "bg-fg-mute"}`} />
          ))}
          <span className="ml-2 text-[11px] text-fg-mute">1 / 3</span>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[440px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">TODAY’S ONE THING</div>
        <div className="mt-3 bg-surface rounded-[14px] h-[60px] flex items-center px-5 text-[14px] text-fg-dim">
          declare ↦
        </div>
      </div>

      <div className="absolute inset-x-0 top-[576px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-4">WEEK</div>
        <WeekBars
          heights={[24, 28, 32, 8, 0, 0, 0]}
          doneFlags={[true, true, true, false, false, false, false]}
          todayIndex={3}
        />
      </div>

      <div className="absolute inset-x-0 top-[710px] px-8 space-y-2">
        {["Notes", "Calendar", "Mail"].map((a) => (
          <div key={a} className="text-[18px] text-fg">{a}</div>
        ))}
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <GhostButton>start focus · earn your first todo</GhostButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 2.2 ─ Home: Day */
export function HomeDay() {
  return (
    <Phone label="2.2   Home — Day">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />

      <div className="absolute inset-x-0 top-[88px] px-8">
        <div className="label-caps text-[10px] text-accent">DAY</div>
        <div className="mt-5 label-caps text-[9px] text-fg-mute">NOW · 23m LEFT</div>
        <div className="mt-2 text-[26px] font-light text-fg tracking-heading">Q2 design review</div>
        <div className="mt-4 h-[3px] bg-surface2 rounded-full overflow-hidden">
          <div className="h-full w-[66%] bg-accent" />
        </div>
        <div className="mt-3 text-[12px] text-fg-dim">finish → unlocks Twitter 15m</div>
      </div>

      <div className="absolute inset-x-0 top-[252px] px-8 flex items-center gap-3">
        <WeekDots flags={[true, true, true, false, false, false, false]} />
        <span className="text-[11px] text-fg-mute">this week · 3 / 7</span>
      </div>

      <div className="absolute inset-x-0 top-[300px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">NEXT</div>
        <div className="mt-3 space-y-2.5">
          {[
            ["Read Atomic Habits", "30m · +3 pts · Spotify ∞"],
            ["Push-ups × 50", "5m · +2 pts"],
            ["Drink water", "1m · +1 pt"],
          ].map(([t, m]) => (
            <div key={t} className="bg-surface rounded-[12px] px-5 py-3">
              <div className="text-[14px] font-medium text-fg">{t}</div>
              <div className="text-[11px] text-fg-dim mt-[2px]">{m}</div>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[516px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">APPS</div>
        <div className="mt-3 space-y-1">
          {["Messages", "Calendar", "Notes", "Mail", "Music"].map((a) => (
            <div key={a} className="text-[17px] text-fg leading-9">{a}</div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <div className="bg-surface rounded-full h-[52px] grid grid-cols-4 items-center text-[12px] font-medium text-fg-dim">
          {["tasks", "review", "search", "feed"].map((d, i) => (
            <div key={d} className={`text-center ${i ? "border-l border-edge" : ""}`}>{d}</div>
          ))}
        </div>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 2.3 ─ Home: Evening */
export function HomeEvening() {
  return (
    <Phone label="2.3   Home — Evening">
      <StatusBar time="21:47" />
      <TopBadge level={4} form={73} points={244} />

      <div className="absolute inset-x-0 top-[88px] px-8">
        <div className="label-caps text-[10px] text-accent">SHUTDOWN</div>
        <div className="mt-5 text-[14px] text-fg-dim">today</div>
        <div className="text-[64px] leading-none font-light text-fg tracking-display mt-2">21:47</div>
      </div>

      <div className="absolute inset-x-0 top-[238px] px-8">
        <div className="bg-surface rounded-[14px] p-5">
          <div className="label-caps text-[9px] text-fg-mute">TODAY’S LEDGER</div>
          <Dashed />
          <LedgerRow label="shipped" value="4 of 5 todos" amount="+12" />
          <LedgerRow label="surrendered" value="morning run" amount="−3" />
          <LedgerRow label="focus" value="4h 12m" amount="·" />
          <LedgerRow label="twitter" value="12m" amount="−12" />
          <Dashed />
        </div>
      </div>

      <div className="absolute inset-x-0 top-[488px] px-8">
        <div className="label-caps text-[9px] text-fg-mute">NET TODAY</div>
        <div className="mt-2 text-[28px] font-light text-fg tracking-heading">−3 pts</div>
        <div className="mt-1 text-[12px] text-fg-dim">form 73% → 71%</div>
      </div>

      <div className="absolute inset-x-0 top-[608px] px-8">
        <div className="label-caps text-[9px] text-fg-mute">TOMORROW’S ONE THING</div>
        <div className="mt-3 bg-surface rounded-[14px] h-[60px] flex items-center px-5 text-[14px] text-fg-dim">
          declare ↦
        </div>
      </div>

      <div className="absolute inset-x-0 top-[718px] px-8 text-[12px] text-fg-dim">
        in 1h 13m: dream mode begins
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <GhostButton>wind down · 4-7-8 breath</GhostButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 2.4 ─ Home: Night */
export function HomeNight() {
  return (
    <Phone label="2.4   Home — Night">
      <StatusBar time="23:14" />
      <div className="absolute inset-x-0 top-[88px] px-8">
        <div className="label-caps text-[10px] text-fg-dim">DREAM MODE</div>
        <div className="mt-[120px] text-[96px] leading-none font-light text-fg-dim tracking-display">
          23:14
        </div>
      </div>

      <div className="absolute inset-x-0 top-[366px] px-8">
        <div className="text-[12px] text-fg-mute">tomorrow</div>
        <div className="mt-4 space-y-3">
          {[
            ["05:30", "alarm  ·  mantra unlock"],
            ["06:00", "declare one thing"],
            ["09:00", "deep work block · 2h"],
            ["12:30", "lunch + walk"],
          ].map(([t, l]) => (
            <div key={t} className="flex gap-7 text-[13px]">
              <span className="text-fg-dim font-medium w-[60px] tabular-nums">{t}</span>
              <span className="text-fg-mute">{l}</span>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[590px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">BREATH</div>
        <div className="mt-4 flex items-end gap-3">
          <div className="w-14 h-14 rounded-full border border-fg-dim flex items-center justify-center text-[18px] text-fg-dim font-light">4</div>
          <div className="w-20 h-20 rounded-full border border-fg-dim flex items-center justify-center text-[22px] text-fg-dim font-light">7</div>
          <div className="w-16 h-16 rounded-full border border-fg-dim flex items-center justify-center text-[20px] text-fg-dim font-light">8</div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[120px] px-8 text-[11px] text-fg-mute leading-5">
        apps sleep until 5:30am<br />
        only kindle and alarm respond
      </div>
      <HomeIndicator />
    </Phone>
  );
}
