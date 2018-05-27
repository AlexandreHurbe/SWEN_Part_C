package mycontroller;

import java.util.HashMap;
import java.util.Set;

public class HandlerLibrary {
	private static HandlerLibrary MyHandlerLib = new HandlerLibrary();
	
	private HashMap<String, Handler> handlers;
	
	private HandlerLibrary() {}
	
	public static HandlerLibrary getInstance() {
		return MyHandlerLib;
	}
	
	public void initialise() {
		this.handlers = new HashMap<String, Handler>();
		Handler wallHandler = new WallHandler();
		MyHandlerLib.registerHandler("wallHandler", wallHandler);
		
		
	}
	
	public boolean registerHandler(String name, Handler handler) {
		this.handlers.put(name, handler);
		if(this.handlers.containsValue(handler)) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean deregisterHandler(String name) {
		this.handlers.remove(name);
		if(this.handlers.containsKey(name)) {
			return false;
		}else {
			return true;
		}
	}
	
	public Handler getHandler(String name) {

		return this.handlers.get(name);
		
	}
	
	public Set<String> availableHandlers(){
		return this.handlers.keySet();
	}
}
