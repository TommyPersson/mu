package mu.master.identity.domain

import mu.master.common.EntityId
import java.util.*

class UserId(value: UUID) : EntityId(value) {
    companion object {
        fun new() = UserId(UUID.randomUUID())
    }
}

data class PasswordHash(val value: String)