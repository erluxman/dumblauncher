package com.erluxman.focuslauncher.config

/**
 * A single feature-flag definition, loaded from `assets/featureflags.json`.
 *
 * Defaults and metadata live in JSON so the in-app settings screen can
 * surface them without a recompile. User overrides are stored separately
 * in DataStore via [FeatureFlagsRepository].
 */
data class FeatureFlag(
    val key: String,
    val stage: Int,
    val default: Boolean,
    val required: Boolean,
    val label: String,
    val description: String,
)

/**
 * String constants for every flag key referenced from Kotlin code. Keeping
 * the keys here (rather than scattered string literals) gives compile-time
 * safety even though values now live in JSON.
 */
object FlagKey {
    const val LAUNCHER_HOME = "LAUNCHER_HOME"
    const val LEGACY_DASHBOARD = "LEGACY_DASHBOARD"
    const val STATS_SHEET = "STATS_SHEET"
    const val ONBOARDING = "ONBOARDING"
    const val TRANSPARENCY = "TRANSPARENCY"
    const val UNINSTALL_FLOW = "UNINSTALL_FLOW"
    const val VIP_CONTACTS = "VIP_CONTACTS"
    const val FOCUS_TIMER = "FOCUS_TIMER"
    const val MANTRA = "MANTRA"
    const val BOREDOM = "BOREDOM"
    const val BREATH_UNLOCK = "BREATH_UNLOCK"
    const val FUTURE_SELF_VIDEO = "FUTURE_SELF_VIDEO"
    const val LOBBY_INTERCEPT = "LOBBY_INTERCEPT"
    const val COGNITIVE_TAX = "COGNITIVE_TAX"
    const val DIMMING_OVERLAY = "DIMMING_OVERLAY"
    const val BEHAVIOR_INDICATOR = "BEHAVIOR_INDICATOR"
    const val FEATURE_FLAGS = "FEATURE_FLAGS"
    const val DATA_EXPORT = "DATA_EXPORT"
    const val IDENTITY_VOTING = "IDENTITY_VOTING"
    const val GRADUATE_MODE = "GRADUATE_MODE"
    const val BUILT_WRAPPED = "BUILT_WRAPPED"
    const val SUBSCRIPTIONS = "SUBSCRIPTIONS"
    const val SLEEP_CORRELATOR = "SLEEP_CORRELATOR"
    const val MOOD_PINGS = "MOOD_PINGS"
    const val IDEA_PARKING = "IDEA_PARKING"
    const val CONSUMPTION_LOG = "CONSUMPTION_LOG"
    const val JOURNAL = "JOURNAL"
    const val PR_WALL = "PR_WALL"
    const val TRAVEL_ATLAS = "TRAVEL_ATLAS"
    const val COMPOUND_CURVE = "COMPOUND_CURVE"
    const val MONEY_MIRROR = "MONEY_MIRROR"
    const val ANTI_BIO = "ANTI_BIO"
    const val SLEEP_WINDOW = "SLEEP_WINDOW"
    const val DAILY_LOGS = "DAILY_LOGS"
    const val HIGHLIGHTS = "HIGHLIGHTS"
    const val TOMBSTONES = "TOMBSTONES"
    const val FUTURE_LETTERS = "FUTURE_LETTERS"
    const val WEEKLY_REVIEW = "WEEKLY_REVIEW"
    const val PROMISE_RATIO = "PROMISE_RATIO"
    const val LEGACY_COUNTER = "LEGACY_COUNTER"
    const val TIME_DILATION = "TIME_DILATION"
    const val SAD_SELF = "SAD_SELF"
    const val TIME_MONEY = "TIME_MONEY"
    const val ESTIMATION = "ESTIMATION"
    const val STRESS = "STRESS"
    const val ANCHOR = "ANCHOR"
    const val SOCIAL_GROUPS = "SOCIAL_GROUPS"
    const val ENERGY_ZONES_SURFACE = "ENERGY_ZONES_SURFACE"
    const val TRACK_STATUS = "TRACK_STATUS"
    const val RECIPROCITY = "RECIPROCITY"

    // ─── Payments + backend (Stage 2) ──────────────────────────────────────
    const val PAYMENTS_NATIVE_ON_DEVICE = "PAYMENTS_NATIVE_ON_DEVICE"
    const val PAYMENTS_NATIVE = "PAYMENTS_NATIVE"
    const val PAYMENTS_WEB = "PAYMENTS_WEB"
    const val FIREBASE_BACKEND = "FIREBASE_BACKEND"
    const val WEB_APP_CHROME_HOME = "WEB_APP_CHROME_HOME"
    const val SOCIAL_DUAL_STREAKS = "SOCIAL_DUAL_STREAKS"
    const val DISAPPOINTMENT = "DISAPPOINTMENT"
    const val BUILDER_PROFILE = "BUILDER_PROFILE"
    const val FEED = "FEED"
    const val PRE_COMMIT = "PRE_COMMIT"
    const val RECEIPT_WALL = "RECEIPT_WALL"
}
