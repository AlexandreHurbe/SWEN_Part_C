package mycontroller;

import java.util.HashMap;
import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;
import utilities.PeekTuple;
import world.Car;
import world.WorldSpatial;
import world.WorldSpatial.RelativeDirection;

public class MyAIController extends CarController{
	private final float SPEED_LIM = (float) 2.5;
	
//	private HashMap<Coordinate, Float> path;
	
	private MyMap myMap = MyMap.getInstance();
	private PathFinding pathFinding;
	private HashMap<Coordinate, Float> path;
	private boolean hitWall = false;

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

		int diff =  (int)this.getAngle()- (int)goalAngle;
		System.out.println("diff:" + diff);
		System.out.println("goalAngle: " + goalAngle);
		System.out.println("carAngle: " + getAngle());

		

		WorldSpatial.RelativeDirection dir = getDirection(diff);
		PeekTuple nextTuple = peek(getVelocity(), goalAngle, dir, delta);

		System.out.println("Goal: " + goalAngle);
		System.out.println("Car: " + getAngle());
		System.out.println("Diff: " + diff);
		if(hitWall) {
			System.out.println("Wall has been hit");
		}
		if(!nextTuple.getReachable()) {
			// slow down and turn

			applyReverseAcceleration();
		}
		else {
			if(!nextTuple.getReachable() && checkAroundWall()) {
				// slow down and turn
				applyReverseAcceleration();
				hitWall = true;
			} 
			else {
				hitWall = false;
				if(diff==0 ) {
					applyForwardAcceleration();
					
				}
		    	if(diff > 0 && diff < 180 || diff>-360 && diff < -180) {
		    		accelerate();
		    		turnLeft(delta);
		    	}
		    	else {
		    		accelerate();
		    		turnRight(delta);
		    	}
			}
	    	if(diff > 0 && diff < 180 || diff>-360 && diff < -180) {
	    		accelerate();
	    		turnLeft(delta);
	    	}
	    	else {
	    		accelerate();
	    		turnRight(delta);
	    	}
		}
	}
	
	 private WorldSpatial.RelativeDirection getDirection(float diff) {
	    	if(diff >= 0 && diff < 180 || diff>-360 && diff < -180) {
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
