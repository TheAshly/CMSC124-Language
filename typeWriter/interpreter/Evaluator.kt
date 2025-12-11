package typeWriter.interpreter

import typeWriter.interpreter.constructors.Field
import typeWriter.interpreter.constructors.Node
import typeWriter.interpreter.keywords.ReservedWords
import typeWriter.interpreter.types.Nothing
import typeWriter.interpreter.exceptions.ReturnValue
import typeWriter.interpreter.exceptions.LoopHandlerValue

import javax.sound.sampled.AudioSystem
import java.io.InputStream
import java.io.BufferedInputStream

fun playBellSound() {
    val inputStream: InputStream? = object {}.javaClass.getResourceAsStream("/sounds/bell.wav")

    if (inputStream != null) {
        try {
            val bufferedIn = BufferedInputStream(inputStream)

            val clip = AudioSystem.getClip()
            clip.open(AudioSystem.getAudioInputStream(bufferedIn))
            clip.start()

        } catch (e: Exception) {
            println("Error playing sound from resource: ${e.message}")
            e.printStackTrace()
        }
    } else {
        println("Error: Cannot find /sounds/bell.wav inside the JAR file.")
    }
}

fun playTypewriterSound() {
    val inputStream: InputStream? = object {}.javaClass.getResourceAsStream("/sounds/typewriter.wav")

    if (inputStream != null) {
        try {
            val bufferedIn = BufferedInputStream(inputStream)

            val clip = AudioSystem.getClip()
            clip.open(AudioSystem.getAudioInputStream(bufferedIn))
            clip.start()

        } catch (e: Exception) {
            println("Error playing sound from resource: ${e.message}")
            e.printStackTrace()
        }
    } else {
        println("Error: Cannot find /sounds/bell.wav inside the JAR file.")
    }
}

class Evaluator() {
    var currentField = Field.Companion.globalField

    fun typeWrite(statement: String){
        statement.forEach { char ->
            playTypewriterSound()
            Thread.sleep(10L)
            print(char)
            Thread.sleep(110L)
        }
        playBellSound()
        Thread.sleep(240L)
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
            ReservedWords.Companion.RETURN -> {
                var leftNode: Any?
                if(node.left is Node){
                    leftNode = expression(node.left)
                    throw ReturnValue(leftNode.toString())
                }
            }
            ReservedWords.Companion.IFSTMT -> {
                currentField = Field(parent = currentField)
                try {
                    condition(node)
                    currentField = currentField.getParent()
                } catch (e: ReturnValue){
                    currentField = currentField.getParent()
                    throw ReturnValue(e.message.toString())
                }

            }
            in ReservedWords.Companion.LOOPSTMT -> {
                currentField = Field(parent = currentField)
                try {
                    looping(node)
                    currentField = currentField.getParent()
                } catch (e: ReturnValue){
                    currentField = currentField.getParent()
                    throw ReturnValue(e.message.toString())
                }
            }
            in ReservedWords.Companion.LOOPHANDLER -> {
                if(node.center is String)
                    throw LoopHandlerValue(node.center)
            }
            ReservedWords.Companion.PRINTCALL -> printing(node)
            in ReservedWords.Companion.ASSIGNMENTS -> assigning(node)
            else -> expression(node)
        }
        return null
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
        if (node.center == ReservedWords.Companion.WHILE) {

            // Use an infinite loop and manage the condition internally
            while (true) {
                var leftNode: Any? = null // Declare inside the loop so it's fresh every time

                // Guaranteed re-evaluation of the condition expression
                if (node.left is Node) {
                    leftNode = expression(node.left)
                }
                // Note: You should handle the 'else' case here if 'node.left' can be null or something else

                // Check if the condition is still true
                ErrorChecker.checkExpressionBool(leftNode) // Ensure it's a boolean
                if (!(leftNode as Boolean)) {
                    break // Exit the loop if the condition is now false
                }

                // --- Execute the loop body (node.right) ---
                if (node.right is Node) {
                    try {
                        evaluateProgram(node.right)
                    } catch (e: LoopHandlerValue) {
                        if (e.message.toString() == ReservedWords.Companion.BREAK)
                            break
                        else if (e.message.toString() == ReservedWords.Companion.CONTINUE) {
                            continue
                        }
                    }
                }
            }
        } else{
            if(node.left is Node){
                leftNode = forCondition(node.left)
            }

            when(leftNode){
                is String -> {
                    for(i in leftNode){
                        currentField.initialize("Letter", "$i")
                        if(node.right is Node){
                            try {
                                evaluateProgram(node.right)
                            } catch (e: LoopHandlerValue){
                                if(e.message.toString() == ReservedWords.Companion.BREAK)
                                    break
                                else if(e.message.toString() == ReservedWords.Companion.CONTINUE)
                                    continue
                            }
                        }
                    }
                }
                is IntProgression -> {
                    for(i in leftNode){
                        currentField.initialize("Count", i)
                        if(node.right is Node){
                            try {
                                evaluateProgram(node.right)
                            } catch (e: LoopHandlerValue){
                                if(e.message.toString() == ReservedWords.Companion.BREAK)
                                    break
                                else if(e.message.toString() == ReservedWords.Companion.CONTINUE)
                                    continue
                            }
                        }
                    }
                }
            }

        }
    }
    fun forCondition(node: Node): Any? {
        var leftNode: Any? = node.left
        var rightNode: Any? = node.right
        if(node.left is Node){
            leftNode = expression(node.left)
        }
        if(node.right is Node){
            rightNode = expression(node.right)
        }

        return when (node.center) {
            "down" -> {
                ErrorChecker.checkBothDouble(node.center, leftNode, rightNode)
                (leftNode as Double).toInt() downTo (rightNode as Double).toInt()
            }
            "up" -> {
                ErrorChecker.checkBothDouble(node.center, leftNode, rightNode)
                (leftNode as Double).toInt()..(rightNode as Double).toInt()
            }
            else -> {
                ErrorChecker.checkExpressionString(leftNode)
                expression(node.left as Node)
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

    fun assigning(node: Node) {
        val leftNode: Any? = node.left
        var rightNode: Any? = node.right
        when (node.center) {
            "refers" -> {
                if(node.right is Node)
                    rightNode = "\"${expression(node.right)}\""
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
                if (ErrorChecker.checkAssignmentBoolean(node.center, rightNode))
                    currentField.initialize((leftNode as String), rightNode)
            }
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
            if(ErrorChecker.checkBothDouble(node.center, leftNode, rightNode))
                when (node.center) {
                    "exceeding" -> return (leftNode as Double) > (rightNode as Double)
                    "below" -> return (leftNode as Double) < (rightNode as Double)
                    "exceeding or equaling" -> return (leftNode as Double) >= (rightNode as Double)
                    "below or equaling" -> return (leftNode as Double) <= (rightNode as Double)
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
            if(ErrorChecker.checkBothSimilar(node.center, leftNode, rightNode)) {
                if(ErrorChecker.checkSafeBothDouble(leftNode, rightNode))
                    when (node.center) {
                        "plus" -> return (leftNode as Double) + (rightNode as Double)
                        "minus" -> return (leftNode as Double) - (rightNode as Double)
                        "times" -> return (leftNode as Double) * (rightNode as Double)
                        "over" -> return (leftNode as Double) / (rightNode as Double)
                        "modulo" -> return (leftNode as Double).mod(rightNode as Double)
                    }
                else{
                    ErrorChecker.checkStringOperation(node.center)
                    return (leftNode as String) + (rightNode as String)
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
            "retrieve" -> {
                try {
                    callingFunc(node)
                    currentField = currentField.getParent()
                } catch (e: ReturnValue){
                    val value = primary(Node(e.message.toString(), null, null))
                    currentField = currentField.getParent()
                    return value
                }
            }
            "your" -> {
                return primary(Node(readln(), null, null))
            }
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

    fun callingFunc(node: Node) {
        var leftNode: Any? = node.left
        val rightNode: Any? = node.right
        var declarations = LinkedHashSet<Any?>()
        var parameters = LinkedHashSet<String>()

        if(rightNode is Node){
            declarations = getDeclarations(rightNode)
        }
        currentField = Field(parent = currentField)

        if(leftNode is String){
            leftNode = Parser.Companion.functions.get(leftNode)
        }
        if(leftNode is Node){
            if(leftNode.center != null){
                if(leftNode.right is Node){
                    parameters = getParameters(leftNode.right)
                }
                val combinedList = parameters.zip(declarations)

                for ((parameter, declaration) in combinedList) {
                        currentField.initialize(parameter, declaration)
                }
                if(leftNode.left is Node)
                    evaluateProgram(leftNode.left)
            } else {
                if(leftNode.left is Node)
                    evaluateProgram(leftNode.left)
            }
        }
    }
    fun getDeclarations(node: Node): LinkedHashSet<Any?>{
        var subNode = node
        val declaration = LinkedHashSet<Any?>()
        if(subNode.left is Node)
            declaration.add(expression(subNode.left))
        while(subNode.center is Node){
            subNode = subNode.center
            if(subNode.left is Node)
                declaration.add(expression(subNode.left))
        }
        return declaration
    }
    fun getParameters(node: Node): LinkedHashSet<String>{
        var subNode = node
        val declaration = LinkedHashSet<String>()
        if(subNode.left is String)
            declaration.add(subNode.left)
        while(subNode.center is Node){
            subNode = subNode.center
            if(subNode.left is String)
                declaration.add(subNode.left)
        }
        return declaration
    }

//    ReservedWords.Companion.CALLFUNC -> {
//
//    }
//    "pertains" -> {
//        try {
//            if(rightNode is Node){
//                statement(rightNode)
//            }
//        } catch (e: ReturnValue){
//            rightNode = e.message.toString()
//        }
//        rightNode = expression(Node(rightNode, null, null))
//        currentField.initialize((leftNode as String), rightNode)
//    }
}


