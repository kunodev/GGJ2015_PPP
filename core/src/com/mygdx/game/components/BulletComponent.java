package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

public class BulletComponent extends Component {
    public static final int STATE_MOVE = 0;
    public static final int STATE_HIT = 1;
    public static final float MOVE_VELOCITY = 500;
    public Vector3 targetVec;
    public static final float WIDTH = 0.3f;
    public static final float HEIGHT = 0.3f;
}