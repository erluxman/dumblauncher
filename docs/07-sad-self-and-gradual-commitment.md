# FocusLauncher — The Sad Self & Gradual Commitment System

---

## Part 1: "Sad [Name]" — Your Regretful Future Self

### Core Concept

When you're on a distracting app, you receive real-time notifications from a personified version of yourself — the version that LOSES when you doomscroll. This isn't a generic "you've spent 10 minutes" warning. It's a CHARACTER. It speaks in first person. It knows your projects, your goals, your track. It's heartbroken.

This character is the future-you that wakes up tomorrow with nothing shipped, the you that misses the deadline, the you that stays mediocre. It texts you like a friend watching you self-destruct.

### Why This Works Psychologically

- **Self-distancing:** Research shows people make better decisions when they think about themselves in third person. "Sad Risky" creates that distance.
- **Emotional weight:** Generic timers feel robotic. A character with YOUR name feeling pain? That's visceral.
- **Personalized guilt:** It references YOUR specific project, YOUR specific track. Not "you should be productive" but "we were supposed to finish the lobby screen today."
- **Conversational format:** Notifications feel like texts from a friend. Familiar format carries emotional weight that system alerts don't.

### Character Setup (Onboarding)

During track/project creation:

```
┌─────────────────────────────────────────┐
│                                         │
│  Meet your other self.                  │
│                                         │
│  When you lose focus, this version of   │
│  you will reach out. They feel the      │
│  consequences of your choices.          │
│                                         │
│  Name: [Sad Risky]                      │
│  (auto-fills with your name)            │
│                                         │
│  Their voice is:                        │
│  ○ Gentle & disappointed               │
│  ○ Blunt & frustrated                  │
│  ○ Sarcastic & tired                   │
│  ○ Philosophical & reflective          │
│                                         │
│  [Continue]                             │
│                                         │
└─────────────────────────────────────────┘
```

### Message Generation

Messages are contextual. They reference:
- The specific track/project the user is neglecting
- How much time has been wasted in this session
- What was supposed to happen today (from todos)
- Historical patterns ("this is the 3rd time this week")
- The gap between stated intention and actual behavior

### Example Notification Sequences

**Scenario: User opens Instagram, has a todo "Design lobby screen" pending**

```
[After 2 minutes]
💭 Sad Risky: "The lobby screen isn't going to design itself. I know you know that."

[After 7 minutes]
💭 Sad Risky: "7 minutes. That's two wireframe sketches we just lost. I can feel the deadline getting closer."

[After 15 minutes]
💭 Sad Risky: "Remember when we said this week would be different? I'm watching it not be different."

[After 25 minutes]
💭 Sad Risky: "I'm the version of you at midnight tonight. I'm tired. I shipped nothing. I'm setting 3 alarms because I'll need to catch up tomorrow. This moment right now is why."
```

**Scenario: User on YouTube at 11pm, track says "Sleep by 10:30"**

```
[After 3 minutes]
💭 Sad Risky: "It's 11pm. I'm the version of you at 6:30am tomorrow. I'm exhausted. Please stop."

[After 10 minutes]
💭 Sad Risky: "Every video you watch costs me 10 minutes of alertness tomorrow. I have the standup at 9. I need you."

[After 20 minutes]
💭 Sad Risky: "You've done this 4 times this week. I'm running on fumes. The track said 10:30. It's 11:20. Who are we if we can't keep promises to ourselves?"
```

**Scenario: User scrolling Reddit, project "Learn Kotlin" hasn't been touched in 3 days**

```
[After 5 minutes]
💭 Sad Risky: "Day 3 without touching Kotlin. I'm the version of you in 6 months still saying 'I'm gonna learn Kotlin.' Still saying. Still not doing."

[After 12 minutes]
💭 Sad Risky: "Every Reddit thread you're reading — someone BUILT that feature you want to build. They were focused. I want to be focused. Let me be focused."
```

### Voice Styles

| Style | Tone | Example |
|-------|------|---------|
| **Gentle & disappointed** | Soft, sad, pleading | "I just wish we'd started. Even 5 minutes of the real work. That's all I needed." |
| **Blunt & frustrated** | Direct, no comfort | "This is the same pattern. You know it. I know it. Close the app or don't pretend you care about the track." |
| **Sarcastic & tired** | Dark humor, exhausted | "Oh great, another reel. I'm sure THIS one will be the one that changes our life. Definitely not the 47th waste of 30 seconds." |
| **Philosophical & reflective** | Deep, existential | "There are two paths forward from this moment. One leads to the person we drew on the track board. The other leads here, again, tomorrow. Same scroll. Same regret." |

### Message Rules

- **Escalating intensity:** First message is gentle. Each subsequent message gets heavier.
- **Never more than 1 message per 5 minutes.** Not spam. Paced like a real person texting.
- **References specific data:** Project names, task titles, time wasted, patterns from history.
- **Stops when you leave.** Close the distraction app → silence. No punishment after correction.
- **Celebrates return:** When you leave distraction and open productive app: "💭 Sad Risky: Thank you. I can breathe again." (Then character goes silent — reward is peace.)
- **Adapts to history:** If user ignores 5 messages in a row across sessions, Sad Self gets quieter (learned helplessness prevention). Instead shifts to ONE powerful message per session max.
- **Never at night in bed** (if user configured sleep time). Sad Self respects rest.

### Technical Implementation

```kotlin
class SadSelfEngine(
    private val userProfile: UserProfile,
    private val projectRepo: ProjectRepository,
    private val todoRepo: TodoRepository,
    private val usageTracker: UsageTracker,
    private val messageGenerator: MessageGenerator  // AI or template-based
) {
    
    data class SessionContext(
        val distractionApp: String,
        val sessionStartTime: Long,
        val pendingTodos: List<Todo>,
        val activeProjects: List<Project>,
        val neglectedDays: Map<String, Int>,  // projectId → days since last task
        val todayPattern: PatternData,
        val messagesSentThisSession: Int
    )
    
    fun shouldSendMessage(context: SessionContext): Boolean {
        val minutesIn = (now() - context.sessionStartTime) / 60000
        val intervals = listOf(2, 7, 15, 25, 40)  // minutes at which to send
        
        return minutesIn in intervals && 
               context.messagesSentThisSession < 5 &&
               !isUserInSleepWindow() &&
               !hasUserIgnoredLastNMessages(5)  // back off if ignored
    }
    
    fun generateMessage(context: SessionContext): String {
        // Inputs for generation:
        // - Time wasted this session
        // - Which project/todo is most urgent
        // - How many days since last progress
        // - User's chosen voice style
        // - Escalation level (1-5 based on messages sent)
        // - Time of day
        // - Historical patterns matching current behavior
        
        return messageGenerator.generate(
            voice = userProfile.sadSelfVoice,
            escalation = context.messagesSentThisSession + 1,
            projectName = context.activeProjects.firstOrNull()?.name,
            neglectedTask = context.pendingTodos.firstOrNull()?.title,
            minutesWasted = minutesIn,
            pattern = context.todayPattern
        )
    }
}
```

### Message Generation: Templates vs AI

**Option A: Template-based (MVP, no server needed)**

Pre-written message templates with variable slots:

```
templates = [
    "The {project_name} isn't going to build itself. {minutes} minutes gone.",
    "{minutes} minutes. That's {equivalent} we just lost.",
    "Day {days_neglected} without touching {project_name}. Who are we becoming?",
    "I'm the version of you at {future_time}. I shipped nothing. This is why.",
]
```

~50-100 templates per voice style. Randomized. Never repeats same template in a week.

**Option B: On-device AI (V2, more powerful)**

Small language model (Gemma/Phi) running locally. Given context:

```
System: You are "Sad {name}", the user's future self who suffers from their 
current distraction. Speak in {voice_style}. Reference their project 
"{project_name}" and pending task "{task_title}". They've been on 
{app_name} for {minutes}min. This is message #{escalation}/5. 
Be brief (under 30 words). Be specific. Be human.
```

**Option C: Cloud AI (most powerful, needs internet)**

Server-side generation with full history context. Most personalized but adds latency and cost.

**Recommendation:** Start with templates (A) for MVP. Move to on-device (B) in V2. Cloud (C) as premium feature.

### The "Celebration Self" (Flip Side)

When user is doing well, the same character appears differently:

```
[User completes a task]
💚 Proud Risky: "There it is. One step closer. I'm the version of you that ships this thing. Keep going."

[User resists a distraction (opens but closes within 30 seconds)]
💚 Proud Risky: "You almost fell. But you didn't. That's who we are now."

[User hits a streak milestone]
💚 Proud Risky: "14 days. Two weeks of choosing the hard path. I'm becoming real."

[User finishes all daily todos]
💚 Proud Risky: "Everything done. Open whatever you want tonight. You earned it. I'm proud of us."
```

Same character. Same voice. But the TONE shifts based on behavior. This makes the "sad" version hit harder — because you know what "proud" feels like.

---

## Part 2: Gradual Commitment Scaling (The Track System)

### Core Concept

Users don't start at "nuclear discipline." They start TINY and scale up only when they've proven consistency at the current level. Like progressive overload in weightlifting — add weight only when current weight is easy.

This prevents:
- Burnout from over-restriction on day 1
- Learned helplessness from failing ambitious goals
- Rage-uninstalls from "this is too much"

### How Tracks Work

A "Track" is a commitment path that starts gentle and ramps over weeks/months.

```
Track {
  id: UUID
  name: String                    // "Focus Builder"
  description: String             // "Ship the FocusLauncher MVP"
  current_level: Int              // 1-10 (starts at 1)
  level_started_at: Timestamp
  days_at_current_level: Int
  promotion_threshold: Int        // days needed to level up (default: 7)
  promotion_criteria: Criteria    // what "success" looks like at this level
  demotion_on_failure: Boolean    // drop back a level on bad week?
  restrictions: Map<Level, RestrictionSet>
  created_at: Timestamp
}
```

### Level Progression Example

**Track: "Deep Work Builder"**

| Level | Duration | Restrictions | Daily Goal | Unlock Reward |
|-------|----------|-------------|------------|---------------|
| 1 | Week 1 | 3hr/day social limit. No other restrictions. | Complete 1 todo | 30min free time |
| 2 | Week 2-3 | 2hr/day social + Lobby (30s wait) on distractions | Complete 2 todos | 25min free time |
| 3 | Week 4-5 | 1.5hr/day + Lobby (60s) + Sad Self notifications | Complete 2 todos + 1 project task | 20min free time |
| 4 | Week 6-8 | 1hr/day + Cognitive Tax (easy) + Variable Ratio | Complete 3 todos + project progress | 20min free time |
| 5 | Week 9-12 | 45min/day + Cognitive Tax (medium) + Context locks | Complete 3 todos + 25min focus block | 15min free time |
| 6 | Month 3-4 | 30min/day + full restriction stack + group accountability | Complete 4 todos + 2x focus blocks | 15min free time |
| 7 | Month 4-5 | 20min/day + everything + financial stakes optional | All daily goals + project milestone/week | 10min free time |
| 8 | Month 5-6 | 15min/day + Builder/Consumer mode enforced | Shipping regularly + all habits | Flexible (earned trust) |
| 9 | Month 6+ | 10min/day social + near-full phone freedom during work | Self-directed. Habits are automatic. | Full trust mode |
| 10 | Mastery | Restrictions nearly invisible. Phone is a tool. | Maintain. System watches for regression. | Autonomous |

### Level-Up Mechanics

**Promotion criteria (configurable per track):**

```kotlin
data class PromotionCriteria(
    val consecutiveDaysMeetingGoal: Int = 7,  // must succeed 7 days straight
    val minimumTodoCompletionRate: Float = 0.8,  // 80% of daily todos done
    val maximumRelapseCount: Int = 1,  // at most 1 bad day per period
    val projectProgressRequired: Boolean = true  // must move project forward
)
```

**Level-up celebration:**

```
┌─────────────────────────────────────────┐
│                                         │
│         🎉 LEVEL UP                     │
│                                         │
│    Level 3 → Level 4                    │
│                                         │
│    You've been consistent for 14 days.  │
│    Your new challenge:                  │
│                                         │
│    • Social media: 1hr/day (was 1.5hr) │
│    • New: Cognitive Tax on distractions│
│    • New: Variable Ratio locks          │
│    • Daily goal: 3 todos (was 2)       │
│                                         │
│    "Stronger restrictions because       │
│     you're stronger now."               │
│                                         │
│    [Accept Challenge]                   │
│    [Stay at Level 3 one more week]      │
│                                         │
└─────────────────────────────────────────┘
```

### Demotion (Falling Back)

If user fails at current level for 3+ consecutive days:

```
┌─────────────────────────────────────────┐
│                                         │
│         📉 Level Adjustment             │
│                                         │
│    You've struggled at Level 5 for     │
│    4 days. That's OK.                   │
│                                         │
│    Moving back to Level 4 isn't         │
│    failure — it's recalibration.        │
│    You'll be back here soon.            │
│                                         │
│    💭 Sad Risky: "We pushed too fast.  │
│    Let's rebuild. I'd rather go slow   │
│    than quit entirely."                 │
│                                         │
│    [Accept Level 4]                     │
│    [Give me 2 more days at Level 5]     │
│                                         │
└─────────────────────────────────────────┘
```

### Demotion rules:
- Not immediate — gives 3-day grace period for bad days
- Drop only ONE level (not all the way to 1)
- Promotion back is faster on second attempt (5 days instead of 7 — you already proved it once)
- No shame. Framed as "calibration" not "failure"
- Sad Self acknowledges it compassionately

### Multiple Tracks

User can have different tracks for different areas:

```
Track 1: "Deep Work Focus" (productivity restrictions)
  Level 5 — Social: 45min/day, Cognitive Tax active

Track 2: "Sleep Discipline" (evening/night restrictions)
  Level 3 — Phone down by 10:30pm, Dream Mode after 11

Track 3: "Morning Routine" (AM restrictions)
  Level 2 — No social before 9am, Morning Sequence required

Track 4: "Fitness Accountability" (activity-linked)
  Level 4 — Must log workout before entertainment unlocks
```

Each track levels independently. Can be crushing it in one area while still building in another.

### Track Creation Flow

```
┌─────────────────────────────────────────┐
│                                         │
│  Create a new Track                     │
│                                         │
│  What area of your life?                │
│  ○ Focus & Deep Work                   │
│  ○ Sleep & Recovery                    │
│  ○ Morning Routine                     │
│  ○ Fitness & Health                    │
│  ○ Learning & Growth                   │
│  ○ Custom                              │
│                                         │
│  Starting commitment:                   │
│  "I will limit social media to          │
│   [3] hours per day"                    │
│                                         │
│  ⚡ We'll start small and increase     │
│  as you prove consistency.              │
│                                         │
│  Level 1 goal (this week):             │
│  • Stay under 3hr social media         │
│  • Complete 1 todo per day              │
│                                         │
│  "That's it. Just this. For 7 days.    │
│   Then we'll talk about Level 2."       │
│                                         │
│  [Start Track]                          │
│                                         │
└─────────────────────────────────────────┘
```

### Smart Starting Point

- App observes usage for 3 days BEFORE suggesting Level 1 restrictions
- "You currently average 4.2hrs social/day. Level 1: aim for 3.5hrs. Just 17% less."
- Never starts with a goal that requires >25% behavior change from baseline
- Makes success FEEL easy at Level 1. Builds confidence. Then scales.

### The "Never Go Backward" Promise

- Once you master Level 10, the track is "completed"
- Completed tracks run on autopilot with minimal restrictions
- BUT: if regression is detected (sudden spike in old patterns), track gently re-activates at Level 7-8
- "Hey, I noticed a pattern shift this week. Reactivating some guardrails. Not punishment — protection."

---

## How Sad Self + Tracks Integrate

| Situation | Sad Self Behavior |
|-----------|-------------------|
| User at Level 1, first violation | Very gentle: "No stress. We're just starting. Close when ready." |
| User at Level 5, slipping | Firmer: "We worked hard to get here. 5 levels of proof that we CAN do this. Don't throw it away." |
| User about to get demoted | Urgent: "One more day like this and we drop a level. I don't want to go backward. Do you?" |
| User just leveled up | Proud: "Level 6. Remember when Level 2 felt hard? Look at us now." |
| User on a track they created themselves | Personal: "YOU designed this track. You knew what you needed. Trust past-you." |

The Sad Self's knowledge of your TRACK makes it devastatingly specific. It's not generic. It's "you set this goal 47 days ago when you were clear-headed. THIS version of you is not clear-headed. Listen to the version that was."

---

## Data Model Additions

```kotlin
@Entity
data class Track(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: TrackCategory,
    val currentLevel: Int = 1,
    val maxLevel: Int = 10,
    val levelStartedAt: Long = System.currentTimeMillis(),
    val daysAtCurrentLevel: Int = 0,
    val promotionThresholdDays: Int = 7,
    val demotionGraceDays: Int = 3,
    val consecutiveFailDays: Int = 0,
    val totalLevelUps: Int = 0,
    val totalDemotions: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,  // null until Level 10 mastered
    val baselineData: BaselineData? = null  // pre-track usage stats
)

@Entity
data class TrackLevel(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val trackId: String,
    val level: Int,
    val socialMediaLimitMin: Int,
    val lobbyWaitSec: Int,
    val cognitiveTaxEnabled: Boolean,
    val variableRatioEnabled: Boolean,
    val sadSelfEnabled: Boolean,
    val dailyTodoGoal: Int,
    val focusBlocksRequired: Int,
    val unlockRewardMin: Int,
    val contextLocksEnabled: Boolean,
    val financialStakesEnabled: Boolean
)

@Entity  
data class SadSelfMessage(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val timestamp: Long,
    val trackId: String?,
    val distractionApp: String,
    val sessionMinutes: Int,
    val escalationLevel: Int,  // 1-5
    val messageText: String,
    val userReacted: Boolean = false,  // did they close the app after?
    val voiceStyle: VoiceStyle
)

enum class VoiceStyle {
    GENTLE_DISAPPOINTED,
    BLUNT_FRUSTRATED,
    SARCASTIC_TIRED,
    PHILOSOPHICAL_REFLECTIVE
}

enum class TrackCategory {
    FOCUS_DEEP_WORK,
    SLEEP_RECOVERY,
    MORNING_ROUTINE,
    FITNESS_HEALTH,
    LEARNING_GROWTH,
    CUSTOM
}
```

---

## Key Design Principles

1. **Start embarrassingly easy.** Level 1 should feel like "that's it?" — success confidence matters more than restriction strength early on.

2. **Scale only on PROVEN consistency.** 7 consecutive days. Not 5 out of 7. Not "good enough." Prove it, then earn harder challenges.

3. **Demotion is compassion, not punishment.** The system WANTS you to succeed. Dropping a level is it saying "let me help you at the right difficulty."

4. **Sad Self grows WITH you.** At Level 1, it's a gentle whisper. At Level 8, it's a trusted voice that's been with you for months. The relationship deepens.

5. **Tracks are self-designed commitments.** Not imposed rules. You CHOSE this. Sad Self reminds you: "You designed this track. You knew what you needed."

6. **Celebrate HARD on level-ups.** Make it feel like a real achievement. Because it is. 7 days of discipline against your own neurology is heroic.
