/**
 * 
 */
package mycontroller;

import java.util.HashMap;
import java.util.Iterator;
import tiles.LavaTrap;
import tiles.MapTile;
import tiles.MapTile.Type;
import utilities.Coordinate;

/**
 * @author lbwde
 *
 */
public class MyMap {

	private static MyMap instance = new MyMap();
	
	
	private HashMap<Coordinate, MapTile> markMap;
	private HashMap<Coordinate, MapTile> map;
	private Coordinate position;
	private HashMap<Integer, Coordinate> keyStorage = new HashMap<Integer, Coordinate>();
	
	
	private MyMap() {}
	
	public static MyMap getInstance() {
		return instance;
	}
	
	public void setOriginalMap(HashMap<Coordinate, MapTile> map, Coordinate position) {
		this.map = map;
		this.position = position;
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
			
				
			if(map.get(coord).isType(MapTile.Type.ROAD) ||map.get(coord).isType(MapTile.Type.FINISH)) {
				if(isPassable(coord)) {
					markMap.put(coord, null);
				}else {
					markMap.put(coord, new MapTile(Type.WALL));
				}
			}
		}
		
		return markMap;
		
	}
	private boolean isPassable(Coordinate destination) {
		// check if the destination is passable from the start
		HashMap<Coordinate, Boolean> traveled = new HashMap<>();
		
		Coordinate end = detectPath(this.position, destination, traveled);
		
		while((end = detectPath(end, destination, traveled)) != null) {
			if(end.equals(destination)) {
				return true;
			}
		}
		return false;
		
	}
	
	private Coordinate detectPath(Coordinate current, Coordinate destination, HashMap<Coordinate, Boolean> traveled){

		Coordinate nextNe;

			for (int i =-1; i<=1; i++) {
				for(int j =-1; j<=1; j++) {
					// should be within map
					nextNe = new Coordinate(current.x+i, current.y+j);
					MapTile tile = this.map.get(nextNe);
					if(tile!=null) {
						if(!tile.isType(Type.WALL) && !traveled.containsKey(nextNe)) {
							traveled.put(nextNe, true);
							return nextNe;
						}
					}
				}
			}
		Iterator<Coordinate> coords = traveled.keySet().iterator();
		while(coords.hasNext()) {
			Coordinate next = coords.next();
			for (int i =-1; i<=1; i++) {
				for(int j =-1; j<=1; j++) {
					// should be within map
					nextNe = new Coordinate(next.x+i, next.y+j);
					MapTile tile = this.map.get(nextNe);
					if(tile!= null) {
						if(!tile.isType(Type.WALL) && !traveled.containsKey(nextNe)) {
							traveled.put(nextNe, true);
							return nextNe;
						}
					}
				}
			}
			
		}
		
			return null;
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
			if (actualTile instanceof LavaTrap) {
				int keyNum = ((LavaTrap)actualTile).getKey();
				if ( keyNum > 0 && !keyStorage.containsKey(keyNum)) {
					keyStorage.put(keyNum, coord);
					System.out.println("Add key: " + ((LavaTrap)actualTile).getKey() + " at " + coord.toString());
				}
			}
			this.map.put(coord, actualTile);
			this.markMap.put(coord, actualTile);
		}
	}
	
	public Coordinate getExit() {
		Iterator<Coordinate> mark = this.map.keySet().iterator();
		Coordinate coord = null;
		while(mark.hasNext()) {
			
			coord = mark.next();
			MapTile tile = map.get(coord);
			if(tile != null) {
				if(tile.getType().equals(MapTile.Type.FINISH)) {
					return coord;
				}
			}
		}
		return coord;
	}
		
	
	public HashMap<Integer, Coordinate>returnKeyStorage() {
			return this.keyStorage;
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