package com.scs.multiplayerplatformer.game;

import ssmith.android.compatibility.Canvas;
import ssmith.android.lib2d.Camera;

public interface IDrawable {

	void doDraw(Canvas g, Camera cam, long interpol, float scale);

}
