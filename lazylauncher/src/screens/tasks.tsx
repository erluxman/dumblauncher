import { Phone, StatusBar, HomeIndicator, TopBadge } from "../components/Phone";
import { GhostButton, HollowButton, PrimaryButton, WeekDots, TaskRow } from "../components/primitives";

/* 3.1 ─ Tasks: Today’s List */
export function TasksList() {
  return (
    <Phone label="3.1   Tasks — Today">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />

      <div className="absolute inset-x-0 top-[88px] px-8">
        <div className="label-caps text-[10px] text-accent">TODAY</div>
        <div className="mt-3 text-[30px] font-light text-fg tracking-heading">5 todos</div>
        <div className="mt-1 text-[12px] text-fg-dim">created −15 pts · payouts +14 if all complete</div>
      </div>

      <div className="absolute inset-x-0 top-[208px] px-8 flex items-center gap-3">
        <WeekDots flags={[true, true, true, false, false, false, false]} />
        <span className="text-[11px] text-fg-mute">3 / 7 days streak this week</span>
      </div>

      <div className="absolute inset-x-0 top-[252px] px-8 space-y-2.5">
        <div className="bg-surface rounded-[12px] px-5 py-3 border border-accent/30">
          <div className="flex items-baseline justify-between">
            <span className="label-caps text-[9px] text-accent">LIVE · 23m LEFT</span>
            <span className="text-[11px] text-fg-dim">+5 pts</span>
          </div>
          <div className="text-[15px] font-medium text-fg mt-1">Q2 design review</div>
          <div className="text-[11px] text-fg-dim mt-[2px]">unlocks Twitter 15m on complete</div>
        </div>
        <TaskRow title="Read Atomic Habits" meta="30m · +3 pts · unlocks Spotify ∞" />
        <TaskRow title="Push-ups × 50" meta="5m · +2 pts" />
        <TaskRow title="Drink water" meta="1m · +1 pt" />
        <TaskRow title="Walk after lunch" meta="20m · +2 pts · done" done />
      </div>

      <div className="absolute inset-x-0 bottom-[120px] px-8">
        <div className="grid grid-cols-3 gap-2">
          <HollowButton className="!h-11 !text-[11px]">+ todo (−3)</HollowButton>
          <HollowButton className="!h-11 !text-[11px]">+ project (−30)</HollowButton>
          <HollowButton className="!h-11 !text-[11px]">+ habit (−50)</HollowButton>
        </div>
        <div className="mt-3 text-[10.5px] text-fg-mute text-center">balance 244 pts</div>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 3.2 ─ Tasks: Create Modal */
export function TaskCreate() {
  return (
    <Phone label="3.2   Tasks — Create">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 flex items-baseline justify-between">
        <div className="label-caps text-[10px] text-accent">CREATE TODO</div>
        <div className="text-[11px] text-fg-dim">balance 244</div>
      </div>

      <div className="absolute inset-x-0 top-[160px] px-8">
        <div className="text-[28px] font-light text-fg tracking-heading">name your task</div>
        <div className="mt-6 h-px bg-fg-mute" />
        <div className="mt-3 text-[17px] text-fg-dim flex items-center">
          finish chapter 3 outline
          <span className="ml-1 inline-block w-[2px] h-[18px] bg-accent animate-pulse" />
        </div>
      </div>

      <div className="absolute inset-x-0 top-[348px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">ESTIMATE</div>
        <div className="mt-3 grid grid-cols-5 gap-2">
          {["5m", "15m", "30m", "1h", "2h"].map((t, i) => (
            <div key={t} className={`h-10 rounded-[10px] flex items-center justify-center text-[13px] ${i === 2 ? "bg-accent text-ink font-semibold" : "bg-surface text-fg-dim"}`}>
              {t}
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[464px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">UNLOCK ON COMPLETE</div>
        <div className="mt-3 bg-surface rounded-[12px] px-5 py-4 flex items-center justify-between">
          <div>
            <div className="text-[14px] font-medium text-fg">Twitter · 15m</div>
            <div className="text-[11px] text-fg-dim mt-[2px]">or pick another app · or none</div>
          </div>
          <div className="text-[18px] text-fg-mute">↦</div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[598px] px-8">
        <div className="bg-surface rounded-[14px] p-5">
          <div className="label-caps text-[9px] text-fg-mute">PREVIEW</div>
          <div className="mt-3 flex items-baseline justify-between text-[13px]">
            <span className="text-fg-dim">cost now</span>
            <span className="text-danger font-semibold tabular-nums">−3 pts</span>
          </div>
          <div className="mt-1 flex items-baseline justify-between text-[13px]">
            <span className="text-fg-dim">on completion</span>
            <span className="text-success font-semibold tabular-nums">+5 pts</span>
          </div>
          <div className="mt-1 flex items-baseline justify-between text-[13px]">
            <span className="text-fg-dim">if late ≤ 1h</span>
            <span className="text-fg-mute font-semibold">refund</span>
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <PrimaryButton>commit · spend 3 pts</PrimaryButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 3.3 ─ Tasks: Completion */
export function TaskCompletion() {
  return (
    <Phone label="3.3   Tasks — Completion">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 flex items-baseline justify-between">
        <div className="label-caps text-[10px] text-success">COMPLETE</div>
        <div className="text-[11px] text-fg-dim">just now</div>
      </div>

      <div className="absolute left-8 top-[180px] w-16 h-16 rounded-full border-2 border-success flex items-center justify-center">
        <span className="text-success text-[28px] leading-none">✓</span>
      </div>

      <div className="absolute inset-x-0 top-[280px] px-8">
        <div className="text-[24px] leading-[32px] font-light text-fg tracking-heading">
          Q2 design review<br />
          <span className="text-fg-dim">shipped.</span>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[424px] px-8">
        <div className="bg-surface rounded-[14px] p-5 space-y-2">
          <div className="flex items-baseline justify-between text-[13px]">
            <span className="text-fg-dim">payout</span>
            <span className="text-success font-semibold tabular-nums">+5 pts</span>
          </div>
          <div className="flex items-baseline justify-between text-[13px]">
            <span className="text-fg-dim">balance</span>
            <span className="text-fg font-semibold tabular-nums">246</span>
          </div>
          <div className="flex items-baseline justify-between text-[13px]">
            <span className="text-fg-dim">streak</span>
            <span className="text-fg font-semibold">12 / 14 to L5</span>
          </div>
          <div className="flex items-baseline justify-between text-[13px]">
            <span className="text-fg-dim">form</span>
            <span className="text-success font-semibold">73% → 75%</span>
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[640px] px-8">
        <div className="bg-ink border border-accent/30 rounded-[14px] p-5">
          <div className="label-caps text-[9px] text-accent">UNLOCKED</div>
          <div className="mt-2 text-[16px] font-medium text-fg">Twitter · 15 min</div>
          <div className="mt-[2px] text-[11px] text-fg-dim">expires when timer runs out, or end of day</div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 space-y-2">
        <PrimaryButton>open Twitter</PrimaryButton>
        <HollowButton className="!h-11 !text-[12px]">skip · pocket the time</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 3.4 ─ Tasks: Surrender */
export function TaskSurrender() {
  return (
    <Phone label="3.4   Tasks — Surrender">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 flex items-baseline justify-between">
        <div className="label-caps text-[10px] text-danger">SURRENDERING</div>
        <div className="text-[11px] text-fg-dim">are you sure?</div>
      </div>

      <div className="absolute inset-x-0 top-[200px] px-8">
        <div className="text-[26px] leading-[34px] font-light text-fg tracking-heading">
          you’re giving up<br />
          <span className="text-fg-dim">"morning run"</span><br />
          at 22%.
        </div>
      </div>

      <div className="absolute inset-x-0 top-[428px] px-8">
        <div className="bg-surface rounded-[14px] p-5 space-y-2">
          <div className="flex items-baseline justify-between text-[13px]">
            <span className="text-fg-dim">forfeit cost</span>
            <span className="text-danger font-semibold tabular-nums">−3 pts</span>
          </div>
          <div className="flex items-baseline justify-between text-[13px]">
            <span className="text-fg-dim">streak impact</span>
            <span className="text-danger font-semibold">day 3 ends</span>
          </div>
          <div className="flex items-baseline justify-between text-[13px]">
            <span className="text-fg-dim">visible to</span>
            <span className="text-fg font-semibold">3 group members</span>
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[616px] px-8 text-[12px] text-fg-mute leading-5">
        this will post to your group feed: <span className="text-fg-dim italic">"surrendered morning run · day 3"</span>.
        Sara reacted to your last completion.
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 space-y-2">
        <PrimaryButton>keep going · 5 more min</PrimaryButton>
        <HollowButton className="!text-danger">surrender anyway</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}
