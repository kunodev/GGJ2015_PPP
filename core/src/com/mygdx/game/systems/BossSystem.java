package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
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
			if (this.time >= 3.0f) {
				this.time -= 3.0f;
				state.set(BossComponent.STATE_WAIT);
			}
		}

		if (state.get() == BossComponent.STATE_WAIT) {
			mov.velocity.set(0, 0);

			if (this.time >= 1.5f) {
				this.time -= 1.5f;
				state.set(BossComponent.STATE_SHOOT);
			}
		}

		if (state.get() == BossComponent.STATE_SHOOT) {
			System.out.println("peng");
			world.createBullet();
			state.set(BossComponent.STATE_SHOOTED);
		}

		if (state.get() == BossComponent.STATE_SHOOTED) {
			if (this.time >= 1.0f) {
				this.time -= 1.0f;
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
