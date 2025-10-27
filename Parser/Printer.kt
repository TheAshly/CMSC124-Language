class Printer {
    
    // For printing of Token List
    fun scannerPrinter(tokens: MutableList<Token>){
         for(i in tokens){
             println("Token: Type=${i.type}, Lexeme=${i.lexeme}, Literal=${i.literal}, Line=${i.line}")
         }
    }
    
    // For printing of the Abstract Syntax Tree
    fun parserPrinter(node: Node?){
        if(node != null){
            if(null !in setOf(node.left, node.right)){
                print("(")
            }
            if(node.center is Node){
                parserPrinter(node.center)
            } else if(node.center is String){
                print(node.center)
            }
            if(node.left is Node){
                print(" ")
                parserPrinter(node.left)
            }
            if(node.right is Node){
                print(" ")
                parserPrinter(node.right)
            }
            if(null !in setOf(node.left, node.right)){
                print(")")
            }
        }
    }
}