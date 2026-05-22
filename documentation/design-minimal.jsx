// Preview mockup for the minimal launcher home.
// Single-file React component. Paste into any JSX sandbox / Claude canvas.
// Mirrors documentation/design-minimal.md and the implementation in
// app/src/main/java/com/erluxman/focuslauncher/ui/home/minimal/MinimalHomeScreen.kt.

import React, { useState } from "react";

const palette = {
  bg: "#0B0B0E",
  fg: "#EEEEEE",
  outline: "#6E6E78",
  accent: "#E5A95C",
};

const font =
  "-apple-system, BlinkMacSystemFont, 'Inter', 'DM Sans', 'Segoe UI', Roboto, sans-serif";

const sectionStyle = {
  position: "absolute",
  inset: 0,
  padding: "40px 32px 24px",
  display: "flex",
  flexDirection: "column",
  color: palette.fg,
  fontFamily: font,
  fontSize: 20,
  lineHeight: 1.6,
  letterSpacing: 0,
};

function PhoneFrame({ children, label }) {
  return (
    <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
      <div style={{ color: palette.outline, fontFamily: font, fontSize: 14, marginBottom: 8 }}>
        {label}
      </div>
      <div
        style={{
          width: 340,
          height: 720,
          borderRadius: 36,
          background: palette.bg,
          position: "relative",
          overflow: "hidden",
          boxShadow: "0 0 0 6px #1a1a1f, 0 8px 30px rgba(0,0,0,0.5)",
        }}
      >
        {children}
      </div>
    </div>
  );
}

function HeaderBar({ time = "14:08", showHint = true }) {
  return (
    <div
      style={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        fontFamily: font,
        fontSize: 14,
        color: palette.outline,
      }}
    >
      <span>· {time}</span>
      {showHint && <span style={{ opacity: 0.4 }}>↓</span>}
    </div>
  );
}

function Dock({ hidden = false }) {
  if (hidden) return null;
  return (
    <div style={{ marginTop: "auto" }}>
      <div
        style={{
          height: 1,
          background: palette.outline,
          opacity: 0.25,
          marginBottom: 16,
        }}
      />
      <div
        style={{
          display: "flex",
          justifyContent: "space-around",
          alignItems: "center",
          fontFamily: font,
          fontSize: 14,
          color: palette.outline,
        }}
      >
        <span>phone</span>
        <span>msgs</span>
        <span>settings</span>
      </div>
    </div>
  );
}

function Morning() {
  return (
    <div style={sectionStyle}>
      <HeaderBar time="06:42" />
      <div style={{ flex: 0.4 }} />
      <div style={{ fontSize: 32, color: palette.fg }}>good morning.</div>
      <div style={{ height: 40 }} />
      <div style={{ color: palette.outline }}>today's one thing —</div>
      <div style={{ height: 8 }} />
      <div style={{ color: palette.accent }}>ship the design refactor.</div>
      <div style={{ height: 40 }} />
      <div style={{ display: "flex", gap: 32 }}>
        {[
          ["water", true],
          ["stretch", true],
          ["no phone", false],
        ].map(([label, done]) => (
          <div
            key={label}
            style={{ display: "flex", flexDirection: "column", alignItems: "center" }}
          >
            <span style={{ color: done ? palette.fg : palette.outline }}>{label}</span>
            <div style={{ height: 8 }} />
            <span style={{ color: done ? palette.accent : palette.outline, fontSize: 22 }}>
              {done ? "●" : "○"}
            </span>
          </div>
        ))}
      </div>
      <Dock />
    </div>
  );
}

function Work() {
  return (
    <div style={sectionStyle}>
      <HeaderBar time="14:08" />
      <div style={{ flex: 0.4 }} />
      <div style={{ fontSize: 32, color: palette.fg }}>day 12 of work.</div>
      <div style={{ height: 40 }} />
      <div style={{ color: palette.outline }}>today —</div>
      <div style={{ height: 8 }} />
      <div style={{ color: palette.fg }}>47 minutes focused, 12 in distraction.</div>
      <div style={{ height: 40 }} />
      <div style={{ color: palette.outline }}>one thing —</div>
      <div style={{ height: 8 }} />
      <div style={{ color: palette.accent }}>ship the design refactor.</div>
      <div style={{ height: 40 }} />
      <div style={{ color: palette.outline }}>you're here because —</div>
      <div style={{ height: 8 }} />
      <div style={{ color: palette.outline, fontSize: 16 }}>
        I want to be the person who builds, not the one who scrolls.
      </div>
      <Dock />
    </div>
  );
}

function Shutdown() {
  return (
    <div style={sectionStyle}>
      <HeaderBar time="19:30" />
      <div style={{ flex: 0.4 }} />
      <div style={{ fontSize: 32, color: palette.fg }}>shutdown.</div>
      <div style={{ height: 40 }} />
      <div style={{ color: palette.outline }}>tomorrow's one thing —</div>
      <div style={{ height: 8 }} />
      <div
        style={{
          color: palette.accent,
          paddingBottom: 4,
          borderBottom: `1px solid ${palette.outline}66`,
        }}
      >
        finish onboarding redesign|
      </div>
      <div style={{ height: 16 }} />
      <div style={{ color: palette.accent, fontSize: 18, alignSelf: "flex-start" }}>save</div>
      <div style={{ height: 40 }} />
      <div style={{ color: palette.outline }}>close the day —</div>
      <div style={{ height: 8 }} />
      {[
        ["review tomorrow", true],
        ["tomorrow's one thing", false],
        ["one-line journal", false],
      ].map(([label, done]) => (
        <div
          key={label}
          style={{
            display: "flex",
            alignItems: "center",
            padding: "4px 0",
            color: done ? palette.fg : palette.outline,
          }}
        >
          <span
            style={{ marginRight: 12, color: done ? palette.accent : palette.outline }}
          >
            {done ? "●" : "○"}
          </span>
          {label}
        </div>
      ))}
      <Dock />
    </div>
  );
}

function Dream() {
  return (
    <div style={sectionStyle}>
      {/* no header, no dock */}
      <div style={{ flex: 1 }} />
      <div style={{ textAlign: "center", color: palette.accent, fontSize: 80 }}>●</div>
      <div style={{ height: 40 }} />
      <div style={{ textAlign: "center", color: palette.outline }}>
        inhale 4. hold 7. exhale 8.
      </div>
      <div style={{ flex: 0.6 }} />
      <div style={{ textAlign: "center", color: palette.outline }}>tomorrow —</div>
      <div style={{ height: 8 }} />
      <div style={{ textAlign: "center", color: palette.fg }}>ship the design refactor.</div>
      <div style={{ flex: 1 }} />
    </div>
  );
}

function Dashboard() {
  const group = (title, lines) => (
    <div style={{ marginBottom: 32 }}>
      <div style={{ color: palette.outline, fontSize: 14, marginBottom: 12 }}>{title}</div>
      {lines.map((l, i) => (
        <div key={i} style={{ color: palette.fg, marginBottom: 6 }}>
          {l}
        </div>
      ))}
    </div>
  );
  return (
    <div style={{ ...sectionStyle, overflowY: "auto" }}>
      <HeaderBar time="14:12" showHint={false} />
      <div style={{ color: palette.outline, fontSize: 14, marginTop: 8, marginBottom: 24, textAlign: "center" }}>
        ↑ swipe up to return
      </div>
      {group("today", [
        "12 days of work.",
        "6 h 24 m asleep last night.",
        "recovery: ready.",
        "3 commits.",
        "10 minutes of meditation.",
      ])}
      {group("this week", [
        "78 minutes meditation across 4 days.",
        "245 minutes reading across 5 days.",
        "2 days at the gym.",
      ])}
      {group("this year", [
        "482 days to graduate.",
        "47 beach saturdays left in your life.",
      ])}
      <div
        style={{
          marginTop: "auto",
          color: palette.outline,
          fontSize: 14,
          textAlign: "center",
          paddingTop: 24,
        }}
      >
        transparency · uninstall
      </div>
    </div>
  );
}

export default function MinimalHomeMockup() {
  const [view, setView] = useState("work");

  const views = [
    { id: "morning", label: "Morning (05–09)", Comp: Morning },
    { id: "work", label: "Work (09–17)", Comp: Work },
    { id: "shutdown", label: "Evening (17–22)", Comp: Shutdown },
    { id: "dream", label: "Night (22–05)", Comp: Dream },
    { id: "dashboard", label: "Dashboard (swipe ↓)", Comp: Dashboard },
  ];
  const Active = views.find((v) => v.id === view).Comp;

  return (
    <div
      style={{
        minHeight: "100vh",
        background: "#1A1A1F",
        padding: "32px 16px 64px",
        fontFamily: font,
        color: palette.fg,
      }}
    >
      <div style={{ maxWidth: 720, margin: "0 auto" }}>
        <h1 style={{ fontSize: 20, fontWeight: 400, marginBottom: 4 }}>
          minimal home — preview
        </h1>
        <p style={{ color: palette.outline, fontSize: 14, marginBottom: 24 }}>
          single section, type-only, hour-of-day swap. dock pinned. all stats
          live behind one swipe-down.
        </p>

        <div
          style={{
            display: "flex",
            flexWrap: "wrap",
            gap: 8,
            marginBottom: 24,
          }}
        >
          {views.map((v) => (
            <button
              key={v.id}
              onClick={() => setView(v.id)}
              style={{
                background: view === v.id ? palette.accent : "transparent",
                color: view === v.id ? palette.bg : palette.fg,
                border: `1px solid ${view === v.id ? palette.accent : palette.outline}`,
                padding: "6px 12px",
                borderRadius: 999,
                fontFamily: font,
                fontSize: 14,
                cursor: "pointer",
              }}
            >
              {v.label}
            </button>
          ))}
        </div>

        <div style={{ display: "flex", justifyContent: "center" }}>
          <PhoneFrame label={views.find((v) => v.id === view).label}>
            <Active />
          </PhoneFrame>
        </div>

        <div
          style={{
            marginTop: 40,
            color: palette.outline,
            fontSize: 14,
            lineHeight: 1.7,
            maxWidth: 480,
            margin: "40px auto 0",
          }}
        >
          <p>palette: bg {palette.bg} · fg {palette.fg} · outline {palette.outline} · accent {palette.accent}</p>
          <p>type: system sans-serif, 32sp display / 20sp body / 14sp caption · letter-spacing 0</p>
          <p>layout: 32dp horizontal padding · content centered · dock pinned · no cards</p>
        </div>
      </div>
    </div>
  );
}
