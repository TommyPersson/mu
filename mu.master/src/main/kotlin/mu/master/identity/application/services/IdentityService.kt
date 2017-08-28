package mu.master.identity.application.services

import mu.master.identity.domain.UserId
import mu.master.identity.views.IIdentityView

class IdentityService(
        private val identityView: IIdentityView
) : IIdentityService {
    override fun doesUserExist(userId: UserId): Boolean {
        return identityView.userAccountsById[userId.value] != null
    }
}