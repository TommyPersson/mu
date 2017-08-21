package mu.fn.platform.jvm18

import mu.fn.jvm.api.IFunction
import org.xeustechnologies.jcl.JarClassLoader
import org.xeustechnologies.jcl.JclObjectFactory
import java.io.File

class FunctionLoader : IFunctionLoader {
    override fun load(jar: File): IFunction {
        val jcl = JarClassLoader().apply {
            add(jar.path)
        }

        val functionClassName = findFunctionClassName(jcl)
        val jclObjectFactory = JclObjectFactory.getInstance(true)

        return jclObjectFactory.create(jcl, functionClassName) as IFunction
    }

    private fun findFunctionClassName(jcl: JarClassLoader): String {
        return "mu.fn.jvm.testfn.TestFunction"
    }
}