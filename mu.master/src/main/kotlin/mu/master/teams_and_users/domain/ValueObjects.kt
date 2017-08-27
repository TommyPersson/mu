package mu.master.teams_and_users.domain

import java.util.*

abstract class EntityId(
        val value: UUID
) {
    override fun toString(): String {
        return "${javaClass.simpleName}($value)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EntityId

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

class UserId(value: UUID) : EntityId(value)

class TeamId(value: UUID) : EntityId(value) {
    companion object {
        fun new(): TeamId {
            return TeamId(UUID.randomUUID())
        }
    }
}

data class Password(val argon2Hash: String, val salt: String)