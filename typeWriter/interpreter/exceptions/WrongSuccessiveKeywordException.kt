package typeWriter.interpreter.exceptions
class WrongSuccessiveKeywordException(line: Int) : Exception("[Line $line] " + "The successive keyword wasn't the one expected to be declared.")
