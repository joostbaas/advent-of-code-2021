package day10

import readInput
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
fun main() {

    data class ScoreAndStack(val score: Int, val stack: List<Char>)

    val charsAndScore: Map<Char, Pair<Char, Int>> =
        mapOf(
            ')' to Pair('(', 3),
            ']' to Pair('[', 57),
            '}' to Pair('{', 1197),
            '>' to Pair('<', 25137),
        )

    fun processLine(it: String) = it.fold(ScoreAndStack(0, emptyList())) { acc, currentChar ->
        if (acc.score != 0) acc
        else {
            when (currentChar) {
                in "{[<(" -> acc.copy(stack = acc.stack + currentChar)
                in charsAndScore.keys -> {
                    val (expected, score) = charsAndScore.get(currentChar)!!
                    if (acc.stack.last() == expected) acc.copy(stack = acc.stack.dropLast(1))
                    else acc.copy(score = score)
                }
                else -> throw IllegalStateException("$currentChar should not happen")
            }
        }
    }

    fun part1(input: List<String>): Int =
        input.map {
            processLine(it)
        }.sumOf { it.score }


    fun part2(input: List<String>): Long =
        input.map {
            processLine(it)
        }.filter { it.score == 0 }
            .map {
                it.stack.reversed().fold(0L) { acc: Long, c: Char ->
                    val penalty = when (c) {
                        '(' -> 1
                        '[' -> 2
                        '{' -> 3
                        '<' -> 4
                        else -> throw IllegalStateException("$c should not happen")
                    }
                    (acc * 5) + penalty
                }
            }.sorted()
            .let {
                println(it)
                it[it.size / 2]
            }



// test if implementation meets criteria from the description, like:
    val testInput = readInput("day10/testInput")
    println(part1(testInput))
    check(part1(testInput) == 26397)
    val input = readInput("day10/input")
    println(part1(input))
    check(part1(input) == 415953)

    println(part2(testInput))
    check(part2(testInput) == 288957L)

    val (part2Answer, duration) = measureTimedValue {
        part2(input)
    }
    println(part2Answer)
    println("took ${duration.inWholeMilliseconds} milis")
    check(part2Answer > 162764112)
    check(part2Answer == -1L)
}
