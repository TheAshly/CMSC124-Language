class Character {

    // Use these for constant Literal Values, Alphabet, Numbers, and Characters
    // Symbols not here will be considered as Errors
    companion object {
        val UPPERCASE = (('A'..'Z') + 'Ñ').toHashSet()
        val LOWERCASE = (('a'..'z') + 'ñ').toHashSet()
        const val COMMENT = '>'
        const val DECIMAL = '.'
        const val PERIOD = '.'
        const val COMMA = ','
        const val STATEMENT = '"'
        const val SPACE = ' '
        val NUMBER = (('0'..'9')).toHashSet()
    }
}