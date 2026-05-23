import { Phone, StatusBar, HomeIndicator, TopBadge } from "../components/Phone";
import { HollowButton, PrimaryButton } from "../components/primitives";

/* 11.1 — Focus Map */
export function FocusMap() {
  const cities: [string, string, number, number, boolean][] = [
    ["Anuj",   "Bangalore",  220, 380, true],
    ["Sara",   "Brooklyn",   100, 220, true],
    ["Bijou",  "Paris",      210, 175, false],
    ["John",   "London",     200, 165, true],
    ["Pawan",  "Kathmandu",  246, 310, true],
    ["Akari",  "Tokyo",      297, 260, true],
    ["Tom",    "Sydney",     312, 470, false],
  ];
  return (
    <Phone label="11.1   Focus Map">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        FOCUS MAP · SOCIAL-027 · LIVE
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[28px] font-bold text-fg tracking-heading">5 in focus</div>
        <div className="mt-1 text-[12px] text-fg-dim">friends in deep work right now · updates every 60s</div>
      </div>

      {/* map area */}
      <div className="absolute left-8 right-8 top-[210px] h-[400px] rounded-[16px] border border-edge bg-[#0d0d0d] overflow-hidden">
        {/* abstract grid for "map" */}
        <div className="absolute inset-0 opacity-[0.15]">
          {Array.from({ length: 8 }).map((_, i) => (
            <div key={`h${i}`} className="absolute left-0 right-0 h-px bg-fg-mute" style={{ top: `${(i + 1) * 12}%` }} />
          ))}
          {Array.from({ length: 8 }).map((_, i) => (
            <div key={`v${i}`} className="absolute top-0 bottom-0 w-px bg-fg-mute" style={{ left: `${(i + 1) * 12}%` }} />
          ))}
        </div>

        {/* dots */}
        {cities.map(([name, city, x, y, focused]) => (
          <div key={name} className="absolute" style={{ left: `${x}px`, top: `${y - 210}px` }}>
            <div className={`w-2.5 h-2.5 rounded-full ${focused ? "bg-accent" : "bg-fg-mute"} ${focused ? "ring-2 ring-accent/30" : ""}`} />
            <div className="text-[9px] text-fg-dim mt-[2px] whitespace-nowrap">{name} · {city}</div>
          </div>
        ))}

        {/* legend */}
        <div className="absolute bottom-3 left-3 right-3 flex items-center gap-3 text-[9px] text-fg-mute">
          <span className="flex items-center gap-1"><span className="w-2 h-2 rounded-full bg-accent" /> focused</span>
          <span className="flex items-center gap-1"><span className="w-2 h-2 rounded-full bg-fg-mute" /> idle</span>
          <span className="ml-auto label-caps">SHARING LOCATION CITY-LEVEL · OPT-IN</span>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[638px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">FOCUSED RIGHT NOW</div>
        <div className="space-y-1">
          {[
            ["Anuj",  "Bangalore",  "Q2 design draft", "24m elapsed"],
            ["Sara",  "Brooklyn",   "essay · 1500w",   "1h 12m elapsed"],
            ["Akari", "Tokyo",      "lift block",      "8m elapsed"],
          ].map(([n, c, task, el]) => (
            <div key={n} className="flex items-baseline gap-3 text-[11.5px] py-[3px]">
              <span className="w-3 h-3 rounded-full bg-accent" />
              <span className="text-fg font-medium w-[60px]">{n}</span>
              <span className="text-fg-dim w-[80px]">{c}</span>
              <span className="text-fg flex-1 truncate">{task}</span>
              <span className="text-fg-mute text-[10px]">{el}</span>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>join Anuj · focus block (24m left)</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 11.2 — Live Focus Rooms */
export function LiveFocusRooms() {
  const rooms: [string, string, string, number, string][] = [
    ["Indie Builders · 6am",  "deep work · silent",   "🔥 18 day room",  4, "live"],
    ["The 90-min Block",       "ambient music",       "⏱ ends 11:00",   7, "live"],
    ["Late Night Crew",        "writing only",        "🌙 PST/IST mix", 12, "live"],
    ["Pottery Studio",         "hobby block",         "🪴 weekends",     3, "scheduled · 2h"],
  ];
  return (
    <Phone label="11.2   Live Focus Rooms">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        LIVE ROOMS · SOCIAL-028
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[28px] font-bold text-fg tracking-heading">4 rooms open</div>
        <div className="mt-1 text-[12px] text-fg-dim">audio-only · mic muted by default · focus state verified</div>
      </div>

      <div className="absolute inset-x-0 top-[228px] px-8 space-y-3">
        {rooms.map(([title, sub, meta, count, status]) => (
          <div key={title} className="bg-surface rounded-[14px] p-4">
            <div className="flex items-baseline justify-between">
              <div className="text-[14.5px] font-medium text-fg">{title}</div>
              <div className={`text-[10px] label-caps ${status === "live" ? "text-success" : "text-accent"}`}>{status}</div>
            </div>
            <div className="text-[11px] text-fg-dim mt-[2px]">{sub}  ·  {meta}</div>
            <div className="mt-3 flex items-center justify-between">
              <div className="flex -space-x-1">
                {Array.from({ length: Math.min(count, 5) }).map((_, i) => (
                  <div key={i} className="w-7 h-7 rounded-full bg-ink border border-edge flex items-center justify-center text-[9px] text-fg-dim">{["AC","SD","BJ","PW","JD"][i]}</div>
                ))}
                {count > 5 && <div className="w-7 h-7 rounded-full bg-ink border border-edge flex items-center justify-center text-[9px] text-fg-dim">+{count - 5}</div>}
              </div>
              <div className="text-[11px] text-fg-mute font-mono">{count} inside</div>
            </div>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 top-[636px] px-8">
        <div className="bg-ink border border-edge rounded-[14px] p-4">
          <div className="label-caps text-[9px] text-fg-mute">JOIN RULE</div>
          <div className="mt-1 text-[12px] text-fg-dim leading-[18px]">
            you can only join if you commit a task. mic stays muted unless you're celebrating
            a completion. leaving early costs your group's streak.
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 space-y-2">
        <PrimaryButton>join "Indie Builders · 6am"</PrimaryButton>
        <HollowButton className="!h-11 !text-[12px]">create a room</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 11.3 — Group Sprint */
export function GroupSprint() {
  const players: [string, string, number][] = [
    ["Anuj",  "wrap Stage-1 spec",      72],
    ["Sara",  "draft 1500 of essay",    44],
    ["Bijou", "60-min lift + protein",  100],
    ["John",  "Q2 board deck",          18],
    ["Pawan", "ship icon redesign",     86],
  ];
  return (
    <Phone label="11.3   Group Sprint">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        SPRINT · SOCIAL-007 · 2h LIVE
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[14px] text-fg-dim">Tuesday sprint · 9:00 — 11:00</div>
        <div className="mt-1 text-[64px] leading-none font-light text-fg tracking-display tabular-nums">1:24</div>
        <div className="mt-2 text-[12px] text-fg-dim">remaining · room locked · cannot leave with phone</div>
      </div>

      <div className="absolute inset-x-0 top-[330px] px-8 space-y-3">
        {players.map(([n, task, pct]) => (
          <div key={n} className="bg-surface rounded-[12px] p-3">
            <div className="flex items-baseline justify-between">
              <span className="text-[13px] font-medium text-fg">{n} {n === "Bijou" && <span className="text-success text-[10px] ml-1">DONE</span>}</span>
              <span className="text-[10.5px] text-fg-mute tabular-nums">{pct}%</span>
            </div>
            <div className="text-[11px] text-fg-dim mt-[2px]">{task}</div>
            <div className="mt-2 h-[3px] bg-surface2 rounded-full overflow-hidden">
              <div className={`h-full ${pct === 100 ? "bg-success" : "bg-accent"} rounded-full`} style={{ width: `${pct}%` }} />
            </div>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 bottom-[110px] px-8">
        <div className="bg-ink border border-edge rounded-[14px] p-4">
          <div className="label-caps text-[9px] text-fg-mute">SHARED REWARD</div>
          <div className="mt-1 text-[11.5px] text-fg-dim leading-[17px]">
            all five finish on time → +25 pts each + dual-streak bonus.
            any one quits → −10 pts to all five.
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>mark my task done</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 11.4 — Focus Duel */
export function FocusDuel() {
  return (
    <Phone label="11.4   Focus Duel">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        DUEL · SOCIAL-034 · 2h BLOCK
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8 text-center">
        <div className="text-[10px] text-fg-mute label-caps">REMAINING</div>
        <div className="mt-1 text-[80px] leading-none font-light text-fg tracking-display tabular-nums">1:07</div>
      </div>

      <div className="absolute inset-x-0 top-[300px] px-8">
        <div className="grid grid-cols-2 gap-3">
          {/* you */}
          <div className="bg-surface rounded-[14px] p-4">
            <div className="text-[10px] text-fg-mute label-caps">YOU</div>
            <div className="mt-1 text-[16px] font-semibold text-fg">erluxman</div>
            <div className="mt-3 text-[40px] font-light text-accent leading-none tabular-nums">62m</div>
            <div className="text-[10.5px] text-fg-mute mt-1">focused · 0 unlocks</div>
            <div className="mt-3 h-[2px] bg-surface2 rounded-full overflow-hidden">
              <div className="h-full w-[51%] bg-accent" />
            </div>
          </div>
          {/* opponent */}
          <div className="bg-ink border border-edge rounded-[14px] p-4">
            <div className="text-[10px] text-fg-mute label-caps">VS</div>
            <div className="mt-1 text-[16px] font-semibold text-fg">@anujch</div>
            <div className="mt-3 text-[40px] font-light text-fg-dim leading-none tabular-nums">54m</div>
            <div className="text-[10.5px] text-fg-mute mt-1">focused · 2 unlocks</div>
            <div className="mt-3 h-[2px] bg-surface2 rounded-full overflow-hidden">
              <div className="h-full w-[45%] bg-fg-dim" />
            </div>
          </div>
        </div>

        <div className="mt-4 bg-surface rounded-[12px] p-3 text-center">
          <div className="text-[10px] text-fg-mute label-caps">SCORE · YOU LEAD BY 8 MIN</div>
          <div className="mt-1 text-[13px] text-fg-dim">winner takes the loser's 20 pts. live for the next 67 min.</div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[612px] px-8">
        <div className="label-caps text-[9.5px] text-fg-mute">EVENTS</div>
        <div className="mt-2 space-y-1 text-[11px]">
          <div className="flex items-baseline gap-3"><span className="text-fg-mute font-mono tabular-nums">9:42</span><span className="text-fg-dim">Anuj unlocked twitter — declined</span></div>
          <div className="flex items-baseline gap-3"><span className="text-fg-mute font-mono tabular-nums">9:58</span><span className="text-fg-dim">you completed "Q2 review"</span></div>
          <div className="flex items-baseline gap-3"><span className="text-fg-mute font-mono tabular-nums">10:11</span><span className="text-fg-dim">Anuj completed "1500w draft"</span></div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>watch live · don't break focus</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}
