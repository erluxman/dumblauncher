import { Phone, StatusBar, HomeIndicator } from "../components/Phone";
import { Dashed, HollowButton, LedgerRow } from "../components/primitives";

/* 1.1 ─ Unlock: Mood */
export function UnlockMood() {
  return (
    <Phone label="1.1   Unlock — Mood">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[9px] text-fg-mute">
        UNLOCK · +1 PT · 8m SINCE LAST
      </div>
      <div className="absolute inset-x-0 top-[330px] px-8">
        <div className="text-[30px] font-light text-fg tracking-heading">mood right now?</div>
      </div>
      <div className="absolute inset-x-0 top-[460px] px-8 grid grid-cols-3">
        {[
          ["😞", "low"],
          ["😐", "ok"],
          ["🙂", "good"],
        ].map(([e, l], i) => (
          <button key={i} className="flex flex-col items-center gap-4 py-4 hover:bg-surface/40 rounded-2xl transition-colors">
            <span className="text-[40px] leading-none">{e}</span>
            <span className="text-[12px] text-fg-dim">{l}</span>
          </button>
        ))}
      </div>
      <div className="absolute left-8 top-[720px] text-[12px] text-fg-mute">or skip ↓</div>
      <div className="absolute left-8 right-8 bottom-[34px] text-[11px] text-fg-mute">
        7 mood pings tracked this week
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 1.2 ─ Unlock: Typed */
export function UnlockTyped() {
  return (
    <Phone label="1.2   Unlock — Typed">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[9px] text-fg-mute">
        UNLOCK · +2 PTS · 2h SINCE LAST
      </div>
      <div className="absolute inset-x-0 top-[270px] px-8">
        <div className="text-[28px] leading-[36px] font-light text-fg tracking-heading">
          what did you ship<br />in the last 2h?
        </div>
      </div>
      <div className="absolute inset-x-0 top-[460px] px-8">
        <div className="h-px bg-fg-mute" />
        <div className="mt-3 text-[17px] text-fg-dim flex items-center">
          wrapped the Q2 design draft
          <span className="ml-1 inline-block w-[2px] h-[18px] bg-accent animate-pulse" />
        </div>
        <div className="mt-3 text-[11px] text-fg-mute">27 / 280 chars · tap to save</div>
      </div>
      <div className="absolute left-8 right-8 bottom-[34px] text-[11px] text-fg-mute">
        11 things shipped this week
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 1.3 ─ Unlock: Sad Self */
export function UnlockSadSelf() {
  return (
    <Phone label="1.3   Unlock — Sad Self">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-danger">
        SAD YOU
      </div>
      <div className="absolute inset-x-0 top-[230px] px-8">
        <div className="text-[30px] leading-[40px] font-light text-fg tracking-heading">
          you opened me<br />14 times today.
        </div>
        <div className="mt-8 text-[17px] text-fg-dim">you said you wouldn’t.</div>
      </div>
      <div className="absolute inset-x-0 top-[480px] px-8">
        <div className="bg-surface rounded-[14px] p-5">
          <div className="label-caps text-[9px] text-fg-mute">TODAY · RECEIPT</div>
          <Dashed />
          <LedgerRow label="twitter" value="47m" amount="−47" />
          <LedgerRow label="tiktok" value="12m" amount="−12" />
          <LedgerRow label="shipped" value="0 todos" amount="·" />
          <Dashed />
          <div className="flex items-baseline justify-between mt-1">
            <span className="label-caps text-[11px] text-fg-dim">NET</span>
            <span className="text-[14px] font-semibold text-danger tabular-nums">−59 pts</span>
          </div>
        </div>
      </div>
      <div className="absolute left-8 right-8 bottom-[34px] text-[12px] text-fg-mute">tap to continue</div>
      <HomeIndicator />
    </Phone>
  );
}

/* 1.4 ─ Unlock: Highlight */
export function UnlockHighlight() {
  return (
    <Phone label="1.4   Unlock — Highlight">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[9px] text-fg-mute tracking-[0.12em]">
        FROM PAGE 47 · ATOMIC HABITS · 2 MONTHS AGO
      </div>
      <div className="absolute left-8 top-[160px] text-[96px] leading-none font-light text-accent select-none">
        “
      </div>
      <div className="absolute inset-x-0 top-[290px] px-8">
        <div className="text-[22px] leading-[32px] font-light text-fg tracking-heading">
          You do not rise to the level of your goals.
          <br />
          <br />
          You fall to the level of your systems.
        </div>
        <div className="mt-10 text-[13px] text-fg-dim">— James Clear</div>
      </div>
      <div className="absolute left-8 right-8 bottom-[110px] text-[11px] text-fg-mute">
        one of 312 highlights you saved.
      </div>
      <div className="absolute left-8 right-8 bottom-[34px]">
        <HollowButton>continue</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}
