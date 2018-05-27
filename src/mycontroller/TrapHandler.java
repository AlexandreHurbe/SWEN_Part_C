/**
 * Project partC for SWEN30006
 * @author Group 28
 * 27/5/2018
 */

package mycontroller;


/**
 * Abstract class for handling traps
 * But not implemented yet 
 * Created for showing extensibility of the subsystem
 */
public abstract class TrapHandler extends Handler{


    public TrapHandler(){
 
    }
    
    public abstract boolean handleTrap();
    
    
}
