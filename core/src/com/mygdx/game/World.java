/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.*;
import com.mygdx.game.systems.RenderingSystem;

import java.util.Random;

public class World {
	public static final float WORLD_WIDTH = 10;
	public static final float WORLD_HEIGHT = 15 * 20;
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	public static final Vector2 gravity = new Vector2(0, -12);

	public final Random rand;
	public int state;
	
	private Engine engine;

	public World (Engine engine) {
		this.engine = engine;
		this.rand = new Random();
	}
	
	public void create() {
		Entity bob = createBob();
		createCamera(bob);
		createBackground();

		this.state = WORLD_STATE_RUNNING;
	}
	
	private Entity createBob() {
		Entity entity = new Entity();
		
		AnimationComponent animation = new AnimationComponent();
		PlayerComponent bob = new PlayerComponent();
		BoundsComponent bounds = new BoundsComponent();
		MovementComponent movement = new MovementComponent();
		TransformComponent position = new TransformComponent();
		StateComponent state = new StateComponent();
		TextureComponent texture = new TextureComponent();
		
		//animation.animations.put(PlayerComponent.STATE_FALL, Assets.bobFall);
		//animation.animations.put(PlayerComponent.STATE_HIT, Assets.bobHit);
		//animation.animations.put(PlayerComponent.STATE_JUMP, Assets.bobJump);
		
		bounds.bounds.width = PlayerComponent.WIDTH;
		bounds.bounds.height = PlayerComponent.HEIGHT;
		
		position.pos.set(5.0f, 1.0f, 0.0f);
		
		state.set(PlayerComponent.STATE_JUMP);
		
		entity.add(animation);
		entity.add(bob);
		entity.add(bounds);
		entity.add(movement);
		entity.add(position);
		entity.add(state);
		entity.add(texture);
		
		engine.addEntity(entity);
		
		return entity;
	}
	
	private void createPlatform(int type, float x, float y) {
		Entity entity = new Entity();
		
		AnimationComponent animation = new AnimationComponent();
		BoundsComponent bounds = new BoundsComponent();
		MovementComponent movement = new MovementComponent();
		TransformComponent position = new TransformComponent();
		StateComponent state = new StateComponent();
		TextureComponent texture = new TextureComponent();

		
		position.pos.set(x, y, 1.0f);
		
		entity.add(animation);
		entity.add(bounds);
		entity.add(movement);
		entity.add(position);
		entity.add(state);
		entity.add(texture);
		
		engine.addEntity(entity);
	}
	
	private void createCamera(Entity target) {
		Entity entity = new Entity();
		
		CameraComponent camera = new CameraComponent();
		camera.camera = engine.getSystem(RenderingSystem.class).getCamera();
		camera.target = target;
		
		entity.add(camera);
		
		engine.addEntity(entity);
	}
	
	private void createBackground() {
		Entity entity = new Entity();
		
		BackgroundComponent background = new BackgroundComponent();
		TransformComponent position = new TransformComponent();
		TextureComponent texture = new TextureComponent();
		
		//texture.region = Assets.backgroundRegion;
		
		entity.add(background);
		entity.add(position);
		entity.add(texture);
		
		engine.addEntity(entity);
	}
}
