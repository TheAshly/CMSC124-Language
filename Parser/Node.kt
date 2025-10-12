class Node(val center: Any?, val left: Any?, val right: Any?){

    // Checks the Leaf of the current node and recurses until it sees a literal/null value
    fun checkLeaf(): Boolean = when (center) {
        is Node -> center.checkLeaf()
        null -> false
        else -> true
    }
}


// Node class for easier passing of values in Parser in form of Syntax Tree
// Center -> Parent of Tree, Left and Right -> Children in the Tree