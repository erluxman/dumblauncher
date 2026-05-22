package com.erluxman.focuslauncher.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userDataStore by preferencesDataStore(name = "focus_prefs")

object PrefKeys {
    val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
    val WHY_HERE = stringPreferencesKey("why_here")
    val DAILY_SCREEN_TIME_TARGET_MIN = intPreferencesKey("daily_target_min")
    val DISTRACTION_PACKAGES = stringSetPreferencesKey("distraction_packages")
    val UNINSTALL_REQUESTED_AT = longPreferencesKey("uninstall_requested_at")
    val VIP_CONTACTS = stringSetPreferencesKey("vip_contacts")

    val ONE_THING_TEXT = stringPreferencesKey("one_thing_text")
    val ONE_THING_DATE = stringPreferencesKey("one_thing_date")  // yyyy-MM-dd

    val STREAK_DAYS = intPreferencesKey("streak_days")
    val STREAK_BEST = intPreferencesKey("streak_best")
    val STREAK_LAST_CHECK_DATE = stringPreferencesKey("streak_last_check_date")

    val MANTRA_PHRASE = stringPreferencesKey("mantra_phrase")
    val FOCUS_SESSIONS_TODAY = intPreferencesKey("focus_sessions_today")
    val FOCUS_SESSIONS_DATE = stringPreferencesKey("focus_sessions_date")

    val TIME_BANK_TOTAL_MIN = intPreferencesKey("time_bank_total_min")
    val TIME_BANK_LAST_DATE = stringPreferencesKey("time_bank_last_date")

    val PHANTOM_BUZZ_COUNT = intPreferencesKey("phantom_buzz_count")
    val PHANTOM_BUZZ_DATE = stringPreferencesKey("phantom_buzz_date")

    val MORNING_DONE_DATE = stringPreferencesKey("morning_done_date")
    val MORNING_DONE_STEPS = stringSetPreferencesKey("morning_done_steps")

    val SHUTDOWN_DONE_DATE = stringPreferencesKey("shutdown_done_date")
    val SHUTDOWN_DONE_STEPS = stringSetPreferencesKey("shutdown_done_steps")

    val IDENTITY_BUILDER_COUNT = intPreferencesKey("identity_builder_count")
    val IDENTITY_CONSUMER_COUNT = intPreferencesKey("identity_consumer_count")
    val IDENTITY_DATE = stringPreferencesKey("identity_date")

    val TIME_DEBT_MIN = intPreferencesKey("time_debt_min")
    val TIME_DEBT_DATE = stringPreferencesKey("time_debt_date")  // date the debt reflects

    val MOOD_PINGS = stringSetPreferencesKey("mood_pings")  // "yyyy-MM-dd HH:mm|emoji|note"

    val NUCLEAR_PASSPHRASE = stringPreferencesKey("nuclear_passphrase")
    val FUTURE_SELF_LETTER = stringPreferencesKey("future_self_letter")

    val LOCKED_TODAY_PACKAGES = stringSetPreferencesKey("locked_today_packages")
    val LOCKED_TODAY_DATE = stringPreferencesKey("locked_today_date")

    val UNLOCK_COUNTS = stringSetPreferencesKey("unlock_counts")  // "yyyy-MM-dd|pkg|count"

    val GRACE_DAYS = stringSetPreferencesKey("grace_days")  // declared yyyy-MM-dd entries
    val STREAK_FREEZES = intPreferencesKey("streak_freezes")
    val AFTER_FALL_DUE = stringPreferencesKey("after_fall_due_date")  // ritual due on this iso date
    val AFTER_FALL_STEPS = stringSetPreferencesKey("after_fall_steps")

    val HOURLY_RATE_USD = intPreferencesKey("hourly_rate_usd")
    val USER_AGE = intPreferencesKey("user_age")

    val APP_TOMBSTONES = stringSetPreferencesKey("app_tombstones")  // "label|date_added"
    val FUTURE_LETTERS = stringSetPreferencesKey("future_letters")  // "id|deliverDate|delivered(0/1)|text"

    val DOMAIN_STREAKS = stringSetPreferencesKey("domain_streaks")  // "domain|days|best|lastDate"

    val FOCUS_POINTS = intPreferencesKey("focus_points")
    val ENERGY_ZONES = stringSetPreferencesKey("energy_zones")  // "0-4|HIGH"

    val TRACK_LEVEL = intPreferencesKey("track_level")
    val TRACK_POINTS = intPreferencesKey("track_points")
    val TRACK_MISSES = intPreferencesKey("track_misses")
    val TRACK_RECALIBRATED = booleanPreferencesKey("track_recalibrated")
    val BUILDER_MODE = booleanPreferencesKey("builder_mode")

    val SAD_VOICE = stringPreferencesKey("sad_voice")  // STERN / COMPASSIONATE / WITTY / DRILL
    val SAD_DISMISS_COUNT = intPreferencesKey("sad_dismiss_count")
    val SAD_SUPPRESSED_UNTIL = longPreferencesKey("sad_suppressed_until")
    val CELEBRATION_LAST_STREAK = intPreferencesKey("celebration_last_streak")
    val CELEBRATION_LAST_LEVEL = intPreferencesKey("celebration_last_level")

    val NOURISHING_PACKAGES = stringSetPreferencesKey("nourishing_packages")
    val DAY_SUMMARY_DATES = stringSetPreferencesKey("day_summary_dates")  // ISO dates already summarized

    val BASELINE_SAMPLES = stringSetPreferencesKey("baseline_samples")  // "iso|minutes"
    val BASELINE_APPLIED = booleanPreferencesKey("baseline_applied")

    /** Per-domain track snapshots. Format: "domain|level|points|miss|date". */
    val DOMAIN_TRACKS = stringSetPreferencesKey("domain_tracks")

    val EMERGENCY_PASSES = intPreferencesKey("emergency_passes")
    val EMERGENCY_WEEK = stringPreferencesKey("emergency_week")  // yyyy-Www

    // Transparency toggles (ETHICS-001): each technique opt-out-able
    val TECH_LOBBY = booleanPreferencesKey("tech_lobby")
    val TECH_DIMMING = booleanPreferencesKey("tech_dimming")
    val TECH_BEHAVIOR_INDICATOR = booleanPreferencesKey("tech_behavior_indicator")
    val TECH_HIDDEN_DRAWER = booleanPreferencesKey("tech_hidden_drawer")
    val TECH_VARIABLE_RATIO = booleanPreferencesKey("tech_variable_ratio")
    val TECH_ESCALATING = booleanPreferencesKey("tech_escalating")
    val TECH_DREAM = booleanPreferencesKey("tech_dream")
    val TECH_ANCHORING = booleanPreferencesKey("tech_anchoring")

    /** LIFE-009 + LIFE-012 mortality widgets. Opt-in. */
    val MORTALITY_WIDGETS_OPT_IN = booleanPreferencesKey("mortality_widgets_opt_in")

    /** SUB-003 caffeine log. Each entry: "epochMs|mg". Trimmed to last 24h on write. */
    val CAFFEINE_DOSES = stringSetPreferencesKey("caffeine_doses")

    /** SLEEP-003 sleep window guardrails. 24h ints, 0..23. */
    val SLEEP_CUTOFF_HOUR = intPreferencesKey("sleep_cutoff_hour")
    val SLEEP_WAKE_HOUR = intPreferencesKey("sleep_wake_hour")

    /** SUB-001 hangover calculus drink log. Each entry: "epochMs|units (e.g. 1.0)". */
    val DRINK_LOG = stringSetPreferencesKey("drink_log")

    /** MIND-001 meditation log. Each entry: "iso|minutes|technique". */
    val MEDITATION_LOG = stringSetPreferencesKey("meditation_log")

    /** PROD-009 idea parking lot. Each entry: "epochMs|text". */
    val IDEA_PARKING = stringSetPreferencesKey("idea_parking")

    /** READ-001 reading log. Each entry: "iso|minutes". */
    val READING_LOG = stringSetPreferencesKey("reading_log")

    /** FIT-002 workout log. Each entry: "iso|minutes|kind". */
    val WORKOUT_LOG = stringSetPreferencesKey("workout_log")

    /** INTEG-008 commit log. Each entry: "iso|commitCount". */
    val COMMIT_LOG = stringSetPreferencesKey("commit_log")

    /** FIT-003 personal records. Each entry: "isoFirstSet|label|value|unit". */
    val PR_WALL = stringSetPreferencesKey("pr_wall")

    /** LOC-006 travel atlas. Each entry: "year|location". */
    val TRAVEL_ATLAS = stringSetPreferencesKey("travel_atlas")

    /** FIN-003 subscription log. Each entry: "name|monthlyUsd". */
    val SUBSCRIPTIONS = stringSetPreferencesKey("subscriptions")

    /** FIN-002 + FIN-004 manual money snapshot. All whole USD. */
    val MONEY_INCOME = intPreferencesKey("money_income_usd")
    val MONEY_EXPENSE = intPreferencesKey("money_expense_usd")
    val MONEY_ASSETS = intPreferencesKey("money_assets_usd")
    val MONEY_LIABILITIES = intPreferencesKey("money_liabilities_usd")

    /** READ-002 raw text highlights. */
    val HIGHLIGHTS = stringSetPreferencesKey("highlights")

    /** LIFECYCLE-001 onboarding completion timestamp (ms). */
    val ONBOARDING_COMPLETED_AT = longPreferencesKey("onboarding_completed_at")

    /** SOCIAL-013 anti-bio: what the user has chosen NOT to do. */
    val ANTI_BIO = stringPreferencesKey("anti_bio")
}

class UserPrefs(private val context: Context) {
    private val store get() = context.userDataStore

    val onboardingComplete: Flow<Boolean> =
        store.data.map { it[PrefKeys.ONBOARDING_COMPLETE] ?: false }

    val whyHere: Flow<String> =
        store.data.map { it[PrefKeys.WHY_HERE].orEmpty() }

    val dailyTargetMin: Flow<Int> =
        store.data.map { it[PrefKeys.DAILY_SCREEN_TIME_TARGET_MIN] ?: 180 }

    val distractionPackages: Flow<Set<String>> =
        store.data.map { it[PrefKeys.DISTRACTION_PACKAGES] ?: DEFAULT_DISTRACTIONS }

    val uninstallRequestedAt: Flow<Long?> =
        store.data.map { it[PrefKeys.UNINSTALL_REQUESTED_AT] }

    val vipContacts: Flow<Set<String>> =
        store.data.map { it[PrefKeys.VIP_CONTACTS] ?: emptySet() }

    val oneThingText: Flow<String> =
        store.data.map { it[PrefKeys.ONE_THING_TEXT].orEmpty() }

    val oneThingDate: Flow<String> =
        store.data.map { it[PrefKeys.ONE_THING_DATE].orEmpty() }

    val streakDays: Flow<Int> = store.data.map { it[PrefKeys.STREAK_DAYS] ?: 0 }
    val streakBest: Flow<Int> = store.data.map { it[PrefKeys.STREAK_BEST] ?: 0 }
    val streakLastCheckDate: Flow<String> =
        store.data.map { it[PrefKeys.STREAK_LAST_CHECK_DATE].orEmpty() }

    val mantraPhrase: Flow<String> = store.data.map { it[PrefKeys.MANTRA_PHRASE].orEmpty() }
    val focusSessionsToday: Flow<Int> = store.data.map { it[PrefKeys.FOCUS_SESSIONS_TODAY] ?: 0 }
    val focusSessionsDate: Flow<String> = store.data.map { it[PrefKeys.FOCUS_SESSIONS_DATE].orEmpty() }

    val timeBankTotalMin: Flow<Int> = store.data.map { it[PrefKeys.TIME_BANK_TOTAL_MIN] ?: 0 }
    val timeBankLastDate: Flow<String> = store.data.map { it[PrefKeys.TIME_BANK_LAST_DATE].orEmpty() }

    val phantomBuzzCount: Flow<Int> = store.data.map { it[PrefKeys.PHANTOM_BUZZ_COUNT] ?: 0 }
    val phantomBuzzDate: Flow<String> = store.data.map { it[PrefKeys.PHANTOM_BUZZ_DATE].orEmpty() }

    val morningDoneDate: Flow<String> = store.data.map { it[PrefKeys.MORNING_DONE_DATE].orEmpty() }
    val morningDoneSteps: Flow<Set<String>> = store.data.map { it[PrefKeys.MORNING_DONE_STEPS] ?: emptySet() }

    val shutdownDoneDate: Flow<String> = store.data.map { it[PrefKeys.SHUTDOWN_DONE_DATE].orEmpty() }
    val shutdownDoneSteps: Flow<Set<String>> = store.data.map { it[PrefKeys.SHUTDOWN_DONE_STEPS] ?: emptySet() }

    val identityBuilderCount: Flow<Int> = store.data.map { it[PrefKeys.IDENTITY_BUILDER_COUNT] ?: 0 }
    val identityConsumerCount: Flow<Int> = store.data.map { it[PrefKeys.IDENTITY_CONSUMER_COUNT] ?: 0 }
    val identityDate: Flow<String> = store.data.map { it[PrefKeys.IDENTITY_DATE].orEmpty() }

    val timeDebtMin: Flow<Int> = store.data.map { it[PrefKeys.TIME_DEBT_MIN] ?: 0 }
    val timeDebtDate: Flow<String> = store.data.map { it[PrefKeys.TIME_DEBT_DATE].orEmpty() }

    val moodPings: Flow<Set<String>> = store.data.map { it[PrefKeys.MOOD_PINGS] ?: emptySet() }

    val nuclearPassphrase: Flow<String> = store.data.map { it[PrefKeys.NUCLEAR_PASSPHRASE].orEmpty() }
    val futureSelfLetter: Flow<String> = store.data.map { it[PrefKeys.FUTURE_SELF_LETTER].orEmpty() }

    val lockedTodayPackages: Flow<Set<String>> = store.data.map { it[PrefKeys.LOCKED_TODAY_PACKAGES] ?: emptySet() }
    val lockedTodayDate: Flow<String> = store.data.map { it[PrefKeys.LOCKED_TODAY_DATE].orEmpty() }

    val unlockCounts: Flow<Set<String>> = store.data.map { it[PrefKeys.UNLOCK_COUNTS] ?: emptySet() }

    val graceDays: Flow<Set<String>> = store.data.map { it[PrefKeys.GRACE_DAYS] ?: emptySet() }
    val streakFreezes: Flow<Int> = store.data.map { it[PrefKeys.STREAK_FREEZES] ?: 0 }
    val afterFallDue: Flow<String> = store.data.map { it[PrefKeys.AFTER_FALL_DUE].orEmpty() }
    val afterFallSteps: Flow<Set<String>> = store.data.map { it[PrefKeys.AFTER_FALL_STEPS] ?: emptySet() }

    val hourlyRateUsd: Flow<Int> = store.data.map { it[PrefKeys.HOURLY_RATE_USD] ?: 0 }
    val userAge: Flow<Int> = store.data.map { it[PrefKeys.USER_AGE] ?: 0 }

    val appTombstones: Flow<Set<String>> = store.data.map { it[PrefKeys.APP_TOMBSTONES] ?: emptySet() }
    val futureLetters: Flow<Set<String>> = store.data.map { it[PrefKeys.FUTURE_LETTERS] ?: emptySet() }
    val domainStreaks: Flow<Set<String>> = store.data.map { it[PrefKeys.DOMAIN_STREAKS] ?: emptySet() }

    val focusPoints: Flow<Int> = store.data.map { it[PrefKeys.FOCUS_POINTS] ?: 0 }
    val energyZones: Flow<Set<String>> = store.data.map { it[PrefKeys.ENERGY_ZONES] ?: emptySet() }

    val trackLevel: Flow<Int> = store.data.map { it[PrefKeys.TRACK_LEVEL] ?: 1 }
    val trackPoints: Flow<Int> = store.data.map { it[PrefKeys.TRACK_POINTS] ?: 0 }
    val trackMisses: Flow<Int> = store.data.map { it[PrefKeys.TRACK_MISSES] ?: 0 }
    val trackRecalibrated: Flow<Boolean> = store.data.map { it[PrefKeys.TRACK_RECALIBRATED] ?: false }
    val builderMode: Flow<Boolean> = store.data.map { it[PrefKeys.BUILDER_MODE] ?: false }

    val sadVoice: Flow<String> = store.data.map { it[PrefKeys.SAD_VOICE] ?: "STERN" }
    val sadDismissCount: Flow<Int> = store.data.map { it[PrefKeys.SAD_DISMISS_COUNT] ?: 0 }
    val sadSuppressedUntil: Flow<Long> = store.data.map { it[PrefKeys.SAD_SUPPRESSED_UNTIL] ?: 0L }
    val celebrationLastStreak: Flow<Int> = store.data.map { it[PrefKeys.CELEBRATION_LAST_STREAK] ?: 0 }
    val celebrationLastLevel: Flow<Int> = store.data.map { it[PrefKeys.CELEBRATION_LAST_LEVEL] ?: 0 }

    val nourishingPackages: Flow<Set<String>> = store.data.map { it[PrefKeys.NOURISHING_PACKAGES] ?: emptySet() }
    val daySummaryDates: Flow<Set<String>> = store.data.map { it[PrefKeys.DAY_SUMMARY_DATES] ?: emptySet() }

    val baselineSamples: Flow<Set<String>> = store.data.map { it[PrefKeys.BASELINE_SAMPLES] ?: emptySet() }
    val baselineApplied: Flow<Boolean> = store.data.map { it[PrefKeys.BASELINE_APPLIED] ?: false }

    val domainTracks: Flow<Set<String>> = store.data.map { it[PrefKeys.DOMAIN_TRACKS] ?: emptySet() }
    val emergencyPasses: Flow<Int> = store.data.map { it[PrefKeys.EMERGENCY_PASSES] ?: 0 }
    val emergencyWeek: Flow<String> = store.data.map { it[PrefKeys.EMERGENCY_WEEK].orEmpty() }

    /** Default OFF: mortality widgets are opt-in only. */
    val mortalityWidgetsOptIn: Flow<Boolean> =
        store.data.map { it[PrefKeys.MORTALITY_WIDGETS_OPT_IN] ?: false }

    suspend fun setMortalityWidgetsOptIn(value: Boolean) {
        store.edit { it[PrefKeys.MORTALITY_WIDGETS_OPT_IN] = value }
    }

    val caffeineDoses: Flow<Set<String>> =
        store.data.map { it[PrefKeys.CAFFEINE_DOSES] ?: emptySet() }

    /** Append a dose and prune anything older than 24h. */
    suspend fun logCaffeine(mg: Int, nowMs: Long = System.currentTimeMillis()) {
        if (mg <= 0) return
        store.edit {
            val cutoff = nowMs - 24L * 3_600_000L
            val current = it[PrefKeys.CAFFEINE_DOSES] ?: emptySet()
            val pruned = current.filter { entry ->
                entry.substringBefore("|").toLongOrNull()?.let { ts -> ts >= cutoff } ?: false
            }.toSet()
            it[PrefKeys.CAFFEINE_DOSES] = pruned + "$nowMs|$mg"
        }
    }

    suspend fun clearCaffeineLog() {
        store.edit { it.remove(PrefKeys.CAFFEINE_DOSES) }
    }

    val sleepCutoffHour: Flow<Int> =
        store.data.map {
            (it[PrefKeys.SLEEP_CUTOFF_HOUR] ?: com.erluxman.focuslauncher.service.lobby.SleepWindow.DEFAULT_CUTOFF_HOUR)
                .coerceIn(0, 23)
        }

    val sleepWakeHour: Flow<Int> =
        store.data.map {
            (it[PrefKeys.SLEEP_WAKE_HOUR] ?: com.erluxman.focuslauncher.service.lobby.SleepWindow.DEFAULT_WAKE_HOUR)
                .coerceIn(0, 23)
        }

    suspend fun setSleepCutoffHour(h: Int) {
        store.edit { it[PrefKeys.SLEEP_CUTOFF_HOUR] = h.coerceIn(0, 23) }
    }

    suspend fun setSleepWakeHour(h: Int) {
        store.edit { it[PrefKeys.SLEEP_WAKE_HOUR] = h.coerceIn(0, 23) }
    }

    val drinkLog: Flow<Set<String>> =
        store.data.map { it[PrefKeys.DRINK_LOG] ?: emptySet() }

    suspend fun logDrink(units: Double, nowMs: Long = System.currentTimeMillis()) {
        if (units <= 0.0) return
        store.edit {
            val cutoff = nowMs - 24L * 3_600_000L
            val pruned = (it[PrefKeys.DRINK_LOG] ?: emptySet()).filter { entry ->
                entry.substringBefore("|").toLongOrNull()?.let { ts -> ts >= cutoff } ?: false
            }.toSet()
            it[PrefKeys.DRINK_LOG] = pruned + "$nowMs|$units"
        }
    }

    suspend fun clearDrinkLog() {
        store.edit { it.remove(PrefKeys.DRINK_LOG) }
    }

    val meditationLog: Flow<Set<String>> =
        store.data.map { it[PrefKeys.MEDITATION_LOG] ?: emptySet() }

    suspend fun logMeditation(isoDate: String, minutes: Int, technique: String) {
        if (minutes <= 0) return
        val safeTech = technique.replace("|", " ").ifBlank { "Breath" }
        store.edit {
            val current = it[PrefKeys.MEDITATION_LOG] ?: emptySet()
            it[PrefKeys.MEDITATION_LOG] = current + "$isoDate|$minutes|$safeTech"
        }
    }

    suspend fun clearMeditationLog() {
        store.edit { it.remove(PrefKeys.MEDITATION_LOG) }
    }

    val ideaParking: Flow<Set<String>> =
        store.data.map { it[PrefKeys.IDEA_PARKING] ?: emptySet() }

    suspend fun addParkedIdea(text: String, nowMs: Long = System.currentTimeMillis()) {
        val safe = text.trim().replace("|", " ")
        if (safe.isEmpty()) return
        store.edit {
            val current = it[PrefKeys.IDEA_PARKING] ?: emptySet()
            it[PrefKeys.IDEA_PARKING] = current + "$nowMs|$safe"
        }
    }

    suspend fun removeParkedIdea(entry: String) {
        store.edit {
            val current = it[PrefKeys.IDEA_PARKING] ?: emptySet()
            it[PrefKeys.IDEA_PARKING] = current - entry
        }
    }

    suspend fun clearParkedIdeas() {
        store.edit { it.remove(PrefKeys.IDEA_PARKING) }
    }

    val readingLog: Flow<Set<String>> =
        store.data.map { it[PrefKeys.READING_LOG] ?: emptySet() }

    suspend fun logReading(isoDate: String, minutes: Int) {
        if (minutes <= 0) return
        store.edit {
            val current = it[PrefKeys.READING_LOG] ?: emptySet()
            it[PrefKeys.READING_LOG] = current + "$isoDate|$minutes"
        }
    }

    suspend fun clearReadingLog() {
        store.edit { it.remove(PrefKeys.READING_LOG) }
    }

    val workoutLog: Flow<Set<String>> =
        store.data.map { it[PrefKeys.WORKOUT_LOG] ?: emptySet() }

    suspend fun logWorkout(isoDate: String, minutes: Int, kind: String) {
        if (minutes <= 0) return
        val safe = kind.replace("|", " ").ifBlank { "Strength" }
        store.edit {
            val current = it[PrefKeys.WORKOUT_LOG] ?: emptySet()
            it[PrefKeys.WORKOUT_LOG] = current + "$isoDate|$minutes|$safe"
        }
    }

    suspend fun clearWorkoutLog() {
        store.edit { it.remove(PrefKeys.WORKOUT_LOG) }
    }

    val commitLog: Flow<Set<String>> =
        store.data.map { it[PrefKeys.COMMIT_LOG] ?: emptySet() }

    suspend fun addCommits(isoDate: String, count: Int) {
        if (count <= 0) return
        store.edit {
            val current = it[PrefKeys.COMMIT_LOG] ?: emptySet()
            val existing = current.firstOrNull { e -> e.startsWith("$isoDate|") }
            val prev = existing?.substringAfter("|")?.toIntOrNull() ?: 0
            val without = current.filterNot { e -> e == existing }.toSet()
            it[PrefKeys.COMMIT_LOG] = without + "$isoDate|${prev + count}"
        }
    }

    suspend fun clearCommitLog() {
        store.edit { it.remove(PrefKeys.COMMIT_LOG) }
    }

    val prWall: Flow<Set<String>> =
        store.data.map { it[PrefKeys.PR_WALL] ?: emptySet() }

    suspend fun addPersonalRecord(isoDate: String, label: String, value: String, unit: String) {
        val safeLabel = label.replace("|", " ").trim().ifBlank { return }
        val safeValue = value.replace("|", " ").trim().ifBlank { return }
        val safeUnit = unit.replace("|", " ").trim()
        store.edit {
            val current = it[PrefKeys.PR_WALL] ?: emptySet()
            it[PrefKeys.PR_WALL] = current + "$isoDate|$safeLabel|$safeValue|$safeUnit"
        }
    }

    suspend fun removePersonalRecord(entry: String) {
        store.edit {
            val current = it[PrefKeys.PR_WALL] ?: emptySet()
            it[PrefKeys.PR_WALL] = current - entry
        }
    }

    val travelAtlas: Flow<Set<String>> =
        store.data.map { it[PrefKeys.TRAVEL_ATLAS] ?: emptySet() }

    suspend fun addTravel(year: Int, location: String) {
        val safeLoc = location.replace("|", " ").trim().ifBlank { return }
        if (year !in 1900..2100) return
        store.edit {
            val current = it[PrefKeys.TRAVEL_ATLAS] ?: emptySet()
            it[PrefKeys.TRAVEL_ATLAS] = current + "$year|$safeLoc"
        }
    }

    suspend fun removeTravel(entry: String) {
        store.edit {
            val current = it[PrefKeys.TRAVEL_ATLAS] ?: emptySet()
            it[PrefKeys.TRAVEL_ATLAS] = current - entry
        }
    }

    val subscriptions: Flow<Set<String>> =
        store.data.map { it[PrefKeys.SUBSCRIPTIONS] ?: emptySet() }

    suspend fun addSubscription(name: String, monthlyUsd: Double) {
        val safe = name.replace("|", " ").trim().ifBlank { return }
        if (monthlyUsd <= 0) return
        store.edit {
            val current = it[PrefKeys.SUBSCRIPTIONS] ?: emptySet()
            // Replace existing by name.
            val others = current.filterNot { e -> e.substringBefore("|") == safe }.toSet()
            it[PrefKeys.SUBSCRIPTIONS] = others + "$safe|$monthlyUsd"
        }
    }

    suspend fun removeSubscription(entry: String) {
        store.edit {
            val current = it[PrefKeys.SUBSCRIPTIONS] ?: emptySet()
            it[PrefKeys.SUBSCRIPTIONS] = current - entry
        }
    }

    val moneyIncome: Flow<Int> = store.data.map { it[PrefKeys.MONEY_INCOME] ?: 0 }
    val moneyExpense: Flow<Int> = store.data.map { it[PrefKeys.MONEY_EXPENSE] ?: 0 }
    val moneyAssets: Flow<Int> = store.data.map { it[PrefKeys.MONEY_ASSETS] ?: 0 }
    val moneyLiabilities: Flow<Int> = store.data.map { it[PrefKeys.MONEY_LIABILITIES] ?: 0 }

    suspend fun setMoneyIncome(v: Int) { store.edit { it[PrefKeys.MONEY_INCOME] = v.coerceAtLeast(0) } }
    suspend fun setMoneyExpense(v: Int) { store.edit { it[PrefKeys.MONEY_EXPENSE] = v.coerceAtLeast(0) } }
    suspend fun setMoneyAssets(v: Int) { store.edit { it[PrefKeys.MONEY_ASSETS] = v.coerceAtLeast(0) } }
    suspend fun setMoneyLiabilities(v: Int) { store.edit { it[PrefKeys.MONEY_LIABILITIES] = v.coerceAtLeast(0) } }

    val highlights: Flow<Set<String>> =
        store.data.map { it[PrefKeys.HIGHLIGHTS] ?: emptySet() }

    suspend fun addHighlight(text: String) {
        val safe = text.trim()
        if (safe.isEmpty()) return
        store.edit {
            val current = it[PrefKeys.HIGHLIGHTS] ?: emptySet()
            it[PrefKeys.HIGHLIGHTS] = current + safe
        }
    }

    suspend fun removeHighlight(text: String) {
        store.edit {
            val current = it[PrefKeys.HIGHLIGHTS] ?: emptySet()
            it[PrefKeys.HIGHLIGHTS] = current - text
        }
    }

    fun technique(key: Preferences.Key<Boolean>): Flow<Boolean> =
        store.data.map { it[key] ?: true }

    suspend fun setOnboardingComplete(value: Boolean) {
        store.edit {
            it[PrefKeys.ONBOARDING_COMPLETE] = value
            if (value && it[PrefKeys.ONBOARDING_COMPLETED_AT] == null) {
                it[PrefKeys.ONBOARDING_COMPLETED_AT] = System.currentTimeMillis()
            }
        }
    }

    val onboardingCompletedAt: Flow<Long> =
        store.data.map { it[PrefKeys.ONBOARDING_COMPLETED_AT] ?: 0L }

    val antiBio: Flow<String> =
        store.data.map { it[PrefKeys.ANTI_BIO].orEmpty() }

    suspend fun setAntiBio(text: String) {
        store.edit { it[PrefKeys.ANTI_BIO] = text.take(280) }
    }

    suspend fun setWhyHere(text: String) {
        store.edit { it[PrefKeys.WHY_HERE] = text }
    }

    suspend fun setDailyTargetMin(min: Int) {
        store.edit { it[PrefKeys.DAILY_SCREEN_TIME_TARGET_MIN] = min }
    }

    suspend fun setDistractionPackages(packages: Set<String>) {
        store.edit { it[PrefKeys.DISTRACTION_PACKAGES] = packages }
    }

    suspend fun setTechnique(key: Preferences.Key<Boolean>, value: Boolean) {
        store.edit { it[key] = value }
    }

    suspend fun startUninstallRequest(nowMs: Long = System.currentTimeMillis()) {
        store.edit { it[PrefKeys.UNINSTALL_REQUESTED_AT] = nowMs }
    }

    suspend fun cancelUninstallRequest() {
        store.edit { it.remove(PrefKeys.UNINSTALL_REQUESTED_AT) }
    }

    suspend fun setVipContacts(contacts: Set<String>) {
        store.edit { it[PrefKeys.VIP_CONTACTS] = contacts }
    }

    suspend fun setOneThing(text: String, todayIso: String) {
        store.edit {
            it[PrefKeys.ONE_THING_TEXT] = text
            it[PrefKeys.ONE_THING_DATE] = todayIso
        }
    }

    suspend fun clearOneThing() {
        store.edit {
            it.remove(PrefKeys.ONE_THING_TEXT)
            it.remove(PrefKeys.ONE_THING_DATE)
        }
    }

    suspend fun applyStreak(newDays: Int, newBest: Int, todayIso: String) {
        store.edit {
            it[PrefKeys.STREAK_DAYS] = newDays
            it[PrefKeys.STREAK_BEST] = newBest
            it[PrefKeys.STREAK_LAST_CHECK_DATE] = todayIso
        }
    }

    suspend fun setMantraPhrase(phrase: String) {
        store.edit { it[PrefKeys.MANTRA_PHRASE] = phrase }
    }

    suspend fun bumpFocusSession(todayIso: String) {
        store.edit {
            val date = it[PrefKeys.FOCUS_SESSIONS_DATE]
            val current = if (date == todayIso) (it[PrefKeys.FOCUS_SESSIONS_TODAY] ?: 0) else 0
            it[PrefKeys.FOCUS_SESSIONS_TODAY] = current + 1
            it[PrefKeys.FOCUS_SESSIONS_DATE] = todayIso
            it[PrefKeys.FOCUS_POINTS] = (it[PrefKeys.FOCUS_POINTS] ?: 0) + 1
        }
    }

    suspend fun depositTimeBank(minutes: Int, todayIso: String) {
        if (minutes <= 0) return
        store.edit {
            val lastDate = it[PrefKeys.TIME_BANK_LAST_DATE]
            if (lastDate == todayIso) return@edit  // already deposited today
            val total = it[PrefKeys.TIME_BANK_TOTAL_MIN] ?: 0
            it[PrefKeys.TIME_BANK_TOTAL_MIN] = total + minutes
            it[PrefKeys.TIME_BANK_LAST_DATE] = todayIso
        }
    }

    suspend fun bumpPhantomBuzz(todayIso: String) {
        store.edit {
            val date = it[PrefKeys.PHANTOM_BUZZ_DATE]
            val current = if (date == todayIso) (it[PrefKeys.PHANTOM_BUZZ_COUNT] ?: 0) else 0
            it[PrefKeys.PHANTOM_BUZZ_COUNT] = current + 1
            it[PrefKeys.PHANTOM_BUZZ_DATE] = todayIso
        }
    }

    suspend fun toggleMorningStep(step: String, todayIso: String) {
        store.edit {
            val date = it[PrefKeys.MORNING_DONE_DATE]
            val current = if (date == todayIso) (it[PrefKeys.MORNING_DONE_STEPS] ?: emptySet()) else emptySet()
            val next = if (step in current) current - step else current + step
            it[PrefKeys.MORNING_DONE_STEPS] = next
            it[PrefKeys.MORNING_DONE_DATE] = todayIso
        }
    }

    suspend fun toggleShutdownStep(step: String, todayIso: String) {
        store.edit {
            val date = it[PrefKeys.SHUTDOWN_DONE_DATE]
            val current = if (date == todayIso) (it[PrefKeys.SHUTDOWN_DONE_STEPS] ?: emptySet()) else emptySet()
            val next = if (step in current) current - step else current + step
            it[PrefKeys.SHUTDOWN_DONE_STEPS] = next
            it[PrefKeys.SHUTDOWN_DONE_DATE] = todayIso
        }
    }

    suspend fun setTimeDebt(min: Int, todayIso: String) {
        store.edit {
            it[PrefKeys.TIME_DEBT_MIN] = min.coerceAtLeast(0)
            it[PrefKeys.TIME_DEBT_DATE] = todayIso
        }
    }

    suspend fun setNuclearPassphrase(phrase: String) {
        store.edit { it[PrefKeys.NUCLEAR_PASSPHRASE] = phrase }
    }

    suspend fun setFutureSelfLetter(text: String) {
        store.edit { it[PrefKeys.FUTURE_SELF_LETTER] = text }
    }

    suspend fun lockPackageForToday(pkg: String, todayIso: String) {
        store.edit {
            val date = it[PrefKeys.LOCKED_TODAY_DATE]
            val current = if (date == todayIso) (it[PrefKeys.LOCKED_TODAY_PACKAGES] ?: emptySet()) else emptySet()
            it[PrefKeys.LOCKED_TODAY_PACKAGES] = current + pkg
            it[PrefKeys.LOCKED_TODAY_DATE] = todayIso
        }
    }

    suspend fun resetLockedTodayIfStale(todayIso: String) {
        store.edit {
            val date = it[PrefKeys.LOCKED_TODAY_DATE]
            if (date != todayIso) {
                it.remove(PrefKeys.LOCKED_TODAY_PACKAGES)
                it[PrefKeys.LOCKED_TODAY_DATE] = todayIso
            }
        }
    }

    suspend fun addGraceDay(dateIso: String) {
        store.edit {
            val current = it[PrefKeys.GRACE_DAYS] ?: emptySet()
            it[PrefKeys.GRACE_DAYS] = current + dateIso
        }
    }

    suspend fun removeGraceDay(dateIso: String) {
        store.edit {
            val current = it[PrefKeys.GRACE_DAYS] ?: emptySet()
            it[PrefKeys.GRACE_DAYS] = current - dateIso
        }
    }

    suspend fun setStreakFreezes(n: Int) {
        store.edit { it[PrefKeys.STREAK_FREEZES] = n.coerceIn(0, 3) }
    }

    suspend fun setAfterFallDue(dateIso: String) {
        store.edit {
            it[PrefKeys.AFTER_FALL_DUE] = dateIso
            it[PrefKeys.AFTER_FALL_STEPS] = emptySet()
        }
    }

    suspend fun clearAfterFall() {
        store.edit {
            it.remove(PrefKeys.AFTER_FALL_DUE)
            it.remove(PrefKeys.AFTER_FALL_STEPS)
        }
    }

    suspend fun setHourlyRate(usd: Int) {
        store.edit { it[PrefKeys.HOURLY_RATE_USD] = usd.coerceIn(0, 9999) }
    }

    suspend fun setUserAge(age: Int) {
        store.edit { it[PrefKeys.USER_AGE] = age.coerceIn(0, 120) }
    }

    suspend fun addTombstone(label: String, dateIso: String) {
        if (label.isBlank()) return
        store.edit {
            val current = it[PrefKeys.APP_TOMBSTONES] ?: emptySet()
            it[PrefKeys.APP_TOMBSTONES] = current + "${label.replace("|", " ")}|$dateIso"
        }
    }

    suspend fun removeTombstone(entry: String) {
        store.edit {
            val current = it[PrefKeys.APP_TOMBSTONES] ?: emptySet()
            it[PrefKeys.APP_TOMBSTONES] = current - entry
        }
    }

    suspend fun addFutureLetter(deliverDateIso: String, text: String) {
        val id = System.currentTimeMillis().toString()
        val safe = text.replace("|", " ")
        store.edit {
            val current = it[PrefKeys.FUTURE_LETTERS] ?: emptySet()
            it[PrefKeys.FUTURE_LETTERS] = current + "$id|$deliverDateIso|0|$safe"
        }
    }

    suspend fun markLetterDelivered(entry: String) {
        store.edit {
            val current = it[PrefKeys.FUTURE_LETTERS] ?: emptySet()
            val parts = entry.split("|", limit = 4)
            if (parts.size < 4) return@edit
            val replaced = "${parts[0]}|${parts[1]}|1|${parts[3]}"
            it[PrefKeys.FUTURE_LETTERS] = (current - entry) + replaced
        }
    }

    suspend fun applyTrackSnapshot(level: Int, points: Int, misses: Int, recalibrated: Boolean) {
        store.edit {
            it[PrefKeys.TRACK_LEVEL] = level
            it[PrefKeys.TRACK_POINTS] = points
            it[PrefKeys.TRACK_MISSES] = misses
            it[PrefKeys.TRACK_RECALIBRATED] = recalibrated
        }
    }

    suspend fun clearRecalibrated() {
        store.edit { it[PrefKeys.TRACK_RECALIBRATED] = false }
    }

    suspend fun setBuilderMode(value: Boolean) {
        store.edit { it[PrefKeys.BUILDER_MODE] = value }
    }

    suspend fun setSadVoice(voice: String) {
        store.edit { it[PrefKeys.SAD_VOICE] = voice }
    }

    suspend fun bumpSadDismissAndSuppress(nowMs: Long) {
        store.edit {
            val n = (it[PrefKeys.SAD_DISMISS_COUNT] ?: 0) + 1
            it[PrefKeys.SAD_DISMISS_COUNT] = n
            // Back off: 30m * 2^(n-1), capped at 8 hours.
            val backoff = (30L * 60_000L * (1L shl (n - 1).coerceAtMost(4)))
                .coerceAtMost(8L * 60 * 60 * 1000)
            it[PrefKeys.SAD_SUPPRESSED_UNTIL] = nowMs + backoff
        }
    }

    suspend fun resetSadDismiss() {
        store.edit {
            it[PrefKeys.SAD_DISMISS_COUNT] = 0
            it[PrefKeys.SAD_SUPPRESSED_UNTIL] = 0L
        }
    }

    suspend fun setNourishingPackages(packages: Set<String>) {
        store.edit { it[PrefKeys.NOURISHING_PACKAGES] = packages }
    }

    suspend fun addBaselineSample(dateIso: String, minutes: Int) {
        store.edit {
            val current = it[PrefKeys.BASELINE_SAMPLES] ?: emptySet()
            // One sample per date.
            val others = current.filterNot { e -> e.startsWith("$dateIso|") }.toSet()
            it[PrefKeys.BASELINE_SAMPLES] = others + "$dateIso|$minutes"
        }
    }

    suspend fun markBaselineApplied() {
        store.edit { it[PrefKeys.BASELINE_APPLIED] = true }
    }

    suspend fun updateDomainTrack(domain: String, level: Int, points: Int, miss: Int, dateIso: String) {
        store.edit {
            val current = it[PrefKeys.DOMAIN_TRACKS] ?: emptySet()
            val others = current.filterNot { e -> e.startsWith("$domain|") }.toSet()
            it[PrefKeys.DOMAIN_TRACKS] = others + "$domain|$level|$points|$miss|$dateIso"
        }
    }

    suspend fun resetEmergencyPassesIfNewWeek(thisWeek: String) {
        store.edit {
            val last = it[PrefKeys.EMERGENCY_WEEK]
            if (last != thisWeek) {
                it[PrefKeys.EMERGENCY_PASSES] = 5
                it[PrefKeys.EMERGENCY_WEEK] = thisWeek
            }
        }
    }

    suspend fun spendEmergencyPass(): Boolean {
        var spent = false
        store.edit {
            val current = it[PrefKeys.EMERGENCY_PASSES] ?: 0
            if (current > 0) {
                it[PrefKeys.EMERGENCY_PASSES] = current - 1
                spent = true
            }
        }
        return spent
    }

    suspend fun markDaySummary(dateIso: String) {
        store.edit {
            val current = it[PrefKeys.DAY_SUMMARY_DATES] ?: emptySet()
            it[PrefKeys.DAY_SUMMARY_DATES] = current + dateIso
        }
    }

    suspend fun setCelebrationLast(streak: Int, level: Int) {
        store.edit {
            it[PrefKeys.CELEBRATION_LAST_STREAK] = streak
            it[PrefKeys.CELEBRATION_LAST_LEVEL] = level
        }
    }

    suspend fun setEnergyZone(window: String, energy: String) {
        store.edit {
            val current = it[PrefKeys.ENERGY_ZONES] ?: emptySet()
            val others = current.filterNot { e -> e.startsWith("$window|") }.toSet()
            it[PrefKeys.ENERGY_ZONES] = others + "$window|$energy"
        }
    }

    suspend fun addFocusPoints(delta: Int) {
        store.edit {
            val current = it[PrefKeys.FOCUS_POINTS] ?: 0
            it[PrefKeys.FOCUS_POINTS] = (current + delta).coerceAtLeast(0)
        }
    }

    suspend fun updateDomainStreak(domain: String, days: Int, best: Int, dateIso: String) {
        store.edit {
            val current = it[PrefKeys.DOMAIN_STREAKS] ?: emptySet()
            val others = current.filterNot { e -> e.startsWith("$domain|") }.toSet()
            it[PrefKeys.DOMAIN_STREAKS] = others + "$domain|$days|$best|$dateIso"
        }
    }

    suspend fun toggleAfterFallStep(step: String) {
        store.edit {
            val current = it[PrefKeys.AFTER_FALL_STEPS] ?: emptySet()
            val next = if (step in current) current - step else current + step
            it[PrefKeys.AFTER_FALL_STEPS] = next
        }
    }

    suspend fun bumpUnlockCount(pkg: String, todayIso: String) {
        store.edit {
            val all = it[PrefKeys.UNLOCK_COUNTS] ?: emptySet()
            // Drop entries from earlier days while we're here.
            val sameDay = all.filter { e -> e.startsWith("$todayIso|") }
            val key = "$todayIso|$pkg"
            val existing = sameDay.firstOrNull { e -> e.startsWith("$key|") }
            val nextCount = (existing?.substringAfterLast("|")?.toIntOrNull() ?: 0) + 1
            val updated = sameDay.filterNot { e -> e.startsWith("$key|") }.toSet() + "$key|$nextCount"
            it[PrefKeys.UNLOCK_COUNTS] = updated
        }
    }

    suspend fun addMoodPing(timestamp: String, emoji: String, note: String) {
        val sanitizedNote = note.replace("|", " ").trim()
        val entry = "$timestamp|$emoji|$sanitizedNote"
        store.edit {
            val current = it[PrefKeys.MOOD_PINGS] ?: emptySet()
            // Keep last 60 entries.
            val merged = (current + entry).sortedDescending().take(60).toSet()
            it[PrefKeys.MOOD_PINGS] = merged
        }
    }

    suspend fun voteIdentity(isBuilder: Boolean, todayIso: String) {
        store.edit {
            val date = it[PrefKeys.IDENTITY_DATE]
            val sameDay = date == todayIso
            val builder = if (sameDay) (it[PrefKeys.IDENTITY_BUILDER_COUNT] ?: 0) else 0
            val consumer = if (sameDay) (it[PrefKeys.IDENTITY_CONSUMER_COUNT] ?: 0) else 0
            if (isBuilder) it[PrefKeys.IDENTITY_BUILDER_COUNT] = builder + 1
            else it[PrefKeys.IDENTITY_BUILDER_COUNT] = builder
            if (!isBuilder) it[PrefKeys.IDENTITY_CONSUMER_COUNT] = consumer + 1
            else it[PrefKeys.IDENTITY_CONSUMER_COUNT] = consumer
            it[PrefKeys.IDENTITY_DATE] = todayIso
        }
    }

    companion object {
        val DEFAULT_DISTRACTIONS = setOf(
            "com.instagram.android",
            "com.zhiliaoapp.musically",       // TikTok
            "com.google.android.youtube",
            "com.twitter.android",
            "com.x.android",
            "com.reddit.frontpage",
            "com.facebook.katana",
            "com.snapchat.android"
        )
    }
}
