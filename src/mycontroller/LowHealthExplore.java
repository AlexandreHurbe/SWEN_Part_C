package mycontroller;

import java.util.HashMap;
import java.util.Iterator;

import tiles.HealthTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class LowHealthExplore extends ExploreStrategy{
	
	MyMap myMap = MyMap.getInstance();
	Coordinate coord;
	
	
	@Override
	public Coordinate getDestination() {
//		System.out.println("start explore key");
		HashMap<Coordinate, MapTile> markMap = myMap.getMarkMap();
//		Coordinate currentPosition = myMap.getPosition();
//		System.out.println(markMap.toString());
		Coordinate coord;
		Iterator<Coordinate> mark = markMap.keySet().iterator();

		while(mark.hasNext()) {
			coord = mark.next();
//			System.out.println(coord);
//			System.out.println(coord.toString() + "is" + markMap.get(coord).getType().toString());
			if(markMap.get(coord) instanceof HealthTrap) {
				System.out.println("found health at: " + coord.toString());
				return coord;
			}
		}
		
		while(mark.hasNext()) {
			coord = mark.next();
//			System.out.println(coord);
//			System.out.println(coord.toString() + "is" + markMap.get(coord).getType().toString());
			if(markMap.get(coord) == null) {
				System.out.println("found destination (even though health is low) at: " + coord.toString());
				return coord;
			}
		}
		return null;
	}
	
	public int estimateCost(Coordinate start, Coordinate destination) {
		int estimateCost;
		HashMap<Coordinate, MapTile> markMap = myMap.getMarkMap();
		if (markMap.get(start).isType(MapTile.Type.TRAP) || !(markMap.get(start) instanceof HealthTrap)) {
			estimateCost = 100;
		}
		else {
			estimateCost = Math.abs(destination.x - start.x) + Math.abs(destination.y - start.y);
		}
		return estimateCost;
	}
}
