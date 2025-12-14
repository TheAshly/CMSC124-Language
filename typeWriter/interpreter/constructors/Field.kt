package typeWriter.interpreter.constructors
class Field(private val parent: Field? = null) {
    private val declarations = HashMap<String, Any?>()

    private fun updateAncestor(identifier: String, type: Any?): Boolean {
        return if (declarations.containsKey(identifier)) {
            declarations[identifier] = type
            true
        } else {
            parent?.updateAncestor(identifier, type) ?: false
        }
    }

    fun initialize(identifier: String, type: Any?) {
//        val updated = updateAncestor(identifier, type)
//        if (!updated) {
            declarations[identifier] = type
//        }
    }
    fun retrieve(identifier: String): Any? {
        return declarations.get(identifier) ?: parent?.retrieve(identifier)
    }
    fun getParent(): Field {
        return this.parent ?: this
    }
    fun changeField(): Field {
        this.saveField()
        return Field()
    }
    fun saveField(){
        saveField = this
    }
    fun retrieveField(): Field {
        return saveField
    }

    companion object{
        val globalField = Field()
        var saveField = Field()
    }
}