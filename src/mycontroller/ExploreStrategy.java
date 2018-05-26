package mycontroller;

import java.util.HashMap;
import java.util.Iterator;

import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class ExploreStrategy implements IMoveStrategy {
	MyMap myMap = MyMap.getInstance();
	Coordinate coord;
	
//	public abstract void explore(View currentView, Float health);
	@Override
	public Coordinate getDestination() {
//		System.out.println("start explore key");
		HashMap<Coordinate, MapTile> markMap = myMap.getMarkMap();
//		Coordinate currentPosition = myMap.getPosition();
//		System.out.println(markMap.toString());
		Coordinate coord = null, minCoord = null;
		Iterator<Coordinate> mark = markMap.keySet().iterator();
//		System.out.println(markMap.keySet());
		int minDist = Integer.MAX_VALUE;
		if(!reachedDest()) {
			minCoord = findDest();
		}else {
			while(mark.hasNext()) {
				
				coord = mark.next();
				
	//			System.out.println(coord);
	//			System.out.println(coord.toString() + "is" + markMap.get(coord).getType().toString());
				if(markMap.get(coord) == null) {
					this.coord = coord;
					int distance = estimateCost(myMap.getPosition(), coord);
					if(distance < minDist) {
						minCoord = coord;
						minDist = distance;
					}
	
				}
			}
		}

		//System.out.println("found destination" + this.coord.toString());

		//System.out.println("found destination" + minCoord.toString());

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
	private Coordinate findDest() {
		HashMap<Coordinate, MapTile> map = myMap.getMap();
		Iterator<Coordinate> mark = map.keySet().iterator();
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
		return cost;
	}
	
	 	
	
}
