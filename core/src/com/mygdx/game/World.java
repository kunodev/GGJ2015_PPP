/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
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

package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.components.*;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.systems.WallCollisionListener;
import de.panda.tiled.TextureHelper;

import java.util.List;

public class World {
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;

	public int state;

	public PowerfulPandaApp game;
	private Engine engine;

	public Entity player;
	public Entity boss;
	public Sound bossMucke;

	public World(PowerfulPandaApp game) {
		this.game = game;
		engine = game.engine;
	}

	public void create() {
		player = createPlayer();
		boss = createBoss();
		createCamera(player);
		//createBackground();

		this.state = WORLD_STATE_RUNNING;

		bossMucke = game.assetManager.get("Sound/Monster.mp3.mp3");
		bossMucke.loop(0.2f);
	}

	private Entity createPlayer() {
		Entity entity = new Entity();

		PlayerComponent bob = new PlayerComponent();
		BoundsComponent bounds = new BoundsComponent();
		MovementComponent movement = new MovementComponent();
		TransformComponent position = new TransformComponent();
		StateComponent state = new StateComponent();
		TextureComponent texture = new TextureComponent();
		CollisionComponent col = new CollisionComponent();
		AnimationComponent animComp = new AnimationComponent();

		// TODO Real playercollisionsListener
		col.listener = new WallCollisionListener();

		Texture walk = game.assetManager.get("Living/headbut_walk_animscheet.png");
		List<TextureRegion> walkList = TextureHelper.extractListOfRegions(walk, 4);
		animComp.animations.put(PlayerComponent.STATE_IDLE, new Animation(5, TextureHelper.cast(walkList)));
		animComp.animations.get(PlayerComponent.STATE_IDLE).setPlayMode(PlayMode.NORMAL);
		animComp.animations.put(PlayerComponent.STATE_WALKING, new Animation(0.3f, TextureHelper.cast(walkList)));
		animComp.animations.get(PlayerComponent.STATE_WALKING).setPlayMode(PlayMode.LOOP);

		Texture attack = game.assetManager.get("Living/headbut_attack_animscheet.png");
		List<TextureRegion> attckList = TextureHelper.extractListOfRegions(attack, 2);
		animComp.animations.put(PlayerComponent.STATE_HEADBUTT, new Animation(PlayerComponent.ATTACK_DURATION, TextureHelper.cast(attckList)));
		animComp.animations.get(PlayerComponent.STATE_HEADBUTT).setPlayMode(PlayMode.NORMAL);

		bounds.bounds.width = PlayerComponent.WIDTH;
		bounds.bounds.height = PlayerComponent.HEIGHT;

		position.pos.set(64f * 45f, 64f * 20, 0.0f);

		state.set(PlayerComponent.STATE_WALKING);

		entity.add(bob);
		entity.add(bounds);
		entity.add(movement);
		entity.add(position);
		entity.add(state);
		entity.add(texture);
		entity.add(col);
		entity.add(animComp);
		engine.addEntity(entity);

		return entity;
	}

	private Entity createBoss() {
		final Entity entity = new Entity();
		BossComponent boss = new BossComponent();
		BoundsComponent bounds = new BoundsComponent();
		MovementComponent movement = new MovementComponent();
		TransformComponent position = new TransformComponent();
		StateComponent state = new StateComponent();
		TextureComponent texture = new TextureComponent();
		AnimationComponent anim = new AnimationComponent();
		HealthComponent health = new HealthComponent();
		health.attackListener = new HealthComponent.AttackListener() {
			@Override
			public void attack(Entity enemy, int healthLeft) {
				if (healthLeft == 0) {
					game.engine.removeEntity(entity);
					bossMucke.stop();
					Sound world = game.assetManager.get("Sound/Level1Idee1.mp3.mp3");
					world.loop(0.2f);

					MovementComponent mc = player.getComponent(MovementComponent.class);
					mc.velocity.setZero();
					mc.accel.setZero();

					TransformComponent tc = player.getComponent(TransformComponent.class);
					tc.pos.x = 64f * 66f;
					tc.pos.y = 64f * 78f;
					tc.rotation = 0f;
				}
			}
		};

		Texture text = game.assetManager.get("Living/boss_sprite.png");
		anim.animations.put(BossComponent.STATE_ACTIONED, new Animation(Float.MAX_VALUE, new TextureRegion(text)));
		anim.animations.put(BossComponent.STATE_JUMP, new Animation(Float.MAX_VALUE, new TextureRegion(text)));
		anim.animations.put(BossComponent.STATE_MOVE, new Animation(Float.MAX_VALUE, new TextureRegion(text)));
		anim.animations.put(BossComponent.STATE_SHOOT, new Animation(Float.MAX_VALUE, new TextureRegion(text)));

		Texture tex = game.assetManager.get("Living/headbut_boss_attack_animsheet.png");
		anim.animations.put(BossComponent.STATE_WAIT, new Animation(BossComponent.WAIT_DURATION / 2, TextureHelper.cast(TextureHelper.extractListOfRegions(tex, 4))));
		bounds.bounds.width = BossComponent.WIDTH;
		bounds.bounds.height = BossComponent.HEIGHT;

		position.pos.set(1000.0f, 2000.0f, 1.0f);

		state.set(BossComponent.STATE_MOVE);

		entity.add(boss);
		entity.add(bounds);
		entity.add(movement);
		entity.add(position);
		entity.add(state);
		entity.add(texture);
		entity.add(anim);
		entity.add(health);

		engine.addEntity(entity);

		return entity;
	}

	public Entity createBullet() {
		Entity entity = new Entity();

		BulletComponent bullet = new BulletComponent();
		MovementComponent movement = new MovementComponent();
		TransformComponent position = new TransformComponent();
		StateComponent state = new StateComponent();
		TextureComponent texture = new TextureComponent();

		Vector3 playerPos = this.player.getComponent(TransformComponent.class).pos;
		Vector3 bossPos = this.boss.getComponent(TransformComponent.class).pos;

		position.pos.set(bossPos);
		bullet.targetVec = playerPos.cpy().sub(bossPos).nor().scl(BulletComponent.MOVE_VELOCITY);

		state.set(BulletComponent.STATE_MOVE);

		Texture tex = game.assetManager.get("Stuff/boss_attack_kugel.png");
		TextureRegion texReg = new TextureRegion(tex);
		texture.region = texReg;

		entity.add(bullet);
		// entity.add(bounds);
		entity.add(movement);
		entity.add(position);
		entity.add(state);
		entity.add(texture);

		engine.addEntity(entity);

		return entity;
	}

	private void createCamera(Entity target) {
		Entity entity = new Entity();

		CameraComponent camera = new CameraComponent();
		camera.camera = engine.getSystem(RenderingSystem.class).getCamera();
		camera.target = target;

		entity.add(camera);

		engine.addEntity(entity);
	}
}
