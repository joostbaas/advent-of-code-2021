package day13

import doRun
import readInput

sealed class Fold {
    abstract fun doFold(point: Point): Point

    data class Horizontal(val y: Int) : Fold() {
        override fun doFold(point: Point): Point =
            if (point.y < y) point
            else {
                val diff = point.y - y
                val newY = y - diff
                point.copy(y = newY)
            }
    }

    data class Vertical(val x: Int) : Fold() {
        override fun doFold(point: Point): Point =
            if (point.x < x) point
            else {
                val diff = point.x - x
                val newX = x - diff
                point.copy(x = newX)
            }
    }
}

data class Point(val x: Int, val y: Int)

fun main() {
    fun parseInput(input: List<String>): Pair<List<Point>, List<Fold>> =
        input.fold(Pair(emptyList(), emptyList())) { acc, line ->
            if (line.isEmpty()) return@fold acc
            """([0-9]+),([0-9]+)""".toRegex().find(line)?.let {
                val (x, y) = it.destructured
                acc.copy(first = acc.first + Point(x.toInt(), y.toInt()))
            } ?: line.split(" ").let {
                it.last().split("=").let {
                    val (direction, value) = it
                    when (direction) {
                        "y" -> Fold.Horizontal(value.toInt())
                        "x" -> Fold.Vertical(value.toInt())
                        else -> throw IllegalArgumentException("did not expect $direction")
                    }.let {
                        acc.copy(second = acc.second + it)
                    }
                }
            }
        }

    fun part1(input: List<String>): Int =
        parseInput(input).let {
            val (points, folds) = it
            val onlyFold = folds[0]
            points.map { point ->
                onlyFold.doFold(point)
            }
        }.toSet().size

    fun part2(input: List<String>): Unit =
        parseInput(input).let {
            val (startingPoints, folds) = it
            folds.fold(startingPoints.toSet()) { acc, fold ->
                acc.map { point ->
                    fold.doFold(point)
                }.toSet()
            }
        }.let { points: Set<Point> ->
            val (maxX, maxY) = points.fold(Point(0, 0)) { acc, point ->
                Point(maxOf(acc.x, point.x), maxOf(acc.y, point.y))
            }
            (0..maxX).reversed().forEach { x ->
                (0..maxY).forEach { y ->
                    if (Point(x, y) in points) {
                        print("#")
                    } else {
                        print(".")
                    }
                }
                print("\n")
            }
        }


    val testInput = readInput("day13/testInput")
    val input = readInput("day13/input")

    doRun("part1 test", 17) { part1(testInput) }
    doRun("part1 real", 607) { part1(input) }
    doRun("part2 test") {
        part2(testInput)
        -1
    }
    doRun("part2 real") {
        part2(input)
        -1
    }
}
