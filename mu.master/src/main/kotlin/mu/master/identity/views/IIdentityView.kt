package mu.master.identity.views

import mu.libs.cqrs.IView
import mu.master.identity.application.api.UserDTO
import java.util.*

interface IIdentityView : IView {
    val userAccountsById: Map<UUID, UserDTO>
    val userAccountsByEmail: Map<String, UserDTO>
    val passwordHashesByEmail: Map<String, String>
}