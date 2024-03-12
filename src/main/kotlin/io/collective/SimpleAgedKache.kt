package io.collective

import java.time.Clock

class SimpleAgedKache (val clock : Clock = Clock.systemDefaultZone(), var cache : MutableMap<Any?, ExpirableEntry> = hashMapOf()){

    fun put(key: Any?, value: Any?, retentionInMillis: Int) {
        val entry: ExpirableEntry = ExpirableEntry(value, clock.millis() + retentionInMillis);
        cache[key] = entry
    }

    fun isEmpty(): Boolean {
        cleanCache()
        return cache.isEmpty()
    }

    fun size(): Int {
        cleanCache()
        return cache.size
    }

    fun get(key: Any?): Any? {
        cleanCache()
        val entry: ExpirableEntry? = cache[key]
        if (entry != null) {
            return entry.value
        } else {
            return null
        }
    }

    private fun cleanCache() {
        val currentTime: Long = clock.millis()
        println(currentTime)
        for ((key, value) in cache) {
            println(value.expirationTime)
        }
        cache.values.removeIf { it.isExpired(currentTime) }
        println(cache.values)
    }

    inner class ExpirableEntry (val value: Any?, val expirationTime: Long){

        fun isExpired(currentTime: Long): Boolean {
            return currentTime >= expirationTime;
        }

    }
}