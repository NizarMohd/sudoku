package sudoku

/**
 * Brain logic of the game.
 */
class GameDriver(
    private val puzzle: Puzzle,
    private val parser: CommandParser,
    private val validator: Validator,
    private val printer: BoardPrinter,
    private val io: IOManager
) {
    private val board = puzzle.board

    fun play() {
        io.write("Welcome to Sudoku!\n")
        io.write("Here is your puzzle:\n")
        io.write(printer.printBoard(board))

        while (true) {
            io.write("\nEnter command (e.g., A3 4, C5 clear, hint, check, quit):")
            when (val command = parser.parse(io.read())) {
                is Command.Place   -> place(command)
                is Command.Clear   -> clear(command)
                is Command.Hint    -> hint()
                is Command.Check   -> io.write(describe(validator.check(board)))
                is Command.Invalid -> io.write(command.reason)
                is Command.Quit    -> { io.write("Goodbye!"); return }
            }
            if (board.isComplete && validator.check(board) is ValidationResult.Valid) {
                io.write("\n" + printer.printBoard(board))
                io.write("You have successfully completed the Sudoku puzzle!")
                return
            }
        }
    }

    private fun place(cmd: Command.Place) {
        val cell = board.cellAt(cmd.pos)
        if (cell.isGiven) {
            io.write("Invalid move. ${cmd.pos.label} is pre-filled.")
            return
        }
        cell.set(cmd.value)
        io.write("\nMove accepted.")
        io.write("\nCurrent grid:\n" + printer.printBoard(board))
    }

    private fun clear(cmd: Command.Clear) {
        val cell = board.cellAt(cmd.pos)
        if (cell.isGiven) {
            io.write("Invalid move. ${cmd.pos.label} is prefilled.")
            return
        }
        cell.clear()
        io.write("\nCell ${cmd.pos.label} cleared.\n" + printer.printBoard(board))
    }

    /** Reveals the first empty cell's correct value, taken from the retained solution. */
    private fun hint() {
        for (r in 0..8) for (c in 0..8) {
            val pos = Position(r, c)
            if (board.cellAt(pos).isEmpty) {
                val answer = puzzle.solution[r][c]
                board.cellAt(pos).set(answer)
                io.write("Hint: Cell ${pos.label} = $answer")
                return
            }
        }
    }

    private fun describe(result: ValidationResult): String = when (result) {
        is ValidationResult.Valid     -> "No rule violations detected."
        is ValidationResult.Violation -> result.message
    }
}
