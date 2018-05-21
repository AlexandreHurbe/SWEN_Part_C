package mycontroller;

import java.util.HashMap;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;

public class MyAIController extends CarController{
	private View currentView = new View(getMap());
	private IMoveStrategy currentStrategy;
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
		

		
	}
	
	
	
}
