package mycontroller;

import java.util.HashMap;
import java.util.Iterator;

import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import tiles.MapTile.Type;
import utilities.Coordinate;

public class LowHealthExplore extends ExploreStrategy{
	
	MyMap myMap = MyMap.getInstance();
	HandlerLibrary handlers = HandlerLibrary.getInstance();
	@Override
	public int distance(Coordinate start, Coordinate end) {
		int lava = 10;
		int health = -5;
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
//	private int surroundWall(Coordinate end) {
//		int wall = 0;
//		Coordinate current;
//		for(int i = -1; i < 1; i++) {
//			for (int j = -1; j < 1 ; j++) {
//				current = new Coordinate(end.x+i, end.y+j);
//				if(myMap.getMap().get(current) != null) {
//					if(myMap.getMap().get(current).isType(Type.WALL)) {
//						wall +=1;
//					}
//				}
//			}
//		}
//		return wall;
//	}

	@SuppressWarnings("null")
	@Override
	public Coordinate getDestination() {
//		System.out.println("start explore key");
		HashMap<Coordinate, MapTile> markMap = myMap.getMarkMap();
//		Coordinate currentPosition = myMap.getPosition();
//		System.out.println(markMap.toString());
		Coordinate coord, minCoord, alterCoord;
		coord = minCoord = alterCoord = null;
		int minDistance = Integer.MAX_VALUE;
		Iterator<Coordinate> mark = markMap.keySet().iterator();

		while(mark.hasNext()) {
			
			coord = mark.next();
			//System.out.println("Found health");
	//			System.out.println(coord.toString() + "is" + markMap.get(coord).getType().toString());
			if(markMap.get(coord) instanceof HealthTrap) {
				int currentDistance = estimateCost(myMap.getPosition(), coord);
				if (currentDistance < minDistance){
					minDistance = currentDistance;
					minCoord = coord;
				}
				//System.out.println("Found health at: " + coord.toString());
			}
			if(markMap.get(coord) == null) {
				alterCoord = coord;
			}
		}
		
		if(minCoord != null) {
			System.out.println("Found dest minCoord in LowHealth: " + minCoord.toString());
			return minCoord;
		}
		else {
			System.out.println("Found dest alterCoord in LowHealth: " + alterCoord.toString());
			return alterCoord;
		}
	}
	@Override
	public int estimateCost(Coordinate start, Coordinate end) {
		int estimateCost;
		estimateCost = Math.abs(end.x - start.x) + Math.abs(end.y - start.y);
		return estimateCost;
		
		
	}
}
