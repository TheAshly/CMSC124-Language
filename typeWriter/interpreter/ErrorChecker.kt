package typeWriter.interpreter

import typeWriter.interpreter.constructors.Node
import typeWriter.interpreter.constructors.Token
import typeWriter.interpreter.exceptions.EndingSentenceException
import typeWriter.interpreter.exceptions.ExceedingPointersException
import typeWriter.interpreter.exceptions.LackingPointersException
import typeWriter.interpreter.exceptions.LexemeCapitalizationException
import typeWriter.interpreter.exceptions.LexemeSpellingException
import typeWriter.interpreter.exceptions.LexemeStructureException
import typeWriter.interpreter.exceptions.LiteralRedeclarationException
import typeWriter.interpreter.exceptions.MismatchingAssignmentException
import typeWriter.interpreter.exceptions.MismatchingLiteralsException
import typeWriter.interpreter.exceptions.MisplacedKeywordException
import typeWriter.interpreter.exceptions.MissingPeriodException
import typeWriter.interpreter.exceptions.NoProperSpacingException
import typeWriter.interpreter.exceptions.NullPointerException
import typeWriter.interpreter.exceptions.NumericalStructureException
import typeWriter.interpreter.exceptions.TokenScanningException
import typeWriter.interpreter.exceptions.UndefinedComparatorException
import typeWriter.interpreter.exceptions.UndefinedLeftExpressionException
import typeWriter.interpreter.exceptions.UndefinedRightExpressionException
import typeWriter.interpreter.exceptions.UnknownKeywordException
import typeWriter.interpreter.exceptions.UnknownLiteralException
import typeWriter.interpreter.exceptions.WrongSuccessiveKeywordException
import typeWriter.interpreter.exceptions.WrongSuccessiveLiteralException
import typeWriter.interpreter.exceptions.WrongSuccessiveSymbolException
import typeWriter.interpreter.exceptions.WrongUnaryLiteralException
import typeWriter.interpreter.exceptions.ExceedingIndentException
import typeWriter.interpreter.exceptions.MissingCommaException
import typeWriter.interpreter.exceptions.NonBoolExpressionException
import typeWriter.interpreter.exceptions.LackingIndentException
import typeWriter.interpreter.exceptions.NonStringExpressionException
import typeWriter.interpreter.exceptions.InvalidStringOperationException
import typeWriter.interpreter.exceptions.InvalidIdentifierException
import typeWriter.interpreter.exceptions.FunctionRedeclarationException

import typeWriter.interpreter.keywords.Character
import typeWriter.interpreter.keywords.ReservedWords
import typeWriter.interpreter.types.Nothing

class ErrorChecker {

    companion object {
        // Scanner Error Checkers
        fun checkCharNull(symbol: Char?, line: Int): Boolean {
            try {
                if (symbol != null) throw TokenScanningException(line)
                return true
            } catch (e: TokenScanningException){
                println(e)
                return false
            }
        }

        fun checkSentenceStructure(checker: Boolean, line: Int): Boolean {
            try {
                if (!checker)
                    throw EndingSentenceException(line)
                return true
            } catch (e: EndingSentenceException){
                println(e)
                return false
            }
        }

        fun checkNumberStructure(checker: Boolean, line: Int): Boolean {
            try {
                if (!checker)
                    throw NumericalStructureException(line)
                return true
            } catch (e: NumericalStructureException){
                println(e)
                return false
            }
        }

        fun checkLexemeStructure(checker: Boolean, line: Int): Boolean {
            try {
                if (!checker)
                    throw LexemeStructureException(line)
                return true
            } catch (e: LexemeStructureException){
                println(e)
                return false
            }
        }

        fun checkLexemeCapitalization(leniency: Boolean, lexeme: String, keyword: String, edit: Int, line: Int): Boolean {
            try {
                if(lexeme.equals(keyword, ignoreCase = true)){
                    if(leniency || lexeme == keyword)
                        return true
                    else
                        throw LexemeCapitalizationException(keyword, line)
                }
            } catch (e: LexemeCapitalizationException){
                println(e)
            }
            // if(edit <= keyword.length.floorDiv(2))
            try {
                if(edit <= 2)
                    throw LexemeSpellingException(keyword, line)
            } catch (e: LexemeSpellingException){
                println(e)
            }
            return false
        }

        fun checkKeywordExistence(checker: Boolean, line: Int): Boolean {
            try {
                if (!checker) throw UnknownKeywordException(line)
                return true
            } catch (e: UnknownKeywordException){
                println(e)
                return false
            }
        }


        // Parser Error Checkers
        fun checkEmptiness(token: Token): Boolean {
            return token.type == "EOF"
        }

        fun checkLiteral(token: Token): Boolean {
            try {
                if (ReservedWords.Companion.LITERAL.contains(token.type) || token.type == ReservedWords.Companion.IDENTIFIER)
                    return true
                else
                    throw UnknownLiteralException(token.lexeme, token.line)
            } catch (e: UnknownLiteralException){
                println(e)
                return false
            }
        }

        fun checkSpaces(token: Token) {
            try {
                if (token.type == "space")
                    throw NoProperSpacingException(token.line)
            } catch (e: NoProperSpacingException){
                println(e)
            }
        }

        fun checkContainsSpaces(token: Token): Boolean {
            if (token.type == "space")
                return true
            else
                return false
        }

        fun checkRedeclaration(token: Token) {
            try {
                if (ReservedWords.Companion.LITERAL.contains(token.type) || token.type in hashSetOf(ReservedWords.Companion.IDENTIFIER, ReservedWords.Companion.CALLFUNC,
                        ReservedWords.Companion.REQUEST))
                    throw LiteralRedeclarationException(token.line)
            } catch (e: LiteralRedeclarationException){
                println(e)
            }
        }

        fun checkVariable(node: Node, line: Int) {
            try {
                if (node.left is Node && !node.left.checkLeaf())
                    throw UndefinedLeftExpressionException(node.center, line)
                if (node.right is Node && !node.right.checkLeaf())
                    throw UndefinedRightExpressionException(node.center, line)
            } catch (e: UndefinedLeftExpressionException){
                println(e)
            } catch (e: UndefinedRightExpressionException){
                println(e)
            }
        }

        fun checkComparator(token: Token) {
            try {
                if (!ReservedWords.Companion.VALUECOMPARATOR.contains(token.type) && !ReservedWords.Companion.LITERALCOMPARATOR.contains(token.type))
                    throw UndefinedComparatorException(token.lexeme, token.line)
            } catch (e: UndefinedComparatorException){
                println(e)
            }
        }

        fun checkEndingPeriod(token: Token) {
            try {
                if (token.type != "period")
                    throw MissingPeriodException(token.line)
            } catch (e: MissingPeriodException){
                println(e)
            }
        }

        fun checkSuccessiveKeywords(token: Token, keyset: HashSet<String>) {
            try {
                if (!keyset.contains(token.type))
                    throw WrongSuccessiveKeywordException(token.line)
            } catch (e: WrongSuccessiveKeywordException){
                println(e)
            }
        }

        fun checkSuccessiveKeyword(token: Token, keyword: String) {
            try {
                if (token.type != keyword)
                    throw WrongSuccessiveKeywordException(token.line)
            } catch (e: WrongSuccessiveKeywordException){
                println(e)
            }
        }

        fun checkSuccessiveSymbol(token: Token, symbol: Char) {
            try {
                if (token.lexeme[0] != symbol)
                    throw WrongSuccessiveSymbolException(token.line)
            } catch (e: WrongSuccessiveSymbolException){
                println(e)
            }
        }

        fun checkSuccessiveLiteral(token: Token, type: String): Boolean {
            try {
                if (token.type != type)
                    throw WrongSuccessiveLiteralException(token.line)
                return true
            } catch (e: WrongSuccessiveLiteralException){
                println(e)
                return false
            }
        }

        fun checkWrongKeyword(line: Int, keyword: String) {
            try {
                if (ReservedWords.Companion.KEYWORDS.contains(keyword) && !ReservedWords.Companion.OPERATORS.contains(keyword))
                    throw MisplacedKeywordException(keyword, line)
            } catch (e: MisplacedKeywordException){
                println(e)
            }
        }

        fun checkPointersExceedingCount(line: Int, count: Int) {
            try {
                if (count < 0)
                    throw ExceedingPointersException(line)
            } catch (e: ExceedingPointersException){
                println(e)
            }
        }

        fun checkPointersLackingCount(line: Int, count: Int) {
            try {
                if (count > 0)
                    throw LackingPointersException(line)
            } catch (e: LackingPointersException){
                println(e)
            }
        }

        fun checkPointerCommas(token: Token, count: Int): Boolean {
            try {
                if (token.lexeme[0] != Character.Companion.COMMA && count > 0)
                    throw WrongSuccessiveSymbolException(token.line)
                else if (token.lexeme[0] != Character.Companion.PERIOD && count == 0)
                    throw MissingPeriodException(token.line)
                else if(token.lexeme[0] == Character.Companion.COMMA && count > 0)
                    return true
            } catch (e: WrongSuccessiveSymbolException){
                println(e)
            } catch (e: MissingPeriodException){
                println(e)
            }
            return false
        }

        fun checkIndentDeclaration(token: Token) {
            try {
                if (token.type != "indent")
                    throw LackingIndentException(token.line)
            } catch (e: LackingIndentException){
                println(e)
            }
        }

        fun checkExceedingIndent(token: Token) {
            try {
                if (token.type == "indent")
                    throw ExceedingIndentException(token.line)
            } catch (e: ExceedingIndentException){
                println(e)
            }
        }

        fun checkEndingComma(token: Token) {
            try {
                if (token.type != "comma")
                    throw MissingCommaException(token.line)
            } catch (e: MissingCommaException){
                println(e)
            }
        }
        fun checkProperIdentifier(token: Token) {
            try {
                if (token.type != ReservedWords.Companion.IDENTIFIER)
                    throw InvalidIdentifierException(token.line)
            } catch (e: InvalidIdentifierException){
                println(e)
            }
        }
        fun checkFunctionRedeclaration(node: Node?, line: Int) {
            try {
                if (node != null)
                    throw FunctionRedeclarationException(line)
            } catch (e: FunctionRedeclarationException){
                println(e)
            }
        }

        // Evaluator Error Checkers
        fun checkUnaryLiteralsBool(operator: Any?, left: Any?): Boolean {
            try {
                if (operator == "not"){
                    if(left is Boolean)
                        return true
                    else
                        throw WrongUnaryLiteralException(operator)
                }
            }catch (e: WrongUnaryLiteralException){
                println(e)
            }
            return false
        }
        fun checkUnaryLiteralsDouble(operator: Any?, left: Any?): Boolean {
            try {
                if (operator == "negative"){
                    if(left is Double)
                        return true
                    else
                        throw WrongUnaryLiteralException(operator)
                }
            }catch (e: WrongUnaryLiteralException){
                println(e)
            }
            return false
        }
        fun checkBothBoolean(operator: Any?, left: Any?, right: Any?): Boolean {
            try {
                if (left is Boolean && right is Boolean)
                    return true
                throw MismatchingLiteralsException(operator)
            } catch (e: MismatchingLiteralsException){
                println(e)
                return false
            }
        }

        fun checkAssignmentDouble(operator: Any?, value: Any?): Boolean {
            try {
                if (value is Double || value is Nothing)
                    return true
                throw MismatchingAssignmentException(operator)
            } catch (e: MismatchingAssignmentException){
                println(e)
                return false
            }
        }

        fun checkAssignmentString(operator: Any?, value: Any?): Boolean {
            try {
                if (value is String || value is Nothing)
                    return true
                throw MismatchingAssignmentException(operator)
            } catch (e: MismatchingAssignmentException){
                println(e)
                return false
            }
        }

        fun checkAssignmentBoolean(operator: Any?, value: Any?): Boolean {
            try {
                if (value is Boolean || value is Nothing)
                    return true
                throw MismatchingAssignmentException(operator)
            } catch (e: MismatchingAssignmentException){
                println(e)
                return false
            }
        }

        fun checkNullPrinting(value: Any?): Boolean {
            try {
                if (value == null)
                    throw NullPointerException()
                return true
            } catch (e: NullPointerException){
                println(e)
                return false
            }
        }

        fun checkExpressionBool(left: Any?): Boolean {
            try {
                if(left is Boolean)
                    return true
                else
                    throw NonBoolExpressionException()
            }catch (e: NonBoolExpressionException){
                println(e)
            }
            return false
        }
        fun checkExpressionString(left: Any?): Boolean {
            try {
                if(left is String)
                    return true
                else
                    throw NonStringExpressionException()
            }catch (e: NonStringExpressionException){
                println(e)
            }
            return false
        }
        fun checkBothSimilar(operator: Any?, left: Any?, right: Any?): Boolean {
            try {
                if (left is Double && right is Double || left is String && right is String)
                    return true
                throw MismatchingLiteralsException(operator)
            } catch (e: MismatchingLiteralsException){
                println(e)
                return false
            }
        }
        fun checkStringOperation(operator: Any?) {
            try {
                if (operator != "plus") throw InvalidStringOperationException()
            } catch (e: InvalidStringOperationException){
                println(e)
            }

        }
        fun checkBothDouble(operator: Any?, left: Any?, right: Any?): Boolean  {
            try {
                if (left is Double && right is Double || left is String && right is String)
                    return true
                throw MismatchingLiteralsException(operator)
            } catch (e: MismatchingLiteralsException){
                println(e)
                return false
            }
        }
        fun checkSafeBothDouble(left: Any?, right: Any?): Boolean  {
            if (left is Double && right is Double)
                return true
            return false
        }
    }
}