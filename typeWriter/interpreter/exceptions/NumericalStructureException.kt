package typeWriter.interpreter.exceptions
class NumericalStructureException(line: Int) : Exception("[Line $line] " + "Not a proper number.")
