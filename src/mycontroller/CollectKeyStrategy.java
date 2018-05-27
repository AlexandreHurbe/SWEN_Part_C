package mycontroller;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import tiles.MapTile.Type;
import utilities.Coordinate;

public class CollectKeyStrategy implements IMoveStrategy {
	
	MyMap myMap = MyMap.getInstance();
//	Coordinate dest;
	

	@Override
	public int estimateCost(Coordinate start, Coordinate end) {
		// TODO Auto-generated method stub
		int estimateCost = Math.abs(end.x - start.x) + Math.abs(end.y - start.y);
		
		return estimateCost;
	}

	@Override
	public Coordinate getDestination() {
		// TODO Auto-generated method stub
		return null;
	}
	

	public Coordinate getDestination(int keyRemaining) {

		//  got all keys go to exit
		if(keyRemaining == 1) {
			System.out.println("Getting Finish");
			Coordinate exit = myMap.getExit();
			return exit;
		}
		// got all keys in order, now let's get key
		Coordinate coord = (Coordinate) myMap.returnKeyStorage().get(keyRemaining - 1);
		Coordinate dest = new Coordinate(coord.x, coord.y);
		System.out.println("Dest in collectKey: " + dest.toString());
		return dest;
	}

	@Override
	public int distance(Coordinate start, Coordinate end) {
		// TODO Auto-generated method stub
		
		int lava = 30;
		int health = 0;
		int road = 0;
		int cost = estimateCost(start, end);
		if (myMap.getMap().get(end) instanceof LavaTrap) {
			cost += lava;
		}
		if (myMap.getMap().get(end) instanceof HealthTrap) {
			cost += health;
		}
		if(myMap.getMap().get(end).isType(MapTile.Type.ROAD)) {
			cost += road;
		}
		return cost + surroundWall(end);
		
	}
	private int surroundWall(Coordinate end) {
		int wall = 0;
		Coordinate current;
		for(int i = -1; i < 1; i++) {
			for (int j = -1; j < 1 ; j++) {
				current = new Coordinate(end.x+i, end.y+j);
				if(myMap.getMap().get(current) != null) {
					if(myMap.getMap().get(current).isType(Type.WALL)) {
						wall +=1;
					}
				}
			}
		}
		return wall;
	}
}
