import { Phone, StatusBar, HomeIndicator, TopBadge } from "../components/Phone";
import { HollowButton, PrimaryButton } from "../components/primitives";

/* 12.1 — Couples Pair Dashboard */
export function CouplesPair() {
  return (
    <Phone label="12.1   Couples Pair">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        COUPLES · COUPLES-001 · paired Mar 02</div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[28px] font-bold text-fg tracking-heading leading-tight">
          you + Asha
        </div>
        <div className="mt-1 text-[12px] text-fg-dim">we, not me · shared streak · proud pings</div>
      </div>

      <div className="absolute inset-x-0 top-[214px] px-8 grid grid-cols-3 gap-2">
        {[
          ["83",     "DAYS PAIRED",         "longest"],
          ["41h",    "SCREEN TIME SAVED",   "together"],
          ["12",     "PHONE SABBATHS",      "this year"],
        ].map(([v, l, s]) => (
          <div key={l as string} className="bg-surface rounded-[12px] p-3">
            <div className="text-[20px] font-light text-fg tracking-display tabular-nums">{v}</div>
            <div className="text-[9px] text-fg-mute label-caps mt-1">{l}</div>
            <div className="text-[9.5px] text-fg-dim mt-[2px]">{s}</div>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 top-[336px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">SHARED HABITS</div>
        <div className="bg-surface rounded-[14px] divide-y divide-edge/40">
          {[
            ["phone-down at dinner",  "7 / 7 days",     "🔥 41"],
            ["walk after lunch",      "5 / 7 days",     "🔥 13"],
            ["no phone in bed",       "6 / 7 days",     "🔥 28"],
          ].map(([h, w, s]) => (
            <div key={h} className="flex items-baseline justify-between px-4 py-[10px]">
              <div>
                <div className="text-[12.5px] text-fg">{h}</div>
                <div className="text-[10.5px] text-fg-mute mt-[2px]">{w}</div>
              </div>
              <div className="text-[12px] text-accent font-medium tabular-nums">{s}</div>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[560px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">ASHA SAID · 2h ago</div>
        <div className="bg-ink border border-accent/30 rounded-[14px] p-4">
          <div className="text-[13px] text-fg leading-[19px] italic">
            "good walk today. felt like college again."
          </div>
        </div>
        <div className="mt-2 text-[10.5px] text-fg-mute">she completed "walk after lunch" at 1:42pm. proud ping was auto-sent.</div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 space-y-2">
        <PrimaryButton>schedule a phone sabbath · this Sunday</PrimaryButton>
        <HollowButton className="!h-11 !text-[12px]">we-ledger · finances (couples mode)</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 12.2 — Family Parent Dashboard */
export function FamilyParent() {
  return (
    <Phone label="12.2   Family Parent">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        FAMILY · FAMILY-001 · parent dashboard
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[24px] font-bold text-fg tracking-heading leading-tight">Aaryan, 14</div>
        <div className="mt-1 text-[12px] text-fg-dim">school week · L2 · form 64% · 412 pts</div>
      </div>

      <div className="absolute inset-x-0 top-[208px] px-8">
        <div className="bg-surface rounded-[14px] p-4">
          <div className="label-caps text-[9px] text-fg-mute">TODAY</div>
          <div className="mt-2 grid grid-cols-3 gap-3">
            <div>
              <div className="text-[18px] font-light text-fg tabular-nums">3h 12m</div>
              <div className="text-[9px] text-fg-mute label-caps">screen</div>
            </div>
            <div>
              <div className="text-[18px] font-light text-fg tabular-nums">2 / 3</div>
              <div className="text-[9px] text-fg-mute label-caps">todos done</div>
            </div>
            <div>
              <div className="text-[18px] font-light text-success tabular-nums">9:48pm</div>
              <div className="text-[9px] text-fg-mute label-caps">phone down</div>
            </div>
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[346px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">PENDING REQUESTS · 2</div>
        <div className="space-y-2">
          {[
            ["YouTube · 30 min",  "for project research", "school report due fri"],
            ["Snapchat · 20 min", "weekend",              "wants to ask out classmate"],
          ].map(([app, reason, ctx]) => (
            <div key={app} className="bg-surface rounded-[12px] p-3">
              <div className="flex items-baseline justify-between">
                <div className="text-[13px] font-medium text-fg">{app}</div>
                <div className="text-[10px] text-fg-mute label-caps">12m ago</div>
              </div>
              <div className="text-[10.5px] text-fg-dim mt-[2px]">{reason}</div>
              <div className="text-[10px] text-fg-mute mt-[2px] italic">"{ctx}"</div>
              <div className="mt-3 flex gap-2">
                <div className="flex-1 h-8 rounded-full bg-success/20 border border-success text-success text-[11px] font-semibold flex items-center justify-center">approve</div>
                <div className="flex-1 h-8 rounded-full border border-edge text-fg-dim text-[11px] flex items-center justify-center">deny</div>
                <div className="flex-1 h-8 rounded-full border border-edge text-fg-dim text-[11px] flex items-center justify-center">talk first</div>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[638px] px-8">
        <div className="bg-ink border border-edge rounded-[14px] p-4">
          <div className="label-caps text-[9px] text-fg-mute">WEEKLY DIGEST · SUNDAYS 9PM</div>
          <div className="mt-1 text-[12px] text-fg-dim leading-[18px]">
            you and Aaryan get the same summary. ground rules editable together — not by you alone.
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>see Aaryan's view · what they see</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 12.3 — NFC Setup */
export function NFCSetup() {
  return (
    <Phone label="12.3   NFC Setup">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        NFC KEY · HARDWARE-001
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[26px] font-bold text-fg tracking-heading leading-tight">
          tap your phone to<br />the desk tag.
        </div>
        <div className="mt-2 text-[12.5px] text-fg-dim leading-[19px]">
          this NFC tag becomes the only place distraction apps will unlock. leave the desk → apps lock.
        </div>
      </div>

      <div className="absolute inset-x-0 top-[290px] px-8">
        <div className="relative w-full h-[240px] rounded-[18px] border border-edge bg-ink overflow-hidden flex items-center justify-center">
          <div className="absolute inset-0 flex items-center justify-center">
            <div className="w-32 h-32 rounded-full border-2 border-accent/40 animate-pulse" />
          </div>
          <div className="absolute inset-0 flex items-center justify-center">
            <div className="w-20 h-20 rounded-full border-2 border-accent flex items-center justify-center text-accent text-[22px] font-light">
              ⌒
            </div>
          </div>
          <div className="absolute bottom-3 inset-x-3 text-center text-[10px] text-fg-mute label-caps">
            HOLD WITHIN 4 CM · WAITING…
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[572px] px-8">
        <div className="label-caps text-[9px] text-fg-mute mb-2">APPS THIS TAG WILL UNLOCK</div>
        <div className="space-y-1">
          {[
            ["Twitter",   "15 min budget"],
            ["YouTube",   "30 min budget"],
            ["Slack DMs", "no limit"],
          ].map(([a, b]) => (
            <div key={a} className="bg-surface rounded-[10px] px-4 py-2 flex items-baseline justify-between">
              <span className="text-[12.5px] text-fg">{a}</span>
              <span className="text-[10.5px] text-fg-mute">{b}</span>
            </div>
          ))}
          <div className="text-center text-[10.5px] text-fg-mute pt-1">+ pick more apps</div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>order more tags · $9 for 3</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 12.4 — Mantra Gate Listening */
export function MantraListening() {
  return (
    <Phone label="12.4   Mantra Gate — Listening">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        MANTRA · MANTRA-001 · LISTENING
      </div>

      <div className="absolute inset-x-0 top-[200px] px-8">
        <div className="text-[26px] leading-[34px] font-light text-fg tracking-heading">
          say it,<br />three times.
        </div>
      </div>

      <div className="absolute inset-x-0 top-[358px] px-8 text-center">
        <div className="text-[22px] font-light text-fg leading-[32px]">
          "i build things.<br />i don’t scroll them."
        </div>
      </div>

      {/* audio waveform */}
      <div className="absolute inset-x-0 top-[500px] px-8">
        <div className="flex items-center justify-center gap-[3px] h-[60px]">
          {Array.from({ length: 40 }).map((_, i) => {
            const heights = [8,14,22,30,18,40,32,12,26,42,38,20,16,30,46,36,22,14,34,40,30,18,42,28,12,22,36,40,30,16,24,38,20,14,30,42,18,28,12,8];
            return <div key={i} className="w-1 rounded-full bg-accent" style={{ height: `${heights[i]}px` }} />;
          })}
        </div>
        <div className="mt-3 text-center text-[11px] text-fg-mute label-caps">PICKED UP · CLEAR</div>
      </div>

      <div className="absolute inset-x-0 top-[670px] px-8">
        <div className="flex justify-center gap-4">
          {[true, false, false].map((done, i) => (
            <div key={i} className={`w-14 h-14 rounded-full border-2 ${done ? "border-accent text-accent" : "border-fg-mute text-fg-mute"} flex items-center justify-center text-[16px]`}>
              {done ? "✓" : i + 1}
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 text-center text-[11px] text-fg-mute">
        in public? <span className="text-fg-dim underline">whisper mode</span> · <span className="text-fg-dim underline">type fallback</span>
      </div>
      <HomeIndicator />
    </Phone>
  );
}
