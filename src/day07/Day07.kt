package day07

import readInput
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {

    fun List<String>.parse() = this[0].split(",").map { it.toInt() }

    fun calculateFuel(input: List<String>, fuelCostFunction: (Int, Int) -> Int) =
        input.parse().let { crabPositions: List<Int> ->
            (crabPositions.minOf { it }..crabPositions.maxOf { it })
                .map { candidate ->
                    crabPositions.sumOf { crabPosition -> fuelCostFunction(candidate, crabPosition) }
                }.minOf { it }
        }

    fun part1(input: List<String>): Int =
        calculateFuel(input) { pos1, pos2 -> abs(pos1 - pos2) }

    tailrec fun progressiveCost(diff: Int, acc: Int = 0) : Int =
        when(diff) {
            0 -> acc
            else -> progressiveCost(diff - 1, acc + diff)
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

    measureTimeMillis {
        val part2Answer = part2(input)
        println(part2Answer)
        check(part2Answer == 96864235)
    }.let {
        println("took ${it} milis")
    }
}
