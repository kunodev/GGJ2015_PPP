package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;

public class BossComponent extends Component {
	public static final int STATE_MOVE = 0;
	public static final int STATE_WAIT = 1;
	public static final int STATE_SHOOT = 2;
	public static final int STATE_JUMP = 3;
	public static final int STATE_ACTIONED = 4;
	public static final float MOVE_VELOCITY = 200;
	public static final float WIDTH = 100f;
	public static final float HEIGHT = 100f;
	public static final float MOVEMENT_DURATION = 3.0f;
	public static final float WAIT_DURATION = 1.5f;
	public static final float COOLDOWN = 1.0f;
}
