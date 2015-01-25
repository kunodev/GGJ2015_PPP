package com.mygdx.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.mygdx.game.GameScreen;
import com.mygdx.game.components.PlayerComponent;

public class SpawnEnemySystem extends IntervalIteratingSystem {
    final GameScreen gameScreen;

    public SpawnEnemySystem(GameScreen gameScreen, float interval) {
        super(Family.all(PlayerComponent.class).get(), interval);
        this.gameScreen = gameScreen;
    }

    @Override
    protected void updateInterval() {
        if (gameScreen.getState() == GameScreen.GAME_RUNNING) {
            super.updateInterval();
        }
    }

    @Override
    protected void processEntity(Entity entity) {
        System.out.println("SPAWN NEW ENEMY");
    }
}
