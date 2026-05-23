package com.erluxman.focuslauncher.backend

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

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

    /** Phone Sabbath shared schedule (COUPLES-002). */
    val phoneSabbathAt: Flow<Long?>
    suspend fun scheduleSabbath(atMs: Long): Result<Unit>
    suspend fun cancelSabbath(): Result<Unit>

    /** Best Friends list (SOCIAL-039). Stub keeps whatever was added. */
    val bestFriends: Flow<List<Friend>>
    suspend fun addBestFriend(uid: String, name: String): Result<Unit>
    suspend fun removeBestFriend(uid: String): Result<Unit>

    /** Parent/Child pair (FAMILY-001). */
    val familyPair: Flow<FamilyPair?>
    suspend fun pairFamily(uid: String, name: String, role: FamilyRole): Result<Unit>
    suspend fun unpairFamily(): Result<Unit>

    /** Active uninstall vote per group (SOCIAL-002). */
    fun uninstallVote(groupId: String): Flow<UninstallVote?>
    suspend fun requestUninstallVote(groupId: String, reason: String): Result<Unit>

    /** Money stake (FINANCE-001). Single active stake in the stub. */
    val moneyStake: Flow<MoneyStake?>
    suspend fun createMoneyStake(amountUsd: Int, daysCommitted: Int, charity: String): Result<Unit>
    suspend fun cancelMoneyStake(): Result<Unit>

    /** Focus duel (SOCIAL-034). */
    val activeDuel: Flow<FocusDuel?>
    suspend fun challengeFocusDuel(otherUid: String, otherName: String, durationMin: Int): Result<Unit>
    suspend fun cancelFocusDuel(): Result<Unit>

    /** Hashtag tracks (SOCIAL-019). */
    val joinedHashtags: Flow<Set<String>>
    suspend fun joinHashtag(tag: String): Result<Unit>
    suspend fun leaveHashtag(tag: String): Result<Unit>

    data class UninstallVote(val groupId: String, val reason: String, val createdAtMs: Long, val ayes: Int, val nays: Int)
    data class MoneyStake(val amountUsd: Int, val daysCommitted: Int, val charity: String, val startedAtMs: Long)
    data class FocusDuel(val otherUid: String, val otherName: String, val durationMin: Int, val startedAtMs: Long)

    enum class FamilyRole { PARENT, CHILD }
    data class Friend(val uid: String, val name: String, val addedMs: Long)
    data class FamilyPair(val uid: String, val name: String, val role: FamilyRole, val sinceMs: Long)

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

    private val _sabbath = MutableStateFlow<Long?>(null)
    override val phoneSabbathAt: Flow<Long?> = _sabbath.asStateFlow()

    override suspend fun scheduleSabbath(atMs: Long): Result<Unit> {
        _sabbath.value = atMs
        return Result.success(Unit)
    }

    override suspend fun cancelSabbath(): Result<Unit> {
        _sabbath.value = null
        return Result.success(Unit)
    }

    private val _bestFriends = MutableStateFlow<List<BackendRepository.Friend>>(emptyList())
    override val bestFriends: Flow<List<BackendRepository.Friend>> = _bestFriends.asStateFlow()

    override suspend fun addBestFriend(uid: String, name: String): Result<Unit> {
        if (_bestFriends.value.size >= 8) return Result.failure(IllegalStateException("max 8 best friends"))
        if (_bestFriends.value.any { it.uid == uid }) return Result.success(Unit)
        _bestFriends.value = _bestFriends.value + BackendRepository.Friend(uid, name, System.currentTimeMillis())
        return Result.success(Unit)
    }

    override suspend fun removeBestFriend(uid: String): Result<Unit> {
        _bestFriends.value = _bestFriends.value.filterNot { it.uid == uid }
        return Result.success(Unit)
    }

    private val _family = MutableStateFlow<BackendRepository.FamilyPair?>(null)
    override val familyPair: Flow<BackendRepository.FamilyPair?> = _family.asStateFlow()

    override suspend fun pairFamily(uid: String, name: String, role: BackendRepository.FamilyRole): Result<Unit> {
        _family.value = BackendRepository.FamilyPair(uid, name, role, System.currentTimeMillis())
        return Result.success(Unit)
    }

    override suspend fun unpairFamily(): Result<Unit> {
        _family.value = null
        return Result.success(Unit)
    }

    private val _votes = MutableStateFlow<Map<String, BackendRepository.UninstallVote>>(emptyMap())
    override fun uninstallVote(groupId: String): Flow<BackendRepository.UninstallVote?> =
        _votes.map { it[groupId] }

    override suspend fun requestUninstallVote(groupId: String, reason: String): Result<Unit> {
        _votes.value = _votes.value + (groupId to BackendRepository.UninstallVote(
            groupId, reason, System.currentTimeMillis(), ayes = 0, nays = 0,
        ))
        return Result.success(Unit)
    }

    private val _stake = MutableStateFlow<BackendRepository.MoneyStake?>(null)
    override val moneyStake: Flow<BackendRepository.MoneyStake?> = _stake.asStateFlow()

    override suspend fun createMoneyStake(amountUsd: Int, daysCommitted: Int, charity: String): Result<Unit> {
        if (amountUsd <= 0 || daysCommitted <= 0) return Result.failure(IllegalArgumentException("invalid stake"))
        _stake.value = BackendRepository.MoneyStake(amountUsd, daysCommitted, charity.trim(), System.currentTimeMillis())
        return Result.success(Unit)
    }

    override suspend fun cancelMoneyStake(): Result<Unit> {
        _stake.value = null
        return Result.success(Unit)
    }

    private val _duel = MutableStateFlow<BackendRepository.FocusDuel?>(null)
    override val activeDuel: Flow<BackendRepository.FocusDuel?> = _duel.asStateFlow()

    override suspend fun challengeFocusDuel(otherUid: String, otherName: String, durationMin: Int): Result<Unit> {
        _duel.value = BackendRepository.FocusDuel(otherUid, otherName, durationMin, System.currentTimeMillis())
        return Result.success(Unit)
    }

    override suspend fun cancelFocusDuel(): Result<Unit> {
        _duel.value = null
        return Result.success(Unit)
    }

    private val _hashtags = MutableStateFlow<Set<String>>(emptySet())
    override val joinedHashtags: Flow<Set<String>> = _hashtags.asStateFlow()

    override suspend fun joinHashtag(tag: String): Result<Unit> {
        _hashtags.value = _hashtags.value + tag.trim().removePrefix("#")
        return Result.success(Unit)
    }

    override suspend fun leaveHashtag(tag: String): Result<Unit> {
        _hashtags.value = _hashtags.value - tag.trim().removePrefix("#")
        return Result.success(Unit)
    }
}
