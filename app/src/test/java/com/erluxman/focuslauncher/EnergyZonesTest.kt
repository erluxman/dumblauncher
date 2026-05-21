package com.erluxman.focuslauncher

import com.erluxman.focuslauncher.service.EnergyZones
import org.junit.Assert.assertEquals
import org.junit.Test

class EnergyZonesTest {

    @Test
    fun windowIndex_bucketsByHour() {
        assertEquals(0, EnergyZones.windowIndex(0))
        assertEquals(0, EnergyZones.windowIndex(3))
        assertEquals(1, EnergyZones.windowIndex(4))
        assertEquals(5, EnergyZones.windowIndex(23))
    }

    @Test
    fun activeEnergy_readsStoredEntry() {
        val stored = setOf("8-12|HIGH", "12-16|MED")
        assertEquals(EnergyZones.Energy.HIGH, EnergyZones.activeEnergy(10, stored))
        assertEquals(EnergyZones.Energy.MED, EnergyZones.activeEnergy(13, stored))
        assertEquals(EnergyZones.Energy.UNSET, EnergyZones.activeEnergy(2, stored))
    }

    @Test
    fun suggestion_changesByEnergy() {
        val high = EnergyZones.suggestion(EnergyZones.Energy.HIGH)
        val low = EnergyZones.suggestion(EnergyZones.Energy.LOW)
        assert(high != low)
    }
}
