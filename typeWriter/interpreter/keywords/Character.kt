package typeWriter.interpreter.keywords
import kotlin.collections.toHashSet

class Character {

    // Use these for constant Literal Values, Alphabet, Numbers, and Characters
    // Symbols not here will be considered as Parser.Errors
    companion object {
        val UPPERCASE = (('A'..'Z') + 'Ñ').toHashSet()
        val LOWERCASE = (('a'..'z') + 'ñ').toHashSet()
        const val COMMENT = '>'
        const val DECIMAL = '.'
        const val PERIOD = '.'
        const val COMMA = ','
        const val STATEMENT = '"'
        const val SPACE = ' '
        const val INDENT = '\t'
        val NEWLINE = hashSetOf('\n', '\r')
        const val OPENREF = '('
        const val CLOSEREF = ')'
        val NUMBER = (('0'..'9')).toHashSet()
//        val ENDOFLINES = ("'>").toHashSet()
    }
}