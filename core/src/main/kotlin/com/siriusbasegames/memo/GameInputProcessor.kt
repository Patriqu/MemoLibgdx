package com.siriusbasegames.memo

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3

class GameInputProcessor(private val floor: Floor, private val camera: Camera, private val winLoseHandle : WinLoseHandle,
                         private val disposer: Disposer) : InputProcessor {

    override fun keyDown(keycode: Int): Boolean {
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            floor.enterTheBreakpoint()
        }

        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (Gdx.input.isTouched(0)) {
            if (!winLoseHandle.isWinOrLose()) {
                Gdx.app.log("Mouse", "LMB. x=$screenX, y=$screenY")

                val touchPosition = Vector3()
                touchPosition[screenX.toFloat(), screenY.toFloat()] = 0f

                val worldPosition = camera.unproject(touchPosition)
                val x = worldPosition.x
                val y = worldPosition.y

                floor.revealCard(x, y, camera)
            } else {
                memoMain?.dispose()
                memoMain?.create()
            }
        }

        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

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