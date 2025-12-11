package typeWriter.interpreter.exceptions

class FunctionRedeclarationException(line: Int) : Exception("[Line $line] " + "Trying to declare a function that already has been declared.")
