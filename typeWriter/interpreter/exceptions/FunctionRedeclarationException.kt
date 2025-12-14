package typeWriter.interpreter.exceptions

class InvalidIdentifierException(line: Int) : Exception("[Line $line] " + "Expecting Identifier declaration but something else was declared.")
