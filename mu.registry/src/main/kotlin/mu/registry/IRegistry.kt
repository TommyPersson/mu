package mu.registry

interface IRegistry {
    fun listFunctions(): List<String>
    fun createFunction(name: String)
    fun deleteFunction(name: String)

    fun listVersions(functionName: String): List<String>
    fun addVersion(functionName: String, version: String, pkg: FunctionPackage)
    fun removeVersion(functionName: String, version: String)
}