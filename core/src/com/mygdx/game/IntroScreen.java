package com.mygdx.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.components.TextureComponent;

public class IntroScreen extends ScreenAdapter {
    static final int INTRO_READY = 0;
    static final int INTRO_1 = 1;
    static final int INTRO_2 = 2;
    static final int INTRO_3 = 3;
    static final int INTRO_4 = 4;

    PowerfulPandaApp game;

    World world;

    private int state;

    Texture background;
    float backgroundStateTime = 0.0f;

    public IntroScreen(PowerfulPandaApp game) {
        this.game = game;
        state = INTRO_READY;
        game.camera.position.set(PowerfulPandaApp.DEFAULT_WIDTH / 2, PowerfulPandaApp.DEFAULT_HEIGHT / 2, 0);
        world = new World(game);
    }

    public void update(float deltaTime) {
        backgroundStateTime += deltaTime;

        TextureComponent texComp = new TextureComponent();
        Texture tex = game.assetManager.get("menu_1.jpg");
        texComp.region = new TextureRegion(tex);

        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void render(float delta) {
        update(delta);
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

        background = new Texture("Background/menu_1.jpg");
    }

    @Override
    public void hide() {
        game.assetManager.clear();
    }
}
