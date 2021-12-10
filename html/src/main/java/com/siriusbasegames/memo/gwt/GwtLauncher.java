package com.siriusbasegames.memo.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.freetype.gwt.FreetypeInjector;
import com.badlogic.gdx.utils.GdxRuntimeException;
// import com.siriusbasegames.memo.MemoMain;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(640, 480);
    }

    @Override
    public ApplicationListener createApplicationListener () {
        throw new GdxRuntimeException("Kotlin is currently not supported by GWT.");
        // return new MemoMain();
    }

    @Override
    public void onModuleLoad () {
        FreetypeInjector.inject(GwtLauncher.super::onModuleLoad);
    }
}
