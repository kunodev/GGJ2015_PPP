package de.panda.tiled;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.PowerfulPandaApp;
import com.mygdx.game.components.*;

public class MobFactory {

    public enum ENMEY_TYPE {
        RAT,
        MONSTER_ONE
    }

    public static Entity spawnMob(ENMEY_TYPE enemyType, PowerfulPandaApp game, Vector2 pos) {
        Entity e = new Entity();
        TextureComponent txc = new TextureComponent();
        AnimationComponent ac = new AnimationComponent();
        EnemyComponent ec = new EnemyComponent();
        MovementComponent mc = new MovementComponent();
        TransformComponent tc = new TransformComponent();
        HealthComponent hc = new HealthComponent();
        hc.attackListener = game.enemyKiller;

        StateComponent sc = new StateComponent();
        tc.pos.set(pos.x, pos.y, 0);

        Vector2 vel = new Vector2((float) Math.random(), (float) Math.random()).nor().scl((float) Math.random() * 48f);
        mc.velocity.x = vel.x;
        mc.velocity.y = vel.y;

        tc.rotation = vel.scl(-1f).angle();

        switch (enemyType) {
            case RAT: {
                Texture rat = game.assetManager.get("Living/buthead_ratte_gegner_animsheet.png");
                Animation ani = new Animation(0.5f, TextureHelper.doTextureMagic(rat, 2));
                //set region to get bounding rect for collision
                txc.region = ani.getKeyFrame(0f);
                ani.setPlayMode(Animation.PlayMode.LOOP);
                ac.animations.put(EnemyComponent.STATE_RUNNING_AROUND, ani);
                sc.set(EnemyComponent.STATE_RUNNING_AROUND);
                hc.health = 300;
            }
            break;
            case MONSTER_ONE: {
                Texture mon = game.assetManager.get("Living/buthead_gegner1_animsheet.png");
                Animation ani = new Animation(0.75f, TextureHelper.doTextureMagic(mon, 3));
                //set region to get bounding rect for collision
                ani.setPlayMode(Animation.PlayMode.LOOP);
                txc.region = ani.getKeyFrame(0f);
                ac.animations.put(EnemyComponent.STATE_RUNNING_AROUND, ani);
                sc.set(EnemyComponent.STATE_RUNNING_AROUND);
                hc.health = 600;
            }
            break;
        }
        e.add(txc);
        e.add(ac);
        e.add(ec);
        e.add(mc);
        e.add(tc);
        e.add(hc);
        e.add(sc);

        game.engine.addEntity(e);

        return e;
    }
}
