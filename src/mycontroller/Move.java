package mycontroller;

import java.util.HashMap;

import utilities.Coordinate;
import world.WorldSpatial;

public class Move {
	enum Action { FORWARD, BACKWARD, LEFT, RIGHT, KEEPGOING}
	private float carAngle;
	private Coordinate currentPos;
    public Move(){
       
    }
    public void update(float angle, Coordinate coord) {
    	this.carAngle = angle;
    	this.currentPos = coord;
    }
    // return a direction to turn or not 
    public Action followPath(HashMap<Coordinate, MyDirection.Direction> path) {
//    	System.out.println(path.toString());
//    	System.out.println(view.getPosition().toString());

    	MyDirection.Direction direction;
    	float goalAngle;
//    	System.out.println(path.containsKey(currentPos));
    	if(!path.containsKey(currentPos)) {
    		
    		return Action.KEEPGOING;
    	}
    	else {
    		direction = path.get(currentPos);
    		goalAngle = MyDirection.coupleDirection.get(direction);

    		return compareAngle(goalAngle);
    	}
    }
    
    private Action compareAngle(float goalAngle) {
    	float diff = goalAngle - this.carAngle;
    	System.out.println(diff);
    	if (diff == 0 || diff == 360) {
    		return Action.FORWARD;
    	} else if(diff > 0 && diff < 180) {
    		return Action.LEFT;
    	}else {
    		return Action.RIGHT;
    	}
    }
}
