//// Scanner Parser.Errors
//class TokenScanningException(line: Int) : Exception("[Line $line] " + "Character/Symbol not found in the alphabet.")
//
//class EndingSentenceException(line: Int) : Exception("[Line $line] " + "Character/Symbol not found in the alphabet.")
//
//class NumericalStructureException(line: Int) : Exception("[Line $line] " + "Not a proper number.")
//
//class LexemeStructureException(line: Int) : Exception("[Line $line] " + "Word doesn't follow proper lexeme structure.")
//
//class LexemeCapitalizationException(keyword: String, line: Int) : Exception("[Line $line] " + "Did you mean $keyword?")
//
//class LexemeSpellingException(keyword: String, line: Int) : Exception("[Line $line] " + "Did you mean $keyword?")
//
//class UnknownKeywordException(line: Int) : Exception("[Line $line] " + "Reserved Keyword not found in dictionary.")
//
//// Parser Parser.Errors
//class UnknownLiteralException(lexeme: String, line: Int) : Exception("[Line $line] " + "\"$lexeme\" is not a proper Literal.")
//
//class NoProperSpacingException(line: Int) : Exception("[Line $line] " + "No proper spacing after Keyword(s)/Literal(s).")
//
//class LiteralRedeclarationException(line: Int) : Exception("[Line $line] " + "Another literal after a literal was already declared.")
//
//class UndefinedLeftExpressionException(keyword: Any?, line: Int) : Exception("[Line $line] " + "Cannot \"$keyword} to unexisting variable.")
//
//class UndefinedRightExpressionException(keyword: Any?, line: Int) : Exception("[Line $line] " + "Expecting expression after \"$keyword\".")
//
//class UndefinedComparatorException(keyword: Any?, line: Int) : Exception("[Line $line] " + "\"$keyword\" is not a proper comparator.")
//
//class MissingPeriodException(line: Int) : Exception("[Line $line] " + "Statement wasn't properly ended using a \".\".")
//
//class WrongSuccessiveKeywordException(line: Int) : Exception("[Line $line] " + "The successive keyword wasn't the one expected to be declared.")
//
//class WrongSuccessiveSymbolException(line: Int) : Exception("[Line $line] " + "The successive symbol wasn't the one expected to be declared.")
//
//class WrongSuccessiveLiteralException(line: Int) : Exception("[Line $line] " + "The successive literal wasn't the one expected to be declared.")
//
//class MisplacedKeywordException(keyword: Any?, line: Int) : Exception("[Line $line] " + "\"$keyword\" shouldn't be used in an expression but declared otherwise.")
//
//class LackingPointersException(line: Int) : Exception("[Line $line] " + "Amount of Pointers are lacking compared to references declared.")
//
//class ExceedingPointersException(line: Int) : Exception("[Line $line] " + "Amount of Pointers are exceeding compared to references declared.")
//
//// Evaluator Parser.Errors
//class WrongUnaryLiteralException(operator: Any?) : Exception("Cannot \"$operator\" the corresponding literal.")
//
//class MismatchingLiteralsException(operator: Any?) : Exception("Cannot \"${operator}\" the corresponding mismatching literal(s).")
//
//class MismatchingAssignmentException(operator: Any?) : Exception("Cannot assign the corresponding literal(s) to mismatching assignment declaration of \"$operator\".")
//
//class NullPointerException() : Exception("Cannot reference a null pointer.")
//
//
//
//
//
