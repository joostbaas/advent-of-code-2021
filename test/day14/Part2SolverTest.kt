package day14

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Part2SolverTest {
    val testInput = "NNCB"
    val rules = mapOf(
        listOf('C','H') to 'B',
        listOf('H','H') to 'N',
        listOf('C','B') to 'H',
        listOf('N','H') to 'C',
        listOf('H','B') to 'C',
        listOf('H','C') to 'B',
        listOf('H','N') to 'C',
        listOf('N','N') to 'C',
        listOf('B','H') to 'H',
        listOf('N','C') to 'B',
        listOf('N','B') to 'B',
        listOf('B','N') to 'B',
        listOf('B','B') to 'N',
        listOf('B','C') to 'B',
        listOf('C','C') to 'N',
        listOf('C','N') to 'C',
    )

    @Test
    fun canDoTwoCharacters() {
        Part2Solver(rules).solve("NN".toList(), 1).let {
            assertEquals(2, it['N'])
            assertEquals(1, it['C'])
        }
    }

    @Test
    fun canDoThreeCharacters() {
        // result after one step: NCNBC
        Part2Solver(rules).solve("NNC".toList(), 1).let {
            assertEquals(1, it['B'])
            assertEquals(2, it['N'])
            assertEquals(2, it['C'])
        }
    }

    @Test
    fun canDoTwoCharactersTwoSteps() {
        // result after two steps: NBCCN
        Part2Solver(rules).solve("NN".toList(), 2).let {
            assertEquals(1, it['B'])
            assertEquals(2, it['N'])
            assertEquals(2, it['C'])
        }
    }

    @Test
    fun canDoMultipleCharacters() {
        // result after one step: NCNBCHB
        Part2Solver(rules).solve(testInput.toList(), 1).let {
            assertEquals(2, it['C'])
            assertEquals(2, it['B'])
            assertEquals(2, it['N'])
            assertEquals(1, it['H'])
        }
    }

    @Test
    fun canDoMultipleCharactersMultipleSteps() {
        // result after 10 steps:
        // B occurs 1749 times,
        // C occurs 298 times,
        // H occurs 161 times, and
        // N occurs 865 times
        Part2Solver(rules).solve(testInput.toList(), 10).let {
            assertEquals(1749, it['B'])
            assertEquals(298, it['C'])
            assertEquals(161, it['H'])
            assertEquals(865, it['N'])
        }
    }

    @Test
    fun canDoMultipleCharactersLotsOfSteps() {
        // result after 10 steps:
        // B occurs 1749 times,
        // C occurs 298 times,
        // H occurs 161 times, and
        // N occurs 865 times
        Part2Solver(rules).solve(testInput.toList(), 40).let {
            assertEquals(2192039569602L, it['B'])
            assertEquals(3849876073L, it['H'])
        }
    }
}