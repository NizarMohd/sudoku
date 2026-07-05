package sudoku
/** handles logic for the sudoku board. 9×9 grid knows nothing about rules **/
class Board(private val grid: Array<Array<Cell>>) {
    fun column(c: Int): List<Cell> = grid.map { it[c] }
    fun row(r: Int): List<Cell> = grid[r].toList()
    fun cellAt(pos: Position): Cell = grid[pos.row][pos.col]


    fun subgrid(r: Int, c: Int): List<Cell> {
        val br = (r / 3) * 3; val bc = (c / 3) * 3
        return (br until br + 3).flatMap { i -> (bc until bc + 3).map { j -> grid[i][j] } }
    }

    val isComplete get() = grid.all { row -> row.none { it.isEmpty } }

    companion object {
        fun from(values: Array<IntArray>): Board = Board(
            Array(9) { r -> Array(9) { c ->
                val v = values[r][c]
                Cell(v, isGiven = v != Cell.EMPTY)
            } }
        )
    }
}