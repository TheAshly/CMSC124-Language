package typeWriter.interpreter.exceptions
class TokenScanningException(line: Int) : Exception("[Line $line] " + "Character/Symbol not found in the alphabet.")
