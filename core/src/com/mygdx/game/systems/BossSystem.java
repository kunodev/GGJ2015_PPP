package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.World;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.BossComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TransformComponent;

public class BossSystem extends IteratingSystem {
    private static final Family family = Family.getFor(BossComponent.class,
            StateComponent.class,
            TransformComponent.class,
            MovementComponent.class);

    private World world;

    private ComponentMapper<BossComponent> bm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;

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
        TransformComponent t = tm.get(entity);
        StateComponent state = sm.get(entity);
        MovementComponent mov = mm.get(entity);
        BossComponent boss = bm.get(entity);

        if (state.get() == BossComponent.STATE_MOVE) {

        }

        if (state.get() == BossComponent.STATE_SHOOT) {

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
