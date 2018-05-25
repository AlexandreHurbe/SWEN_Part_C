package mycontroller;

import java.util.ArrayList;
import java.util.List;

import utilities.Coordinate;

public class CompositeStrategy implements IMoveStrategy{
	private List<IMoveStrategy> strategies = new ArrayList<>();
	
	
	public CompositeStrategy() {
		
	}
//	public void addStrategy(IMoveStrategy strategy) {
//		strategies.add(strategy);
//	}
//	
//	public void add(IMoveStrategy strategy) {
//		strategies.add(strategy);
//	}
//	
//	public void remove(IMoveStrategy strategy) {
//		strategies.remove(strategy);
//	}
	@Override
	public int estimateCost(Coordinate start, Coordinate end) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Coordinate getDestination() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int distance(Coordinate start, Coordinate end) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
