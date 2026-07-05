package sudoku

/** Renders a board as text. */
class BoardPrinter {

    fun printBoard(board: Board): String {
        val sb = StringBuilder()
        sb.appendLine("    1 2 3 4 5 6 7 8 9")
        for (r in 0..8) {
            sb.append("  ${'A' + r} ")
            val cells = board.row(r).map { if (it.isEmpty) "_" else it.value.toString() }
            sb.appendLine(cells.joinToString(" "))
        }
        return sb.toString()
    }
}
