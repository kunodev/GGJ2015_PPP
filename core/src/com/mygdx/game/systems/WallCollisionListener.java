package com.mygdx.game.systems;

import com.mygdx.game.systems.CollisionSystem.CollisionListener;

public class WallCollisionListener implements CollisionListener {

	@Override
	public void hit() {
		System.out.println("Collision Detected");
	}

}
