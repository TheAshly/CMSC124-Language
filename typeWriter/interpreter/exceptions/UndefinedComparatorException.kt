package typeWriter.interpreter.exceptions
class UndefinedComparatorException(keyword: Any?, line: Int) : Exception("[Line $line] " + "\"$keyword\" is not a proper comparator.")
