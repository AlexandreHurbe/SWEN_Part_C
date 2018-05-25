package mycontroller;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;
import utilities.PeekTuple;
import world.Car;
import world.WorldSpatial;
import world.WorldSpatial.RelativeDirection;

public class MyAIController extends CarController{

	private float SPEED_LIM = 3f;
	private static final float MAX_DEGREES = 360;
	private static final float MAX_SPEED = 3f;
	private static final float SLOW_SPEED = 0.2f;
	private boolean SHOULD_SPEED = false;
	private WorldSpatial.RelativeDirection lastTurnDirection = null; // Shows the last turn direction the car takes.
	private boolean isTurningLeft = false;
	private boolean isTurningRight = false; 
	private WorldSpatial.Direction previousState = null; // Keeps track of the previous state
	private int EAST_THRESHOLD = 3;
	
	private final float TURN_SPEED_LIM = 1f;
	public int keysRemaining;
	public int keysToCollect = getKey();
//	private HashMap<Coordinate, Float> path;
	
	private MyMap myMap = MyMap.getInstance();
	private PathFinding pathFinding;
	private HashMap<Coordinate, Float> path;
	private boolean hitWall = false;

	public MyAIController(Car car) {
		super(car);	
		myMap.setOriginalMap(getMap(), new Coordinate(getPosition()));
	}
	
	@Override
	public void update(float delta) {
		keysRemaining = getKey();

		try {
			pathFinding = new PathFinding(this);
		} 
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		path = pathFinding.findPath();
		
		
		alexMove(delta);
	}
	
	

	private void alexMove(float delta) {
		//System.out.println(path.toString());
		//checkStateChange();
		//readjust(lastTurnDirection, delta);
		
		SPEED_LIM = MAX_SPEED;
		Coordinate currentCoord = new Coordinate(getPosition());
		if (path.containsKey(currentCoord)) {
			float goalAngle = path.get(currentCoord);
			float currentAngle = this.getAngle();
			int deltaAngle = (int)goalAngle - (int)currentAngle;
			
			if (deltaAngle == 0) {
				for (int i = 0; i < 3; i++) {
					Coordinate nextCoord = checkNextCoord(currentCoord, delta);
					if (path.containsKey(nextCoord)){
						currentCoord = nextCoord;
						nextCoord = checkNextCoord(currentCoord, delta);
					}
					/*else if (){
						SPEED_LIM = SLOW_SPEED;
						accelerate();
					}*/
				}
				System.out.println("Go straight");
				accelerate();
			}
			if (0 < deltaAngle && deltaAngle <= 90) {
				System.out.println("Go left");
				applyLeftTurn(getOrientation(), delta);
			}
			if (180 < deltaAngle && deltaAngle <= 270 || -90 <= deltaAngle && deltaAngle < 0) {
				System.out.println("Go right");
				applyRightTurn(getOrientation(), delta);
			}
		}
		else {
			accelerate();
		}
	}
	
	

	 /*private void adjustPosition(){
		// move towards where we suppose to go
		 float exactX =  getX();
		 float exactY = getY();
		 if 
	 }*/
	
	private Coordinate checkNextCoord(Coordinate currentCoord, float delta) {
		Vector2 netAcceleration = calculateAcceleration(2f, 0.5f);
		float nextVelocityX = getVelocity().x + netAcceleration.x * delta;
		float nextVelocityY = getVelocity().y + netAcceleration.y * delta;
		int nextX = (int) ((int)currentCoord.x +  nextVelocityX * delta);
		int nextY = (int) ((int)currentCoord.x +  nextVelocityY * delta);
		Coordinate nextCoord = new Coordinate(nextX, nextY);
		return nextCoord;
	}
	
	private Vector2 calculateAcceleration(float drivingForce, float frictionForce){

		Vector2 acceleration = new Vector2(1,0);
		acceleration.rotate(0);
		acceleration.scl(drivingForce);

		Vector2 friction = new Vector2(1,0);
		if(acceleration.len() > 0){
			friction.rotate(acceleration.angle() - MAX_DEGREES/2);
		} else {
			friction.rotate((0 - MAX_DEGREES/2) % MAX_DEGREES);
		}
		friction.scl(frictionForce);
		Vector2 netAcceleration = acceleration.add(friction);

		return netAcceleration;
	}

	

	 private WorldSpatial.RelativeDirection getDirection(float diff) {
	    	if(diff > 0) {
	    		return RelativeDirection.LEFT;
	    	}
	    	else {
	    		return RelativeDirection.RIGHT;
	    	}
	 }

	
	/**
	 * Readjust the car to the orientation we are in.
	 * @param lastTurnDirection
	 * @param delta
	 */
	private void readjust(WorldSpatial.RelativeDirection lastTurnDirection, float delta) {
		if(lastTurnDirection != null){
			if(!isTurningRight && lastTurnDirection.equals(WorldSpatial.RelativeDirection.RIGHT)){
				adjustRight(getOrientation(),delta);
			}
			else if(!isTurningLeft && lastTurnDirection.equals(WorldSpatial.RelativeDirection.LEFT)){
				adjustLeft(getOrientation(),delta);
			}
		}
		
	}
	
	/**
	 * Try to orient myself to a degree that I was supposed to be at if I am
	 * misaligned.
	 */
	private void adjustLeft(WorldSpatial.Direction orientation, float delta) {
		
		switch(orientation){
		case EAST:
			if(getAngle() > WorldSpatial.EAST_DEGREE_MIN+EAST_THRESHOLD){
				turnRight(delta);
			}
			break;
		case NORTH:
			if(getAngle() > WorldSpatial.NORTH_DEGREE){
				turnRight(delta);
			}
			break;
		case SOUTH:
			if(getAngle() > WorldSpatial.SOUTH_DEGREE){
				turnRight(delta);
			}
			break;
		case WEST:
			if(getAngle() > WorldSpatial.WEST_DEGREE){
				turnRight(delta);
			}
			break;
			
		default:
			break;
		}
		
	}

	private void adjustRight(WorldSpatial.Direction orientation, float delta) {
		System.out.println("The car needs to be readjusted!");
		switch(orientation){
		case EAST:
			if(getAngle() > WorldSpatial.SOUTH_DEGREE && getAngle() < WorldSpatial.EAST_DEGREE_MAX){
				turnLeft(delta);
			}
			break;
		case NORTH:
			if(getAngle() < WorldSpatial.NORTH_DEGREE){
				turnLeft(delta);
			}
			break;
		case SOUTH:
			if(getAngle() < WorldSpatial.SOUTH_DEGREE){
				turnLeft(delta);
			}
			break;
		case WEST:
			if(getAngle() < WorldSpatial.WEST_DEGREE){
				turnLeft(delta);
			}
			break;
			
		default:
			break;
		}
		
	}
	
	/**
	 * Checks whether the car's state has changed or not, stops turning if it
	 *  already has.
	 */
	private void checkStateChange() {
		if(previousState == null){
			previousState = getOrientation();
		}
		else{
			if(previousState != getOrientation()){
				if(isTurningLeft){
					isTurningLeft = false;
				}
				if(isTurningRight){
					isTurningRight = false;
				}
				previousState = getOrientation();
			}
		}
	}
	
	/**
	 * Turn the car counter clock wise (think of a compass going counter clock-wise)
	 */
	private void applyLeftTurn(WorldSpatial.Direction orientation, float delta) {
		switch(orientation){
		case EAST:
			if(!getOrientation().equals(WorldSpatial.Direction.NORTH)){
				turnLeft(delta);
			}
			break;
		case NORTH:
			if(!getOrientation().equals(WorldSpatial.Direction.WEST)){
				turnLeft(delta);
			}
			break;
		case SOUTH:
			if(!getOrientation().equals(WorldSpatial.Direction.EAST)){
				turnLeft(delta);
			}
			break;
		case WEST:
			if(!getOrientation().equals(WorldSpatial.Direction.SOUTH)){
				turnLeft(delta);
			}
			break;
		default:
			break;
		
		}
		
	}
	
	/**
	 * Turn the car clock wise (think of a compass going clock-wise)
	 */
	private void applyRightTurn(WorldSpatial.Direction orientation, float delta) {
		switch(orientation){
		case EAST:
			if(!getOrientation().equals(WorldSpatial.Direction.SOUTH)){
				turnRight(delta);
			}
			break;
		case NORTH:
			if(!getOrientation().equals(WorldSpatial.Direction.EAST)){
				turnRight(delta);
			}
			break;
		case SOUTH:
			if(!getOrientation().equals(WorldSpatial.Direction.WEST)){
				turnRight(delta);
			}
			break;
		case WEST:
			if(!getOrientation().equals(WorldSpatial.Direction.NORTH)){
				turnRight(delta);
			}
			break;
		default:
			break;
		
		}
		
	}
	
	
	private void moveForward(float diff, float delta) {
		if(diff == 0 || diff == 360) {
			accelerate();
		} else if(diff > 0 && diff <=180 ||  diff >-360 && diff <= -180) {
			accelerate();
			turnLeft(delta);
		} else {
			accelerate();
			turnRight(delta);
		}
	}

	private void accelerate() {
		if(getSpeed() < SPEED_LIM) {
			applyForwardAcceleration();
		}
	}
	
	private boolean checkAroundWall() {
		Coordinate currentCoord = new Coordinate(getPosition());
		for(int i= -1; i < 1;i++) {
			for(int j= -1; j < 1;j++) {
				Coordinate newCoord = new Coordinate(currentCoord.x+i,currentCoord.y +j);
				if(myMap.getMap().get(newCoord).isType(MapTile.Type.WALL)) {
					return true;
				}
			}
		}
		return false;
	}

}
