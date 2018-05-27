package mycontroller;
import mycontroller.IMoveStrategy;
/* 
 * factory produces strategies for MyAIController
 * 
 */
public class MyStrategyFactory {
	
	public IMoveStrategy getMoveStrategy(String strategyName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		// create strategy with this name 
		try {
			Class<?> clazz = Class.forName("mycontroller." + strategyName);
			IMoveStrategy strategy = (IMoveStrategy) clazz.newInstance();
			return strategy;
		}
		catch (ClassNotFoundException e) {
			
			IMoveStrategy strategy = new ExploreStrategy();
			return strategy;
		}
	}
	
	
}
