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
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.World;
import com.mygdx.game.components.*;

public class CollisionSystem extends IteratingSystem {
	private ComponentMapper<TransformComponent> tm;
	private ComponentMapper<CollisionComponent> cm;
	private ComponentMapper<TextureComponent> texm;
	private ComponentMapper<DummyComponent> dm;
	private ComponentMapper<HealthComponent> enemies;
	private ComponentMapper<PlayerComponent> players;

	public static interface CollisionListener {
		public void hit(Entity thisEntity, Entity otherEntity);
	}

	private World world;
	public ImmutableArray<Entity> collidables;
	private static final Family colliderFamily = Family.all(TransformComponent.class).one(WallComponent.class, CollisionComponent.class, HealthComponent.class).get();

	public CollisionSystem(World world) {
		super(colliderFamily);
		this.world = world;

		tm = ComponentMapper.getFor(TransformComponent.class);
		cm = ComponentMapper.getFor(CollisionComponent.class);
		texm = ComponentMapper.getFor(TextureComponent.class);
		dm = ComponentMapper.getFor(DummyComponent.class);
		enemies = ComponentMapper.getFor(HealthComponent.class);
		players = ComponentMapper.getFor(PlayerComponent.class);
	}

	@Override
	public void update(float deltaTime) {
		collidables = super.getEntities();
		super.update(deltaTime);
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		Rectangle thisEntityRect = buildRectangle(entity);
		for (Entity collidable : collidables) {
			if(collidable.getId() == entity.getId() || players.has(collidable)){
				continue;
			}

			Rectangle collidableRect = buildRectangle(collidable);
			if (thisEntityRect.overlaps(collidableRect)) {
				PlayerComponent pc = players.get(entity);
				HealthComponent hc = enemies.get(collidable);
				if(pc != null && hc != null && entity.getComponent(StateComponent.class).get() == PlayerComponent.STATE_HEADBUTT){
					hc.health = Math.max(0, hc.health - 10);
					if(hc.attackListener != null){
						hc.attackListener.attack(collidable, hc.health);
					}
				}else {
					// skip enemy vs. enemy
					HealthComponent entityHc = entity.getComponent(HealthComponent.class);
					if(hc != null && entityHc != null){
						continue;
					}
					getCollisionComponent(entity).listener.hit(entity, collidable);

					Vector2 vector2 = entity.getComponent(MovementComponent.class).velocity;

					if (vector2.x > 0) {
						entity.getComponent(TransformComponent.class).pos.x -= 5;
					} else if (vector2.x < 0) {
						entity.getComponent(TransformComponent.class).pos.x += 5;
					}

					if (vector2.y> 0) {
						entity.getComponent(TransformComponent.class).pos.y -= 5;
					} else if (vector2.y < 0) {
						entity.getComponent(TransformComponent.class).pos.y += 5;
					}
				}
			}
		}
	}

	private Rectangle buildRectangle(Entity entity) {
		Rectangle result = new Rectangle(0, 0, 0, 0);
		CollisionComponent collisionComp = getCollisionComponent(entity);

		if (collisionComp != null && collisionComp.width > 0) {
			result.setWidth(collisionComp.width);
			result.setHeight(collisionComp.height);
		} else {
			TextureComponent texComp = texm.get(entity);
			if (texComp != null) {
				result.setWidth(texComp.region.getRegionWidth());
				result.setHeight(texComp.region.getRegionHeight());
			}
			DummyComponent dummyComp = dm.get(entity);
			if (dummyComp != null) {
				result.setWidth(dummyComp.width);
				result.setHeight(dummyComp.height);
			}
		}
		Vector3 pos2 = tm.get(entity).pos;
		Vector2 pos = new Vector2(pos2.x, pos2.y);
		result.setCenter(pos);
		return result;
	}

	private CollisionComponent getCollisionComponent(Entity entity) {
		CollisionComponent collisionComp = cm.get(entity);
		if (collisionComp == null) {
			collisionComp = entity.getComponent(WallComponent.class);
		}
		return collisionComp;
	}
}
