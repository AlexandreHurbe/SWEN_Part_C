package mycontroller;

import java.util.HashMap;
import java.util.Iterator;

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
		Coordinate coord;
		Iterator<Coordinate> mark = markMap.keySet().iterator();
//		System.out.println(markMap.keySet());

		while(mark.hasNext()) {
			
			coord = mark.next();
//			System.out.println(coord);
//			System.out.println(coord.toString() + "is" + markMap.get(coord).getType().toString());
			if(markMap.get(coord) == null) {
				System.out.println("found destination" + coord.toString());
				this.coord = coord;
				return coord;
			}
		}
		System.out.println("found no key");
		return null;
	}

	@Override
	public int estimateCost(Coordinate start, Coordinate end) {
		// TODO Auto-generated method stub
		int estimateCost = Math.abs(coord.x - start.x) + Math.abs(coord.y - start.y);
		return estimateCost;

	}
	
}
