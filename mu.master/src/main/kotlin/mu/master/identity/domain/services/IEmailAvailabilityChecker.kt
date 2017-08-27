package mu.master.identity.domain.services

interface IEmailAvailabilityChecker {
    fun isEmailAvailable(email: String): Boolean
}

