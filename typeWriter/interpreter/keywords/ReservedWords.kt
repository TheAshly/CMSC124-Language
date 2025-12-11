package typeWriter.interpreter.keywords
class ReservedWords(){
    // Use these for checking Reserved Keywords

    companion object {
        val KEYWORDS = hashSetOf(
            // Function
            "dear", "sincerely", "retrieve",

            // Assignment
            "refers", "equals", "correlates",

            // Literals
            "factual", "faulty", "nothing",

            // Printing and Inputs
            "i", "your", "statement", "referring", "state",

            // Two-way Selectors
            "if", "otherwise", "else",

            // Loops
            "for", "while", "down", "up",
            "range",

            // Checkers
            "in", // Range
            "to", // Assignment
            "its", // Itself
            "is", // Boolean

            // Loop Controls
            "recommence", "abandon",

            // Boolean Comparators
            "or", "and",

            // Literal Comparators
            "equaling", "unlike",

            // Value Comparators
            "exceeding", "below",

            // Unary Operators
            "increment", "decrement", "not", "negative",

            // Arithmetic Operators
            "plus", "minus", "times", "over", "modulo"
            )
        val OPERATORS = hashSetOf(
            // Boolean Comparators
            "or", "and",

            // Literal Comparators
            "equaling", "unlike",

            // Value Comparators
            "exceeding", "below",

            // Unary Operators
            "not", "negative",

            // Arithmetic Operators
            "plus", "minus", "times", "over", "modulo"
        )

        val BOOLEANCOMPARATOR = hashSetOf("or", "and")
        val LITERALCOMPARATOR = hashSetOf("equaling", "unlike")
        val VALUECOMPARATOR = hashSetOf("exceeding", "below", "exceeding or equaling", "below or equaling")
        val UNARY = hashSetOf("not", "negative")
        val OPERATOR = hashSetOf("plus", "minus", "times", "over", "modulo")

        val LITERAL = hashSetOf("Numeric", "Statement", "Preposition", "Null")
        val STRING = "Statement"
        val POINTER = "Reference"

        const val IDENTIFIER = "Identifier"
        val ASSIGNMENTS = hashSetOf("refers", "equals", "correlates")
        const val ASSIGNING = "to"

        const val PRINT = "i"
        const val STRINGHANDLER = "state"
        const val PRINTCALL = "i state"
        const val REFERENCING = "referring"

        const val IFSTMT = "if"
        const val OTHERWISE = "otherwise"
        const val ELSESTMT = "else"

        val LOOPSTMT =  hashSetOf("while", "for")
        const val WHILE = "while"
        const val RANGEDOWN = "down"
        const val RANGEUP = "up"
        const val LETTERCOUNT = "range"
        const val RANGE = "in"
        val LOOPHANDLER = hashSetOf("abandon", "recommence")
        const val BREAK = "abandon"
        const val CONTINUE = "recommence"

        const val FUNCTION = "dear"
        const val RETURN = "sincerely"
        const val CALLFUNC = "retrieve"

        const val REQUEST = "your"
        const val INPUT = "statement"

    }
}