class Parser() {

    // Initializing Parser Variables
    var listIterator = mutableListOf<Token>().listIterator()
    var currToken: Token = Token("Placeholder", "Placeholder", null, 1)

    // Gets the next token in the iterable list of tokens
    fun lex(){
        if(listIterator.hasNext()) {
            currToken = listIterator.next()
        }
    }

    // Parses the token list taken from the scanner
    fun parseTokens(tokens: MutableList<Token>){
        this.listIterator = tokens.listIterator()   // Initializes the Token List to be iterated
        this.currToken = listIterator.next()        // Initializes the first token of the list

        // If the first token is already the end of line there was no expression, meaning an Error
        if(currToken.type == "end"){
            println("[Line ${currToken.line}] Expression Error: Expecting expression.")
        } else {
            printTree(expression())
        }

    }

    // Context-Free Grammar Functions, returns a Node for the parent and left and right children
    // Checks if it follows the proper grammar order and throws and error if not
    // Expression: comparison { ( "or" | "and" ) comparison }, sends an error if the next comparisons doesn't exist
    fun expression(): Node  {
        var expr = comparator()

        while (currToken.type in setOf("or", "and")) {
            val operator = currToken.type
            lex()
            val value = comparator()
            if(value.checkLeaf() && expr.checkLeaf()) expr = Node(operator, expr, value)
            else {
                if(expr.checkLeaf()){
                    println("[Line ${currToken.line}] Syntax Error: Expecting expression after \"$operator\".")
                } else {
                    println("[Line ${currToken.line}] Syntax Error: Cannot \"$operator\" none existing variable.")
                }
                expr = Node(null, null, null)
            }
        }
       return expr
    }

    // Comparison: term ["is" (["not"] "equivaling" | ("exceeding"|"below")["or" "equivaling"] ) term]
    // SCREEE WHTIISSSS RULEEEe
    // Sends and error if "is" was used, but did get followed up by a proper comparator
    // and if "or" was used but was not followed by "equivaling"
    fun comparator(): Node {
        var expr = term()
        var operator = ""
        var placeholder = ""

        if (currToken.type == "is") {
            lex()
            if (currToken.type in setOf("exceeding", "below")) {
                operator = currToken.type
                lex()
                if (currToken.type == "or") {
                    placeholder = " ${currToken.type}"
                    lex()
                    if (currToken.type == "equivaling") {
                        operator += placeholder + " ${currToken.type}"
                        lex()
                    }
                    else {
                        println("[Line ${currToken.line}] Syntax Error: Expecting comparator after \"or\".")
                        return Node(null, null, null)
                    }
                }
                expr = Node(operator, expr, term())
            } else {

                if (currToken.type == "not") {
                    operator = "${currToken.type} "
                    lex()
                }
                if (currToken.type == "equivaling") {
                    operator += currToken.type
                    lex()
                    expr = Node(operator, expr, term())
                } else {
                    println("[Line ${currToken.line}] Syntax Error: Expecting comparator after \"is\".")
                    expr = Node(null, null, null)
                }
            }
        }
        return expr
    }

    // Term: factor { ( "add" | "subtract"|"multiply"|"over"|"modulo") factor }
    // Sends an error when what comes after an operator does not exist
    fun term(): Node {
        var expr = Node(factor(), null, null)
        while(currToken.type in setOf("add", "subtract", "multiply", "over", "modulo")) {
            val operator = currToken.type
            lex()
            val value = factor()
            if(value.checkLeaf() && expr.checkLeaf()) expr = Node(operator, expr, value)
            else {
                if(expr.checkLeaf()){
                    println("[Line ${currToken.line}] Syntax Error: Expecting factor after \"$operator\".")
                } else {
                    println("[Line ${currToken.line}] Syntax Error: Cannot \"$operator\" to not existing variable.")
                }
                return Node(null, null, null)
            }
        }
        return expr
    }

    // Factor: ( "invert" | "not" ) factor | primary
    // Sends an error when what comes after the negation does not exist
    fun factor(): Node{
        var expr: Node = Node(null, null, null)
        if (currToken.type in setOf("invert", "not")) {
            val operator = currToken.type
            lex()
            val value = factor()
            if(value.checkLeaf()) expr = Node(operator, value, null)
            else {
                println("[Line ${currToken.line}] Syntax Error: Expecting literal after \"$operator\".")
                expr = Node(null, null, null)
            }

        } else {
            val placeholder = primary()
            lex()
            if(placeholder != null){
                if(currToken.type in setOf("Numeric","String", "factual","faulty")) {
                    println("[Line ${currToken.line}] Syntax Error: Another literal after a literal was already declared.")
                } else if("Exception" in currToken.type){
                    println(currToken.type)
                } else if("Error" in currToken.type || currToken.type == "Identifier") {
                    println("[Line ${currToken.line}] Parsing Error: \"${currToken.lexeme}\" is not a keyword.")
                } else {
                    expr = Node(placeholder, null, null)
                }
            }
        }
        return expr
    }

    // Primary: NUMBER | STRING | "factual" | "faulty" | "empty" | IDENTIFIER
    // Sends an error when the token being parsed is not a Literal
    fun primary(): String? {

        if(currToken.type in setOf("Numeric","String")){
            return currToken.literal
        } else if(currToken.type in setOf("factual","faulty")){
            return currToken.type
        } else {
            println("[Line ${currToken.line}] Parsing Error: \"${currToken.lexeme}\" is not a proper Literal.")
            return null
        }

    }

    // For printing of the Abstract Syntax Tree
    fun printTree(parsed: Node){
        if(null !in setOf(parsed.left, parsed.right)){
            print("(")
        }
        if(parsed.center is Node){
            printTree(parsed.center)
        } else if(parsed.center is String){
            print(parsed.center)
        }
        if(parsed.left is Node){
            print(" ")
            printTree(parsed.left)
        }
        if(parsed.right is Node){
            print(" ")
            printTree(parsed.right)
        }
        if(null !in setOf(parsed.left, parsed.right)){
            print(")")
        }
    }
}

// Errors:
// Margin error: Indention level is wrong (mainly on loops and if)
// Capitalization Error: Capitalizing Word that shouldn't be and vice versa
// Wrong Spelling: Wrong lexeme for token
// Grammar Error: The tokens are not being given properly

