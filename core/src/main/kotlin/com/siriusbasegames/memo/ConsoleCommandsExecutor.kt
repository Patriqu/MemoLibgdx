package com.siriusbasegames.memo;

import com.strongjoshua.console.CommandExecutor

class ConsoleCommandsExecutor(private val gameStateHandler: GameStateHandler) : CommandExecutor() {
    fun winLevel() = gameStateHandler.setState(GameState.LEVEL_COMPLETE)

    fun nextLevel() = gameStateHandler.setState(GameState.NEXT_LEVEL)

    fun loseLevel() = gameStateHandler.setState(GameState.LOSE)

    fun resetLevel() {
        gameStateHandler.setLevel(gameStateHandler.currentLevel() - 1)
        gameStateHandler.setState(GameState.NEXT_LEVEL)

    }

    fun loadlevel(level: Int) {
        gameStateHandler.setLevel(level - 1)
        gameStateHandler.setState(GameState.NEXT_LEVEL)
    }
}
