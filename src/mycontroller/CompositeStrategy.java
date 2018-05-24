package mycontroller;

import java.util.ArrayList;
import java.util.List;

public class CompositeStrategy implements IMoveStrategy{
	private List<IMoveStrategy> strategies = new ArrayList<>();
	public CompositeStrategy() {
		
	}
	public void addStrategy(IMoveStrategy strategy) {
		strategies.add(strategy);
	}
	@Override
	public void move() {
		// TODO Auto-generated method stub
		for(IMoveStrategy strategy: strategies) {
			strategy.move();
		}
	}
	
	public void add(IMoveStrategy strategy) {
		strategies.add(strategy);
	}
	
	public void remove(IMoveStrategy strategy) {
		strategies.remove(strategy);
	}
}
