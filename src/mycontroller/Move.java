package mycontroller;

import java.util.HashMap;

import utilities.Coordinate;
import world.WorldSpatial;

public class Move {
	enum Action { FORWARD, BACKWARD, LEFT, RIGHT, KEEPGOING}
    public Move(){
       
    }
    // return a direction to turn or not 
    public Action followPath(HashMap<Coordinate, MyDirection.Direction> path, View view) {
//    	System.out.println(path.toString());
//    	System.out.println(view.getPosition().toString());
    	Coordinate currentPos = view.getPosition();
    	MyDirection.Direction direction;
    	float carAngle;
    	int goalAngle;
//    	System.out.println(path.containsKey(currentPos));
    	if(!path.containsKey(currentPos)) {
    		
    		return Action.KEEPGOING;
    	}
    	else {
    		direction = path.get(currentPos);
    		goalAngle = MyDirection.coupleDirection.get(direction);
    		carAngle = view.getAngle();
    		return compareAngle(carAngle, goalAngle);
    	}
    }
    
    private Action compareAngle(float carAngle, int goalAngle) {
    	float diff = (float)goalAngle - carAngle;
    	
    	if(diff > 0) {
    		return Action.LEFT;
    	}else if(diff < 0) {
    		return Action.RIGHT;
    	}else {
    		return Action.FORWARD;
    	}
    }
}
