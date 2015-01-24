package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class BossComponent extends Component {
    public static final int STATE_MOVE = 0;
    public static final int STATE_SHOOT = 1;
    public static final float MOVE_VELOCITY = 10;
    public static final float WIDTH = 20f;
    public static final float HEIGHT = 20f;
}
