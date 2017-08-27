package mu.master.identity.application

import mu.libs.cqrs.ICommand
import mu.master.identity.domain.UserId

data class RegisterUserAccountCommand(
        val userId: UserId,
        val displayName: String,
        val email: String,
        val password: String
) : ICommand