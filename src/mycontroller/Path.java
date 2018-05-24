package mycontroller;

import java.util.HashMap;
import java.util.Stack;

import utilities.Coordinate;

public class Path {
	private HashMap<Coordinate, Float> currentPath;
	public Path() {
		// TODO Auto-generated constructor stub
	}
	
	
//	public void updatePath(HashMap<Coordinate, MyDirection.Direction> path) {
//		this.currentPath = path;
//	}
	
	public void updatePath(Stack<Coordinate> totalPath) {
		this.currentPath = fomulatePath(totalPath);
	}
	private HashMap<Coordinate, Float> fomulatePath(Stack<Coordinate> path) {
		HashMap<Coordinate, Float> myPath = new HashMap<>();
		// iterate through the stack and get directions
		Coordinate currentCoord = path.pop();
		while(!path.isEmpty()) {
			Coordinate nextCoord = path.pop();
			// get direction of current Coord
			myPath.put(currentCoord, getAngle(currentCoord, nextCoord));
			currentCoord = nextCoord;
		}
		
//		System.out.println(myPath.toString());
		return myPath;
	}
	
	private float getAngle(Coordinate current, Coordinate next) {
		int difX = next.x - current.x;
		int difY = next.y - current.y;
		float angle;
		angle =  (float) Math.toDegrees(Math.atan2(difY, difX));
		// prevent negative angles 	
		if(angle < 0) {
			angle += 360;
		}
//		System.out.println(angle);
		return angle;
	}
	
	public HashMap<Coordinate, Float> getPath(){
		return this.currentPath;
	}
	
}
