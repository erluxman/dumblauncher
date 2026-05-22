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

    fun technique(key: Preferences.Key<Boolean>): Flow<Boolean> =
        store.data.map { it[key] ?: true }

    suspend fun setOnboardingComplete(value: Boolean) {
        store.edit { it[PrefKeys.ONBOARDING_COMPLETE] = value }
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
