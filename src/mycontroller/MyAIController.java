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
	private final float SPEED_LIM = (float) 3.0;
	
	private IMoveStrategy currentStrategy;
	
	private View view = new View(getMap());
	private Coordinate currentPos;
	private Float angle;
	private Move move = new Move();
	private Move.Action lastAction;
	private HashMap<Coordinate, MyDirection.Direction> path;
	private ExploreStrategy explore = new ExploreStrategy();
	private Coordinate destination;
	public MyAIController(Car car) {
		super(car);	
		
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		/*
		 *  Explore the map and record where the key is when sees a key
		 *  
		 */
		init();
//		Coordinate destination = new Coordinate(5,10);
		this.view.update(getView(), this.currentPos, this.angle);
		this.destination = this.explore.exploreKey(view);
//		Coordinate destination = new Coordinate(5,17);
		path = view.findPath(destination);
		// find the list of path given destination coordinates
		this.move.update(this.angle, this.currentPos);
		// find the action to take by the car given this path
		
		Move.Action action = move.followPath(path);
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
