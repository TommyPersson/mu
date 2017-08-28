package mu.master.identity.infrastructure

import de.mkammerer.argon2.Argon2Factory
import mu.master.identity.domain.PasswordHash
import mu.master.identity.domain.services.IPasswordHasher

class PasswordHasher : IPasswordHasher {

    private val argon2 = Argon2Factory.create()

    override fun createPasswordHash(plainPassword: String): PasswordHash {
        val hash = calculateHash(plainPassword)

        return PasswordHash(hash)
    }

    override fun verifyHash(plainPassword: String, passwordHash: PasswordHash): Boolean {
        return argon2.verify(passwordHash.value, plainPassword)
    }

    private fun calculateHash(plainPassword: String): String {
        return argon2.hash(3, 12, 1, plainPassword)
    }
}
