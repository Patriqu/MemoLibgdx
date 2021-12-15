package com.siriusbasegames.memo

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class MemoMain : KtxGame<KtxScreen>() {
    private val gameStateHandler = GameStateHandler()

    override fun create() {
        KtxAsync.initiate()

        addScreen(MainScreen(gameStateHandler))
        setScreen<MainScreen>()
    }

    override fun dispose() {
        super.dispose()
        removeScreen<MainScreen>()
    }
}
class MainScreen(private var gameStateHandler: GameStateHandler) : KtxScreen {
    private val virtualHeight = 4F  // meters

    private val batch = SpriteBatch()

    private val topdownCamera = OrthographicCamera()
    private val uiCamera = OrthographicCamera()

    private val gridController: GridController

    private val disposer: Disposer

    private var guiDrawer: GuiDrawer = GuiDrawer(gameStateHandler)

    init {
        uiCamera.setToOrtho(false)

        gridController = GridController(batch, gameStateHandler)

        disposer = Disposer(batch, gridController, guiDrawer)

        input.inputProcessor = GameInputProcessor(gridController, topdownCamera, gameStateHandler)
    }

    override fun render(delta: Float) {
        updateLogic()

        Gdx.gl.glClearColor(1f, 1f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        topdownCamera.update()
        uiCamera.update()

        batch.begin()

        batch.projectionMatrix = topdownCamera.combined
        gridController.draw(topdownCamera)

        batch.projectionMatrix = uiCamera.combined
        guiDrawer.draw(batch)

        batch.end()
    }

    private fun updateLogic() {
        // game state handling
        when(gameStateHandler.getState()) {
            GameState.STARTED -> {
                gameStateHandler.runGame()
            }

            GameState.RUNNING -> {}

            GameState.LOSE -> {}

            GameState.LEVEL_COMPLETE -> {
                guiDrawer.win()
            }

            GameState.NEXT_LEVEL -> {
                guiDrawer.reset()
                gameStateHandler.nextLevel()
                gridController.resetBoard()
            }

            GameState.WIN -> {
                guiDrawer.win()
            }

            GameState.RESET -> {
                guiDrawer.reset()
                gameStateHandler.resetGame()
                gridController.resetBoard()
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        topdownCamera.setToOrtho(false, virtualHeight * width / height.toFloat(), virtualHeight)
    }

    override fun dispose() {
        disposer.dispose()
    }
}

