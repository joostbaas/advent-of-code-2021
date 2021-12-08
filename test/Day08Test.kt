import day08.createLookupTable
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day08Test {

    val line = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"

    @Test
    fun deduceFromALine() {
        val deducedLine = day08.deduceLine(line)
        assertEquals(
            mapOf(
                'd' to 'a',
                'e' to 'b',
                'a' to 'c',
                'f' to 'd',
                'g' to 'e',
                'b' to 'f',
                'c' to 'g',
            ),
            deducedLine)
    }

    @Test
    fun canConvertSingleToInt() {
        val lookupTable = createLookupTable(line)
        mapOf("acedgfb" to 8,
            "cdfbe" to 5,
            "gcdfa" to 2,
            "fbcad" to 3,
            "dab" to 7,
            "cefabd" to 9,
            "cdfgeb" to 6,
            "eafb" to 4,
            "cagedb" to 0,
            "ab" to 1)
            .forEach { (chars, expected) ->
                assertEquals(expected, lookupTable(chars))
            }
    }

    @Test
    fun canConvertSingleToIntLine2() {
        val lookupTable = createLookupTable("edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc")
        mapOf(
            "fcgedb" to 9,
            "cgb" to 7,
            "dgebacf" to 8,
            "gc" to 1,
        )
            .forEach { (chars, expected) ->
                assertEquals(expected, lookupTable(chars))
            }
    }
}