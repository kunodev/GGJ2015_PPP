package com.mygdx.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameScreen;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.TransformComponent;
import de.panda.tiled.MobFactory;

import java.util.Random;

public class SpawnEnemySystem extends IntervalIteratingSystem {
    private static float MIN_SPAWN_RADIUS = 128f;
    private static float MAX_SPAWN_RADIUS = 512f;
    private static int MAX_ENEMY_COUNT = 64;

    final GameScreen gameScreen;
    Random random;

    public SpawnEnemySystem(GameScreen gameScreen, float interval) {
        super(Family.all(PlayerComponent.class).get(), interval);
        this.gameScreen = gameScreen;
        random = new Random();
    }

    @Override
    protected void updateInterval() {
        if (gameScreen.getState() == GameScreen.GAME_RUNNING) {
            super.updateInterval();
        }
    }

    @Override
    protected void processEntity(Entity entity) {
        if (gameScreen.getGame().enemyCounter >= MAX_ENEMY_COUNT) {
            return;
        }

        //player pos ref
        Vector3 playerPos = entity.getComponent(TransformComponent.class).pos;

        float radius = MIN_SPAWN_RADIUS + (float) Math.random() * (MAX_SPAWN_RADIUS - MIN_SPAWN_RADIUS);
        Vector2 enemyPos = new Vector2(playerPos.x, playerPos.y).add(new Vector2((float) Math.random(), (float) Math.random()).nor().scl(radius));

        int enemyTypeIndex = random.nextInt(MobFactory.ENMEY_TYPE.values().length);

        MobFactory.spawnMob(MobFactory.ENMEY_TYPE.values()[enemyTypeIndex], gameScreen.getGame(), enemyPos);

        gameScreen.getGame().enemyCounter++;
    }
}
