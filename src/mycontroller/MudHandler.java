/**
 * Project partC for SWEN30006
 * @author Group 28
 * 27/5/2018
 */

package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;

public class MudHandler extends TrapHandler{

	public MudHandler(boolean blocked, HashMap<Coordinate, MapTile> view) {
		super(blocked, view);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean handleTrap() {
		// TODO Auto-generated method stub
		return false;
	}

}
