import { Phone, StatusBar, HomeIndicator, TopBadge } from "../components/Phone";
import { HollowButton, PrimaryButton } from "../components/primitives";

/* 9.1 — Phone Funeral */
export function PhoneFuneral() {
  const attendees = ["AC","SD","PR","BJ","RK","JV","TM"];
  return (
    <Phone label="9.1   Phone Funeral">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        PHONE FUNERAL · SOCIAL-040
      </div>
      <div className="absolute inset-x-0 top-[136px] px-8">
        <div className="text-[28px] font-light text-fg tracking-heading leading-[36px]">
          today we remember<br /><span className="text-fg-mute">Instagram.</span>
        </div>
        <div className="mt-3 text-[12.5px] text-fg-dim">30 days, zero opens. the community will witness.</div>
      </div>

      <div className="absolute inset-x-0 top-[280px] px-8">
        <div className="bg-surface rounded-[14px] p-6 text-center">
          <div className="font-mono text-[10px] text-fg-mute label-caps tracking-[0.2em]">— HERE LIES —</div>
          <div className="mt-4 text-[34px] font-light text-fg tracking-display">Instagram</div>
          <div className="mt-2 text-[11px] text-fg-mute">installed feb 2014  ·  killed may 23, 2026</div>
          <div className="mt-5 dashed-row text-[10px] text-fg-mute">- - - - - - - - - - - - - - - - - - - -</div>
          <div className="mt-4 text-[12px] text-fg-dim italic leading-[18px] max-w-[250px] mx-auto">
            "it took 2,847 hours from him.<br />he is reclaiming them now."
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[576px] px-8">
        <div className="label-caps text-[9.5px] text-fg-mute mb-2">ATTENDING · 7 OF GROUP</div>
        <div className="flex flex-wrap gap-2">
          {attendees.map((a) => (
            <div key={a} className="w-9 h-9 rounded-full border border-edge bg-ink flex items-center justify-center text-[11px] text-fg-dim font-medium">{a}</div>
          ))}
        </div>
        <div className="mt-3 text-[11px] text-fg-mute">
          Anuj wrote: <span className="text-fg-dim italic">"proud of you, brother."</span>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 space-y-2">
        <PrimaryButton>add a gravestone to my profile</PrimaryButton>
        <HollowButton className="!h-11 !text-[12px]">leave the funeral · home</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 9.2 — Boredom Preservatory */
export function Boredom() {
  return (
    <Phone label="9.2   Boredom Preservatory">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 flex items-baseline justify-between">
        <div className="label-caps text-[10px] text-accent">BOREDOM · RESTRICT-005</div>
        <div className="text-[11px] text-fg-mute">+2 pts on complete</div>
      </div>

      <div className="absolute inset-x-0 top-[330px] px-8 text-center">
        <div className="text-[14px] text-fg-mute label-caps tracking-[0.22em]">SIT WITH IT</div>
        <div className="mt-12 text-[96px] font-light text-fg tracking-display tabular-nums leading-none">
          01:47
        </div>
        <div className="mt-6 text-[12.5px] text-fg-mute">remaining of 02:00</div>
      </div>

      <div className="absolute left-1/2 -translate-x-1/2 top-[640px] w-[180px] h-[2px] bg-surface2 rounded-full overflow-hidden">
        <div className="h-full w-[12%] bg-accent" />
      </div>

      <div className="absolute inset-x-0 top-[700px] px-8 text-center text-[11px] text-fg-mute leading-[17px]">
        nothing happens until the timer ends.<br />
        no music, no scrolling, no notifications.<br />
        breathe.
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 text-center text-[10.5px] text-fg-mute">
        leaving early costs 3 pts.
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 9.3 — AI Life Coach */
export function LifeCoach() {
  return (
    <Phone label="9.3   AI Life Coach">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        AI LIFE COACH · LIFE-011 · ON-DEVICE
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[28px] font-bold text-fg tracking-heading">Stoic mode</div>
        <div className="mt-1 text-[12px] text-fg-dim">reads your last 14 days · responds in 4–6 sentences · nothing leaves the device</div>
      </div>

      <div className="absolute inset-x-0 top-[212px] px-8 space-y-3">
        <div className="bg-surface rounded-[14px] rounded-bl-[4px] p-4 max-w-[300px]">
          <div className="label-caps text-[9px] text-fg-mute mb-1">YOU · this morning</div>
          <div className="text-[12.5px] text-fg leading-[18px]">i feel like i’m falling behind everyone in my cohort.</div>
        </div>

        <div className="bg-ink border border-accent/30 rounded-[14px] rounded-br-[4px] p-4 ml-auto max-w-[330px]">
          <div className="label-caps text-[9px] text-accent mb-1">LAZY · 3s thinking</div>
          <div className="text-[12.5px] text-fg-dim leading-[19px]">
            you’ve focused 4h 12m this week, slept 7h on average, shipped 4 of 5 todos.
            that is not falling behind — that is a sustainable pace.
            <br /><br />
            the cohort’s output is mostly performance. yours is measured. you’re doing fine.
            what specifically is making you feel that way today?
          </div>
        </div>

        <div className="bg-surface rounded-[14px] rounded-bl-[4px] p-4 max-w-[300px]">
          <div className="label-caps text-[9px] text-fg-mute mb-1">YOU · now</div>
          <div className="text-[12.5px] text-fg leading-[18px]">i opened twitter and saw a launch i wasn’t invited to.</div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[110px] px-8">
        <div className="bg-surface rounded-full h-[48px] flex items-center px-4 gap-3">
          <span className="text-fg-mute text-[16px]">+</span>
          <span className="text-[13px] text-fg-dim flex-1">type or hold to speak</span>
          <span className="font-mono text-[10px] text-fg-mute">0 / 280</span>
        </div>
        <div className="mt-2 text-center text-[10.5px] text-fg-mute">
          coach pulls from: journal · receipts · sleep · workouts · form
        </div>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 9.4 — Identity Vote */
export function IdentityVote() {
  return (
    <Phone label="9.4   Identity Vote">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 flex items-baseline justify-between">
        <div className="label-caps text-[10px] text-accent">IDENTITY · PSYCH-001</div>
        <div className="text-[11px] text-fg-mute">before opening Instagram</div>
      </div>

      <div className="absolute inset-x-0 top-[180px] px-8">
        <div className="text-[26px] leading-[34px] font-light text-fg tracking-heading">
          this open is a vote.<br /><span className="text-fg-dim">which one are you?</span>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[368px] px-8 space-y-3">
        <button className="w-full bg-surface rounded-[16px] p-5 text-left">
          <div className="flex items-baseline justify-between">
            <span className="text-[18px] font-semibold text-fg tracking-heading">creator</span>
            <span className="text-[10px] text-fg-mute label-caps">YEAR · 247 VOTES</span>
          </div>
          <p className="mt-2 text-[11.5px] text-fg-dim leading-[17px]">
            i’m opening this to find something useful. i’ll close it in 15 min.
          </p>
        </button>

        <button className="w-full bg-ink border border-edge rounded-[16px] p-5 text-left">
          <div className="flex items-baseline justify-between">
            <span className="text-[18px] font-semibold text-fg tracking-heading">consumer</span>
            <span className="text-[10px] text-fg-mute label-caps">YEAR · 1,108 VOTES</span>
          </div>
          <p className="mt-2 text-[11.5px] text-fg-dim leading-[17px]">
            i’m opening this because i’m bored. i’ll be honest about it.
          </p>
        </button>
      </div>

      <div className="absolute inset-x-0 top-[636px] px-8">
        <div className="bg-surface rounded-[14px] p-4">
          <div className="label-caps text-[9px] text-fg-mute">YOUR YEAR SO FAR</div>
          <div className="mt-2 flex items-baseline gap-3">
            <div>
              <div className="text-[28px] font-light text-accent tracking-display tabular-nums">18%</div>
              <div className="text-[9.5px] text-fg-mute mt-1">creator</div>
            </div>
            <div className="text-[20px] font-light text-fg-mute">/</div>
            <div>
              <div className="text-[28px] font-light text-fg-dim tracking-display tabular-nums">82%</div>
              <div className="text-[9.5px] text-fg-mute mt-1">consumer</div>
            </div>
          </div>
          <div className="mt-2 text-[10.5px] text-fg-mute">trending: +2% creator vs last month.</div>
        </div>
      </div>
      <HomeIndicator />
    </Phone>
  );
}
