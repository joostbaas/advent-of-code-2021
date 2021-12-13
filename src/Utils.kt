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
fun doRun(name: String, expected: Int? = null, block: () -> Int) {
    measureTimedValue {
        block.invoke()
    }.let { (actual, duration): TimedValue<Int> ->
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