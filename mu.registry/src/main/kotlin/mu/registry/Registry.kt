package mu.registry

class Registry(
        val account: Account
) : IRegistry {
    val functionNames = mutableListOf<String>()

    override fun listFunctions(): List<String> {
        return functionNames.toList()
    }

    override fun createFunction(name: String) {
        if (functionNames.contains(name)) {
            throw IllegalArgumentException("Duplicate function name")
        }
        functionNames.add(name)
    }

    override fun deleteFunction(name: String) {
        val removed = functionNames.remove(name)
        if (!removed) {
            throw IllegalArgumentException("Unknown function name")
        }
    }

    override fun listVersions(functionName: String): List<String> {
        return emptyList()
    }

    override fun addVersion(functionName: String, version: String, pkg: FunctionPackage) {

    }

    override fun removeVersion(functionName: String, version: String) {

    }
}