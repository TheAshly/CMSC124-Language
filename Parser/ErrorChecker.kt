class ErrorChecker {
    fun checkVariable(node: Node, line: Int): Boolean{
        if(node.left is Node){
            if(!node.left.checkLeaf()){
                println("[Line ${line}] Syntax Error: Cannot \"${node.center}\" to unexisting variable.")
                return false
            }
        }
        if(node.right is Node){
            if(!node.right.checkLeaf()){
                println("[Line ${line}] Syntax Error: Expecting expression after \"${node.center}\".")
                return false
            }
        }
        return true
    }
    fun checkRedundancy(token: Token): Boolean{
        if(ReservedWords.LITERAL.contains(token.type) || ReservedWords.BOOLEANLITERAL.contains(token.type)) {
            println("[Line ${token.line}] Syntax Error: Another literal after a literal was already declared.")
            return false
        } else if("Exception" in token.type){
            println(token.type)
            return false
        } else if("Error" in token.type || token.type == "Identifier") {
            println("[Line ${token.line}] Parsing Error: \"${token.lexeme}\" is not a keyword.")
            return false
        } else {
            return true
        }
    }
    fun checkLiteral(token: Token): Boolean{
        if(ReservedWords.LITERAL.contains(token.type) || ReservedWords.BOOLEANLITERAL.contains(token.type)){
            return true
        } else {
            println("[Line ${token.line}] Parsing Error: \"${token.lexeme}\" is not a proper Literal.")
            return false
        }
    }

    fun checkComparator(token: Token): Boolean{
        if (ReservedWords.VALUECOMPARATOR.contains(token.type) || ReservedWords.LITERALCOMPARATOR.contains(token.type)) return true
        else {
            println("[Line ${token.line}] Parsing Error: \"${token.lexeme}\" is not a proper comparator.")
            return false
        }
    }

    fun checkEmptiness(token: Token): Boolean{
        if(token.type == "end"){
            println("[Line ${token.line}] Expression Error: Expecting expression.")
            return true
        } else {
            return false
        }
    }

    fun checkBothBoolean(operator: Any?, left: Any?, right: Any?): Boolean{
        if(left is Boolean && right is Boolean){
            return true
        } else {
            println("[Line 1] Type Mismatch: Cannot \"${operator}\" mismatching literal(s).")
            return false
        }

    }
    fun checkBothDouble(operator: Any?, left: Any?, right: Any?): Boolean {
        if (left is Double && right is Double) {
            return true
        } else {
            println("[Line 1] Type Mismatch: Cannot \"${operator}\" mismatching literal(s).")
            return false
        }
    }
    fun checkIfBoolean(left: Any?): Boolean{
        if (left is Boolean) {
            return true
        } else {
            println("[Line 1] Type Mismatch: Cannot not a non bool literal.")
            return false
        }
    }
    fun checkIfDouble(left: Any?): Boolean{
        if (left is Double) {
            return true
        } else {
            println("[Line 1] Type Mismatch: Cannot negate a non numeric literal.")
            return false
        }
    }
}