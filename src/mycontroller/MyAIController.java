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
	private static final float SLOW_SPEED = 2f;
	private boolean SHOULD_SPEED = false;	
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
		
		
		alexMove(delta);
	}
	

	
	private void alexMove(float delta) {
		//System.out.println(path.toString());
		Coordinate currentCoord = new Coordinate(getPosition());
		if (path.containsKey(currentCoord)) {
			float goalAngle = path.get(currentCoord);
			float currentAngle = this.getAngle();
			int deltaAngle = (int)goalAngle - (int)currentAngle;
			SHOULD_SPEED = false;
			//System.out.println(deltaAngle);
			if (4 > deltaAngle && deltaAngle >= 0 || -359 >= deltaAngle && deltaAngle < -355) {
				Coordinate nextCoord = checkNextCoord(currentCoord, delta);
				//Coordinate previousCoord = currentCoord;
				for (int i = 0; i < 3; i ++) {
					if (path.containsKey(nextCoord)) {
						float nextAngle = path.get(nextCoord);
						//System.out.println(nextAngle-currentAngle);
						currentAngle = nextAngle;
						currentCoord = nextCoord;
						nextCoord = checkNextCoord(nextCoord, delta);
					}
					else {
						for (int x = -1; x < 2; x++) {
							for (int y = -1; y < 2; y++) {
								if (Math.abs(x)!=Math.abs(y) && x != 0 && y != 0) {
									nextCoord = new Coordinate(currentCoord.x + x, currentCoord.y + y);
									if (path.containsKey(nextCoord)) {
										this.SPEED_LIM = SLOW_SPEED;
										accelerate();
									}
									
								}
							}
						}
					}
				}
				
				SHOULD_SPEED = true;
				if (SHOULD_SPEED = true){
					//System.out.println("The car should go faster");
					this.SPEED_LIM = MAX_SPEED;
					accelerate();
				}
			}
			else {
				this.SPEED_LIM = SLOW_SPEED;
				if(deltaAngle > 0 && deltaAngle < 180 || deltaAngle >-360 && deltaAngle < -180) {
					accelerate();
					turnLeft(delta);
		    	}
		    	else {
		    		accelerate();
		    		turnRight(delta);
		    	}
			}
			if(getSpeed() == 0) {
				applyReverseAcceleration();
			}
		}
		else {
			accelerate();
		}
		
	}
	
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

	
	 private void adjustPosition(){
		// move towards where we suppose to go
		 
	 }

	 private WorldSpatial.RelativeDirection getDirection(float diff) {
	    	if(diff > 0) {
	    		return RelativeDirection.LEFT;
	    	}
	    	else {
	    		return RelativeDirection.RIGHT;
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
	/*
	private void checkNextMove(float delta) {
		PeekTuple tuple = peek(getVelocity(), targetDegree, turnDirection, delta)
	}*/

}
