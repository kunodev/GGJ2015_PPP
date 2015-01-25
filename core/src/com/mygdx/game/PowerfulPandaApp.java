package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
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
import com.mygdx.game.components.HealthComponent;

public class PowerfulPandaApp extends Game {
	public static float DEFAULT_WIDTH = 960;
	public static float DEFAULT_HEIGHT = 540;

	public AssetManager assetManager;
	public SpriteBatch batcher;
	public ShapeRenderer shapeRenderer;
	public OrthographicCamera camera;
	public Engine engine;

	public int enemyCounter = 0;
	public HealthComponent.AttackListener enemyKiller = new HealthComponent.AttackListener() {
		@Override
		public void attack(Entity enemy, int healthLeft) {
			if (healthLeft == 0) {
				engine.removeEntity(enemy);
				enemyCounter -= 1;
			}
		}
	};

	@Override
	public void create() {
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

		batcher = new SpriteBatch();
		camera = new OrthographicCamera(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		Gdx.graphics.setDisplayMode((int)DEFAULT_WIDTH, (int)DEFAULT_HEIGHT, false);

		// debugging settings
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		shapeRenderer = new ShapeRenderer();

		engine = new Engine();

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
