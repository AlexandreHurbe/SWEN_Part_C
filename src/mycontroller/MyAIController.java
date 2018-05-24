package mycontroller;

import java.util.HashMap;
import controller.CarController;
import utilities.Coordinate;
import utilities.PeekTuple;
import world.Car;
import world.WorldSpatial;
import world.WorldSpatial.RelativeDirection;

public class MyAIController extends CarController{
	private final float SPEED_LIM = (float) 3;
	
//	private HashMap<Coordinate, Float> path;
	
	private MyMap myMap = MyMap.getInstance();
	private PathFinding pathFinding;
	private HashMap<Coordinate, Float> path;
	

	public MyAIController(Car car) {
		super(car);	
		myMap.setOriginalMap(getMap());
	}
	
	@Override
	public void update(float delta) {
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
		
		
		move(delta);
	}
	

	
	private void move(float delta) {
		Coordinate currentCoord = new Coordinate(getPosition());
		
		float goalAngle = path.get(currentCoord);
		int diff = (int)goalAngle - (int)this.getAngle();
		
		WorldSpatial.RelativeDirection dir = getDirection(diff);
		PeekTuple nextTuple = peek(getVelocity(), goalAngle, dir, delta);
		System.out.println("Goal: " + goalAngle);
		System.out.println("Car: " + getAngle());
		System.out.println("Diff: " + diff);
		if(!nextTuple.getReachable()) {
			// slow down and turn
			applyBrake();
			applyReverseAcceleration();
		} else {

			if(diff==0 ) {
				applyForwardAcceleration();
			}
	    	if(diff > 0 && diff < 180 || diff>-360 && diff < -180) {
	    		accelerate();
	    		turnLeft(delta);
	    	}else {
	    		accelerate();
	    		turnRight(delta);
	    	}
		}
		
		

	}
	
	 private WorldSpatial.RelativeDirection getDirection(float diff) {
	    	if(diff >= 0 && diff < 180) {
	    		return RelativeDirection.LEFT;
	    	}else {
	    		return RelativeDirection.RIGHT;
	    	}
	    }
	private void accelerate() {
		if(getSpeed() < SPEED_LIM) {
			applyForwardAcceleration();
		}
		else {
			applyBrake();

		}
	}
	/*
	private void checkNextMove(float delta) {
		PeekTuple tuple = peek(getVelocity(), targetDegree, turnDirection, delta)
	}*/

}
