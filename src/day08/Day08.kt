package day08

import readInput
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

val linePattern = """(([a-g]+ ?)+) \| (([a-g]+ ?)+)""".toRegex()

@OptIn(ExperimentalTime::class)
fun main() {
    val uniqueSegmentLengths = setOf(2, 4, 3, 7)

    fun part1(input: List<String>): Int {
        val regex = """.+ \| (([a-g]+ ?)+)""".toRegex()
        return input.flatMap {
            val (output) = regex.find(it)?.destructured!!
            output.split(" ").filter { it.length in uniqueSegmentLengths }
        }.count()
    }

    fun part2(input: List<String>): Int =
        input.sumOf { line ->
            val lookupTable: (String) -> Int = createLookupTable(line)
            val (_, _, numbersOnTheRight) = linePattern.find(line)?.destructured!!
            numbersOnTheRight.split(" ")
                .map(lookupTable)
                .joinToString(separator = "")
                .toInt()
        }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day08/testInput")
    println(part1(testInput))
    check(part1(testInput) == 26)
    val input = readInput("day08/input")
    println(part1(input))
    check(part1(input) == 375)

    println(part2(testInput))
    check(part2(testInput) == 61229)

    val (part2Answer, duration) = measureTimedValue {
        part2(input)
    }
    println(part2Answer)
    println("took ${duration.inWholeMilliseconds} milis")
    check(part2Answer == 1019355)
}


fun createLookupTable(line: String): (String) -> Int {
    val map = deduceLine(line)
    fun String.sort() = this.toList().sortedBy { it }.joinToString(separator = "")
    return { chars ->
        val translated = chars.map { map[it] }.joinToString(separator = "").sort()
        when (translated) {
            "abcefg" -> 0
            "cf" -> 1
            "acdeg" -> 2
            "acdfg" -> 3
            "bcdf" -> 4
            "abdfg" -> 5
            "abdefg" -> 6
            "acf" -> 7
            "abcdefg" -> 8
            "abcdfg" -> 9
            else -> throw IllegalArgumentException()
        }
    }
}

fun deduceLine(line: String): Map<Char, Char> {
    val (numbersOnTheLeft) = linePattern.find(line)?.destructured!!
    val countToChar = countTotalOccurrences(numbersOnTheLeft)
    numbersOnTheLeft.split(" ")
        .let { numberStrings ->
            val c1 = numberStrings.filter { it.length == 2 }.only().toList()
            val c4 = numberStrings.filter { it.length == 4 }.only().toList()
            val c7 = numberStrings.filter { it.length == 3 }.only().toList()
            val a = c7.minus(c1.toSet()).only()
            val b = countToChar[6]!!.toList().only()
            val c = countToChar[8]!!.filterNot { it == a }.only()
            val d = c4.minus(c1).minus(b).toList().only()
            val e = countToChar[4]!!.toList().only()
            val f = countToChar[9]!!.toList().only()
            val g = countToChar[7]!!.filterNot { it in setOf(a, b, c, d, e, f) }.only()

            return mapOf(
                a to 'a',
                b to 'b',
                c to 'c',
                d to 'd',
                e to 'e',
                f to 'f',
                g to 'g',
            )
        }
}

private fun countTotalOccurrences(numbersOnTheLeft: String): Map<Int, Set<Char>> {
    val countToChar = numbersOnTheLeft.toList().filterNot { it == ' ' }.groupingBy { it }.eachCount().let {
        it.map { it.value to it.key }
            .groupBy { it.first }
            .map { it.key to it.value.map { it.second }.toSet() }
            .toMap()
    }
    return countToChar
}

fun <T> List<T>.only(): T = if (this.size != 1) throw IllegalStateException() else this[0]