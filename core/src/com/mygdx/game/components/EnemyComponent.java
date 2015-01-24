package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;

public class EnemyComponent extends Component {

	public static final int STATE_RUNNING_AROUND = 1;
	public static final int DO_NOTHING_TACTICS = 0;

	public int whatDo;

}
