package day14

import doRun
import readInput

fun main() {

    fun step(target: String, rules: Map<String, String>) =
        target.windowed(2).fold(target[0].toString()) { acc, s ->
            rules[s]!!.let {
                acc + it + s[1]
            }
        }

    fun polymerize(input: List<String>, steps: Int): Long {
        val target = input[0]
        val rules = input.drop(2).associate {
            val (between, insert) = it.split(" -> ")
            between to insert
        }
        (0 until steps).fold(target) { acc, _ ->
            step(acc, rules)
        }.let {
            it.toList().groupingBy { it }.eachCount().toList().let {
                val max = it.maxOf { it.second }
                val min = it.minOf { it.second }
                return (max - min).toLong()
            }
        }
    }

    fun part1(input: List<String>): Int =
        polymerize(input, 10).toInt()

    fun part2(input: List<String>): Long {
        val target = input[0]
        val rules: Map<List<Char>, Char> = input.drop(2).associate {
            val (between, insert) = it.split(" -> ")
            between.toList() to insert[0]
        }
        return Part2Solver(rules).solve(target.toList(), 40).let {
            characterCount ->
                val max = characterCount.maxOf { it.value }
                val min = characterCount.minOf { it.value }
                max - min
        }
    }


    val testInput = readInput("day14/testInput")
    val input = readInput("day14/input")

    doRun("part1 test", 1588) { part1(testInput) }
    doRun("part1 real", 2587) { part1(input) }
    doRun("part2 test", 2188189693529) { part2(testInput) }
    doRun("part2 real", 3318837563123) { part2(input) }
}
