package typeWriter.interpreter

import typeWriter.interpreter.constructors.Node
import typeWriter.interpreter.constructors.Token

class Printer {
    
    // For printing of Token List
    fun scannerPrinter(tokens: MutableList<Token>){
         for(i in tokens){
             println("Token: Type=${i.type}, Lexeme=${i.lexeme}, Literal=${i.literal}, Line=${i.line}")
         }
    }
    
    // For printing of the Abstract Syntax Tree
    fun parserTraverser(set: Node?){
        println()
        if(set != null) {
            if (set.center is Node){
                parserTraverser(set.center)
                if (set.left is Node)
                    parserPrinter(set.left)

            } else {
                parserPrinter(set)
            }
        }
    }

    fun parserPrinter(node: Node?){
        if(node != null){
            print("(")
            if(node.left is Node){
                parserPrinter(node.left)
            } else if(node.left is String){
                print(" ")
                print(node.left)
                print(",")
            } else {
                print(" ")
                print("null")
                print(",")
            }
            if(node.center is Node){
                parserPrinter(node.center)
            } else if(node.center is String){
                print(" ")
                print(node.center)
                print(",")
            }else {
                print(" ")
                print("null")
                print(",")
            }
            if(node.right is Node){
                parserPrinter(node.right)
            } else if(node.right is String){
                print(" ")
                print(node.right)
            } else {
                print(" ")
                print("null")
            }
                print(")")
            print(",")
        }
    }
}