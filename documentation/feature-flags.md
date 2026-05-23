# Feature flags

Runtime-toggleable gating for work-in-progress features so half-built code
doesn't block the rest of the project. The user can flip any non-required
flag from a settings screen in the app itself.

Distinct from the **technique** toggles in `TransparencyScreen` — those gate
individual psychological techniques inside *working* features (the lobby's
variable-ratio penalty, anchoring, etc.). Feature flags gate entire
features on or off, including their UI surface.

## Layout

```
config/
  ├── FeatureFlag.kt              data class + FlagKey constants
  └── FeatureFlagsRepository.kt   loads JSON + DataStore overrides
assets/
  └── featureflags.json           source of truth for defaults + metadata
ui/flags/
  └── FeatureFlagsScreen.kt       in-app toggle UI (Screen.FeatureFlags)
```

Definitions live in JSON; user overrides live in a dedicated DataStore
(`feature_flag_overrides`); effective value = override if set, else default
— *unless* the flag is `required: true`, in which case it's always `true`.

## The JSON

`assets/featureflags.json`:

```json
{
  "flags": [
    { "key": "STATS_SHEET", "stage": 1, "default": true, "required": false,
      "label": "stats sheet", "description": "sentence-only dashboard" }
  ]
}
```

Fields:

- `key` — uppercase identifier, mirrored in `FlagKey` for compile-time safety
- `stage` — 1–4, matches `project-plan.md` §6; used to group the UI
- `default` — value when no user override is set
- `required` — if `true`, can't be toggled off (e.g. `LAUNCHER_HOME`,
  `ONBOARDING`, `FEATURE_FLAGS` itself)
- `label` / `description` — what the user sees on the settings screen

## Adding a flag

1. Add the row to `assets/featureflags.json`.
2. Add a `const val` to `FlagKey` in `FeatureFlag.kt` matching the JSON key.
3. Gate the relevant code: `if (flags[FlagKey.X] == true) { ... }`.
4. For Compose, collect from `repo.effective` and read the map. The
   `MinimalMenuScreen` pattern is a good reference.

The flag immediately appears in the in-app settings screen, grouped by
stage, no extra wiring needed.

## When to use a flag (vs the alternatives)

| Need | Use |
|---|---|
| Hide an incomplete feature while developing | feature flag |
| Let the user opt out of a psychological technique inside a working feature | `TransparencyScreen` technique toggle |
| Numeric tuning constant (durations, thresholds) | `Tuning.kt`-style constants |
| Permanent code (no toggle needed) | just write it |

## Reading flags in code

```kotlin
@Composable
fun MyScreen(repo: FeatureFlagsRepository) {
    val flags by repo.effective.collectAsState(initial = repo.defaults)
    if (flags[FlagKey.BUILT_WRAPPED] == true) {
        WrappedSection(...)
    }
}
```

Always pass `repo.defaults` as the `initial` to avoid an empty-map flicker
on the first frame.

## Required flags

Some flags can't be toggled because turning them off would brick the app
(e.g. `LAUNCHER_HOME`) or remove the user's ability to re-enable things
(`FEATURE_FLAGS`). They render as locked switches in the UI and
`setOverride` is a no-op for them.

## Promotion lifecycle

1. Add flag default `false`, write partial code behind it.
2. Build the feature; smoke-test by flipping on in the settings screen.
3. Flip default to `true` once the feature is complete.
4. After a release cycle in `default: true` without regressions, delete the
   flag entry, the `FlagKey` constant, and every `if (flags[...])` check.

Flags are scaffolding, not load-bearing. Don't let them accumulate.
