package com.siriusbasegames.memo

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GameInputProcessorTest {
    private lateinit var gridController: GridController
    private lateinit var camera: Camera
    private lateinit var gameStateHandler: GameStateHandler

    private lateinit var gameInputProcessor: InputProcessor

    @BeforeEach
    fun setUp() {
        gridController = mock<GridController>()
        camera = mock<Camera>()
        gameStateHandler = mock<GameStateHandler>()

        gameInputProcessor = GameInputProcessor(gridController, camera, gameStateHandler)
    }

    @Test
    fun testChangeGameCompletionState_levelComplete() {
        whenever(gameStateHandler.getState()).thenReturn(GameState.LEVEL_COMPLETE)

        gameInputProcessor.keyDown(Input.Keys.ENTER)

        verify(gameStateHandler).getState()
        verify(gameStateHandler).setState(GameState.NEXT_LEVEL)
    }

    @Test
    fun testChangeGameCompletionState_lose() {
        whenever(gameStateHandler.getState()).thenReturn(GameState.LOSE, GameState.WIN)

        gameInputProcessor.keyDown(Input.Keys.ENTER)

        verify(gameStateHandler).setState(GameState.RESET)
    }
}