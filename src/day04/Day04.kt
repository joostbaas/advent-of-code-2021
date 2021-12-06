package day03

import readInput

fun List<List<Int>>.transposed(): List<List<Int>> {
    val length = this[0].size
    return (0 until length).map { i ->
        this.map { binaryString -> binaryString[i] }
    }
}

data class Bingo(val board: List<List<Int>>) {
    fun hasBingo(drawn: Set<Int>) =
        board.any { row -> drawn.containsAll(row) }
                || board.transposed().any {column -> drawn.containsAll(column) }
}

fun String.toInts(delimiter: String  = ",") = split(delimiter).filterNot { it.isBlank() }.map { it.toInt() }

fun main() {
    fun parseBoards(bingoLines: List<String>): Set<Bingo> {
        data class Acc(val finished: Set<Bingo>, val current: Bingo)
        return bingoLines.fold(Acc(emptySet(), Bingo(emptyList()))) { acc, currentLine ->
            if (currentLine.isBlank()) {
                Acc(finished = acc.finished + acc.current, Bingo(emptyList()))
            } else {
                val newRow: List<Int> = currentLine.toInts(" ")
                val newBoard: List<List<Int>> = acc.current.board + listOf(newRow)
                acc.copy(current = acc.current.copy(board = newBoard))
            }
        }.let {
            it.finished + it.current
        }
    }

    fun getScoreFromFirstWinningBoard(
        drawn: List<Int>,
        boards: Set<Bingo>,
    ): Int {
        drawn.fold(emptySet<Int>()) { alreadyDrawn, justDrawn ->
            val newAlreadyDrawn = alreadyDrawn + justDrawn
            boards.firstOrNull { it.hasBingo(newAlreadyDrawn) }
                .let { winningBoard ->
                    if (winningBoard != null) {
                        println("winning board after $justDrawn! $winningBoard")
                        val sumOfUnmarked = winningBoard.board.flatten()
                            .filterNot { newAlreadyDrawn.contains(it) }
                            .sum()
                        return sumOfUnmarked * justDrawn
                    }
                }
            newAlreadyDrawn
        }
        throw IllegalStateException("no winning boards!")
    }

    fun part1(input: List<String>): Int {
        val drawn = input.first().toInts()
        val boards = parseBoards(input.drop(2))

        return getScoreFromFirstWinningBoard(drawn, boards)
    }


    fun part2(input: List<String>): Int {
        val drawn = input.first().toInts()
        val boards = parseBoards(input.drop(2))

        drawn.fold(emptySet<Int>()) {
                alreadyDrawn, justDrawn ->
            val newAlreadyDrawn = alreadyDrawn + justDrawn
            boards.filterNot { it.hasBingo(newAlreadyDrawn) }
                .let { nonWinningBoards: List<Bingo> ->
                    if (nonWinningBoards.size == 1) {
                        val onlyNonWinningBoard = nonWinningBoards[0]
                        println("last non-winning board after $justDrawn! $onlyNonWinningBoard")

                        return getScoreFromFirstWinningBoard(drawn, setOf(onlyNonWinningBoard))
                    }
                }
            newAlreadyDrawn
        }
        throw IllegalStateException("no winning boards!")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/testInput")
    println(part1(testInput))
    check(part1(testInput) == 4512)
    val input = readInput("day03/input")
    println(part1(input))
    check(part1(input) == 46920)

    println(part2(testInput))
    check(part2(testInput) == 1924)

    println(part2(input))
}
