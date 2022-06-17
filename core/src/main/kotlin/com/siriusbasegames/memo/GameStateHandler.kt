package com.siriusbasegames.memo

class GameStateHandler {
    private var actualState = GameState.STARTED

    private var currentLevel = 1
    private var maxLevels = 3

    fun currentLevel(): Int {
        return currentLevel
    }

    fun nextLevel() {
        if (currentLevel <= 0) {
            currentLevel = 1
        } else if (currentLevel < maxLevels) {
            ++currentLevel
        } else {
            currentLevel = 1
        }
    }

    fun setLevel(level: Int) {
        currentLevel = if (level > maxLevels) {
            maxLevels
        } else {
            level
        }
    }

    fun runGame() {
        actualState = GameState.RUNNING
    }

    fun setWinState() {
        actualState = if (currentLevel == maxLevels) {
            GameState.WIN
        } else {
            GameState.LEVEL_COMPLETE
        }
    }

    fun resetGame() {
        currentLevel = 1
        actualState = GameState.RUNNING
    }

    fun getState(): GameState {
        return actualState
    }

    fun setState(state: GameState) {
        actualState = state
    }
}

enum class GameState {
    STARTED,
    RUNNING,
    LOSE,
    LEVEL_COMPLETE,
    NEXT_LEVEL,
    WIN,
    RESET
}