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
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.panda.tiled.MapRenderer;

public class MapLoaderTestScreen extends ScreenAdapter {
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_LEVEL_END = 3;
	static final int GAME_OVER = 4;

	PowerfulPandaApp game;

	Engine engine;
	MapRenderer renderer;

	private int state;

	public MapLoaderTestScreen(PowerfulPandaApp game) {
		this.game = game;
		engine = new Engine();
	}

	public void update(float deltaTime) {
		if (deltaTime > 0.1f)
			deltaTime = 0.1f;

		engine.update(deltaTime);
	}

	private void updateReady() {

	}

	private void updateRunning(float deltaTime) {
	}

	private void updatePaused() {
	}

	private void updateLevelEnd() {
	}

	private void updateGameOver() {
	}

	public void drawUI() {
	}

	private void presentReady() {
	}

	private void presentRunning() {
	}

	private void presentPaused() {
	}

	private void presentLevelEnd() {
	}

	private void presentGameOver() {
	}

	private void pauseSystems() {
	}

	private void resumeSystems() {
	}

	@Override
	public void show() {
		// use in own test-screen
		game.assetManager.load("stage_test.tmx", TiledMap.class);
		game.assetManager.finishLoading();

		renderer = new MapRenderer("stage_test.tmx", game.camera, game.assetManager);
		renderer.loadComponentsFromMap(engine);
	}

	@Override
	public void hide() {
		game.assetManager.clear();
	}

	@Override
	public void render(float delta) {
		renderer.render();
		update(delta);
	}

	@Override
	public void pause() {
	}
}