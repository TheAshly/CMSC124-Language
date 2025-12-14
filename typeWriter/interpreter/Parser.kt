package typeWriter.interpreter

import typeWriter.interpreter.constructors.Node
import typeWriter.interpreter.constructors.Token
import typeWriter.interpreter.exceptions.WrongSuccessiveSymbolException
import typeWriter.interpreter.keywords.Character
import typeWriter.interpreter.keywords.ReservedWords

class Parser() {

    companion object{
        var functions = hashMapOf<String, Node>()
    }

    var listIterator = mutableListOf<Token>().listIterator()
    var currToken: Token = Token("Placeholder", "Placeholder", null, 1)
    var currLineNum: Int = 0
    var indentLevel: Int = 0
    var expectedIndent: Int = 0

    fun lex(){
        if(listIterator.hasNext()) {
            currToken = listIterator.next()
            if(ErrorChecker.checkContainsSpaces(currToken)){
                currToken = listIterator.next()
                ErrorChecker.checkSpaces(currToken)
            }
        }
    }

    fun parseTokens(tokens: MutableList<Token>): Node? {

        this.listIterator = tokens.listIterator()
        this.currToken = listIterator.next()

        // If the first token is already the end of line there was no expression
        if(ErrorChecker.checkEmptiness(currToken)) return null
        return program()
    }

    fun program(): Node {
        currLineNum = currToken.line
        var programNode = statement()
        while (currLineNum != currToken.line){
            currLineNum = currToken.line
            programNode = Node(programNode, statement(), null)
        }
        return programNode
    }

    fun statement(): Node {
        var stmt: Node
        when(currToken.type){
            ReservedWords.Companion.FUNCTION -> {
                stmt = functionStmt()
            }
            ReservedWords.Companion.RETURN -> {
                stmt = returnStmt()
                ErrorChecker.checkEndingPeriod(currToken)
                lex()
            }
            ReservedWords.Companion.IFSTMT -> {
                stmt = conditionStmt()
            }
            in ReservedWords.Companion.LOOPSTMT -> {
                stmt = loopStmt()
            }
            in ReservedWords.Companion.LOOPHANDLER -> {
                stmt = Node(currToken.type, null, null)
                lex()
                ErrorChecker.checkEndingPeriod(currToken)
                lex()
            }
            ReservedWords.Companion.PRINT -> {
                stmt = printingStmt()
                ErrorChecker.checkEndingPeriod(currToken)
                lex()
            }
            ReservedWords.Companion.IDENTIFIER -> {
                stmt = assigningStmt()
                ErrorChecker.checkEndingPeriod(currToken)
                lex()
            }
            else -> {
                stmt = expression()
                ErrorChecker.checkEndingPeriod(currToken)
                lex()
            }
        }
        return stmt
    }

    fun blockStmt(): Node {
        var subNode: Node
        this.indentLevel += 1
        for(i in 0 until this.indentLevel){
            ErrorChecker.checkIndentDeclaration(currToken)
            lex()
        }
        ErrorChecker.checkExceedingIndent(currToken)

        currLineNum = currToken.line
        subNode = statement()

        var indentCounter = 0
        OUTER@while (currLineNum != currToken.line){
            indentCounter = 0
            for(i in 0 until this.indentLevel){
                if(currToken.type != "indent"){
                    if(this.expectedIndent != this.indentLevel){
                        break@OUTER
                    }
                    this.expectedIndent = 0
                    break
                }
                indentCounter++
                lex()
            }
            ErrorChecker.checkExceedingIndent(currToken)
            currLineNum = currToken.line
            subNode = Node(subNode, statement(), null)
        }
        this.indentLevel -= 1
        this.expectedIndent = indentCounter
        return subNode
    }

    fun functionStmt(): Node {
        var block: Node
        lex()
        ErrorChecker.checkProperIdentifier(currToken)
        val functionName = currToken.lexeme
        ErrorChecker.checkFunctionRedeclaration(functions.get(functionName), currLineNum)
        lex()
        if(currToken.type == "colon"){
            lex()
            val state = "parametered"
            val parameters = parameters()
            block = Node(state,blockStmt(), parameters)
        } else {
            ErrorChecker.checkEndingComma(currToken)
            lex()
            block = Node(null, blockStmt(), null)
        }

        functions[functionName] = block
        return Node(null, null, null)

    }
    fun parameters(): Node? {
        if(currToken.type == ReservedWords.Companion.IDENTIFIER){
            val identifier = currToken.lexeme
            lex()
            ErrorChecker.checkSuccessiveSymbol(currToken, Character.Companion.COMMA)
            lex()
            return Node(parameters(), identifier, null)
        } else
            return null
    }

    fun returnStmt(): Node {
        val operator = currToken.type
        lex()
        ErrorChecker.checkSuccessiveSymbol(currToken, Character.Companion.COMMA)
        lex()
        return Node(operator, expression(), null)
    }

    fun conditionStmt(): Node {
        var condition: Node
        val ifstmt = currToken.type
        lex()
        val expression = expression()
        ErrorChecker.checkEndingComma(currToken)
        lex()
        condition = Node(ifstmt, expression, conditionMoreStmt(blockStmt()))
        return condition

    }
    fun conditionMoreStmt(block: Node): Node {
        var condition = Node(null, block, null)
        when (currToken.type) {
            ReservedWords.Companion.OTHERWISE -> {
                lex()
                val expression = expression()
                ErrorChecker.checkEndingComma(currToken)
                lex()
                condition = Node(expression, block, conditionMoreStmt(blockStmt()))
            }
            ReservedWords.Companion.ELSESTMT -> {
                lex()
                ErrorChecker.checkEndingComma(currToken)
                lex()
                condition = Node(Node("true",null , null),
                    block ,
                    Node(null, blockStmt(), null))
            }
        }
        return condition
    }

    fun loopStmt(): Node {
        var expression: Node
        val loopstmt = currToken.type
        lex()
        if(loopstmt == ReservedWords.Companion.WHILE){
            expression = expression()
            ErrorChecker.checkEndingComma(currToken)
            lex()

        } else {
            expression = forCondition()
            ErrorChecker.checkEndingComma(currToken)
            lex()
        }

        return Node(loopstmt, expression, blockStmt())
    }
    fun forCondition(): Node {
        var operator: String
        if(currToken.type == ReservedWords.Companion.LETTERCOUNT){
            operator = currToken.type
            lex()
            ErrorChecker.checkSuccessiveKeyword(currToken, ReservedWords.Companion.RANGE)
            lex()
            return Node(operator, expression(), null)
        } else{
            var leftexpr = expression()
            if(currToken.type == ReservedWords.Companion.RANGEUP)
                operator = currToken.type
            else {
                ErrorChecker.checkSuccessiveKeyword(currToken, ReservedWords.Companion.RANGEDOWN)
                operator = currToken.type
            }
            lex()
            ErrorChecker.checkSuccessiveKeyword(currToken, ReservedWords.Companion.ASSIGNING)
            lex()
            return Node(operator, leftexpr, expression())
        }
    }

    fun printingStmt(): Node {
        var stmt: Node
        var print = currToken.type
        lex()

        ErrorChecker.checkSuccessiveKeyword(currToken, ReservedWords.Companion.STRINGHANDLER)
        print += " ${currToken.type}"
        lex()

        ErrorChecker.checkSuccessiveSymbol(currToken, Character.Companion.COMMA)
        lex()

        ErrorChecker.checkSuccessiveLiteral(currToken, ReservedWords.Companion.STRING)
        val string = currToken.literal
        lex()

        stmt = Node(print, string, null)

        val references = mutableListOf<Token>()
        var referenceNum = 0
        while(currToken.type == ReservedWords.Companion.POINTER){
            references.add(currToken)
            lex()
            referenceNum++
        }
        if(referenceNum > 0){
            ErrorChecker.checkSuccessiveSymbol(currToken, Character.Companion.COMMA)
            lex()
            ErrorChecker.checkSuccessiveKeyword(currToken, ReservedWords.Companion.REFERENCING)
            lex()
            ErrorChecker.checkSuccessiveKeyword(currToken, ReservedWords.Companion.ASSIGNING)
            lex()
            val pointers = mutableListOf<Token>()
            while(currToken.type == ReservedWords.Companion.IDENTIFIER) {
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

    fun assigningStmt(): Node {
        val identifier = currToken.lexeme
        lex()

        ErrorChecker.checkSuccessiveKeywords(currToken, ReservedWords.Companion.ASSIGNMENTS)
        val assigning = currToken.type
        lex()
        ErrorChecker.checkSuccessiveKeyword(currToken, ReservedWords.Companion.ASSIGNING)
        lex()

        return Node(assigning, identifier, expression())
    }

    // Context-Free Grammar Functions, returns a Node for the parent and left and right children
    // Checks if it follows the proper grammar order and throws and error if not
    // Expression: comparison { ( "or" | "and" ) comparison }, sends an error if the next comparisons doesn't exist
    fun expression(): Node {
        var expr: Node = comparator()

//        ErrorChecker.checkWrongKeyword(currToken.line, currToken.type)
        while (ReservedWords.Companion.BOOLEANCOMPARATOR.contains(currToken.type)){
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
            if (ReservedWords.Companion.VALUECOMPARATOR.contains(currToken.type)) {
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
        while(ReservedWords.Companion.OPERATOR.contains(currToken.type)){
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
        if (ReservedWords.Companion.UNARY.contains(currToken.type)) {
            val operator = currToken.type
            lex()
            expr = Node(operator, factor(), null)
            ErrorChecker.checkVariable(expr, currToken.line)
        } else {
            if(currToken.type == ReservedWords.Companion.CALLFUNC) expr = callStmt()
            else if(currToken.type == ReservedWords.Companion.REQUEST) expr = inputStmt()
            else expr = Node(primary(), null, null)
            lex()
            ErrorChecker.checkRedeclaration(currToken)
        }
        return expr
    }

    // Primary: NUMBER | STRING | "factual" | "faulty" | "empty" | IDENTIFIER
    // Sends an error when the token being parsed is not a Literal
    fun primary(): String? {
        return if (ErrorChecker.checkLiteral(currToken)){
            if(currToken.type == ReservedWords.Companion.IDENTIFIER)
                currToken.lexeme
            else
                currToken.literal
        }
        else
            null
    }

    fun callStmt(): Node {
        var block: Node

        val operator = currToken.type
        lex()
        ErrorChecker.checkProperIdentifier(currToken)
        val functionName = currToken.lexeme
        lex()

        if(currToken.type == "colon"){
            val declarations = declarations()
            block = Node(operator,functionName , declarations)
        } else {
            block = Node(operator, functionName, null)
        }
        return block

    }
    fun declarations(): Node? {
        if(currToken.type == "colon"){
            lex()
        } else if(currToken.type == "period"){
            return null
        }
        else {
            ErrorChecker.checkSuccessiveSymbol(currToken, Character.Companion.COMMA)
            lex()
        }

        val identifier = expression()
        return Node(declarations(), identifier, null)

    }

    fun inputStmt(): Node {
        val operator = currToken.type
        lex()
        ErrorChecker.checkSuccessiveKeyword(currToken, ReservedWords.Companion.INPUT)
        return Node(operator, currToken.type, null)
    }

}



