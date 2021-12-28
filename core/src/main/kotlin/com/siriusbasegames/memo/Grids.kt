package com.siriusbasegames.memo

import kotlin.random.Random
import kotlin.random.nextInt

class Grids {
    private var gridLayout: MutableList<Map<String, Int>> = mutableListOf()
    private var gridCoordinates: MutableList<List<Tile>> = mutableListOf()

    private val initX = 0.1F
    private val initY = 2.9F

    private val w = 0.7F    // in meters
    private val h = 0.7F
    private val offset = 0.2F

    private val sumWidthOffset = w + offset
    private val sumHeightOffset = h + offset

    init {
        gridLayout.add(0, mapOf("rows" to 4, "columns" to 3, "cells" to 4*3))
        gridLayout.add(1, mapOf("rows" to 4, "columns" to 4, "cells" to 4*4))
        gridLayout.add(2, mapOf("rows" to 4, "columns" to 6, "cells" to 4*6))

        initGridsTableLayout()
    }

    private fun initGridsTableLayout() {
        for (l in 0 until gridLayout.size) {
            val tiles: MutableList<Tile> = mutableListOf()

            var x = initX
            var y = initY

            for (i in 0 until gridLayout[l]["rows"]!!) {
                for (j in 0 until gridLayout[l]["columns"]!!) {
                    tiles.add(Tile(x, y, w, h))
                    x += sumWidthOffset
                }

                x = initX
                y -= sumHeightOffset
            }

            gridCoordinates.add(l, tiles)
        }
    }

    fun gridCoordinates(level: Int): List<Tile> {
        return gridCoordinates[level-1]
    }

    fun gridLayout(level: Int): Map<String, Int> {
        return gridLayout[level-1]
    }

    fun cellsAmount(level: Int): Int? {
        return gridLayout[level-1]["cells"]
    }

    fun widthWithOffset(): Float {
        return sumWidthOffset
    }
    fun heightWithOffset(): Float {
        return sumHeightOffset
    }

    fun randomizeCardsOnGrid(level: Int, cardTypes: MutableList<Map<String, String>>): MutableList<Int> {
        val cellCardMapping: MutableList<Int> = MutableList(cellsAmount(level)!!) { -1 }

        val cellsRange = 0 until cellsAmount(level)!!
        val availableCells = cellsRange.toMutableList()

        for ((i, _) in cardTypes.withIndex()) {
            if (availableCells.isNotEmpty()) {
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

        return cellCardMapping
    }

    class Tile(
        val x: Float,
        val y: Float,
        val w: Float,
        val h: Float
    )
}