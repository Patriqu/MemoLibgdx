package com.siriusbasegames.memo

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.assets.disposeSafely

class Disposer(private val batch: SpriteBatch, private val gridController: GridController, private val guiDrawer : GuiDrawer?) {

    fun dispose() {
        batch.disposeSafely()
        gridController.dispose()
        guiDrawer?.dispose()
    }
}