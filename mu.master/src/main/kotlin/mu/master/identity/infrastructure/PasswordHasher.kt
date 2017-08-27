package mu.master.identity.infrastructure

import de.mkammerer.argon2.Argon2Factory
import mu.master.identity.domain.PasswordHash
import mu.master.identity.domain.services.IPasswordHasher
import java.security.MessageDigest
import java.util.*

class PasswordHasher : IPasswordHasher {

    private val argon2 = Argon2Factory.create()
    private val sha1 = MessageDigest.getInstance("SHA-1")

    override fun createPasswordHash(plainPassword: String): PasswordHash {
        val salt = createRandomSalt()
        val hash = calculateHash(salt, plainPassword)

        return PasswordHash(salt, hash)
    }

    override fun verifyHash(plainPassword: String, passwordHash: PasswordHash): Boolean {
        return argon2.verify(passwordHash.hash, passwordHash.salt + plainPassword)
    }

    private fun calculateHash(salt: String, plainPassword: String): String {
        return argon2.hash(3, 12, 1, salt + plainPassword)
    }

    private fun createRandomSalt(): String {
        val randomInput = UUID.randomUUID().toString().toByteArray()
        val sha1Bytes = sha1.digest(randomInput)
        return Base64.getEncoder().encodeToString(sha1Bytes)
    }
}