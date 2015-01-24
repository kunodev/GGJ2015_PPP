package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class HealthComponent extends Component {
    public static interface AttackListener {
        public void attack(Entity enemy, int healthLeft);
    }

    public int health = 1000;

    public AttackListener attackListener;

    public HealthComponent() {
    }

    public HealthComponent(int health) {
        this.health = health;
    }
}

