package com.siriusbasegames.memo

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.utils.Timer

class WinLoseHandle {
    private var textFontGenerator: FreeTypeFontGenerator?
    private var fontGeneratorParameter: FreeTypeFontGenerator.FreeTypeFontParameter = FreeTypeFontGenerator
        .FreeTypeFontParameter()
    private val fontType = "arial.ttf"
    private val assetsDir = "assets/"
    private val fontsDir = "fonts/"

    // win/lose text
    private var winFont: BitmapFont? = null
    private var winTextX = graphics.width/2F /*- 150F*/
    private var winTextY = graphics.height/2F /*300F*/
    private var winText = "YOU WIN!"
    private var loseText = "YOU LOSE!"

    // countdown text
    private var countdownFont: BitmapFont? = null
    private var countdownTextX = 50F /*camera.viewportWidth * 0.05F*/
    private var countdownTextY = graphics.height - 20F /*camera.viewportHeight - (camera.viewportHeight * 0.05F)*//*570F*/

    private var countdown = 80

    private var countdownTask: Timer.Task

    private var gameState: GameState  = GameState.STARTED

    init {
        // Countdown initialization
        var file = Gdx.files.internal(assetsDir + fontsDir + fontType)     // Android
        if (file != null) {
            file = Gdx.files.internal(fontsDir + fontType)     // Desktop
        }
        textFontGenerator = FreeTypeFontGenerator(Gdx.files.internal(file.path()))

        // win/lose text
        fontGeneratorParameter.size = 120
        winTextX -= fontGeneratorParameter.size * 3
        fontGeneratorParameter.color = Color.RED

        winFont = textFontGenerator?.generateFont(fontGeneratorParameter)
        winFont!!.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        // countdown text
        fontGeneratorParameter.size = 46
        fontGeneratorParameter.color = Color.RED

        countdownFont = textFontGenerator?.generateFont(fontGeneratorParameter)
        textFontGenerator?.dispose()

        countdownFont!!.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        countdownTask = Timer.schedule(object: Timer.Task() {
            override fun run() { countdown--; if (countdown == 0) {
                this.cancel()
                gameState = GameState.LOSE
            } }
        }, 1F, 1F)
    }

    fun draw(batch: Batch) {
        winDraw(batch)
        loseDraw(batch)

        countdownFont?.draw(batch, countdown.toString(), countdownTextX, countdownTextY)
    }

    private fun winDraw(batch: Batch) {
        if (gameState == GameState.WIN) {
            winFont?.draw(batch, winText, winTextX, winTextY)
        }
    }
    private fun loseDraw(batch: Batch) {
        if (gameState == GameState.LOSE) {
            winFont?.draw(batch, loseText, winTextX, winTextY)
        }
    }

    fun win() {
        countdownTask.cancel()
        gameState = GameState.WIN
    }

    fun isWinOrLose(): Boolean {
        if (gameState == GameState.LOSE || gameState == GameState.WIN) {
            return true
        }
        return false
    }

    fun dispose() {
        winFont?.dispose()
    }
}