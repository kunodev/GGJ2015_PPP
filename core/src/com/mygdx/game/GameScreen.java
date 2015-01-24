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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.systems.*;

public class GameScreen extends ScreenAdapter {
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_LEVEL_END = 3;
	static final int GAME_OVER = 4;

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

	public GameScreen(PowerfulPandaApp game) {
		this.game = game;
		state = GAME_READY;
		game.camera.position.set(PowerfulPandaApp.DEFAULT_WIDTH / 2, PowerfulPandaApp.DEFAULT_HEIGHT / 2, 0);

		touchPoint = new Vector3();
		collisionListener = new CollisionSystem.CollisionListener() {
			@Override
			public void hit() {
				//Assets.playSound(Assets.hitSound);
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
		engine.addSystem(new CollisionSystem(world, collisionListener));
		engine.addSystem(new RenderingSystem(game));

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
				//Assets.playSound(Assets.clickSound);
				state = GAME_PAUSED;
				pauseSystems();
				return;
			}
		}

		float accelX = 0.0f;
		float accelY = 0.0f;

			if (Gdx.input.isKeyPressed(Keys.A))
				accelX = 5f;
			if (Gdx.input.isKeyPressed(Keys.D))
				accelX = -5f;
			if (Gdx.input.isKeyPressed(Keys.S))
				accelY = 5f;
			if (Gdx.input.isKeyPressed(Keys.W))
				accelY = -5f;

		Vector3 v3 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f).sub(world.bob.getComponent(TransformComponent.class).pos);

		world.bob.getComponent(TransformComponent.class).rotation = new Vector2(v3.x, v3.y).angle();
		engine.getSystem(PlayerSystem.class).setAccelX(accelX);
		engine.getSystem(PlayerSystem.class).setAccelY(accelY);

		// if (world.state == World.WORLD_STATE_NEXT_LEVEL) {
		// game.setScreen(new WinScreen(game));
		// }

		if (world.state == World.WORLD_STATE_GAME_OVER) {
			state = GAME_OVER;
			pauseSystems();
		}
	}

	private void updatePaused() {
		if (Gdx.input.justTouched()) {
			game.camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (resumeBounds.contains(touchPoint.x, touchPoint.y)) {
				//Assets.playSound(Assets.clickSound);
				state = GAME_RUNNING;
				resumeSystems();
				return;
			}

			if (quitBounds.contains(touchPoint.x, touchPoint.y)) {
				//Assets.playSound(Assets.clickSound);
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
		game.batcher.draw(game.assetManager.get("f.png", Texture.class),128.0f, 128.0f);
		switch (state) {
		case GAME_READY:
			presentReady();
			break;
		case GAME_RUNNING:
			presentRunning();
			break;
		case GAME_PAUSED:
			presentPaused();
			break;
		case GAME_LEVEL_END:
			presentLevelEnd();
			break;
		case GAME_OVER:
			presentGameOver();
			break;
		}
		font.setColor(Color.GREEN);
		font.draw(game.batcher, "QUIT", quitBounds.x, quitBounds.y + quitBounds.height);
		font.draw(game.batcher, "PAUSE", pauseBounds.x, pauseBounds.y + pauseBounds.height);
		font.draw(game.batcher, "RESUME", resumeBounds.x, resumeBounds.y + resumeBounds.height);

		game.batcher.end();

		if(game.shapeRenderer != null){
			game.shapeRenderer.setProjectionMatrix(game.camera.combined);

			game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			game.shapeRenderer.rect(quitBounds.x, quitBounds.y, quitBounds.width, quitBounds.height);
			game.shapeRenderer.rect(pauseBounds.x, pauseBounds.y, pauseBounds.width, pauseBounds.height);
			game.shapeRenderer.rect(resumeBounds.x, resumeBounds.y, resumeBounds.width, resumeBounds.height);
			game.shapeRenderer.end();
		}
	}

	private void presentReady() {
		//game.batcher.draw(Assets.ready, 160 - 192 / 2, 240 - 32 / 2, 192, 32);
	}

	private void presentRunning() {
		//game.batcher.draw(Assets.pause, 320 - 64, 480 - 64, 64, 64);
		//Assets.font.draw(game.batcher, scoreString, 16, 480 - 20);
	}

	private void presentPaused() {
		//game.batcher.draw(Assets.pauseMenu, 160 - 192 / 2, 240 - 96 / 2, 192, 96);
		//Assets.font.draw(game.batcher, scoreString, 16, 480 - 20);
	}

	private void presentLevelEnd() {
		String topText = "the princess is ...";
		String bottomText = "in another castle!";
		//float topWidth = Assets.font.getBounds(topText).width;
		//float bottomWidth = Assets.font.getBounds(bottomText).width;
		//Assets.font.draw(game.batcher, topText, 160 - topWidth / 2, 480 - 40);
		//Assets.font.draw(game.batcher, bottomText, 160 - bottomWidth / 2, 40);
	}

	private void presentGameOver() {
		//game.batcher.draw(Assets.gameOver, 160 - 160 / 2, 240 - 96 / 2, 160, 96);
		//float scoreWidth = Assets.font.getBounds(scoreString).width;
		//Assets.font.draw(game.batcher, scoreString, 160 - scoreWidth / 2, 480 - 20);
	}

	private void pauseSystems() {
		engine.getSystem(PlayerSystem.class).setProcessing(false);
		engine.getSystem(MovementSystem.class).setProcessing(false);
		engine.getSystem(BoundsSystem.class).setProcessing(false);
		engine.getSystem(StateSystem.class).setProcessing(false);
		engine.getSystem(AnimationSystem.class).setProcessing(false);
		engine.getSystem(CollisionSystem.class).setProcessing(false);
	}

	private void resumeSystems() {
		engine.getSystem(PlayerSystem.class).setProcessing(true);
		engine.getSystem(MovementSystem.class).setProcessing(true);
		engine.getSystem(BoundsSystem.class).setProcessing(true);
		engine.getSystem(StateSystem.class).setProcessing(true);
		engine.getSystem(AnimationSystem.class).setProcessing(true);
		engine.getSystem(CollisionSystem.class).setProcessing(true);
	}

	@Override
	public void render(float delta) {
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
		game.assetManager.load("f.png", Texture.class);
		game.assetManager.finishLoading();
		font = new BitmapFont();

		world.create();
	}

	@Override
	public void hide() {
		game.assetManager.clear();
		font.dispose();
	}
}