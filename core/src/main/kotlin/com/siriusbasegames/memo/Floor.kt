package com.siriusbasegames.memo

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.files
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Timer
import kotlin.random.Random
import kotlin.random.nextInt


class Floor(private val batch: Batch, private val winLoseHandle: WinLoseHandle /*private val camera: Camera*/) {
    //private val unit = 20F

    private val rows = 4
    private val columns = 6
    private val halfColumns = columns / 2
    private val cells = rows * columns

    private val initX = 0.1F  /*50*/ /*graphics.width/10*/ /*graphics.width/2*/ /*- 300*/ /*60*/
    private val initY = 3F /*400*/ /*graphics.height/10*/ /*100*/ /*graphics.height/2*/ /*+ 100*/ /*400*/
    private val w = 0.6F /*80*/  // meters
    private val h = 0.6F /*80*/
    private val offset = /*20*/ 0.2F   // in meters
    private val wPlusOffset = w + offset

    //private val halfViewportWidth = camera.viewportWidth / 2

    private val assetsDir = "assets/"
    private val cardsDir = "cards/"

    private var nrRevealedCards = 0
    private var remainingCards = cells
    private val maxAllowedRevealedCards = 2

    // textures
    private val reverseTexture: Texture = Texture("Field.png")
    private var cardsTextures: MutableList<Texture> = ArrayList()

    // grid and cells
    private var memoGrid: MutableList<Tile> = ArrayList()
    private var cellsRevealed: MutableList<Boolean>

    // cards
    private var cardTypes: MutableList<Map<String, String>>

    // mappings
    // map each cell in the grid to one card from cardTypes set, can exist two the same card types in the grid
    private var cellCardMapping: MutableList<Int>

    private val reverseDelay = 0.3F
    private val deleteCardsDelay = 0.3F

    init {
        initGrid()

        cardTypes = ArrayList()
        loadCards()

        cellCardMapping = MutableList(24) { index -> index }
        cellCardMapping.fill(-1)

        cellsRevealed = MutableList(24) { false }

        randomizeCardsPlacement()
    }

    private fun initGrid() {
        var x = initX
        var y = initY
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                memoGrid.add(Tile(x, y, w, h))
                x += w + offset
            }
            x = initX
            y -= /*+=*/ h + offset
        }
    }

    private fun loadCards() {
        var cardFiles = files.internal(cardsDir).list()     // Android
        if (cardFiles.isEmpty()) {
            cardFiles = files.internal(assetsDir + cardsDir).list()     // Desktop
        }

        for (file in cardFiles) {
            cardsTextures.add(Texture(cardsDir + file.name()))

            cardTypes.add(mapOf("name" to file.nameWithoutExtension(), "revealed" to "false"))
        }
    }

    private fun randomizeCardsPlacement() {
        val cellsRange = 0 until cells
        val availableCells = cellsRange.toMutableList()

        for ((i, card) in cardTypes.withIndex()) {
            var j = 0
            while (j < 2) {
                var cellNr = -1
                while (cellNr !in availableCells) {
                    cellNr = Random.nextInt(cellsRange)
                }

                cellCardMapping[cellNr] = i

                availableCells.remove(cellNr)
                ++j
            }

        }
    }

    fun draw(camera: Camera) {
        for ((i, cell) in memoGrid.withIndex()) {
            if (cellCardMapping[i] != -1) {
                //var x = cell.x.toFloat() / unit
                //var y = cell.y.toFloat() / unit
                //var w = cell.w.toFloat() / camera.viewportWidth /*cell.w.toFloat() / unit*/
                //var h = cell.h.toFloat() / camera.viewportHeight /*cell.h.toFloat() / unit*/

                batch.draw(if (cellsRevealed[i]) cardsTextures[cellCardMapping[i]] else reverseTexture,
                    calculateDrawnCellX(cell, camera), cell.y, w, h)
            }
        }
    }

    fun enterTheBreakpoint() {
        return
    }

    fun revealCard(x: Float, y: Float, camera: Camera) {
        if (nrRevealedCards < maxAllowedRevealedCards) {
            val it = memoGrid.listIterator()
            var i = 0
            while (it.hasNext()) {
                val cell = it.next()

                val actualTileX = calculateDrawnCellX(cell, camera)

                if (x >= actualTileX && x < actualTileX + cell.w && y > cell.y && y < cell.y + cell.h) {
                    if (!cellsRevealed[i]) {
                        cellsRevealed[i] = true
                        logTileClicked(cell)

                        ++nrRevealedCards

                        val indices = ArrayList<Int>()
                        if (isMatchedCards(indices)) {
                            matchedCardsHandle(indices)
                        }

                        reverseCardsStep(indices)

                        //winLoseHandle.checkWinConditions(cellCardMapping)
                    }

                    break
                }

                ++i
            }
        }
    }

    private fun calculateDrawnCellX(cell: Tile, camera: Camera): Float {
        return cell.x + camera.viewportWidth / 2 - (wPlusOffset * halfColumns)
    }

    private fun matchedCardsHandle(indices: ArrayList<Int>) {
        Gdx.app.log("Matched", "Matched revealed cards")

        // destroy matched cards
        Timer.schedule(object: Timer.Task() {
            override fun run() {
                destroyMatchedCards(indices)

                if (remainingCards == 0) {
                    winLoseHandle.win()
                }
            }
        }, deleteCardsDelay)
    }

    private fun reverseCardsStep(indices: ArrayList<Int>) {
        if (nrRevealedCards == maxAllowedRevealedCards) {
            Timer.schedule(object : Timer.Task() {
                override fun run() {
                    reverseAllCards(indices)
                    nrRevealedCards = 0
                }
            }, reverseDelay)
        }
    }

    private fun reverseAllCards(indices: ArrayList<Int>) {
        for (index in indices) {
            cellsRevealed[index] = false
        }
    }

    private fun isMatchedCards(indices: ArrayList<Int>) : Boolean {
        val revealed = cellsRevealed.filterIndexed { index, b -> addRevealedIndex(indices, b, index) }

        if (revealed.size == maxAllowedRevealedCards) {
            return cellCardMapping[indices[0]] == cellCardMapping[indices[1]]
        }

        return false
    }

    private fun addRevealedIndex(indices: ArrayList<Int>, revealed: Boolean, index: Int): Boolean {
        if (revealed) {
            return indices.add(index)
        }
        return false
    }

    private fun destroyMatchedCards(cardIndices: ArrayList<Int>) {
        for (index in cardIndices) {
            destroyCard(index)
        }

        remainingCards -= 2
    }

    private fun destroyCard(cardNr: Int) {
       cellCardMapping[cardNr] = -1
    }

    private fun logTileClicked(tile: Tile) {
        Gdx.app.log(
            "Tile", "x=" + tile.x + ", y=" + tile.y + ", x+w=" + (tile.x + tile.w) + ", " +
                    "y+h=" + (tile.y + tile.h)
        )
    }

    fun dispose() {
        reverseTexture.dispose()
        cardsTextures.forEach { it.dispose() }
    }

    private class Tile(
        val x: Float,
        val y: Float,
        val w: Float,
        val h: Float
    )
}