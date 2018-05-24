package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;


import tiles.MapTile;
import utilities.Coordinate;

public class PathFinding {
	private static final Integer INFINITY = Integer.MAX_VALUE;
	MyMap myMap = MyMap.getInstance();
	
	public PathFinding(HashMap<Coordinate, MapTile> map) {
		// TODO Auto-generated constructor stub
		myMap.setOriginalMap(map);
	}
	// use A* to find path to destination
	public HashMap<Coordinate, Float> findPath(Coordinate destination) {
		Coordinate start = myMap.getPosition();
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

		Iterator<Coordinate> mapTiles = myMap.getMap().keySet().iterator();
		HashMap<Coordinate, Integer> score = new HashMap<>();
		MapTile tile;
		Coordinate coord;
		while(mapTiles.hasNext()) {
			coord = mapTiles.next();
			tile = myMap.getMap().get(coord);
			if(!tile.isType(MapTile.Type.WALL)) {
				score.put(coord, INFINITY);
			}
		}
		
		return score;
	}
	
	// help to initialize the mapping of final route
	private HashMap<Coordinate, Coordinate> initMap() {
		HashMap<Coordinate, Coordinate> mapping = new HashMap<>();
		Iterator<Coordinate> mapTiles = myMap.getMap().keySet().iterator();
		MapTile tile;
		Coordinate coord;
		while(mapTiles.hasNext()) {
			coord = mapTiles.next();
			tile = myMap.getMap().get(coord);
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
					MapTile tile = myMap.getMap().get(coord);
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
	private HashMap<Coordinate, Float> reconstructPath(HashMap<Coordinate, Coordinate> mapping, Coordinate current){
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
		HashMap<Coordinate, Float> myPath;
		Path path = new Path();
		path.updatePath(totalPath);
		myPath = path.getPath();
		return myPath;
	}
}
