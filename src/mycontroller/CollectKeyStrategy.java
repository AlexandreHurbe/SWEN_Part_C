/**
 * Project partC for SWEN30006
 * @author Group 28
 * 27/5/2018
 */

package mycontroller;

import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;


/**
 * Strategy for collecting keys in order
 *
 */
public class CollectKeyStrategy implements IMoveStrategy {
	
	MyMap myMap = MyMap.getInstance();
	HandlerLibrary handlers = HandlerLibrary.getInstance();
	

	@Override
	public int distance(Coordinate start, Coordinate end) {
		int distance = Math.abs(end.x - start.x) + Math.abs(end.y - start.y);
		return distance;
	}

	@Override
	public Coordinate getDestination() {
		return null;
	}
	
	// Overload getDestination method
	public Coordinate getDestination(int keyRemaining) {

		//  got all keys go to exit
		if(keyRemaining == 1) {
			Coordinate exit = myMap.getExit();
			return exit;
		}
		
		// got all keys in order, now let's get key
		Coordinate coord = (Coordinate) myMap.returnKeyStorage().get(keyRemaining - 1);
		Coordinate dest = new Coordinate(coord.x, coord.y);
		return dest;
	}

	@Override
	public int estimateCost(Coordinate start, Coordinate end) {
		int lava = 30;
		int health = 0;
		int road = 0;
		int cost = distance(start, end);
		if (myMap.getMap().get(end) instanceof LavaTrap) {
			cost += lava;
		}
		if (myMap.getMap().get(end) instanceof HealthTrap) {
			cost += health;
		}
		if(myMap.getMap().get(end).isType(MapTile.Type.ROAD)) {
			cost += road;
		}
		Handler wallHandler = handlers.getHandler("wallHandler");
		return cost + ((WallHandler)wallHandler).handleMapTile(end);
		
	}
}
