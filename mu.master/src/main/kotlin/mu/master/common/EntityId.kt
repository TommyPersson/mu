package mu.master.common

import java.util.*

abstract class EntityId(
        val value: UUID
) {
    override fun toString(): String {
        return "${javaClass.simpleName}($value)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EntityId

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}