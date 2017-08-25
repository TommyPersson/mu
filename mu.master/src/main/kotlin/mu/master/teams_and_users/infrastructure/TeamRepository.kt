package mu.master.teams_and_users.infrastructure

import mu.libs.cqrs.AbstractRepository
import mu.libs.cqrs.store.IEventStore
import mu.master.DI
import mu.master.teams_and_users.domain.ITeamRepository
import mu.master.teams_and_users.domain.Team

class TeamRepository(eventStore: IEventStore) : ITeamRepository, AbstractRepository<Team>(eventStore, { Team() })