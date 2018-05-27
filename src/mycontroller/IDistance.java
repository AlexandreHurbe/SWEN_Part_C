/**
 * Project partC for SWEN30006
 * @author Group 28
 * 27/5/2018
 */

package mycontroller;

import utilities.Coordinate;

public interface IDistance {
	// distance between two coordinates
	// could be calculating differently depends
	public int distance(Coordinate start, Coordinate end);
}
