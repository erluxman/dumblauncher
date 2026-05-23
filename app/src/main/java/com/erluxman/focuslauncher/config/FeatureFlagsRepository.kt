package com.erluxman.focuslauncher.config

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONObject

private val Context.featureFlagStore by preferencesDataStore(name = "feature_flag_overrides")

/**
 * Loads flag definitions from `assets/featureflags.json` (the source of truth
 * for defaults + metadata), overlays user overrides stored in DataStore, and
 * exposes the effective value as a [Flow] of `Map<flagKey, enabled>`.
 *
 * Construct once per process (see [MainActivity]) and pass it down. The
 * definitions list is parsed once and cached.
 */
class FeatureFlagsRepository(context: Context) {

    private val appContext = context.applicationContext

    /** Parsed flag list, in JSON order. */
    val definitions: List<FeatureFlag> = loadDefinitions(appContext)

    /** Sync-readable defaults map. Use as the `initial` for `collectAsState`. */
    val defaults: Map<String, Boolean> = definitions.associate { it.key to it.default }

    /**
     * Effective flag values — defaults overlaid with any user overrides
     * stored in DataStore. Required flags are forced to `true` regardless
     * of override, so the user cannot brick the app from the settings UI.
     */
    val effective: Flow<Map<String, Boolean>> = appContext.featureFlagStore.data.map { prefs ->
        definitions.associate { def ->
            val override = prefs[booleanPreferencesKey(def.key)]
            val value = when {
                def.required -> true
                override != null -> override
                else -> def.default
            }
            def.key to value
        }
    }

    suspend fun setOverride(key: String, enabled: Boolean) {
        val def = definitions.firstOrNull { it.key == key } ?: return
        if (def.required) return
        appContext.featureFlagStore.edit { it[booleanPreferencesKey(key)] = enabled }
    }

    suspend fun clearOverride(key: String) {
        appContext.featureFlagStore.edit { it.remove(booleanPreferencesKey(key)) }
    }

    suspend fun resetAll() {
        appContext.featureFlagStore.edit { it.clear() }
    }

    companion object {
        private const val ASSET_NAME = "featureflags.json"

        private fun loadDefinitions(context: Context): List<FeatureFlag> {
            val text = context.assets.open(ASSET_NAME).bufferedReader().use { it.readText() }
            val arr = JSONObject(text).getJSONArray("flags")
            return List(arr.length()) { i ->
                val o = arr.getJSONObject(i)
                FeatureFlag(
                    key = o.getString("key"),
                    stage = o.getInt("stage"),
                    default = o.getBoolean("default"),
                    required = o.optBoolean("required", false),
                    label = o.getString("label"),
                    description = o.getString("description"),
                )
            }
        }
    }
}
