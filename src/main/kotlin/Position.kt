package sudoku

/** creates data claass of the position index **/
data class Position(val row: Int, val col: Int) {
    init { require(row in 0..8 && col in 0..8) { "Position is out of bounds" } }

    val label get() = "${'A' + row}${col + 1}"
    companion object {
        fun parse(token: String): Position? {
            if (token.length < 2) return null
            val row = token[0].uppercaseChar() - 'A'
            val col = token.substring(1).toIntOrNull()?.minus(1) ?: return null
            return if (row in 0..8 && col in 0..8) Position(row, col) else null
        }
    }
}