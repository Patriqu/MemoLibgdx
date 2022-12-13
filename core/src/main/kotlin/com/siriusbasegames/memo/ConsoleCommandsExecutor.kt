package com.siriusbasegames.memo;

import com.strongjoshua.console.CommandExecutor

class ConsoleCommandsExecutor(private val debugMode: DebugMode) : CommandExecutor() {
    fun winLevel() = debugMode.winLevel()

    fun nextLevel() = debugMode.nextLevel()

    fun loseLevel() = debugMode.loseLevel()

    fun resetLevel() = debugMode.resetLevel()

    fun loadLevel(level: Int) = debugMode.loadLevel(level)
}
