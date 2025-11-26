class Field(private val parent: Field? = null) {
    private val declarations = HashMap<String, Any?>()

    fun declare(identifier: String, type: Any?) {
        declarations[identifier] = type
    }

    fun resolve(identifier: String): Any? {
        return declarations.get(identifier) ?: parent?.resolve(identifier)
    }

    fun getParent(): Field {
        return this.parent ?: this
    }
    companion object{
        val globalField = Field()
    }
}