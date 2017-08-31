package mu.master.teams_and_users.domain

import mu.master.testutils.applyChanges
import mu.master.testutils.from
import mu.master.testutils.hasPublished
import mu.master.testutils.throws
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object TeamSpec : Spek({

    describe("::ctor") {
        val newTeamId = TeamId.new()
        val newTeamName = "Team Name 1"
        val createdBy = UserId.new()
        val teamAdmin = UserId.new()
        val userExistenceChecker = UserExistenceCheckerFake()

        val call = { Team(newTeamId, newTeamName, teamAdmin, createdBy, userExistenceChecker) }

        context("createdBy exists & teamAdmin exists") {
            beforeEachTest { userExistenceChecker.reset(createdBy, teamAdmin) }

            it("publishes TeamCreated") {
                val team = call()

                team hasPublished TeamCreated(newTeamId, teamAdmin, newTeamName, createdBy)
            }
        }

        context("createdBy does not exist") {
            beforeEachTest { userExistenceChecker.reset(teamAdmin) }

            it("throws IllegalArgumentException") {
                call.throws<IllegalArgumentException>()
            }
        }

        context("teamAdmin does not exist") {
            beforeEachTest { userExistenceChecker.reset(createdBy) }

            it("throws IllegalArgumentException") {
                call.throws<IllegalArgumentException>()
            }
        }

        context("neither createdBy nor teamAdmin exists") {
            beforeEachTest { userExistenceChecker.reset() }

            it("throws IllegalArgumentException") {
                call.throws<IllegalArgumentException>()
            }
        }
    }

    describe("::addUser") {
        val teamId = TeamId.new()
        val teamAdmin = UserId.new()
        val notTeamAdmin = UserId.new()
        val newUser = UserId.new()
        val userExistenceChecker = UserExistenceCheckerFake()

        val team = { Team() }.from(TeamCreated(teamId, teamAdmin, "Team Name 1", teamAdmin))

        val call = { byUser: UserId -> team.addUser(newUser, byUser, userExistenceChecker) }

        context("newUser exists & newUser is not on team & byUser is teamAdmin") {
            beforeEachTest { userExistenceChecker.reset(newUser) }

            it("publishes UserAddedToTeam") {
                call(teamAdmin)

                team hasPublished UserAddedToTeam(teamId, newUser, teamAdmin)
            }
        }

        context("byUser is not teamAdmin") {
            beforeEachTest { userExistenceChecker.reset(newUser) }

            it("throws IllegalArgumentException") {
                { call(notTeamAdmin) }.throws<IllegalArgumentException>()
            }
        }

        context("newUser is on team") {
            beforeEachTest {
                userExistenceChecker.reset(newUser)
                team.applyChanges(UserAddedToTeam(teamId, newUser, teamAdmin))
            }

            it("throws IllegalArgumentException") {
                { call(teamAdmin) }.throws<IllegalArgumentException>()
            }
        }

        context("newUser does not exist") {
            beforeEachTest { userExistenceChecker.reset() }

            it("throws IllegalArgumentException") {
                { call(teamAdmin) }.throws<IllegalArgumentException>()
            }
        }
    }

    describe("::removeUser") {
        val teamId = TeamId.new()
        val teamAdmin = UserId.new()
        val notTeamAdmin = UserId.new()
        val aMember = UserId.new()
        val notAMember = UserId.new()

        val team = { Team() }.from(
                TeamCreated(teamId, teamAdmin, "Team Name 1", teamAdmin),
                UserAddedToTeam(teamId, aMember, teamAdmin))

        val call = { user: UserId, byUser: UserId -> team.removeUser(user, byUser) }

        context("user is on team & user is not teamAdmin & byUser is teamAdmin") {
            it("publishes UserAddedToTeam") {
                call(aMember, teamAdmin)

                team hasPublished UserRemovedFromTeam(teamId, aMember, teamAdmin)
            }
        }

        context("byUser is not teamAdmin") {
            it("throws IllegalArgumentException") {
                { call(aMember, notTeamAdmin) }.throws<IllegalArgumentException>()
            }
        }

        context("user is not on team") {
            it("throws IllegalArgumentException") {
                { call(notAMember, teamAdmin) }.throws<IllegalArgumentException>()
            }
        }

        context("user is teamAdmin") {
            it("throws IllegalArgumentException") {
                { call(teamAdmin, teamAdmin) }.throws<IllegalArgumentException>()
            }
        }
    }
})

