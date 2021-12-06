package day03

import readInput

fun List<String>.transposed(): List<List<Char>> {
    val length = this[0].length
    return (0 until length).map { i ->
        this.map { binaryString -> binaryString[i] }
    }
}

fun List<Char>.mostCommonChar() =
    this.groupBy { it } // mapOf(0 -> [0,0,0,0], 1 -> [1,1,1,1])
        .toSortedMap { v1, v2 -> v2 - v1 }
        .maxByOrNull { it.value.size }?.key!!

fun List<Char>.leastCommonChar() =
    when (mostCommonChar()) {
        '0' -> '1'
        else -> '0'
    }

fun List<Char>.toInt() =
    this.joinToString(separator = "")
        .toInt(radix = 2)

fun main() {
    fun part1(input: List<String>): Int {
        val gamma = input.transposed()
            .map(List<Char>::mostCommonChar)
            .toInt()
        val epsilon = input.transposed()
            .map(List<Char>::leastCommonChar)
            .toInt()
        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        fun List<String>.charsAt(i: Int): List<Char> = map { it[i] }
        val length = input[0].length
        val oxygen = (0 until length) // [0,1,2,3,4]
            .fold(input) { acc: List<String>, i: Int ->
                if (acc.size == 1) acc else {
                    val mostCommonChar = acc.charsAt(i).mostCommonChar()
                    acc.filter { it[i] == mostCommonChar }
                }
            }.first().toInt(radix = 2)

        val co2 = (0 until length).fold(input) { acc: List<String>, i: Int ->
            if (acc.size == 1) acc else {
                val leastCommonChar = acc.charsAt(i).leastCommonChar()
                acc.filter { it[i] == leastCommonChar }
            }
        }.first().toInt(radix = 2)

        return oxygen * co2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04/testInput")
    check(part1(testInput) == 198)
    val input = readInput("day04/input")
    check(part1(input) == 4103154)
    println(part1(input))

    println(part2(testInput))
    check(part2(testInput) == 230)

    println(part2(input))
}
