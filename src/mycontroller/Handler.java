package mycontroller;

public abstract class Handler {
	MyMap myMap = MyMap.getInstance();
	public Handler() {}
	// handle each types of tiles
	public abstract void handleMapTile();
}
