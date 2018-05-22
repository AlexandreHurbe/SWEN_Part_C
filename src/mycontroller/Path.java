package mycontroller;

import java.util.HashMap;

import utilities.Coordinate;
import world.WorldSpatial;

public class Path {
	private HashMap<Coordinate, WorldSpatial.Direction> currentPath;
	public Path() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void updatePath(HashMap<Coordinate, WorldSpatial.Direction> path) {
		this.currentPath = path;
	}
	
	
}
