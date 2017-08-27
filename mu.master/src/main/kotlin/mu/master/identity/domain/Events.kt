package mu.master.identity.domain

import mu.libs.cqrs.IEvent

data class UserAccountRegistered(
        val userId: UserId,
        val displayName: String,
        val email: String,
        val passwordHash: PasswordHash
) : IEvent