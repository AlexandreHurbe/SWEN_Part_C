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
		currentView.update(getView(), new Coordinate(getPosition()));
		// for testing path finding in View
		Coordinate destination = new Coordinate(8,15);
		HashMap<Coordinate, MyDirection.Direction> path = currentView.findPath(destination);
		move.followPath(path, currentView);

		
		
		
	}
	

	private void move() {
		
	}
}
