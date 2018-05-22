package mycontroller;

import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.ui.List;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;

public class MyAIController extends CarController{
	private View currentView = new View();
	private IMoveStrategy currentStrategy;
	private HashMap<Coordinate, MapTile> map = getMap();
	private Coordinate currentPos = new Coordinate(getPosition());
	private List<Key> keys;
	public MyAIController(Car car) {
		super(car);	
		System.out.println(getView().size());
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		/*
		 *  Explore the map and record where the key is when sees a key
		 *  
		 */
		currentView.update(getView(), new Coordinate(getPosition()));
		
//		applyForwardAcceleration();
//		// no wall on the left
//		if(!checkLeftWall()) {
//			turnLeft(delta);
//		}

		
		
	}
	
//	private boolean checkLeftWall() {
//		Coordinate coord;
//		for(int i = 0; i <=2; i++) {
//			coord = new Coordinate(currentPos.x,currentPos.y+i);
//			if(map.get(coord).isType(MapTile.Type.WALL)) {
//				return true;
//			}
//		}
//		return false;
//	}

	
}
