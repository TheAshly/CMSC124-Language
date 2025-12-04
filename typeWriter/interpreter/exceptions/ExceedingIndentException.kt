package typeWriter.interpreter.exceptions

class ExceedingIndentException(line: Int) : Exception("[Line $line] " + "Indent value exceeding current level of Indention.")