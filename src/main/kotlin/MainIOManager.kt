package sudoku

/** The actual ConsoleIO, using standard input/output. */
class MainIOManager : IOManager {
    override fun read(): String = readlnOrNull().orEmpty()
    override fun write(message: String) = println(message)
}
