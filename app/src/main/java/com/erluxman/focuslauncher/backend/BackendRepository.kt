package com.erluxman.focuslauncher.backend

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Thin facade over the optional Firebase backend. Every consumer goes
 * through this interface; the impl is either a real Firebase one (if
 * `FirebaseInit.isAvailable` AND the FIREBASE_BACKEND flag is on) or a
 * stub that returns frozen sample data.
 *
 * Add new methods here as Stage 3 social features land — never call
 * Firebase classes directly from UI / ViewModels.
 */
interface BackendRepository {

    /**
     * Stable user id. With Firebase enabled this is the FirebaseAuth uid;
     * with the stub it's a per-install UUID stored in DataStore.
     */
    val uid: Flow<String?>

    /** Sign in anonymously (or no-op for the stub). */
    suspend fun ensureSignedIn()

    /** Payment-router remote document. See payment-architecture.md. */
    val paymentConfig: Flow<PaymentConfig>

    /** Groups the user is in (SOCIAL-001). */
    val myGroups: Flow<List<Group>>

    suspend fun createGroup(name: String): Result<Group>
    suspend fun joinGroup(inviteCode: String): Result<Group>
    suspend fun leaveGroup(groupId: String): Result<Unit>

    /** Dual-streak counter for a specific friend pair (SOCIAL-025). */
    fun dualStreak(otherUid: String): Flow<DualStreak>

    /** Send a "disappointment" ping (SOCIAL-005). One per week per pair, enforced server-side. */
    suspend fun sendDisappointment(toUid: String, worstStat: String): Result<Unit>

    /** Chronological feed (SOCIAL-016): your posts + groups + followed users. */
    val feed: Flow<List<Post>>

    /** Post a story / pre-commitment / quiet brag (SOCIAL-021, SOCIAL-033, SOCIAL-036). */
    suspend fun addPost(text: String, kind: PostKind, expiresAtMs: Long? = null): Result<Post>

    suspend fun removePost(postId: String): Result<Unit>

    /** Vote in a group uninstall request (SOCIAL-002). */
    suspend fun voteUninstall(groupId: String, allow: Boolean): Result<Unit>

    /** Disappointment pings RECEIVED (SOCIAL-020). */
    val disappointmentInbox: Flow<List<Disappointment>>

    suspend fun markDisappointmentRead(id: String): Result<Unit>

    /** Time donation (SOCIAL-035) — gift saved minutes to a friend's bank. */
    suspend fun donateTime(toUid: String, minutes: Int, note: String): Result<Unit>

    /** Sponsor system (SOCIAL-008). */
    val sponsor: Flow<Sponsor?>
    suspend fun setSponsor(uid: String, name: String): Result<Unit>
    suspend fun clearSponsor(): Result<Unit>

    /** Couples pair (COUPLES-001). */
    val couplesPartner: Flow<CouplesPartner?>
    suspend fun pairWithPartner(uid: String, name: String): Result<Unit>
    suspend fun unpairPartner(): Result<Unit>

    data class Disappointment(
        val id: String,
        val fromName: String,
        val worstStat: String,
        val createdAtMs: Long,
        val read: Boolean,
    )

    data class Sponsor(val uid: String, val name: String, val sinceMs: Long)
    data class CouplesPartner(val uid: String, val name: String, val sinceMs: Long)

    enum class PostKind { STORY, PRE_COMMIT, QUIET_BRAG, MILESTONE }

    data class Post(
        val id: String,
        val authorUid: String,
        val kind: PostKind,
        val text: String,
        val createdAtMs: Long,
        val expiresAtMs: Long?,
    )

    data class PaymentConfig(
        val nativeOnDevice: Boolean,
        val webFallbackUrl: String,
    )

    data class Group(
        val id: String,
        val name: String,
        val memberCount: Int,
        val sharedStreak: Int,
    )

    data class DualStreak(
        val days: Int,
        val youDoneToday: Boolean,
        val otherDoneToday: Boolean,
    )
}

/**
 * Stub impl. Returns plausible fake data so the UI can render before the
 * Firebase project is provisioned. Persists nothing across process death.
 */
class StubBackendRepository(@Suppress("UNUSED_PARAMETER") context: Context) : BackendRepository {

    private val _uid = MutableStateFlow<String?>("stub-${System.currentTimeMillis()}")
    override val uid: Flow<String?> = _uid.asStateFlow()

    override suspend fun ensureSignedIn() { /* no-op */ }

    private val _paymentConfig = MutableStateFlow(
        BackendRepository.PaymentConfig(
            nativeOnDevice = false,
            webFallbackUrl = "https://built.app/pay",
        )
    )
    override val paymentConfig: Flow<BackendRepository.PaymentConfig> = _paymentConfig.asStateFlow()

    private val _groups = MutableStateFlow<List<BackendRepository.Group>>(emptyList())
    override val myGroups: Flow<List<BackendRepository.Group>> = _groups.asStateFlow()

    override suspend fun createGroup(name: String): Result<BackendRepository.Group> {
        val g = BackendRepository.Group(
            id = "stub-${System.currentTimeMillis()}",
            name = name,
            memberCount = 1,
            sharedStreak = 0,
        )
        _groups.value = _groups.value + g
        return Result.success(g)
    }

    override suspend fun joinGroup(inviteCode: String): Result<BackendRepository.Group> {
        val g = BackendRepository.Group(
            id = "stub-$inviteCode",
            name = "Joined ($inviteCode)",
            memberCount = 2,
            sharedStreak = 0,
        )
        _groups.value = _groups.value + g
        return Result.success(g)
    }

    override suspend fun leaveGroup(groupId: String): Result<Unit> {
        _groups.value = _groups.value.filterNot { it.id == groupId }
        return Result.success(Unit)
    }

    override fun dualStreak(otherUid: String): Flow<BackendRepository.DualStreak> =
        MutableStateFlow(
            BackendRepository.DualStreak(days = 0, youDoneToday = false, otherDoneToday = false)
        ).asStateFlow()

    override suspend fun sendDisappointment(toUid: String, worstStat: String): Result<Unit> {
        // Stub: pretend it sent.
        return Result.success(Unit)
    }

    private val _feed = MutableStateFlow<List<BackendRepository.Post>>(emptyList())
    override val feed: Flow<List<BackendRepository.Post>> = _feed.asStateFlow()

    override suspend fun addPost(
        text: String,
        kind: BackendRepository.PostKind,
        expiresAtMs: Long?,
    ): Result<BackendRepository.Post> {
        val now = System.currentTimeMillis()
        val p = BackendRepository.Post(
            id = "stub-${now}",
            authorUid = _uid.value.orEmpty(),
            kind = kind,
            text = text.trim(),
            createdAtMs = now,
            expiresAtMs = expiresAtMs,
        )
        _feed.value = listOf(p) + _feed.value
        return Result.success(p)
    }

    override suspend fun removePost(postId: String): Result<Unit> {
        _feed.value = _feed.value.filterNot { it.id == postId }
        return Result.success(Unit)
    }

    override suspend fun voteUninstall(groupId: String, allow: Boolean): Result<Unit> {
        // Stub: no-op; real impl posts to /groups/{groupId}/uninstallVotes/{uid}
        return Result.success(Unit)
    }

    private val _inbox = MutableStateFlow<List<BackendRepository.Disappointment>>(
        // Seed with one sample so the inbox isn't lifeless on first open.
        listOf(
            BackendRepository.Disappointment(
                id = "seed-1",
                fromName = "sample",
                worstStat = "2h 34m in tiktok yesterday.",
                createdAtMs = System.currentTimeMillis() - 86_400_000L,
                read = false,
            )
        )
    )
    override val disappointmentInbox: Flow<List<BackendRepository.Disappointment>> = _inbox.asStateFlow()

    override suspend fun markDisappointmentRead(id: String): Result<Unit> {
        _inbox.value = _inbox.value.map { if (it.id == id) it.copy(read = true) else it }
        return Result.success(Unit)
    }

    override suspend fun donateTime(toUid: String, minutes: Int, note: String): Result<Unit> {
        // Stub: pretend it landed in their bank.
        return Result.success(Unit)
    }

    private val _sponsor = MutableStateFlow<BackendRepository.Sponsor?>(null)
    override val sponsor: Flow<BackendRepository.Sponsor?> = _sponsor.asStateFlow()

    override suspend fun setSponsor(uid: String, name: String): Result<Unit> {
        _sponsor.value = BackendRepository.Sponsor(uid, name, System.currentTimeMillis())
        return Result.success(Unit)
    }

    override suspend fun clearSponsor(): Result<Unit> {
        _sponsor.value = null
        return Result.success(Unit)
    }

    private val _partner = MutableStateFlow<BackendRepository.CouplesPartner?>(null)
    override val couplesPartner: Flow<BackendRepository.CouplesPartner?> = _partner.asStateFlow()

    override suspend fun pairWithPartner(uid: String, name: String): Result<Unit> {
        _partner.value = BackendRepository.CouplesPartner(uid, name, System.currentTimeMillis())
        return Result.success(Unit)
    }

    override suspend fun unpairPartner(): Result<Unit> {
        _partner.value = null
        return Result.success(Unit)
    }
}
