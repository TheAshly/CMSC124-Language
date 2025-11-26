class Evaluator() {
    var currentField = Field.globalField

    fun evaluateProgram(trees: LinkedHashSet<*>){
        for(node in trees){
            if(node is Node) {
                statement(node)
            }
            if(node is LinkedHashSet<*>) {
                currentField = Field(parent = currentField)
                evaluateProgram(node)
                currentField = currentField.getParent()
            }
        }
    }

    fun statement(node: Node) {
        when(val center = node.center){
            in ReservedWords.ASSIGNMENTS -> assigning(node)
            ReservedWords.PRINTCALL -> printing(node)
            else -> expression(node)
        }
    }

    fun assigning(node: Node) {
        val leftNode: Any? = node.left
        var rightNode: Any? = node.right
        when (node.center) {
            "refers" -> {
                if(node.right is Node)
                    rightNode = expression(node.right)
                if (ErrorChecker.checkAssignmentString(node.center, rightNode))
                    currentField.declare((leftNode as String), (rightNode as String))
            }
            "equals" -> {
                if(node.right is Node)
                    rightNode = expression(node.right)
                if (ErrorChecker.checkAssignmentDouble(node.center, rightNode))
                    currentField.declare((leftNode as String), (rightNode as Double))
            }
            "correlates" -> {
                if(node.right is Node)
                    rightNode = expression(node.right)
                if (ErrorChecker.checkAssignmentBoolean(node.center, rightNode))
                    currentField.declare((leftNode as String), (rightNode as Boolean))
            }
        }
    }

    fun printing(node: Node) {
        val leftNode: Any? = node.left
        var rightNode: Any? = node.right

        if(leftNode is String){
            var printstring = leftNode as String
            while (rightNode is Node) {
                if (rightNode.center is String) {
                    if (rightNode.left is String) {
                        val resolvedValue = currentField.resolve(rightNode.left)
                        if(ErrorChecker.checkNullPrinting(resolvedValue))
                            printstring = printstring.replace(rightNode.center, resolvedValue.toString())

                    } else if (rightNode.left is Node) {
                        val expressedValue = expression(rightNode.left)
                        if(ErrorChecker.checkNullPrinting(expressedValue))
                            printstring = printstring.replace(rightNode.center, expressedValue.toString())
                    }
                }
                rightNode = rightNode.right
            }
            println(printstring.removePrefix("\"").removeSuffix("\""))
        }


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
            if(ErrorChecker.checkBothBoolean(node.center, leftNode, rightNode)) {
                when (node.center) {
                    "and" -> return (leftNode as Boolean) && (rightNode as Boolean)
                    "or" -> return (leftNode as Boolean) || (rightNode as Boolean)
                }
            }
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
            if(ErrorChecker.checkBothDouble(node.center, leftNode, rightNode)) {
                when (node.center) {
                    "exceeding" -> return (leftNode as Double) > (rightNode as Double)
                    "below" -> return (leftNode as Double) < (rightNode as Double)
                    "exceeding or equaling" -> return (leftNode as Double) >= (rightNode as Double)
                    "below or equaling" -> return (leftNode as Double) <= (rightNode as Double)
                }
            }
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
            if(ErrorChecker.checkBothDouble(node.center, leftNode, rightNode)) {
                when (node.center) {
                    "plus" -> return (leftNode as Double) + (rightNode as Double)
                    "minus" -> return (leftNode as Double) - (rightNode as Double)
                    "times" -> return (leftNode as Double) * (rightNode as Double)
                    "over" -> return (leftNode as Double) / (rightNode as Double)
                    "modulo" -> return (leftNode as Double) % (rightNode as Double)
                }
            }
        }
        return factor(node)
    }

    fun factor(node: Node): Any? {
        var leftNode: Any? = node.left
        if (ReservedWords.UNARY.contains(node.center)){
            if(node.left is Node)
                leftNode = factor(node.left)

            return if(ErrorChecker.checkUnaryLiterals(node.center, leftNode))
                !(leftNode as Boolean)
            else
                -(leftNode as Double)

        }
        return primary(node)

    }

    fun primary(node: Node): Any? {
        return when (val center = node.center) {
            is Node -> primary(center)
            "true" -> true
            "false" -> false
            "nothing" -> Nothing()
            is String -> {
                center.toDoubleOrNull() ?:
                if (center.contains('"'))
                    center.removePrefix("\"").removeSuffix("\"")
                else
                    currentField.resolve(center)
            }
            else -> center

        }
    }
}


