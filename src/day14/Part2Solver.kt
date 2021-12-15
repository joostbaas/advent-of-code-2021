package day14

import CountingMap

class Part2Solver(private val rules: Map<List<Char>, Char>) {

    private val cacheAfter20Steps: Map<List<Char>, CountingMap<Char>> =
        rules.map {
            it.key to solve(it.key, 20)
        }.toMap()

    fun solve(input: List<Char>, steps: Int, acc: CountingMap<Char> = CountingMap()): CountingMap<Char> {
        if (steps == 20 && input.size == 2 && cacheAfter20Steps != null && cacheAfter20Steps[input] != null) {
            return acc + cacheAfter20Steps[input]!!
        }
        if (input.size == 2 && steps == 1) {
            val after = listOf(input[0], rules[input], input[1]).joinToString(separator = "")
            return after.countChars(acc)
        }
        if (input.size == 2) {
            val after = listOf(input[0], rules[input]!!, input[1])
            return solve(after, steps -1, acc)
        }
        val firstTwo = input.take(2)
        solve(firstTwo, steps, acc)
        return  solve(input.drop(1),steps, acc).removeOne(firstTwo[1])
    }

    private fun String.countChars(acc: CountingMap<Char>): CountingMap<Char> =
        this.fold(acc) { acc, c ->
            acc.addOne(c)
        }
}