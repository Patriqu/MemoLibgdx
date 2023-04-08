package com.siriusbasegames.memo

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor

class DebugInputProcessor(
    private val gameStateHandler: GameStateHandler) : InputProcessor {
    override fun keyDown(keycode: Int): Boolean {
        debugKeys(keycode)
        return false
    }

    private fun debugKeys(keycode: Int) {
        if (keycode == Input.Keys.NUMPAD_0) {
            gameStateHandler.setState(GameState.LEVEL_COMPLETE)
        }
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}