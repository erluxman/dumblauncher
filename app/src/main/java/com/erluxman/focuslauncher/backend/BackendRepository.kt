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
}
