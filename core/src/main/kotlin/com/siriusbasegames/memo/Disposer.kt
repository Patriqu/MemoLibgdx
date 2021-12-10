package com.siriusbasegames.memo

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.assets.disposeSafely

class Disposer(private val batch: SpriteBatch, private val floor: Floor, private val winLoseHandle : WinLoseHandle) {

    fun dispose() {
        batch.disposeSafely()
        floor.dispose()
        winLoseHandle.dispose()
    }
}