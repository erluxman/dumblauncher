package com.erluxman.focuslauncher.service

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * BACKUP-001 Encrypted Local Backup.
 *
 * Wraps the JSON export in AES-256-GCM, with the key derived from the user's
 * passphrase via PBKDF2 (200k iterations, 256-bit key). Output is a single
 * base64 line prefixed with [HEADER], salt, and nonce.
 *
 * Format (text):  fl1|<base64 salt>|<base64 nonce>|<base64 ciphertext+tag>
 */
object EncryptedBackup {

    const val HEADER = "fl1"
    private const val ALGO = "AES/GCM/NoPadding"
    private const val KDF = "PBKDF2WithHmacSHA256"
    private const val ITERATIONS = 200_000
    private const val KEY_BITS = 256
    private const val SALT_LEN = 16
    private const val NONCE_LEN = 12
    private const val TAG_BITS = 128

    fun encrypt(plain: String, passphrase: CharArray): String {
        val rng = SecureRandom()
        val salt = ByteArray(SALT_LEN).also { rng.nextBytes(it) }
        val nonce = ByteArray(NONCE_LEN).also { rng.nextBytes(it) }
        val key = deriveKey(passphrase, salt)
        val cipher = Cipher.getInstance(ALGO).apply {
            init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(TAG_BITS, nonce))
        }
        val ct = cipher.doFinal(plain.toByteArray(Charsets.UTF_8))
        val b64 = { bytes: ByteArray -> Base64.getEncoder().encodeToString(bytes) }
        return "$HEADER|${b64(salt)}|${b64(nonce)}|${b64(ct)}"
    }

    fun decrypt(payload: String, passphrase: CharArray): String {
        val parts = payload.split("|")
        require(parts.size == 4 && parts[0] == HEADER) { "Invalid backup payload" }
        val decoder = Base64.getDecoder()
        val salt = decoder.decode(parts[1])
        val nonce = decoder.decode(parts[2])
        val ct = decoder.decode(parts[3])
        val key = deriveKey(passphrase, salt)
        val cipher = Cipher.getInstance(ALGO).apply {
            init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(TAG_BITS, nonce))
        }
        return cipher.doFinal(ct).toString(Charsets.UTF_8)
    }

    private fun deriveKey(passphrase: CharArray, salt: ByteArray): SecretKey {
        val factory = SecretKeyFactory.getInstance(KDF)
        val spec = PBEKeySpec(passphrase, salt, ITERATIONS, KEY_BITS)
        val raw = factory.generateSecret(spec).encoded
        return SecretKeySpec(raw, "AES")
    }
}
