import { Phone, StatusBar, HomeIndicator, TopBadge } from "../components/Phone";
import { HollowButton } from "../components/primitives";

/* 4.1 ─ Level */
export function Level() {
  const levels = [
    ["L1", "15m / day", "starter — 1 todo"],
    ["L2", "30m / day", "2 todos · first habit"],
    ["L3", "1h / day", "3 todos · 1 habit"],
    ["L4", "2h / day", "5 todos · 2 habits · 1 project"],
    ["L5", "3h / day", "7 todos · 3 habits"],
    ["L6", "4h / day", "first project deadlines"],
    ["L7", "5h / day", "team commitments"],
    ["L8", "6h / day", "deep work blocks"],
    ["L9", "7h / day", "near-grind"],
    ["L10", "15m / day", "graduate · habits internalized"],
  ];
  return (
    <Phone label="4.1   Level">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />

      <div className="absolute inset-x-0 top-[88px] px-8">
        <div className="label-caps text-[10px] text-accent">LEVEL</div>
        <div className="mt-3 flex items-baseline gap-3">
          <span className="text-[64px] leading-none font-light text-fg tracking-display">L4</span>
          <span className="text-[14px] text-fg-dim">of 10</span>
        </div>
        <div className="mt-3 text-[13px] text-fg-dim">12 / 14 days to L5 · 2 fail days → demote to L3</div>
        <div className="mt-3 flex gap-1.5">
          {Array.from({ length: 14 }).map((_, i) => (
            <span key={i} className={`block w-2.5 h-2.5 rounded-full ${i < 12 ? "bg-accent" : "border border-fg-mute"}`} />
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[298px] px-8 right-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">LADDER</div>
        <div className="space-y-[2px]">
          {levels.map(([lv, t, d], i) => {
            const current = i === 3;
            const passed = i < 3;
            return (
              <div key={lv} className={`flex items-baseline gap-3 text-[12px] py-[5px] px-2 rounded ${current ? "bg-surface" : ""}`}>
                <span className={`font-mono w-7 tabular-nums ${current ? "text-fg" : passed ? "text-fg-dim" : "text-fg-mute"}`}>{lv}</span>
                <span className={`font-medium w-[64px] tabular-nums ${current ? "text-fg" : passed ? "text-fg-dim" : "text-fg-mute"}`}>{t}</span>
                <span className={`flex-1 ${current ? "text-fg-dim" : "text-fg-mute"}`}>{d}</span>
                {current && <span className="text-[10px] label-caps text-accent">YOU</span>}
              </div>
            );
          })}
        </div>
      </div>

      <HomeIndicator />
    </Phone>
  );
}

/* 4.2 ─ Form */
export function Form() {
  return (
    <Phone label="4.2   Form">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />

      <div className="absolute inset-x-0 top-[88px] px-8">
        <div className="label-caps text-[10px] text-accent">FORM</div>
        <div className="mt-3 flex items-baseline gap-3">
          <span className="text-[80px] leading-none font-light text-fg tracking-display">73%</span>
          <span className="text-[14px] text-fg-dim">trending ↗</span>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[260px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">FORMULA</div>
        <div className="mt-3 font-mono text-[12px] text-fg-dim leading-6">
          F = 0.5 · completion<br />
          &nbsp;&nbsp;&nbsp;&nbsp;+ 0.3 · streak<br />
          &nbsp;&nbsp;&nbsp;&nbsp;− 0.2 · misses
        </div>
      </div>

      <div className="absolute inset-x-0 top-[420px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">14 DAYS</div>
        <div className="mt-3 flex gap-1.5">
          {[1,1,1,0,1,1,1,1,0,1,1,1,1,0].map((d, i) => (
            <span key={i} className={`block w-3 h-3 rounded-full ${d ? "bg-accent" : "bg-fg-mute"}`} />
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[520px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">EFFECTS · YOUR FORM RIGHT NOW</div>
        <div className="mt-3 bg-surface rounded-[14px] p-5 space-y-3 text-[13px]">
          <div className="flex items-baseline justify-between">
            <span className="text-fg-dim">lobby wait</span>
            <span className="text-fg">45s <span className="text-fg-mute">(of 90s)</span></span>
          </div>
          <div className="flex items-baseline justify-between">
            <span className="text-fg-dim">sad you</span>
            <span className="text-fg">quiet · 1×/day</span>
          </div>
          <div className="flex items-baseline justify-between">
            <span className="text-fg-dim">L5 eligibility</span>
            <span className="text-success">yes · need F &gt; 60%</span>
          </div>
          <div className="flex items-baseline justify-between">
            <span className="text-fg-dim">at &lt; 40%</span>
            <span className="text-danger">breath gate · sad self loudens</span>
          </div>
          <div className="flex items-baseline justify-between">
            <span className="text-fg-dim">at &gt; 80%</span>
            <span className="text-success">instant unlocks · advanced prompts</span>
          </div>
        </div>
      </div>

      <HomeIndicator />
    </Phone>
  );
}

/* 4.3 ─ Review */
export function Review() {
  return (
    <Phone label="4.3   Review">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />

      <div className="absolute inset-x-0 top-[88px] px-8">
        <div className="label-caps text-[10px] text-accent">REVIEW</div>
        <div className="mt-3 text-[30px] font-light text-fg tracking-heading">this week</div>
        <div className="mt-1 text-[12px] text-fg-dim">tap any line for the breakdown</div>
      </div>

      <div className="absolute inset-x-0 top-[230px] px-8">
        <div className="space-y-[14px] text-[15px] leading-[22px]">
          <p className="text-fg">you focused <span className="text-accent font-medium">4h 12m</span> across <span className="text-fg">14 deep blocks</span>.</p>
          <p className="text-fg">slept <span className="text-fg-dim">7h 04m</span> avg · consistency <span className="text-success">84%</span>.</p>
          <p className="text-fg">spent <span className="text-fg-dim">$312</span> — savings rate <span className="text-success">38%</span>.</p>
          <p className="text-fg">shipped <span className="text-fg">14 todos</span>, surrendered <span className="text-danger">2</span>. net <span className="text-success">+47 pts</span>.</p>
          <p className="text-fg">mood ↑ <span className="text-success">12%</span> vs last week. <span className="text-fg-dim">2-4pm still your dip.</span></p>
          <p className="text-fg">read <span className="text-fg-dim">23 pages</span> · captured <span className="text-fg-dim">8 highlights</span>.</p>
          <p className="text-fg">4 workouts · <span className="text-fg-dim">41k steps</span> · HRV <span className="text-fg-dim">47ms</span> avg.</p>
          <p className="text-fg-dim italic">you haven’t talked to <span className="text-fg">Sarah</span> in 18 days.</p>
          <p className="text-fg-dim italic">pattern: you fail Twitter most after her notifications.</p>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 flex items-center justify-between text-[11px] text-fg-mute">
        <span>← month</span>
        <span>this week ●</span>
        <span>year →</span>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 4.4 ─ Review Detail (one sentence expanded) */
export function ReviewDetail() {
  return (
    <Phone label="4.4   Review — Detail">
      <StatusBar />
      <div className="absolute left-8 top-[60px] text-[12px] text-fg-dim">← back to review</div>

      <div className="absolute inset-x-0 top-[100px] px-8">
        <div className="label-caps text-[10px] text-accent">SPENT</div>
        <div className="mt-3 text-[40px] font-light text-fg tracking-display">$312</div>
        <div className="mt-1 text-[13px] text-fg-dim">this week · last week $278 · ↑ 12%</div>
      </div>

      <div className="absolute inset-x-0 top-[252px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-3">BY CATEGORY</div>
        <div className="space-y-3">
          {[
            ["food", "$142", 46],
            ["transit", "$58", 19],
            ["coffee", "$47", 15],
            ["software", "$32", 10],
            ["misc", "$33", 10],
          ].map(([cat, val, pct]) => (
            <div key={cat as string}>
              <div className="flex items-baseline justify-between text-[13px]">
                <span className="text-fg">{cat}</span>
                <span className="text-fg-dim tabular-nums">{val} · {pct}%</span>
              </div>
              <div className="mt-1 h-[2px] bg-surface2 rounded-full overflow-hidden">
                <div className="h-full bg-accent rounded-full" style={{ width: `${pct}%` }} />
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[576px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-3">REGRETS PENDING REVIEW</div>
        <div className="space-y-2">
          {[
            ["$120 · jacket", "24h ago · rate it"],
            ["$32 · ride home", "yesterday · 8/10"],
            ["$5 · third coffee", "this morning · −∞"],
          ].map(([t, m]) => (
            <div key={t as string} className="bg-surface rounded-[12px] px-5 py-3">
              <div className="text-[13px] font-medium text-fg">{t}</div>
              <div className="text-[11px] text-fg-dim mt-[2px]">{m}</div>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>see month →</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}
