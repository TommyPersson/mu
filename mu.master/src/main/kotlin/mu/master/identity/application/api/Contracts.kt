package mu.master.identity.application.api

import java.util.*

data class RegisterUserAccountRequestDTO(
        val displayName: String,
        val email: String,
        val password: String)

data class RegisterUserAccountResponseDTO(
        val id: UUID)

data class CreateAuthTokenRequestDTO(
        val email: String,
        val password: String)

data class CreateAuthTokenResponseDTO(
        val jwt: String)

// TODO unused in api layer, move where?
data class UserDTO(
        val id: UUID,
        val displayName: String,
        val email: String)