package sudoku

import kotlin.random.Random

/**
 * Generates a puzzle for the user
 */
class Generator(private val random: Random = Random.Default) {

    fun generate(clues: Int = DEFAULT_CLUES): Puzzle {
        val solution = Array(9) { IntArray(9) }
        fill(solution)
        val puzzle = solution.map { it.clone() }.toTypedArray()
        hideCells(puzzle, clues)
        return Puzzle(Board.from(puzzle), solution)
    }


    private fun fill(g: Array<IntArray>, pos: Int = 0): Boolean {
        if (pos == 81) return true
        val r = pos / 9; val c = pos % 9

        for (n in (1..9).shuffled(random)) {
            if (canPlace(g, r, c, n)) {
                g[r][c] = n
                if (fill(g, pos + 1)) return true
                g[r][c] = 0
            }
        }
        return false
    }

    /** Would placing `n` at (r, c) be legal right now? */
    private fun canPlace(g: Array<IntArray>, r: Int, c: Int, n: Int): Boolean {
        for (i in 0..8) if (g[r][i] == n || g[i][c] == n) return false
        val br = (r / 3) * 3; val bc = (c / 3) * 3
        for (i in br until br + 3) for (j in bc until bc + 3)
            if (g[i][j] == n) return false
        return true
    }

    private fun hideCells(g: Array<IntArray>, clues: Int) {
        (0..80).shuffled(random).drop(clues).forEach { p -> g[p / 9][p % 9] = 0 }
    }

    companion object { const val DEFAULT_CLUES = 30 }
}
