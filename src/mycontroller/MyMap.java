/**
 * 
 */
package mycontroller;

import java.util.HashMap;
import java.util.Iterator;

import tiles.MapTile;
import utilities.Coordinate;

/**
 * @author lbwde
 *
 */
public class MyMap {

	/**
	 * 
	 */
	private static MyMap instance = new MyMap();
	
	
	private HashMap<Coordinate, MapTile> markMap;
	private HashMap<Coordinate, MapTile> map;
	private Coordinate position;
	
	
	private MyMap() {
		
	}
	
	public static MyMap getInstance() {
		return instance;
	}
	
	public void setOriginalMap(HashMap<Coordinate, MapTile> map) {
		this.map = map;
	}
	
	public void update(Coordinate position, HashMap<Coordinate, MapTile> view) {
		this.position = position;
		updateMap(view);
		
	}
	private void updateMap(HashMap<Coordinate, MapTile> view) {
		Iterator<Coordinate> viewCoords = view.keySet().iterator();
		Coordinate coord;
		MapTile actualTile;
		while(viewCoords.hasNext()) {
			coord = viewCoords.next();
			actualTile = view.get(coord);
			this.map.put(coord, actualTile);
			this.markMap.put(coord, actualTile);
		}
	}
}
