package com.academy.manager.util

import java.security.MessageDigest
import java.security.SecureRandom

object PinHasher {

    private const val SALT_BYTES = 16

    fun generateSalt(): String {
        val bytes = ByteArray(SALT_BYTES)
        SecureRandom().nextBytes(bytes)
        return bytes.toHex()
    }

    fun hashPin(pin: String, saltHex: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val input = "$saltHex:$pin".toByteArray(Charsets.UTF_8)
        return digest.digest(input).toHex()
    }

    fun verifyPin(pin: String, saltHex: String, expectedHashHex: String): Boolean {
        val computed = hashPin(pin, saltHex).toByteArray(Charsets.UTF_8)
        val expected = expectedHashHex.toByteArray(Charsets.UTF_8)
        return MessageDigest.isEqual(computed, expected)
    }

    private fun ByteArray.toHex(): String =
        joinToString(separator = "") { byte -> "%02x".format(byte) }
}
