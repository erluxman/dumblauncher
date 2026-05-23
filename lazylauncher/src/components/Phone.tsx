import { ReactNode } from "react";

/**
 * 412 × 915 phone frame. Rounded, OLED-black, with a hairline border.
 * Each screen renders its own status bar and home indicator so it can
 * opt out (e.g. fullscreen intercepts).
 */
export function Phone({ children, label }: { children: ReactNode; label?: string }) {
  return (
    <div className="relative shrink-0">
      {label && (
        <div className="absolute -top-7 left-1 text-[11px] text-fg-mute font-medium">
          {label}
        </div>
      )}
      <div
        className="relative w-[412px] h-[915px] rounded-[48px] bg-ink border border-edge overflow-hidden"
        style={{ boxShadow: "0 24px 60px -20px rgba(0,0,0,0.6)" }}
      >
        {children}
      </div>
    </div>
  );
}

export function StatusBar({ time = "9:42" }: { time?: string }) {
  return (
    <div className="absolute top-0 left-0 right-0 px-8 pt-[22px] flex items-start justify-between text-[12.5px] z-10 select-none">
      <span className="font-semibold text-fg tabular-nums">{time}</span>
      <span className="font-medium text-fg-dim tracking-wider text-[11px] pt-[2px]">●●● ●● 100%</span>
    </div>
  );
}

export function HomeIndicator() {
  return (
    <div className="absolute left-1/2 -translate-x-1/2 bottom-[18px] w-[136px] h-[4px] bg-fg-mute rounded-full" />
  );
}

export function TopBadge({ level, form, points }: { level: number; form: number; points: number }) {
  return (
    <div className="absolute right-8 top-[58px] text-[11px] font-medium text-fg-dim tabular-nums">
      L{level} · {form}% · {points}
    </div>
  );
}
