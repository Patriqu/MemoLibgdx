package com.siriusbasegames.memo

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3

class GameInputProcessor(private val gridController: GridController, private val camera: Camera, private val gameStateHandler:
    GameStateHandler) : InputProcessor {

    override fun keyDown(keycode: Int): Boolean {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            val actualState = gameStateHandler.getState()
            if (actualState == GameState.LEVEL_COMPLETE) {
                gameStateHandler.setState(GameState.NEXT_LEVEL)
            } else if (actualState == GameState.LOSE || actualState == GameState.WIN) {
                gameStateHandler.setState(GameState.RESET)
            }
        }

        debugKeys()

        return false
    }

    private fun debugKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            gridController.enterTheBreakpoint()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
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
        if (Gdx.input.isTouched(0)) {
            val actualState = gameStateHandler.getState()
            if (actualState == GameState.RUNNING) {
                Gdx.app.log("Mouse", "LMB x=$screenX, y=$screenY")

                val touchPosition = Vector3()
                touchPosition[screenX.toFloat(), screenY.toFloat()] = 0f

                val worldPosition = camera.unproject(touchPosition)
                val x = worldPosition.x
                val y = worldPosition.y

                gridController.revealCard(x, y, camera.viewportWidth)
            } else if (actualState == GameState.LEVEL_COMPLETE) {
                gameStateHandler.setState(GameState.NEXT_LEVEL)
            } else if (actualState == GameState.LOSE || actualState == GameState.WIN) {
                gameStateHandler.setState(GameState.RESET)
            }
        }

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