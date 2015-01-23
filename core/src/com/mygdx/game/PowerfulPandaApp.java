package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class PowerfulPandaApp extends Game {
	public static float DEFAULT_WIDTH = 1920;
	public static float DEFAULT_HEIGHT = 1080;

	AssetManager assetManager;
	SpriteBatch batcher;
	ShapeRenderer shapeRenderer;
	OrthographicCamera camera;

	@Override
	public void create() {
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

		batcher = new SpriteBatch();
		camera = new OrthographicCamera(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		// debugging settings
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		shapeRenderer = new ShapeRenderer();

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
}