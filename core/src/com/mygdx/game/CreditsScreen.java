package com.mygdx.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class CreditsScreen extends ScreenAdapter {
    PowerfulPandaApp game;

    World world;

    Texture background;
    private Sound backgroundMucke;

    CreditsScreen(PowerfulPandaApp game){
        this.game = game;
        game.camera.position.set(PowerfulPandaApp.DEFAULT_WIDTH / 2, PowerfulPandaApp.DEFAULT_HEIGHT / 2, 0);
        world = new World(game);
    }

    @Override
    public void render(float delta) {
        background = new Texture("Background/menu_2.jpg");

        game.batcher.disableBlending();
        game.batcher.begin();
        game.batcher.draw(background, 0f, 0f, PowerfulPandaApp.DEFAULT_WIDTH, PowerfulPandaApp.DEFAULT_HEIGHT);
        game.batcher.end();
        game.batcher.enableBlending();
    }

    public void show() {
        game.assetManager.load("Background/menu_2.jpg", Texture.class);
        game.assetManager.load("Sound/Outro.mp3.mp3", Sound.class);
        game.assetManager.finishLoading();

        backgroundMucke = game.assetManager.get("Sound/Outro.mp3.mp3");
        backgroundMucke.loop();
    }
}
