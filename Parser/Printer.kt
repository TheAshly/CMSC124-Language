class Printer {
    
    // For printing of Token List
    fun scannerPrinter(tokens: MutableList<Token>){
         for(i in tokens){
             println("Token: Type=${i.type}, Lexeme=${i.lexeme}, Literal=${i.literal}, Line=${i.line}")
         }
    }
    
    // For printing of the Abstract Syntax Tree
    fun parserTraverser(set: LinkedHashSet<*>?){
        if(set != null)
            for(node in set){
                if(node is Node) {
                    parserPrinter(node)
                }
                if(node is LinkedHashSet<*>) {
                    parserTraverser(node)
                }
                println()
            }
    }

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
            } else if(node.left is String){
                print(" ")
                print(node.left)
            }
            if(node.right is Node){
                print(" ")
                parserPrinter(node.right)
            } else if(node.right is String){
                print(" ")
                print(node.right)
            }
            if(null !in setOf(node.left, node.right)){
                print(")")
            }
        }
    }
}