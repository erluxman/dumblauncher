import { ReactNode } from "react";

/* ── Helpers ───────────────────────────────────────────────── */
function Page({ children, className = "" }: { children: ReactNode; className?: string }) {
  return <section className={`doc-page ${className}`}>{children}</section>;
}
function H1({ children }: { children: ReactNode }) {
  return <h1 className="text-[40px] font-bold leading-[1.06] tracking-display text-fg">{children}</h1>;
}
function H2({ children, accent }: { children: ReactNode; accent?: boolean }) {
  return <h2 className={`text-[24px] font-bold leading-tight tracking-heading mb-3 ${accent ? "text-accent" : "text-fg"}`}>{children}</h2>;
}
function H3({ children }: { children: ReactNode }) {
  return <h3 className="text-[10.5px] font-semibold uppercase tracking-[0.18em] text-fg-mute mt-6 mb-2">{children}</h3>;
}
function P({ children, className = "" }: { children: ReactNode; className?: string }) {
  return <p className={`text-[12.5px] leading-[19px] text-fg-dim mb-3 ${className}`}>{children}</p>;
}
function Pill({ children, color = "fg-mute" }: { children: ReactNode; color?: string }) {
  return <span className={`bg-surface rounded-full px-3 py-[5px] text-[10.5px] font-mono text-${color} mr-2 mb-2 inline-block`}>{children}</span>;
}
function Stat({ value, label, sub }: { value: string; label: string; sub?: string }) {
  return (
    <div>
      <div className="label-caps text-[9px] text-fg-mute">{label}</div>
      <div className="text-[32px] font-light text-fg tracking-display tabular-nums leading-none mt-1">{value}</div>
      {sub && <div className="text-[10px] text-fg-mute mt-1">{sub}</div>}
    </div>
  );
}

/* ── 01 Cover ──────────────────────────────────────────────── */
function Cover() {
  return (
    <Page className="!justify-between">
      <div>
        <div className="label-caps text-[11px] text-accent">GO-TO-MARKET PLAN · v0.1</div>
        <div className="mt-3 text-[12px] text-fg-mute">2026 · erluxman</div>
      </div>
      <div>
        <div className="text-[72px] leading-[1] font-bold tracking-display text-fg">100,000<br />users<br /><span className="text-fg-mute">in 12 months.</span></div>
        <p className="mt-8 text-[16px] text-fg-dim leading-[24px] max-w-[540px]">
          Bootstrap budget · global English-speaking · zero paid acquisition.
          A go-to-market plan that tells the truth about the math first, then
          works backwards to the channels that close the gap.
        </p>
        <p className="mt-10 label-caps text-[11px] text-accent">
          ORGANIC-FIRST · CONTENT-LED · COMMUNITY-COMPOUNDING
        </p>
      </div>
      <div className="text-[11px] text-fg-mute leading-[18px]">
        US · UK · CA · AU primary · India secondary (passive)<br />
        Stage 1 MVP at launch · Stage 2 V1 by month 6
      </div>
    </Page>
  );
}

/* ── 02 Executive Summary ──────────────────────────────────── */
function ExecSummary() {
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">01</div>
      <H1>Executive Summary</H1>
      <P className="mt-6 !text-[14px] !leading-[22px] text-fg max-w-[640px]">
        Lazy Launcher is an Android home-screen replacement that asks one tiny
        question every time you unlock and converts that data into habit-building
        loops. Tasks gate apps. Friction gates distraction. Points record everything.
      </P>

      <H3>The opportunity</H3>
      <P className="max-w-[640px]">
        Average smartphone use is 4h 37m/day (Data.ai, 2024). 71% of US adults feel
        overwhelmed by their phones (Pew, 2023). The digital-wellness category will
        triple from $5.3B → $20B by 2030. There are credible alternatives but none
        combine ambient capture, a real point economy, and intercepts in a single
        Android launcher.
      </P>

      <H3>The wedge</H3>
      <P className="max-w-[640px]">
        Existing tools either block (Opal, Cold Turkey) or strip (Olauncher, Minimalist
        Phone). Lazy Launcher does both, then adds the things they don't: tasks that
        unlock apps, points you spend like time, Sad-Self interventions on time, a
        72-hour anti-uninstall flow. It hurts to leave. That's the moat.
      </P>

      <H3>The plan</H3>
      <P className="max-w-[640px]">
        Bootstrap. Three big launches (Product Hunt, HackerNews, Reddit waves) +
        sustained content (twice-weekly threads, monthly long-form blog) + 5–10
        creator collabs across the year. Word-of-mouth amplified by group features.
        100,000 users by month 12, $0–$2k total spend, mostly tools and merch.
      </P>

      <H3>The honest part</H3>
      <P className="max-w-[640px]">
        100k bootstrap in Y1 is a stretch. Olauncher took ~24 months. Forest took ~6
        years. We have viral mechanics (groups, dual streaks, surrender posts) and a
        sharp narrative the press already wants to tell. The plan models what has to
        be true: one Product-Hunt #1, one HN front-page, three viral threads, one
        7-figure-view TikTok, and 70%+ day-7 retention.
      </P>

      <div className="mt-10 grid grid-cols-4 gap-6 max-w-[640px]">
        <Stat value="100k" label="USERS · Y1" sub="cumulative" />
        <Stat value="$0" label="PAID SPEND" sub="organic only" />
        <Stat value="3" label="LAUNCH EVENTS" sub="PH / HN / Press" />
        <Stat value="$2k" label="TOTAL Y1 COSTS" sub="tools + cloud" />
      </div>
    </Page>
  );
}

/* ── 03 Market ─────────────────────────────────────────────── */
function Market() {
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">02</div>
      <H1>Market &amp; Opportunity</H1>

      <H3>The phone-overuse epidemic, in numbers</H3>
      <div className="grid grid-cols-3 gap-6 max-w-[640px]">
        <Stat value="4h 37m" label="DAILY AVG"        sub="smartphone time, 2024 (Data.ai)" />
        <Stat value="58×"    label="UNLOCKS / DAY"   sub="median knowledge worker" />
        <Stat value="71%"    label="FEEL OVERWHELMED" sub="US adults (Pew, 2023)" />
        <Stat value="$5.3B"  label="MKT TODAY"       sub="digital wellness, 2023" />
        <Stat value="$20B"   label="MKT BY 2030"     sub="projected CAGR 21%" />
        <Stat value="+470%"  label="POST-2020"       sub="anti-social app DLs (Sensor Tower)" />
      </div>

      <H3>Why now</H3>
      <P className="max-w-[640px]">
        Three forces are converging. (1) Cultural fatigue with the engagement
        economy — there is now a thriving genre of "I quit Instagram" essays.
        (2) Hardware experiments (Brick, Light Phone, Unpluq) have proven a
        willingness to pay for friction. (3) On-device LLMs (Gemini Nano, Apple
        Intelligence) make ambient capture finally feasible without sending
        every meal photo to a cloud.
      </P>

      <H3>Where the user lives</H3>
      <P className="max-w-[640px]">
        The buyer is reachable on four surfaces: Reddit (r/digitalminimalism 130k,
        r/dumbphones 240k, r/getdisciplined 1.5M, r/productivity 950k), Twitter/X
        (focus / productivity / build-in-public clusters), YouTube
        (Ali Abdaal, Matt D'Avella, Better Ideas, Cal Newport's audience), and
        TikTok (#dopaminedetox 4.1B views, #digitaldetox 3.2B views).
      </P>

      <H3>Adjacent spends</H3>
      <P className="max-w-[640px]">
        The user is already paying for: Headspace/Calm ($70/yr), Notion/Obsidian
        (free → $96/yr), a Kindle ($120), a journal ($25), a habit-tracker
        (Habitica/Streaks $50/yr), occasionally a Brick ($120) or an Unpluq
        ($50). A $5–8/mo premium for a launcher that closes the loop on all of
        the above is comfortably within their budget.
      </P>
    </Page>
  );
}

/* ── 04 Competitors ────────────────────────────────────────── */
function Competitors() {
  const rows: { name: string; cat: string; users: string; price: string; what: string; gap: string }[] = [
    { name: "Forest",            cat: "focus timer",      users: "3M+",          price: "$3.99",       what: "Plant a tree, scrolling kills it.",                                  gap: "no launcher · single mechanic" },
    { name: "Opal",              cat: "screen-time blocker", users: "1M+",       price: "$60/yr",      what: "iOS/Android. Schedules app blocks.",                                  gap: "iOS-first · no economy · no launcher" },
    { name: "Olauncher",         cat: "minimal launcher", users: "~500k",        price: "free",        what: "Text-only Android launcher.",                                          gap: "no intercepts · no points · no social" },
    { name: "Minimalist Phone",  cat: "minimal launcher", users: "~200k",        price: "free / $",    what: "Pixel-style minimal home.",                                            gap: "no behavioral system" },
    { name: "Niagara Launcher",  cat: "minimal launcher", users: "~3M",          price: "free / $35",  what: "Beautiful single-list launcher.",                                      gap: "still engagement-flavored · no friction" },
    { name: "Light Phone II/III",cat: "hardware",         users: "~50k",         price: "$300",        what: "Dedicated minimal phone, no apps.",                                    gap: "expensive · all-or-nothing" },
    { name: "Brick",             cat: "hardware",         users: "~50k",         price: "$59",         what: "NFC puck to lock distractions.",                                       gap: "one mechanic · easy to bypass" },
    { name: "Unpluq",            cat: "hardware",         users: "~50k",         price: "$59",         what: "NFC tag + app blocker.",                                               gap: "one mechanic · friction wears off" },
    { name: "Freedom",           cat: "blocker",          users: "~2M",          price: "$40/yr",      what: "Cross-device blocker.",                                                gap: "no ambient capture · desktop-first" },
    { name: "Cold Turkey",       cat: "blocker",          users: "~500k",        price: "$39 one-time",what: "Desktop-first website blocker.",                                       gap: "no mobile-first story · no economy" },
    { name: "Habitica",          cat: "habit tracker",    users: "~4M",          price: "free / $",    what: "RPG-style habit tracker.",                                             gap: "no launcher · gamified for engagement, not for quitting" },
    { name: "Streaks",           cat: "habit tracker",    users: "~1M",          price: "$5 once",     what: "Beautiful habit counter.",                                             gap: "no app control · no social · iOS-only" },
  ];
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">03</div>
      <H1>Competitor Landscape</H1>
      <P className="mt-4 !text-[13px] !leading-[20px] text-fg max-w-[640px]">
        The market is fragmented into three pieces: minimal launchers (no behavioral
        system), blockers / hardware (one mechanic), and habit trackers (the engagement
        flavor of the very thing the user is trying to escape). Lazy Launcher is the
        first to combine the launcher, the intercepts, the economy, and the social
        accountability in a single Android-native package.
      </P>

      <div className="mt-6 bg-surface rounded-[14px] p-4 max-w-[700px]">
        <div className="grid grid-cols-[110px,90px,70px,80px,1fr] gap-3 text-[9px] label-caps text-fg-mute border-b border-edge pb-2">
          <span>product</span><span>category</span><span>users</span><span>price</span><span>gap we exploit</span>
        </div>
        {rows.map((r) => (
          <div key={r.name} className="grid grid-cols-[110px,90px,70px,80px,1fr] gap-3 text-[10.5px] py-[5px] border-b border-edge/60 last:border-0">
            <span className="text-fg font-semibold">{r.name}</span>
            <span className="text-fg-dim">{r.cat}</span>
            <span className="text-fg-dim tabular-nums">{r.users}</span>
            <span className="text-fg-dim tabular-nums">{r.price}</span>
            <span className="text-fg-mute">{r.gap}</span>
          </div>
        ))}
      </div>

      <H3>The narrative wedge</H3>
      <P className="max-w-[640px]">
        "Other apps block your phone. Lazy Launcher makes you earn it." Productivity-
        anyone has seen the blocker pitch a thousand times. The earn-your-phone framing
        is fresh, points to the mechanic, and gives the press a story to write that
        isn't another digital-detox piece.
      </P>
    </Page>
  );
}

/* ── 05 Personas ───────────────────────────────────────────── */
function Personas() {
  const personas = [
    {
      name: "The Burned-Out Knowledge Worker",
      who: "25–32 · Tech / finance / design · $80–150k · SF, NYC, London, Toronto, Berlin",
      hooks: ["scrolls 4–5h/day on phone", "has tried Forest, Opal, deleting IG", "identifies as 'should-be-doing-more'", "owns AirPods, Kindle, an analog journal"],
      buys: "Premium ($6/mo) for backup, AI insights, group features.",
      reach: "Reddit r/digitalminimalism · X (focus + productivity clusters) · Cal Newport's audience",
      voice: "speak to the 'I'm wasting my potential' anxiety, not the screen-time stat.",
    },
    {
      name: "The Maker / Indie",
      who: "22–35 · Designer / dev / writer / artist · solo or small team · ships side projects",
      hooks: ["identifies as a 'builder'", "actively consumes 5h+/day", "uses Notion / Obsidian / Linear", "follows Pieter Levels, Daniel Vassallo, Andrej Karpathy"],
      buys: "Premium for the data export, public builder profile, dual streaks with friends.",
      reach: "Twitter/X (build-in-public) · Indie Hackers · Lobsters · HackerNews",
      voice: "you are what you make. Receipts beat narratives. Build in public.",
    },
    {
      name: "The ADHD-Aware Student",
      who: "18–24 · University or first job · cash-poor but time-rich · openly diagnosed/self-diagnosed ADHD",
      hooks: ["dopamine-fried, knows it", "has tried 6+ productivity apps and abandoned them", "shares mental-health openly", "uses TikTok heavily but hates the habit"],
      buys: "Free tier indefinitely. Word-of-mouth multiplier — they tell 5 friends.",
      reach: "TikTok #dopaminedetox · r/ADHD · r/getdisciplined · YouTube short-form",
      voice: "skip the productivity gospel. Show the receipts. Be honest about how hard it is.",
    },
    {
      name: "The Recovering Doomscroller",
      who: "26–45 · any role · just had a wake-up moment (kid, breakup, health scare, job loss)",
      hooks: ["religious-level commitment to quitting", "needs accountability not motivation", "would pay for hardware (Brick, Light Phone)", "tells friends, posts the journey"],
      buys: "Premium + Lifetime if offered. Money Stake adopter.",
      reach: "press (Wired, Verge), long-form blogs, Medium, Substack",
      voice: "no gimmicks. The app is on your side. You'll fail and the app expects that.",
    },
  ];
  return (
    <>
      <Page>
        <div className="label-caps text-[11px] text-accent">04</div>
        <H1>Target Personas</H1>
        <P className="mt-4 max-w-[640px]">
          Four people, ranked by share of the first 100k installs and revenue contribution.
          We target the first two for paid; the second two are the volume + virality engine.
        </P>
        {personas.slice(0, 2).map((p) => (
          <div key={p.name} className="bg-surface rounded-[14px] p-5 mt-5">
            <div className="text-[18px] font-semibold text-fg tracking-heading">{p.name}</div>
            <div className="text-[11px] text-fg-mute mt-1">{p.who}</div>
            <div className="mt-3 grid grid-cols-2 gap-x-6 gap-y-1">
              {p.hooks.map((h) => (
                <div key={h} className="text-[11.5px] text-fg-dim leading-[17px]">· {h}</div>
              ))}
            </div>
            <div className="mt-3 text-[11.5px] text-fg-dim"><span className="label-caps text-[9px] text-fg-mute mr-2">buys</span>{p.buys}</div>
            <div className="text-[11.5px] text-fg-dim mt-1"><span className="label-caps text-[9px] text-fg-mute mr-2">reach</span>{p.reach}</div>
            <div className="text-[11.5px] text-fg-dim mt-1"><span className="label-caps text-[9px] text-fg-mute mr-2">voice</span>{p.voice}</div>
          </div>
        ))}
      </Page>
      <Page>
        <div className="label-caps text-[11px] text-accent">04 · CONT.</div>
        <H1>Target Personas</H1>
        <P className="mt-4 max-w-[640px]">— continued —</P>
        {personas.slice(2).map((p) => (
          <div key={p.name} className="bg-surface rounded-[14px] p-5 mt-5">
            <div className="text-[18px] font-semibold text-fg tracking-heading">{p.name}</div>
            <div className="text-[11px] text-fg-mute mt-1">{p.who}</div>
            <div className="mt-3 grid grid-cols-2 gap-x-6 gap-y-1">
              {p.hooks.map((h) => (
                <div key={h} className="text-[11.5px] text-fg-dim leading-[17px]">· {h}</div>
              ))}
            </div>
            <div className="mt-3 text-[11.5px] text-fg-dim"><span className="label-caps text-[9px] text-fg-mute mr-2">buys</span>{p.buys}</div>
            <div className="text-[11.5px] text-fg-dim mt-1"><span className="label-caps text-[9px] text-fg-mute mr-2">reach</span>{p.reach}</div>
            <div className="text-[11.5px] text-fg-dim mt-1"><span className="label-caps text-[9px] text-fg-mute mr-2">voice</span>{p.voice}</div>
          </div>
        ))}
      </Page>
    </>
  );
}

/* ── 06 Positioning ────────────────────────────────────────── */
function Positioning() {
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">05</div>
      <H1>Positioning</H1>

      <H3>The one-line</H3>
      <P className="!text-[15px] !leading-[24px] text-fg max-w-[640px]">
        <span className="font-semibold">Lazy Launcher.</span> An Android launcher that
        asks one tiny thing every time you unlock — and quietly builds the habits you
        wish you had.
      </P>

      <H3>Variants by audience</H3>
      <div className="space-y-3 max-w-[640px]">
        <div>
          <div className="label-caps text-[9px] text-fg-mute">TO MAKERS</div>
          <P className="!mb-0">"Other apps block your phone. Lazy Launcher makes you earn it."</P>
        </div>
        <div>
          <div className="label-caps text-[9px] text-fg-mute">TO STUDENTS</div>
          <P className="!mb-0">"Your phone now asks one question per unlock. Three weeks in, the question is the habit."</P>
        </div>
        <div>
          <div className="label-caps text-[9px] text-fg-mute">TO TECH JOURNALISTS</div>
          <P className="!mb-0">"The first launcher built around loss aversion. It hurts to leave. That's the point."</P>
        </div>
        <div>
          <div className="label-caps text-[9px] text-fg-mute">TO YOUR DAD</div>
          <P className="!mb-0">"Replaces your home screen. The phone is quieter. You do less of the things that don't matter."</P>
        </div>
      </div>

      <H3>Brand voice</H3>
      <div className="grid grid-cols-2 gap-x-8 gap-y-2 max-w-[640px] text-[12px]">
        <div><span className="text-success font-semibold">say:</span> <span className="text-fg-dim">honest, slightly mean, type-led, mathematically literate</span></div>
        <div><span className="text-danger font-semibold">avoid:</span> <span className="text-fg-dim">hustle-bro, gamification-positive, productivity-porn</span></div>
        <div><span className="text-success font-semibold">vibe:</span> <span className="text-fg-dim">a friend who has just quit the same thing you want to quit</span></div>
        <div><span className="text-danger font-semibold">never:</span> <span className="text-fg-dim">"limitless potential," "10x," "boss," "grind"</span></div>
      </div>

      <H3>Visual identity</H3>
      <div className="flex flex-wrap">
        <Pill>OLED black</Pill>
        <Pill>warm orange (#FF6B35)</Pill>
        <Pill>Inter Light / Bold</Pill>
        <Pill>type-led, no icons</Pill>
        <Pill>monospace receipts</Pill>
        <Pill>sentence-only data</Pill>
      </div>

      <H3>Anti-positioning</H3>
      <P className="max-w-[640px]">
        Not minimalism for aesthetics. Not gamification for engagement. Not productivity for
        identity. Not a wellness app for vibes. We're a behavioral substrate that happens to
        be a launcher. Press release tone: deadpan, never inspirational.
      </P>
    </Page>
  );
}

/* ── 07 The 100k Math ──────────────────────────────────────── */
function Math100k() {
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">06</div>
      <H1>The 100,000 Math</H1>
      <P className="mt-4 max-w-[640px]">
        Working backwards from 100,000 cumulative installs by month 12. We model a
        viral coefficient k between 0.3–0.5 driven by group features (dual streaks,
        surrender posts, shame notifications) and a 7-day retention of 55–70%.
      </P>

      <H3>The funnel</H3>
      <div className="bg-surface rounded-[14px] p-5 max-w-[700px]">
        <div className="grid grid-cols-[180px,100px,1fr,80px] gap-4 text-[10px] label-caps text-fg-mute border-b border-edge pb-2">
          <span>stage</span><span>rate</span><span>note</span><span>volume</span>
        </div>
        {[
          ["impression → click",    "2.5%",   "varies wildly across channels; PH/HN higher, X lower",         "—"],
          ["click → Play Store visit","60%", "decent landing page",                                            "—"],
          ["visit → install",       "25%",   "category-typical for launchers",                                  "—"],
          ["install → onboarded",   "55%",   "Lazy Launcher onboarding is heavy (20 min); expect drop",         "—"],
          ["onboarded → day-7",     "62%",   "groups + sad self drive retention",                                "—"],
          ["day-7 → day-30",        "80%",   "category-typical for retained users",                              "—"],
          ["day-30 → year-1 viral",  "k=0.4",  "each retained user brings 0.4 new installs",                    "—"],
        ].map(([s, r, n, v]) => (
          <div key={s} className="grid grid-cols-[180px,100px,1fr,80px] gap-4 text-[11px] py-[5px] border-b border-edge/60 last:border-0">
            <span className="text-fg">{s}</span>
            <span className="text-accent font-mono tabular-nums">{r}</span>
            <span className="text-fg-dim">{n}</span>
            <span className="text-fg-mute tabular-nums">{v}</span>
          </div>
        ))}
      </div>

      <H3>What must be true</H3>
      <P className="max-w-[640px]">
        Reach roughly <span className="text-fg font-semibold">2.7M impressions</span> across the
        year, convert to <span className="text-fg font-semibold">~270,000 store visits</span> →{" "}
        <span className="text-fg font-semibold">~67,500 organic installs</span>. With k=0.4
        compounding, the cumulative active base hits ~100k by month 12. Below k=0.3 we miss
        by ~30%. Above k=0.5 we hit 100k by month 8.
      </P>

      <H3>Where the 2.7M comes from</H3>
      <div className="bg-surface rounded-[14px] p-5 max-w-[700px]">
        <div className="grid grid-cols-[1fr,100px,140px] gap-3 text-[10px] label-caps text-fg-mute border-b border-edge pb-2">
          <span>channel</span><span>impressions</span><span>%</span>
        </div>
        {[
          ["Product Hunt launch (top 3 day)",  "120k",  "4.4%"],
          ["HackerNews front page (Show HN)",  "180k",  "6.7%"],
          ["Reddit (consistent posts × 50)",   "400k",  "14.8%"],
          ["Twitter/X (build-in-public · 200 tweets)", "350k", "13.0%"],
          ["YouTube collabs (8 videos × ~50k views)", "400k", "14.8%"],
          ["TikTok organic (40+ videos · 1 hit)",     "700k", "25.9%"],
          ["Press (Wired/Verge/TechCrunch × 1–2)",   "300k", "11.1%"],
          ["Podcast appearances (5–8)",        "150k",  "5.6%"],
          ["Word of mouth uncounted",          "100k",  "3.7%"],
        ].map(([c, i, p]) => (
          <div key={c} className="grid grid-cols-[1fr,100px,140px] gap-3 text-[11px] py-[5px] border-b border-edge/60 last:border-0">
            <span className="text-fg">{c}</span>
            <span className="text-fg-dim tabular-nums">{i}</span>
            <span className="text-fg-mute tabular-nums">{p}</span>
          </div>
        ))}
        <div className="mt-3 pt-2 border-t border-edge flex items-baseline justify-between">
          <span className="label-caps text-[10px] text-fg-mute">TOTAL</span>
          <span className="text-[12px] font-semibold text-fg tabular-nums">~2.7M impressions</span>
        </div>
      </div>
    </Page>
  );
}

/* ── 08 Channel Stack ──────────────────────────────────────── */
function Channels() {
  const channels = [
    {
      name: "Product Hunt launch",
      cost: "$0",
      time: "1 dedicated week to prep, 1 day launch",
      goal: "5–15k installs in week 1, top-3 product of day",
      tactics: [
        "build a hunter list of 200 supporters across X, Indie Hackers, Discord",
        "ship a launch video (90 sec) showing the unlock → todo → unlock loop",
        "Tuesday 12:01 AM PST launch · live in the comments for 14 hrs",
        "second-wave: HN Show post 48 hrs later with PH metrics as social proof",
      ],
    },
    {
      name: "HackerNews launch + sustained presence",
      cost: "$0",
      time: "1 month build, 1 day launch + weekly drips",
      goal: "front page (~180k impressions), 5–15k installs, ongoing comments",
      tactics: [
        "Show HN: build narrative around the point-economy mechanic (not features)",
        "release a 'How the Lobby works' technical post, open-source the lobby logic",
        "respond to every comment in the first 6 hours",
        "follow up monthly with technical pieces (form metric, voice-LLM journaling)",
      ],
    },
    {
      name: "Reddit (4 subs × 12 posts / year)",
      cost: "$0",
      time: "1–2 posts / week",
      goal: "10k installs cumulative · sustained inbound",
      tactics: [
        "r/digitalminimalism · 130k · monthly long-form 'one month with Lazy Launcher'",
        "r/dumbphones · 240k · soft-sell as the 'software dumb phone'",
        "r/getdisciplined · 1.5M · short progress threads with screenshots",
        "r/productivity · 950k · receipts > methods — show the data, not the philosophy",
        "never link the app on first post — link the website with a demo video",
      ],
    },
    {
      name: "Twitter/X — build in public",
      cost: "$0",
      time: "2 threads/week, daily check-ins",
      goal: "10k cumulative installs · loud build narrative",
      tactics: [
        "post daily Lazy Launcher screenshots from your own use (raw)",
        "weekly thread: one mechanic explained with the math behind it",
        "tag and reply-guy with Pieter Levels, Daniel Vassallo, Andrej K., Visa",
        "DM 5 creators / week with a free Premium link — no asks, no follow-up",
      ],
    },
    {
      name: "YouTube — 5–10 creator collabs",
      cost: "$0 (free product, no payment)",
      time: "8 weeks outreach, staggered drops",
      goal: "200–400k total views · 25–40k installs",
      tactics: [
        "tier 1: Ali Abdaal, Matt D'Avella, Better Ideas — 5M+ subs",
        "tier 2: Cal Newport (when he tours), Dan Koe, Justin Sung",
        "send: a 7-day-use video script for them to remix, no embargo",
        "include: 1 unique receipt screenshot per collab for in-video moment",
      ],
    },
    {
      name: "TikTok organic",
      cost: "$0",
      time: "3–5 short videos / week, ongoing",
      goal: "one viral hit (>1M views) → 30–80k installs in one wave",
      tactics: [
        "format: screen recording of receipt + voiceover · 15–25 sec",
        "format: 'I let an app post my failures to my friends' (surrender mechanic)",
        "format: 'this app charges you points to open Instagram' (point economy)",
        "post daily for 6 weeks; one will hit; the rest train the algorithm",
      ],
    },
    {
      name: "Press / podcasts",
      cost: "$0",
      time: "Q2 onwards",
      goal: "1 major (Wired/Verge/TechCrunch), 5–8 podcasts, 300k+ impressions",
      tactics: [
        "story for Wired: 'The first launcher built on loss aversion'",
        "story for Verge: 'You can't uninstall this Android launcher for 72 hours'",
        "podcast pitch: Hard Fork, The Vergecast, Decoder, My First Million, Indie Hackers",
        "press kit ready by month 3: screenshots, founder quote, founder bio, 30 sec demo",
      ],
    },
  ];
  return (
    <>
      <Page>
        <div className="label-caps text-[11px] text-accent">07</div>
        <H1>Channel Stack — Bootstrap</H1>
        <P className="mt-4 max-w-[640px]">
          Seven channels, ranked by 100k-contribution. Every channel is organic and
          time-rich. Total cash budget across the year: tools and hosting, &lt; $2k.
        </P>
        {channels.slice(0, 4).map((c) => (
          <ChannelCard key={c.name} c={c} />
        ))}
      </Page>
      <Page>
        <div className="label-caps text-[11px] text-accent">07 · CONT.</div>
        <H1>Channel Stack — Bootstrap</H1>
        {channels.slice(4).map((c) => (
          <ChannelCard key={c.name} c={c} />
        ))}
      </Page>
    </>
  );
}
function ChannelCard({ c }: { c: { name: string; cost: string; time: string; goal: string; tactics: string[] } }) {
  return (
    <div className="bg-surface rounded-[14px] p-5 mt-4">
      <div className="flex items-baseline justify-between">
        <div className="text-[16px] font-semibold text-fg tracking-heading">{c.name}</div>
        <div className="text-[10px] text-fg-mute font-mono">{c.cost} · {c.time}</div>
      </div>
      <div className="text-[11px] text-fg-dim mt-1"><span className="label-caps text-[9px] text-fg-mute mr-2">goal</span>{c.goal}</div>
      <ul className="mt-3 space-y-1">
        {c.tactics.map((t) => (
          <li key={t} className="text-[11.5px] text-fg-dim leading-[17px]">· {t}</li>
        ))}
      </ul>
    </div>
  );
}

/* ── 09 Content & Community ───────────────────────────────── */
function ContentCommunity() {
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">08</div>
      <H1>Content &amp; Community</H1>

      <H3>Content engine — twice-weekly cadence</H3>
      <div className="grid grid-cols-2 gap-4 max-w-[700px]">
        <div className="bg-surface rounded-[14px] p-5">
          <div className="text-[12px] font-semibold text-fg mb-2">LONG-FORM (Tuesday)</div>
          <ul className="text-[11px] text-fg-dim leading-[17px] space-y-1">
            <li>· "I let an app post my failures to my friends. 30 days in."</li>
            <li>· "The math behind the Form metric (and why your habit tracker is lying to you)"</li>
            <li>· "What I learned by hiding my apps for a year"</li>
            <li>· "Why Lazy Launcher charges you points to open Instagram"</li>
            <li>· founder build logs: weekly, raw, with screenshots</li>
          </ul>
        </div>
        <div className="bg-surface rounded-[14px] p-5">
          <div className="text-[12px] font-semibold text-fg mb-2">SHORT-FORM (Friday)</div>
          <ul className="text-[11px] text-fg-dim leading-[17px] space-y-1">
            <li>· receipt-of-the-week screenshot</li>
            <li>· 60-sec demo of one mechanic per video</li>
            <li>· user-submitted Sad Self messages</li>
            <li>· "this is what my home screen looked like last year" then/now</li>
            <li>· thread cadence: monday + thursday, ~6 tweets each</li>
          </ul>
        </div>
      </div>

      <H3>Community — Discord + Built groups</H3>
      <P className="max-w-[640px]">
        A single Discord, three rooms: <span className="text-fg-dim">#receipts</span> (post yours
        weekly), <span className="text-fg-dim">#surrender-club</span> (no judgment),{" "}
        <span className="text-fg-dim">#shipping</span> (what did you make this week). Goal is
        not engagement — goal is texture for new users to read before they install.
      </P>

      <H3>Ambassador program (Q2 onwards)</H3>
      <P className="max-w-[640px]">
        Free Lifetime Premium to verified Lazy Launcher users (365 days + 3 shipped projects
        + 3 vouches) who agree to: (a) post a weekly receipt publicly, (b) write one blog
        post per quarter, (c) be available for a 30-min interview if asked. Target 30
        ambassadors by month 9.
      </P>

      <H3>Built Wrapped as a content event</H3>
      <P className="max-w-[640px]">
        Year-end Wrapped (SOCIAL-010) is the biggest content moment of Y1. Every retained
        user gets a shareable card. Even 20% share rate from 50k retained users = 10k
        organic posts in week 50–52, late Y1 install spike, and the natural Y2 launch.
      </P>
    </Page>
  );
}

/* ── 10 — 12-month roadmap ────────────────────────────────── */
function Roadmap12Mo() {
  const months: { m: string; theme: string; install: string; cum: string; ship: string }[] = [
    { m: "M1", theme: "closed alpha · 50 testers",        install: "+50",     cum: "50",      ship: "Stage 1 MVP feature-complete · invite-only beta" },
    { m: "M2", theme: "open beta · 500 users",             install: "+450",    cum: "500",     ship: "Play Store unlisted link · Discord launch · first 5 long-form posts" },
    { m: "M3", theme: "press kit + ambassador seed",       install: "+1.5k",   cum: "2k",      ship: "Play Store listing live · first podcast appearance · founder build log starts" },
    { m: "M4", theme: "Product Hunt launch",                install: "+8k",     cum: "10k",     ship: "PH #1-of-day target · HN Show 48 hrs later · twitter blowup engineered" },
    { m: "M5", theme: "first YouTube collab",               install: "+7k",     cum: "17k",     ship: "Matt D'Avella tier collab · receipts content goes weekly" },
    { m: "M6", theme: "Stage 2 V1 launch · Track engine",  install: "+8k",     cum: "25k",     ship: "Sad Self GA · Mantra Gate · HealthConnect · backup live" },
    { m: "M7", theme: "press push · Wired or Verge",       install: "+10k",    cum: "35k",     ship: "embargoed press story · 3 podcast drops in one week" },
    { m: "M8", theme: "TikTok wave · viral hit",            install: "+15k",    cum: "50k",     ship: "one >1M-view TikTok target · 40+ shorts published this month" },
    { m: "M9", theme: "ambassador program GA",              install: "+10k",    cum: "60k",     ship: "30 ambassadors live · receipt wall public · referral loop tightened" },
    { m: "M10", theme: "second YouTube wave · 3 collabs",   install: "+12k",    cum: "72k",     ship: "Ali Abdaal-tier · group features highlighted" },
    { m: "M11", theme: "year-end content prep",            install: "+13k",    cum: "85k",     ship: "Built Wrapped pre-launch · year-in-review content seeds" },
    { m: "M12", theme: "Wrapped launch · 100k push",        install: "+15k",    cum: "100k",    ship: "Built Wrapped GA · year-end thread · Y2 fundraising decision" },
  ];
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">09</div>
      <H1>12-Month Launch Roadmap</H1>
      <P className="mt-4 max-w-[640px]">
        Months are not equal. Three peak weeks carry roughly 40% of total installs:
        Product Hunt launch (M4), press wave (M7), TikTok viral (M8). Everything else
        is the consistent compounding that makes those peaks possible.
      </P>

      <div className="mt-5 bg-surface rounded-[14px] p-4 max-w-[700px]">
        <div className="grid grid-cols-[40px,1fr,80px,80px,1.5fr] gap-3 text-[9px] label-caps text-fg-mute border-b border-edge pb-2">
          <span>month</span><span>theme</span><span>installs</span><span>cumulative</span><span>shipped</span>
        </div>
        {months.map((m) => (
          <div key={m.m} className="grid grid-cols-[40px,1fr,80px,80px,1.5fr] gap-3 text-[10.5px] py-[5px] border-b border-edge/60 last:border-0">
            <span className="font-mono text-accent">{m.m}</span>
            <span className="text-fg">{m.theme}</span>
            <span className="text-fg-dim tabular-nums">{m.install}</span>
            <span className="text-fg font-semibold tabular-nums">{m.cum}</span>
            <span className="text-fg-mute leading-[14px]">{m.ship}</span>
          </div>
        ))}
      </div>

      <H3>What carries the year</H3>
      <P className="max-w-[640px]">
        Three peaks · seven steady channels · one community. The roadmap holds even if
        any one peak underperforms, but it breaks if more than one fails. Mitigation in
        Section 11.
      </P>
    </Page>
  );
}

/* ── 11 KPIs ───────────────────────────────────────────────── */
function Kpis() {
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">10</div>
      <H1>KPIs &amp; Dashboard</H1>
      <P className="mt-4 max-w-[640px]">
        One number per layer. If any layer breaks, the model breaks. We track these weekly
        on the founder's wall, share them publicly month-over-month for accountability.
      </P>

      <div className="grid grid-cols-2 gap-4 max-w-[700px] mt-4">
        {[
          { title: "TOP-LINE", lines: [
            ["cumulative installs", "100k by M12"],
            ["MAU", "60k by M12 (60% retained)"],
            ["WAU / MAU ratio", "> 50%"],
            ["DAU / MAU ratio", "> 25%"],
          ]},
          { title: "FUNNEL", lines: [
            ["install → onboarded", "≥ 55%"],
            ["onboarded → day 7", "≥ 60%"],
            ["day 7 → day 30", "≥ 75%"],
            ["day 30 → day 90", "≥ 80%"],
          ]},
          { title: "VIRAL", lines: [
            ["k-factor (12-week)", "0.4"],
            ["time to first invite", "< 7 days"],
            ["avg group size", "3.5+"],
            ["dual-streak adoption", "≥ 30% of retained"],
          ]},
          { title: "QUALITY", lines: [
            ["NPS", "> 50"],
            ["churn reason: PMF", "< 20%"],
            ["churn reason: bugs", "< 5%"],
            ["weekly journal entries", "≥ 4 / user"],
          ]},
          { title: "REVENUE (M9 onwards)", lines: [
            ["paid conversion", "≥ 5% of WAU"],
            ["ARPU (annualized)", "$36"],
            ["MRR by M12", "$15k"],
            ["LTV / CAC", "n/a (CAC = $0)"],
          ]},
          { title: "BRAND", lines: [
            ["organic searches / mo", "20k by M12"],
            ["Twitter/X followers", "10k by M12"],
            ["Discord members", "5k by M12"],
            ["press mentions", "≥ 15 by M12"],
          ]},
        ].map((g) => (
          <div key={g.title} className="bg-surface rounded-[14px] p-4">
            <div className="label-caps text-[9px] text-accent mb-2">{g.title}</div>
            {g.lines.map(([k, v]) => (
              <div key={k as string} className="flex items-baseline justify-between text-[10.5px] py-[3px] border-b border-edge/40 last:border-0">
                <span className="text-fg-dim">{k}</span>
                <span className="text-fg font-medium tabular-nums">{v}</span>
              </div>
            ))}
          </div>
        ))}
      </div>
    </Page>
  );
}

/* ── 12 Risks ──────────────────────────────────────────────── */
function Risks() {
  const risks = [
    { name: "PMF risk", level: "high", desc: "Anti-engagement niche may be smaller than mainstream productivity. Even 100k may be the whole market without crossover.", fix: "Reframe at M6: less 'anti-phone,' more 'phone you can finally trust.' Lead with the ambient capture story, not the restriction story." },
    { name: "Google Play takedown", level: "medium", desc: "Launchers with restriction overlays + accessibility services + device admin can flag review. A takedown wipes months of work.", fix: "Submit early under 'productivity' not 'wellness.' Get a Play Store contact via Indian dev relations. Mirror to F-Droid + APK Mirror as backup distribution." },
    { name: "Solo-founder burnout", level: "high", desc: "Content cadence + dev + community + press is 60+ hr/wk. Most indie launches die here at month 6–8.", fix: "Hire a part-time community manager at M6 if $5k MRR. Outsource short-form video edits to a freelancer. Make the build log itself the content." },
    { name: "Copycat from Forest / Opal", level: "medium", desc: "If we hit ~50k, a funded competitor copies the point economy + sad self mechanic.", fix: "Move fast on the social moat (groups + dual streaks). Open-source non-core mechanics to plant your flag. The point economy is a system not a feature — hard to copy partially." },
    { name: "Hardware competitor scales", level: "low-medium", desc: "Brick or Light Phone pivots to a software-only offer in the same niche.", fix: "Position Lazy Launcher as 'the hardware experience for $0.' Affiliate / bundle deal with Brick where they recommend our app for in-between hours." },
    { name: "Single channel dependence", level: "high in M4–M8", desc: "If Product Hunt fails AND HackerNews fails AND no TikTok hit by M8, the model is short by 30k+ users.", fix: "Hold the M11–M12 Wrapped event as the safety net. Pre-seed Wrapped content from M9. Be ready to do a paid push (~$5k) in M11 only if all three peaks failed." },
    { name: "Tone backlash", level: "medium", desc: "Sad Self mechanic + public surrender could be read as 'dark pattern' by tech-ethics writers.", fix: "Lead with ETHICS-001 (the transparency page) in every press piece. Get Center for Humane Technology or similar endorsement before press push. Voice toggle is opt-in by default." },
  ];
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">11</div>
      <H1>Risks &amp; Mitigations</H1>
      <P className="mt-4 max-w-[640px]">
        Honest list. None of these are abstract — they have all killed comparable
        indie launches. Each has a specific mitigation we own.
      </P>

      <div className="mt-5 space-y-3">
        {risks.map((r) => (
          <div key={r.name} className="bg-surface rounded-[14px] p-4">
            <div className="flex items-baseline justify-between">
              <div className="text-[14px] font-semibold text-fg tracking-heading">{r.name}</div>
              <div className={`text-[9px] label-caps ${r.level.includes("high") ? "text-danger" : r.level.includes("low") ? "text-success" : "text-accent"}`}>{r.level}</div>
            </div>
            <P className="!text-[11.5px] !leading-[17px] !mb-2 mt-1">{r.desc}</P>
            <P className="!text-[11.5px] !leading-[17px] !mb-0"><span className="label-caps text-[9px] text-fg-mute mr-2">mitigation</span>{r.fix}</P>
          </div>
        ))}
      </div>
    </Page>
  );
}

/* ── 13 Year Two ───────────────────────────────────────────── */
function YearTwo() {
  return (
    <Page>
      <div className="label-caps text-[11px] text-accent">12</div>
      <H1>Year-Two Setup</H1>
      <P className="mt-4 max-w-[640px]">
        Decisions made at the end of Y1 that shape Y2. Default is "don't fundraise" —
        the unit economics support it. Only deviate if month 12 numbers are above plan.
      </P>

      <H3>Hire when</H3>
      <ul className="text-[12px] text-fg-dim leading-[20px] list-disc pl-5 space-y-1 max-w-[640px]">
        <li><span className="text-fg">Community Manager</span> at $5k MRR (≈M6). Part-time, $1.5k/mo, handle Discord + ambassador program.</li>
        <li><span className="text-fg">Android dev #2</span> at $20k MRR (≈M11–M12). Full-time, $5–7k/mo, focus on Stage 3 social tier.</li>
        <li><span className="text-fg">Content / video editor</span> at $10k MRR (≈M9). Freelance, $1k/mo, edit short-form.</li>
        <li><span className="text-fg">First hire is never marketing.</span> Founder owns marketing through 250k users.</li>
      </ul>

      <H3>Fundraise when (or never)</H3>
      <P className="max-w-[640px]">
        Default no. If at M12 we are profitable, growing 15%+ MoM, and the social tier is
        live — keep bootstrapping. Raise only if (a) a hardware partner appears (Brick/Light/Unpluq
        acquisition or partnership), (b) a Play Store policy threat needs a legal war chest, or
        (c) clear B2B traction with therapists/teams. Skip pre-seed; go straight to seed at $5M+
        ARR signal.
      </P>

      <H3>Geographic expansion</H3>
      <P className="max-w-[640px]">
        India is the obvious Y2 expansion — your network, Hindi/Tamil localization,
        culturally-tuned Sad Self voice. Expect lower ARPU (~$8/yr vs $36 in US) but 5×
        the install volume. Decision gate: M14, after Y1 retention proves in.
      </P>

      <H3>The narrative for Y2</H3>
      <P className="max-w-[640px]">
        "We built a launcher 100,000 people couldn't quit. Now we're going to make it the
        operating system for their life." Y2 is when LIFE-001/002 (Bloomberg terminal +
        auto-insight engine) and SOCIAL-010 (Wrapped) carry the press cycle.
      </P>
    </Page>
  );
}

/* ── Doc ───────────────────────────────────────────────────── */
export default function MarketingDoc() {
  return (
    <div className="doc-root">
      <Cover />
      <ExecSummary />
      <Market />
      <Competitors />
      <Personas />
      <Positioning />
      <Math100k />
      <Channels />
      <ContentCommunity />
      <Roadmap12Mo />
      <Kpis />
      <Risks />
      <YearTwo />
    </div>
  );
}
