import ReservedWords

class Parser() {

    // Initializing Parser Variables
    var listIterator = mutableListOf<Token>().listIterator()
    var currToken: Token = Token("Placeholder", "Placeholder", null, 1)
    var currLineNum: Int = 0
    var indentLevel: Int = 0

    // Gets the next token in the iterable list of tokens
    fun lex(){
        if(listIterator.hasNext()) {
            currToken = listIterator.next()
            if(ErrorChecker.checkContainsSpaces(currToken)){
                currToken = listIterator.next()
                ErrorChecker.checkSpaces(currToken)
            }
        }
    }

    // Parses the token list taken from the scanner
    fun parseTokens(tokens: MutableList<Token>): LinkedHashSet<Any>? {

        this.listIterator = tokens.listIterator()
        this.currToken = listIterator.next()

        // If the first token is already the end of line there was no expression
        if(ErrorChecker.checkEmptiness(currToken)) return null
        return program()
    }

    fun program(): LinkedHashSet<Any>  {
        currLineNum = currToken.line
        val nodeLinkedHashSet = LinkedHashSet<Any>()
        nodeLinkedHashSet.add(statement())

        while (currLineNum != currToken.line){
            currLineNum = currToken.line
            nodeLinkedHashSet.add(statement())
        }
        return nodeLinkedHashSet
    }

    fun statement(): Any  {
        var stmt: Any

        when(currToken.type){
            ReservedWords.IDENTIFIER -> {
                stmt = assigningStmt()
                ErrorChecker.checkEndingPeriod(currToken)
                lex()
            }
            ReservedWords.PRINT -> {
                stmt = printingStmt()
                ErrorChecker.checkEndingPeriod(currToken)
                lex()
            }
            else ->
                if(currToken.type == "indent") {
                    stmt = blockStmt()
                }
                else {
                    stmt = expression()
                    ErrorChecker.checkEndingPeriod(currToken)
                    lex()
                }
        }
        return stmt
    }

    fun assigningStmt(): Node {
        var stmt: Node
        val identifier = currToken.lexeme
        lex()

        ErrorChecker.checkSuccessiveKeywords(currToken, ReservedWords.ASSIGNMENTS)
        val assigning = currToken.type
        lex()

        ErrorChecker.checkSuccessiveKeyword(currToken, ReservedWords.ASSIGNING)
        lex()
        stmt = Node(assigning, identifier, expression())

        return stmt
    }

    fun printingStmt(): Node {
        var stmt: Node
        var print = currToken.type
        lex()

        ErrorChecker.checkSuccessiveKeyword(currToken, ReservedWords.STRINGHANDLER)
        print += " ${currToken.type}"
        lex()

        ErrorChecker.checkSuccessiveSymbol(currToken, Character.COMMA)
        lex()

        ErrorChecker.checkSuccessiveLiteral(currToken, ReservedWords.STRING)
        val string = currToken.literal
        lex()

        stmt = Node(print, string, null)

        val references = mutableListOf<Token>()
        var referenceNum = 0
        while(currToken.type == ReservedWords.POINTER){
            references.add(currToken)
            lex()
            referenceNum++
        }
        if(referenceNum > 0){
            ErrorChecker.checkSuccessiveSymbol(currToken, Character.COMMA)
            lex()
            ErrorChecker.checkSuccessiveKeyword(currToken, ReservedWords.REFERENCING)
            lex()
            ErrorChecker.checkSuccessiveKeyword(currToken, ReservedWords.ASSIGNING)
            lex()
            val pointers = mutableListOf<Token>()
            while(currToken.type == ReservedWords.IDENTIFIER) {
                referenceNum--
                ErrorChecker.checkPointersExceedingCount(currToken.line, referenceNum)
                pointers.add(currToken)
                lex()
                if(ErrorChecker.checkPointerCommas(currToken, referenceNum)) lex()
            }
            ErrorChecker.checkPointersLackingCount(currToken.line, referenceNum)
            var refNode: Node? = null
            for (i in references.lastIndex downTo 0){
                refNode = Node(references[i].lexeme, pointers[i].lexeme, refNode)
            }
            stmt = Node(print, string, refNode)
        }
        return stmt
    }

    fun blockStmt(): LinkedHashSet<Any>  {
        while(currToken.type == "indent") {
            this.indentLevel++
            lex()
        }
        currLineNum = currToken.line
        val subNodeLinkedHashSet = LinkedHashSet<Any>()
        subNodeLinkedHashSet.add(statement())

        var indentCounter: Int = 0
        while (currLineNum != currToken.line){
            indentCounter = 0
            while(currToken.type == "indent") {
                indentCounter++
                lex()
            }
            if(indentCounter < this.indentLevel) break
            currLineNum = currToken.line
            subNodeLinkedHashSet.add(statement())

        }
        this.indentLevel = indentCounter
        return subNodeLinkedHashSet
    }

    // Context-Free Grammar Functions, returns a Node for the parent and left and right children
    // Checks if it follows the proper grammar order and throws and error if not
    // Expression: comparison { ( "or" | "and" ) comparison }, sends an error if the next comparisons doesn't exist
    fun expression(): Node  {
        var expr: Node = comparator()

        ErrorChecker.checkWrongKeyword(currToken.line, currToken.type)
        while (ReservedWords.BOOLEANCOMPARATOR.contains(currToken.type)){
            val operator = currToken.type
            lex()
            expr = Node(operator, expr, comparator())
            ErrorChecker.checkVariable(expr, currToken.line)
        }
       return expr
    }

    // Comparison: term ["is" (["not"] "equivaling" | ("exceeding"|"below")["or" "equivaling"] ) term]
    // SCREEE WHTIISSSS RULEEEe
    // Sends and error if "is" was used, but did get followed up by a proper comparator
    // and if "or" was used but was not followed by "equivaling"
    fun comparator(): Node {
        var expr: Node = term()
        var operator: String

        if (currToken.type == "is") {
            lex()
            ErrorChecker.checkComparator(currToken)
            if (ReservedWords.VALUECOMPARATOR.contains(currToken.type)) {
                operator = currToken.type
                lex()
                if (currToken.type == "or") {
                    operator += " ${currToken.type}"
                    lex()
                    ErrorChecker.checkComparator(currToken)
                    operator += " ${currToken.type}"
                    lex()
                }
                expr = Node(operator, expr, term())
                ErrorChecker.checkVariable(expr, currToken.line)

            } else {
                    operator = currToken.type
                    lex()
                    expr = Node(operator, expr, term())
                    ErrorChecker.checkVariable(expr, currToken.line)
            }
        }
        return expr
    }

    // Term: factor { ( "add" | "subtract"|"multiply"|"over"|"modulo") factor }
    // Sends an error when what comes after an operator does not exist
    fun term(): Node {
        var expr: Node = factor()
        while(ReservedWords.OPERATOR.contains(currToken.type)){
            val operator = currToken.type
            lex()
            expr = Node(operator, expr, factor())
            ErrorChecker.checkVariable(expr, currToken.line)
        }
        return expr
    }

    // Factor: ( "invert" | "not" ) factor | primary
    // Sends an error when what comes after the negation does not exist
    fun factor(): Node {
        var expr: Node
        if (ReservedWords.UNARY.contains(currToken.type)) {
            val operator = currToken.type
            lex()
            expr = Node(operator, factor(), null)
            ErrorChecker.checkVariable(expr, currToken.line)
        } else {
            expr = Node(primary(), null, null)
            lex()
            ErrorChecker.checkRedeclaration(currToken)
        }
        return expr
    }

    // Primary: NUMBER | STRING | "factual" | "faulty" | "empty" | IDENTIFIER
    // Sends an error when the token being parsed is not a Literal
    fun primary(): String? {
        return if (ErrorChecker.checkLiteral(currToken)){
            if(currToken.type == ReservedWords.IDENTIFIER)
                currToken.lexeme
            else
                currToken.literal
        }
        else
            null
    }
}

// Parser.Errors:
// Margin error: Indention level is wrong (mainly on loops and if)
// Grammar Error: The tokens are not being given properly

