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
	private HashMap<Coordinate, MapTile> map;
	private Coordinate currentPosition;
	private final int INFINITY = Integer.MAX_VALUE;
	public View(HashMap<Coordinate, MapTile> map) {
		this.map = map;
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
	
	public void update(HashMap<Coordinate, MapTile> currentView, Coordinate myPosition) {
		this.currentView = currentView;
		this.currentPosition = myPosition;
	}
	// use A* to find path to destination
	public Stack<Coordinate> findPath(Coordinate destination) {
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
	

	private HashMap<Coordinate, Coordinate> initMap() {
		HashMap<Coordinate, Coordinate> mapping = new HashMap<>();
		Iterator<Coordinate> mapTiles = this.map.keySet().iterator();
		MapTile tile;
		Coordinate coord;
		while(mapTiles.hasNext()) {
			coord = mapTiles.next();
			tile = this.map.get(coord);
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
	
	private List<Coordinate> findNeighbor(Coordinate current){
		List<Coordinate> neighbors = new ArrayList<>();
		for (int i =-1; i<=1; i++) {
			for(int j =-1; j<=1; j++) {
				Coordinate coord = new Coordinate(current.x+i, current.y+j);
				MapTile tile = this.map.get(coord);
				if(!tile.isType(MapTile.Type.WALL) && !coord.equals(current)) {
					neighbors.add(coord);
				}
			}
		}
		return neighbors;
	}
	// actual distance between 2 coordinates
	private int distance(Coordinate from, Coordinate to) {
		int distance = Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
		return distance;
	}
	
	private Stack<Coordinate> reconstructPath(HashMap<Coordinate, Coordinate> mapping, Coordinate current){
		Stack<Coordinate> totalPath = new Stack<>();
		totalPath.push(current);
		while(mapping.containsKey(current)) {
			current = mapping.get(current);
			totalPath.push(current);
		}
		return totalPath;
	}
}
