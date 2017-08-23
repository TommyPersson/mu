package mu.master.teams_and_users.infrastructure

import mu.libs.cqrs.AbstractRepository
import mu.master.DI
import mu.master.teams_and_users.domain.ITeamRepository
import mu.master.teams_and_users.domain.Team

class TeamRepository : ITeamRepository, AbstractRepository<Team>(DI.Common.eventStore, { Team() })