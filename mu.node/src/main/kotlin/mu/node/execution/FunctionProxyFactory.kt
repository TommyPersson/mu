package mu.node.execution

import mu.fn.platform.base.IFunctionProxy
import mu.fn.platform.base.FunctionMetadata
import mu.fn.platform.base.IFunctionPlatform
import org.xeustechnologies.jcl.JarClassLoader
import org.xeustechnologies.jcl.JclObjectFactory
import java.io.File
import java.util.jar.JarFile

class FunctionProxyFactory : IFunctionProxyFactory {

    companion object {
        private val jars = listOf(
                File("mu.fn.platform.jvm18/build/libs/mu.fn.platform.jvm18-1.0-SNAPSHOT-all.jar")
        )
        private val serviceEntry = "META-INF/services/mu.fn.platform.base.IFunctionPlatform"
    }


    private val jcl = JarClassLoader().apply {
        addAll(jars.map { it.absolutePath })
    }

    private val jclObjectFactory = JclObjectFactory.getInstance(true)

    private val platforms: Map<String, IFunctionPlatform> = loadPlatforms()

    suspend override fun create(metadata: FunctionMetadata, functionFile: File): IFunctionProxy {
        val platform = platforms[metadata.platformKey] ?: throw Exception("Unsupported function type")

        // TODO reuse proxy for repeated function invokes
        return platform.createProxy(metadata, functionFile)
    }

    private fun loadPlatforms(): Map<String, IFunctionPlatform> {
        val platforms = findPlatformClassNames(jars).map {
            jclObjectFactory.create(jcl, it) as IFunctionPlatform
        }.associateBy { it.key }

        platforms.forEach {
            println("Loaded function platform: ${it.value.name} <${it.key}>")
        }

        return platforms
    }

    private fun findPlatformClassNames(jars: List<File>): List<String> {
        return jars.map(::JarFile).map {
            val entry = it.getJarEntry(serviceEntry)
            it.getInputStream(entry).readBytes().toString(Charsets.UTF_8)
        }
    }
}