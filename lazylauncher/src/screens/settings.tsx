import { Phone, StatusBar, HomeIndicator, TopBadge } from "../components/Phone";
import { HollowButton, PrimaryButton } from "../components/primitives";

/* 8.1 — Settings root */
export function SettingsRoot() {
  const groups: { title: string; rows: [string, string][] }[] = [
    { title: "YOU",      rows: [["mantra", "edit · 24h cooldown"], ["why are you here", "captured Feb 3"], ["future-self video", "30s · re-record"], ["VIP contacts", "5 of 10"]] },
    { title: "TRUST",    rows: [["transparency", "12 techniques · all on"], ["passphrase", "set · paper backup"], ["data export", "PDF · CSV · photo book"], ["device admin", "active"]] },
    { title: "PAY",      rows: [["plan", "premium · $6/mo · annual"], ["money stake", "$200 escrow · 22 days left"], ["payments", "Play Billing · GPay"], ["subscription hunter", "3 found this month"]] },
    { title: "HIDDEN",   rows: [["hidden apps", "23 of 47"], ["sad self voice", "blunt"], ["lobby wait time", "form-modulated"], ["feature flags", "8 enabled"]] },
  ];
  return (
    <Phone label="8.1   Settings — Root">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />
      <div className="absolute inset-x-0 top-[88px] px-8">
        <div className="label-caps text-[10px] text-accent">SETTINGS · ADMIN</div>
        <div className="mt-2 text-[30px] font-bold text-fg tracking-heading">Settings</div>
        <div className="mt-1 text-[11.5px] text-fg-dim">accessed via long-press dock + passphrase. cooldown on edits.</div>
      </div>

      <div className="absolute inset-x-0 top-[200px] px-8 space-y-4 overflow-hidden">
        {groups.map((g) => (
          <div key={g.title}>
            <div className="label-caps text-[9.5px] text-accent mb-2">{g.title}</div>
            <div className="bg-surface rounded-[12px] divide-y divide-edge/40">
              {g.rows.map(([k, v]) => (
                <div key={k} className="flex items-baseline justify-between px-4 py-[10px]">
                  <span className="text-[13px] text-fg">{k}</span>
                  <span className="text-[11px] text-fg-dim">{v}</span>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 8.2 — Transparency toggles */
export function SettingsTransparency() {
  const techniques = [
    ["loss aversion (Sad Self)", "ON"],
    ["personification (named voice)", "ON"],
    ["receipts (post-session)", "ON"],
    ["intent declaration (lobby)", "ON"],
    ["progressive dimming", "ON"],
    ["cognitive tax (puzzles)", "ON"],
    ["social shame (group)", "ON"],
    ["public surrender posts", "ON"],
    ["opportunity-cost overlay", "OFF"],
    ["mirror widget (camera)", "OFF"],
    ["phantom-vibration counter", "ON"],
    ["future-self video at relapse", "ON"],
  ];
  return (
    <Phone label="8.2   Settings — Transparency">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8">
        <div className="label-caps text-[10px] text-accent">SETTINGS · ETHICS-001</div>
        <div className="mt-2 text-[26px] font-bold text-fg tracking-heading">Transparency</div>
        <div className="mt-2 text-[12px] text-fg-dim leading-[19px] max-w-[348px]">
          every psychological technique this app uses on you, with a switch.
          turn off anything that doesn’t serve you. the app keeps working.
        </div>
      </div>

      <div className="absolute inset-x-0 top-[256px] px-8 space-y-[6px]">
        {techniques.map(([name, state]) => (
          <div key={name} className="bg-surface rounded-[12px] px-4 py-[10px] flex items-center justify-between">
            <span className="text-[12.5px] text-fg">{name}</span>
            <div className={`w-9 h-5 rounded-full ${state === "ON" ? "bg-accent" : "bg-surface2"} relative`}>
              <div className={`absolute top-[2px] w-4 h-4 bg-white rounded-full transition-all ${state === "ON" ? "left-[19px]" : "left-[2px]"}`} />
            </div>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 bottom-[110px] px-8 text-[10.5px] text-fg-mute leading-[16px]">
        the app does not get worse when you toggle these off. it gets less
        like itself. read the <span className="text-fg-dim underline">why behind each technique</span>.
      </div>
      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>reset to defaults</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 8.3 — Crisis Soften Mode */
export function CrisisSoften() {
  return (
    <Phone label="8.3   Crisis — Soften Mode">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        SOFTEN MODE · SAFETY-001 · QUIETLY ACTIVATED
      </div>
      <div className="absolute inset-x-0 top-[180px] px-8">
        <div className="text-[28px] leading-[36px] font-light text-fg tracking-heading">
          hey.<br />
          <span className="text-fg-dim">we noticed.</span>
        </div>
        <p className="mt-5 text-[13.5px] text-fg leading-[21px] max-w-[348px]">
          you’ve been scrolling at 3am for four nights, you haven’t completed any
          todos this week, your mood pings have been flat, and your phone has barely moved.
        </p>
        <p className="mt-3 text-[13.5px] text-fg-dim leading-[21px] max-w-[348px]">
          we’ve paused Sad Self until you turn it back on. nothing else has changed.
        </p>
      </div>

      <div className="absolute inset-x-0 top-[492px] px-8">
        <div className="bg-surface rounded-[14px] p-5">
          <div className="label-caps text-[9px] text-fg-mute">RIGHT NOW</div>
          <ul className="mt-2 text-[12.5px] text-fg-dim leading-[20px] list-disc pl-5">
            <li><span className="text-fg">text a friend</span> — Anuj or Sara are awake</li>
            <li><span className="text-fg">call iCall</span> — 9152987821 (India)</li>
            <li><span className="text-fg">call 988</span> — Suicide & Crisis Lifeline (US/CA)</li>
            <li><span className="text-fg">call Samaritans</span> — 116 123 (UK)</li>
          </ul>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[722px] px-8 text-[11px] text-fg-mute leading-[16px]">
        no streak break · no notifications to your group · this is private.
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 space-y-2">
        <PrimaryButton>text Anuj · sponsor</PrimaryButton>
        <HollowButton className="!h-11 !text-[12px]">i’m okay · re-enable Sad Self</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 8.4 — App Tombstones */
export function Tombstones() {
  const apps = [
    ["Instagram", "Aug 14, 2025", "287 days quiet"],
    ["TikTok",    "Nov 02, 2025", "192 days quiet"],
    ["Twitter",   "Jan 09, 2026", "—"],
    ["Reddit",    "Feb 24, 2026", "85 days quiet"],
    ["Snapchat",  "Mar 10, 2026", "—"],
  ];
  return (
    <Phone label="8.4   App Tombstones">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        APP TOMBSTONES · PSYCH-014
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[30px] font-bold text-fg tracking-heading">Graveyard</div>
        <div className="mt-1 text-[12.5px] text-fg-dim">apps you killed. don’t bring them back without a reason.</div>
      </div>

      <div className="absolute inset-x-0 top-[208px] px-8 space-y-3">
        {apps.map(([name, date, since]) => (
          <div key={name} className="bg-surface rounded-[14px] p-5">
            <div className="flex items-baseline justify-between">
              <div>
                <div className="text-[16px] font-medium text-fg">{name}</div>
                <div className="text-[10.5px] text-fg-mute mt-[2px]">deleted {date}</div>
              </div>
              <div className="text-right">
                <div className="text-[12px] text-fg-dim">{since}</div>
                <div className="font-mono text-[10px] text-fg-mute mt-[2px]">RIP</div>
              </div>
            </div>
            <div className="mt-3 dashed-row text-[10px] text-fg-mute">- - - - - - - - - - - - - - - - - - - - -</div>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 text-center text-[11px] text-fg-mute">
        bringing one back costs 100 pts + sponsor confirmation.
      </div>
      <HomeIndicator />
    </Phone>
  );
}
