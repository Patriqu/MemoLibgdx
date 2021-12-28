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
class MainScreen(gameStateHandler: GameStateHandler) : KtxScreen {
    private val virtualHeight = 4F  // meters

    private val batch = SpriteBatch()

    private val topdownCamera = OrthographicCamera()
    private val uiCamera = OrthographicCamera()

    private val gridController: GridController

    private val disposer: Disposer

    private val guiDrawer: GuiDrawer = GuiDrawer(gameStateHandler)

    private val updater: Updater

    init {
        uiCamera.setToOrtho(false)

        gridController = GridController(batch, gameStateHandler)

        disposer = Disposer(batch, gridController, guiDrawer)

        input.inputProcessor = GameInputProcessor(gridController, topdownCamera, gameStateHandler)

        updater = Updater(gameStateHandler, guiDrawer, gridController)
    }

    override fun render(delta: Float) {
        updater.update()

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

    override fun resize(width: Int, height: Int) {
        topdownCamera.setToOrtho(false, virtualHeight * width / height.toFloat(), virtualHeight)
    }

    override fun dispose() {
        disposer.dispose()
    }
}

