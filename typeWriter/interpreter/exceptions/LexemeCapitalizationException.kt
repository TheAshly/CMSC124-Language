package typeWriter.interpreter.exceptions
class LexemeCapitalizationException(keyword: String, line: Int) : Exception("[Line $line] " + "Did you mean $keyword?")
