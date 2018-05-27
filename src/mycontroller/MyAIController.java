package mycontroller;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;

import controller.CarController;
import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;
import utilities.PeekTuple;
import world.Car;
import world.WorldSpatial;
import world.WorldSpatial.Direction;
import world.WorldSpatial.RelativeDirection;

public class MyAIController extends CarController{

	private float speedLim = 3f;
//	private static final float MAX_DEGREES = 360;
	private static final float MAX_SPEED = 3f;
	private static final float SLOW_SPEED = 3f;
	private static final float TURN_SPEED = 1f;
//	private boolean SHOULD_SPEED = false;
	

//	private final float TURN_SPEED_LIM = 1f;
//	public int keysRemaining;
	private int keysToCollect = getKey();
//	private HashMap<Coordinate, Float> path;
	
	private MyMap myMap = MyMap.getInstance();
	private PathFinding pathFinding;
	private HashMap<Coordinate, Float> path;
	boolean needHealing = false;
//	private boolean hitWall = false;

	public MyAIController(Car car) {
		super(car);	
		myMap.setOriginalMap(getMap(), new Coordinate(getPosition()));
	}
	
	@Override
	public void update(float delta) {
		System.out.println("###########################################UPDATE###########################################");
		System.out.println("Keys: " + myMap.returnKeyStorage().toString());
//		keysRemaining = getKey();
		System.out.println("keyRemaining: " + getKey());
		//System.out.println("++++++++++++++++++++++++Key remain: " + keysRemaining);
		// TODO Auto-generated method stub
		/*
		 *  Explore the map and record where the key is when sees a key
		 *  
		 */
//		init();
////		Coordinate destination = new Coordinate(5,10);
//		this.view.update(getView(), this.currentPos, this.angle);
//		this.destination = this.explore.exploreKey(view);
////		Coordinate destination = new Coordinate(5,17);
//		path = view.findPath(destination);
//		// find the list of path given destination coordinates
//		this.move.update(this.angle, this.currentPos);
//		// find the action to take by the car given this path
//		
//		Move.Action action = move.followPath(path);
//		// perform this action 
//		move(action, delta);
		try {
			pathFinding = new PathFinding(this);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		path = pathFinding.findPath();
		System.out.println("Path: " + path.toString());
		
//		alexMove(delta);
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
//		if(actual == current) {
//			return true;
//		}
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
		Coordinate currentCoord = new Coordinate(getPosition());
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

//	private void alexMove(float delta) {
//		//System.out.println(path.toString());
//		Coordinate currentCoord = new Coordinate(getPosition());
		

//			float deltaAngle = goalAngle - currentAngle;
			
			//System.out.println(deltaAngle);
//			if ( deltaAngle == 0 || deltaAngle == 360) {
//				Coordinate nextCoord = checkNextCoord(currentCoord, delta);
				//Coordinate previousCoord = currentCoord;
//				for (int i = 0; i < 3; i ++) {
//					if (path.containsKey(nextCoord)) {
//						float nextAngle = path.get(nextCoord);
//						//System.out.println(nextAngle-currentAngle);
//						currentAngle = nextAngle;
//						currentCoord = nextCoord;
//						nextCoord = checkNextCoord(nextCoord, delta);
//					}
//					else {
//						for (int x = -1; x < 2; x++) {
//							for (int y = -1; y < 2; y++) {
//								if (Math.abs(x)!=Math.abs(y) && x != 0 && y != 0) {
//									nextCoord = new Coordinate(currentCoord.x + x, currentCoord.y + y);
//									if (path.containsKey(nextCoord)) {
//										this.SPEED_LIM = SLOW_SPEED;
//										adjustPosition(delta);
//										accelerate();
//									}
//									
//								}
//							}
//						}
//					}
//				}
				

				//System.out.println("The car should go faster");
//				this.SPEED_LIM = MAX_SPEED;
//				accelerate();

				
//			}
//			else {
//				this.SPEED_LIM = SLOW_SPEED;
//				if(deltaAngle > 0 && deltaAngle <= 180 || deltaAngle >-360 && deltaAngle <= -180) {
//					accelerate();
//					turnLeft(delta);
//					
//		    	}
//		    	else {
//		    		accelerate();
//		    		turnRight(delta);
//		    		
//		    	}
//			}
//			if(getSpeed() == 0) {
//				applyReverseAcceleration();
//				
//			}
		
//		else {
//			adjustPosition(delta);
//			accelerate();
//			
//		}
	
		
//	}
	
//	private Coordinate checkNextCoord(Coordinate currentCoord, float delta) {
//		Vector2 netAcceleration = calculateAcceleration(2f, 0.5f);
//		float nextVelocityX = getVelocity().x + netAcceleration.x * delta;
//		float nextVelocityY = getVelocity().y + netAcceleration.y * delta;
//		int nextX = (int) ((int)currentCoord.x +  nextVelocityX * delta);
//		int nextY = (int) ((int)currentCoord.x +  nextVelocityY * delta);
//		Coordinate nextCoord = new Coordinate(nextX, nextY);
//		return nextCoord;
//	}
	
//	private Vector2 calculateAcceleration(float drivingForce, float frictionForce){
//
//		Vector2 acceleration = new Vector2(1,0);
//		acceleration.rotate(0);
//		acceleration.scl(drivingForce);
//
//		Vector2 friction = new Vector2(1,0);
//		if(acceleration.len() > 0){
//			friction.rotate(acceleration.angle() - MAX_DEGREES/2);
//		} else {
//			friction.rotate((0 - MAX_DEGREES/2) % MAX_DEGREES);
//		}
//		friction.scl(frictionForce);
//		Vector2 netAcceleration = acceleration.add(friction);
//
//		return netAcceleration;
//	}

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
//		 Coordinate currentCoord = new Coordinate(getPosition());
//		 float goalAngle = path.get(currentCoord);
//		 float currentAngle = this.getAngle();
//		 float deltaAngle = Math.abs(goalAngle - currentAngle);
//		 float xPos = getX();
//		 float yPos = getY();
//		 Coordinate coord =  new Coordinate(getPosition());
//		 if(deltaAngle > 0 && deltaAngle < 180 || deltaAngle >-360 && deltaAngle < -180
//				 || goalAngle == 90 && xPos > coord.x || goalAngle == 270 && xPos < coord.x
//				 || goalAngle == 0  && yPos > coord.y || goalAngle == 180 && yPos < coord.y) {
//				System.out.println("LEFT");
//				turnLeft(delta);
//				
//	    	}
//		 if(deltaAngle > -180 && deltaAngle < 0 || deltaAngle > 180 && deltaAngle < 360
//				 || goalAngle == 90 && xPos < coord.x || goalAngle == 270 && xPos > coord.x
//				 || goalAngle == 0  && yPos < coord.y || goalAngle == 180 && yPos > coord.y) {
//				System.out.println("RIGHT");
//	    		turnRight(delta);
//	    		
//	    	}
	
	
		// move towards where we suppose to go
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
	 

//	 private WorldSpatial.RelativeDirection getDirection(float diff) {
//	    	if(diff > 0) {
//	    		return RelativeDirection.LEFT;
//	    	}
//	    	else {
//	    		return RelativeDirection.RIGHT;
//	    	}
//	 }

//	private void moveForward(float diff, float delta) {
//		if(diff == 0 || diff == 360) {
//			accelerate();
//		} else if(diff > 0 && diff <=180 ||  diff >-360 && diff <= -180) {
//			accelerate();
//			turnLeft(delta);
//		} else {
//			accelerate();
//			turnRight(delta);
//		}
//	}

	private void accelerate() {
		if(getSpeed() < speedLim) {
			applyForwardAcceleration();
		}
	}
	
	public int getKeysToCollect() {
		return this.keysToCollect;
	}
//	private boolean checkAroundWall() {
//		Coordinate currentCoord = new Coordinate(getPosition());
//		for(int i= -1; i < 1;i++) {
//			for(int j= -1; j < 1;j++) {
//				Coordinate newCoord = new Coordinate(currentCoord.x+i,currentCoord.y +j);
//				if(myMap.getMap().get(newCoord).isType(MapTile.Type.WALL)) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}
	/*
	private void checkNextMove(float delta) {
		PeekTuple tuple = peek(getVelocity(), targetDegree, turnDirection, delta)
	}*/

}
