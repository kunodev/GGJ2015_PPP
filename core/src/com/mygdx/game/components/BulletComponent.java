package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;

public class BulletComponent extends Component {
    public static final int STATE_MOVE = 0;
    public static final int STATE_HIT = 1;
    public static final float MOVE_VELOCITY = 10;
    public static final float WIDTH = 0.8f;
    public static final float HEIGHT = 0.8f;
}