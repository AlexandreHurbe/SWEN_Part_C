package mycontroller;

import tiles.MapTile.Type;
import utilities.Coordinate;

public class WallHandler extends Handler{
	public WallHandler() {}
	
	public void handleMapTile() {
		
	}
	// check the tile is next to walls
	public int handleMapTile(Coordinate end) {
		int wall = 0;
		Coordinate current;
		for(int i = -1; i < 1; i++) {
			for (int j = -1; j < 1 ; j++) {
				current = new Coordinate(end.x+i, end.y+j);
				if(i != j) {
					if(myMap.getMap().get(current) != null) {
						if(myMap.getMap().get(current).isType(Type.WALL)) {
							wall +=1;
						}
					}
				}
			}
		}
		return wall;
	}
}
