package com.siriusbasegames.memo

class Updater(private val gameStateHandler: GameStateHandler, private val guiDrawer: GuiDrawer, private val gridController: GridController) {
    fun update() {
        handleGameState()
    }

    private fun handleGameState() {
        when(gameStateHandler.getState()) {
            GameState.STARTED -> {
                gameStateHandler.runGame()
            }

            GameState.RUNNING -> {}

            GameState.LOSE -> {}

            GameState.LEVEL_COMPLETE -> {
                guiDrawer.win()
            }

            GameState.NEXT_LEVEL -> {
                guiDrawer.reset()
                gameStateHandler.nextLevel()
                gridController.resetBoard()
                gameStateHandler.setState(GameState.RUNNING)
            }

            GameState.WIN -> {
                guiDrawer.win()
            }

            GameState.RESET -> {
                guiDrawer.reset()
                gameStateHandler.resetGame()
                gridController.resetBoard()
            }
        }
    }
}