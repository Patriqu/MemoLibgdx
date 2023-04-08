package com.siriusbasegames.memo

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.files
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Timer
import ktx.assets.toInternalFile

class GridController(private val batch: Batch, private val gameStateHandler: GameStateHandler) {
    private val assetsDir = "assets/"
    private val cardsDir = "cards/"

    private var nrRevealedCards = 0
    private var remainingCards = 0
    private val maxAllowedRevealedCards = 2

    // textures
    private val reverseTexture: Texture = Texture("Reverse.png".toInternalFile())
        .apply { setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) }
    private var cardsTextures: MutableList<Texture> = ArrayList()

    // grid and cells
    private val grids: Grids = Grids()
    private var memoGrid: List<Grids.Tile> = mutableListOf()
    private var cellsRevealed: MutableList<Boolean>

    // cards
    private var cardTypes: MutableList<Map<String, String>> = mutableListOf()

    // mappings
    // map each cell in the grid to one card from cardTypes set, can exist two the same card types in the grid
    private var cellCardMapping: MutableList<Int>

    // delays
    private val reverseDelay = 0.3F
    private val deleteCardsDelay = 0.3F

    init {
        loadCards()

        memoGrid = grids.gridCoordinates(1)

        cellCardMapping = grids.randomizeCardsOnGrid(1, cardTypes)
        cellsRevealed = MutableList(grids.cellsAmount(1)!!) { false }
        remainingCards = grids.cellsAmount(1)!!
    }

    private fun loadCards() {
        var cardFiles = files.internal(cardsDir).list()     // Android
        if (cardFiles.isEmpty()) {
            cardFiles = files.internal(assetsDir + cardsDir).list()     // Desktop
        }

        cardFiles = cardFiles.filter { !it.isDirectory }.toTypedArray()

        for (file in cardFiles) {
            cardsTextures.add(Texture(cardsDir + file.name()))

            cardTypes.add(mapOf("name" to file.nameWithoutExtension(), "revealed" to "false"))
        }
    }

    fun draw(bounds: List<Float>) {
        if (cellCardMapping.isNotEmpty()) {
            for ((i, cell) in memoGrid.withIndex()) {
                if (cellCardMapping[i] != -1) {
                    batch.draw(
                        if (cellsRevealed[i]) cardsTextures[cellCardMapping[i]] else reverseTexture,
                        calculateDrawnCellX(cell, bounds[0]), calculateDrawnCellY(cell, bounds[1]), cell.w,
                        cell.h
                    )
                }
            }
        }
    }

    fun revealCard(x: Float, y: Float, viewportWidth: Float) {
        if (nrRevealedCards < maxAllowedRevealedCards) {
            val it = memoGrid.listIterator()
            var i = 0
            while (it.hasNext()) {
                val cell = it.next()

                val actualTileX = calculateDrawnCellX(cell, viewportWidth)

                if (x >= actualTileX && x < actualTileX + cell.w && y > cell.y && y < cell.y + cell.h) {
                    handleClickedCard(i, cell)

                    break
                }

                ++i
            }
        }
    }

    private fun handleClickedCard(i: Int, cell: Grids.Tile) {
        if (!cellsRevealed[i]) {
            cellsRevealed[i] = true
            logTileClicked(cell)

            ++nrRevealedCards

            val indices = ArrayList<Int>()
            if (isMatchedCards(indices)) {
                matchedCardsHandle(indices)
            }

            reverseCardsStep(indices)
        }
    }

    private fun calculateDrawnCellX(cell: Grids.Tile, viewPortWidth: Float): Float {
        return cell.x + viewPortWidth / 2 -
                (grids.widthWithOffset() * grids.gridLayout(gameStateHandler.currentLevel())["columns"]!! / 2)
    }

    private fun calculateDrawnCellY(cell: Grids.Tile, viewportHeight: Float): Float {
        return cell.y - (viewportHeight / 2 - grids.heightWithOffset() * 2 - 0.2F)
    }

    private fun matchedCardsHandle(indices: ArrayList<Int>) {
        Gdx.app.log("Matched", "Matched revealed cards")

        // destroy matched cards
        Timer.schedule(object : Timer.Task() {
            override fun run() {
                destroyMatchedCards(indices)

                if (remainingCards == 0) {
                    gameStateHandler.setWinState()

                }
            }
        }, deleteCardsDelay)
    }

    private fun reverseCardsStep(indices: ArrayList<Int>) {
        if (nrRevealedCards == maxAllowedRevealedCards) {
            Timer.schedule(object : Timer.Task() {
                override fun run() {
                    reverseCards(indices)
                    nrRevealedCards = 0
                }
            }, reverseDelay)
        }
    }

    private fun reverseCards(indices: ArrayList<Int>) {
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

    private fun logTileClicked(tile: Grids.Tile) {
        Gdx.app.log(
            "Tile", "x=" + tile.x + ", y=" + tile.y + ", x+w=" + (tile.x + tile.w) + ", " +
                    "y+h=" + (tile.y + tile.h)
        )
    }

    fun resetBoard() {
        remainingCards = grids.cellsAmount(gameStateHandler.currentLevel())!!

        memoGrid = grids.gridCoordinates(gameStateHandler.currentLevel())
        reverseAllCards()
        cellCardMapping = grids.randomizeCardsOnGrid(gameStateHandler.currentLevel(), cardTypes)
    }

    private fun reverseAllCards() {
        cellsRevealed.let {false}
        cellsRevealed = MutableList(grids.cellsAmount(gameStateHandler.currentLevel())!!) { false }
    }

    fun dispose() {
        reverseTexture.dispose()
        cardsTextures.forEach { it.dispose() }
    }
}