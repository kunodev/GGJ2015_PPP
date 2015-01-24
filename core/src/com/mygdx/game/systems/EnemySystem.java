package com.mygdx.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.components.EnemyComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.TransformComponent;

public class EnemySystem extends IteratingSystem {

	public EnemySystem() {
		super(Family.all(EnemyComponent.class, TransformComponent.class, MovementComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		switch (entity.getComponent(EnemyComponent.class).whatDo) {
		case EnemyComponent.DO_NOTHING_TACTICS: {
			break; // DO NOTHING LOL!
		}
		}

	}
}
