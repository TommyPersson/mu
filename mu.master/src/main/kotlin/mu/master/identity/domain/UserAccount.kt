package mu.master.identity.domain

import mu.libs.cqrs.AggregateRoot
import mu.libs.cqrs.AggregateRootId
import mu.libs.cqrs.IEvent
import mu.master.identity.domain.services.IEmailAvailabilityChecker
import mu.master.identity.domain.services.IPasswordHasher

class UserAccount() : AggregateRoot() {

    private lateinit var _id: UserId

    private var passwordHash: PasswordHash? = null

    override val id: AggregateRootId
        get() = AggregateRootId(_id.value)

    constructor(
            userId: UserId,
            displayName: String,
            email: String,
            plainPassword: String,
            emailAvailabilityChecker: IEmailAvailabilityChecker,
            passwordHasher: IPasswordHasher) : this() {

        if (!emailAvailabilityChecker.isEmailAvailable(email)) {
            throw IllegalArgumentException("Email is not available")
        }

        val passwordHash = passwordHasher.createPasswordHash(plainPassword)

        applyChange(UserAccountRegistered(userId, displayName = displayName, email = email, passwordHash = passwordHash))
    }

    fun authenticate(password: String, passwordHasher: IPasswordHasher): Boolean {
        val passwordHash = passwordHash ?: throw Exception("User has no password")

        return passwordHasher.verifyHash(password, passwordHash)
    }

    override fun apply(event: IEvent) {
        when (event) {
            is UserAccountRegistered -> {
                _id = event.userId
                passwordHash = event.passwordHash
            }
        }
    }
}