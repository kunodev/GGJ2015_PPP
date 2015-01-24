package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.World;
import com.mygdx.game.components.*;

public class BossSystem extends IteratingSystem {
    private static final Family family = Family.all(BossComponent.class, StateComponent.class,
            TransformComponent.class,
            MovementComponent.class).get();

    private World world;

    private Float time = 0.0f;

    private ComponentMapper<BossComponent> bm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<PlayerComponent> pm;


    public BossSystem(World world) {
        super(family);

        this.world = world;

        bm = ComponentMapper.getFor(BossComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        this.time = this.time + deltaTime;

        TransformComponent t = tm.get(entity);
        StateComponent state = sm.get(entity);
        MovementComponent mov = mm.get(entity);
        BossComponent boss = bm.get(entity);
        Entity player = world.bob;
        TransformComponent playerPos = tm.get(player);

        if (state.get() == BossComponent.STATE_MOVE) {
            Vector3 targetVec = playerPos.pos.cpy().sub(t.pos).nor().scl(BossComponent.MOVE_VELOCITY);
            mov.velocity.set(targetVec.x, targetVec.y);
            if(this.time >= 3.0f) {
                this.time -= 3.0f;
                state.set(BossComponent.STATE_WAIT);
            }
        }

        if (state.get() == BossComponent.STATE_WAIT) {
            mov.velocity.set(0, 0);

            if(this.time >= 1.5f) {
                this.time -= 1.5f;
                state.set(BossComponent.STATE_SHOOT);
            }
        }

        if (state.get() == BossComponent.STATE_SHOOT) {
            System.out.println("peng");
            world.createBullet();
            state.set(BossComponent.STATE_SHOOTED);
        }

        if (state.get() == BossComponent.STATE_SHOOTED) {
            if(this.time >= 1.0f) {
                this.time -= 1.0f;
                state.set(BossComponent.STATE_MOVE);
            }
        }
    }
}
