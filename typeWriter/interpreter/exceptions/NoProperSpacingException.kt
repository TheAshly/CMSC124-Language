package typeWriter.interpreter.exceptions
class NoProperSpacingException(line: Int) : Exception("[Line $line] " + "No proper spacing after Keyword(s)/Literal(s).")
