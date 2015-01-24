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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.components.AnimationComponent;
import com.mygdx.game.components.BackgroundComponent;
import com.mygdx.game.components.BossComponent;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.BulletComponent;
import com.mygdx.game.components.CameraComponent;
import com.mygdx.game.components.CollisionComponent;
import com.mygdx.game.components.DummyComponent;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.systems.WallCollisionListener;

public class World {
	public static final float WORLD_WIDTH = 10;
	public static final float WORLD_HEIGHT = 15 * 20;
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	public static final Vector2 gravity = new Vector2(0, -12);

	public final Random rand;
	public int state;

	public PowerfulPandaApp game;
	private Engine engine;

	public Entity bob;
	public Entity boss;

	public World(PowerfulPandaApp game) {
		this.game = game;
		engine = game.engine;
		this.rand = new Random();
	}

	public void create() {
		bob = createBob();
		boss = createBoss();
		createCamera(bob);
		createBackground();

		this.state = WORLD_STATE_RUNNING;
	}

	private Entity createBob() {
		Entity entity = new Entity();

		// AnimationComponent animation = new AnimationComponent();
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

		// animation.animations.put(PlayerComponent.STATE_WALK, );
		Texture walk = game.assetManager.get("Living/headbut_walk_animscheet.png");
		List<TextureRegion> walkList = extractListOfRegions(walk, 4);
		animComp.animations.put(PlayerComponent.STATE_IDLE, new Animation(5, cast(walkList)));
		animComp.animations.get(PlayerComponent.STATE_IDLE).setPlayMode(PlayMode.NORMAL);
		animComp.animations.put(PlayerComponent.STATE_WALKING, new Animation(0.3f, cast(walkList)));
		animComp.animations.get(PlayerComponent.STATE_WALKING).setPlayMode(PlayMode.LOOP);

		Texture attack = game.assetManager.get("Living/headbut_attack_animscheet.png");
		List<TextureRegion> attckList = extractListOfRegions(attack, 2);
		animComp.animations.put(PlayerComponent.STATE_HEADBUTT, new Animation(PlayerComponent.ATTACK_DURATION, cast(attckList)));
		animComp.animations.get(PlayerComponent.STATE_HEADBUTT).setPlayMode(PlayMode.NORMAL);

		bounds.bounds.width = PlayerComponent.WIDTH;
		bounds.bounds.height = PlayerComponent.HEIGHT;

		position.pos.set(5.0f, 1.0f, 0.0f);

		state.set(PlayerComponent.STATE_WALKING);

		// entity.add(animation);
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

	private TextureRegion[] cast(List<TextureRegion> walkList) {
		TextureRegion[] result = new TextureRegion[walkList.size()];
		walkList.toArray(result);
		return result;
	}

	private List<TextureRegion> extractListOfRegions(Texture walk, int xParts) {
		List<TextureRegion> walkList = new ArrayList<TextureRegion>();
		int heightOfOne = walk.getHeight();
		int widthOfOne = walk.getWidth() / xParts;
		for (int i = 0; i < xParts; i++) {
			TextureRegion nextOne = new TextureRegion(walk, widthOfOne * i, 0, widthOfOne, heightOfOne);
			walkList.add(nextOne);
		}
		return walkList;
	}

	private Entity createBoss() {
		final Entity entity = new Entity();

		// AnimationComponent animation = new AnimationComponent();
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
					System.out.println("DEINE MUDDA");
					game.engine.removeEntity(entity);
				} else {
					System.out.println("ATTACK");
				}
			}
		};

		Texture text = game.assetManager.get("Living/boss_sprite.png");
		anim.animations.put(BossComponent.STATE_ACTIONED, new Animation(Float.MAX_VALUE, new TextureRegion(text)));
		anim.animations.put(BossComponent.STATE_JUMP, new Animation(Float.MAX_VALUE, new TextureRegion(text)));
		anim.animations.put(BossComponent.STATE_MOVE, new Animation(Float.MAX_VALUE, new TextureRegion(text)));
		anim.animations.put(BossComponent.STATE_SHOOT, new Animation(Float.MAX_VALUE, new TextureRegion(text)));

		Texture tex = game.assetManager.get("Living/headbut_boss_attack_animsheet.png");
		anim.animations.put(BossComponent.STATE_WAIT, new Animation(BossComponent.WAIT_DURATION, cast(extractListOfRegions(tex, 4))));
		bounds.bounds.width = BossComponent.WIDTH;
		bounds.bounds.height = BossComponent.HEIGHT;

		position.pos.set(500.0f, 200.0f, 1.0f);

		state.set(BossComponent.STATE_MOVE);

		// entity.add(animation);
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

		AnimationComponent animation = new AnimationComponent();
		BulletComponent bullet = new BulletComponent();
		MovementComponent movement = new MovementComponent();
		TransformComponent position = new TransformComponent();
		StateComponent state = new StateComponent();
		TextureComponent texture = new TextureComponent();
		DummyComponent dummy = new DummyComponent();

		// bounds.bounds.width = BulletComponent.WIDTH;
		// bounds.bounds.height = BulletComponent.HEIGHT;

		Vector3 playerPos = this.bob.getComponent(TransformComponent.class).pos;
		Vector3 bossPos = this.boss.getComponent(TransformComponent.class).pos;

		position.pos.set(bossPos);
		bullet.targetVec = playerPos.cpy().sub(bossPos).nor().scl(BulletComponent.MOVE_VELOCITY);

		state.set(BulletComponent.STATE_MOVE);

		dummy.color = Color.BLUE;
		dummy.width = bullet.WIDTH;
		dummy.height = bullet.HEIGHT;

		Texture tex = game.assetManager.get("Stuff/boss_attack_kugel.png");
		TextureRegion texReg = new TextureRegion(tex);
		texture.region = texReg;

		entity.add(animation);
		entity.add(bullet);
		// entity.add(bounds);
		entity.add(movement);
		entity.add(position);
		entity.add(state);
		entity.add(dummy);

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

	private void createBackground() {
		Entity entity = new Entity();

		BackgroundComponent background = new BackgroundComponent();
		TransformComponent position = new TransformComponent();
		TextureComponent texture = new TextureComponent();

		// texture.region = Assets.backgroundRegion;

		entity.add(background);
		entity.add(position);
		entity.add(texture);

		engine.addEntity(entity);
	}

}
