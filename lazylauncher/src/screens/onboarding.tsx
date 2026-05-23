import { Phone, StatusBar, HomeIndicator } from "../components/Phone";
import { GhostButton, PrimaryButton, HollowButton } from "../components/primitives";

/* 6.1 — Welcome / Why Are You Here */
export function OnboardWelcome() {
  return (
    <Phone label="6.1   Onboarding — Why">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        STEP 1 of 5  ·  ONBOARD-001 / 002
      </div>
      <div className="absolute inset-x-0 top-[160px] px-8">
        <div className="text-[32px] leading-[40px] font-light text-fg tracking-heading">
          before you start,<br />tell me why you’re here.
        </div>
        <p className="mt-6 text-[13px] text-fg-dim leading-[20px] max-w-[348px]">
          one paragraph. plain language. raw. this will resurface every time
          you try to leave the app, and every time you slip.
        </p>
      </div>
      <div className="absolute inset-x-0 top-[420px] px-8">
        <div className="bg-surface rounded-[14px] p-5 min-h-[180px]">
          <div className="label-caps text-[9px] text-fg-mute">YOUR REASON</div>
          <div className="mt-3 text-[15px] text-fg-dim leading-[22px]">
            my daughter said “put the phone down” twice this week.
            i don’t want to be the dad who watches reels at dinner.
            i also haven’t finished the side project in 8 months.
            i want my hands back.<span className="inline-block w-[2px] h-[18px] bg-accent align-middle ml-[1px] animate-pulse" />
          </div>
        </div>
        <div className="mt-2 text-[11px] text-fg-mute">142 / 280 chars · be specific</div>
      </div>
      <div className="absolute inset-x-0 top-[720px] px-8 text-[11px] text-fg-mute leading-[16px]">
        this is the only thing the app will not let you delete.
      </div>
      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <PrimaryButton>save · continue</PrimaryButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 6.2 — Mantra Capture */
export function OnboardMantra() {
  return (
    <Phone label="6.2   Onboarding — Mantra">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        STEP 2 of 5  ·  MANTRA-001
      </div>
      <div className="absolute inset-x-0 top-[160px] px-8">
        <div className="text-[28px] leading-[36px] font-light text-fg tracking-heading">
          pick a mantra.<br />speak it three times.
        </div>
        <p className="mt-5 text-[12.5px] text-fg-dim leading-[19px] max-w-[348px]">
          you’ll say this aloud before unlocking your phone in the morning.
          choose something you’d be proud to hear yourself say.
        </p>
      </div>
      <div className="absolute inset-x-0 top-[368px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">YOUR MANTRA</div>
        <div className="mt-2 h-px bg-fg-mute" />
        <div className="mt-3 text-[18px] text-fg leading-[26px]">
          "i build things.<br />i don’t scroll them."
        </div>
      </div>
      <div className="absolute inset-x-0 top-[538px] px-8">
        <div className="label-caps text-[10px] text-fg-mute">REPS · LISTENING</div>
        <div className="mt-4 flex items-end gap-3">
          <div className="w-14 h-14 rounded-full border-2 border-accent flex items-center justify-center text-accent text-[16px]">✓</div>
          <div className="w-20 h-20 rounded-full border-2 border-accent flex items-center justify-center text-accent text-[18px]">✓</div>
          <div className="w-16 h-16 rounded-full border-2 border-fg-mute flex items-center justify-center text-fg-mute text-[14px]">
            <span className="animate-pulse">●</span>
          </div>
        </div>
        <div className="mt-3 text-[12px] text-fg-dim">listening… say it once more</div>
      </div>
      <div className="absolute inset-x-0 top-[750px] px-8 text-[11px] text-fg-mute leading-[16px]">
        in public? <span className="text-fg-dim underline">switch to whisper mode</span> or <span className="text-fg-dim underline">type fallback</span>.
      </div>
      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>skip · type fallback</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 6.3 — Future Self Video */
export function OnboardFutureSelf() {
  return (
    <Phone label="6.3   Onboarding — Future Self">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        STEP 3 of 5  ·  PSYCH-006
      </div>
      <div className="absolute inset-x-0 top-[160px] px-8">
        <div className="text-[28px] leading-[36px] font-light text-fg tracking-heading">
          record a 30-second video<br />for the version of you<br />who wants to quit.
        </div>
      </div>
      <div className="absolute inset-x-0 top-[378px] px-8">
        <div className="relative w-full h-[280px] rounded-[20px] border border-edge bg-[#0e0e0e] overflow-hidden">
          <div className="absolute inset-0 flex items-center justify-center">
            <div className="w-20 h-20 rounded-full border-2 border-danger flex items-center justify-center">
              <div className="w-10 h-10 rounded-full bg-danger" />
            </div>
          </div>
          <div className="absolute top-3 left-3 text-[10px] text-danger label-caps">● REC  ·  0:12</div>
          <div className="absolute bottom-3 inset-x-3 text-[10px] text-fg-mute label-caps text-center">
            front camera · audio level ●●●○○
          </div>
        </div>
        <p className="mt-4 text-[11.5px] text-fg-dim leading-[18px]">
          say what your future self stands to lose. say it like you mean it. this clip
          will autoplay before every uninstall attempt and at every relapse moment.
        </p>
      </div>
      <div className="absolute inset-x-0 bottom-[40px] px-8 space-y-2">
        <PrimaryButton>stop · save</PrimaryButton>
        <HollowButton className="!h-11 !text-[12px]">restart</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 6.4 — VIP Contacts */
export function OnboardVIP() {
  const contacts = [
    ["Asha (wife)", "+91 ••• ••8421", "vip · always ring"],
    ["Mom",          "+91 ••• ••2109", "vip · always ring"],
    ["Dr. Patel",    "+91 ••• ••0011", "vip · always ring"],
    ["Sponsor (Anuj)","+91 ••• ••5577", "vip · also notified on uninstall"],
  ];
  return (
    <Phone label="6.4   Onboarding — VIP">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        STEP 4 of 5  ·  CALLS-001 / SOCIAL-008
      </div>
      <div className="absolute inset-x-0 top-[160px] px-8">
        <div className="text-[28px] leading-[36px] font-light text-fg tracking-heading">
          pick who can always<br />reach you.
        </div>
        <p className="mt-4 text-[12.5px] text-fg-dim leading-[19px] max-w-[348px]">
          up to ten numbers. these break Dream Mode, ring through DND, and bypass focus blocks.
          24-hour cooldown on edits — choose carefully.
        </p>
      </div>
      <div className="absolute inset-x-0 top-[378px] px-8 space-y-2">
        {contacts.map(([n, p, m], i) => (
          <div key={i} className="bg-surface rounded-[12px] px-5 py-3 flex items-center gap-3">
            <div className="w-9 h-9 rounded-full border border-edge text-[11px] flex items-center justify-center text-fg-dim font-medium">{n[0]}</div>
            <div className="flex-1 min-w-0">
              <div className="text-[13.5px] text-fg font-medium truncate">{n}</div>
              <div className="text-[10.5px] text-fg-dim">{p} · {m}</div>
            </div>
            <span className="text-[14px] text-accent">✓</span>
          </div>
        ))}
        <div className="text-center text-[11px] text-fg-mute pt-2">+ add up to 6 more</div>
      </div>
      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <PrimaryButton>save · continue</PrimaryButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 6.5 — Baseline observation start */
export function OnboardBaseline() {
  return (
    <Phone label="6.5   Onboarding — Baseline">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        STEP 5 of 5  ·  TRACK-004
      </div>
      <div className="absolute inset-x-0 top-[160px] px-8">
        <div className="text-[28px] leading-[36px] font-light text-fg tracking-heading">
          we’ll watch you<br />for 72 hours.<br /><span className="text-fg-dim">nothing’s blocked yet.</span>
        </div>
        <p className="mt-5 text-[12.5px] text-fg-dim leading-[19px] max-w-[348px]">
          before Level 1 begins, the app observes your real baseline so it can scale you
          up gently — never more than +25% behavior change from where you actually are.
        </p>
      </div>
      <div className="absolute inset-x-0 top-[424px] px-8">
        <div className="bg-surface rounded-[14px] p-5 space-y-2">
          <div className="flex items-baseline justify-between text-[12.5px]">
            <span className="text-fg-dim">observation ends</span>
            <span className="text-fg font-medium tabular-nums">Sat, May 26 · 9:42</span>
          </div>
          <div className="flex items-baseline justify-between text-[12.5px]">
            <span className="text-fg-dim">restrictions active</span>
            <span className="text-fg-mute">none yet</span>
          </div>
          <div className="flex items-baseline justify-between text-[12.5px]">
            <span className="text-fg-dim">unlock interview</span>
            <span className="text-success font-medium">live now</span>
          </div>
          <div className="flex items-baseline justify-between text-[12.5px]">
            <span className="text-fg-dim">L1 begins</span>
            <span className="text-accent font-medium">in 72h</span>
          </div>
        </div>
      </div>
      <div className="absolute inset-x-0 top-[632px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-3">DURING BASELINE, YOU’LL SEE</div>
        <ul className="text-[12px] text-fg-dim leading-[20px] list-disc pl-5">
          <li>the unlock interview (one tiny question per unlock)</li>
          <li>your receipts after closing apps</li>
          <li>your week ledger building up — no judgement yet</li>
        </ul>
      </div>
      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <PrimaryButton>start observation · enter the app</PrimaryButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}
