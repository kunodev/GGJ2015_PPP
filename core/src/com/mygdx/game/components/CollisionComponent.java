package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.mygdx.game.systems.CollisionSystem.CollisionListener;

public class CollisionComponent extends Component {

	public CollisionListener listener;
	public float width;
	public float height;

}
