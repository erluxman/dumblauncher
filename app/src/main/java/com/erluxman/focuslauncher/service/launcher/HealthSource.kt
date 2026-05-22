package com.erluxman.focuslauncher.service.launcher

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/**
 * INTEG-001 / FIT-001 / SLEEP-001 HealthConnect reader.
 *
 * Reads today's step count and last night's sleep total. Returns zeros when
 * HealthConnect isn't installed or permission isn't granted — the rest of the
 * app shouldn't have to care.
 */
object HealthSource {

    val REQUIRED_PERMISSIONS = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class)
    )

    fun availability(context: Context): Int =
        HealthConnectClient.getSdkStatus(context)

    fun isAvailable(context: Context): Boolean =
        availability(context) == HealthConnectClient.SDK_AVAILABLE

    suspend fun hasPermissions(context: Context): Boolean {
        if (!isAvailable(context)) return false
        val client = HealthConnectClient.getOrCreate(context)
        return client.permissionController.getGrantedPermissions()
            .containsAll(REQUIRED_PERMISSIONS)
    }

    suspend fun todaySteps(context: Context): Int {
        if (!isAvailable(context)) return 0
        val client = runCatching { HealthConnectClient.getOrCreate(context) }.getOrNull() ?: return 0
        val zone = ZoneId.systemDefault()
        val startOfDay = LocalDate.now(zone).atStartOfDay(zone).toInstant()
        val now = Instant.now()
        return try {
            val response = client.aggregate(
                AggregateRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startOfDay, now)
                )
            )
            (response[StepsRecord.COUNT_TOTAL] ?: 0L).toInt()
        } catch (_: Throwable) { 0 }
    }

    suspend fun lastNightSleepMinutes(context: Context): Int {
        if (!isAvailable(context)) return 0
        val client = runCatching { HealthConnectClient.getOrCreate(context) }.getOrNull() ?: return 0
        // Window: noon-yesterday → noon-today catches one nightly sleep block.
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)
        val start = today.minusDays(1).atTime(12, 0).atZone(zone).toInstant()
        val end = today.atTime(12, 0).atZone(zone).toInstant()
        return try {
            val response = client.readRecords(
                ReadRecordsRequest(
                    recordType = SleepSessionRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(start, end)
                )
            )
            response.records.sumOf {
                ChronoUnit.MINUTES.between(it.startTime, it.endTime)
            }.toInt()
        } catch (_: Throwable) { 0 }
    }
}
