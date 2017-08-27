package mu.master.identity.domain.services

import mu.master.identity.domain.PasswordHash

interface IPasswordHasher {
    fun createPasswordHash(plainPassword: String): PasswordHash
    fun verifyHash(plainPassword: String, passwordHash: PasswordHash): Boolean
}

