package de.panda.tiled;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.World;
import com.mygdx.game.components.AnimationComponent;
import com.mygdx.game.components.EnemyComponent;
import com.mygdx.game.components.HealthComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;

public class MobFactory {

	public static void spawnMob(String val, World w, Vector2 pos) {
		Entity e = new Entity();
		TextureComponent txc = new TextureComponent();
		AnimationComponent ac = new AnimationComponent();
		EnemyComponent ec = new EnemyComponent();
		MovementComponent mc = new MovementComponent();
		TransformComponent tc = new TransformComponent();
		HealthComponent hc = new HealthComponent();
		tc.pos.set(pos.x, pos.y, 0);

		switch (val) {
		case "rat":
			Texture rat = w.game.assetManager.get("Living/buthead_ratte_gegner_animsheet.png");
			ac.animations.put(EnemyComponent.STATE_RUNNING_AROUND, new Animation(0.5f, w.doTextureMagic(rat, 2)));
			break;
		case "monster1":
			Texture mon = w.game.assetManager.get("Living/buthead_gegner1_animsheet.png");
			ac.animations.put(EnemyComponent.STATE_RUNNING_AROUND, new Animation(0.75f, w.doTextureMagic(mon, 3)));
			break;
		}
		e.add(txc);
		e.add(ac);
		e.add(ec);
		e.add(mc);
		e.add(tc);
		e.add(hc);
	}
}
