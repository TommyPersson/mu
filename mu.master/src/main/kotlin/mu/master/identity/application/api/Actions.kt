package mu.master.identity.application.api

import mu.master.DI
import mu.master.identity.application.RegisterUserAccountCommand
import mu.master.identity.application.identityContextCommandHandlers
import mu.master.identity.domain.UserId
import mu.master.utils.createActions

val identityContextActions = createActions {

    val identityService = DI.Identity.identityService

    actionOf<RegisterUserAccountRequestDTO, RegisterUserAccountResponseDTO>("users.register", requiresAuthentication = false) { input, _ ->
        val newUserId = UserId.new()
        val command = RegisterUserAccountCommand(newUserId, displayName = input.displayName, email = input.email, password = input.password)

        identityContextCommandHandlers.handle(command)

        RegisterUserAccountResponseDTO(newUserId.value)
    }

    actionOf<CreateAuthTokenRequestDTO, CreateAuthTokenResponseDTO>("auth.token.create", requiresAuthentication = false) { input, _ ->
        val token = identityService.createAuthenticationToken(input.email, input.password) ?: throw IllegalArgumentException("Invalid credentials")

        CreateAuthTokenResponseDTO(token.value)
    }
}