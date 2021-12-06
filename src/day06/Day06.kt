package day06

import readInput

fun main() {

    fun countGenerations(startingGenerations: Map<Int, Long>, g: Int): Long =
        (1..g).fold(startingGenerations) { acc, _ ->
            acc.flatMap { (age, size) ->
                when (age) {
                    0 -> listOf(6 to size, 8 to size)
                    else -> listOf(age - 1 to size)
                }
            }.groupBy { it.first }
                .map { (key, entries) -> key to entries.sumOf { it.second } }
                .toMap()
        }.toList().sumOf { it.second }

    fun parseInput(input: List<String>): Map<Int, Long> {
        val startingGenerations: Map<Int, Long> = input[0].split(",")
            .map { it.toInt() }
            .groupBy { it }
            .map { (k, v) -> k to v.size.toLong() }
            .toMap()
        return startingGenerations
    }

    fun part1(input: List<String>): Int = countGenerations(parseInput(input), 80).toInt()

    fun part2(input: List<String>): Long = countGenerations(parseInput(input), 256)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day06/testInput")
    check(part1(testInput) == 5934)
    val input = readInput("day06/input")
    println(part1(input))
    check(part1(input) == 345793)

    println(part2(testInput))
    check(part2(testInput) == 26984457539)

    println(part2(input))
    check(part2(input) == 1572643095893)
}
