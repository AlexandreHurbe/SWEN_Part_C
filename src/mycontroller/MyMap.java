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
	// singleton
	public static MyMap getInstance() {
		return instance;
	}
	// initialize everything
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
				// initialize all road or finish to null
				// when it is possible to get to there
				if(isPassable(coord)) {
					markMap.put(coord, null);
				// not possible to get to there
				}else {
					// give a wall type tile to that coordinate
					// so will not try to find destination to here
					markMap.put(coord, new MapTile(Type.WALL));
				}
			}
		}
		
		return markMap;
		
	}
	// check if the destination is passable 
	private boolean isPassable(Coordinate destination) {
		// where we have traveled
		HashMap<Coordinate, Boolean> traveled = new HashMap<>();
		
		Coordinate end = detectPath(this.position, destination, traveled);
		// when this destination is not passable it will return null
		while((end = detectPath(end, destination, traveled)) != null) {
			// find a coordinate and it is the destination,
			// then it is passable
			if(end.equals(destination)) {
				return true;
			}
		}
		return false;
		
	}
	
	private Coordinate detectPath(Coordinate current, Coordinate destination, HashMap<Coordinate, Boolean> traveled){

		// give traveled an initial set
		Coordinate nextNe;
			// check surroundings
			for (int i =-1; i<=1; i++) {
				for(int j =-1; j<=1; j++) {
					// should be within map
					nextNe = new Coordinate(current.x+i, current.y+j);
					MapTile tile = this.map.get(nextNe);
					// check within map
					if(tile!=null) {
						// check if able to pass and not being traveled before
						if(!tile.isType(Type.WALL) && !traveled.containsKey(nextNe)) {
							traveled.put(nextNe, true);
							return nextNe;
						}
					}
				}
			}
			
		// now search for rest of the map
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
		// found all the map and nothing
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
		// update view into map and mark map
		// also keys into key storage
		while(viewCoords.hasNext()) {
			coord = viewCoords.next();
			actualTile = view.get(coord);
			if (actualTile instanceof LavaTrap) {
				int keyNum = ((LavaTrap)actualTile).getKey();
				if ( keyNum > 0 && !keyStorage.containsKey(keyNum)) {
					keyStorage.put(keyNum, coord);
				}
			}
			this.map.put(coord, actualTile);
			this.markMap.put(coord, actualTile);
		}
	}
	// find the exit
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