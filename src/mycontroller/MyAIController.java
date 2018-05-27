/**
 * Project partC for SWEN30006
 * @author Group 28
 * 27/5/2018
 */

package mycontroller;

import java.util.HashMap;

import controller.CarController;
import tiles.HealthTrap;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

/**
 * Controls car 
 *
 */
public class MyAIController extends CarController{

	private float speedLim = 3f;

	private static final float MAX_SPEED = 3f;
	private static final float SLOW_SPEED = 3f;
	private static final float TURN_SPEED = 1f;
	
	
	private int keysToCollect = getKey();
	
	private MyMap myMap = MyMap.getInstance();
	private PathFinding pathFinding;
	private HashMap<Coordinate, Float> path;
	boolean needHealing = false;


	public MyAIController(Car car) {
		super(car);	
		// initialize the map
		myMap.setOriginalMap(getMap(), new Coordinate(getPosition()));
		HandlerLibrary handlers = HandlerLibrary.getInstance();
		// initialize the handler
		handlers.initialise();
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
		// avoid null path 
		try {
			pathFinding = new PathFinding(this);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		path = pathFinding.findPath();
		// slow down when close to health trap
		if(nearHealthTrap()) {
			applyBrake();
		} else {
			move(delta);
		}
	}
	
	private boolean nearHealthTrap() {
		Coordinate currentCoord = new Coordinate(getPosition());
		
		if(path.size()==0) {
			if(this.myMap.getMap().get(currentCoord) instanceof HealthTrap) {
				return true;	
			}
		}
		return false;
	}
	private boolean onPath() {
		Coordinate currentCoord = new Coordinate(getPosition());
		// get current position on float
		float xPos = getX();
		float yPos = getY();
		// difference threshold for testing on path
		float angleDiff = 0.2f;
		float posDiff = 0.05f;
		if(isHorizontal()) {
			//check Y position
			if(almostSame(yPos, currentCoord.y, angleDiff) && almostSame(getAngle(), this.path.get(currentCoord)
					,posDiff)) {
				return true;
			}
		}
		if(isVertical()) {
			//check X position
			if(almostSame(xPos, currentCoord.x, angleDiff) && almostSame(getAngle(), this.path.get(currentCoord)
					,posDiff)) {
				return true;
			}
		}
		return false;
	}
	private boolean almostSame(float actual, float current, float diff) {
		float max = current + diff;
		float min = current - diff;
		
		if(actual > min && actual < max) {
			return true;
		}
		return false;
	}
	private boolean isHorizontal() {
		Coordinate currentCoord = new Coordinate(getPosition());
		float goalAngle;
		if(path.containsKey(currentCoord)) {
			goalAngle = path.get(currentCoord);
			if(goalAngle == WorldSpatial.EAST_DEGREE_MIN || goalAngle == WorldSpatial.WEST_DEGREE) {
				return true;
			}
		}
		return false;
	}
	private boolean isVertical() {
		Coordinate currentCoord = new Coordinate(getPosition());
		float goalAngle;
		if(path.containsKey(currentCoord)) {
			goalAngle = path.get(currentCoord);
			if(goalAngle == WorldSpatial.NORTH_DEGREE || goalAngle ==WorldSpatial.SOUTH_DEGREE) {
				return true;
			}
		}
		return false;
	}
	
	private void move(float delta) {
		// following the right path
		if(path.size()== 0 || onPath() )  {
			
			speedLim = MAX_SPEED;
			accelerate();
		} else {
	
			speedLim = SLOW_SPEED;
			
			if(getSpeed() > SLOW_SPEED) {
				applyBrake();
				turn(delta);
				
			} else if(getSpeed() == 0){
				applyReverseAcceleration();
				turn(delta);
			} else {
				speedLim = TURN_SPEED;
				accelerate();
				turn(delta);
			}
			
			
		}
	}


	private void turn(float delta) {
		 Coordinate currentCoord = new Coordinate(getPosition());
		 float goalAngle = path.get(currentCoord);
		 float currentAngle = this.getAngle();
		 float deltaAngle = goalAngle - currentAngle;
		 float diffAngle  = Math.abs(deltaAngle);
		 // created after testing different numbers
		 float maxDiff = 3f;
		 if(diffAngle > maxDiff) {
			 // left turn when turning left is closer to goal angle
			 if(deltaAngle > WorldSpatial.EAST_DEGREE_MIN && deltaAngle <= WorldSpatial.WEST_DEGREE 
					 || deltaAngle >-WorldSpatial.EAST_DEGREE_MAX && deltaAngle <= -WorldSpatial.WEST_DEGREE) {
				 turnLeft(delta);
				 return;
			 } else {
		
				 turnRight(delta);
				 return;
			 }
		 } else {
			 // when the turning phase is finished, the angle difference is smaller than 
			 // maxDiff, the car will adjust its position
			 adjustPosition(delta);
		 }
	}
 private void adjustPosition(float delta){
	
		// move towards where we suppose to be

		 float xPos = getX();
		 float yPos = getY();
		 Coordinate coord =  new Coordinate(getPosition());

		 float angle = path.get(coord);
	
		 //up
		 if(angle == WorldSpatial.NORTH_DEGREE) {
			 if(xPos > (float)coord.x) {
		
				 turnLeft(delta);
				 return;
			 }
			 if (xPos < (float)coord.x) {
		
				 turnRight(delta);
				 return;
			 }
		 }
		//down
		if(angle == WorldSpatial.SOUTH_DEGREE) {
			 if(xPos < (float)coord.x) {
		
				 turnLeft(delta);
				 return;
			 }
			 if (xPos > (float)coord.x) {
		
				 turnRight(delta);
				 return;
			 }
		}
		//right
		if(angle == WorldSpatial.EAST_DEGREE_MIN) {
			 if(yPos > (float)coord.y) {
	;
				 turnRight(delta);
				 return;
			 }
			 if (yPos < (float)coord.y) {
		
				 turnLeft(delta);
				 return;
			 }
			 
		 }
		//left
		if(angle == WorldSpatial.WEST_DEGREE) {
			 if(yPos < (float)coord.y) {
		
				 turnRight(delta);
				 return;
			 }
			 if (yPos > (float)coord.y) {
		
				 turnLeft(delta);
				 return;
			 }
			 
		}
		 
	 }
	 


	private void accelerate() {
		if(getSpeed() < speedLim) {
			applyForwardAcceleration();
		}
	}
	
	public int getKeysToCollect() {
		return this.keysToCollect;
	}

}
