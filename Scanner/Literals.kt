class Literals {
    companion object {
        val UPPERCASE = (('A'..'Z') + 'Ñ').toSet()
        val LOWERCASE = (('a'..'z') + 'ñ').toSet()
        val COMMENT = '>'
        val DECIMAL = '.'
        val STATEMENT = '"'
        val SPACE = ' '
        val NUMBER = (('0'..'9')).toSet()


        val NOT = "not"
        val POSITIVE = "positive"
        val NEGATIVE = "negative"
        val OR = "or"
        val AND = "and"
        val STRINGTYPE = "refer"
        val INTTYPE = "equals"
        val EQUAL = "equal"
//        val GREATER = "greater than"
//        val LESS = "less than"
//        val NOTEQUAL = "not equal"
//        val GREATEREQUAL = "greater than or equal"
//        val LESSEQUAL = "less than equal"
        val ADD = "add"
        val SUBTRACT = "subtract"
        val MULTIPLY = "multiply"
        val DIVIDE = "divide"
        val MODULO = "modulo"
        val TRUE = "factual"
        val FALSE = "faulty"
        val BOOLEANSIGNIFIER = "its"
        val RANGESIGNIFIER = "in"
        val ASSIGNCOMPARE = "to"


        val CONTINUE = "Continue"
        val BREAK = "Break"
        val INCREMENT = "Increment"
        val DECREMENT = "Decrement"
        val IF = "If"
        val ELSE = "Else"
//        val OTHERWISE = "Otherwise if"
        val FOR = "For"
        val WHILE = "While"
    }
}