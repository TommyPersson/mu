package mu.master.identity.application.services

import mu.master.identity.domain.UserId

interface IIdentityService {
    fun doesUserExist(userId: UserId): Boolean
}