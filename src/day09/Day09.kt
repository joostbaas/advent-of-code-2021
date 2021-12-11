package day09

import readInput
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

fun List<String>.toGrid() =
    this.map {
        it.toList().map { it.toString().toInt() }
    }.let {
        Grid(it)
    }

data class Pos(val x: Int, val y: Int)

data class Grid(val lines: List<List<Int>>) {
    private val width = lines[0].size
    private val height = lines.size

    operator fun get(pos: Pos): Int? =
        this[pos.x, pos.y]

    operator fun get(x: Int, y: Int): Int? {
        return lines.getOrNull(y)?.getOrNull(x)
    }

    private fun neighbourCells(pos: Pos): Set<Pos> =
        (-1..1).mapNotNull { ny ->
            (-1..1).mapNotNull { nx ->
                val x = pos.x + nx
                val y = pos.y + ny
                if (this[x, y] != null) {
                    Pos(x, y)
                } else null
            }
        }.flatten().toSet()

    fun findConnected(start: Pos): Set<Pos> {
        val result = mutableSetOf(start)
        while (true) {
            val toAdd = result.flatMap { pos ->
//                neighbourCells(it)
                listOf(
                    pos.copy(x = pos.x - 1),
                    pos.copy(x = pos.x + 1),
                    pos.copy(y = pos.y - 1),
                    pos.copy(y = pos.y + 1),
                ).filter { this[it] != null }
                    .toSet()
            }
                .filterNot { it in result }
                .filterNot { this[it] == 9 }
                .toSet()
            if (toAdd.isEmpty())
                return result
            else {
                result.addAll(toAdd)
            }
        }
    }


    fun neighbours(x: Int, y: Int): List<Int> =
        neighbourCells(Pos(x, y)).mapNotNull { this[it.x, it.y] }

    fun listPos() =
        (0 until this.width).flatMap { x ->
            (0 until this.height).map { y ->
                Pos(x, y)
            }
        }
}

@OptIn(ExperimentalTime::class)
fun main() {

    fun part1(input: List<String>): Int =
        input.toGrid().let { grid ->
            grid.listPos().mapNotNull { (x, y) ->
                val cell = grid[x, y]!!
                val neighbours = grid.neighbours(x, y)
                if (neighbours.none { cell > it })
                    cell + 1
                else null
            }
        }.sum()

    fun part2(input: List<String>): Int {
        val grid = input.toGrid()
        grid.listPos().fold(emptySet()) { acc: Set<Set<Pos>>, pos ->
            when {
                pos in acc.flatten() -> acc
                grid[pos] == 9 -> acc
                else -> acc + setOf(grid.findConnected(pos))
            }
        }.let { basins ->
            return basins.map { it.size }
                .sortedDescending()
                .take(3)
                .fold(1) { acc, i ->
                    acc * i
                }
        }
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("day09/testInput")
    println(part1(testInput))
    check(part1(testInput) == 15)
    val input = readInput("day09/input")
    println(part1(input))
    check(part1(input) == 522)

    println(part2(testInput))
    check(part2(testInput) == 1134)

    val (part2Answer, duration) = measureTimedValue {
        part2(input)
    }
    println(part2Answer)
    println("took ${duration.inWholeMilliseconds} milis")
    check(part2Answer == 1019355)
}
