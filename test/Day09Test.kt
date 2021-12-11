import day08.createLookupTable
import day09.Pos
import day09.toGrid
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09Test {

    @Test
    fun findBasin() {
        readInput("day09/testInput").toGrid().let { grid ->
            grid.findConnected(Pos(0,0)).let {
                assertEquals(3, it.size)
            }
        }
    }

    @Test
    fun findConnectedHorizontally() {
        listOf("12").toGrid().let { grid ->
            assertEquals(2, grid.findConnected(Pos(0,0)).size)
        }
        listOf("123").toGrid().let { grid ->
            assertEquals(3, grid.findConnected(Pos(0,0)).size)
        }
    }

    @Test
    fun findConnectedHorizontallyAndVertically() {
        listOf("12","34").toGrid().let { grid ->
            assertEquals(4, grid.findConnected(Pos(0,0)).size)
        }
        listOf("111","222","333").toGrid().let { grid ->
            assertEquals(9, grid.findConnected(Pos(0,0)).size)
        }
    }

    @Test
    fun shouldStopAt9() {
        listOf("1291").toGrid().let { grid ->
            assertEquals(2, grid.findConnected(Pos(0,0)).size)
        }
        listOf("1","2","9","1").toGrid().let { grid ->
            assertEquals(2, grid.findConnected(Pos(0,0)).size)
        }
    }
}