/**
 * Project partC for SWEN30006
 * @author Group 28
 * 27/5/2018
 */

package mycontroller;

/**
 * Abstract class that handles a MapTile
 *
 */
public abstract class Handler {
	MyMap myMap = MyMap.getInstance();
	public Handler() {}
	// handle each types of tiles
	public abstract void handleMapTile();
}
