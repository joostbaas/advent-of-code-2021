package day02

import readInput

fun main() {
    val regex = """([a-z]+) ([0-9]+)""".toRegex()

    fun part1(input: List<String>): Int {
        fun add(line: String): Pair<Int, Int> {
            regex.find(line)?.destructured?.let {
                val (dir, n) = it
                return when (dir) {
                    "forward" -> Pair(n.toInt(), 0)
                    "up" -> Pair(0, -n.toInt())
                    "down" -> Pair(0, n.toInt())
                    else -> throw IllegalArgumentException(dir)
                }
            } ?: throw IllegalStateException(line)
        }
        return input.fold(Pair(0, 0)) { pos, line ->
            add(line).let { Pair(pos.first + it.first, pos.second + it.second) }
        }.let {
            it.first * it.second
        }
    }

    fun part2(input: List<String>): Int {
        data class State(val horizontal: Int, val depth: Int, val aim: Int)
        fun add(current: State, line: String): State {
            regex.find(line)?.destructured?.let {
                val (dir, nStr) = it
                val n = nStr.toInt()
                return when (dir) {
                    "forward" -> current.copy(horizontal = current.horizontal+n, depth = current.depth + n*current.aim)
                    "up" ->     current.copy(aim = current.aim - n)
                    "down" ->    current.copy(aim = current.aim + n)
                    else -> throw IllegalArgumentException(dir)
                }
            } ?: throw IllegalStateException(line)
        }
        return input.fold(State(0, 0, 0)) { pos, line ->
//            println("$pos $line")
            add(pos, line)
        }.let {
            it.horizontal * it.depth
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    println(part2(testInput))
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
