import { FEATURES, STAGES, Feature } from "./data/features";

/* ── Helpers ───────────────────────────────────────────────── */
function ScoreRow({ label, value }: { label: string; value: number }) {
  return (
    <div className="flex items-center gap-3 text-[10.5px]">
      <span className="label-caps text-fg-mute w-[80px]">{label}</span>
      <span className="font-mono text-fg-dim tracking-[0.18em] tabular-nums">
        {"●".repeat(value)}{"○".repeat(5 - value)}
      </span>
      <span className="text-fg-dim tabular-nums">{value}</span>
    </div>
  );
}

function FeatureCard({ f }: { f: Feature }) {
  return (
    <div className="bg-surface rounded-[14px] p-5 flex flex-col gap-3">
      {/* header */}
      <div className="flex items-baseline justify-between gap-3">
        <span className="font-mono text-[12px] text-accent font-semibold tracking-wider">{f.id}</span>
        <span className="label-caps text-[9px] text-fg-mute">STAGE {f.stage} · {f.category}</span>
      </div>

      {/* name */}
      <div className="text-[17px] leading-[22px] font-semibold text-fg tracking-heading">{f.name}</div>

      {/* description */}
      <div className="text-[11.5px] leading-[17px] text-fg-dim">{f.description}</div>

      {/* scores */}
      <div className="grid grid-cols-2 gap-x-4 gap-y-[3px] mt-1">
        <ScoreRow label="priority" value={f.priority} />
        <ScoreRow label="impact"   value={f.impact} />
        <ScoreRow label="difficulty" value={f.difficulty} />
        <ScoreRow label="feasibility" value={f.feasibility} />
      </div>

      {/* deps */}
      <div className="text-[10.5px] text-fg-mute leading-[15px] mt-1">
        <span className="label-caps text-[9px] text-fg-mute">depends on </span>
        <span className="font-mono text-fg-dim">
          {f.deps.length ? f.deps.join(" · ") : "—"}
        </span>
      </div>
    </div>
  );
}

/* ── Cover ─────────────────────────────────────────────────── */
function Cover() {
  const count = FEATURES.length;
  const byStage = STAGES.map((s) => FEATURES.filter((f) => f.stage === s.stage).length);
  return (
    <section className="doc-page !justify-between">
      <div>
        <div className="label-caps text-[11px] text-accent">FEATURES CATALOG · v0.1</div>
        <div className="mt-3 text-[12px] text-fg-mute">2026 · erluxman</div>
      </div>

      <div>
        <div className="text-[80px] leading-[1] font-bold tracking-display text-fg">{count}<br />Features</div>
        <p className="mt-8 text-[16px] text-fg-dim leading-[24px] max-w-[540px]">
          Every feature in the Lazy Launcher master spec, organized by stage. Two per page.
          Each card carries the spec scores, dependencies, and the category it belongs to.
        </p>

        <div className="mt-10 grid grid-cols-4 gap-3 max-w-[640px]">
          {STAGES.map((s, i) => (
            <div key={s.stage} className="bg-surface rounded-[12px] p-4">
              <div className="label-caps text-[9px] text-accent">STAGE {s.stage} · {s.name}</div>
              <div className="mt-2 text-[28px] font-light text-fg tracking-display tabular-nums">{byStage[i]}</div>
              <div className="text-[10px] text-fg-mute">{s.window}</div>
            </div>
          ))}
        </div>
      </div>

      <div className="text-[11px] text-fg-mute leading-[18px]">
        Scores · priority / difficulty / impact / feasibility on 5-point dot rows.<br />
        Dependencies · other feature IDs this one requires to exist first.<br />
        18 "technically impossible" features are excluded from this catalog.
      </div>
    </section>
  );
}

/* ── Stage Intro Page ──────────────────────────────────────── */
function StageIntro({ stage }: { stage: typeof STAGES[number] }) {
  const features = FEATURES.filter((f) => f.stage === stage.stage);
  const categories = Array.from(new Set(features.map((f) => f.category)));
  return (
    <section className="doc-page">
      <div className="label-caps text-[11px] text-accent">STAGE {stage.stage} · {stage.window.toUpperCase()}</div>
      <div className="mt-3 text-[48px] font-bold leading-[1.05] tracking-display text-fg">
        {stage.name} <span className="text-fg-mute">— {stage.title}</span>
      </div>

      <p className="mt-8 text-[16px] text-fg leading-[24px] max-w-[640px]">{stage.summary}</p>

      <div className="mt-12 flex items-baseline gap-6">
        <div>
          <div className="label-caps text-[10px] text-fg-mute">FEATURES</div>
          <div className="text-[40px] font-light text-fg tracking-display tabular-nums">{features.length}</div>
        </div>
        <div>
          <div className="label-caps text-[10px] text-fg-mute">CATEGORIES</div>
          <div className="text-[40px] font-light text-fg tracking-display tabular-nums">{categories.length}</div>
        </div>
        <div>
          <div className="label-caps text-[10px] text-fg-mute">PAGES TO FOLLOW</div>
          <div className="text-[40px] font-light text-fg tracking-display tabular-nums">{Math.ceil(features.length / 2)}</div>
        </div>
      </div>

      <div className="mt-10 max-w-[640px]">
        <div className="label-caps text-[10px] text-fg-mute mb-3">CATEGORIES IN THIS STAGE</div>
        <div className="flex flex-wrap gap-2">
          {categories.map((c) => (
            <span key={c} className="bg-surface rounded-full px-3 py-[6px] text-[11px] text-fg-dim font-mono">{c}</span>
          ))}
        </div>
      </div>

      <div className="mt-auto pt-12 text-[10.5px] text-fg-mute leading-[16px] max-w-[600px]">
        The pages that follow show every {stage.name} feature with its scores and dependencies.
        IDs are stable across stages — a feature that appears in Stage {stage.stage} may be referenced
        by features in later stages via the "depends on" line.
      </div>
    </section>
  );
}

/* ── Feature Pair Page ─────────────────────────────────────── */
function PairPage({ a, b }: { a: Feature; b?: Feature }) {
  return (
    <section className="doc-page !gap-6">
      <div className="flex items-baseline justify-between">
        <div className="label-caps text-[10px] text-fg-mute">STAGE {a.stage} · {a.category}{b && a.category !== b.category ? ` / ${b.category}` : ""}</div>
        <div className="font-mono text-[10px] text-fg-mute">{a.id}{b ? ` · ${b.id}` : ""}</div>
      </div>
      <FeatureCard f={a} />
      {b && <FeatureCard f={b} />}
    </section>
  );
}

/* ── Document ──────────────────────────────────────────────── */
export default function FeaturesDoc() {
  // Group features by stage, then pair them up within each stage.
  const pairsPages: { stage: number; pairs: [Feature, Feature?][] }[] = STAGES.map((s) => {
    const list = FEATURES.filter((f) => f.stage === s.stage);
    const pairs: [Feature, Feature?][] = [];
    for (let i = 0; i < list.length; i += 2) {
      pairs.push([list[i], list[i + 1]]);
    }
    return { stage: s.stage, pairs };
  });

  return (
    <div className="doc-root">
      <Cover />
      {STAGES.map((s, i) => (
        <div key={s.stage}>
          <StageIntro stage={s} />
          {pairsPages[i].pairs.map((p, idx) => (
            <PairPage key={`${s.stage}-${idx}`} a={p[0]} b={p[1]} />
          ))}
        </div>
      ))}
    </div>
  );
}
