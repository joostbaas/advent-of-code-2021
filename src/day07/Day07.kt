package day07

import readInput
import kotlin.math.abs

fun main() {

    fun List<String>.parse() = this[0].split(",").map { it.toInt() }

    fun calculateFuel(input: List<String>, fuelCostFunction: (Int, Int) -> Int) =
        input.parse().let { startingPositions ->
            (startingPositions.minOf { it }..startingPositions.maxOf { it })
                .map { candidate ->
                    startingPositions.sumOf { crabPosition -> fuelCostFunction(candidate, crabPosition) }
                }.minOf { it }
        }

    fun part1(input: List<String>): Int =
        calculateFuel(input) { pos1, pos2 -> abs(pos1 - pos2) }

    fun progressiveCost(diff: Int) : Int =
        when(diff) {
            0 -> 0
            else -> diff + progressiveCost(diff - 1)
        }

    fun part2(input: List<String>): Int =
        calculateFuel(input) { pos1, pos2 -> progressiveCost(abs(pos1 - pos2)) }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day07/testInput")
    check(part1(testInput) == 37)
    val input = readInput("day07/input")
    println(part1(input))
    check(part1(input) == 336120)

    println(part2(testInput))
    check(part2(testInput) == 168)

    println(part2(input))
    check(part2(input) == 96864235)
}
