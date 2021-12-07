package com.siriusbasegames.memo

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.async.KtxAsync

class MemoMain : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        addScreen(CameraScreen())
        setScreen<CameraScreen>()
    }
}

class CameraScreen() : KtxScreen {
    //private val image = Texture("logo.png".toInternalFile(), true).apply { setFilter(Linear, Linear) }
    private val batch = SpriteBatch()

    private val topdownCamera: OrthographicCamera = OrthographicCamera()

    //private Stage stage;
    private val floor: Floor = Floor(batch)

    // setup input listeners
    private val gameInputProcessor = GameInputProcessor(floor, topdownCamera)

    init {
        topdownCamera.setToOrtho(false, 800f, 600f)
        input.inputProcessor = gameInputProcessor
    }

    override fun render(delta: Float) {
        //clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)

        Gdx.gl.glClearColor(1f, 1f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        /*batch.use {
            it.projectionMatrix = topdownCamera.combined
s
            floor.draw()
            //it.draw(image, 100f, 160f)
        }*/

        batch.projectionMatrix = topdownCamera.combined
        batch.begin()
        floor.draw()
        batch.end()
    }

    override fun dispose() {
        //image.disposeSafely()
        batch.disposeSafely()
        floor.dispose()
    }
}
