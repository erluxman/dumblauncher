import { Phone, StatusBar, HomeIndicator, TopBadge } from "../components/Phone";
import { HollowButton, PrimaryButton } from "../components/primitives";

/* 10.1 — Money Stake setup */
export function MoneyStake() {
  return (
    <Phone label="10.1   Money Stake">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        MONEY STAKE · FINANCE-001 · ESCROW
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[28px] font-bold text-fg tracking-heading leading-[34px]">
          put your money<br /><span className="text-fg-dim">where your habits are.</span>
        </div>
        <p className="mt-3 text-[12.5px] text-fg-dim leading-[19px]">
          you deposit. you set a goal. you complete = refund + bonus.
          you fail = goes to a cause you hate.
        </p>
      </div>

      <div className="absolute inset-x-0 top-[280px] px-8">
        <div className="bg-surface rounded-[14px] p-5">
          <div className="label-caps text-[9px] text-fg-mute">DEPOSIT</div>
          <div className="mt-2 text-[44px] font-light text-fg tracking-display tabular-nums">$200</div>
          <div className="mt-3 grid grid-cols-5 gap-2">
            {["$50","$100","$200","$300","$500"].map((v, i) => (
              <div key={v} className={`h-9 rounded-[10px] flex items-center justify-center text-[12px] ${i === 2 ? "bg-accent text-ink font-semibold" : "bg-ink border border-edge text-fg-dim"}`}>{v}</div>
            ))}
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[460px] px-8">
        <div className="label-caps text-[9px] text-fg-mute mb-2">GOAL · 30 DAYS</div>
        <div className="bg-surface rounded-[14px] p-4">
          <div className="text-[14px] text-fg leading-[20px]">
            ship 1 todo per day · less than 30 min Twitter daily · 0 surrenders
          </div>
          <div className="mt-2 text-[10.5px] text-fg-mute">change goal → custom</div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[598px] px-8">
        <div className="label-caps text-[9px] text-fg-mute mb-2">FAIL DESTINATION</div>
        <div className="bg-surface rounded-[14px] p-4 flex items-baseline justify-between">
          <span className="text-[13px] text-fg">Anti-Vegan Society</span>
          <span className="text-[10px] text-fg-mute">pick another</span>
        </div>
        <div className="mt-2 text-[10.5px] text-fg-mute">pick something so painful you’ll do anything to not let it happen.</div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 space-y-2">
        <PrimaryButton>commit $200 to escrow</PrimaryButton>
        <HollowButton className="!h-11 !text-[12px]">read the terms · refund + 10% bonus</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 10.2 — Dual Streak detail */
export function DualStreak() {
  const days = [1,1,1,1,1,1,1,1,1,0,1,1,1,1];
  const mine = [1,1,1,1,1,1,1,1,1,1,1,1,1,1];
  const them = [1,1,1,1,1,1,1,1,1,0,1,1,1,1];
  return (
    <Phone label="10.2   Dual Streak — Detail">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 flex items-baseline justify-between">
        <div className="label-caps text-[10px] text-accent">DUAL STREAK · SOCIAL-025</div>
        <div className="text-[11px] text-fg-mute">since Mar 11</div>
      </div>

      <div className="absolute inset-x-0 top-[140px] px-8 text-center">
        <div className="text-[14px] text-fg-mute label-caps">YOU + ANUJ</div>
        <div className="mt-3 text-[96px] leading-none font-light text-accent tracking-display tabular-nums">73 🔥</div>
        <div className="mt-2 text-[12px] text-fg-dim">day streak · paused once · ends if either misses 2 days</div>
      </div>

      <div className="absolute inset-x-0 top-[420px] px-8 space-y-4">
        <div>
          <div className="flex items-baseline justify-between mb-2">
            <span className="text-[12px] text-fg-dim">you</span>
            <span className="text-[10px] text-fg-mute font-mono tabular-nums">14 / 14 days</span>
          </div>
          <div className="flex gap-1">
            {mine.map((d, i) => (
              <div key={i} className={`flex-1 h-6 rounded-[3px] ${d ? "bg-accent" : "bg-surface2"}`} />
            ))}
          </div>
        </div>
        <div>
          <div className="flex items-baseline justify-between mb-2">
            <span className="text-[12px] text-fg-dim">Anuj</span>
            <span className="text-[10px] text-fg-mute font-mono tabular-nums">13 / 14 days · forgiven</span>
          </div>
          <div className="flex gap-1">
            {them.map((d, i) => (
              <div key={i} className={`flex-1 h-6 rounded-[3px] ${d ? "bg-accent" : "bg-danger"}`} />
            ))}
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[632px] px-8">
        <div className="bg-surface rounded-[14px] p-4">
          <div className="label-caps text-[9px] text-fg-mute">SHARED HABIT</div>
          <div className="mt-1 text-[14px] text-fg font-medium">read 30 minutes · before 10 PM</div>
          <div className="mt-1 text-[10.5px] text-fg-mute">streak charm: you’ve read 36h together this year.</div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 space-y-2">
        <PrimaryButton>nudge Anuj · "i did mine"</PrimaryButton>
        <HollowButton className="!h-11 !text-[12px]">change shared habit · 24h cooldown</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 10.3 — Sponsor setup */
export function SponsorSetup() {
  const triggers = [
    ["i try to uninstall",         "on"],
    ["my form drops below 40%",     "on"],
    ["i surrender > 2 in a week",   "on"],
    ["i open a blocked app at 2am", "on"],
    ["i miss my one thing 3 days",  "off"],
  ];
  return (
    <Phone label="10.3   Sponsor — Setup">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        SPONSOR · SOCIAL-008
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[28px] font-bold text-fg tracking-heading">Anuj is your sponsor.</div>
        <div className="mt-1 text-[12.5px] text-fg-dim">they get one push per trigger. they can call. they cannot scold.</div>
      </div>

      <div className="absolute inset-x-0 top-[220px] px-8">
        <div className="bg-surface rounded-[14px] p-5 flex items-center gap-3">
          <div className="w-12 h-12 rounded-full border border-edge bg-ink flex items-center justify-center text-[16px] text-fg font-bold">A</div>
          <div className="flex-1">
            <div className="text-[15px] text-fg font-medium">Anuj Choudhary</div>
            <div className="text-[10.5px] text-fg-dim">verified · sponsor of 2 · in your group</div>
          </div>
          <div className="text-[11px] text-accent label-caps">ACTIVE</div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[340px] px-8">
        <div className="label-caps text-[9px] text-fg-mute mb-2">TRIGGERS · ANUJ IS NOTIFIED WHEN</div>
        <div className="bg-surface rounded-[12px] divide-y divide-edge/40">
          {triggers.map(([name, state]) => (
            <div key={name} className="flex items-center justify-between px-4 py-[10px]">
              <span className="text-[12.5px] text-fg">{name}</span>
              <div className={`w-9 h-5 rounded-full ${state === "on" ? "bg-accent" : "bg-surface2"} relative`}>
                <div className={`absolute top-[2px] w-4 h-4 bg-white rounded-full ${state === "on" ? "left-[19px]" : "left-[2px]"}`} />
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[640px] px-8 text-[11px] text-fg-mute leading-[17px]">
        you can change sponsor with a 14-day cooldown.<br />
        Anuj can resign at any time.<br />
        the relationship is logged in your journal.
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>change sponsor · 14d cooldown</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 10.4 — Group Creation */
export function GroupCreate() {
  return (
    <Phone label="10.4   Group — Create">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        GROUPS · SOCIAL-001
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[28px] font-bold text-fg tracking-heading leading-[34px]">
          start a group.<br /><span className="text-fg-dim">max 5 people.</span>
        </div>
        <div className="mt-2 text-[12.5px] text-fg-dim leading-[19px]">
          shared feed · shared dual streaks · uninstall requires majority vote.
        </div>
      </div>

      <div className="absolute inset-x-0 top-[252px] px-8">
        <div className="label-caps text-[9px] text-fg-mute">GROUP NAME</div>
        <div className="mt-2 h-px bg-fg-mute" />
        <div className="mt-3 text-[18px] text-fg leading-tight">
          The Tuesday Builders<span className="inline-block w-[2px] h-[18px] bg-accent align-middle ml-[1px] animate-pulse" />
        </div>
      </div>

      <div className="absolute inset-x-0 top-[348px] px-8">
        <div className="label-caps text-[9px] text-fg-mute mb-2">INVITE · 4 OF 4</div>
        <div className="space-y-[6px]">
          {[
            ["Anuj Choudhary", "+91 ••• ••5577", "added"],
            ["Sara Davis", "sara@built.app", "added"],
            ["Bijou Lin", "+1 ••• ••0481", "pending"],
            ["John Doe", "@johndoe", "pending"],
          ].map(([n, c, s]) => (
            <div key={n} className="bg-surface rounded-[10px] px-4 py-2 flex items-center justify-between">
              <div>
                <div className="text-[12.5px] text-fg">{n}</div>
                <div className="text-[10px] text-fg-mute">{c}</div>
              </div>
              <span className={`text-[10px] label-caps ${s === "added" ? "text-success" : "text-fg-mute"}`}>{s}</span>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[614px] px-8">
        <div className="label-caps text-[9px] text-fg-mute mb-2">RULES OF THIS GROUP</div>
        <div className="bg-surface rounded-[12px] divide-y divide-edge/40">
          {[
            ["shared dual streak", "yes"],
            ["uninstall vote (3 of 5)", "yes"],
            ["see each others' surrenders", "yes"],
            ["weekly group review", "Sundays 10pm"],
          ].map(([k, v]) => (
            <div key={k} className="flex items-baseline justify-between px-4 py-[8px]">
              <span className="text-[12px] text-fg-dim">{k}</span>
              <span className="text-[11px] text-fg font-medium">{v}</span>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <PrimaryButton>create group · send invites</PrimaryButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}
