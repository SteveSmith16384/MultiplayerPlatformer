package com.scs.multiplayerplatformer.game;

import ssmith.android.lib2d.MyPointF;

public class PhysicsEngine {
	
	private MyPointF dir;
	public MyPointF offset;
	private float speed, grav;

	public PhysicsEngine(MyPointF _dir, float _speed, float _grav) {
		super();
		
		dir = _dir;
		speed = _speed;
		grav = _grav;
		offset = new MyPointF();
	}
	
	
	public void process() {
		offset.x = dir.x * speed;
		offset.y = dir.y * speed;

		dir.y += grav; //Statics.ROCK_GRAVITY;
	}

}
