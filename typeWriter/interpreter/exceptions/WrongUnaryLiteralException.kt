package typeWriter.interpreter.exceptions
class WrongUnaryLiteralException(operator: Any?) : Exception("Cannot \"$operator\" the corresponding literal.")
