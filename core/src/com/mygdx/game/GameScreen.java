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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.systems.*;
import de.panda.tiled.MapRenderer;

public class GameScreen extends ScreenAdapter {
	public static final int GAME_READY = 0;
	public static final int GAME_RUNNING = 1;
	public static final int GAME_PAUSED = 2;
	public static final int GAME_LEVEL_END = 3;
	public static final int GAME_OVER = 4;

	PowerfulPandaApp game;

	Vector3 touchPoint;
	World world;
	CollisionSystem.CollisionListener collisionListener;
	Rectangle pauseBounds;
	Rectangle resumeBounds;
	Rectangle quitBounds;

	Engine engine;

	private int state;

	BitmapFont font;
	private MapRenderer renderer;

	public GameScreen(PowerfulPandaApp game) {
		this.game = game;
		state = GAME_READY;
		game.camera.position.set(PowerfulPandaApp.DEFAULT_WIDTH / 2, PowerfulPandaApp.DEFAULT_HEIGHT / 2, 0);

		touchPoint = new Vector3();
		collisionListener = new CollisionSystem.CollisionListener() {
			@Override
			public void hit(Entity a, Entity b) {
				// Assets.playSound(Assets.hitSound);
			}
		};

		engine = game.engine;
		world = new World(game);

		engine.addSystem(new PlayerSystem(world));
		engine.addSystem(new BossSystem(world));
		engine.addSystem(new BulletSystem(world));
		engine.addSystem(new CameraSystem());
		engine.addSystem(new BackgroundSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new BoundsSystem());
		engine.addSystem(new StateSystem());
		engine.addSystem(new AnimationSystem());
		engine.addSystem(new CollisionSystem(world));
		engine.addSystem(new RenderingSystem(game));
		engine.addSystem(new SpawnEnemySystem(this, 5f));


		engine.getSystem(BackgroundSystem.class).setCamera(game.camera);

		pauseBounds = new Rectangle(8f, PowerfulPandaApp.DEFAULT_HEIGHT - 40f, 64f, 32f);
		resumeBounds = new Rectangle(80f, PowerfulPandaApp.DEFAULT_HEIGHT - 40f, 64f, 32f);
		quitBounds = new Rectangle(152f, PowerfulPandaApp.DEFAULT_HEIGHT - 40f, 64f, 32f);

		pauseSystems();
	}

	public void update(float deltaTime) {
		if (deltaTime > 0.1f)
			deltaTime = 0.1f;

		engine.update(deltaTime);

		switch (state) {
		case GAME_READY:
			updateReady();
			break;
		case GAME_RUNNING:
			updateRunning(deltaTime);
			break;
		case GAME_PAUSED:
			updatePaused();
			break;
		case GAME_LEVEL_END:
			updateLevelEnd();
			break;
		case GAME_OVER:
			updateGameOver();
			break;
		}
	}

	private void updateReady() {
		if (Gdx.input.justTouched()) {
			state = GAME_RUNNING;
			resumeSystems();
		}
	}

	private void updateRunning(float deltaTime) {
		if (Gdx.input.justTouched()) {
			game.camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (pauseBounds.contains(touchPoint.x, touchPoint.y)) {
				state = GAME_PAUSED;
				pauseSystems();
				return;
			}
		}


		if (world.state == World.WORLD_STATE_GAME_OVER) {
			state = GAME_OVER;
			pauseSystems();
		}
	}

	private void updatePaused() {
		if (Gdx.input.justTouched()) {
			game.camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (resumeBounds.contains(touchPoint.x, touchPoint.y)) {
				state = GAME_RUNNING;
				resumeSystems();
				return;
			}

			if (quitBounds.contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(new MainMenuScreen(game));
				return;
			}
		}
	}

	private void updateLevelEnd() {
		if (Gdx.input.justTouched()) {
			engine.removeAllEntities();
			world = new World(game);
			state = GAME_READY;
		}
	}

	private void updateGameOver() {
		if (Gdx.input.justTouched()) {
			game.setScreen(new MainMenuScreen(game));
		}
	}

	public void drawUI() {
		game.camera.update();
		game.batcher.setProjectionMatrix(game.camera.combined);

		game.batcher.begin();
		switch (state) {
		case GAME_READY:
			break;
		case GAME_RUNNING:
			break;
		case GAME_PAUSED:
			break;
		case GAME_LEVEL_END:
			break;
		case GAME_OVER:
			break;
		}
		font.setColor(Color.GREEN);
		font.draw(game.batcher, "QUIT", quitBounds.x, quitBounds.y + quitBounds.height);
		font.draw(game.batcher, "PAUSE", pauseBounds.x, pauseBounds.y + pauseBounds.height);
		font.draw(game.batcher, "RESUME", resumeBounds.x, resumeBounds.y + resumeBounds.height);

		game.batcher.end();

		if (game.shapeRenderer != null) {
			game.shapeRenderer.setProjectionMatrix(new Matrix4());

			game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			game.shapeRenderer.rect(quitBounds.x, quitBounds.y, quitBounds.width, quitBounds.height);
			game.shapeRenderer.rect(pauseBounds.x, pauseBounds.y, pauseBounds.width, pauseBounds.height);
			game.shapeRenderer.rect(resumeBounds.x, resumeBounds.y, resumeBounds.width, resumeBounds.height);
			game.shapeRenderer.end();
		}
	}

	private void pauseSystems() {
		engine.getSystem(PlayerSystem.class).setProcessing(false);
		engine.getSystem(MovementSystem.class).setProcessing(false);
		engine.getSystem(BoundsSystem.class).setProcessing(false);
		engine.getSystem(StateSystem.class).setProcessing(false);
		engine.getSystem(AnimationSystem.class).setProcessing(false);
		engine.getSystem(CollisionSystem.class).setProcessing(false);
		engine.getSystem(SpawnEnemySystem.class).setProcessing(false);
	}

	private void resumeSystems() {
		engine.getSystem(PlayerSystem.class).setProcessing(true);
		engine.getSystem(MovementSystem.class).setProcessing(true);
		engine.getSystem(BoundsSystem.class).setProcessing(true);
		engine.getSystem(StateSystem.class).setProcessing(true);
		engine.getSystem(AnimationSystem.class).setProcessing(true);
		engine.getSystem(CollisionSystem.class).setProcessing(true);
		engine.getSystem(SpawnEnemySystem.class).setProcessing(true);
	}

	@Override
	public void render(float delta) {
		renderer.render();
		update(delta);
		drawUI();
	}

	@Override
	public void pause() {
		if (state == GAME_RUNNING) {
			state = GAME_PAUSED;
			pauseSystems();
		}
	}

	@Override
	public void show() {
		game.assetManager.load("Stuff/boss_attack_kugel.png", Texture.class);
		game.assetManager.load("Living/boss_sprite.png", Texture.class);
		game.assetManager.load("Living/headbut_attack_animscheet.png", Texture.class);
		game.assetManager.load("Living/headbut_walk_animscheet.png", Texture.class);
		game.assetManager.load("stage_test.tmx", TiledMap.class);
		game.assetManager.load("Living/headbut_boss_attack_animsheet.png", Texture.class);
		game.assetManager.load("Living/buthead_gegner1_animsheet.png", Texture.class);
		game.assetManager.load("Living/buthead_ratte_gegner_animsheet.png", Texture.class);
		game.assetManager.load("Sound/headbut.mp3.mp3", Sound.class);
		game.assetManager.load("Sound/Level1Idee1.mp3.mp3", Sound.class);
		game.assetManager.load("Sound/Monster.mp3.mp3", Sound.class);
		game.assetManager.load("Sound/Outro.mp3.mp3", Sound.class);
		game.assetManager.load("Sound/Schwusch.mp3.mp3", Sound.class);
		game.assetManager.finishLoading();
		font = new BitmapFont();

		world.create();

		renderer = new MapRenderer("stage_test.tmx", game.camera, game.assetManager);
		renderer.loadComponentsFromMap(engine);

		resumeSystems();
		state = GAME_RUNNING;
	}

	@Override
	public void hide() {
		game.assetManager.clear();
		font.dispose();
	}

	public int getState() {
		return state;
	}
}