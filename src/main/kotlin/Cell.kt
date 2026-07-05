package sudoku

/**  Handles logic of each cell in the sudoku table **/
class Cell(value: Int = EMPTY, val isGiven: Boolean = false) {
    var value: Int = value
        private set

    fun set(newValue: Int) {
        require(!isGiven) { "Cannot modify cell that is prefilled" }
        require(newValue in 1..9) { "Number must be between 1 and 9" }
        value = newValue
    }

    fun clear() {
        require(!isGiven) { "Cannot clear prefilled cell" }
        value = EMPTY
    }

    val isEmpty get() = value == EMPTY

    companion object { const val EMPTY = 0 }
}