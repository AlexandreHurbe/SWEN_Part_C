package mycontroller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import tiles.HealthTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class LowHealthExplore extends ExploreStrategy{
	
<<<<<<< HEAD
//	public Coordinate explore(View currentView) {
////		System.out.println("start explore key");
//		HashMap<Coordinate, MapTile> markMap = currentView.getMarkMap();
//		Coordinate currentPosition = currentView.getPosition();
////		System.out.println(markMap.toString());
//		Coordinate coord;
//		Iterator<Coordinate> mark = markMap.keySet().iterator();
////		System.out.println(markMap.keySet());
//
//		while(mark.hasNext()) {
//			
//			coord = mark.next();
////			System.out.println(coord);
////			System.out.println(coord.toString() + "is" + markMap.get(coord).getType().toString());
//			if(markMap.get(coord) instanceof HealthTrap) {
//				System.out.println("found health at: " + coord.toString());
//				return coord;
//			}
//		}
//		while(mark.hasNext()) {
//			coord = mark.next();
////			System.out.println(coord);
////			System.out.println(coord.toString() + "is" + markMap.get(coord).getType().toString());
//			if(markMap.get(coord) == null) {
//				System.out.println("found destination (even though health is low) at: " + coord.toString());
//				return coord;
//			}
//		}
//		return null;
//	}
=======
	private HashMap<Coordinate, MapTile> markMap;
	
	public Coordinate explore(View currentView) {
//		System.out.println("start explore key");
		markMap = currentView.getMarkMap();
		Coordinate currentPosition = currentView.getPosition();
//		System.out.println(markMap.toString());
		Coordinate coord;
		Iterator<Coordinate> mark = markMap.keySet().iterator();
//		System.out.println(markMap.keySet());

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
>>>>>>> 3c607906764576f251258545b2b9de2493fce9f1
	
	public int estimateCost(Coordinate start, Coordinate destination) {
		int estimateCost;
		if (markMap.get(start).isType(MapTile.Type.TRAP) || !(markMap.get(start) instanceof HealthTrap)) {
			estimateCost = 100;
		}
		else {
			estimateCost = Math.abs(destination.x - start.x) + Math.abs(destination.y - start.y);
		}
		return estimateCost;
	}
}
