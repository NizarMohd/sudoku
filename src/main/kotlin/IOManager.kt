package sudoku

/**
 * Abstraction of IO. allows for testing
 */
interface IOManager {
    fun read(): String
    fun write(message: String)
}
