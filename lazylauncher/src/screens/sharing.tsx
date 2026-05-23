import { Phone, StatusBar, HomeIndicator, TopBadge } from "../components/Phone";
import { HollowButton, PrimaryButton } from "../components/primitives";

/* 15.1 — Promise Kept Ratio */
export function PromiseKept() {
  const last30 = [1,1,0,1,1,1,1,0,1,1,1,0,1,1,1,1,1,1,0,1,1,1,1,1,0,1,1,1,1,1];
  return (
    <Phone label="15.1   Promise Kept Ratio">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        IDENTITY · IDENT-002 · SELF-TRUST</div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[80px] font-light text-fg tracking-display tabular-nums leading-none">83%</div>
        <div className="mt-2 text-[12px] text-fg-dim">promises kept · last 30 days · ↑ 7% vs prior 30</div>
      </div>

      <div className="absolute inset-x-0 top-[296px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-3">LAST 30 DAYS</div>
        <div className="grid grid-cols-15 gap-[3px]" style={{ gridTemplateColumns: "repeat(15, 1fr)" }}>
          {last30.map((d, i) => (
            <div key={i} className={`h-5 rounded-[3px] ${d ? "bg-accent" : "bg-danger"}`} />
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[420px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">BROKEN RECENTLY</div>
        <div className="space-y-2">
          {[
            ["call mom",                "Sun May 18",  "rescheduled · kept Mon"],
            ["gym at 6am",              "Fri May 16",  "slept in"],
            ["finish chapter 3",        "Thu May 15",  "shipped 6 days late"],
            ["1500w draft by tuesday",  "Tue May 13",  "shipped Wed"],
          ].map(([p, when, why]) => (
            <div key={p} className="bg-surface rounded-[12px] px-4 py-2">
              <div className="flex items-baseline justify-between">
                <span className="text-[12.5px] text-fg">{p}</span>
                <span className="text-[10px] text-fg-mute">{when}</span>
              </div>
              <div className="text-[10.5px] text-fg-dim italic mt-[2px]">{why}</div>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <div className="bg-ink border border-edge rounded-[14px] p-4">
          <div className="label-caps text-[9px] text-fg-mute">SELF-TRUST · COMPOUNDS</div>
          <div className="mt-1 text-[11.5px] text-fg-dim leading-[17px]">
            this score is the longest leading indicator we measure. nothing else moves your
            life as much. 83% is good. 90% is rare.
          </div>
        </div>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 15.2 — Vault Stories (close friends data sharing) */
export function VaultStories() {
  return (
    <Phone label="15.2   Vault Stories">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        VAULT · SOCIAL-024 · 3 TRUSTED</div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[26px] font-bold text-fg tracking-heading leading-tight">
          your real numbers,<br /><span className="text-fg-dim">for three people.</span>
        </div>
        <div className="mt-2 text-[12.5px] text-fg-dim leading-[19px]">
          end-to-end encrypted. nothing edited. brutal honesty club.
          today's vault expires in 24h.
        </div>
      </div>

      <div className="absolute inset-x-0 top-[300px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">TODAY'S VAULT</div>
        <div className="bg-surface rounded-[14px] p-5">
          <div className="grid grid-cols-3 gap-3">
            {[
              ["3h 47m", "TOTAL SCREEN"],
              ["62m",   "TWITTER"],
              ["3",     "TODOS DONE"],
              ["1",     "SURRENDER"],
              ["−3 pts","NET TODAY"],
              ["mid",   "MOOD"],
            ].map(([v, l]) => (
              <div key={l as string}>
                <div className="text-[16px] font-medium text-fg tabular-nums">{v}</div>
                <div className="text-[9px] text-fg-mute label-caps mt-[2px]">{l}</div>
              </div>
            ))}
          </div>
          <div className="mt-4 dashed-row text-[10px] text-fg-mute">- - - - - - - - - - - - - - - - - - -</div>
          <div className="mt-3 text-[12px] text-fg-dim italic leading-[18px]">
            "i opened twitter after seeing a launch i wasn’t invited to.
            it’s the third time this week i’ve done that."
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[576px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">CLOSE FRIENDS · 3</div>
        <div className="space-y-1">
          {[
            ["Anuj C.",  "viewed 18 min ago", "🟢"],
            ["Asha",     "viewed 1h ago",     "🟢"],
            ["Bo Jacobs","not yet today",     "🟡"],
          ].map(([n, when]) => (
            <div key={n} className="bg-surface rounded-[10px] px-4 py-2 flex items-baseline justify-between">
              <span className="text-[12.5px] text-fg">{n}</span>
              <span className="text-[10px] text-fg-mute">{when}</span>
            </div>
          ))}
          <div className="text-center text-[10.5px] text-fg-mute pt-1">+ add 1 more · max 3</div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <PrimaryButton>publish today's vault · expires 24h</PrimaryButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 15.3 — Confession Booth (anonymous failure post) */
export function ConfessionBooth() {
  return (
    <Phone label="15.3   Confession Booth">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        CONFESSION · SOCIAL-023 · ANONYMOUS</div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[26px] font-bold text-fg tracking-heading leading-tight">
          tell the community,<br /><span className="text-fg-dim">not your name.</span>
        </div>
        <div className="mt-2 text-[12.5px] text-fg-dim leading-[19px]">
          identity-stripped at upload. expires in 24h. you get back
          curated stickers, never replies.
        </div>
      </div>

      <div className="absolute inset-x-0 top-[296px] px-8">
        <div className="bg-surface rounded-[14px] p-5 min-h-[160px]">
          <div className="label-caps text-[9px] text-fg-mute">YOUR CONFESSION</div>
          <div className="mt-3 text-[14px] text-fg-dim leading-[22px]">
            i told my partner i quit twitter 41 days ago.
            i opened a private tab on safari this morning at 6am.
            i scrolled for 38 minutes before they woke up.
            <span className="inline-block w-[2px] h-[18px] bg-accent align-middle ml-[1px] animate-pulse" />
          </div>
          <div className="mt-3 text-[10.5px] text-fg-mute">214 / 280 chars · uploads without your handle</div>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[544px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">STICKERS YOU MAY RECEIVE</div>
        <div className="grid grid-cols-4 gap-2">
          {[
            ["🫂",  "i hear you"],
            ["🌱", "still growing"],
            ["⏳", "fix it tomorrow"],
            ["🪞", "tell your partner"],
          ].map(([e, l]) => (
            <div key={l} className="bg-surface rounded-[10px] p-2 text-center">
              <div className="text-[20px]">{e}</div>
              <div className="text-[9px] text-fg-mute mt-1">{l}</div>
            </div>
          ))}
        </div>
        <div className="mt-2 text-[10px] text-fg-mute text-center">no replies. no DMs. only stickers.</div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 space-y-2">
        <PrimaryButton>upload anonymously</PrimaryButton>
        <HollowButton className="!h-11 !text-[12px]">delete · don’t post</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 15.4 — Pre-Commit Post (tomorrow's goals) */
export function PreCommit() {
  return (
    <Phone label="15.4   Pre-Commit">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        PRE-COMMIT · SOCIAL-033 · PUBLIC</div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[26px] font-bold text-fg tracking-heading leading-tight">
          tell the feed what<br />tomorrow looks like.
        </div>
        <div className="mt-2 text-[12.5px] text-fg-dim leading-[19px]">
          auto-graded at 10pm tomorrow. you'll see who did what they said.
        </div>
      </div>

      <div className="absolute inset-x-0 top-[296px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">MY 3 FOR TOMORROW</div>
        <div className="space-y-2">
          {[
            "ship Stage 2 onboarding flow",
            "60 min lift · legs",
            "call mom · 8pm before dinner",
          ].map((t, i) => (
            <div key={i} className="bg-surface rounded-[12px] p-3 flex items-center gap-3">
              <div className="w-6 h-6 rounded-full border border-fg-mute flex items-center justify-center text-[10px] text-fg-mute">{i + 1}</div>
              <div className="flex-1 text-[13px] text-fg">{t}</div>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[508px] px-8">
        <div className="label-caps text-[10px] text-fg-mute mb-2">VISIBLE TO · GROUP</div>
        <div className="flex gap-2 flex-wrap">
          {["Anuj","Sara","Bijou","John","Pawan"].map((p) => (
            <span key={p} className="bg-surface rounded-full px-3 py-1 text-[11px] text-fg-dim">{p}</span>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[612px] px-8">
        <div className="bg-ink border border-edge rounded-[14px] p-4">
          <div className="label-caps text-[9px] text-fg-mute">AUTO-GRADE</div>
          <div className="mt-1 text-[11.5px] text-fg-dim leading-[17px]">
            at 10pm tomorrow you'll get a public 3/3 · 2/3 · 1/3 · 0/3 stamp.
            kept = +6 pts. failed = the feed sees.
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <PrimaryButton>post to the feed · spend 6 pts</PrimaryButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}
