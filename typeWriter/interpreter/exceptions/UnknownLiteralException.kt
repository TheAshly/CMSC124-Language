package typeWriter.interpreter.exceptions
class UnknownLiteralException(lexeme: String, line: Int) : Exception("[Line $line] " + "\"$lexeme\" is not a proper Literal.")
