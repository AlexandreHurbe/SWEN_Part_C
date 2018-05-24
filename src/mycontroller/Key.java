package mycontroller;

import java.awt.List;
import java.util.ArrayList;

import utilities.Coordinate;

public class Key {
	private int keyNumber;
	private Coordinate coord;	
	
	
	public void setKey(int Keynumber, Coordinate coord) {
		this.keyNumber = Keynumber;
		this.coord = coord;
	}
	
	public int getKeyNumber() {
		return keyNumber;
	}
	
	public Coordinate getKeyCoord() {
		return coord;
	}

}
