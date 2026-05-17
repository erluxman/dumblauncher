# FocusLauncher — Productivity Features Spec

## Philosophy

The launcher IS the productivity system. Not a launcher that links to a productivity app — the home screen itself is your task board, project tracker, and behavior mirror. Phone becomes a TOOL, not a toy.

---

## P1: The 5-App Dock

### Rules

- Maximum 5 app slots visible on home screen
- **2 slots are FIXED**: Phone + Messages (communication is not distraction)
- **3 slots are user-configurable** — choose wisely
- Changing a slot has a 24-hour cooldown (no impulsive app shuffling)
- Apps NOT in the dock can still be launched but require intentional search + friction

### UX Flow

```
┌─────────────────────────────────────┐
│                                     │
│        [Project Progress]           │
│        [Behavior Indicator]         │
│                                     │
│        [Widgets Area - max 5]       │
│                                     │
│                                     │
│   ┌─────┐ ┌─────┐ ┌─���───┐         │
│   │App 1��� │App 2│ │App 3│         │
│   └─────┘ └──��──┘ └─────┘         │
│        ┌─────┐ ┌─────┐            │
│        │Phone│ │ Msg │            │
│        └─────┘ └─────┘            │
└─────────��───────────────────────────┘
```

### Hidden App Access

- Swipe up / long-press → minimal search bar (text only, no grid)
- Type app name → The Lobby / Cognitive Tax kicks in before launch
- No browseable app drawer. Must KNOW what you want. No "let me see what's there" browsing.

### Slot Configuration

- Settings → "Choose your 3 tools"
- Show usage stats for each candidate: "You used Spotify 12min/day avg. Calendar 4min/day."
- After selecting, 24hr lock. Can't swap again until tomorrow.
- Optional: Group can vote on which 3 apps are allowed (extreme mode)

---

## P2: Todo System with App-Unlock Rewards

### Core Concept

Todos aren't just a list — they're KEYS. Complete a task → unlock access to a specific app for a defined duration.

### Todo Structure

```
Todo {
  id: UUID
  title: String              // "Write 500 words of blog post"
  project: Project?          // optional project link
  unlocks_app: AppPackage?   // "com.instagram.android"
  unlock_duration: Minutes   // 15min of Instagram after completion
  due_date: Date?
  priority: low | medium | high
  status: pending | in_progress | completed | expired
  verification: none | photo | timer | location
  created_at: Timestamp
  completed_at: Timestamp?
}
```

### Verification Methods

| Method | How It Works | Use Case |
|--------|-------------|----------|
| None | Honor system. Mark complete. | Quick tasks, trusted items |
| Timer | Must have app open for X minutes (productive app) | "Spend 30min in Figma" |
| Photo | Take photo as proof | "Go for a walk", "Clean desk" |
| Location | GPS confirms you're at gym/office/park | "Go to gym", "Walk 2km" |
| Companion confirm | Group member verifies | High-stakes tasks |

### App Unlock Flow

```
User completes todo "Write 500 words"
  → Verification passes (photo of writing app showing word count)
  → 🎉 "Instagram unlocked for 15 minutes. Timer starts NOW."
  → App becomes available in dock (temporary 6th slot, highlighted)
  → Timer counts down visibly
  → At 0: app force-closes (overlay), slot disappears
  → Bonus: Unused time banks as credit (completed but didn't use = bonus streak points)
```

### Smart Suggestions

- AI suggests unlock durations based on patterns: "You usually spend 23min on Instagram. Set reward to 15min to stay in control?"
- If user always uses full unlock time: suggest shorter durations
- If user often doesn't use unlock: "You earned 15min of Twitter but didn't use it 3 times this week. Maybe you don't actually want it?"

### Rules

- Max 3 app-unlocking todos per day (scarcity creates value)
- Can't create todo JUST to unlock (minimum task complexity, or task must link to a project)
- Expired todos (past due date) don't grant unlocks
- Unlock earned = unlock earned. Even if you do 5 more todos, still only the original unlock duration. No "stacking" unlocks for same app.

---

## P3: Journal / Day Summary

### Auto-Generated Daily Summary

Every night (user-configurable time, default 9pm), app generates a journal entry based on the day's data.

### Data Sources for Journal

| Source | What It Reveals |
|--------|----------------|
| Screen time per app | Where attention went |
| Unlock count + times | Frequency and patterns |
| Todos completed | Productivity output |
| Project progress | Movement toward goals |
| Behavior indicator history | Emotional/focus trajectory |
| Apps blocked/resisted | Willpower moments |
| Context data (location, time) | Where you were, what you did |

### Journal Entry Structure

```
┌─────────────────────────────────────────┐
│ 📅 Friday, May 16, 2026                │
│                                         │
│ FOCUS SCORE: 7.2/10 (↑ from 6.8 avg)  │
│                                         │
│ ✅ Completed: 4/5 todos                │
│ 📊 Project "FocusLauncher": 12% → 15% │
│                                         │
│ 🏆 WIN: Resisted Instagram 6 times     │
│ ⚠️ PATTERN: 3pm slump (4 unlocks in   │
│    20 min, no intent)                   │
│                                         │
│ 📱 Screen: 2h 14m (↓ 34min from avg)  │
│ 🔓 Unlocks: 34 (↓ from 52 avg)        │
│                                         │
│ 💭 AI INSIGHT:                         │
│ "Best focus block was 9-11am. You      │
│ didn't touch phone for 2 hours.        │
│ Worst was 3-4pm — every day this week. │
│ Consider: phone in drawer after lunch?" │
│                                         │
│ [Add personal note...]                  │
│ [View full timeline]                    │
└─────────────────────���───────────────────┘
```

### User Input Layer

- After auto-summary generates, user can add personal reflection (optional)
- Prompt: "One sentence — how do you feel about today?"
- Or: tag the day with an emoji mood (😤😐🙂😊🔥)
- Notes are private, never shared with group

### Weekly / Monthly Rollups

- Weekly: Best day, worst day, trend direction, streak status
- Monthly: "Month in review" — biggest wins, persistent patterns, project velocity
- Exportable as PDF/markdown for personal records

### Behavior Pattern Detection in Journal

Over time, journal entries reveal:
- "Every Monday you spiral after standup meeting"
- "Your best days correlate with morning walks"
- "You haven't journaled a 'good' day in 2 weeks — check in?"
- "When you complete 3+ todos before noon, afternoon screen time drops 40%"

---

## P4: Widget System (Max 5)

### Rules

- Maximum 5 widgets on home screen
- Widgets are from OUR system only (no third-party widgets — those are distractions dressed as productivity)
- Each widget is compact, information-dense, actionable

### Available Widgets

| Widget | Size | What It Shows |
|--------|------|---------------|
| **Active Project** | Large | Current project name, progress bar, next task, deadline |
| **Today's Todos** | Medium | Checklist of today's tasks + unlock rewards |
| **Behavior Meter** | Small | Green/yellow/red indicator + "put phone down?" |
| **Streak Counter** | Small | Current streak days + all-time best |
| **Focus Timer** | Medium | Pomodoro-style timer, starts on tap |
| **Daily Score** | Small | Today's focus score (0-10) + trend arrow |
| **Clock + Remaining** | Small | Current time + "estimated productive hours left today" |
| **Quote / Intent** | Medium | Today's declared intention or rotating motivational quote |
| **Group Status** | Medium | Who's focused, who's struggling, your rank today |
| **Next Unlock** | Small | Closest todo to completion + what app it unlocks |

### Widget Placement

- User drags widgets into a flexible grid
- Widgets auto-resize to fill space (no gaps)
- Can be reordered but not removed without 24hr cooldown (same as apps — prevent impulsive UI changes)
- Default layout on fresh install: Active Project (large) + Today's Todos (medium) + Behavior Meter (small)

---

## P5: Projects & Progress Tracking

### Core Concept

Projects are the MAIN element on the home screen. Every time you pick up your phone, you see your project progress. Not app icons. Not notifications. YOUR WORK.

### Project Structure

```
Project {
  id: UUID
  name: String              // "Launch FocusLauncher MVP"
  description: String?
  color: Color              // Visual identification
  icon: Emoji               // 🚀
  progress: Float (0-100)   // Calculated from tasks
  milestones: [Milestone]
  tasks: [Todo]             // Todos linked to this project
  deadline: Date?
  created_at: Timestamp
  daily_goal: String?       // "Ship one feature per day"
  streak: Int               // Days with at least 1 task completed
}

Milestone {
  id: UUID
  name: String              // "Core launcher working"
  target_progress: Float    // At 25%
  reached: Boolean
  reached_at: Timestamp?
}
```

### Home Screen Project Display

```
┌─────────────────────────────────────────┐
│                                         │
│  🚀 FocusLauncher MVP                  │
│  ████████████░░░░░░░░ 58%              │
│  🔥 12-day streak                       │
│  Next: "Implement lobby screen"         │
│                                         │
│  ───────────────────────────────        │
│  📖 Learn Kotlin Coroutines             │
│  ████░░░░░░░░░░░░░░░░ 22%              │
│  ⚡ 3-day streak                        │
│  Next: "Chapter 5 exercises"            │
│                                         │
└────��────────────────────────────────────┘
```

### Rules

- Max 3 active projects (focus = saying no)
- Archived projects stay visible in a "completed" section (pride shelf)
- Progress auto-calculates from completed/total tasks
- If a project has 0 tasks completed in 7 days → warning: "Stale project. Commit or archive?"
- Projects with approaching deadlines get priority positioning

### Project → Todo → App Unlock Pipeline

```
Project: "Launch FocusLauncher MVP"
  → Todo: "Design lobby screen UI" (unlocks: Figma 45min)
  → Todo: "Write DeviceAdmin receiver" (unlocks: YouTube 10min)
  → Todo: "Test on 3 devices" (unlocks: Reddit 15min)
```

Everything connects. Projects have tasks. Tasks unlock apps. Phone becomes a tool FOR the project.

---

## P6: Behavior Indicator ("Doing Well" vs "Put Phone Down")

### Core Concept

A persistent, always-visible indicator that tells the user — in real-time — whether their current phone session is healthy or harmful. Like a speedometer for attention.

### Signal Inputs

| Signal | Weight | What It Measures |
|--------|--------|-----------------|
| Session duration | High | How long since last lock |
| Unlocks in last hour | High | Frequency of checking |
| App category in use | Medium | Productive vs distraction |
| Time of day | Medium | Late night = worse signal |
| Todos completed today | Medium | Have you earned this? |
| Rapid app switching | High | Anxiety/boredom pattern |
| Days into streak | Low | Established good behavior gives buffer |

### States

| State | Icon | Color | Meaning | Action |
|-------|------|-------|---------|--------|
| **Thriving** | 🌱 | Green | Focused, productive, earned time | None — positive reinforcement |
| **Neutral** | 😐 | Grey | Neither good nor bad. Short check-in. | Gentle: "Got what you need?" |
| **Drifting** | 🌊 | Yellow | Starting to slip. Multiple opens, no purpose. | "You're drifting. Close and flip phone?" |
| **Sinking** | 🔴 | Red | Deep in distraction. Long session, no productivity. | "Put phone face-down. Walk away. You'll thank yourself." |
| **Drowning** | ⚫ | Black | Multiple hours, late night, breaking patterns. | Full-screen intervention. Overlay. Group notification. |

### Display

- Always visible on home screen (small widget or status area)
- Color subtly tints the entire home screen background (green glow → red glow)
- Transition between states is smooth, not jarring (boiling frog for the user to notice)
- Tapping the indicator shows WHY: "You've unlocked 8 times in 40 minutes with no todos completed"

### "Put Phone Down" Suggestions

When state hits Yellow or worse, contextual suggestions appear:

- "Flip phone face-down for 10 minutes. Timer will track it." (proximity sensor detects face-down)
- "Your project 'FocusLauncher MVP' has a task ready. Open it instead?"
- "You said mornings are for deep work. It's 10:14am."
- "3 people in your group are focused right now. Join them?"
- "Walk to the kitchen. Get water. Come back in 2 minutes."

### Face-Down Detection

- Proximity sensor + accelerometer detect phone placed face-down on surface
- Timer tracks face-down duration
- After returning: "Nice. 14 minutes away. Focus score: +0.3"
- Streak of face-down breaks builds over time: "You've taken 23 intentional breaks this week"

### Recovery Mechanic

- State degrades smoothly but RECOVERS quickly (reward stopping)
- 5 minutes of no phone after Yellow → back to Neutral
- Completing a todo while in Yellow → instant jump to Green
- Positive actions weigh more than negative (encouragement > punishment)

---

## P7: Integration Between All Systems

### The Flywheel

```
Projects define what matters
  → Todos break projects into actions
    → App unlocks reward completing actions
      → Journal reflects on the day
        → Behavior indicator keeps you honest in real-time
          → All of it visible on home screen every single unlock
```

### Cross-System Rules

| If... | Then... |
|-------|---------|
| No active project exists | App nags to create one before allowing full use |
| All today's todos completed | Bonus: unlock any app 10min free (celebration mode) |
| Behavior indicator hits Red | Todo unlock rewards reduced by 50% (can't earn your way out of a spiral) |
| Project streak breaks | Home screen shows the break prominently for 24hrs |
| Journal not reflected on for 3 days | Gentle nudge: "You haven't checked in. 10 seconds?" |
| User face-down for 25min (full pomodoro) | Auto-complete a "take a break" todo if one exists |
| Group member completes a todo | Optional toast: "Marcus just shipped a task 💪" |

### Data Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   Projects   │────▶│    Todos     │────▶│ App Unlocks  │
│ (goals)      │     │ (actions)    │     │ (rewards)    │
└──────────────┘     └──────────────┘     └──────────────┘
       │                    │                     │
       ▼                    ▼                     ▼
┌──────────────────────────────────────────────────────────┐
│              Usage & Behavior Tracking                    │
└──────────────────────────────────────────────────────────┘
       │                    │                     │
       ▼                    ▼                     ▼
┌─���────────────┐     ┌──────────────┐     ┌─��────────────┐
│  Behavior    │     │   Journal    │     │   Streaks    │
│  Indicator   │     │  (summary)   │     │  & Scores    │
└──────────────┘     └──────────────┘     └──────────────┘
```

---

## P8: What the Home Screen Actually Looks Like

### Default State (Healthy User, Morning)

```
┌─────────────────────────────────────────┐
│  9:14 AM          🌱 Thriving    Day 14 │
│─────────────────────────────────────────│
│                                         │
│  🚀 FocusLauncher MVP          58%     │
│  ██████████░░░░░░░░░░                   │
│  Next: "Implement lobby screen"         │
│                                         │
│─────────────────────────────────────────│
│  TODAY                                  │
│  □ Design lobby screen UI    → Figma 45m│
│  □ Write tests for rule engine → YT 10m │
│  ☑ Morning walk (✓ 7:30am)             │
│                                         │
│─────────────────────────────────────────│
│  ⏱ Focus: 1h 42m  │  🔓 Unlocks: 3    │
│─────────────────────────────────────────│
│                                         │
│   [Figma]  [VSCode] [Spotify]           │
│        [Phone]  [Messages]              │
│                                         │
└──────────────────────────────���──────────┘
```

### Degraded State (User Slipping, Afternoon)

```
┌────────────────────────────────��────────┐
│  3:47 PM          🌊 Drifting    Day 14 │
│─────────────────────────────────────────│
│                                         │
│  🚀 FocusLauncher MVP          58%     │
│  ██████████░░░░░░░░░░  (no change)     │
│  ⚠️ No progress since 11am              │
│                                         │
│─────────────────────────────────────────│
│  TODAY                                  │
│  □ Design lobby screen UI    → Figma 45m│
│  □ Write tests for rule engine → YT 10m │
│  ☑ Morning walk (✓ 7:30am)             │
│                                         │
│  💡 "Complete one task to unlock apps"  │
│─────────────────────────────────────────│
│  ⏱ Focus: 1h 42m  │  🔓 Unlocks: 11   │
│            ↑ stopped growing     ↑ high │
│──��──────────────────────────────────────│
│                                         │
│   [Figma]  [VSCode] [Spotify]           │
│        [Phone]  [Messages]              │
│                                         │
│  🔻 "You've checked 8x in 40min.       │
│      Flip phone down. Walk away."       │
│                                         │
└──��────────────────────────────────���─────┘
```

### Critical State (Late Night Spiral)

```
┌─────────────────────────────────────────┐
│  1:23 AM          ⚫ Drowning    Day 14 │
│─────────────────────────────────────────│
│                                         │
│  ┌─────────────────────────────────┐    │
│  │                                 │    │
│  │   It's 1:23 AM.                │    │
│  │                                 │    │
│  │   You have 6 hours until your  │    │
│  │   alarm. Every minute here is  │    │
│  │   a minute of sleep lost.      │    │
│  │                                 │    │
│  │   Tomorrow's first task:       │    │
│  │   "Design lobby screen UI"     │    │
│  │                                 │    │
│  │   You'll need focus for that.  │    │
│  │                                 │    │
│  │   [Put phone down]             │    │
│  │   [I need 5 more minutes]      │    │
│  │                                 │    │
│  └──��──────────────────────────────┘    │
│                                         │
│        [Phone]  [Messages]              │
│   (only emergency apps visible)         │
│                                         ��
└─────────────────────────────────────────┘
```

---

## Technical Implementation Notes

### Storage (Local — Room DB)

```kotlin
@Entity
data class Project(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val emoji: String,
    val color: Int,
    val description: String? = null,
    val deadline: Long? = null,
    val dailyGoal: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val archived: Boolean = false
)

@Entity
data class Todo(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val projectId: String? = null,
    val unlocksApp: String? = null,       // package name
    val unlockDurationMin: Int = 15,
    val dueDate: Long? = null,
    val priority: Priority = Priority.MEDIUM,
    val status: TodoStatus = TodoStatus.PENDING,
    val verification: VerificationType = VerificationType.NONE,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

@Entity
data class JournalEntry(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val date: Long,                       // day timestamp
    val focusScore: Float,                // 0-10
    val screenTimeMin: Int,
    val unlockCount: Int,
    val todosCompleted: Int,
    val todosTotal: Int,
    val projectProgress: Map<String, Float>,  // projectId → delta
    val aiInsight: String,
    val userNote: String? = null,
    val mood: String? = null,             // emoji
    val generatedAt: Long = System.currentTimeMillis()
)

@Entity
data class BehaviorSnapshot(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val timestamp: Long,
    val state: BehaviorState,             // THRIVING, NEUTRAL, DRIFTING, SINKING, DROWNING
    val sessionDurationSec: Int,
    val unlocksLastHour: Int,
    val currentApp: String?,
    val triggerReason: String?            // "8 unlocks in 40min"
)
```

### Behavior Indicator Algorithm (Simplified)

```kotlin
fun calculateBehaviorState(
    sessionDurationMin: Int,
    unlocksLastHour: Int,
    currentAppCategory: AppCategory,
    todosCompletedToday: Int,
    timeOfDay: Int,  // hour 0-23
    rapidSwitches: Int  // app switches in last 5min
): BehaviorState {
    var score = 100  // start healthy

    // Session duration penalty
    score -= (sessionDurationMin * 2)  // -2 per minute

    // Unlock frequency penalty
    score -= (unlocksLastHour * 5)  // -5 per unlock

    // Distraction app penalty
    if (currentAppCategory == SOCIAL || currentAppCategory == ENTERTAINMENT) {
        score -= 20
    }

    // Rapid switching penalty (anxiety signal)
    score -= (rapidSwitches * 8)

    // Late night penalty
    if (timeOfDay in 23..24 || timeOfDay in 0..5) {
        score -= 25
    }

    // Todo completion bonus
    score += (todosCompletedToday * 10)

    return when {
        score >= 80 -> THRIVING
        score >= 60 -> NEUTRAL
        score >= 40 -> DRIFTING
        score >= 20 -> SINKING
        else -> DROWNING
    }
}
```

### Permissions Needed for Productivity Features

| Feature | Permission | Risk |
|---------|-----------|------|
| App usage tracking | PACKAGE_USAGE_STATS | User grants in Settings |
| Face-down detection | No permission (proximity sensor + accelerometer are permission-free) | ✅ |
| Location verification | ACCESS_FINE_LOCATION | Only if user uses location-verified todos |
| Camera (photo verification) | CAMERA | Only when verifying photo-proof todos |
| Calendar integration (context) | READ_CALENDAR | For time-aware behavior |
| Notification for journal | POST_NOTIFICATIONS | Standard |

---

## Open Questions for This Feature Set

1. **Should uncompleted todos from yesterday carry over or expire?**
2. **Can user create todos WITHOUT app unlock reward?** (pure productivity, no carrot)
3. **Should project progress be shareable with group?** (accountability + pride)
4. **Journal: fully auto-generated or require user input to save?**
5. **Behavior indicator: should Red/Black state FORCE restrictions or just suggest?**
6. **Widgets: allow third-party widgets (calendar, weather) or ONLY our widgets?**
