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
	public boolean handleTrap(Path path) {
		// TODO Auto-generated method stub
		return false;
	}

}
