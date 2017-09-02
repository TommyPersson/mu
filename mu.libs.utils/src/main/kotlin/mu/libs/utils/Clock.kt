package mu.libs.utils

import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

class Clock : IClock {
    override fun utcNow(): ZonedDateTime {
        return ZonedDateTime.now(ZoneOffset.UTC)
    }
}

fun ZonedDateTime.toClassicDate() = Date.from(this.toInstant())