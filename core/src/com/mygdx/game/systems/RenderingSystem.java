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

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.PowerfulPandaApp;
import com.mygdx.game.components.DummyComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;

public class RenderingSystem extends IteratingSystem {

	private Array<Entity> renderQueue;
	private Comparator<Entity> comparator;

	private ComponentMapper<DummyComponent> dummyM;
	private ComponentMapper<TextureComponent> textureM;
	private ComponentMapper<TransformComponent> transformM;

	private PowerfulPandaApp game;

	public RenderingSystem(PowerfulPandaApp game) {
		super(Family.all(TransformComponent.class).one(TextureComponent.class, DummyComponent.class).get());
		this.game = game;

		dummyM = ComponentMapper.getFor(DummyComponent.class);
		textureM = ComponentMapper.getFor(TextureComponent.class);
		transformM = ComponentMapper.getFor(TransformComponent.class);

		renderQueue = new Array<Entity>();
		comparator = new Comparator<Entity>() {
			@Override
			public int compare(Entity entityA, Entity entityB) {
				return (int) Math.signum(transformM.get(entityB).pos.z - transformM.get(entityA).pos.z);
			}
		};
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		renderQueue.sort(comparator);

		game.camera.update();
		game.batcher.setProjectionMatrix(game.camera.combined);
		game.batcher.begin();
		game.shapeRenderer.setProjectionMatrix(game.camera.combined);
		game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

		for (Entity entity : renderQueue) {
			TextureComponent tex = textureM.get(entity);
			DummyComponent dum = dummyM.get(entity);
			TransformComponent t = transformM.get(entity);

			if (tex == null) {
				if (dum != null) {
					game.shapeRenderer.setColor(dum.color);
					game.shapeRenderer.rect(t.pos.x, t.pos.y, dum.width, dum.height);
				}
			} else if (tex.region != null) {

				float width = tex.region.getRegionWidth();
				float height = tex.region.getRegionHeight();

				game.shapeRenderer.rect(t.pos.x - (width / 2), t.pos.y - (height / 2), width, height);
				game.batcher.draw(tex.region, t.pos.x - (width / 2), t.pos.y - (height / 2), width / 2, height / 2, width, height, t.scale.x, t.scale.y, t.rotation + 50);

			} else if (dum != null) {
				game.shapeRenderer.setColor(dum.color);
				game.shapeRenderer.rect(t.pos.x, t.pos.y, dum.width, dum.height);
			}
		}

		game.batcher.end();
		game.shapeRenderer.end();
		renderQueue.clear();
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		renderQueue.add(entity);
	}

	public OrthographicCamera getCamera() {
		return game.camera;
	}
}
