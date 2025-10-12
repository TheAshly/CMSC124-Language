enum class ReservedWords(val token: String){

    // Use these for checking Reserved Keywords

    // Comparators
    EQUAL("equivaling"),    // EQUAL("equal"),
    GREATER("exceeding"),   // GREATER("greater than"),
    LESS("below"),          // LESS("less than"),
    OR("or"),
    AND("and"),

    // Unary
    NEGATION("not"),
    NEGATIVE("invert"),

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
    ADDITION("add"),
    SUBTRACTION("subtract"),
    MULTIPLICATION("multiply"),
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