package typeWriter.interpreter.exceptions
class UnknownKeywordException(line: Int) : Exception("[Line $line] " + "Reserved Keyword not found in dictionary.")
