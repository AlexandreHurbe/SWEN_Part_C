package mycontroller;

import java.util.Arrays;
import java.util.Collections;

import tiles.HealthTrap;
import tiles.LavaTrap;
import utilities.Coordinate;

public class CollectKeyStrategy implements IMoveStrategy {
	
	MyMap myMap = MyMap.getInstance();
	//int currentKey = -1;
	Coordinate dest;
	public void move() {
		// TODO Auto-generated method stub
		
	}
	
	public void collectKey() {
		
	}

	@Override
	public int estimateCost(Coordinate start, Coordinate end) {
		// TODO Auto-generated method stub
		int estimateCost = Math.abs(dest.x - start.x) + Math.abs(dest.y - start.y);
		
		return estimateCost;
	}

	@Override
	public Coordinate getDestination() {
		// TODO Auto-generated method stub
		//if currentKey = -1
		Object[] keys = myMap.returnKeyStorage().keySet().toArray();
		Arrays.sort(keys, Collections.reverseOrder());
		Coordinate coord = (Coordinate) myMap.returnKeyStorage().get(keys[0]);
		Coordinate dest = new Coordinate(coord.x, coord.y);
		//myMap.returnKeyStorage().remove(keys[0]);
		//System.out.println("KeyStorage: " + myMap.returnKeyStorage().toString());
		//System.out.println("Dest in collectKey: " + dest.toString());
		return dest;
	}
	
	public Coordinate getDestination(int keyRemaining) {
//		Object[] keys = myMap.returnKeyStorage().keySet().toArray();
//		Arrays.sort(keys, Collections.reverseOrder());
		Coordinate coord = (Coordinate) myMap.returnKeyStorage().get(keyRemaining - 1);
		Coordinate dest = new Coordinate(coord.x, coord.y);
		this.dest = dest;
		//myMap.returnKeyStorage().remove(keys[0]);
		//System.out.println("KeyStorage: " + myMap.returnKeyStorage().toString());
		System.out.println("Dest in collectKey: " + dest.toString());
		return dest;
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
