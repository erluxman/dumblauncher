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

    // Transparency toggles (ETHICS-001): each technique opt-out-able
    val TECH_LOBBY = booleanPreferencesKey("tech_lobby")
    val TECH_DIMMING = booleanPreferencesKey("tech_dimming")
    val TECH_BEHAVIOR_INDICATOR = booleanPreferencesKey("tech_behavior_indicator")
    val TECH_HIDDEN_DRAWER = booleanPreferencesKey("tech_hidden_drawer")
    val TECH_VARIABLE_RATIO = booleanPreferencesKey("tech_variable_ratio")
    val TECH_ESCALATING = booleanPreferencesKey("tech_escalating")
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
