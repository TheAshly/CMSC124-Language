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
        if(enumValues<Literals>().any { it.type == token.type } || enumValues<BooleanLiteral>().any { it.type == token.type }) {
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
        if(enumValues<Literals>().any { it.type == token.type } || enumValues<BooleanLiteral>().any { it.type == token.type }){
            return true
        } else {
            println("[Line ${token.line}] Parsing Error: \"${token.lexeme}\" is not a proper Literal.")
            return false
        }
    }

    fun checkComparator(token: Token): Boolean{
        if (enumValues<ValueComparators>().any { it.token == token.type } || enumValues<LiteralComparators>().any { it.token == token.type }) return true
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
}