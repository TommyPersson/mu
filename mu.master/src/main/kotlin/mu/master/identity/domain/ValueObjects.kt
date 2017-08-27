package mu.master.identity.domain

import mu.master.teams_and_users.domain.EntityId
import java.util.*

class UserId(value: UUID) : EntityId(value) {
    companion object {
        fun new() = UserId(UUID.randomUUID())
    }
}

data class PasswordHash(
    val salt: String,
    val hash: String)