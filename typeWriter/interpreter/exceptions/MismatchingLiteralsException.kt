package typeWriter.interpreter.exceptions
class MismatchingLiteralsException(operator: Any?) : Exception("Cannot \"${operator}\" the corresponding mismatching literal(s).")
