package mu.fn.platform.jvm18

import mu.fn.jvm.api.IFunction
import java.io.File

interface IFunctionLoader {
    fun load(jar: File): IFunction
}