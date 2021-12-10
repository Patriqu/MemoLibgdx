package com.siriusbasegames.memo.android

import android.os.Bundle

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.siriusbasegames.memo.MemoMain

/** Launches the Android application. */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(MemoMain(), AndroidApplicationConfiguration().apply {

            useGL30 = true
            useAccelerometer = false
            useCompass = false
        })
    }
}
