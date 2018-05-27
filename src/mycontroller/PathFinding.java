package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import tiles.MapTile;
import utilities.Coordinate;

public class PathFinding implements IDistance{
	private static final Integer INFINITY = Integer.MAX_VALUE;
	MyMap myMap = MyMap.getInstance();
	IMoveStrategy strategy;
	private MyStrategyFactory factory = new MyStrategyFactory();
	private Coordinate destination;
	private  int lowHealth = 66;

	
	public PathFinding(MyAIController controller) throws InstantiationException, IllegalAccessException, ClassNotFoundException {


		this.strategy = chooseStrategy(controller);
		myMap.update(new Coordinate(controller.getPosition()), controller.getView());
		if(this.strategy instanceof CollectKeyStrategy) {
			this.destination = ((CollectKeyStrategy)this.strategy).getDestination(controller.getKey());
			if(myMap.getMap().get(this.destination).isType(MapTile.Type.FINISH)) {
				System.out.println("------------------------------CHANGE LOWHEALTH VALUE----------------------");
				this.lowHealth = 20;
			}
		}else {
			this.destination = this.strategy.getDestination();
		}
		
		System.out.println("Dest in Pathfinding: " + this.destination.toString());
		
	}
	
	private IMoveStrategy chooseStrategy(MyAIController controller) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (controller.getHealth() == 100) {
			controller.needHealing = false;
		}
		if (controller.getHealth() < lowHealth) {
			controller.needHealing = true;
		}
		
		if (controller.needHealing) {
			System.out.println("LowHealthStrategy");
			return factory.getMoveStrategy("LowHealthExplore");
		}
		else if (myMap.returnKeyStorage() != null && controller.getKeysToCollect() - myMap.returnKeyStorage().size() == 1) {
			System.out.println("CollectKeyStrategy");
			return factory.getMoveStrategy("CollectKeyStrategy");
		}
		else {
			System.out.println("ExploreStrategy");
			return factory.getMoveStrategy("ExploreStrategy");
		}
	}
	// use A* to find path to destination
	public HashMap<Coordinate, Float> findPath() {
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
		fScore.put(start, strategy.distance(start, destination));
		
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
			if (neighbors != null) {
				for(Coordinate neighbor: neighbors) {
					if(closedSet.contains(neighbor)) {
						continue;
					}
					// discover a new node
					if(!openSet.contains(neighbor)) {
						openSet.add(neighbor);
					}
					int combinedGscore = gScore.get(current) + strategy.estimateCost(current, neighbor);

					if(combinedGscore >= gScore.get(neighbor)) {
						continue;
					}
					cameFrom.put(neighbor, current);
					gScore.put(neighbor, combinedGscore);
					fScore.put(neighbor, gScore.get(neighbor) + strategy.distance(neighbor, destination));
				}
			}
			else {
				System.out.println("System cought");
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

			if(!tile.isType(MapTile.Type.WALL)) {
				mapping.put(coord, null);
			}
			
		}
		return mapping;
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
					try {
						if(!tile.isType(MapTile.Type.WALL)) {
							neighbors.add(coord);
						}
					}
					catch (NullPointerException e) {
						return null;
					}
					}
				}
			}
		return neighbors;
	}
	// actual distance between 2 coordinates
	@Override
	public int distance(Coordinate from, Coordinate to) {
		int distance = Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
		return distance;
	}
	
	// construct the route to destination using this data structure
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

		// Formulate Path to be used by move
		HashMap<Coordinate, Float> myPath = fomulatePath(totalPath);
		return myPath;
	}
	
	private HashMap<Coordinate, Float> fomulatePath(Stack<Coordinate> path) {
		HashMap<Coordinate, Float> myPath = new HashMap<>();
		// iterate through the stack and get directions
		Coordinate currentCoord = path.pop();
		while(!path.isEmpty()) {
			Coordinate nextCoord = path.pop();
			// get direction of current Coord
			myPath.put(currentCoord, getAngle(currentCoord, nextCoord));
			currentCoord = nextCoord;
		}
		

		return myPath;
	}
	
	private float getAngle(Coordinate current, Coordinate next) {
		int difX = next.x - current.x;
		int difY = next.y - current.y;
		float angle;
		angle =  (float) Math.toDegrees(Math.atan2(difY, difX));
		// prevent negative angles 	
		if(angle < 0) {
			angle += 360;
		}

		return angle;
	}
	
	
	
}

