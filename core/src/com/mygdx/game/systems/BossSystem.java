package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap.Entries;
import com.mygdx.game.World;
import com.mygdx.game.components.AnimationComponent;
import com.mygdx.game.components.BossComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;

public class BossSystem extends IteratingSystem {
	@SuppressWarnings("unchecked")
	private static final Family family = Family.all(BossComponent.class, StateComponent.class, TransformComponent.class, MovementComponent.class)
			.get();

	private World world;

	private Float time = 0.0f;

	private ComponentMapper<BossComponent> bm;
	private ComponentMapper<StateComponent> sm;
	private ComponentMapper<TransformComponent> tm;
	private ComponentMapper<MovementComponent> mm;
	private ComponentMapper<PlayerComponent> pm;

	public BossSystem(World world) {
		super(family);

		this.world = world;

		bm = ComponentMapper.getFor(BossComponent.class);
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
		this.time = this.time + deltaTime;

		TransformComponent t = tm.get(entity);
		StateComponent state = sm.get(entity);
		MovementComponent mov = mm.get(entity);
		Entity player = world.bob;

		Vector2 targetVec = getDeepCopyCentralPos(player).sub(getDeepCopyCentralPos(entity)).nor().scl(BossComponent.MOVE_VELOCITY);
		float angle = targetVec.angle();
		t.rotation = angle + 180f;
		if (state.get() == BossComponent.STATE_MOVE) {
			mov.velocity.set(targetVec.x, targetVec.y);
			if (this.time >= BossComponent.MOVEMENT_DURATION) {
				this.time -= BossComponent.MOVEMENT_DURATION;
				state.set(BossComponent.STATE_WAIT);
			}
		}

		if (state.get() == BossComponent.STATE_WAIT) {
			mov.velocity.set(0, 0);

			if (this.time >= BossComponent.WAIT_DURATION) {
				this.time -= BossComponent.WAIT_DURATION;

				double rand = Math.random();
				if (rand >= 0.4) {
					state.set(BossComponent.STATE_SHOOT);

				} else {
					state.set(BossComponent.STATE_JUMP);
				}
			}
		}
		if (state.get() == BossComponent.STATE_SHOOT) {
			world.createBullet();
			state.set(BossComponent.STATE_ACTIONED);
			Sound swusch = world.game.assetManager.get("Sound/Schwusch.mp3.mp3");
			swusch.play();
		}

		if (state.get() == BossComponent.STATE_JUMP) {
			System.out.println("jump");
			float distance = 10.0f;
			int xPlus = (int) Math.round(Math.random());
			int yPlus = (int) Math.round(Math.random());
			float xRand = (float) Math.random() * 10;
			float yRand = (float) Math.random() * 10;
			if (xPlus == 1) {
				t.pos.x += distance * xRand;
			} else {
				t.pos.x -= distance * xRand;
			}
			if (yPlus == 1) {
				t.pos.y += distance * yRand;
			} else {
				t.pos.y -= distance * yRand;
			}
			state.set(BossComponent.STATE_ACTIONED);
		}

		if (state.get() == BossComponent.STATE_ACTIONED) {
			if (this.time >= BossComponent.COOLDOWN) {
				this.time -= BossComponent.COOLDOWN;
				state.set(BossComponent.STATE_MOVE);
			}
		}
	}

	public static Vector2 getDeepCopyCentralPos(Entity e) {
		Vector3 pos3d = e.getComponent(TransformComponent.class).pos;
		Vector2 pos = new Vector2(pos3d.x, pos3d.y);
		TextureRegion region = e.getComponent(TextureComponent.class).region;
		Vector2 offset;

		if (region != null) {
			offset = new Vector2(region.getRegionWidth() / 2, region.getRegionHeight() / 2);
		} else {
			AnimationComponent component = e.getComponent(AnimationComponent.class);
			Entries<Animation> entries = component.animations.entries();
			region = entries.next().value.getKeyFrames()[0];
			offset = new Vector2(region.getRegionWidth() / 2, region.getRegionHeight() / 2);

		}
		return pos.add(offset);
	}
}
