package sudoku

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ValidatorTest {
    private val validator = Validator()
    private fun grid() = Array(9) { IntArray(9) }

    @Test fun `clean grid reports valid`() {
        assertEquals(ValidationResult.Valid, validator.check(Board.from(grid())))
    }

    @Test fun `duplicate in a row is reported`() {
        val g = grid().also { it[0][0] = 3; it[0][2] = 3 }
        assertEquals(ValidationResult.Violation("Number 3 already exists in Row A."),
            validator.check(Board.from(g)))
    }

    @Test fun `duplicate in a column is reported`() {
        val g = grid().also { it[0][0] = 5; it[2][0] = 5 }
        assertEquals(ValidationResult.Violation("Number 5 already exists in Column 1."),
            validator.check(Board.from(g)))
    }

    @Test fun `duplicate in a subgrid is reported`() {
        val g = grid().also { it[0][1] = 8; it[1][2] = 8 }
        assertEquals(
            ValidationResult.Violation("Number 8 already exists in the same 3x3 subgrid."),
            validator.check(Board.from(g)))
    }
}
