package mu.master.teams_and_users.domain

import java.util.*


data class UserId(val value: UUID) {
    override fun toString(): String {
        return value.toString()
    }
}

data class TeamId(val value: UUID) {
    companion object {
        fun create(): TeamId {
            return TeamId(UUID.randomUUID())
        }
    }

    override fun toString(): String {
        return value.toString()
    }
}

data class Password(val argon2Hash: String, val salt: String)