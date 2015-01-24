/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.World;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TransformComponent;

public class PlayerSystem extends IteratingSystem {
	private static final Family family = Family
			.getFor(PlayerComponent.class, StateComponent.class, TransformComponent.class, MovementComponent.class);

	private float accelX = 0.0f;
	private float accelY = 0.0f;
	private World world;

	private ComponentMapper<PlayerComponent> bm;
	private ComponentMapper<StateComponent> sm;
	private ComponentMapper<TransformComponent> tm;
	private ComponentMapper<MovementComponent> mm;

	public PlayerSystem(World world) {
		super(family);

		this.world = world;

		bm = ComponentMapper.getFor(PlayerComponent.class);
		sm = ComponentMapper.getFor(StateComponent.class);
		tm = ComponentMapper.getFor(TransformComponent.class);
		mm = ComponentMapper.getFor(MovementComponent.class);
	}

	public void setAccelX(float accelX) {
		this.accelX = accelX;
	}

	public void setAccelY(float accelY) {
		this.accelY = accelY;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		accelY = 0.0f;
		accelX = 0.0f;
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		TransformComponent t = tm.get(entity);
		StateComponent state = sm.get(entity);
		MovementComponent mov = mm.get(entity);
		PlayerComponent bob = bm.get(entity);
		if (Gdx.input.isKeyPressed(Keys.A))
			accelX = 5f;
		if (Gdx.input.isKeyPressed(Keys.D))
			accelX = -5f;
		if (Gdx.input.isKeyPressed(Keys.S))
			accelY = 5f;
		if (Gdx.input.isKeyPressed(Keys.W))
			accelY = -5f;

		Vector3 v3 = new Vector3(world.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).x,
				world.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).y, 0f).sub(world.bob
				.getComponent(TransformComponent.class).pos);

		world.bob.getComponent(TransformComponent.class).rotation = new Vector2(v3.x, v3.y).angle();
		world.game.engine.getSystem(PlayerSystem.class).setAccelX(accelX);
		world.game.engine.getSystem(PlayerSystem.class).setAccelY(accelY);

	}
}
