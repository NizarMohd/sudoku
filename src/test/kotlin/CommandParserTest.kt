package sudoku

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CommandParserTest {
    private val parser = CommandParser()

    @Test fun `parses a place command`() {
        assertEquals(Command.Place(Position(1, 2), 7), parser.parse("B3 7"))
    }

    @Test fun `parses a clear command case-insensitively`() {
        assertEquals(Command.Clear(Position(2, 4)), parser.parse("C5 CLEAR"))
    }

    @Test fun `parses bare keywords`() {
        assertEquals(Command.Hint, parser.parse("hint"))
        assertEquals(Command.Check, parser.parse("check"))
        assertEquals(Command.Quit, parser.parse("quit"))
    }

    @Test fun `rejects out of range number`() {
        assertTrue(parser.parse("A1 0") is Command.Invalid)
        assertTrue(parser.parse("A1 10") is Command.Invalid)
    }

    @Test fun `rejects a bad cell reference`() {
        assertTrue(parser.parse("Z9 4") is Command.Invalid)
    }
}
