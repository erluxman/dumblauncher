import { Phone, StatusBar, HomeIndicator, TopBadge } from "../components/Phone";
import { HollowButton } from "../components/primitives";

/* 14.1 — Sleep dashboard */
export function SleepDash() {
  const hours = [6.2, 7.5, 7.1, 5.9, 8.0, 7.4, 7.8, 7.3, 6.5, 7.2, 7.7, 8.1, 7.0, 7.4];
  const target = 7.5;
  return (
    <Phone label="14.1   Sleep">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        REVIEW · SLEEP · SLEEP-001/002</div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[40px] font-light text-fg tracking-display tabular-nums">7h 12m</div>
        <div className="mt-1 text-[12px] text-fg-dim">avg this week · debt 1.4h · consistency 84%</div>
      </div>

      <div className="absolute inset-x-0 top-[240px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-3">LAST 14 NIGHTS</div>
        <div className="flex items-end gap-1 h-[88px]">
          {hours.map((h, i) => {
            const pct = Math.min(100, (h / 9) * 100);
            const okay = h >= target - 0.5;
            return (
              <div key={i} className="flex-1 flex flex-col items-center">
                <div className={`w-full rounded-t-sm ${okay ? "bg-accent" : "bg-danger"}`} style={{ height: `${pct}%` }} />
              </div>
            );
          })}
        </div>
        <div className="mt-1 flex justify-between text-[9px] text-fg-mute font-mono tabular-nums">
          <span>2w ago</span><span>last week</span><span>this week</span>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[420px] px-8 space-y-[10px]">
        {[
          ["sleep ↔ scroll",   "−47m sleep per 1h scroll after 9pm",  "danger"],
          ["sleep ↔ workout",  "+22m sleep on workout days",            "ok"],
          ["sleep ↔ caffeine", "−39m on days with coffee after 2pm",   "danger"],
          ["sleep ↔ mood",     "+18% mood next day if sleep > 7h",      "ok"],
        ].map(([k, v, tone]) => (
          <div key={k} className="bg-surface rounded-[12px] p-3">
            <div className="flex items-baseline justify-between">
              <span className="text-[11.5px] text-fg-dim label-caps">{k}</span>
              <span className={`text-[10px] label-caps ${tone === "danger" ? "text-danger" : "text-success"}`}>{tone === "danger" ? "FIXING" : "HELPING"}</span>
            </div>
            <div className="mt-1 text-[12.5px] text-fg">{v}</div>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>set sleep window · 10:45 to 6:30</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 14.2 — Money dashboard */
export function MoneyDash() {
  const months = [3200, 2980, 3450, 2870, 3120, 2890];
  return (
    <Phone label="14.2   Money">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        REVIEW · MONEY · FIN-001..006</div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="flex items-end gap-4">
          <div>
            <div className="text-[40px] font-light text-fg tracking-display tabular-nums">$2,890</div>
            <div className="text-[11px] text-fg-mute">spend this month · ↓ $230 vs avg</div>
          </div>
          <div className="pb-1">
            <div className="text-[14px] font-semibold text-success tabular-nums">38%</div>
            <div className="text-[10px] text-fg-mute label-caps">SAVINGS</div>
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[230px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">LAST 6 MONTHS</div>
        <div className="flex items-end gap-2 h-[80px]">
          {months.map((m, i) => (
            <div key={i} className="flex-1 bg-accent rounded-t-sm" style={{ height: `${(m / 3500) * 100}%`, opacity: i === 5 ? 1 : 0.5 }} />
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[376px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">BY CATEGORY · THIS MONTH</div>
        <div className="space-y-[6px]">
          {[
            ["food",     "$1,142",  "39%", 39],
            ["transit",  "$432",    "15%", 15],
            ["coffee",   "$214",    "7%",  7],
            ["software", "$236",    "8%",  8],
            ["misc",     "$866",    "30%", 30],
          ].map(([cat, val, pct, num]) => (
            <div key={cat}>
              <div className="flex items-baseline justify-between text-[12px]">
                <span className="text-fg">{cat}</span>
                <span className="text-fg-dim font-mono tabular-nums">{val} · {pct}</span>
              </div>
              <div className="mt-[2px] h-[2px] bg-surface2 rounded-full">
                <div className="h-full bg-accent rounded-full" style={{ width: `${num}%` }} />
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[612px] px-8">
        <div className="bg-surface rounded-[14px] p-3">
          <div className="label-caps text-[9px] text-accent">SUBSCRIPTION HUNTER · FIN-003</div>
          <div className="mt-1 text-[12px] text-fg-dim">
            found 3 unused this month: <span className="text-fg">Notion AI ($10)</span> · <span className="text-fg">Headspace ($13)</span> · <span className="text-fg">Disney+ ($11)</span>. cancel = +$408/yr.
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>see future-self projection · age 65</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 14.3 — Workout dashboard */
export function WorkoutDash() {
  return (
    <Phone label="14.3   Workout">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        REVIEW · WORKOUTS · FIT-002/003/004/005</div>
      <div className="absolute inset-x-0 top-[124px] px-8 grid grid-cols-3 gap-2">
        {[["41","SESSIONS · Y1"], ["73%","FORM"], ["47ms","HRV avg"]].map(([v, l]) => (
          <div key={l} className="bg-surface rounded-[12px] p-3">
            <div className="text-[22px] font-light text-fg tracking-display tabular-nums">{v}</div>
            <div className="text-[9px] text-fg-mute label-caps mt-1">{l}</div>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 top-[252px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">PR WALL · FIT-003</div>
        <div className="space-y-2">
          {[
            ["squat 100kg × 5", "Apr 22 · NEW"],
            ["bench 75kg × 5",  "Mar 18"],
            ["5k 22:14",        "May 14 · NEW"],
            ["10 unbroken pull-ups", "Feb 09"],
          ].map(([pr, when]) => (
            <div key={pr} className="bg-surface rounded-[12px] px-4 py-2 flex items-baseline justify-between">
              <span className="text-[13px] text-fg font-medium">{pr}</span>
              <span className={`text-[10.5px] ${when.includes("NEW") ? "text-success" : "text-fg-mute"}`}>{when}</span>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[504px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">SPORT CLOCKS</div>
        <div className="bg-surface rounded-[14px] divide-y divide-edge/40">
          {[
            ["last run",           "2 days ago",   "ok"],
            ["last lift",          "1 day ago",    "ok"],
            ["days since rest",    "6",            "warn"],
            ["overtraining risk",  "moderate",     "warn"],
          ].map(([k, v, tone]) => (
            <div key={k} className="flex items-baseline justify-between px-4 py-[10px]">
              <span className="text-[12.5px] text-fg-dim">{k}</span>
              <span className={`text-[12px] ${tone === "warn" ? "text-accent" : "text-success"} font-medium`}>{v}</span>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[710px] px-8">
        <div className="bg-ink border border-accent/30 rounded-[12px] p-3">
          <div className="label-caps text-[9px] text-accent">RECOVERY SCORE · FIT-005</div>
          <div className="mt-1 text-[12px] text-fg">68 / 100 · take a rest day → +12 by tomorrow</div>
        </div>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 14.4 — Reading dashboard */
export function ReadingDash() {
  return (
    <Phone label="14.4   Reading">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        REVIEW · READING · READ-001/002</div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[40px] font-light text-fg tracking-display tabular-nums">23 books</div>
        <div className="mt-1 text-[12px] text-fg-dim">this year · 312 highlights · 2h 14m / week</div>
      </div>

      <div className="absolute inset-x-0 top-[228px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">CURRENT</div>
        <div className="bg-surface rounded-[14px] p-4">
          <div className="flex items-baseline justify-between">
            <span className="text-[14px] font-medium text-fg">Atomic Habits</span>
            <span className="text-[10px] text-fg-mute">James Clear</span>
          </div>
          <div className="mt-2 h-[2px] bg-surface2 rounded-full overflow-hidden">
            <div className="h-full w-[68%] bg-accent" />
          </div>
          <div className="mt-2 text-[10.5px] text-fg-dim">page 187 of 271 · 7 days estimated</div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[396px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">TODAY'S HIGHLIGHT · READ-002</div>
        <div className="bg-ink border border-accent/30 rounded-[14px] p-4">
          <div className="text-[12.5px] text-fg leading-[18px] italic">
            "You do not rise to the level of your goals. You fall to the level of your systems."
          </div>
          <div className="mt-2 text-[10px] text-fg-mute">— James Clear · resurfaced from 2 months ago</div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[594px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">FINISHED RECENTLY · 4 OF 23</div>
        <div className="space-y-1">
          {[
            ["The Almanack of Naval", "Eric Jorgenson",   "May 11"],
            ["Four Thousand Weeks",   "Oliver Burkeman",  "Apr 30"],
            ["Tao of Pooh",           "Benjamin Hoff",    "Apr 14"],
            ["Deep Work",             "Cal Newport",      "Mar 28"],
          ].map(([t, a, d]) => (
            <div key={t} className="flex items-baseline gap-3 py-[2px] text-[12px]">
              <span className="text-fg font-medium flex-1 min-w-0 truncate">{t}</span>
              <span className="text-fg-dim text-[10.5px]">{a}</span>
              <span className="text-fg-mute text-[10px] font-mono tabular-nums">{d}</span>
            </div>
          ))}
        </div>
      </div>

      <HomeIndicator />
    </Phone>
  );
}
