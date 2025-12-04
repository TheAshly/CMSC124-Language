package typeWriter.interpreter.exceptions

class MissingCommaException(line: Int) : Exception("[Line $line] " + "Statement wasn't properly ended using a \",\".")