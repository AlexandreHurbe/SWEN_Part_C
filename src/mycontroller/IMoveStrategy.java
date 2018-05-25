package mycontroller;

import utilities.Coordinate;

public interface IMoveStrategy {
	public int estimateCost(Coordinate start, Coordinate end);
	public int distance(Coordinate start, Coordinate end);
	public Coordinate getDestination();
}
