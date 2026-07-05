package sudoku

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GameDriverTest {
    private class FakeIO(private val inputs: MutableList<String>) : IOManager {
        val output = StringBuilder()
        override fun read() = if (inputs.isEmpty()) "quit" else inputs.removeAt(0)
        override fun write(message: String) { output.append(message).append("\n") }
    }

    private fun gameWith(grid: Array<IntArray>, vararg inputs: String): FakeIO {
        val puzzle = Puzzle(Board.from(grid), grid.map { it.clone() }.toTypedArray())
        val io = FakeIO(inputs.toMutableList())
        GameDriver(puzzle, CommandParser(), Validator(), BoardPrinter(), io).play()
        return io
    }

    @Test fun `rejects editing a pre-filled cell`() {
        val g = Array(9) { IntArray(9) }.also { it[0][0] = 5 }
        val io = gameWith(g, "A1 6", "quit")
        assertTrue(io.output.contains("A1 is pre-filled"))
    }

    @Test fun `accepts a valid move on an empty cell`() {
        val io = gameWith(Array(9) { IntArray(9) }, "A2 4", "quit")
        assertTrue(io.output.contains("Move accepted"))
    }

    @Test fun `check reports a violation after a duplicate move`() {
        val g = Array(9) { IntArray(9) }.also { it[0][0] = 3 }  // given at A1
        val io = gameWith(g, "A3 3", "check", "quit")
        assertTrue(io.output.contains("Number 3 already exists in Row A."))
    }
}
