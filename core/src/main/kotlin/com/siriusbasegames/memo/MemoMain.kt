package com.siriusbasegames.memo

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

var memoMain: MemoMain? = null

class MemoMain : KtxGame<KtxScreen>() {
    override fun create() {
        memoMain = this

        KtxAsync.initiate()

        addScreen(MainScreen())
        setScreen<MainScreen>()
    }

    override fun dispose() {
        super.dispose()
        removeScreen<MainScreen>()
    }
}
class MainScreen : KtxScreen {
    /*private val unitHeight = 20F
    private var ppu: Float = Gdx.graphics.height / unitHeight
    private var unitWidth = Gdx.graphics.width / ppu*/

    private val virtualHeight = 4F  // meters

    //private val image = Texture("logo.png".toInternalFile(), true).apply { setFilter(Linear, Linear) }
    private val batch = SpriteBatch()

    private val topdownCamera: OrthographicCamera = OrthographicCamera(/*unitWidth, unitHeight*/)
    //private val viewport: Viewport = ExtendViewport(viewportWidth, viewportHeight)

    private val uiTextCamera = OrthographicCamera(/*graphics.width.toFloat(), graphics.height.toFloat()*/)

    //private Stage stage;
    private val floor: Floor

    //test
    private var winLoseHandle: WinLoseHandle

    private val disposer: Disposer

    init {
        testDebug()

        /*topdownCamera.setToOrtho(false, virtualHeight * graphics.width / graphics.height, virtualHeight)
        batch.projectionMatrix = topdownCamera.combined*/

        uiTextCamera.setToOrtho(false)

        winLoseHandle = WinLoseHandle()

        floor = Floor(batch, winLoseHandle)

        disposer = Disposer(batch, floor, winLoseHandle)

        input.inputProcessor = GameInputProcessor(floor, topdownCamera, winLoseHandle, disposer)
    }

    private fun testDebug() {
        //topdownCamera.setToOrtho(false, viewportWidth, viewportHeight)
        //topdownCamera.setToOrtho(/*false*/ true, unitWidth, unitHeight)
        //topdownCamera.setToOrtho(false)
        //topdownCamera.position.set(unitWidth / 2F, unitHeight / 2F, 0F)
        //topdownCamera.update()

        /*font = buildFont("assets/fonts/arial.ttf", 32, "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890\"!`?'.,;:()" +
                "[]{}<>|/@\\^$-%+=#_&~*");*/
    }

    private fun buildFont(filename: String, size: Int, characters: String): BitmapFont {
        val generator = FreeTypeFontGenerator(Gdx.files.internal(filename))
        val parameter: FreeTypeFontGenerator.FreeTypeFontParameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = size
        //parameter.characters = characters
        parameter.kerning = true
        parameter.color = Color.BLUE
        parameter.magFilter = Texture.TextureFilter.Linear
        parameter.minFilter = Texture.TextureFilter.Linear
        val font: BitmapFont = generator.generateFont(parameter)
        font.color = Color.BLUE
        font.data.markupEnabled = true
        generator.dispose()
        return font
    }

    override fun render(delta: Float) {
        //clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)

        Gdx.gl.glClearColor(1f, 1f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        topdownCamera.update()
        uiTextCamera.update()

        batch.begin()

        batch.projectionMatrix = topdownCamera.combined
        floor.draw(topdownCamera)

        batch.projectionMatrix = uiTextCamera.combined
        winLoseHandle.draw(batch)

        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        topdownCamera.setToOrtho(false, virtualHeight * width / height.toFloat(), virtualHeight)
        //batch.projectionMatrix = topdownCamera.combined
    }

    override fun dispose() {
        disposer.dispose()
    }
}

