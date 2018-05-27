package mycontroller;

import java.util.HashMap;

import controller.CarController;
import tiles.HealthTrap;
import utilities.Coordinate;
import world.Car;

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
		myMap.setOriginalMap(getMap(), new Coordinate(getPosition()));
	}
	
	@Override
	public void update(float delta) {
		System.out.println("###########################################UPDATE###########################################");
		System.out.println("Keys: " + myMap.returnKeyStorage().toString());
		System.out.println("keyRemaining: " + getKey());

		// TODO Auto-generated method stub

		try {
			pathFinding = new PathFinding(this);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		path = pathFinding.findPath();
		System.out.println("Path: " + path.toString());
		
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
		
		float xPos = getX();
		float yPos = getY();
		float angleDiff = 0.2f;
		float posDiff = 0.05f;
		System.out.println("Xpos is:" + xPos);
		System.out.println("Ypos is:" + yPos);
		System.out.println("coord is:" + currentCoord);
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
			if(goalAngle == 0f || goalAngle ==180f) {
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
			if(goalAngle == 90f || goalAngle ==270f) {
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
			
			System.out.println("Not On path");
			System.out.println("Not On path, path: " + path.toString());
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

		 if(diffAngle > 3f) {
			 if(deltaAngle > 0 && deltaAngle <= 180 || deltaAngle >-360 && deltaAngle <= -180) {
				 System.out.println("LEFT in turn");
				 turnLeft(delta);
				 return;
			 } else {
				 System.out.println("RIGHT in turn");
				 turnRight(delta);
				 return;
			 }
		 } else {
			 
			 adjustPosition(delta);
		 }
	}
 private void adjustPosition(float delta){
	
		// move towards where we suppose to be
	 	 System.out.println("adjustPostion");
		 float xPos = getX();
		 float yPos = getY();
		 Coordinate coord =  new Coordinate(getPosition());
		 System.out.println("Coord is:" + coord.toString());
		 float angle = path.get(coord);
		 System.out.println("Angle is:" + angle);
		 System.out.println("Xpos is:" + xPos);
		 System.out.println("Ypos is:" + yPos);
		 //up
		 if(angle == 90f) {
			 if(xPos > (float)coord.x) {
				 System.out.println("LEFT");
				 turnLeft(delta);
				 return;
			 }
			 if (xPos < (float)coord.x) {
				 System.out.println("RIGHT");
				 turnRight(delta);
				 return;
			 }
		 }
		//down
		if(angle == 270f) {
			 if(xPos < (float)coord.x) {
				 System.out.println("LEFT");
				 turnLeft(delta);
				 return;
			 }
			 if (xPos > (float)coord.x) {
				 System.out.println("RIGHT");
				 turnRight(delta);
				 return;
			 }
		}
		//right
		if(angle == 0f) {
			 if(yPos > (float)coord.y) {
				 System.out.println("RIGHT");
				 turnRight(delta);
				 return;
			 }
			 if (yPos < (float)coord.y) {
				 System.out.println("LEFT");
				 turnLeft(delta);
				 return;
			 }
			 
		 }
		//left
		if(angle == 180f) {
			 if(yPos < (float)coord.y) {
				 System.out.println("RIGHT");
				 turnRight(delta);
				 return;
			 }
			 if (yPos > (float)coord.y) {
				 System.out.println("LEFT");
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
