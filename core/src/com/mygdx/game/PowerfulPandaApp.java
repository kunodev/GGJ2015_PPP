package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PowerfulPandaApp extends Game {
	public SpriteBatch batcher;
	Texture img;
	
	@Override
	public void create () {
		batcher = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}
}