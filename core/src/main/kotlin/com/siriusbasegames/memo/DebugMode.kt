package com.siriusbasegames.memo

class DebugMode(private val gameStateHandler: GameStateHandler) {

    fun winLevel() = gameStateHandler.setState(GameState.LEVEL_COMPLETE)

    fun nextLevel() = gameStateHandler.setState(GameState.NEXT_LEVEL)

    fun loseLevel() = gameStateHandler.setState(GameState.LOSE)

    fun resetLevel() {
        gameStateHandler.setLevel(gameStateHandler.currentLevel() - 1)
        gameStateHandler.setState(GameState.NEXT_LEVEL)
    }

    fun loadLevel(level: Int) {
        gameStateHandler.setLevel(level - 1)
        gameStateHandler.setState(GameState.NEXT_LEVEL)
    }

    fun rotateLeftCard() {

    }
    fun rotateRightCard() {

    }

    fun showBackCard() {

    }
    fun showFrontCard() {

    }
}