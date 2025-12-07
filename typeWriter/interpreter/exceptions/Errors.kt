package typeWriter.interpreter.exceptions
//
//class ExceedingIndentException(line: Int) : Exception("[Line $line] " + "Indent value exceeding current level of Indention.")
//class MissingCommaException(line: Int) : Exception("[Line $line] " + "Statement wasn't properly ended using a \",\".")
//class NonBoolExpressionException() : Exception("Expected a boolean return but received a non-bool expression.")
//class LackingIndentException(line: Int) : Exception("[Line $line] " + "Expected an Indent but was undeclared.")
class NonStringExpressionException() : Exception("Expected a string return but received a non-bool expression.")
class InvalidStringOperationException() : Exception("Using an operand that is not \"plus\" on a string expression.")
class ReturnValue(value: String) : Exception(value)