package mu.libs.cqrs

import java.util.*


interface IRepository<T> {
    fun getById(id: UUID): T?

    fun update(id: UUID, updateFn: (T) -> Unit)

    fun save(aggregate: T)

}