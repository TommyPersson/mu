package mu.libs.utils

import java.time.ZonedDateTime

interface IClock {
    fun utcNow(): ZonedDateTime
}