enum class ReservedWords(val token: String){

    // Use these for checking Reserved Keywords

    // Value Comparators
    EQUAL("equaling"),    // EQUAL("equal"),
    UNEQUAL("unlike"),
    GREATER("exceeding"),   // GREATER("greater than"),
    LESS("below"),          // LESS("less than"),

    // Boolean Comparators
    OR("or"),
    AND("and"),

    // Unary
    NEGATION("not"),
    NEGATIVE("negative"),

    // Commands
    IF("if"),
    OTHERWISE("otherwise"),
    ELSE("else"),

    FOR("for"),
    WHILE("while"),

    CONTINUE("recommence"),
    BREAK("abandon"),

    CONCATENATION("append"),

    // Assignment
    STRINGTYPE("refers"),
    INTTYPE("equals"),

    // Operators
    ADDITION("plus"),
    SUBTRACTION("minus"),
    MULTIPLICATION("times"),
    DIVISION("over"),
    MODULO("modulo"),

    // Literals
    TRUE("factual"),
    FALSE("faulty"),
    NULL("empty"),

    // Checkers
    RANGE("in"),
    ASSIGNMENT("to"),
    ITSELF("it"),
    BOOLEAN("is");

    // Keywords to be Decided when to/if to be implemented:
    //    INCREMENT("increment"),
    //    DECREMENT("decrement"),

}