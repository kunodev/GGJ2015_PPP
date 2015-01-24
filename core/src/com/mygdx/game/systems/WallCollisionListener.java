package com.mygdx.game.systems;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.systems.CollisionSystem.CollisionListener;

public class WallCollisionListener implements CollisionListener {

	@Override
	public void hit(Entity thisEntity, Entity otherEntity) {
		System.out.println("Collision Detected");
	}

}
