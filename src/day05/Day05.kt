package day05

import readInput

data class Point(
    val x: Int,
    val y: Int,
)

data class Line(
    val p1: Point,
    val p2: Point,
) {
    private fun range(n1: Int, n2: Int) = (if (n1 > n2) (n2..n1).reversed() else (n1..n2)).toList()

    fun allPointsNonDiagonal(): Set<Point> =
        when {
            p1.x != p2.x && p1.y != p2.y -> emptySet()
            else -> allPoints()
        }

    private fun List<Int>.zipPoints(other: List<Int>): Set<Point> =
        when {
            this.size == 1 -> other.map { Point(this[0], it) }
            other.size == 1 -> this.map { Point(it, other[0]) }
            else -> this.zip(other).map { Point(it.first, it.second) }
        }.toSet()

    fun allPoints(): Set<Point> =
        range(p1.x, p2.x).zipPoints(range(p1.y, p2.y))
}


fun main() {
    val regex = """([0-9]+),([0-9]+) -> ([0-9]+),([0-9]+)""".toRegex()

    fun parseInput(line: String): Line {
        val (x1, y1, x2, y2) = regex.find(line)!!.destructured
        return Line(Point(x1.toInt(), y1.toInt()), Point(x2.toInt(), y2.toInt()))
    }

    fun countOverlappingLines(input: List<String>, pointsExtractor: (Line) -> Set<Point>) =
        input.map { parseInput(it) }
            .flatMap(pointsExtractor)
            .groupBy { it }
            .map { it.key to it.value.size }
            .count { (_, number) ->
                number > 1
            }

    fun part1(input: List<String>): Int =
        countOverlappingLines(input, Line::allPointsNonDiagonal)

    fun part2(input: List<String>): Int =
        countOverlappingLines(input, Line::allPoints)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/testInput")
    check(part1(testInput) == 5)
    val input = readInput("day05/input")
    println(part1(input))
    check(part1(input) == 6461)

    println(part2(testInput))
    check(part2(testInput) == 12)

    println(part2(input))
    check(part2(testInput) == 18065)
}
