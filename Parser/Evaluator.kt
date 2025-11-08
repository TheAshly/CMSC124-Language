class Evaluator() {

    val checker = ErrorChecker()

    fun evaluateTree(tree: Node): Any? {
        return expression(tree)
    }

    fun expression(node: Node): Any?  {
        var leftNode: Any? = node.left
        var rightNode: Any? = node.right
        if (ReservedWords.BOOLEANCOMPARATOR.contains(node.center)){
            if(node.left is Node){
                leftNode = expression(node.left)
            }
            if(node.right is Node){
                rightNode = expression(node.right)
            }
            if(checker.checkBothBoolean(node.center, leftNode, rightNode)) {
                when (node.center) {
                    "and" -> return (leftNode as Boolean) && (rightNode as Boolean)
                    "or" -> return (leftNode as Boolean) || (rightNode as Boolean)
                }
            }
            return null

        }
        return equality(node)

    }

    fun equality(node: Node): Any? {
        var leftNode: Any? = node.left
        var rightNode: Any? = node.right
        if (ReservedWords.LITERALCOMPARATOR.contains(node.center)){
            if(node.left is Node){
                leftNode = equality(node.left)
            }
            if(node.right is Node){
                rightNode = equality(node.right)
            }
            when (node.center) {
                "equaling" -> return leftNode == rightNode
                "unlike" -> return leftNode != rightNode
            }
        }
        return comparator(node)
    }

    fun comparator(node: Node): Any? {
        var leftNode: Any? = node.left
        var rightNode: Any? = node.right
        if (ReservedWords.VALUECOMPARATOR.contains(node.center)){
            if(node.left is Node){
                leftNode = comparator(node.left)
            }
            if(node.right is Node){
                rightNode = comparator(node.right)
            }
            if(checker.checkBothDouble(node.center, leftNode, rightNode)) {
                when (node.center) {
                    "exceeding" -> return (leftNode as Double) > (rightNode as Double)
                    "below" -> return (leftNode as Double) < (rightNode as Double)
                    "exceeding or equaling" -> return (leftNode as Double) >= (rightNode as Double)
                    "below or equaling" -> return (leftNode as Double) <= (rightNode as Double)
                }
            }
            return null
        }
        return term(node)
    }

    fun term(node: Node): Any? {
        var leftNode: Any? = node.left
        var rightNode: Any? = node.right
        if (ReservedWords.OPERATOR.contains(node.center)){
            if(node.left is Node){
                leftNode = term(node.left)
            }
            if(node.right is Node){
                rightNode = term(node.right)
            }
            if(checker.checkBothDouble(node.center, leftNode, rightNode)) {
                when (node.center) {
                    "plus" -> return (leftNode as Double) + (rightNode as Double)
                    "minus" -> return (leftNode as Double) - (rightNode as Double)
                    "times" -> return (leftNode as Double) * (rightNode as Double)
                    "over" -> return (leftNode as Double) / (rightNode as Double)
                    "modulo" -> return (leftNode as Double) % (rightNode as Double)
                }
            }
            return null
        }
        if(node.center is Node){
            return factor(node.center)
        }
        return factor(node)
    }

    fun factor(node: Node): Any? {
        var leftNode: Any? = node.left
        if (ReservedWords.UNARY.contains(node.center)){
            if(node.left is Node){
                leftNode = factor(node.left)
            }
            if(checker.checkIfBoolean(leftNode)){
               if(node.center == "not") return !(leftNode as Boolean)
            } else if(checker.checkIfDouble(leftNode)){
                if(node.center == "negative") return -(leftNode as Double)
            }
            return null
        }
        return primary(node)

    }

    fun primary(node: Node): Any? {
        return when (val center = node.center) {
            is Node -> primary(center)
            "factual" -> true
            "faulty" -> false
            is String -> center.toDoubleOrNull() ?: center
            else -> center
        }
    }
}


