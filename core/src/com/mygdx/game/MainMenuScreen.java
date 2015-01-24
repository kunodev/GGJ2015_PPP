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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen extends ScreenAdapter {
	private final static float Y_START = 288f;
	private final static float Y_GAP = 32f;
	private final static float X_GAP = 150f;
	private final static float BUTTON_WIDTH = 600f;
	private final static float BUTTON_HEIGHT = 128f;

	PowerfulPandaApp game;
	Rectangle playBounds;
	Rectangle creditsBounds;
	Rectangle exitBounds;
	BitmapFont font;

	Vector3 touchPoint;
	Animation background;
	float backgroundStateTime = 0.0f;

	public MainMenuScreen(PowerfulPandaApp game) {
		this.game = game;
		game.camera.position.set(PowerfulPandaApp.DEFAULT_WIDTH / 2, PowerfulPandaApp.DEFAULT_HEIGHT / 2, 0);

		exitBounds = new Rectangle(X_GAP, Y_START + Y_GAP, BUTTON_WIDTH, BUTTON_HEIGHT);
		creditsBounds = new Rectangle(X_GAP, Y_START +  2 * Y_GAP + BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		playBounds = new Rectangle(X_GAP, Y_START +  3 * Y_GAP + 2 * BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);

		touchPoint = new Vector3();
	}

	public void update() {
		if (Gdx.input.justTouched()) {
			touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			game.camera.unproject(touchPoint);

			if (playBounds.contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(new GameScreen(game));
				return;
			}
			if (exitBounds.contains(touchPoint.x, touchPoint.y)) {
				System.exit(0);
				return;
			}
			if(creditsBounds.contains(touchPoint.x, touchPoint.y)){
				game.setScreen(new MapLoaderTestScreen(game));
				return;
			}

		}
	}

	public void draw() {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.camera.update();
		game.batcher.setProjectionMatrix(game.camera.combined);

		game.batcher.disableBlending();
		game.batcher.begin();
		game.batcher.draw(background.getKeyFrame(backgroundStateTime),0f, 0f);
		game.batcher.end();

		game.batcher.enableBlending();

		if (game.shapeRenderer != null) {
			game.shapeRenderer.setProjectionMatrix(game.camera.combined);
			game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			game.shapeRenderer.setColor(Color.NAVY);
			game.shapeRenderer.rect(playBounds.x, playBounds.y, playBounds.width, playBounds.height);
			game.shapeRenderer.rect(creditsBounds.x, creditsBounds.y, creditsBounds.width, creditsBounds.height);
			game.shapeRenderer.rect(exitBounds.x, exitBounds.y, exitBounds.width, exitBounds.height);
			game.shapeRenderer.end();
		}


		game.batcher.begin();

		float xOffset = 250f;
		float yOffset = 40f;
		font.setScale(6f);
		Vector2 center = new Vector2();
		playBounds.getCenter(center);
		font.draw(game.batcher, "PLAY", center.x-xOffset, center.y+yOffset);

		creditsBounds.getCenter(center);
		font.draw(game.batcher, "Credits", center.x-xOffset, center.y+yOffset);

		exitBounds.getCenter(center);
		font.draw(game.batcher, "EXIT", center.x-xOffset, center.y+yOffset);

		game.batcher.end();
	}

	@Override
	public void render(float delta) {
		backgroundStateTime += delta;
		update();
		draw();
	}

	@Override
	public void pause() {
		Settings.save();
	}

	@Override
	public void show() {
		game.assetManager.load("Background/menu_1.jpg", Texture.class);
		game.assetManager.load("Background/menu_2.jpg", Texture.class);
		game.assetManager.load("Background/menu_3.jpg", Texture.class);
		game.assetManager.finishLoading();

		font = new BitmapFont();

		background = new Animation(0.25f,
				new TextureRegion(game.assetManager.get("Background/menu_1.jpg", Texture.class)),
				new TextureRegion(game.assetManager.get("Background/menu_2.jpg", Texture.class)),
				new TextureRegion(game.assetManager.get("Background/menu_3.jpg", Texture.class))
		);

		background.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
	}

	@Override
	public void hide() {
		game.assetManager.clear();
		font.dispose();
	}
}
