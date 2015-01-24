/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
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

package com.mygdx.game.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.World;
import com.mygdx.game.components.*;

import java.util.Random;

public class CollisionSystem extends EntitySystem {
	private ComponentMapper<BoundsComponent> bm;
	private ComponentMapper<MovementComponent> mm;
	private ComponentMapper<StateComponent> sm;
	private ComponentMapper<TransformComponent> tm;
	
	public static interface CollisionListener {
		public void hit();
	}

	private Engine engine;
	private World world;
	private CollisionListener listener;
	private Random rand = new Random();
	private ImmutableArray<Entity> player;
	
	public CollisionSystem(World world, CollisionListener listener) {
		this.world = world;
		this.listener = listener;
		
		bm = ComponentMapper.getFor(BoundsComponent.class);
		mm = ComponentMapper.getFor(MovementComponent.class);
		sm = ComponentMapper.getFor(StateComponent.class);
		tm = ComponentMapper.getFor(TransformComponent.class);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		this.engine = engine;
		
		player = engine.getEntitiesFor(Family.getFor(PlayerComponent.class, BoundsComponent.class, TransformComponent.class, StateComponent.class));
	}
	
	@Override
	public void update(float deltaTime) {
		PlayerSystem playerSystem = engine.getSystem(PlayerSystem.class);
		
		for (int i = 0; i < player.size(); ++i) {
			Entity bob = player.get(i);
			
			StateComponent bobState = sm.get(bob);
			
			if (bobState.get() == PlayerComponent.STATE_HIT) {
				continue;
			}
			
			MovementComponent bobMov = mm.get(bob);
			BoundsComponent bobBounds = bm.get(bob);
			
			if (bobMov.velocity.y < 0.0f) {
				TransformComponent bobPos = tm.get(bob);
			}
		}
	}
}
