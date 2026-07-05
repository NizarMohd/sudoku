package sudoku

/** Main Loop of the game */
fun main() {
    val io = MainIOManager()
    do {
        val puzzle = Generator().generate()
        GameDriver(puzzle, CommandParser(), Validator(), BoardPrinter(), io).play()
        io.write("\nPress Enter to play again, or type quit...")
    } while (io.read().trim().lowercase() != "quit")
}