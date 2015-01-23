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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen extends ScreenAdapter {
	PowerfulPandaApp game;
	Rectangle playBounds;
	Rectangle creditsBounds;
	Rectangle tiledBounds;

	Vector3 touchPoint;

	public MainMenuScreen(PowerfulPandaApp game) {
		this.game = game;
		game.camera.position.set(PowerfulPandaApp.DEFAULT_WIDTH / 2, PowerfulPandaApp.DEFAULT_HEIGHT / 2, 0);
		playBounds = new Rectangle(160 - 150, 200 + 18, 300, 36);
		tiledBounds = new Rectangle(10, 200 + 18 + 50, 300, 36);

		creditsBounds = new Rectangle(10f, 10f, 128f, 32f);
		touchPoint = new Vector3();
	}

	public void update() {
		if (Gdx.input.justTouched()) {
			game.camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (playBounds.contains(touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new GameScreen(game));
				return;
			}
			if (tiledBounds.contains(touchPoint.x, touchPoint.y)) {
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
		// background
		game.batcher.end();

		game.batcher.enableBlending();
		game.batcher.begin();

		game.batcher.end();

		if (game.shapeRenderer != null) {
			game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			game.shapeRenderer.setColor(Color.GREEN);
			game.shapeRenderer.rect(playBounds.x, playBounds.y, playBounds.width, playBounds.height);
			game.shapeRenderer.rect(creditsBounds.x, creditsBounds.y, creditsBounds.width, creditsBounds.height);
			game.shapeRenderer.rect(tiledBounds.x, tiledBounds.y, tiledBounds.width, tiledBounds.height);
			game.shapeRenderer.end();
		}
	}

	@Override
	public void render(float delta) {
		update();
		draw();
	}

	@Override
	public void pause() {
		Settings.save();
	}

	@Override
	public void show() {
		// game.assetManager.load("/pathToTexture.png", Texture.class);
		game.assetManager.finishLoading();
	}

	@Override
	public void dispose() {
		game.assetManager.clear();
	}
}