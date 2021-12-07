@file:JvmName("DesktopLauncher")

package com.siriusbasegames.memo.desktop

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.siriusbasegames.memo.MemoMain

/** Launches the desktop (LWJGL) application. */
fun main() {
    LwjglApplication(MemoMain(), LwjglApplicationConfiguration().apply {
        title = "MemoLibgdx"
        width = 640
        height = 480
        intArrayOf(128, 64, 32, 16).forEach{
            addIcon("libgdx$it.png", Files.FileType.Internal)
        }
    })
}
