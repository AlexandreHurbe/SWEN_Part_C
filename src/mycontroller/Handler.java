package mycontroller;

public abstract class Handler {
	MyMap myMap = MyMap.getInstance();
	public Handler() {}
	
	public abstract void handleMapTile();
}
