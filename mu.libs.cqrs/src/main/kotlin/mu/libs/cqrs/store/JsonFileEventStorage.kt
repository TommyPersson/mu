package mu.libs.cqrs.store

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.libs.cqrs.AggregateRootId
import java.io.File
import java.util.*

class JsonFileEventStorage(
        private val file: File
) : IEventStorage {

    private val store = mutableMapOf<AggregateRootId, MutableList<EventEnvelope>>()
    private val allEvents: MutableList<EventEnvelope>

    private val jackson = jacksonObjectMapper().apply {
        enable(SerializationFeature.INDENT_OUTPUT)
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE, JsonTypeInfo.As.PROPERTY)
    }

    init {
        val events = try {
            if (file.exists()) {
                jackson.readValue<Array<EventEnvelope>>(file).toList()
            } else emptyList()
        } catch (e: Exception) {
            println(e)
            emptyList<EventEnvelope>()
        }

        allEvents = Collections.synchronizedList(events.toMutableList())
        allEvents.groupByTo(store, { AggregateRootId(it.aggregateRootId) })
    }

    override fun transact(aggregateRootId: AggregateRootId, transactor: IEventStorage.IEventTransaction.(Int) -> Unit) {
        val events = store.getOrPut(aggregateRootId, { mutableListOf() })

        synchronized(events) {
            val version = events.lastOrNull()?.version ?: 0

            val transaction = object : IEventStorage.IEventTransaction {
                override fun appendToLog(eventEnvelope: EventEnvelope) {
                    synchronized(allEvents) {
                        store.getOrPut(AggregateRootId(eventEnvelope.aggregateRootId), { mutableListOf() }).add(eventEnvelope)
                        allEvents.add(eventEnvelope)
                    }
                }
            }

            transactor(transaction, version)

            persistEventLog()
        }
    }

    override fun getEventsForAggregate(aggregateRootId: AggregateRootId): List<EventEnvelope> {
        return store[aggregateRootId] ?: emptyList()
    }

    override fun getAllEvents(): List<EventEnvelope> {
        return allEvents
    }

    private fun persistEventLog() {
        synchronized(allEvents) {
            if (!file.exists()) {
                file.parentFile.mkdirs()
            }

            jackson.writeValue(file, allEvents)
        }
    }
}