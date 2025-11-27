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
                if (ReservedWords.LITERAL.contains(token.type) || token.type == ReservedWords.IDENTIFIER)
                    return true
                else
                    throw UnknownLiteralException(token.lexeme, token.line)
            } catch (e: UnknownLiteralException){
                println(e)
                return false
            }
        }

        fun checkSpaces(token: Token) {
            if (token.type == "space")
                throw NoProperSpacingException(token.line)
        }

        fun checkContainsSpaces(token: Token): Boolean {
            if (token.type == "space")
                return true
            else
                return false
        }

        fun checkRedeclaration(token: Token) {
            if (ReservedWords.LITERAL.contains(token.type) || token.type == ReservedWords.IDENTIFIER)
                throw LiteralRedeclarationException(token.line)
        }

        fun checkVariable(node: Node, line: Int) {
            if (node.left is Node && !node.left.checkLeaf())
                    throw UndefinedLeftExpressionException(node.center, line)
            if (node.right is Node && !node.right.checkLeaf())
                    throw UndefinedRightExpressionException(node.center, line)
        }

        fun checkComparator(token: Token) {
            if (!ReservedWords.VALUECOMPARATOR.contains(token.type) && !ReservedWords.LITERALCOMPARATOR.contains(token.type))
                throw UndefinedComparatorException(token.lexeme, token.line)
        }

        fun checkEndingPeriod(token: Token) {
            if (token.type != "period")
                throw MissingPeriodException(token.line)
        }

        fun checkSuccessiveKeywords(token: Token, keyset: HashSet<String>) {
            if (!keyset.contains(token.type))
                throw WrongSuccessiveKeywordException(token.line)
        }

        fun checkSuccessiveKeyword(token: Token, keyword: String) {
            if (token.type != keyword)
                throw WrongSuccessiveKeywordException(token.line)
        }

        fun checkSuccessiveSymbol(token: Token, symbol: Char) {
            if (token.lexeme[0] != symbol)
                throw WrongSuccessiveSymbolException(token.line)
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
            if (ReservedWords.KEYWORDS.contains(keyword) && !ReservedWords.OPERATORS.contains(keyword))
                throw MisplacedKeywordException(keyword, line)
        }

        fun checkPointersExceedingCount(line: Int, count: Int) {
            if (count < 0)
                throw ExceedingPointersException(line)
        }

        fun checkPointersLackingCount(line: Int, count: Int) {
            if (count > 0)
                throw LackingPointersException(line)
        }

        fun checkPointerCommas(token: Token, count: Int): Boolean {
            if (token.lexeme[0] != Character.COMMA && count > 0)
                throw WrongSuccessiveSymbolException(token.line)
            else if (token.lexeme[0] != Character.PERIOD && count == 0)
                throw MissingPeriodException(token.line)
            else if(token.lexeme[0] == Character.COMMA && count > 0)
                return true
            return false
        }

        // Evaluator Error Checkers
        fun checkUnaryLiterals(operator: Any?, left: Any?): Boolean {
            return if (operator == "not" && left is Boolean)
                true
            else if (operator == "negative" && left is Double)
                false
            else
                throw WrongUnaryLiteralException(operator)
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

        fun checkBothDouble(operator: Any?, left: Any?, right: Any?): Boolean {
            try {
                if (left is Double && right is Double)
                    return true
                throw MismatchingLiteralsException(operator)
            } catch (e: MismatchingLiteralsException){
                println(e)
                return false
            }
        }

        fun checkAssignmentDouble(operator: Any?, value: Any?): Boolean {
            try {
                if (value is Double || value == null)
                    return true
                throw MismatchingAssignmentException(operator)
            } catch (e: MismatchingAssignmentException){
                println(e)
                return false
            }
        }

        fun checkAssignmentString(operator: Any?, value: Any?): Boolean {
            try {
                if (value is String || value == null)
                    return true
                throw MismatchingAssignmentException(operator)
            } catch (e: MismatchingAssignmentException){
                println(e)
                return false
            }
        }

        fun checkAssignmentBoolean(operator: Any?, value: Any?): Boolean {
            try {
                if (value is Boolean || value == null)
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
    }
}