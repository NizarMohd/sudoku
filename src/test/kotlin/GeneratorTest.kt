package sudoku

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

class GeneratorTest {

    @Test fun `puzzle reveals exactly 30 clues`() {
        val puzzle = Generator(Random(42)).generate(30)
        val filled = (0..8).sumOf { r -> (0..8).count { c ->
            !puzzle.board.cellAt(Position(r, c)).isEmpty } }
        assertEquals(30, filled)
    }

    @Test fun `generated solution has no rule violations`() {
        val puzzle = Generator(Random(42)).generate(30)
        assertEquals(ValidationResult.Valid,
            Validator().check(Board.from(puzzle.solution)))
    }

    @Test fun `every solution row contains 1 through 9`() {
        val puzzle = Generator(Random(1)).generate()
        puzzle.solution.forEach { row -> assertEquals((1..9).toSet(), row.toSet()) }
    }
}
