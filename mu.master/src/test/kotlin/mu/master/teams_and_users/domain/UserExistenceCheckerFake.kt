package mu.master.teams_and_users.domain

import mu.master.teams_and_users.domain.services.IUserExistenceChecker

class UserExistenceCheckerFake(vararg userIds: UserId) : IUserExistenceChecker {
    private val users = mutableListOf(*userIds)

    fun reset(vararg userIds: UserId) {
        users.clear()
        users.addAll(userIds)
    }

    override fun doesUserExist(userId: UserId): Boolean {
        return users.contains(userId)
    }
}