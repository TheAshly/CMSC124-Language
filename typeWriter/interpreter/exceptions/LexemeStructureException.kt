package typeWriter.interpreter.exceptions
class LexemeStructureException(line: Int) : Exception("[Line $line] " + "Word doesn't follow proper lexeme structure.")
