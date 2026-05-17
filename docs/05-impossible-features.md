# FocusLauncher — Impossible & Platform-Limited Features

## Legend

| Icon | Meaning |
|------|---------|
| 🟢 | Fully possible natively |
| 🟡 | Partially possible / hacky workaround exists |
| 🔴 | Impossible without root/jailbreak/MDM |
| ⚫ | Platform doesn't apply (e.g., Mac has no launcher concept) |

---

## 1. Modifying Other Apps' Internal UI/Rendering

**What we want:** Slow down scroll speed, pixelate content, distort faces, change animation speed, render feeds bottom-to-top inside other apps.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🔴 | Apps are sandboxed. Cannot inject code, modify rendering, or alter another app's view hierarchy. AccessibilityService can READ nodes but cannot MODIFY rendering pipeline. |
| iOS | 🔴 | Even stricter sandboxing. No inter-app UI manipulation whatsoever. |
| Mac | 🟡 | Accessibility APIs can manipulate some window properties. AppleScript can control some apps. But modifying internal rendering? Still impossible without injection. |

**Workarounds:**
- **Overlay approach (Android):** Draw a semi-transparent overlay ON TOP of other apps using `SYSTEM_ALERT_WINDOW`. Can dim, add grayscale filter, show countdown timers, or display intercept screens. Cannot modify what's underneath but can obscure/annoy.
- **Grayscale system-wide (Android):** `Settings.Secure` can toggle grayscale for entire display. Not per-app, but effective. Device Admin or Accessibility can toggle it programmatically.
- **Animation scale (Android):** Can change `window_animation_scale`, `transition_animation_scale`, `animator_duration_scale` via `Settings.Global` — BUT affects ALL apps system-wide, not per-app.
- **Screen recording + re-rendering (theoretical):** Capture screen, apply filters, display modified version. Massive latency, battery drain, impractical.
- **VPN-based content modification:** Route app traffic through local VPN, modify responses. Works for web content in apps but not native UI elements. Ethical/legal grey area.

---

## 2. Truly Preventing App Uninstallation

**What we want:** Make it literally impossible for user to remove the app without group approval.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🔴 | User ALWAYS has: ADB uninstall, factory reset, Safe Mode boot. Device Admin only adds friction, never true prevention. |
| iOS | 🔴 | User can always delete from home screen or Settings → Storage. MDM profiles can prevent deletion but require enterprise enrollment. |
| Mac | 🟡 | App can install LaunchDaemon that reinstalls itself. But user with admin access can always find and remove everything. |

**Workarounds:**
- **Device Admin (Android):** Best available. Greys out uninstall button. User must navigate 6-7 steps to deactivate first. Stops 90% of impulse uninstalls.
- **Profile Owner (Android):** If device is provisioned as managed device during setup (factory reset required), app gets near-MDM control. Nuclear option — realistic only for dedicated "focus phone" setups.
- **MDM Profile (iOS):** Organization-managed devices can prevent app deletion. User would need to enroll their personal phone in a "FocusLauncher MDM." Legal/ethical complexity.
- **Social/financial deterrents:** Can't prevent technically, but can make consequences so painful that user self-deters. This is the actual viable path.
- **Companion device strategy:** App runs on a SECOND device (cheap Android) that's physically locked away. Primary phone syncs with it. Even if primary app removed, the locked-away device holds the rules.

---

## 3. Reading Other Apps' Content/Data

**What we want:** Know WHAT user is looking at (specific posts, messages, content type), expose algorithm reasons, detect addictive content patterns.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🔴 | App sandbox. Cannot read another app's memory, files, or internal state. AccessibilityService can read visible text nodes on screen but not structured data. |
| iOS | 🔴 | Absolute sandbox. No inter-app data reading. Not even screen content reading without explicit Screen Recording permission (user-visible indicator). |
| Mac | 🟡 | Accessibility API can read window content. AppleScript can query some apps. More permissive but app-dependent. |

**Workarounds:**
- **Accessibility text reading (Android):** Can read visible text on screen via AccessibilityNodeInfo. Can detect "you're on a comments section" or "this is a video feed." But cannot read what's NOT currently rendered.
- **Usage patterns as proxy:** Can't read content, but CAN track: time in app, number of opens, session duration, time of day, app switches. Pattern = proxy for content type.
- **Notification content (Android):** NotificationListenerService can read notification TEXT. So "Sarah posted a story" is readable. Partial window into content.
- **VPN content inspection:** Local VPN can intercept HTTP traffic. HTTPS (99% of apps) is encrypted. Would need MITM cert installed (user must explicitly trust it). Technically possible, ethically questionable, apps increasingly use cert pinning.
- **Screen capture + OCR (Android):** MediaProjection API can capture screen → run OCR → analyze content. But requires persistent user-visible notification ("Screen is being recorded"). Battery-intensive. Creepy.
- **DNS-level tracking (Android):** Local VPN can see WHICH domains are queried (even with HTTPS). "User made 47 requests to instagram CDN in 5 min" = scrolling heavily. Can't see WHAT, but can see HOW MUCH.

---

## 4. Controlling System Notifications at Source

**What we want:** Prevent specific apps from generating notifications entirely, modify notification content, delay delivery, or batch them.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🟡 | NotificationListenerService can DISMISS notifications but cannot prevent generation or modify content. Can't suppress the sound/vibration of the ORIGINAL before dismissing. |
| iOS | 🔴 | No programmatic notification control for other apps. User must manually configure in Settings. Focus Modes exist but aren't programmatically controllable by third-party apps. |
| Mac | 🟡 | Can configure Do Not Disturb programmatically. Can't selectively filter per-app notifications programmatically without hacks. |

**Workarounds:**
- **Auto-dismiss (Android):** NotificationListener sees notification → instantly dismisses it. User never sees it in shade. BUT: the heads-up notification may flash for a split-second. Sound may already have played.
- **DND mode toggle (Android):** Can programmatically enable Do Not Disturb with exceptions (only calls from favorites). Blocks all notifications system-wide.
- **Notification channel manipulation (Android):** Can guide user to disable specific notification channels per app. But can't do it programmatically for them.
- **Scheduled batch delivery:** Dismiss all notifications in real-time, store content locally, re-deliver as a batch at user-defined times. Effectively "snooze everything until 12pm and 6pm."
- **iOS Shortcuts integration:** On iOS, a Shortcut automation COULD toggle Focus Modes at scheduled times. But requires user to build the automation or use a profile.

---

## 5. Replacing System Clock / Status Bar

**What we want:** Show "hours alive remaining" or "time wasted today" instead of actual time in status bar.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🔴 | Status bar is system UI. Third-party apps cannot modify it. Even SystemUI overlays require system-level access (root or custom ROM). |
| iOS | 🔴 | Status bar completely system-controlled. Zero third-party access. |
| Mac | 🟡 | Menu bar apps can ADD items. Can't replace the system clock but can put custom time display next to it. |

**Workarounds:**
- **Persistent notification (Android):** Ongoing notification that shows "4.2hrs wasted | 413,280hrs remain." Always visible in notification shade and can show on lock screen. Not in status bar, but close.
- **Custom home screen widget (Android):** Since WE are the launcher, our home screen can show anything instead of a clock. User sees our version of time every time they go home.
- **Always-on-display customization (Android):** Some OEMs allow custom AOD. Limited but possible on Samsung via Good Lock.
- **Full-screen clock replacement:** When user explicitly checks time (locks/unlocks), show our version before system clock is visible. Overlay for 2 seconds.
- **Watch face (WearOS):** Companion watch app shows "real clock" (hours remaining, time wasted). User sees it more often than phone status bar.

---

## 6. Force-Closing or Force-Stopping Other Apps

**What we want:** After time limit, KILL the app process. Not just overlay — actually terminate it. Clear scroll position.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🔴 | `killBackgroundProcesses` only kills background processes, not foreground. `forceStopPackage` requires FORCE_STOP_PACKAGES (system-only). Device Admin can wipe app data but that's nuclear. |
| iOS | 🔴 | Zero ability to terminate other apps. OS manages lifecycle exclusively. |
| Mac | 🟡 | Can send `kill` signal to processes if running with appropriate permissions. AppleScript `quit app "X"` works for most apps. |

**Workarounds:**
- **Accessibility "press home" (Android):** AccessibilityService can perform `GLOBAL_ACTION_HOME` or `GLOBAL_ACTION_RECENTS` then swipe away the app. Simulates user closing it. Hacky but works.
- **Overlay blockade (Android):** Don't kill the app — just draw an immovable, full-screen overlay on top. App is technically running but completely unusable. User must deal with YOUR screen.
- **Device Admin `wipeData` for specific app (Android):** `clearApplicationUserData` via Device Policy Manager. Nukes the app's data. Extreme but possible. Resets algorithm, scroll position, login. TRUE memory wipe.
- **Launch home screen (Android):** Simply launch your own launcher activity with FLAG_ACTIVITY_NEW_TASK. Brings user back to home, pushes distraction app to background. Repeated enough = they give up.
- **App pinning (Android):** Lock screen to YOUR app using `startLockTask()`. User literally cannot switch away without PIN. Extreme "lockdown mode."

---

## 7. Per-App Network/Internet Blocking

**What we want:** Cut internet access to specific apps (Instagram can't load new content, but WhatsApp works fine).

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🟡 | No per-app firewall API. BUT: local VPN can inspect traffic by originating UID and selectively drop packets. Works without root. |
| iOS | 🔴 | No VPN-based per-app filtering available to third-party apps. Screen Time can limit, but not programmatically by other apps. |
| Mac | 🟡 | Can modify `/etc/hosts` or use packet filter (pf) rules. Little Snitch model. Requires admin permission. |

**Workarounds:**
- **Local VPN firewall (Android):** Create VPNService, inspect packets, identify originating app by UID, drop traffic for blocked apps. Instagram app opens but shows "no internet" / stale content. Many firewall apps (NetGuard, AFWall+) use this approach.
- **DNS blackholing (Android):** Via local VPN, resolve blocked app domains to 127.0.0.1. App can't reach servers. Lighter than full packet inspection.
- **Time-limited connectivity:** Allow app to load for 30 seconds (fetch initial content), then cut network. User has finite content, no infinite scroll refresh.
- **Bandwidth throttling:** Don't block entirely — throttle to 1kbps. Videos won't load. Images take 30 seconds. Text-only experience. Makes media-heavy apps useless while keeping messaging functional.

---

## 8. Modifying Other Apps' Scroll Behavior / Input

**What we want:** Infinite scroll becomes finite. Feeds end after 20 posts. Scroll speed reduced.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🔴 | Cannot inject touch event modifiers into other apps. Cannot limit scroll or modify feed length. App's RecyclerView/UI is private. |
| iOS | 🔴 | Same sandbox restrictions. |
| Mac | 🟡 | Can theoretically intercept mouse scroll events system-wide. But per-app modification is fragile. |

**Workarounds:**
- **Time-based content cut (VPN):** After N minutes, block the CDN domains that serve images/videos. Feed still scrolls but all content is grey placeholders. Frustrating enough to quit.
- **Overlay countdown:** Overlay a progressively larger/more opaque layer as time increases. App still scrolls underneath but user sees less and less.
- **Accessibility-based scroll monitoring:** Detect scroll events via Accessibility. After N scroll actions, trigger intervention (overlay, lock, vibration). Can't stop the scroll, but can punish it.
- **Custom browser/wrapper:** For web-based services (Reddit, Twitter web), build in-app browser with injected CSS/JS that limits feed, hides infinite scroll, forces stopping points. Only works for services user accesses via your browser.
- **The nuclear option — kill network mid-scroll:** VPN detects rapid sequential requests (scrolling pattern) → throttle to 0 → "feed ends here." Technically network-level but achieves scroll-stopping effect.

---

## 9. Accessing Biometric Data in Real-Time from Phone Sensors

**What we want:** Detect pupil dilation, stress levels, micro-expressions, skin conductance, real-time emotional state from the phone alone.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🔴 | Phone cameras lack IR sensors for pupil tracking. No skin conductance sensor. Emotion detection from front camera requires constant processing + very unreliable accuracy. |
| iOS | 🔴 | Same hardware limitations. TrueDepth camera has more data (face mesh) but Apple doesn't expose real-time emotional analysis APIs. ARKit face tracking works but battery-destroying. |
| Mac | 🔴 | Same issues. No biometric sensors beyond camera. |

**Workarounds:**
- **Heart rate from camera (Android/iOS):** PPG (photoplethysmography) — finger on camera + flash can read heart rate. But requires user to actively hold finger there. Not passive.
- **WearOS/Apple Watch integration:** Smartwatches HAVE heart rate, stress, skin temperature sensors. Companion app reads these in real-time. Detect elevated HR → signal phone to lock. This is the real path.
- **Typing pattern analysis:** Typing speed, error rate, and pressure (on supported devices) correlate with emotional state. Fast/erratic = anxious. Slow/deliberate = calm. Doesn't need special sensors.
- **Usage pattern as emotional proxy:** "Opens phone 8 times in 10 minutes without doing anything" = anxiety pattern. "3am scrolling after 2 weeks of sleeping at 11pm" = something wrong. Behavioral signals replace biometric ones.
- **Grip pressure (limited Android devices):** Some phones have pressure-sensitive edges. Tight grip = stress. Very limited device support.
- **Voice tone analysis:** If user is on calls or recording voice notes, analyze vocal patterns for stress indicators. Requires mic access during specific moments.

---

## 10. Programmatically Installing/Reinstalling Apps

**What we want:** If user uninstalls FocusLauncher, have a companion service that reinstalls it automatically.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🔴 | Cannot silently install apps. `ACTION_INSTALL_PACKAGE` shows system installer UI requiring user confirmation. Background install requires Device Owner (enterprise MDM level). |
| iOS | 🔴 | Absolutely impossible. App Store is only install path (without MDM). |
| Mac | 🟡 | LaunchDaemon could trigger reinstall from .dmg. But user can remove daemon too. Arms race. |

**Workarounds:**
- **Device Owner provisioning (Android):** If user factory-resets and provisions device with FocusLauncher as Device Owner (QR code during setup), app gains full MDM control: silent install/uninstall, cannot be removed without another factory reset. This is the "dedicated focus phone" path.
- **Companion APK (Android):** Install TWO apps. Each monitors the other. If one is uninstalled, the other detects it (package broadcast) and shows persistent "reinstall" prompt + notifies group. Can't force reinstall but can scream about it.
- **Cloud-side detection:** Backend detects when device stops checking in. After 30min of silence → notify accountability group: "erluxman may have uninstalled." Social pressure triggers externally.
- **Work Profile (Android):** Create a managed Work Profile on the device. Apps in work profile are controlled by profile policy. Harder to remove entire work profile than individual app.

---

## 11. Preventing Screenshots / Screen Recording of the App

**What we want:** Prevent user from screenshotting restriction screens to mock the app, or recording to prove "the app is too restrictive" for sympathy.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🟢 | `FLAG_SECURE` on window prevents screenshots and screen recording of YOUR app's UI. System-supported. |
| iOS | 🟢 | Can detect screenshots (notification) but cannot PREVENT them. Can hide sensitive content when app goes to background (app switcher thumbnail). |
| Mac | 🟡 | No direct equivalent. Can detect screen recording status but cannot prevent it. |

**Workarounds:**
- This one is actually possible on Android! `FLAG_SECURE` is the answer. Prevents screenshots, screen recording, and even blocks the app from appearing in recent apps thumbnail.
- Use it for: shame screens, intervention messages, group voting UI — anything user might screenshot to share out of context.

---

## 12. Modifying System Settings Persistently Without User Re-confirmation

**What we want:** Set grayscale ON and keep it on, even if user manually toggles it back. Auto-enforce system settings.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🟡 | Can write to `Settings.Secure` with WRITE_SECURE_SETTINGS (requires ADB grant or Device Admin). But user can always go to Settings and change it back. No way to "lock" a system setting. |
| iOS | 🔴 | Cannot modify system settings programmatically. Only MDM profiles can enforce settings. |
| Mac | 🟡 | Can modify system preferences via scripts. But user can undo. Can run a daemon that re-enforces every N seconds (arms race). |

**Workarounds:**
- **Monitor + re-apply (Android):** Background service watches for setting changes. User toggles grayscale off → service detects within 1 second → toggles it back on. User can play whack-a-mole but the service is faster. Drain on battery though.
- **Overlay-based grayscale (Android):** Instead of system grayscale, draw a desaturating overlay via SYSTEM_ALERT_WINDOW. App controls it entirely. User cannot "undo" it in system settings because it's not a system setting.
- **Device Admin policies (Android):** Some policies are enforceable (password requirements, camera disable, encryption). But display settings aren't in the Device Admin policy set.

---

## 13. Detecting / Blocking App Installation

**What we want:** Prevent user from installing "competitor" apps (other launchers, VPNs to bypass our blocks, screen time bypassers).

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🟡 | Can receive `PACKAGE_ADDED` broadcast and detect new installs. Cannot PREVENT installation. Device Owner CAN restrict app installs to a whitelist. |
| iOS | 🔴 | No broadcast for new installs. No detection. No prevention (without MDM). |
| Mac | 🟡 | Can monitor `/Applications` folder. Can alert on new installs. Cannot prevent without elevated privileges. |

**Workarounds:**
- **Detect + shame (Android):** Receive PACKAGE_ADDED broadcast. If user installs "Digital Wellbeing Bypass" or another launcher → notify accountability group. "erluxman just installed 'Screen Time Bypass Tool.' 🚨"
- **Device Owner whitelist (Android):** `setPackagesSuspended()` or `addUserRestriction(DISALLOW_INSTALL_APPS)`. Full control. But requires MDM-level provisioning.
- **Overlay on Play Store (Android):** When user opens Play Store, show overlay: "What are you looking for? Declare your intent." Guilt trip before they even search. Can't block but can friction.
- **Alert on launcher change (Android):** Detect when another app registers as launcher candidate. Alert user + group immediately.

---

## 14. Running Indefinitely in Background Without Being Killed

**What we want:** Service that monitors app usage, enforces rules, and NEVER gets killed by OS battery optimization.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🟡 | Foreground Service with notification can run indefinitely. But OEMs (Xiaomi, Samsung, Huawei) aggressively kill background apps regardless. Doze mode restricts network/GPS in background. |
| iOS | 🔴 | Background execution is severely limited. Max ~30 seconds after backgrounding. Background fetch is unreliable (15-min minimum, OS decides). No persistent monitoring possible. |
| Mac | 🟢 | LaunchDaemon or LaunchAgent runs persistently. Full background execution. |

**Workarounds:**
- **Foreground Service + notification (Android):** Required anyway for UsageStats monitoring. Shows persistent "FocusLauncher is protecting you" notification. Makes service high-priority.
- **OEM-specific battery whitelist (Android):** Guide user through manufacturer-specific settings to exclude app from battery optimization. Xiaomi: AutoStart + No battery restrictions. Samsung: "Never sleeping apps." Must handle per-OEM.
- **WorkManager periodic checks (Android):** Even if service dies, WorkManager guarantees periodic execution (min 15 min). Can re-check and re-enforce state on wake.
- **Accessibility Service persistence (Android):** Accessibility services are treated as critical system components. OS is extremely reluctant to kill them. If we have Accessibility permission, our service stays alive.
- **iOS Background Modes combo:** Combine location updates (significant location change) + background fetch + push notifications to get semi-regular wake-ups. Still unreliable. iOS is fundamentally hostile to this use case.
- **iOS Screen Time API (MDM-like):** FamilyControls framework (iOS 15+) allows approved apps to monitor/restrict other apps. Requires Apple approval + user explicit enrollment. This IS the iOS path but Apple gates it heavily.

---

## 15. Intercepting App Launch Before It Happens

**What we want:** User taps Instagram icon → OUR screen appears FIRST (lobby, cognitive tax, etc.) → Instagram only opens if they pass.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🟡 | No direct "app launch interceptor" API. BUT: AccessibilityService can detect when an app window appears → immediately overlay/launch home. Sub-second response. Or: UsageStats polling (5-second interval) detects foreground change → overlay. |
| iOS | 🔴 | Cannot detect or intercept other app launches. Screen Time can block with passcode but third-party apps have zero launch interception. |
| Mac | 🟡 | Can monitor process list. Detect new process → activate overlay window. Some latency. |

**Workarounds:**
- **WE are the launcher (Android):** Since we're the home screen, all app launches originate from US. We literally control the launch path. User taps icon in our drawer → we show lobby → then launch the real app via Intent. This is the PRIMARY and BEST approach.
- **Accessibility intercept (Android):** For apps launched from notifications or other apps (not our launcher), Accessibility detects the window change and overlays instantly. User sees 50-200ms of the app before our screen appears. Acceptable.
- **UsageStatsManager polling (Android):** Poll foreground app every 1-3 seconds. Detect restricted app → overlay. Laggy (user sees app for 1-3 seconds). Fallback only.
- **iOS Shortcuts automation:** "When I open Instagram" trigger → run Shortcut that shows alert/question. Requires user to manually build this. We can provide the Shortcut but can't enforce it.
- **Custom DNS + loading delay:** Block app's CDN for first 15 seconds (via local VPN). App opens but shows loading spinner. During that time, show our overlay. App "catches up" only after user passes our gate.

---

## 16. Tracking Exact Content Consumed (What Was Watched/Read)

**What we want:** Know exactly which YouTube videos were watched, which Reddit posts were read, which Instagram stories were viewed. For detailed "where did your time go" reports.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🔴 | Complete app sandbox. Cannot read YouTube's watch history, Instagram's view state, or any app's internal records. |
| iOS | 🔴 | Same. Absolute sandbox isolation. |
| Mac | 🟡 | Browser history is accessible if user grants permission. Native app data still sandboxed. |

**Workarounds:**
- **Browser-only access:** If user agrees to use services via OUR built-in browser (WebView), we control everything. We see every URL, every scroll position, every video loaded. Most powerful but requires user behavior change.
- **Accessibility screen reading (Android):** Read visible text on screen periodically. "User is viewing a post about [topic]." Very rough. Can't get video content. High battery usage.
- **API integration (user-consented):** YouTube Data API, Instagram Graph API, Reddit API — user grants OAuth access. We can pull their actual watch/read history. Requires per-platform API keys and user auth. Most accurate for supported platforms.
- **Notification content (Android):** "New video from MrBeast" — we know what was pushed. If user then spends 15 min in YouTube, likely watched it. Inference.
- **Network traffic analysis (VPN):** See domains/URLs requested. `youtube.com/watch?v=X` reveals exact video. `reddit.com/r/X/comments/Y` reveals exact post. HTTPS means URL is encrypted... BUT DNS queries and SNI (Server Name Indication) leak domains. Deep packet inspection of URL paths requires MITM (user-installed cert).
- **Screen Time Reports API (iOS):** DeviceActivityReport (iOS 15+) gives category-level breakdowns from Apple's Screen Time. Not content-level, but shows time per app/category/website domain.

---

## 17. Playing Audio / Making Sounds While Another App Has Audio Focus

**What we want:** Kazoo mode — play sounds OVER other apps' audio. Or narrate actions while user watches videos.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🟡 | Can request audio focus with AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK — other app ducks (gets quieter) and our sound plays. Or use notification sound channel which bypasses focus. But can't truly MIX audio from two apps at same volume. |
| iOS | 🔴 | Audio session categories are strict. Playing audio from background while foreground app has audio is unreliable/impossible for third-party apps. |
| Mac | 🟢 | Multiple audio sources play simultaneously by default. No issue. |

**Workarounds:**
- **Notification sound (Android):** Post notification with custom sound. System plays it regardless of current audio focus. Short sounds (kazoo honk, applause) work perfectly this way.
- **Alarm channel (Android):** Use USAGE_ALARM audio attribute. System prioritizes it over media. Guaranteed to be heard.
- **Duck other audio (Android):** Request transient audio focus with duck flag. Other app reduces volume by ~50%. Our TTS/sound plays. Then release focus. Other app resumes normal volume.
- **Media button intercept:** Can't force audio but CAN send media button events to pause other app's playback. Pause their music → play our message → release → their music resumes.

---

## 18. Controlling Phone Hardware (Vibration Patterns, LED, Screen Brightness) Per-App

**What we want:** Custom vibration escalation as time limits approach. LED color changes. Screen brightness manipulation based on which app is open.

| Platform | Status | Why |
|----------|--------|-----|
| Android | 🟡 | Vibration: fully controllable with VIBRATE permission. LED: deprecated (Android 8+, replaced by notification channels). Brightness: can set via WRITE_SETTINGS but affects entire system, not per-app. |
| iOS | 🔴 | No custom vibration patterns beyond haptic engine presets. No LED control. No brightness control. |
| Mac | ⚫ | No vibration. Keyboard backlight controllable. Screen brightness controllable. |

**Workarounds:**
- **Vibration (Android):** Fully possible! Create escalating vibration patterns via VibrationEffect. WearOS companion adds wrist vibration. No workaround needed — it works.
- **Brightness (Android):** Monitor foreground app (Accessibility/UsageStats). When distraction app detected → programmatically lower brightness via Settings.System.SCREEN_BRIGHTNESS. Restore when user returns to productive app. Small delay but functional.
- **Overlay-based dimming:** Instead of system brightness, overlay a semi-transparent dark layer. Achieves same visual effect without touching system settings. More responsive, per-app, instant.
- **LED replacement:** Use persistent notification with custom color on devices with LED. On modern phones without LED, use Always-On-Display pulse or edge lighting (Samsung-specific API).

---

## Summary Table: Feature Possibility by Platform

| Feature | Android | iOS | Mac | Best Workaround |
|---------|---------|-----|-----|-----------------|
| Modify other app UI | 🔴 | 🔴 | 🟡 | Overlay + system grayscale |
| Prevent uninstall | 🔴 | 🔴 | 🟡 | Device Admin + social locks |
| Read other app data | 🔴 | 🔴 | 🟡 | Accessibility text + API OAuth |
| Control notifications | 🟡 | 🔴 | 🟡 | Auto-dismiss + batch re-deliver |
| Replace system clock | 🔴 | 🔴 | 🟡 | Custom widget + persistent notification |
| Force-close apps | 🔴 | 🔴 | 🟡 | Accessibility home press + overlay |
| Per-app firewall | 🟡 | 🔴 | 🟡 | Local VPN with UID filtering |
| Modify scroll behavior | 🔴 | 🔴 | 🟡 | VPN content cut + overlay |
| Real-time biometrics | 🔴 | 🔴 | 🔴 | Smartwatch companion |
| Auto-reinstall | 🔴 | 🔴 | 🟡 | Companion app + cloud detection |
| Prevent screenshots (own app) | 🟢 | 🟡 | 🟡 | FLAG_SECURE (Android native) |
| Persist system settings | 🟡 | 🔴 | 🟡 | Monitor + re-apply loop |
| Block app installation | 🟡 | 🔴 | 🟡 | Detect + shame + Device Owner |
| Background persistence | 🟡 | 🔴 | 🟢 | Foreground Service + Accessibility |
| Intercept app launch | 🟡 | 🔴 | 🟡 | BE the launcher + Accessibility |
| Track content consumed | 🔴 | 🔴 | 🟡 | Built-in browser + API OAuth |
| Play audio over apps | 🟡 | 🔴 | 🟢 | Notification/alarm audio channel |
| Hardware control per-app | 🟡 | 🔴 | ⚫ | Vibration + overlay dimming |

---

## Key Insight

**Android is the only viable platform for this app.** iOS sandboxing makes 80% of the concept impossible. The killer features (launcher replacement, accessibility monitoring, device admin, overlay system, local VPN, notification listener) are ALL Android-only capabilities.

iOS version would be severely limited to:
- Screen Time API (FamilyControls — requires Apple approval)
- Shortcuts automations (user-configured, fragile)
- Widget-only experience (no launcher replacement)
- Social/financial features via backend (platform-independent)

**Mac companion** could add value for users who also waste time on desktop, using more permissive APIs.
