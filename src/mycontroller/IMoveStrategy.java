package mycontroller;

import utilities.Coordinate;

public interface IMoveStrategy extends IDistance{
	public int estimateCost(Coordinate start, Coordinate end);

	public Coordinate getDestination();
}
