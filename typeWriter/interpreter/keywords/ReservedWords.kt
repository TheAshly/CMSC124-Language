package typeWriter.interpreter.keywords
class ReservedWords(){
    // Use these for checking Reserved Keywords

    companion object {
        val KEYWORDS = hashSetOf(
            // Printing and Inputs
            "i", "you", "state", "referring",

            // Two-way Selectors
            "if", "otherwise", "else",

            // Loops
            "for", "while", "down", "up",
            "range",

            // Checkers
            "of", // Range
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
            "plus", "minus", "times", "over", "modulo",

            // Assignment
            "refers", "equals", "correlates", "pertains",

            // Literals
            "factual", "faulty", "nothing",

            // Function
            "dear", "sincerely", "retrieve"
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
        val OPERATOR = hashSetOf("plus", "minus", "times", "over", "modulo")
        val UNARY = hashSetOf("not", "negative")

        val LITERAL = hashSetOf("Numeric", "Sentence", "Preposition", "Null")
        val STRING = "Sentence"
        val POINTER = "Reference"
        val BOOLEANLITERAL = hashSetOf("factual", "faulty")

        const val IDENTIFIER = "Identifier"
        val ASSIGNMENTS = hashSetOf("refers", "equals", "correlates", "pertains")
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
        const val FOR = "for"
        const val RANGEDOWN = "down"
        const val RANGEUP = "up"
        const val LETTERCOUNT = "range"
        const val RANGE = "of"

        const val FUNCTION = "dear"
        const val RETURN = "sincerely"
        const val CALLFUNC = "retrieve"

    }

    // Keywords to be Decided when to/if to be implemented:
    // Append
    // Dear
    // Sincerely
    // Exponent
}