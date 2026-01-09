package dev.aperso.composite.skia

import org.jetbrains.skia.DirectContext
import org.lwjgl.opengl.WGL

object SkiaContext {
    private var wglContext: Long = 0

    fun initialize() {
        if (wglContext != 0L) return
        wglContext = WGL.wglCreateContext(WGL.wglGetCurrentDC())
        WGL.wglShareLists(WGL.wglGetCurrentContext(), wglContext)
    }

    val directContext: DirectContext by lazy {
        DirectContext.makeGL()
    }

    fun run(runnable: Runnable) {
        val oldContext = WGL.wglGetCurrentContext()
        WGL.wglMakeCurrent(WGL.wglGetCurrentDC(), wglContext)
        try {
            runnable.run()
        } finally {
            WGL.wglMakeCurrent(WGL.wglGetCurrentDC(), oldContext)
        }
    }
}