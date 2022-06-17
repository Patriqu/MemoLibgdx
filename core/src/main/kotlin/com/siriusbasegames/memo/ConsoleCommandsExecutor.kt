package com.siriusbasegames.memo;

import com.strongjoshua.console.CommandExecutor

class ConsoleCommandsExecutor(private val gameStateHandler: GameStateHandler) : CommandExecutor() {
    fun levelComplete() {
        gameStateHandler.setState(GameState.LEVEL_COMPLETE)
    }

    fun nextLevel() {
        gameStateHandler.setState(GameState.NEXT_LEVEL)
    }

    fun loadLevel(level: Int) {
        gameStateHandler.setLevel(level - 1)
        gameStateHandler.setState(GameState.NEXT_LEVEL)
    }
}
