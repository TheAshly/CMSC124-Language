package typeWriter.interpreter.exceptions
class WrongSuccessiveSymbolException(line: Int) : Exception("[Line $line] " + "The successive symbol wasn't the one expected to be declared.")
