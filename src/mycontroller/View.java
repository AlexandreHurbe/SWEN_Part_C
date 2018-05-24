package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

/* 
 * Get the view of tiles around my car
 *  
 */
public class View  {
	private HashMap<Coordinate, MapTile> currentView;
	
	// this is the map to mark where the car has detected
	private HashMap<Coordinate, MapTile> markMap;
	private HashMap<Coordinate, MapTile> map;
	private Coordinate currentPosition;
	private float angle;
	private final int INFINITY = Integer.MAX_VALUE;
	private final int DETECTLENMIN = -4;
	private final int DETECTLENMAX = 4;
	public View(HashMap<Coordinate, MapTile> map) {
		this.map = map;
		this.markMap = initMarkMap(map);
	}
	
	public void lookForKey(List<Key> keys){
		Iterator<Coordinate> allTiles = this.currentView.keySet().iterator();
	
		MapTile tile;
		Coordinate coord;
		int keyNumber;
		
		while(allTiles.hasNext()) {
			coord = allTiles.next();
			tile = this.currentView.get(coord);
			if(tile instanceof LavaTrap) {
				keyNumber = ((LavaTrap) tile).getKey();
				if(keyNumber > 0) {
					Key key = new Key();
					key.setKey(keyNumber, coord);
					keys.add(key);
				}
			}
		}
	}
	
	public void update(HashMap<Coordinate, MapTile> currentView, Coordinate myPosition, Float angle) {
		this.currentView = currentView;
		this.currentPosition = myPosition;
		this.angle = angle;
		updateMap();
//		System.out.println("update mark map success");
	}
	
	public Coordinate getPosition() {
		return currentPosition;
	}
	public float getAngle() {
		return angle;
	}
	public HashMap<Coordinate, MapTile> getMarkMap() {
		return this.markMap;
	}
	public HashMap<Coordinate, MapTile> getMap() {
		return this.map;
	}
	private void updateMap() {
		Iterator<Coordinate> viewCoords = currentView.keySet().iterator();
		Coordinate coord;
		MapTile actualTile;
		while(viewCoords.hasNext()) {
			coord = viewCoords.next();
			actualTile = currentView.get(coord);
			this.map.put(coord, actualTile);
			this.markMap.put(coord, actualTile);
		}
	}
	// set all road to null
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
	
	// use A* to find path to destination
	public HashMap<Coordinate, MyDirection.Direction> findPath(Coordinate destination) {
		Coordinate start = currentPosition;
		// tiles already been evaluated
		List<Coordinate> closedSet = new ArrayList<>();
		// the current discovered tiles
		// only the start node currently
		List<Coordinate> openSet = new ArrayList<>();
		openSet.add(start);
		//
		HashMap<Coordinate, Coordinate> cameFrom = initMap();
		// for each node, the cost of getting to the destination
		// initialize to infinity
		HashMap<Coordinate, Integer> gScore = initScore();
		// the start is 0
		gScore.put(start, 0);
		// for each node, the cost from the start to destination 
		// passing by this node
		HashMap<Coordinate, Integer> fScore = initScore();
		// calculate the estimateCost for start point to goal
		fScore.put(start, estimateCost(start, destination));
		
		//
		while(!openSet.isEmpty()) {
			Coordinate current = lowestCost(openSet, gScore);
			if(current.equals(destination)) {
				// construct the path
				return reconstructPath(cameFrom, current);
			}
			
			openSet.remove(current);
			closedSet.add(current);
			
			List<Coordinate> neighbors = findNeighbor(current);
			for(Coordinate neighbor: neighbors) {
				if(closedSet.contains(neighbor)) {
					continue;
				}
				// discover a new node
				if(!openSet.contains(neighbor)) {
					openSet.add(neighbor);
				}
				int combinedGscore = gScore.get(current) + distance(current, neighbor);

				if(combinedGscore >= gScore.get(neighbor)) {
					continue;
				}
				cameFrom.put(neighbor, current);
				gScore.put(neighbor, combinedGscore);
				fScore.put(neighbor, gScore.get(neighbor) + estimateCost(neighbor, destination));
			}
		}
		return null;
	} 	
	// help to initialize the Score set in A* search
	private HashMap<Coordinate, Integer> initScore() {

		Iterator<Coordinate> mapTiles = this.map.keySet().iterator();
		HashMap<Coordinate, Integer> score = new HashMap<>();
		MapTile tile;
		Coordinate coord;
		while(mapTiles.hasNext()) {
			coord = mapTiles.next();
			tile = this.map.get(coord);
			if(!tile.isType(MapTile.Type.WALL)) {
				score.put(coord, INFINITY);
			}
		}
		
		return score;
	}
	
	// help to initialize the mapping of final route
	private HashMap<Coordinate, Coordinate> initMap() {
		HashMap<Coordinate, Coordinate> mapping = new HashMap<>();
		Iterator<Coordinate> mapTiles = this.map.keySet().iterator();
		MapTile tile;
		Coordinate coord;
		while(mapTiles.hasNext()) {
			coord = mapTiles.next();
			tile = this.map.get(coord);
//			System.out.println(((MapTile)tile).getType().toString());
			if(!tile.isType(MapTile.Type.WALL)) {
				mapping.put(coord, null);
			}
			
		}
		return mapping;
	}
	// basic heuristic function for 2 coordinates
	private int estimateCost(Coordinate start, Coordinate destination) {
		int estimateCost = Math.abs(destination.x - start.x) + Math.abs(destination.y - start.y);
		return estimateCost;
	}
	// find the lowest costed neighbor node
	private Coordinate lowestCost(List<Coordinate> openSet, HashMap<Coordinate, Integer> score) {
		Iterator<Coordinate> setTiles = openSet.iterator();
		int minCost = INFINITY;
		int tileScore;
		Coordinate currentPos, finalPos = null;
		while(setTiles.hasNext()) {
			currentPos = setTiles.next();
			tileScore = score.get(currentPos);
			if(tileScore <= minCost) {
				finalPos = currentPos;
				minCost = tileScore;
			}
		}
		return finalPos;
	}
	// find neighbors of current coordinates
	private List<Coordinate> findNeighbor(Coordinate current){
		List<Coordinate> neighbors = new ArrayList<>();
		for (int i =-1; i<=1; i++) {
			for(int j =-1; j<=1; j++) {
				if(Math.abs(i)!=Math.abs(j)) {
					Coordinate coord = new Coordinate(current.x+i, current.y+j);
					MapTile tile = this.map.get(coord);
					if(!tile.isType(MapTile.Type.WALL) && !coord.equals(current)) {
						neighbors.add(coord);
					}
				}
			}
				
		}
		return neighbors;
	}
	// actual distance between 2 coordinates
	public int distance(Coordinate from, Coordinate to) {
		int distance = Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
		return distance;
	}
	
	// construct the rout to destination using this data structure
	private HashMap<Coordinate, MyDirection.Direction> reconstructPath(HashMap<Coordinate, Coordinate> mapping, Coordinate current){
		Stack<Coordinate> totalPath = new Stack<>();
		totalPath.push(current);
		while(mapping.containsKey(current)) {
			current = mapping.get(current);
			// fix starting at null
			if(current != null) {
				totalPath.push(current);
			}
		}

//		System.out.println(totalPath.toString());
		// Formulate Path to be used by move
		HashMap<Coordinate, MyDirection.Direction> myPath;
		Path path = new Path();
		path.updatePath(totalPath);
		myPath = path.getPath();
		return myPath;
	}
}
