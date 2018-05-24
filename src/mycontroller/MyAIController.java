package mycontroller;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import com.badlogic.gdx.math.Interpolation.Exp;
import com.badlogic.gdx.scenes.scene2d.ui.List;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

public class MyAIController extends CarController{
	private final float SPEED_LIM = (float) 2;
	


	private Move move = new Move();
	private Move.Action lastAction;
//	private HashMap<Coordinate, Float> path;
	private Coordinate destination;
	
	private MyMap myMap = MyMap.getInstance();
	private PathFinding pathFinding;
	private HashMap<Coordinate, Float> path;

	private IMoveStrategy strategy;
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
		

		pathFinding = new PathFinding(this);
		destination = strategy.getDestination();
		path = pathFinding.findPath(destination);
		
		
		Move.Action action = move.followPath(path);
		
		move(action, delta);
		
		

		
		
		
	}
	

	
	private void move(Move.Action action, float delta) {
		// solution when going off track
		if(action != Move.Action.KEEPGOING) {
			lastAction = action;
		}
		
		switch(action) {
			case LEFT:
				if(getSpeed() <= SPEED_LIM) {
					applyForwardAcceleration();
				}
				turnLeft(delta);
				break;
			case RIGHT:
				if(getSpeed() <= SPEED_LIM) {
					applyForwardAcceleration();
				}
				turnRight(delta);
				break;
			case KEEPGOING:
				move(this.lastAction, delta);
				break;
			case FORWARD:
				
				if(getSpeed() <= SPEED_LIM) {
					applyForwardAcceleration();
				}
				break;
			case BACKWARD:
				applyReverseAcceleration();
				break;
			default:
				return;
		}
	}
	
}
