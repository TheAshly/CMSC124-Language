package typeWriter.interpreter.exceptions
class LexemeSpellingException(keyword: String, line: Int) : Exception("[Line $line] " + "Did you mean $keyword?")
