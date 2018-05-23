package mycontroller;

import java.util.HashMap;

public class MyDirection {
	enum Direction { EAST, WEST, SOUTH, NORTH, NE, NW, SE, SW}
	
	static enum RelativeDirection { LEFT, RIGHT };
	
	public static final int EAST_DEGREE_MIN = 0;
	public static final int EAST_DEGREE_MAX = 360;
	public static final int NORTH_DEGREE = 90;
	public static final int WEST_DEGREE = 180;
	public static final int SOUTH_DEGREE = 270;
	public static final int NE_DEGREE = 45;
	public static final int NW_DEGREE = 135;
	public static final int SE_DEGREE = 315;
	public static final int SW_DEGREE = 225;
	
	public static final HashMap<Direction, Integer> coupleDirection = coupleDirection();
	
	private static HashMap<Direction, Integer> coupleDirection() {
		HashMap<Direction, Integer> coupleDirection = new HashMap<>();
		coupleDirection.put(Direction.EAST, EAST_DEGREE_MIN|EAST_DEGREE_MAX);
		coupleDirection.put(Direction.NORTH, NORTH_DEGREE);
		coupleDirection.put(Direction.WEST, WEST_DEGREE);
		coupleDirection.put(Direction.SOUTH, SOUTH_DEGREE);
		coupleDirection.put(Direction.NORTH, NORTH_DEGREE);
		coupleDirection.put(Direction.NE, NE_DEGREE);
		coupleDirection.put(Direction.NW, NW_DEGREE);
		coupleDirection.put(Direction.SE, SE_DEGREE);
		coupleDirection.put(Direction.SW, SW_DEGREE);
		return coupleDirection;
	}
}
