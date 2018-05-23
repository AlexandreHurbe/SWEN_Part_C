package mycontroller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import utilities.Coordinate;
import world.WorldSpatial;

public class Path {
	private HashMap<Coordinate, MyDirection.Direction> currentPath;
	public Path() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void updatePath(HashMap<Coordinate, MyDirection.Direction> path) {
		this.currentPath = path;
	}

	public void updatePath(Stack<Coordinate> totalPath) {
		this.currentPath = fomulatePath(totalPath);
	}
	private HashMap<Coordinate, MyDirection.Direction> fomulatePath(Stack<Coordinate> path) {
		HashMap<Coordinate, MyDirection.Direction> myPath = new HashMap<>();
		// iterate through the stack and get directions
		Coordinate currentCoord = path.pop();
		while(!path.isEmpty()) {
			Coordinate nextCoord = path.pop();
			// get direction of current Coord
			myPath.put(currentCoord, findDirection(currentCoord, nextCoord));
			currentCoord = nextCoord;
		}
		System.out.println(myPath.toString());
		return myPath;
	}
	
	private MyDirection.Direction findDirection(Coordinate current, Coordinate next) {
		int degree = getAngle(current, next);
		
		switch(degree) {
			case MyDirection.EAST_DEGREE_MAX:
				return MyDirection.Direction.EAST;
			case MyDirection.EAST_DEGREE_MIN:
				return MyDirection.Direction.EAST;
			case MyDirection.NE_DEGREE:
				return MyDirection.Direction.NE;
			case MyDirection.NORTH_DEGREE:
				return MyDirection.Direction.NORTH;
			case MyDirection.NW_DEGREE:
				return MyDirection.Direction.NW;
			case MyDirection.SE_DEGREE:
				return MyDirection.Direction.SE;
			case MyDirection.SOUTH_DEGREE:
				return MyDirection.Direction.SOUTH;
			case MyDirection.SW_DEGREE:
				return MyDirection.Direction.SW;
			case MyDirection.WEST_DEGREE:
				return MyDirection.Direction.WEST;
			default:
				break;
		}
		return null;
			
	}
	
	private int getAngle(Coordinate current, Coordinate next) {
		int difX = next.x - current.x;
		int difY = next.y - current.y;
		int angle;
		angle = (int) Math.toDegrees(Math.atan2(difX, difY));
		// prevent negative angles
		if(angle < 0) {
			angle += 360;
		}
		System.out.println(angle);
		return angle;
	}
	
	public HashMap<Coordinate, MyDirection.Direction> getPath(){
		return this.currentPath;
	}
	
}
