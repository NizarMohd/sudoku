package sudoku

/** Creates evry possible usr action. */
sealed interface Command {
    data class Place(val pos: Position, val value: Int) : Command
    data class Clear(val pos: Position) : Command
    object Hint : Command
    object Check : Command
    object Quit : Command
    data class Invalid(val reason: String) : Command
}
