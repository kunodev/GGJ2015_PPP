package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.World;
import com.mygdx.game.components.*;

public class BulletSystem extends IteratingSystem {
    private static final Family family = Family.getFor(BulletComponent.class,
            StateComponent.class,
            TransformComponent.class,
            MovementComponent.class);

    private float accelX = 0.0f;
    private World world;

    private ComponentMapper<BulletComponent> bm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;

    public BulletSystem(World world) {
        super(family);

        this.world = world;

        bm = ComponentMapper.getFor(BulletComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    public void setAccelX(float accelX) {
        this.accelX = accelX;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        accelX = 0.0f;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent t = tm.get(entity);
        StateComponent state = sm.get(entity);
        MovementComponent mov = mm.get(entity);
        BulletComponent bullet = bm.get(entity);

        if (state.get() == BulletComponent.STATE_MOVE) {

        }

        if (state.get() == BulletComponent.STATE_HIT) {

        }

        if (t.pos.x < 0) {
            t.pos.x = World.WORLD_WIDTH;
        }

        if (t.pos.x > World.WORLD_WIDTH) {
            t.pos.x = 0;
        }

        t.scale.x = mov.velocity.x < 0.0f ? Math.abs(t.scale.x) * -1.0f : Math.abs(t.scale.x);
    }
}
