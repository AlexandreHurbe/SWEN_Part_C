package mycontroller;

import java.util.HashMap;
import java.util.Iterator;

import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class ExploreStrategy implements IMoveStrategy {
	private final int MAXMAP = 100;
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
	
//	public abstract void explore(View currentView, Float health);
	
	public Coordinate exploreKey(View currentView) {
		System.out.println("start explore key");
		HashMap<Coordinate, MapTile> markMap = currentView.getMarkMap();
		System.out.println(markMap.toString());
		Coordinate coord;
		Iterator<Coordinate> mark = markMap.keySet().iterator();
		while(mark.hasNext()) {
			
			coord = mark.next();
//			System.out.println(coord.toString() + "is" + markMap.get(coord).getType().toString());
			if(markMap.get(coord) == null) {
				System.out.println("found destination" + coord.toString());
				return coord;
			}
		}

		
		System.out.println("found no key");
		return null;
	}

}
