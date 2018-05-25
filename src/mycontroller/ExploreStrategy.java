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
		while(mark.hasNext()) {
			
			coord = mark.next();
			
//			System.out.println(coord);
//			System.out.println(coord.toString() + "is" + markMap.get(coord).getType().toString());
			if(markMap.get(coord) == null) {
				//System.out.println("found destination" + coord.toString());
				this.coord = coord;
				int distance = estimateCost(myMap.getPosition(), coord);
				if(distance < minDist) {
					minCoord = coord;
					minDist = distance;
				}

			}
		}

		//System.out.println("found destination" + this.coord.toString());

		//System.out.println("found destination" + minCoord.toString());

		
		return minCoord;


	}

	@Override
	public int estimateCost(Coordinate start, Coordinate end) {
		// TODO Auto-generated method stub
		// make health smaller and 
		int estimateCost = Math.abs(coord.x - start.x) + Math.abs(coord.y - start.y);
		
		return estimateCost;

	}

	@Override
	public int distance(Coordinate start, Coordinate end) {
		// TODO Auto-generated method stub
		int lava = 5;
		int health = -1;
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
