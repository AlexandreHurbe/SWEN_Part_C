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
	private View currentView;
	private IMoveStrategy currentStrategy;
	private HashMap<Coordinate, MapTile> map;
	private Coordinate currentPos;
	private List<Key> keys;
	private HashMap<Coordinate, MyDirection.Direction> path;
	private Move move;
	private Move.Action lastAction = Move.Action.FORWARD;
	private final float SPEED_LIM = (float) 4;
	public MyAIController(Car car) {
		super(car);	
		map = getMap();
		currentView = new View(map);
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
		currentView.update(getView(), new Coordinate(getPosition()), getAngle());
		Coordinate destination = new Coordinate(8,15);
		path = currentView.findPath(destination);
		// for testing path finding in View
		Move.Action action = move.followPath(path, currentView);
		System.out.println(action.toString());
		move(action, delta);
		
		if(action != Move.Action.KEEPGOING) {
			lastAction = action;
		}
		
		
		
	}
	

	private void move(Move.Action action, float delta) {
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
