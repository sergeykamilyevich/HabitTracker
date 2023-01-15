package com.example.habittracker.core_impl.data.crypto

interface Encryption {

    suspend fun encrypt(bytes: ByteArray?): EncryptionResult

    suspend fun decrypt(encryptedBytes: ByteArray?, iv: ByteArray): ByteArray
}