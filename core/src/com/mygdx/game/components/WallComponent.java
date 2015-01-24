package com.mygdx.game.components;

import com.mygdx.game.systems.WallCollisionListener;

public class WallComponent extends CollisionComponent {

	public WallComponent() {
		this.listener = new WallCollisionListener();
	}

}
