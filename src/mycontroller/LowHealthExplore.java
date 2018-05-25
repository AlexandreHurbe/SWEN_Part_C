package mycontroller;

import java.util.HashMap;
import java.util.Iterator;

import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class LowHealthExplore extends ExploreStrategy{
	
	MyMap myMap = MyMap.getInstance();
	
	@Override
	public int distance(Coordinate start, Coordinate end) {
		int lava = 5;
		int health = -2;
		int cost = estimateCost(start, end);
		if (myMap.getMap().get(end) instanceof LavaTrap) {
			cost += lava;
		}
		if (myMap.getMap().get(end) instanceof HealthTrap) {
			cost +=health;
		}
		
		return cost;
	}

	@SuppressWarnings("null")
	@Override
	public Coordinate getDestination() {
//		System.out.println("start explore key");
		HashMap<Coordinate, MapTile> markMap = myMap.getMarkMap();
//		Coordinate currentPosition = myMap.getPosition();
//		System.out.println(markMap.toString());
		Coordinate coord, minCoord = null, alterCoord = null;
		int minDistance = Integer.MAX_VALUE;
		Iterator<Coordinate> mark = markMap.keySet().iterator();

		while(mark.hasNext()) {
			
			coord = mark.next();
			if(coord != null) {
			//System.out.println("Found health");
	//			System.out.println(coord.toString() + "is" + markMap.get(coord).getType().toString());
				if(markMap.get(coord) instanceof HealthTrap) {
					int currentDistance = estimateCost(myMap.getPosition(), coord);
					if (currentDistance < minDistance){
						minDistance = currentDistance;
						minCoord = coord;
					}
					System.out.println("found health at: " + coord.toString());
	
				}
			} else {
				alterCoord = coord;
			}
			

		}
		if(minCoord != null) {
			return minCoord;
		}
		else {
			return alterCoord;
		}
	}
	
	@Override
	public int estimateCost(Coordinate start, Coordinate destination) {
		int estimateCost;

		estimateCost = Math.abs(destination.x - start.x) + Math.abs(destination.y - start.y);
		return estimateCost;
		
		
	}
}
