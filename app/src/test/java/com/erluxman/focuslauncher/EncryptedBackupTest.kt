package com.erluxman.focuslauncher
import com.erluxman.focuslauncher.service.launcher.EncryptedBackup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EncryptedBackupTest {

    @Test
    fun roundTrip_recoversPlaintext() {
        val plain = """{"why":"build","streak":3}"""
        val pass = "twenty_chars_or_more_passphrase".toCharArray()
        val ct = EncryptedBackup.encrypt(plain, pass)
        assertEquals(plain, EncryptedBackup.decrypt(ct, pass))
    }

    @Test
    fun wrongPassphrase_fails() {
        val plain = "hello"
        val ct = EncryptedBackup.encrypt(plain, "right".toCharArray())
        var threw = false
        try {
            EncryptedBackup.decrypt(ct, "wrong".toCharArray())
        } catch (_: Exception) {
            threw = true
        }
        assertTrue(threw)
    }

    @Test
    fun payload_hasFourPipeSeparatedFields() {
        val ct = EncryptedBackup.encrypt("x", "p".toCharArray())
        assertEquals(4, ct.split("|").size)
        assertTrue(ct.startsWith(EncryptedBackup.HEADER + "|"))
    }
}
