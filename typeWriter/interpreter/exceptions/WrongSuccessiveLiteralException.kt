package typeWriter.interpreter.exceptions
class WrongSuccessiveLiteralException(line: Int) : Exception("[Line $line] " + "The successive literal wasn't the one expected to be declared.")
