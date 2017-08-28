package mu.master.teams_and_users.domain.services

import mu.master.teams_and_users.domain.UserId

interface IUserExistenceChecker {
    fun doesUserExist(userId: UserId): Boolean
}

