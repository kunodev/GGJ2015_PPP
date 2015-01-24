package de.panda.tiled;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.WallComponent;

public class MapRenderer {

	public TiledMapRenderer renderer;

	public TiledMap map;

	public OrthographicCamera cam;

	public static class ComponentRegistry {
		Reflections ref = new Reflections("");

		public Map<String, Class<? extends Component>> classNameToClass;

		public ComponentRegistry() {
			Set<Class<? extends Component>> components = this.ref.getSubTypesOf(Component.class);
			this.classNameToClass = new HashMap<String, Class<? extends Component>>();
			for (Class<? extends Component> clazz : components) {
				this.classNameToClass.put(clazz.getSimpleName(), clazz);
			}
		}
	}

	public MapRenderer(String file, OrthographicCamera cam, AssetManager assetMan) {
		this.cam = cam;
		map = assetMan.get(file);
		this.renderer = new OrthogonalTiledMapRenderer(map);
		int width = (Integer) map.getProperties().get("width");
		int tileWidth = (Integer) map.getProperties().get("tilewidth");
		int height = (Integer) map.getProperties().get("height");
		int tileHeight = (Integer) map.getProperties().get("tileheight");
		cam.setToOrtho(false, width * tileWidth, height * tileHeight);
	}

	public void loadComponentsFromMap(Engine engine) {
		ComponentRegistry components = new ComponentRegistry();
		for (MapLayer layer : map.getLayers()) {
			if (layer instanceof TiledMapTileLayer) {
				TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
				for (int i = 0; i < tileLayer.getWidth(); i++) {
					for (int j = 0; j < tileLayer.getHeight(); j++) {
						Entity e = new Entity();
						Cell c = tileLayer.getCell(i, j);
						try {
							Iterator<String> keys = c.getTile().getProperties().getKeys();
							createComponent(components, e, keys);
							engine.addEntity(e);

						} catch (NullPointerException ex) {
							// Shit happens
						}
					}
				}
			}
			for (MapObject obj : layer.getObjects()) {
				if (obj instanceof RectangleMapObject) {
					RectangleMapObject robj = (RectangleMapObject) obj;
					Entity e = new Entity();
					Iterator<String> keys = robj.getProperties().getKeys();
					
					while(keys.hasNext()){
						String prop = keys.next();
						if (prop.equals("wall") ) {							
							WallComponent wallComp = new WallComponent();
							BoundsComponent boundsComp = new BoundsComponent(robj.getRectangle());
							
							e.add(wallComp);
							e.add(boundsComp);
							//System.out.println("wall" + robj.getRectangle().toString());
						}
						
					}
					
					createComponent(components, e, keys);
					engine.addEntity(e);
				}
			}

		}
	}

	private void createComponent(ComponentRegistry components, Entity e, Iterator<String> keys) {
		while (keys.hasNext()) {
			String key = keys.next();
			Class<? extends Component> clazz = components.classNameToClass.get(key);
			try {
				if (clazz != null) {
					e.add(clazz.newInstance());
				}
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
			System.out.println("Something has a property!");
		}
	}

	public void render() {
		cam.update();
		renderer.setView(cam);
		renderer.render();
	}
}
