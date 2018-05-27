/**
 * Project partC for SWEN30006
 * @author Group 28
 * 27/5/2018
 */

package mycontroller;

import utilities.Coordinate;

/**
 * Strategy for getting destinations in different ways
 *
 */
public interface IMoveStrategy extends IDistance{
	// heuristic cost of the route
	public int estimateCost(Coordinate start, Coordinate end);
	// give a destination to the path finder
	public Coordinate getDestination();
}
