package sudoku

/**
 * Checks for violations
 */
class Validator {

    fun check(board: Board): ValidationResult {
        for (r in 0..8) for (c in 0..8) {
            val cell = board.cellAt(Position(r, c))
            if (cell.isEmpty) continue
            val n = cell.value

            if (duplicateExists(board.row(r), n))
                return violation(n, "Row ${'A' + r}")
            if (duplicateExists(board.column(c), n))
                return violation(n, "Column ${c + 1}")
            if (duplicateExists(board.subgrid(r, c), n))
                return ValidationResult.Violation(
                    "Number $n already exists in the same 3x3 subgrid.")
        }
        return ValidationResult.Valid
    }

    private fun duplicateExists(cells: List<Cell>, value: Int): Boolean =
        cells.count { it.value == value } > 1

    private fun violation(n: Int, where: String) =
        ValidationResult.Violation("Number $n already exists in $where.")
}
