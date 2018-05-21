package mycontroller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

/* 
 * Get the view of tiles around my car
 *  
 */
public class View  {
	private HashMap<Coordinate, MapTile> currentView;
	
	public View(HashMap<Coordinate, MapTile> currentView) {
		this.currentView = currentView;
	}
	
	public void lookForKey(List<Key> keys){
		Iterator<Coordinate> allTiles = this.currentView.keySet().iterator();
		MapTile tile;
		Coordinate coord;
		int keyNumber;
		
		while(allTiles.hasNext()) {
			coord = allTiles.next();
			tile = this.currentView.get(coord);
			if(tile instanceof LavaTrap) {
				keyNumber = ((LavaTrap) tile).getKey();
				if(keyNumber > 0) {
					Key key = new Key();
					key.setKey(keyNumber, coord);
					keys.add(key);
				}
			}
		}
	}


}
