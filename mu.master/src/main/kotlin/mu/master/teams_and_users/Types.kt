package mu.master.teams_and_users

import java.util.*


data class UserId(val value: UUID)

data class TeamId(val value: UUID) {
    companion object {
        fun create(): TeamId {
            return TeamId(UUID.randomUUID())
        }
    }
}

data class Password(val argon2Hash: String, val salt: String)