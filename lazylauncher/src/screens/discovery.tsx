import { Phone, StatusBar, HomeIndicator, TopBadge } from "../components/Phone";
import { HollowButton, PrimaryButton } from "../components/primitives";

/* 13.1 — Track Marketplace */
export function TrackMarketplace() {
  const tracks: [string, string, string, string][] = [
    ["#75Hard for Builders",     "Andrew Stark · ✓",    "12k clones",  "free"],
    ["Deep Work · 90-min blocks", "Cal Newport · ✓",    "6k clones",   "$5"],
    ["The 5am Club, but real",   "Ria Sanghvi · ✓",     "4.2k clones", "free"],
    ["Sober October x Built",    "Bo Jacobs · ✓",       "9.1k clones", "free"],
    ["No Reels · 60 days",       "Ali Maheri · ✓",      "11k clones",  "free"],
  ];
  return (
    <Phone label="13.1   Track Marketplace">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        TRACK MARKETPLACE · CREATOR-001
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[26px] font-bold text-fg tracking-heading">curated by verified builders</div>
        <div className="mt-1 text-[12px] text-fg-dim">clone a track, see the cloners' real data, follow their progress</div>
      </div>

      <div className="absolute inset-x-0 top-[220px] px-8 space-y-3">
        {tracks.map(([name, by, count, price]) => (
          <div key={name} className="bg-surface rounded-[14px] p-4">
            <div className="flex items-baseline justify-between">
              <div className="text-[14.5px] font-medium text-fg flex-1 min-w-0 truncate">{name}</div>
              <div className={`text-[11px] font-mono ${price === "free" ? "text-success" : "text-accent"}`}>{price}</div>
            </div>
            <div className="text-[10.5px] text-fg-dim mt-[2px]">{by}</div>
            <div className="mt-3 flex items-center justify-between">
              <div className="flex -space-x-1">
                {["AS","SD","JM","KP","LR"].map((i) => (
                  <div key={i} className="w-6 h-6 rounded-full bg-ink border border-edge text-[8.5px] text-fg-dim flex items-center justify-center font-medium">{i}</div>
                ))}
              </div>
              <div className="text-[11px] text-fg-mute font-mono">{count}</div>
            </div>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 bottom-[110px] px-8">
        <div className="bg-ink border border-edge rounded-[14px] p-4">
          <div className="label-caps text-[9px] text-fg-mute">PUBLISH YOUR TRACK</div>
          <div className="mt-1 text-[12px] text-fg-dim leading-[18px]">
            you need 365 days · 3 shipped projects · 3 vouches.
            revenue split 70/30 with the app.
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>browse all · 142 tracks</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 13.2 — Anti-Influencer Tab */
export function AntiInfluencer() {
  const creators: [string, string, string, string][] = [
    ["@calnewport",   "deep work writer", "1h 20m daily", "11 books"],
    ["@andrew_stark", "fitness coach",     "47m daily",     "0 reels"],
    ["@ria_sanghvi",  "designer",         "2h 03m daily",  "12 ships Q1"],
    ["@bo_jacobs",    "founder",          "3h 14m daily",  "ships 4/wk"],
  ];
  return (
    <Phone label="13.2   Anti-Influencer">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        ANTI-INFLUENCER · SOCIAL-018
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[26px] font-bold text-fg tracking-heading leading-tight">
          their actual<br />numbers.
        </div>
        <div className="mt-2 text-[12.5px] text-fg-dim leading-[19px]">
          verified creators must publish their real Lazy Launcher data.
          no edits. no filters. updated daily.
        </div>
      </div>

      <div className="absolute inset-x-0 top-[286px] px-8 space-y-3">
        {creators.map(([handle, role, screen, output]) => (
          <div key={handle} className="bg-surface rounded-[14px] p-4">
            <div className="flex items-baseline justify-between">
              <div>
                <div className="text-[14px] font-semibold text-fg">{handle}</div>
                <div className="text-[10.5px] text-fg-dim">{role}</div>
              </div>
              <div className="w-5 h-5 rounded-full border border-accent text-accent text-[10px] flex items-center justify-center">✓</div>
            </div>
            <div className="mt-3 grid grid-cols-2 gap-2">
              <div>
                <div className="text-[9px] text-fg-mute label-caps">SCREEN TIME</div>
                <div className="text-[14px] font-medium text-fg tabular-nums">{screen}</div>
              </div>
              <div>
                <div className="text-[9px] text-fg-mute label-caps">OUTPUT</div>
                <div className="text-[14px] font-medium text-success">{output}</div>
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8 space-y-2">
        <HollowButton>request to be verified</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 13.3 — Hashtag Tracks */
export function HashtagTracks() {
  const board: [string, number, string][] = [
    ["@ria_sanghvi", 14, "Ria · L7"],
    ["@you",          14, "erluxman · L4"],
    ["@bo_jacobs",    13, "Bo · L6"],
    ["@AC.choudhary", 12, "Anuj · L5"],
    ["@sara_d",       11, "Sara · L4"],
    ["@johndoe",      10, "John · L3"],
  ];
  return (
    <Phone label="13.3   Hashtag Tracks">
      <StatusBar />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        HASHTAG TRACK · SOCIAL-019
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[30px] font-bold text-fg tracking-heading">#buildinpublic</div>
        <div className="mt-1 text-[12px] text-fg-dim">2,847 builders · 14-day public commitment · ends Jun 06</div>
      </div>

      <div className="absolute inset-x-0 top-[220px] px-8">
        <div className="bg-surface rounded-[14px] p-4">
          <div className="label-caps text-[9px] text-fg-mute">THE RULES</div>
          <ul className="mt-1 text-[11.5px] text-fg-dim leading-[17px] list-disc pl-4">
            <li>ship one thing every day for 14 days, post the receipt publicly</li>
            <li>miss a day → out of the leaderboard</li>
            <li>top 10 get a verified badge for 90 days</li>
          </ul>
        </div>
      </div>

      <div className="absolute inset-x-0 top-[372px] px-8">
        <div className="flex items-baseline justify-between mb-2">
          <div className="label-caps text-[9.5px] text-fg-mute">LEADERBOARD · DAY 14</div>
          <div className="text-[10px] text-fg-mute">your rank · #2</div>
        </div>
        <div className="bg-surface rounded-[14px] divide-y divide-edge/40">
          {board.map(([handle, days, name], i) => (
            <div key={handle} className={`flex items-baseline gap-3 px-4 py-2 ${handle === "@you" ? "bg-accent/10" : ""}`}>
              <span className="font-mono text-[11px] text-fg-mute tabular-nums w-5">{i + 1}</span>
              <span className="text-[12.5px] font-medium text-fg flex-1 truncate">{name}</span>
              <span className="font-mono text-[11px] text-accent tabular-nums">{days}/14</span>
            </div>
          ))}
        </div>
      </div>

      <div className="absolute inset-x-0 top-[660px] px-8 grid grid-cols-2 gap-2">
        {[["#75hard", "1,204"], ["#sober_oct", "6,318"]].map(([t, c]) => (
          <div key={t} className="bg-surface rounded-[10px] p-3">
            <div className="text-[12.5px] font-medium text-fg">{t}</div>
            <div className="text-[10px] text-fg-mute">{c} active</div>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <PrimaryButton>commit to #buildinpublic · −10 pts</PrimaryButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}

/* 13.4 — Today's Top Builders */
export function TopBuilders() {
  const rows: [string, string, string, string][] = [
    ["1",  "Ria Sanghvi",   "shipped 3 components · 4h focus", "L7 · 87%"],
    ["2",  "Bo Jacobs",     "wrote 2,200 words · 0 unlocks",    "L6 · 81%"],
    ["3",  "erluxman",      "Q2 design draft · 2h block",       "L4 · 73%"],
    ["4",  "Anuj C.",       "shipped icon redesign · 1h",       "L5 · 78%"],
    ["5",  "Akari Hino",    "60 min lift · 3 todos",            "L5 · 75%"],
    ["6",  "Sara Davis",    "1500w essay draft",                "L4 · 70%"],
    ["7",  "Pawan Acharya", "12 commits · 30 min cardio",       "L4 · 68%"],
  ];
  return (
    <Phone label="13.4   Today's Top Builders">
      <StatusBar />
      <TopBadge level={4} form={73} points={244} />
      <div className="absolute inset-x-0 top-[88px] px-8 label-caps text-[10px] text-accent">
        DISCOVER · SOCIAL-017 · DATA-VERIFIED
      </div>
      <div className="absolute inset-x-0 top-[124px] px-8">
        <div className="text-[26px] font-bold text-fg tracking-heading">today, by output</div>
        <div className="mt-1 text-[12px] text-fg-dim">no follower counts · no posting · just what they made</div>
      </div>

      <div className="absolute inset-x-0 top-[208px] px-8 space-y-[6px]">
        {rows.map(([rank, name, what, badge]) => (
          <div key={name} className="bg-surface rounded-[12px] p-3 flex items-baseline gap-3">
            <span className="font-mono text-[12px] text-fg-mute w-5 tabular-nums">{rank}</span>
            <div className="flex-1 min-w-0">
              <div className="text-[12.5px] font-medium text-fg">{name}</div>
              <div className="text-[10.5px] text-fg-dim truncate">{what}</div>
            </div>
            <span className="font-mono text-[10px] text-accent">{badge}</span>
          </div>
        ))}
      </div>

      <div className="absolute inset-x-0 bottom-[110px] px-8">
        <div className="bg-ink border border-edge rounded-[14px] p-4">
          <div className="label-caps text-[9px] text-fg-mute">HOW THIS IS RANKED</div>
          <div className="mt-1 text-[11px] text-fg-dim leading-[17px]">
            output × form × consistency. you cannot buy rank. you cannot grind to the top
            with low form. you cannot post photos. there is nothing to like.
          </div>
        </div>
      </div>

      <div className="absolute inset-x-0 bottom-[40px] px-8">
        <HollowButton>see this week · this month · this year</HollowButton>
      </div>
      <HomeIndicator />
    </Phone>
  );
}
