package mu.master.identity.infrastructure

import mu.master.identity.domain.services.IEmailAvailabilityChecker
import mu.master.identity.views.IIdentityView

class EmailAvailabilityChecker(
        private val identityView: IIdentityView
) : IEmailAvailabilityChecker {

    override fun isEmailAvailable(email: String): Boolean {
        return identityView.userAccountsByEmail[email] == null
    }
}