package mycontroller;

import utilities.Coordinate;

public interface IMoveStrategy extends IDistance{
	// heuristic cost of the route
	public int estimateCost(Coordinate start, Coordinate end);
	// give a destination to the path finder
	public Coordinate getDestination();
}
