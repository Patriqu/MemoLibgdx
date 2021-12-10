@file:JvmName("Lwjgl3Launcher")

package com.siriusbasegames.memo.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.siriusbasegames.memo.MemoMain

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(MemoMain(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("Memo Libgdx Game - v.0.1")
        setResizable(false)
        setWindowedMode(1280, 720)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
