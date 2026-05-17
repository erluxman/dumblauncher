# FocusLauncher — Mantra Gate & Urgent Call System

---

## Part 1: Mantra Gate (Voice Unlock)

### Core Concept

Before the phone "opens" (before home screen becomes interactive), user must speak their personal mantra aloud — 5 times. The app listens, verifies each repetition via speech recognition, and only grants access after all 5 are completed successfully.

This is NOT a security feature. It's a **mindfulness ritual**. Forces the user to:
- Be PRESENT (can't do it while zoned out)
- State their INTENTION (mantra ties to their goals)
- Feel slightly ridiculous doing it in public (reduces casual unlocks)
- Create a 20-30 second buffer between impulse and access (most impulses die here)

### Mantra Setup

During onboarding or track creation:

```
┌─────────────────────────────────────────┐
│                                         │
│  Choose your unlock mantra.             │
│                                         │
│  You'll say this 5 times before your    │
│  phone opens. Make it meaningful.       │
│                                         │
│  Suggestions:                           │
│  • "I am focused. I am intentional."   │
│  • "My time is my life."               │
│  • "I choose creation over consumption"│
│  • "I will only do what I came for."   │
│  • Custom: [________________]           │
│                                         │
│  Say it now to test voice detection:    │
│  🎤 [Recording...]                      │
│                                         │
│  ✓ Detected! Your voice baseline saved.│
│                                         │
│  [Set as my mantra]                     │
│                                         │
└─────────────────────────────────────────┘
```

### Unlock Flow

```
User picks up phone / screen turns on
    │
    ▼
┌─────────────────────────────────────────┐
│                                         │
│          🔇 Phone is silent             │
│                                         │
│   "I am focused. I am intentional."    │
│                                         │
│          Say your mantra (1/5)          │
│                                         │
│          🎤 Listening...                │
│                                         │
│   ○ ○ ○ ○ ○  (progress dots)           │
│                                         │
└─────────────────────────────────────────┘
    │
    ▼ (user speaks)
    │
    ▼ Speech recognition validates
    │
    ├── ✓ Match → dot fills → "2/5"
    │
    ├── ✗ Didn't match → "Try again. Speak clearly."
    │       (no penalty, just re-listen)
    │
    ▼ After 5 successful repetitions (~20-30 seconds)
    │
    ▼
┌─────────────────────────────────────────┐
│                                         │
│   ● ● ● ● ●                            │
│                                         │
│   ✓ Mantra complete.                   │
│   "Remember why you're here."           │
│                                         │
│   [Phone unlocks to home screen]        │
│                                         │
└─────────────────────────────────────────┘
```

### Voice Detection Specs

**Technology:** Android SpeechRecognizer API (offline mode available)

**Validation rules:**
- Speech-to-text must match mantra text at ≥80% accuracy (allows natural variation)
- Must detect VOICE (not silence, not background noise, not a recording playback)
- Each repetition must be a distinct utterance (pause between them)
- Whisper mode allowed (for public settings) — reduced volume threshold
- Can't just play a recording of yourself (liveness detection via variance in each utterance)

**Anti-cheat:**
- Each of the 5 repetitions must have slightly different pitch/timing (natural speech varies)
- If 5 identical waveforms detected → reject as recording playback
- Optional: random word injection: "Say your mantra but replace the 3rd word with 'strong'" (once per day, prevents pure muscle memory)

### Mantra Intensity by Track Level

| Track Level | Mantra Requirement |
|-------------|-------------------|
| Level 1-2 | 1 repetition (gentle start) |
| Level 3-4 | 3 repetitions |
| Level 5-7 | 5 repetitions |
| Level 8-9 | 5 repetitions + state your #1 task for today |
| Level 10 | Optional (earned freedom) |

### When Mantra Is Required

Configurable. Options:
- **Every unlock** (hardcore — 30+ mantras per day for heavy users. Will CRUSH unlock frequency)
- **First unlock of the day only** (morning ritual)
- **After screen-off for 30+ minutes** (re-entry ritual)
- **Only during focus blocks** (targeted)
- **Only in extreme mode** (nuclear option)

**Recommendation:** Default to "after 30+ min screen-off." Frequent enough to matter, rare enough not to be maddening.

### Whisper Mode

For situations where speaking aloud isn't possible (meeting, library, public transport):

- User can enable "whisper mode" which lowers the mic sensitivity
- Must still vocalize (lip movements generate micro-sounds)
- Alternative: mouth the words while holding phone to ear (looks like a call)
- Or: type the mantra 5 times instead (slower, still intentional, but no voice)

### The Psychology

Why 5 times? Why not 1?
- **1 time:** Can be mumbled without thought. Becomes reflex.
- **3 times:** Starting to feel it. Brain engages by repetition 2.
- **5 times:** By repetition 4-5, you either genuinely FEEL the words or you put the phone down because the effort isn't worth the scroll. Both outcomes are wins.

---

## Part 2: Urgent Call System

### Core Concept

Phone is in silent mode + focus mode by default. NO notifications come through. But real emergencies exist. The system handles this with:

1. **Emergency Pass** — 5 free passes per week where phone breaks silence for ANY call
2. **VIP List** — Up to 10 numbers that ALWAYS ring through
3. **Hover Window** — When a call comes during focus, overlay shows "Urgent call from [name]" without fully breaking focus mode

### Silent Mode Rules

```
DEFAULT STATE (Focus Mode active):
├── All notifications: SILENCED + invisible
├── All calls: SILENCED
├── VIP list calls: RING THROUGH + hover window
├── Emergency pass calls: RING THROUGH (if passes remaining)
├── Repeated calls (2x in 3min from same number): RING THROUGH (true emergency signal)
└── Everything else: queued for batch delivery at next break
```

### VIP List

```
┌─────────────────────────────────────────┐
│                                         │
│  Emergency Contacts (max 10)            │
│  These people can always reach you.     │
│                                         │
│  1. 👩 Mom               +1-555-0101   │
│  2. 👨 Dad               +1-555-0102   │
│  3. 💑 Partner (Sarah)   +1-555-0103   │
│  4. 👶 School (Kids)     +1-555-0104   │
│  5. 🏢 Boss (Marcus)     +1-555-0105   │
│  6. ___empty___                         │
│  7. ___empty___                         │
│  8. ___empty___                         │
│  9. ___empty___                         │
│  10. ___empty___                        │
│                                         │
│  [+ Add contact]                        │
│                                         │
│  ℹ️ Changes locked for 24hrs after edit │
│     (prevents impulsive additions)      │
│                                         │
└─────────────────────────────────────────┘
```

**Rules:**
- Maximum 10 contacts (forces prioritization — who TRULY matters?)
- Adding/removing contacts has 24hr cooldown (can't add a friend's number just to receive their memes)
- VIP calls ring at FULL volume regardless of phone silent state
- VIP calls show hover window overlay (not full-screen takeover)
- VIP texts are delivered with 15-minute delay (not urgent enough to interrupt, but not lost)

### Emergency Pass System

```
┌─────────────────────────────────────────┐
│                                         │
│  🆘 Emergency Passes: 3/5 remaining    │
│      Resets every Monday                │
│                                         │
│  When a non-VIP call comes in during    │
│  focus mode, it uses 1 pass to ring.    │
│                                         │
│  Used this week:                        │
│  • Tue 2:14pm — Dentist (+1-555-9999) │
│  • Thu 5:30pm — Unknown (+1-555-7777) │
│                                         │
│  If passes are empty: ALL non-VIP       │
│  calls go to voicemail.                 │
│                                         │
└─────────────────────────────────────────┘
```

**Pass rules:**
- 5 passes per week (resets Monday)
- Each non-VIP call that rings through costs 1 pass
- User is NOT asked "use a pass?" — system auto-answers (the phone rings normally)
- When passes run out: all non-VIP calls → voicemail
- Voicemail transcription shows in next batch notification delivery
- Repeated call from same unknown number (2x in 3min) = rings through WITHOUT using a pass (true emergency heuristic)
- User can BUY extra passes by completing todos (1 todo = 1 extra pass). Productivity earns communication access.

### Hover Window (During Focus/Call)

When a VIP call comes in during focus mode:

```
┌─────────────────────────────────────────┐
│  [Current app / focus screen visible    │
│   underneath, dimmed]                   │
│                                         │
│   ┌───────────────────────────────┐     │
│   │  📞 Incoming: Mom             │     │
│   │                               │     │
│   │  VIP Contact • Always allowed │     │
│   │                               │     │
│   │  [Answer]    [Decline + Text] │     │
│   │                               │     │
│   │  Auto-decline in 30s          │     │
│   └───────────────────────────────┘     │
│                                         │
│  [Focus mode continues underneath]      │
│                                         │
└─────────────────────────────────────────┘
```

**Hover window behaviors:**
- Appears as floating overlay (SYSTEM_ALERT_WINDOW)
- Does NOT take over full screen (preserves context)
- Shows caller name, VIP badge, and two options
- "Decline + Text" sends pre-written auto-reply: "In focus mode. Will call back in [X] minutes."
- Auto-declines after 30 seconds if no action (sends auto-text)
- If answered: full phone UI activates. After call ends → "Return to focus?" prompt → mantra NOT required (you were interrupted, not distracted)
- Call duration tracked but NOT counted against focus time (legitimate interruption)

### Auto-Reply Templates

When declining a VIP call:

```
Default: "I'm in focus mode right now. I'll call back within 30 minutes."
Custom options:
- "In a meeting. Call you back at [next break time from schedule]."
- "Focusing on [current project name]. Free after [time]."
- "Can't talk now. Text me if urgent."
```

User configures these once. System auto-sends on decline.

### Non-VIP Call Handling (When Passes Available)

```
Non-VIP call comes in → pass available?
  │
  ├── YES → phone rings normally (uses 1 pass)
  │         hover window shows: "📞 [Name/Number] — Using emergency pass (2 remaining)"
  │
  └── NO → straight to voicemail
            notification queued for batch delivery
            if same number calls 2x in 3min → override, ring through (free, no pass used)
```

### Integration with Mantra Gate

| Scenario | Behavior |
|----------|----------|
| Phone locked + VIP call | Rings through. No mantra needed to answer a call. |
| Phone locked + emergency pass call | Rings through. No mantra needed. |
| After call ends, phone was locked | Mantra required to access home screen (call didn't "unlock" phone for general use) |
| During focus block + VIP call | Hover window. After call, focus block continues (time paused during call). |
| Mantra in progress + call | Mantra interrupted. Call takes priority. After call, mantra resumes from where left off. |

---

## Part 3: Combined Flow (Full System)

```
Phone screen turns on (any reason)
    │
    ├── Incoming VIP call? → Ring + hover window (skip everything)
    ├── Incoming pass call? → Ring normally (skip mantra)
    │
    ▼ (not a call)
    │
    ▼ Mantra Gate
    │   "Say your mantra (1/5)..."
    │   🎤 Listening...
    │   ... 5 successful repetitions ...
    │
    ▼ Phone unlocks to home screen
    │   (Behavior indicator visible, projects shown, todos ready)
    │
    ▼ User navigates
    │
    ├── Opens productive app → no friction, timer starts, Flow State protected
    │
    ├── Opens distraction app → Lobby wait → Cognitive Tax → Access granted
    │       │
    │       ▼ Sad Self notifications begin after 2 min
    │       ▼ Behavior indicator degrades
    │       ▼ After time limit → overlay lockout
    │
    └── Puts phone down → Applause / "Nice, 3 min session" / behavior improves

CALLS DURING ANY STATE:
- VIP → always hover, always ring
- Emergency pass → ring if passes available
- Repeat caller (2x/3min) → ring free (assumed emergency)
- All others → voicemail, queued for batch
```

---

## Technical Implementation

### Mantra Recognition

```kotlin
class MantraGateManager(
    private val speechRecognizer: SpeechRecognizer,
    private val mantraText: String,
    private val requiredRepetitions: Int = 5
) {
    private var completedReps = 0
    private val utteranceTimestamps = mutableListOf<Long>()
    private val utteranceEnergies = mutableListOf<Float>()  // anti-replay
    
    fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, 
                     RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)  // works offline!
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
        speechRecognizer.startListening(intent)
    }
    
    fun onResult(result: String, energy: Float): MantraResult {
        val similarity = calculateSimilarity(result.lowercase(), mantraText.lowercase())
        
        if (similarity < 0.80f) {
            return MantraResult.NO_MATCH  // didn't say the right words
        }
        
        // Anti-replay: check this utterance differs from previous
        if (utteranceEnergies.isNotEmpty()) {
            val variance = abs(energy - utteranceEnergies.last())
            if (variance < 0.01f) {
                return MantraResult.SUSPECTED_REPLAY
            }
        }
        
        utteranceTimestamps.add(System.currentTimeMillis())
        utteranceEnergies.add(energy)
        completedReps++
        
        return if (completedReps >= requiredRepetitions) {
            MantraResult.COMPLETE
        } else {
            MantraResult.ACCEPTED(completedReps, requiredRepetitions)
        }
    }
    
    private fun calculateSimilarity(spoken: String, target: String): Float {
        // Levenshtein distance normalized to 0-1
        val distance = levenshtein(spoken, target)
        return 1f - (distance.toFloat() / maxOf(spoken.length, target.length))
    }
}
```

### Call Handling

```kotlin
class UrgentCallManager(
    private val vipContacts: List<String>,  // phone numbers
    private val weeklyPassLimit: Int = 5,
    private val passesUsedThisWeek: Int
) {
    fun handleIncomingCall(callerNumber: String, callLog: List<CallRecord>): CallAction {
        // VIP always rings
        if (callerNumber in vipContacts) {
            return CallAction.RING_WITH_HOVER(isVip = true)
        }
        
        // Repeated caller (2x in 3min) = emergency
        val recentFromSame = callLog.filter { 
            it.number == callerNumber && 
            (now() - it.timestamp) < 3.minutes 
        }
        if (recentFromSame.size >= 1) {  // this is the 2nd call
            return CallAction.RING_WITH_HOVER(isEmergency = true)
        }
        
        // Use emergency pass if available
        if (passesUsedThisWeek < weeklyPassLimit) {
            usePass()
            return CallAction.RING_WITH_HOVER(passUsed = true)
        }
        
        // No passes left → voicemail
        return CallAction.VOICEMAIL(queueNotification = true)
    }
}

sealed class CallAction {
    data class RING_WITH_HOVER(
        val isVip: Boolean = false,
        val isEmergency: Boolean = false,
        val passUsed: Boolean = false
    ) : CallAction()
    
    data class VOICEMAIL(
        val queueNotification: Boolean = true
    ) : CallAction()
}
```

### Permissions Required

| Feature | Permission | Notes |
|---------|-----------|-------|
| Mantra voice recognition | RECORD_AUDIO | Required. Offline recognition available. |
| Call detection | READ_PHONE_STATE | Detects incoming calls |
| Call handling / hover | SYSTEM_ALERT_WINDOW + READ_CONTACTS | For overlay + caller name |
| Auto-reply SMS | SEND_SMS | For "decline + text" feature |
| DND mode control | ACCESS_NOTIFICATION_POLICY | To enforce silence |
| Voicemail access | READ_VOICEMAIL (optional) | For transcription |

---

## Open Questions

1. **Mantra language:** Support multiple languages? What about non-Latin scripts?
2. **Mantra change frequency:** Can user change mantra? Cooldown? (Prevent making it "a" to bypass)
3. **Accessibility:** Mute/deaf users can't do voice. Alternative: type mantra 5 times? Gesture pattern?
4. **Kids calling from school:** Add "any call from [location]" as VIP rule? Not just numbers?
5. **Pass economy:** Can unused passes roll over? Or use-it-or-lose-it weekly?
6. **Group calls:** Video calls (WhatsApp, FaceTime) — same VIP/pass rules? Technically different from phone calls.
