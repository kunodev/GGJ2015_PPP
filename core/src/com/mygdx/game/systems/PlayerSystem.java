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
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.World;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TransformComponent;

public class PlayerSystem extends IteratingSystem {
	private static final Family family = Family.all(PlayerComponent.class, StateComponent.class, TransformComponent.class, MovementComponent.class)
			.get();

	private float accelX = 0.0f;
	private float accelY = 0.0f;
	private World world;

	private ComponentMapper<PlayerComponent> bm;
	private ComponentMapper<StateComponent> sm;
	private ComponentMapper<TransformComponent> tm;
	private ComponentMapper<MovementComponent> mm;

	private float headButtCooldown = 0;

	public PlayerSystem(World world) {
		super(family);

		this.world = world;

		bm = ComponentMapper.getFor(PlayerComponent.class);
		sm = ComponentMapper.getFor(StateComponent.class);
		tm = ComponentMapper.getFor(TransformComponent.class);
		mm = ComponentMapper.getFor(MovementComponent.class);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		TransformComponent t = tm.get(entity);
		StateComponent state = sm.get(entity);
		MovementComponent mov = mm.get(entity);
		PlayerComponent bob = bm.get(entity);
		if (state.get() != PlayerComponent.STATE_HEADBUTT) {
			if (Gdx.input.isKeyPressed(Keys.A)) {
				accelX = -200f;
			} else if (Gdx.input.isKeyPressed(Keys.D)) {
				accelX = 200f;
			} else {
				accelX = 0;
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				accelY = -200f;
			} else if (Gdx.input.isKeyPressed(Keys.W)) {
				accelY = 200f;
			} else {
				accelY = 0;
			}
			if (accelY == 0 && accelX == 0) {
				state.set(PlayerComponent.STATE_IDLE);
			} else {
				state.set(PlayerComponent.STATE_WALKING);
			}
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				state.set(PlayerComponent.STATE_HEADBUTT);
				Sound headbut = world.game.assetManager.get("Sound/headbut.mp3.mp3");
				headbut.play();
			}

			Vector2 playerPos = BossSystem.getDeepCopyCentralPos(entity);
			Vector3 mousePos = world.game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			Vector2 mousePos2 = new Vector2(mousePos.x, mousePos.y);
			t.rotation = mousePos2.sub(playerPos).angle();
		} else {
			// t.rotation needs to be locked
			Vector2 newVelocity = new Vector2(300, 0).rotate(t.rotation);
			this.accelX = newVelocity.x;
			this.accelY = newVelocity.y;
			headButtCooldown += deltaTime;
			if (headButtCooldown >= PlayerComponent.ATTACK_DURATION) {
				headButtCooldown = 0;
				state.set(PlayerComponent.STATE_IDLE);
			}

		}

		mov.velocity.set(accelX, accelY);
	}
}
