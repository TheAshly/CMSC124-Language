import kotlin.collections.contains

class Parser() {

    // Initializing Parser Variables
    var listIterator = mutableListOf<Token>().listIterator()
    var currToken: Token = Token("Placeholder", "Placeholder", null, 1)
    val checker = ErrorChecker()

    // Gets the next token in the iterable list of tokens
    fun lex(){
        if(listIterator.hasNext()) {
            currToken = listIterator.next()
        }
    }

    // Parses the token list taken from the scanner
    fun parseTokens(tokens: MutableList<Token>): Node? {
        this.listIterator = tokens.listIterator()   // Initializes the Token List to be iterated
        this.currToken = listIterator.next()        // Initializes the first token of the list

        // If the first token is already the end of line there was no expression, meaning an Error
        if(checker.checkEmptiness(currToken)) return null
        return expression()
    }

    // Context-Free Grammar Functions, returns a Node for the parent and left and right children
    // Checks if it follows the proper grammar order and throws and error if not
    // Expression: comparison { ( "or" | "and" ) comparison }, sends an error if the next comparisons doesn't exist
    fun expression(): Node?  {
        var expr: Node? = comparator()

        while (ReservedWords.BOOLEANCOMPARATOR.contains(currToken.type)){
            val operator = currToken.type
            lex()
            val value = comparator()
            expr = Node(operator, expr, value)
            if(!checker.checkVariable(expr, currToken.line)) expr = null
        }
       return expr
    }

    // Comparison: term ["is" (["not"] "equivaling" | ("exceeding"|"below")["or" "equivaling"] ) term]
    // SCREEE WHTIISSSS RULEEEe
    // Sends and error if "is" was used, but did get followed up by a proper comparator
    // and if "or" was used but was not followed by "equivaling"
    fun comparator(): Node? {
        var expr: Node? = term()
        var operator: String
        var placeholder: String
        val value: Node?

        if (currToken.type == "is") {
            lex()
            if(!checker.checkComparator(currToken)) expr = null
            else if (ReservedWords.VALUECOMPARATOR.contains(currToken.type)) {
                operator = currToken.type
                lex()
                if (currToken.type == "or") {
                    placeholder = " ${currToken.type}"
                    lex()
                    if(!checker.checkComparator(currToken)) return null
                    else {
                        operator += placeholder + " ${currToken.type}"
                        lex()
                    }
                }
                value = term()
                expr = Node(operator, expr, value)
                if(!checker.checkVariable(expr, currToken.line)) expr = null

            } else {
                    operator = currToken.type
                    lex()
                    value = term()
                    expr = Node(operator, expr, value)
                    if(!checker.checkVariable(expr, currToken.line)) expr = null
            }
        }
        return expr
    }

    // Term: factor { ( "add" | "subtract"|"multiply"|"over"|"modulo") factor }
    // Sends an error when what comes after an operator does not exist
    fun term(): Node? {
        var expr: Node? = Node(factor(), null, null)
        while(ReservedWords.OPERATOR.contains(currToken.type)){
            val operator = currToken.type
            lex()
            val value = factor()
            expr = Node(operator, expr, value)
            if(!checker.checkVariable(expr, currToken.line)) expr = null
        }
        return expr
    }

    // Factor: ( "invert" | "not" ) factor | primary
    // Sends an error when what comes after the negation does not exist
    fun factor(): Node? {
        var expr: Node?
        if (ReservedWords.UNARY.contains(currToken.type)) {
            val operator = currToken.type
            lex()
            val value = factor()
            expr = Node(operator, value, null)
            if(!checker.checkVariable(expr, currToken.line)) expr = null
        } else {
            val placeholder = primary()
            expr = Node(placeholder, null, null)
            lex()
            if(!checker.checkRedundancy(currToken)) expr = null
        }
        return expr
    }

    // Primary: NUMBER | STRING | "factual" | "faulty" | "empty" | IDENTIFIER
    // Sends an error when the token being parsed is not a Literal
    fun primary(): String? {
        if (!checker.checkLiteral(currToken)) return null
        else {
            if (ReservedWords.LITERAL.contains(currToken.type)) {
                return currToken.literal
            }
            return currToken.type
        }
    }
}

// Errors:
// Margin error: Indention level is wrong (mainly on loops and if)
// Capitalization Error: Capitalizing Word that shouldn't be and vice versa
// Wrong Spelling: Wrong lexeme for token
// Grammar Error: The tokens are not being given properly

