package mu.master.identity.views

import mu.libs.cqrs.AllConsumingView
import mu.libs.cqrs.IEvent
import mu.libs.cqrs.store.IEventStore
import mu.master.identity.application.api.UserDTO
import mu.master.identity.domain.UserAccountRegistered
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class IdentityView(eventStore: IEventStore) : IIdentityView, AllConsumingView(eventStore) {
    private val _userAccountsById = ConcurrentHashMap<UUID, UserDTO>()
    private val _userAccountsByEmail = ConcurrentHashMap<String, UserDTO>()

    override val userAccountsById: Map<UUID, UserDTO> = _userAccountsById
    override val userAccountsByEmail: Map<String, UserDTO> = _userAccountsByEmail

    override fun handle(event: IEvent) {
        when (event) {
            is UserAccountRegistered -> {
                val userDto = UserDTO(
                        id = event.userId.value,
                        displayName = event.displayName,
                        email = event.email)

                _userAccountsById.put(userDto.id, userDto)
                _userAccountsByEmail.put(userDto.email, userDto)
            }
        }
    }
}