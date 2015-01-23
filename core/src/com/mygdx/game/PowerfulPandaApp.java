package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import de.panda.tiled.MapRenderer;

public class PowerfulPandaApp extends Game {
	public SpriteBatch batcher;
	Texture img;
	MapRenderer renderer;

	@Override
	public void create() {
		img = new Texture("badlogic.jpg");
		AssetManager manager = new AssetManager();
		manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		manager.load("stage_test.tmx", TiledMap.class);
		manager.finishLoading();

		OrthographicCamera cam = new OrthographicCamera();

		renderer = new MapRenderer("stage_test.tmx", cam, manager);
		renderer.loadComponentsFromMap(new Engine());

		batcher = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		// setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.render();
		super.render();
	}

}
