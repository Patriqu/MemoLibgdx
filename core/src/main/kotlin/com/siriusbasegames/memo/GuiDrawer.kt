package com.siriusbasegames.memo

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.utils.Timer
import ktx.graphics.center

class GuiDrawer(private var gameStateHandler: GameStateHandler) {
    private var textFontGenerator: FreeTypeFontGenerator?
    private var fontGeneratorParameter: FreeTypeFontGenerator.FreeTypeFontParameter = FreeTypeFontGenerator
        .FreeTypeFontParameter()
    private val fontType = "arial.ttf"
    private val assetsDir = "assets/"
    private val fontsDir = "fonts/"

    // game complete texts
    private var completeTexts: Map<String, Map<String, String>>
            = mapOf(
        "levelComplete" to mapOf("primary" to "LEVEL COMPLETE!", "secondary" to "Click to go to the next level"),
        "win" to mapOf("primary" to "YOU WIN!", "secondary" to "Click to restart the game"),
        "lose" to mapOf("primary" to "YOU LOSE!", "secondary" to "Click to restart the game")
    )

    private var completeCoordinates: Map<String, Map<String, MutableMap<String, Float>>>
            = mapOf(
        "levelComplete" to mapOf("X" to mutableMapOf("primary" to 0F, "secondary" to 0F),
            "Y" to mutableMapOf("primary" to 0F, "secondary" to 0F)),
        "win" to mapOf("X" to mutableMapOf("primary" to 0F, "secondary" to 0F),
            "Y" to mutableMapOf("primary" to 0F, "secondary" to 0F)),
        "lose" to mapOf("X" to mutableMapOf("primary" to 0F, "secondary" to 0F),
            "Y" to mutableMapOf("primary" to 0F, "secondary" to 0F)))

    private val textTypes: List<String> = listOf("primary", "secondary")

    private var completeFonts: MutableMap<String, BitmapFont?> = mutableMapOf()

    // level text
    private var levelFont: BitmapFont? = null
    private var levelTextX = 300F
    private var levelTextY = graphics.height - 20F
    private var levelText = "Level "

    // countdown text
    private var countdownFont: BitmapFont? = null
    private var countdownTextX = 50F
    private var countdownTextY = graphics.height - 20F

    private val countdownMax = 80
    private var countdown = countdownMax
    private var countdownTask: Timer.Task

    init {
        // font type generator initialization
        var file = Gdx.files.internal(assetsDir + fontsDir + fontType)     // Android
        if (file != null) {
            file = Gdx.files.internal(fontsDir + fontType)     // Desktop
        }
        textFontGenerator = FreeTypeFontGenerator(Gdx.files.internal(file.path()))

        fontGeneratorParameter.color = Color.RED
        fontGeneratorParameter.magFilter = Texture.TextureFilter.Linear
        fontGeneratorParameter.minFilter = Texture.TextureFilter.Linear

        //// win/lose text

        // primary
        fontGeneratorParameter.size = 120
        completeFonts["primary"] = textFontGenerator?.generateFont(fontGeneratorParameter)

        // secondary
        fontGeneratorParameter.size = 40
        completeFonts["secondary"] = textFontGenerator?.generateFont(fontGeneratorParameter)

        calculateCenterTexts()

        // level text
        fontGeneratorParameter.size = 46

        levelTextX -= fontGeneratorParameter.size * 3
        levelFont = textFontGenerator?.generateFont(fontGeneratorParameter)

        // countdown text
        countdownFont = textFontGenerator?.generateFont(fontGeneratorParameter)

        textFontGenerator?.dispose()

        countdownTask = scheduleCountdown()
    }

    private fun calculateCenterTexts() {
        for (completeText in completeTexts) {
            val a = completeText.value

            for (type in textTypes) {
                val coordsPrimary = a[type]?.let {
                    completeFonts[type]?.center(
                        it, graphics.width.toFloat(), graphics
                            .height.toFloat(), 0f, 0f)
                }

                if (coordsPrimary != null) {
                    completeCoordinates[completeText.key]?.get("X")?.set(type, coordsPrimary.x)
                    completeCoordinates[completeText.key]?.get("Y")?.set(type, coordsPrimary.y
                            - if (type == "secondary") 150F else 0F )
                }
            }
        }
    }

    private fun scheduleCountdown(): Timer.Task {
        return Timer.schedule(object : Timer.Task() {
            override fun run() {
                countdown--
                if (countdown == 0) {
                    this.cancel()
                    gameStateHandler.setState(GameState.LOSE)
                }
            }
        }, 1F, 1F)
    }

    fun win() {
        if (countdownTask.isScheduled) {
            countdownTask.cancel()
        }
    }

    fun reset() {
        resetCountdown()
    }

    private fun resetCountdown() {
        if (countdownTask.isScheduled) {
            countdownTask.cancel()
        }
        countdown = countdownMax
        countdownTask = scheduleCountdown()
    }

    fun draw(batch: Batch) {
        drawCompleteText(batch)

        countdownFont?.draw(batch, countdown.toString(), countdownTextX, countdownTextY)
        levelFont?.draw(batch, levelText + gameStateHandler.currentLevel(), levelTextX, levelTextY)
    }

    private fun drawCompleteText(batch: Batch) {
        when (gameStateHandler.getState()) {
            GameState.LEVEL_COMPLETE -> drawCompleteText(batch, "levelComplete")
            GameState.WIN -> drawCompleteText(batch, "win")
            GameState.LOSE -> drawCompleteText(batch, "lose")
            else -> Unit
        }
    }

    private fun drawCompleteText(batch: Batch, key: String) {
        completeCoordinates[key]?.get("X")?.get("primary")?.let {
            completeCoordinates[key]?.get("Y")?.get("primary")?.let { it1 ->
                completeFonts["primary"]?.draw(batch, completeTexts[key]?.get("primary") ?: "",
                    it, it1
                )
            }
        }

        completeCoordinates[key]?.get("X")?.get("secondary")?.let {
            completeCoordinates[key]?.get("Y")?.get("secondary")?.let { it1 ->
                completeFonts["secondary"]?.draw(batch, completeTexts[key]?.get("secondary") ?: "",
                    it, it1
                )
            }
        }
    }

    fun dispose() {
        completeFonts.forEach { (_, v) -> v?.dispose() }
    }
}