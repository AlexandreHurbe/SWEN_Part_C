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
		this.markMap = initMarkMap(map);
	}
	// initialize markMap
	private HashMap<Coordinate, MapTile> initMarkMap(HashMap<Coordinate, MapTile> map) {
		@SuppressWarnings("unchecked")
		HashMap<Coordinate, MapTile> markMap = (HashMap<Coordinate, MapTile>) map.clone();
		Iterator<Coordinate> allCoords = map.keySet().iterator();

		Coordinate coord;
		while(allCoords.hasNext()) {
			coord = allCoords.next();
			if(map.get(coord).isType(MapTile.Type.ROAD)) {
				markMap.put(coord, null);
			}
		}
		
		return markMap;
		
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
	
	public Coordinate getPosition() {
		return this.position;
	}
	
	public HashMap<Coordinate, MapTile> getMap() {
		return this.map;
	}
	
	public HashMap<Coordinate, MapTile> getMarkMap() {
		return this.markMap;
	}
}
