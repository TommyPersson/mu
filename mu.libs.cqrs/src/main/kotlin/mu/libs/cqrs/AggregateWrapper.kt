package mu.libs.cqrs

data class AggregateWrapper<out TAggregate : AggregateRoot>(
        val aggregate: TAggregate,
        val version: Int)