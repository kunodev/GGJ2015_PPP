package com.mygdx.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.components.TextureComponent;

public class IntroScreen extends ScreenAdapter {
    static final int INTRO_READY = 0;
    static final int INTRO_1 = 1;
    static final int INTRO_2 = 2;
    static final int INTRO_3 = 3;
    static final int INTRO_FINISH = 4;

    PowerfulPandaApp game;

    World world;

    private int state;

    Texture background;
    float backgroundStateTime = 0.0f;
    private Sound backgroundMucke;

    public IntroScreen(PowerfulPandaApp game) {
        this.game = game;
        state = INTRO_READY;
        game.camera.position.set(PowerfulPandaApp.DEFAULT_WIDTH / 2, PowerfulPandaApp.DEFAULT_HEIGHT / 2, 0);
        world = new World(game);
    }

    @Override
    public void render(float delta) {
        switch (state) {
            case INTRO_READY:
                background = new Texture("Background/intro1.jpg");
                break;
            case INTRO_1:
                background = new Texture("Background/intro2.jpg");
                break;
            case INTRO_2:
                background = new Texture("Background/intro3.jpg");
                break;
            case INTRO_3:
                background = new Texture("Background/intro4.jpg");
                break;
            case INTRO_FINISH:
                backgroundMucke.stop();
                game.setScreen(new GameScreen(game));
                return;
        }

        game.batcher.disableBlending();
        game.batcher.begin();
        game.batcher.draw(background, 0f, 0f, PowerfulPandaApp.DEFAULT_WIDTH, PowerfulPandaApp.DEFAULT_HEIGHT);
        game.batcher.end();
        game.batcher.enableBlending();

        backgroundStateTime += delta;
        if(backgroundStateTime >= 4) {
            backgroundStateTime -= 4;
            state++;
        }
    }

    public void show() {
        game.assetManager.load("Background/intro1.jpg", Texture.class);
        game.assetManager.load("Background/intro2.jpg", Texture.class);
        game.assetManager.load("Background/intro3.jpg", Texture.class);
        game.assetManager.load("Background/intro4.jpg", Texture.class);
        game.assetManager.load("Sound/Level1Idee1.mp3.mp3", Sound.class);
        game.assetManager.finishLoading();

        backgroundMucke = game.assetManager.get("Sound/Level1Idee1.mp3.mp3");
        backgroundMucke.loop(0.1f);
    }
}
