package sudoku

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BoardTest {
    private fun emptyGrid() = Array(9) { IntArray(9) }

    @Test fun `pre-filled cell cannot be modified`() {
        val board = Board.from(emptyGrid().also { it[0][0] = 5 })
        assertThrows<IllegalArgumentException> { board.cellAt(Position(0, 0)).set(6) }
    }

    @Test fun `number outside 1 to 9 is rejected`() {
        val board = Board.from(emptyGrid())
        assertThrows<IllegalArgumentException> { board.cellAt(Position(0, 0)).set(10) }
    }

    @Test fun `subgrid returns the nine cells of its block`() {
        val board = Board.from(emptyGrid())
        assertEquals(9, board.subgrid(4, 4).size)
    }

    @Test fun `board is complete only when no cell is empty`() {
        val full = Array(9) { r -> IntArray(9) { c -> (r * 3 + r / 3 + c) % 9 + 1 } }
        assertTrue(Board.from(full).isComplete)
        assertFalse(Board.from(emptyGrid()).isComplete)
    }
}
