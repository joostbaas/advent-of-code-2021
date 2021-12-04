package day01

import readInput

fun main() {
    fun part1(input: List<String>): Int =
        input.map { it.toInt() }
            .windowed(size = 2, step = 1)
            .fold(0) {acc, i: List<Int> ->
                if(i[1] > i[0]) acc + 1
                else acc
            }


    fun part2(input: List<String>): Int =
        input.map { it.toInt() }
            .windowed(size = 3, step = 1)
            .map { it.sum() }
            .windowed(size = 2, step = 1)
            .fold(0) {acc, i: List<Int> ->
                if(i[1] > i[0]) acc + 1
                else acc
            }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
