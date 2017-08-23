package mu.libs.cqrs

import java.util.*


interface IRepository<T> {
    fun getById(id: UUID): T?

    fun save(aggregate: T): Unit
}