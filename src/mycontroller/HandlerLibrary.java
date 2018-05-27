package mycontroller;

import java.util.HashMap;

public class HandlerLibrary {
	private static HandlerLibrary MyHandlerLib = new HandlerLibrary();
	
	private HashMap<String, Handler> handlers;
	
	private HandlerLibrary() {}
	
	public static HandlerLibrary getInstance() {
		return MyHandlerLib;
	}
	
	
}
