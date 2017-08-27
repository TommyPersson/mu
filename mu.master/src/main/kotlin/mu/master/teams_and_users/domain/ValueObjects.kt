package mu.master.teams_and_users.domain

import mu.master.common.EntityId
import java.util.*

class UserId(value: UUID) : EntityId(value)

class TeamId(value: UUID) : EntityId(value) {
    companion object {
        fun new(): TeamId {
            return TeamId(UUID.randomUUID())
        }
    }
}