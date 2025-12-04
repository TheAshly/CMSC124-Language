package typeWriter.interpreter.exceptions

class LackingIndentException(line: Int) : Exception("[Line $line] " + "Expected an Indent but was undeclared.")