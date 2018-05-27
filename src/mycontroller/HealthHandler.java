package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;

public class HealthHandler extends TrapHandler {

	public HealthHandler(boolean blocked, HashMap<Coordinate, MapTile> view) {
		super(blocked, view);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean handleTrap() {
		// TODO Auto-generated method stub
		return false;
	}

}
