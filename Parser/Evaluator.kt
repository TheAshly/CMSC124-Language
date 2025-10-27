class Evaluator() {

    // Initializing Parser Variables


    // Parses the token list taken from the scanner
    fun evaluateTree(node: Node?) {
        if(node == null){
            println("wtf")
        }
        else {
            println(expression(node))
        }
    }

    // Context-Free Grammar Functions, returns a Node for the parent and left and right children
    // Checks if it follows the proper grammar order and throws and error if not
    // Expression: comparison { ( "or" | "and" ) comparison }, sends an error if the next comparisons doesn't exist
    fun expression(node: Node): Any?  {
        var leftNode: Any?
        var rightNode: Any?
        if(node.center == "and"){

            if(node.left is Node){
                leftNode = expression(node.left)
            } else {
                leftNode = node.left
            }
            if(node.right is Node){
                rightNode = expression(node.right)
            } else {
                rightNode = node.right
            }
            if(leftNode is Boolean && rightNode is Boolean){
                return leftNode && rightNode
            } else {
                println("and: type mismatch")
                return "wtf"
            }

        } else if(node.center == "or"){

            if(node.left is Node){
                leftNode = expression(node.left)
            } else {
                leftNode = node.left
            }
            if(node.right is Node){
                rightNode = expression(node.right)
            } else {
                rightNode = node.right
            }
            if(leftNode is Boolean && rightNode is Boolean){
                return leftNode || rightNode
            } else {
                println("or: type mismatch")
                return "wtf"
            }

        } else {
            return equality(node)
        }
    }

    // Comparison: term ["is" ("unlike" | "equaling" | ("exceeding"|"below")["or" "equivaring"] ) term]
    // SCREEE WHTIISSSS RULEEEe
    // Sends and error if "is" was used, but did get followed up by a proper comparator
    // and if "or" was used but was not followed by "equivaring"
    fun equality(node: Node): Any? {
        var leftNode: Any?
        var rightNode: Any?
        if(node.center == "equaling"){

            if(node.left is Node){
                leftNode = equality(node.left)
            } else {
                leftNode = node.left
            }
            if(node.right is Node){
                rightNode = equality(node.right)
            } else {
                rightNode = node.right
            }
            return leftNode == rightNode

        } else if(node.center == "unlike"){

            if(node.left is Node){
                leftNode = equality(node.left)
            } else {
                leftNode = node.left
            }
            if(node.right is Node){
                rightNode = equality(node.right)
            } else {
                rightNode = node.right
            }
            return leftNode != rightNode

        } else {
            return comparator(node)
        }
    }

    fun comparator(node: Node): Any? {
        var leftNode: Any?
        var rightNode: Any?
        if(node.center == "exceeding"){

            if(node.left is Node){
                leftNode = comparator(node.left)
            } else {
                leftNode = node.left
            }
            if(node.right is Node){
                rightNode = comparator(node.right)
            } else {
                rightNode = node.right
            }
            if(leftNode is Double && rightNode is Double){
                return leftNode > rightNode
            } else {
                println(">: type mismatch")
                return "wtf"
            }

        } else if(node.center == "below"){

            if(node.left is Node){
                leftNode = comparator(node.left)
            } else {
                leftNode = node.left
            }
            if(node.right is Node){
                rightNode = comparator(node.right)
            } else {
                rightNode = node.right
            }
            if(leftNode is Double && rightNode is Double){
                return leftNode < rightNode
            } else {
                println("<: type mismatch")
                return "wtf"
            }

        } else if (node.center == "exceeding or equaling"){

            if(node.left is Node){
                leftNode = comparator(node.left)
            } else {
                leftNode = node.left
            }
            if(node.right is Node){
                rightNode = comparator(node.right)
            } else {
                rightNode = node.right
            }
            if(leftNode is Double && rightNode is Double){
                return leftNode >= rightNode
            } else {
                println(">=: type mismatch")
                return "wtf"
            }

        } else if(node.center == "below or equaling"){

            if(node.left is Node){
                leftNode = comparator(node.left)
            } else {
                leftNode = node.left
            }
            if(node.right is Node){
                rightNode = comparator(node.right)
            } else {
                rightNode = node.right
            }
            if(leftNode is Double && rightNode is Double){
                return leftNode <= rightNode
            } else {
                println("<=: type mismatch")
                return "wtf"
            }

        } else {
            return term(node)
        }
    }

    // Term: factor { ( "add" | "subtract"|"multiply"|"over"|"modulo") factor }
    // Sends an error when what comes after an operator does not exist
    fun term(node: Node): Any? {
        var leftNode: Any?
        var rightNode: Any?
        if(node.center == "plus"){

            if(node.left is Node){
                leftNode = term(node.left)
            } else {
                leftNode = node.left
            }
            if(node.right is Node){
                rightNode = term(node.right)
            } else {
                rightNode = node.right
            }
            if(leftNode is Double && rightNode is Double){
                return leftNode + rightNode
            } else {
                println("+: type mismatch")
                return "wtf"
            }

        } else if(node.center == "minus"){

            if(node.left is Node){
                leftNode = term(node.left)
            } else {
                leftNode = node.left
            }
            if(node.right is Node){
                rightNode = term(node.right)
            } else {
                rightNode = node.right
            }
            if(leftNode is Double && rightNode is Double){
                return leftNode - rightNode
            } else {
                println("-: ype mismatch")
                return "wtf"
            }

        } else if (node.center == "times"){

            if(node.left is Node){
                leftNode = term(node.left)
            } else {
                leftNode = node.left
            }
            if(node.right is Node){
                rightNode = term(node.right)
            } else {
                rightNode = node.right
            }
            if(leftNode is Double && rightNode is Double){
                return leftNode * rightNode
            } else {
                println("*: type mismatch")
                return "wtf"
            }

        } else if(node.center == "over"){

            if(node.left is Node){
                leftNode = term(node.left)
            } else {
                leftNode = node.left
            }
            if(node.right is Node){
                rightNode = term(node.right)
            } else {
                rightNode = node.right
            }
            if(leftNode is Double && rightNode is Double){
                return leftNode / rightNode
            } else {
                println("/: type mismatch")
                return "wtf"
            }

        } else if(node.center == "modulo"){

            if(node.left is Node){
                leftNode = term(node.left)
            } else {
                leftNode = node.left
            }
            if(node.right is Node){
                rightNode = term(node.right)
            } else {
                rightNode = node.right
            }
            if(leftNode is Double && rightNode is Double){
                return leftNode % rightNode
            } else {
                println("%: type mismatch")
                return "wtf"
            }

        } else {
            if(node.center is Node){
                return factor(node.center)
            }
            return factor(node)
        }
    }

    // Factor: ( "invert" | "not" ) factor | primary
    // Sends an error when what comes after the negation does not exist
    fun factor(node: Node): Any? {
        var leftNode: Any?
//        println("center:${node.center} left:${node.left} right:${node.right}")
        if(node.center == "negative"){

            if(node.left is Node){
                leftNode = factor(node.left)
            } else {
                leftNode = node.left
            }

            if(leftNode is Double){
                return -leftNode
            } else {
                println("nega: type mismatch")
                return "wtf"
            }

        } else if(node.center == "not"){

            if(node.left is Node){
                leftNode = factor(node.left)
            } else {
                leftNode = node.left
            }
            if(leftNode is Boolean){
                return !leftNode
            } else {
                println("not: type mismatch")
                return "wtf"
            }

        } else {
            return primary(node)
        }
    }

    // Primary: NUMBER | STRING | "factual" | "faulty" | "empty" | IDENTIFIER
    // Sends an error when the token being parsed is not a Literal
    fun primary(node: Node): Any? {
        var checker: Double? = null
        if (node.center is Node){
            return primary(node.center)
        } else {
            if (node.center is String){
                checker = node.center.toDoubleOrNull()
            }
            if (node.center == "factual"){
                return true
            } else if (node.center == "faulty"){
                return false
            } else if (checker != null){
                return checker
            } else {
                return node.center
            }
        }
    }
}

// Errors:
// Margin error: Indention level is wrong (mainly on loops and if)
// Capitalization Error: Capitalizing Word that shouldn't be and vice versa
// Wrong Spelling: Wrong lexeme for token
// Grammar Error: The tokens are not being given properly

