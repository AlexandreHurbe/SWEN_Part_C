package mycontroller;

import utilities.Coordinate;

public interface IDistance {
	// distance between two coordinates
	// could be calculating differently depends
	public int distance(Coordinate start, Coordinate end);
}
