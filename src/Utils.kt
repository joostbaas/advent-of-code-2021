import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.system.exitProcess
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

@OptIn(ExperimentalTime::class)
fun <T> doRun(name: String, expected: T? = null, block: () -> T) {
    measureTimedValue {
        block.invoke()
    }.let { (actual, duration): TimedValue<T> ->
        val status = when (expected) {
            null -> "33m?" // yellow
            actual -> "32mV" // green
            else -> "31mX" // red
        }.let {
            "\u001B[${it}\u001B[0m"
        }
        println("[$status] $name: ${actual} (took ${duration.inWholeMilliseconds}ms)")
        expected?.let {
            if (actual != expected) exitProcess(-1)
        }
    }
}

class CountingMap<T> : Map<T, Long> {
    private val backingMap = mutableMapOf<T, Long>()
    override val entries: Set<Map.Entry<T, Long>> by backingMap::entries
    override val keys: Set<T> by backingMap::keys
    override val size: Int by backingMap::size
    override val values: Collection<Long> by backingMap::values

    override fun containsKey(key: T): Boolean = backingMap.containsKey(key)
    override fun containsValue(value: Long): Boolean = backingMap.containsValue(value)
    override fun get(key: T): Long? = backingMap[key]
    override fun isEmpty(): Boolean = backingMap.isEmpty()

    fun addOne(key: T): CountingMap<T> {
        val current = this.getOrDefault(key, 0L)
        backingMap[key] = current + 1L
        return this
    }

    fun removeOne(key: T): CountingMap<T> {
        val current = this.getOrDefault(key, 0L)
        backingMap[key] = current - 1L
        return this
    }

    operator fun plus(other: CountingMap<T>): CountingMap<T> {
        (this.keys + other.keys).forEach {
            backingMap[it] = this.getOrDefault(it, 0L) + other.getOrDefault(it, 0L)
        }
        return this
    }
}
