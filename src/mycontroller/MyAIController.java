package mycontroller;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import com.badlogic.gdx.scenes.scene2d.ui.List;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

public class MyAIController extends CarController{
	private final float SPEED_LIM = (float) 4.0;
	
	private IMoveStrategy currentStrategy;
	
	private View view;
	private Coordinate currentPos;
	private Float angle;
	private Move move;
	private Move.Action lastAction;

	public MyAIController(Car car) {
		super(car);	

		view = new View(getMap());
		currentPos = new Coordinate(getPosition());
		new Path();
		move = new Move();
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		/*
		 *  Explore the map and record where the key is when sees a key
		 *  
		 */
		init();
		
		Coordinate destination = new Coordinate(8,15);
		view.update(getView(), this.currentPos, this.angle);
		// find the list of path given destination coordinates
		move.update(this.angle, this.currentPos);
		// find the action to take by the car given this path
		Move.Action action = move.followPath(view.findPath(destination));
		// perform this action 
		move(action, delta);
		

		
		
		
	}
	
	private void init() {
		this.currentPos = new Coordinate(getPosition());
		this.angle = getAngle();
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
