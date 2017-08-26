package mu.master.teams_and_users.domain

import java.util.*

abstract class EntityId(
        val value: UUID
) {
    override fun toString(): String {
        return "${javaClass.simpleName}($value)"
    }
}

class UserId(value: UUID) : EntityId(value)

class TeamId(value: UUID) : EntityId(value) {
    companion object {
        fun create(): TeamId {
            return TeamId(UUID.randomUUID())
        }
    }
}

data class Password(val argon2Hash: String, val salt: String)