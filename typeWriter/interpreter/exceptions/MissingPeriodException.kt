package typeWriter.interpreter.exceptions
class MissingPeriodException(line: Int) : Exception("[Line $line] " + "Statement wasn't properly ended using a \".\".")
