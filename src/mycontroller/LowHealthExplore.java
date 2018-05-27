/**
 * Project partC for SWEN30006
 * @author Group 28
 * 27/5/2018
 */

package mycontroller;

import java.util.HashMap;
import java.util.Iterator;

import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class LowHealthExplore extends ExploreStrategy{
	
	MyMap myMap = MyMap.getInstance();
	HandlerLibrary handlers = HandlerLibrary.getInstance();
	@Override
	public int estimateCost(Coordinate start, Coordinate end) {
		int lava = 10;
		int health = -5;
		int cost = distance(start, end);
		if (myMap.getMap().get(end) instanceof LavaTrap) {
			cost += lava;
		}
		if (myMap.getMap().get(end) instanceof HealthTrap) {
			cost +=health;
		}
		Handler wallHandler = handlers.getHandler("wallHandler");
		return cost + ((WallHandler)wallHandler).handleMapTile(end);
	}

	@Override
	public Coordinate getDestination() {

		HashMap<Coordinate, MapTile> markMap = myMap.getMarkMap();

		Coordinate coord, minCoord, alterCoord;
		coord = minCoord = alterCoord = null;
		int minDistance = Integer.MAX_VALUE;
		Iterator<Coordinate> mark = markMap.keySet().iterator();
		// find the closest health trap on the map
		while(mark.hasNext()) {
			
			coord = mark.next();

			if(markMap.get(coord) instanceof HealthTrap) {
				int currentDistance = distance(myMap.getPosition(), coord);
				if (currentDistance < minDistance){
					minDistance = currentDistance;
					minCoord = coord;
				}
				
			}
			if(markMap.get(coord) == null) {
				alterCoord = coord;
			}
		}
		
		if(minCoord != null) {
			return minCoord;
		}
		// when there is no health trap
		// good luck and explore the map see if find any
		else {	
			return alterCoord;
		}
	}
	
	@Override
	public int distance(Coordinate start, Coordinate end) {
		// TODO Auto-generated method stub
		int distance = Math.abs(end.x - start.x) + Math.abs(end.y - start.y);
		
		return distance;
	}
}
