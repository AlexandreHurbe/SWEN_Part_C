package mycontroller;

import java.util.HashMap;
import java.util.Iterator;

import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class ExploreStrategy implements IMoveStrategy {
	MyMap myMap = MyMap.getInstance();
	HandlerLibrary handlers = HandlerLibrary.getInstance();
	
	@Override
	public Coordinate getDestination() {

		HashMap<Coordinate, MapTile> markMap = myMap.getMarkMap();

		Coordinate coord = null, minCoord = null;
		Iterator<Coordinate> mark = markMap.keySet().iterator();

		int minDist = Integer.MAX_VALUE;
		if(!reachedDest()) {
			minCoord = myMap.getExit();
		}else {
			while(mark.hasNext()) {
				
				coord = mark.next();
	
				if(markMap.get(coord) == null) {
					int distance = estimateCost(myMap.getPosition(), coord);
					if(distance < minDist) {
						minCoord = coord;
						minDist = distance;
					}
	
				}
			}
		}
		if(minCoord == null) {
			minCoord = myMap.getExit();
		}
		System.out.println("Found destination in exploreStrategy: " + minCoord.toString());
		return minCoord;
		

	}
	private boolean reachedDest() {
		HashMap<Coordinate, MapTile> markMap = myMap.getMarkMap();
		Iterator<Coordinate> mark = markMap.keySet().iterator();
		Coordinate coord;
		while(mark.hasNext()) {
			
			coord = mark.next();
			MapTile tile = markMap.get(coord);
			if(tile != null) {
				if(tile.getType().equals(MapTile.Type.FINISH)) {
					return true;
				}
			}
		
		}
		return false;
	}
	
	
	@Override
	public int estimateCost(Coordinate start, Coordinate end) {
		// TODO Auto-generated method stub
		// make health smaller and 
//		System.out.println(start.toString()+ en);
		int estimateCost = Math.abs(end.x - start.x) + Math.abs(end.y - start.y);
		
		return estimateCost;

	}

	@Override
	public int distance(Coordinate start, Coordinate end) {
		// TODO Auto-generated method stub
		int lava = 30;
		int health = 0;
		int cost = estimateCost(start, end);
		if (myMap.getMap().get(end) instanceof LavaTrap) {
			cost += lava;
		}
		if (myMap.getMap().get(end) instanceof HealthTrap) {
			cost +=health;
		}
		Handler wallHandler = handlers.getHandler("wallHandler");
		return cost + ((WallHandler)wallHandler).handleMapTile(end);
	}
	
	
}
