package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class BossComponent extends Component {
    public static final int STATE_MOVE = 0;
    public static final int STATE_WAIT = 1;
    public static final int STATE_SHOOT = 2;
    public static final int STATE_SHOOTED = 3;
    public static final float MOVE_VELOCITY = 10;
    public static final float WIDTH = 5f;
    public static final float HEIGHT = 5f;
}
