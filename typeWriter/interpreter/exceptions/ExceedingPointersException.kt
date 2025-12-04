package typeWriter.interpreter.exceptions
class ExceedingPointersException(line: Int) : Exception("[Line $line] " + "Amount of Pointers are exceeding compared to references declared.")