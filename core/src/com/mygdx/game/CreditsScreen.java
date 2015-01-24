package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.systems.RenderingSystem;

public class CreditsScreen extends ScreenAdapter {

    PowerfulPandaApp game;
    Engine engine;
    Entity testEnte;

    CreditsScreen(PowerfulPandaApp game){
        this.game = game;
        this.engine = game.engine;
        engine.addSystem(new RenderingSystem(game));
    }

    public void createTestEnte(){
        TransformComponent transformComp = new TransformComponent();
        TextureComponent texComp = new TextureComponent();

        transformComp.pos.x = 128.0f;
        transformComp.pos.y = 128.0f;

        Texture tex = game.assetManager.get("f.png");
        texComp.region = new TextureRegion(tex);

        testEnte = new Entity();
        testEnte.add(transformComp);
        testEnte.add(texComp);

        game.engine.addEntity(testEnte);
    }

    @Override
    public void show() {
        game.assetManager.load("f.png", Texture.class);
        game.assetManager.finishLoading();

        createTestEnte();
    }

    @Override
    public void hide() {
        game.assetManager.clear();
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
    }
}
