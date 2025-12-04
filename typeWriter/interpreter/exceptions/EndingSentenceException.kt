package typeWriter.interpreter.exceptions
class EndingSentenceException(line: Int) : Exception("[Line $line] " + "Character/Symbol not found in the alphabet.")