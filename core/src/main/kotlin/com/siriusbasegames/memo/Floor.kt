package com.siriusbasegames.memo

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import kotlin.random.Random

import kotlin.random.nextInt

class Floor(private val batch: Batch) {
    private val rows = 4
    private val columns = 6
    private val cells = rows * columns

    private val initX = 60
    private val initY = 100 /*400*/
    private val w = 80
    private val h = 80
    private val OFFSET = 20

    private val CARDS_DIR = "cards/"

    private var nrRevealedCards = 0

    // textures
    private val reverseTexture: Texture = Texture("field.png")
    private var cardsTextures: MutableList<Texture> = ArrayList()

    // grid and cells
    private var memoGrid: MutableList<Tile> = ArrayList()
    private var cellsRevealed: MutableList<Boolean>

    // cards
    private var cardTypes: MutableList<Map<String, String>>
    private var cardsRemoved: MutableList<Int> = ArrayList()

    // mappings
    private var cellCardMapping: MutableList<Int>

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
                x += w + OFFSET
            }
            x = initX
            y += h + OFFSET
        }
    }

    private fun loadCards() {
        val files = Gdx.files.local("assets/cards/").list()

        for (file in files) {
            //Texture("logo.png".toInternalFile(), true)

            cardsTextures.add(Texture(CARDS_DIR + file.name()))

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

    fun draw() {
        for ((i, cell) in memoGrid.withIndex()) {
            batch.draw(if (cellsRevealed[i]) cardsTextures[cellCardMapping[i]] else reverseTexture, cell.x.toFloat(),
                cell.y.toFloat(), cell.w.toFloat(), cell.h.toFloat())

        }
    }

    fun revealCard(x: Int, y: Int) {
        val it = memoGrid.listIterator()
        var i = 0
        while (it.hasNext()) {
            val tile = it.next()
            if (x >= tile.x && x < tile.x + tile.w && y > tile.y && y < tile.y + tile.h) {
                cellsRevealed[i] = true
                logTileClicked(tile)

                ++nrRevealedCards

                if (isMatchedCards()) {
                    Gdx.app.log(
                        "Matched", "Matched revealed cards"
                    )

                    // todo: destroy matched cards
                    nrRevealedCards = 0
                }

                break
            }

            ++i
        }
    }

    private fun isMatchedCards() : Boolean {
        val indices = ArrayList<Int>()

        val revealed = cellsRevealed.filterIndexed { index, b -> addRevealedIndex(indices, b, index) }

        //cardsTextures[gridCardMapping[revealed]]

        if (revealed.size == 2) {
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

    fun destroyMatchedCards(firstX: Int, firstY: Int, secondX: Int, secondY: Int) {
        destroyCard(firstX, firstY)
        destroyCard(secondX, secondY)
    }

    fun destroyCard(x: Int, y: Int) {
        // todo: fix
        val it = cards.iterator()
        while (it.hasNext()) {
            val tile = it.next()
            if (x >= tile.x && x < tile.x + tile.w && y > tile.y && y < tile.y + tile.h) {
                //logTileClicked(tile)
                it.remove()

                break
            }
        }
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
        val x: Int,
        val y: Int,
        val w: Int,
        val h: Int
    )
}