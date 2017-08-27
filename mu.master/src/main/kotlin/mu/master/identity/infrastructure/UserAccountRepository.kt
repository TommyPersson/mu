package mu.master.identity.infrastructure

import mu.libs.cqrs.AbstractRepository
import mu.libs.cqrs.store.IEventStore
import mu.master.identity.domain.IUserAccountRepository
import mu.master.identity.domain.UserAccount

class UserAccountRepository(eventStore: IEventStore) :
        IUserAccountRepository,
        AbstractRepository<UserAccount>(eventStore, { UserAccount() })