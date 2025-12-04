package typeWriter.interpreter

import typeWriter.interpreter.constructors.Field
import typeWriter.interpreter.constructors.Node
import typeWriter.interpreter.keywords.ReservedWords
import typeWriter.interpreter.types.Nothing

class Evaluator() {
    var currentField = Field.Companion.globalField

    fun typeWrite(statement: String){
        statement.forEach { char ->
            print(char)
//            Thread.sleep(60L)
        }
        Thread.sleep(80L)
        println()
    }

    fun evaluateProgram(tree: Node){
        if (tree.center is Node){
            evaluateProgram(tree.center)
            if (tree.left is Node)
                statement(tree.left)

        } else {
            statement(tree)
        }
    }

    fun statement(node: Node): Any? {
        when(node.center){
            in ReservedWords.Companion.ASSIGNMENTS -> assigning(node)
            ReservedWords.Companion.PRINTCALL -> printing(node)
            ReservedWords.Companion.IFSTMT -> {
                currentField = Field(parent = currentField)
                condition(node)
                currentField = currentField.getParent()

            }
            in ReservedWords.Companion.LOOPSTMT -> {
                currentField = Field(parent = currentField)
                looping(node)
                currentField = currentField.getParent()

            }
            else -> expression(node)
        }
        return null
    }

    fun assigning(node: Node) {
        val leftNode: Any? = node.left
        var rightNode: Any? = node.right
        when (node.center) {
            "refers" -> {
                if(node.right is Node)
                    rightNode = expression(node.right)
                if (ErrorChecker.checkAssignmentString(node.center, rightNode))
                    currentField.initialize((leftNode as String), rightNode)
            }
            "equals" -> {
                if(node.right is Node)
                    rightNode = expression(node.right)
                if (ErrorChecker.checkAssignmentDouble(node.center, rightNode))
                    currentField.initialize((leftNode as String), rightNode)
            }
            "correlates" -> {
                if(node.right is Node)
                    rightNode = expression(node.right)
                if (ErrorChecker.checkAssignmentBoolean(node.center, rightNode))
                    currentField.initialize((leftNode as String), rightNode)
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
                        val resolvedValue = currentField.retrieve(rightNode.left)
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
            typeWrite(printstring.removePrefix("\"").removeSuffix("\""))
        }


    }

    fun condition(node: Node) {
        var leftNode: Any? = node.left
        if(node.left is Node){
            leftNode = expression(node.left)
        }
        ErrorChecker.checkExpressionBool(leftNode)
        if(node.right is Node){
            conditionBlocks(leftNode as Boolean, node.right)
        }
    }

    fun conditionBlocks(result: Boolean, node: Node) {
        var centerNode: Any? = node.center
        if(result){

            if(node.left is Node){
                evaluateProgram(node.left)
            }
        } else {
            if(node.center is Node){
                centerNode = expression(node.center)
                ErrorChecker.checkExpressionBool(centerNode)
                if(node.right is Node){
                    conditionBlocks(centerNode as Boolean, node.right)
                }
            }
        }
    }
    fun looping(node: Node) {
        var leftNode: Any? = node.left
        if(node.center == ReservedWords.Companion.WHILE){
            if(node.left is Node){
                leftNode = expression(node.left)
            }
            ErrorChecker.checkExpressionBool(leftNode)
            while(leftNode as Boolean){
                if(node.left is Node){
                    leftNode = expression(node.left)
                }
                ErrorChecker.checkExpressionBool(leftNode)
                if(node.right is Node){
                    evaluateProgram(node.right)
                }
            }
        } else{
            if(node.left is Node)
                for(i in forCondition(node.left))
                    if(node.right is Node){
                        evaluateProgram(node.right)
                    }
        }
    }

    fun forCondition(node: Node): IntProgression {
        var leftNode: Any? = node.left
        var rightNode: Any? = node.right
        if(node.left is Node){
            leftNode = expression(node.left)
        }
        if(node.right is Node){
            rightNode = expression(node.right)
        }
        ErrorChecker.checkBothBoolean(node.center, leftNode, rightNode)

        return when (node.center) {
            "down" -> (leftNode as Double).toInt() downTo (rightNode as Double).toInt()
            else -> (leftNode as Double).toInt()..(rightNode as Double).toInt()
        }
    }

    fun expression(node: Node): Any?  {
        var leftNode: Any? = node.left
        var rightNode: Any? = node.right
        if (ReservedWords.Companion.BOOLEANCOMPARATOR.contains(node.center)){
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
        if (ReservedWords.Companion.LITERALCOMPARATOR.contains(node.center)){
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
        if (ReservedWords.Companion.VALUECOMPARATOR.contains(node.center)){
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
        if (ReservedWords.Companion.OPERATOR.contains(node.center)){
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
                    "modulo" -> return (leftNode as Double).mod(rightNode as Double)
                }
            }
        }
        return factor(node)
    }

    fun factor(node: Node): Any? {
        var leftNode: Any? = node.left
        if (ReservedWords.Companion.UNARY.contains(node.center)){
            if(node.left is Node)
                leftNode = factor(node.left)

            if(ErrorChecker.checkUnaryLiteralsBool(node.center, leftNode))
                return !(leftNode as Boolean)
            else if(ErrorChecker.checkUnaryLiteralsDouble(node.center, leftNode))
                return -(leftNode as Double)

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
                    currentField.retrieve(center)
            }
            else -> center

        }
    }
}


