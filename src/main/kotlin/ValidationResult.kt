package sudoku

/** Validate Result of the command **/
sealed interface ValidationResult {
    object Valid : ValidationResult
    data class Violation(val message: String) : ValidationResult
}