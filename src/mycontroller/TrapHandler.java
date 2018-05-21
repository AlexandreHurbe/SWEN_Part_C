package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;


/**
 * Abstract class for handling traps in a path.
 * 
 * @author Ethan Wu
 *
 */
public abstract class TrapHandler {
	protected boolean pathBlocked;
    protected HashMap<Coordinate, MapTile> view;

    public TrapHandler(boolean blocked, HashMap<Coordinate, MapTile> view){
        this.pathBlocked = blocked;
        this.view = view;
    }
    
    public abstract boolean handleTrap(Path path);
    
    
}
