package sudoku

/**
 * Turns raw input into a Command. Input validation is done here based on what user enters.
 */
class CommandParser {

    fun parse(input: String): Command {
        val tokens = input.trim().split(Regex("\\s+"))
        return when (tokens.first().lowercase()) {
            "hint"  -> Command.Hint
            "check" -> Command.Check
            "quit"  -> Command.Quit
            else    -> parsePositional(tokens)
        }
    }
    private fun parsePositional(tokens: List<String>): Command {
        if (tokens.size != 2)
            return Command.Invalid("Invalid command.")

        val pos = Position.parse(tokens[0])
            ?: return Command.Invalid("Invalid cell: '${tokens[0]}'.")

        if (pos.row !in 0..8 || pos.col !in 0..8){
            return Command.Invalid("Invalid cell: '${tokens[0]}'.")
        }

        val arg = tokens[1].lowercase()
        if (arg == "clear") return Command.Clear(pos)

        val value = arg.toIntOrNull()
            ?: return Command.Invalid("Please add a number or 'clear'.")
        if (value !in 1..9)
            return Command.Invalid("Please ensure that number must be between 1 and 9.")

        return Command.Place(pos, value)
    }
}
