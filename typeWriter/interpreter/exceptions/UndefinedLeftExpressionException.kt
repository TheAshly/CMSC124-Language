package typeWriter.interpreter.exceptions
class UndefinedLeftExpressionException(keyword: Any?, line: Int) : Exception("[Line $line] " + "Cannot \"$keyword} to unexisting variable.")
