# Sudoku (Command-Line)

A command-line Sudoku game written in Kotlin. It generates a puzzle with 30
pre-filled cells, then lets the user place and clear numbers, request hints,
check the grid for rule violations, and quit. The game ends when the grid is
completely and correctly filled.

## Design overview

The code is split into small, single-responsibility classes, grouped by concern:

| Class | Responsibility |
|-------|----------------|
| `Board` | Holds the 9×9 grid state and exposes its rows, columns and subgrids. Contains no rule logic. |
| `Cell` | Represents a single cell: its value and whether it is pre-filled. Enforces the 1–9 range and prevents editing pre-filled cells. |
| `Position` | Represents a cell coordinate, parses labels like `B3`, and guards against out-of-bounds indices. |
| `Validator` | Checks the board for duplicates in any row, column or subgrid, and reports the first violation found. |
| `ValidationResult` | Return type of `Validator`: either `Valid` or a `Violation` carrying a message. |
| `Generator` | Produces the starting puzzle — fills a complete valid grid at random, then hides cells down to 30 clues. |
| `Puzzle` | Bundles the playable board together with its full solution (used to serve correct hints). |
| `Command` | The set of valid user actions (place, clear, hint, check, quit), plus an `Invalid` type for unrecognised input. |
| `CommandParser` | Parses raw user input into a `Command`, returning an `Invalid` command (with a reason) when the input is malformed. |
| `IOManager` | Abstraction over input/output. Lets a fake implementation be injected for testing. |
| `MainIOManager` | The production `IOManager`, backed by the real console. |
| `BoardPrinter` | Formats a board into the readable text layout shown to the user. |
| `GameDriver` | The brain of the game: reads input, interprets it, updates the board, prints results, and detects a win. |
| `Main` | Application entry point and the play-again loop. |

### Design principles

Each class has a single reason to change (e.g. `BoardPrinter` only formats
output, `Validator` only checks rules). Behaviour is extended by adding
implementations rather than modifying existing code — `GameDriver` depends on
the `IOManager` abstraction, not a concrete console, which is what makes the
whole game loop testable with a fake I/O. This keeps the design aligned with
the SOLID principles.

## Assumptions

- `check` validates the entire grid and reports the **first** violation it finds,
  scanning in the order row → column → subgrid.
- Placing a number is accepted as long as the target cell is editable and the
  value is between 1 and 9. Duplicates are **not** blocked at placement time;
  they are surfaced when the user runs `check`. (This matches the behaviour shown
  in the assignment's examples.)
- Entering a value into an already-filled editable cell overwrites it.
- A hint fills the first empty cell (in reading order) with its known-correct
  value from the solution.
- The puzzle starts with exactly 30 clues revealed; all other cells begin empty.

## How to build and run

Requires a JDK (version 17 or newer). No separate Gradle installation is needed —
the project uses the Gradle wrapper.

**From the command line:**

**From IntelliJ IDEA (This was developed and ran on IntelliJ IDEA 2022.1.3):**

1. File → Open, and select the project folder (the one containing
   `build.gradle.kts`). Let the Gradle sync finish.
2. Run the app via the green arrow next to `fun main()` in `Main.kt`.
3. Run the tests by right-clicking `src/test/kotlin` → Run, or via the Gradle
   panel → Tasks → verification → test.

## Environment

Cross-platform: runs on Windows, Linux or macOS. Any machine with a JDK 17+
can build and run it through the Gradle wrapper.

# Known Limitations & Design Assumptions

This document records the deliberate assumptions made in this solution and the
known drawbacks of the current approach. It is intended both as honest
documentation and as a record of trade-offs made under the scope of the
assignment.

## Deliberate assumptions

These are intentional choices, made to match the assignment brief or to keep the
solution appropriately scoped.

- **Duplicates are accepted at placement time and surfaced only by `check`.**
  A move is accepted as long as the target cell is editable and the value is
  1–9. Clashes are not blocked when placed; they are reported when the user runs
  `check`. This matches the behaviour shown in the assignment's examples.
  Because validation is isolated in `Validator`, adding a placement-time guard
  later would be straightforward.

- **A hint fills the first empty cell in reading order** with its known-correct
  value, taken from the retained full solution. This keeps hints simple and
  always correct.

- **The puzzle starts with exactly 30 clues, chosen at random.** This matches
  the brief.

- **`check` reports only the first violation found**, scanning in the order
  row → column → subgrid. This matches the single-message format shown in the
  brief's examples.

## Known drawbacks

These are genuine limitations of the current approach that a production version
would address.

### 1. Generated puzzles are not guaranteed to have a unique solution
A proper Sudoku has exactly one solution. This generator fills a complete valid
grid and then removes cells down to 30 at random, without checking that the
remaining puzzle still forces a single answer. As a result, many generated
puzzles may have multiple valid solutions. The win condition still works (any
complete, violation-free grid is accepted), but this would not satisfy a strict
definition of a valid Sudoku puzzle.

*Fix:* after each cell removal, run a solver that counts solutions and only keep
the removal if the count remains exactly 1.

### 2. 30 clues does not guarantee a logically solvable puzzle
Even setting uniqueness aside, a random 30-clue puzzle may require guessing
rather than pure deduction. The brief specified 30 clues, so that number was
followed, but "30 clues" and "a fair, human-solvable puzzle" are not equivalent.

### 3. `check` reports only the first violation
If the grid contains several separate clashes, the user must fix one and re-run
`check` to discover the next. Reporting all violations at once would be more
user-friendly, at the cost of deviating from the brief's single-message format.

### 4. Hints cannot correct a wrongly filled cell
A hint only fills an empty cell. If the user has placed an incorrect number, the
hint logic skips that cell and cannot fix it, so a user can become stuck with a
wrong value that no hint will address.

### 5. No detection of an unwinnable state
If the user enters a value that makes the puzzle impossible to complete (for
example, a wrong number that blocks the only valid solution), the game does not
warn them. `check` only flags current duplicates, not future dead-ends, so the
user may continue playing a board that can no longer be solved.

### 6. Input parsing is minimal
`CommandParser` handles the command formats specified in the brief. Variations
such as reversed argument order (e.g. `7 B3`) or other loosely-formatted input
are treated as invalid. The exact set of accepted inputs should be confirmed
against the parser before relying on this.

### 7. Backtracking generation has no formal time bound
The backtracking fill is near-instant in practice on a 9×9 grid, but it has no
theoretical upper bound on running time. A fixed random seed is used in tests to
keep generation deterministic and repeatable.

### 8. Board state is mutable and shared
`Cell` values are mutable and `Board` exposes cell references directly. This is
simple and works, but it allows callers to mutate cells outside the intended
command flow. A more defensive design would expose immutable views of the board.

## Priority for discussion

If prioritising, items **1 (uniqueness)**, **3 (first-violation-only)** and
**4/5 (hint and dead-end limitations)** are the most significant. Item 1, in
particular, reflects the core distinction between a fully-populated grid and a
genuine Sudoku puzzle.
