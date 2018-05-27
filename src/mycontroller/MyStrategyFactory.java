package mycontroller;
import mycontroller.IMoveStrategy;
/* 
 * factory produces strategies for MyAIController
 * 
 */
public class MyStrategyFactory {
	private static MyStrategyFactory instance = new MyStrategyFactory();
	
	
	public static MyStrategyFactory getInstance() {
		return instance;
	}
	
	public IMoveStrategy getMoveStrategy(String strategyName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

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
