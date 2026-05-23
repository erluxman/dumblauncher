import { ReactNode } from "react";

export function Label({ children, color = "text-fg-mute", className = "" }: { children: ReactNode; color?: string; className?: string }) {
  return <div className={`label-caps text-[10px] ${color} ${className}`}>{children}</div>;
}

export function Card({ children, className = "" }: { children: ReactNode; className?: string }) {
  return <div className={`bg-surface rounded-[14px] p-5 ${className}`}>{children}</div>;
}

export function Dashed() {
  return (
    <div className="dashed-row text-[10px] text-fg-mute my-2 select-none">
      - - - - - - - - - - - - - - - - - - - - - - - -
    </div>
  );
}

export function GhostButton({ children, className = "" }: { children: ReactNode; className?: string }) {
  return (
    <div className={`h-14 rounded-full border border-edge bg-surface text-[13px] font-medium text-fg flex items-center justify-center ${className}`}>
      {children}
    </div>
  );
}

export function HollowButton({ children, className = "" }: { children: ReactNode; className?: string }) {
  return (
    <div className={`h-14 rounded-full border border-edge bg-ink text-[13px] font-medium text-fg-dim flex items-center justify-center ${className}`}>
      {children}
    </div>
  );
}

export function PrimaryButton({ children, className = "" }: { children: ReactNode; className?: string }) {
  return (
    <div className={`h-14 rounded-full bg-accent text-[13px] font-semibold text-ink flex items-center justify-center ${className}`}>
      {children}
    </div>
  );
}

/** Ledger row: label · value · signed-amount. */
export function LedgerRow({ label, value, amount }: { label: string; value: string; amount: string }) {
  const isNeg = amount.includes("−") || amount.includes("-");
  const isDot = amount === "·";
  const color = isNeg ? "text-danger" : isDot ? "text-fg-mute" : "text-success";
  return (
    <div className="flex items-baseline text-[13px] font-medium leading-7 tabular-nums">
      <span className="text-fg-dim font-normal w-[110px] shrink-0">{label}</span>
      <span className="text-fg flex-1">{value}</span>
      <span className={color}>{amount}</span>
    </div>
  );
}

/** Week bars: 7 vertical bars, varied heights. */
export function WeekBars({
  heights,
  doneFlags,
  todayIndex,
}: {
  heights: number[];
  doneFlags: boolean[];
  todayIndex: number;
}) {
  const days = ["M", "T", "W", "T", "F", "S", "S"];
  return (
    <div className="flex items-end gap-[18px] h-[60px]">
      {days.map((d, i) => {
        const h = heights[i];
        const done = doneFlags[i];
        const today = i === todayIndex;
        return (
          <div key={i} className="flex flex-col items-center gap-2">
            <div className="relative w-4 h-9 flex items-end">
              {h > 0 && (
                <div
                  className={`w-4 rounded-[3px] ${done ? "bg-accent" : "bg-fg-mute"}`}
                  style={{ height: `${h}px` }}
                />
              )}
              {today && (
                <div className="absolute inset-0 border border-fg rounded-[3px]" />
              )}
            </div>
            <span className={`text-[10px] font-medium ${today ? "text-fg" : "text-fg-mute"}`}>{d}</span>
          </div>
        );
      })}
    </div>
  );
}

export function WeekDots({ flags }: { flags: boolean[] }) {
  return (
    <div className="flex gap-[10px]">
      {flags.map((f, i) => (
        <span key={i} className={`block w-2 h-2 rounded-full ${f ? "bg-accent" : "bg-fg-mute"}`} />
      ))}
    </div>
  );
}

export function TaskRow({
  title,
  meta,
  done = false,
}: {
  title: string;
  meta: string;
  done?: boolean;
}) {
  return (
    <div className="bg-surface rounded-[12px] px-5 py-3 flex items-center gap-3">
      <div className={`w-4 h-4 rounded-full border ${done ? "border-accent bg-accent" : "border-fg-mute"}`} />
      <div className="flex-1 min-w-0">
        <div className={`text-[14px] font-medium ${done ? "text-fg-dim line-through" : "text-fg"} truncate`}>{title}</div>
        <div className="text-[11px] text-fg-dim mt-[2px]">{meta}</div>
      </div>
    </div>
  );
}

export function Progress({ pct }: { pct: number }) {
  return (
    <div className="relative h-[3px] w-full bg-surface2 rounded-full overflow-hidden">
      <div className="absolute left-0 top-0 h-full bg-accent rounded-full" style={{ width: `${pct}%` }} />
    </div>
  );
}
