package day11

import readInput
import kotlin.system.exitProcess
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue
import kotlin.time.measureTime
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

    fun step(): Pair<Grid, Int> =
        increaseAllByOne().handleFlashes().let { newGrid ->
            Pair(newGrid, newGrid.lines.flatten().count { it == 0 })
        }

    fun printMe() =
        lines.map { it.joinToString(separator = "") { if (it == 10) "x" else it.toString() } }
            .joinToString(separator = "\n").let {
                println("------\n$it\n------\n")
            }

    private fun handleFlashes(): Grid {
        var previousGrid: Grid = this
        var newGrid: Grid? = null
        while (previousGrid != newGrid) {
            if (newGrid != null) {
                previousGrid = newGrid
            }
            val flashingCells = previousGrid.listPos().filter { previousGrid[it]!! > 9 }
            newGrid = flashingCells.fold(previousGrid) { acc, i ->
                acc.flashCell(i)
            }
        }
        return newGrid
    }

    private fun increaseAllByOne() = Grid(lines.map { line -> line.map { it + 1 } })

    private fun flashCell(pos: Pos): Grid {
        val neighbours = neighbourCells(pos)
        return lines.mapIndexed { y, line ->
            line.mapIndexed { x, v ->
                when {
                    this[x, y] == 0 -> 0
                    Pos(x, y) == pos -> 0
                    Pos(x, y) in neighbours -> v + 1
                    else -> v
                }
            }
        }.let {
            Grid(it)
        }
    }

    private fun neighbourCells(pos: Pos): Set<Pos> =
        (((pos.x - 1)..(pos.x + 1))).mapNotNull { nx ->
            (((pos.y - 1)..(pos.y + 1))).mapNotNull { ny ->
                val neighbour = Pos(nx, ny)
                if (neighbour != pos && this[neighbour] != null) {
                    neighbour
                } else null
            }
        }.flatten().toSet()

    private fun listPos() =
        (0 until this.width).flatMap { x ->
            (0 until this.height).map { y ->
                Pos(x, y)
            }
        }
}

@OptIn(ExperimentalTime::class)
fun main() {

    fun part1(input: List<String>): Int =
        input.toGrid().let { startingGrid ->
            (1..100).fold(Pair(startingGrid, 0)) { (grid, flashes), i ->
                val (nextGrid, inc) = grid.step()
                Pair(nextGrid, flashes + inc)
            }.second
        }

    tailrec fun countStepsUntilAllFlashed(grid: Grid, acc: Int = 0): Int =
        if (grid.lines.flatten().all { it == 0 }) acc
        else countStepsUntilAllFlashed(grid.step().first, acc + 1)

    fun part2(input: List<String>): Int {
        val startingGrid = input.toGrid()
        return countStepsUntilAllFlashed(startingGrid)
    }

    val java = Grid::class.java
    val testInput = readInput("day11/testInput")
    val input = readInput("day11/input")

    fun doRun(name: String, expected: Int? = null, block: () -> Int) {
        measureTimedValue {
            block.invoke()
        }.let { (actual, duration): TimedValue<Int> ->
            val status = when (expected) {
                null -> "33m?" // yellow
                actual -> "32mV" // green
                else -> "31mX" // red
            }.let {
                "\u001B[${it}\u001B[0m"
            }
            println("[$status] $name: ${actual} (took ${duration.inWholeMilliseconds}ms)")
            expected?.let {
                if (actual != expected) exitProcess(-1)
            }
        }
    }
    doRun("part1 test", 1656) { part1(testInput) }
    doRun("part1 real", 1700) { part1(input) }
    doRun("part2 test", 195) { part2(testInput) }
    doRun("part2 real", 273) { part2(input) }
}
