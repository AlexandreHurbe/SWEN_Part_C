package mycontroller;

public abstract class ExploreStrategy implements IMoveStrategy {

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
	
	public abstract void explore(View currentView, Float health);
		
}
