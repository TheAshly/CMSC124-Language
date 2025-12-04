package typeWriter.interpreter.exceptions
class LiteralRedeclarationException(line: Int) : Exception("[Line $line] " + "Another literal after a literal was already declared.")
