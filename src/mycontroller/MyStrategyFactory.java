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
		System.out.println(strategyName);
		try {
			Class<?> clazz = Class.forName("mycontroller." + strategyName);
			IMoveStrategy strategy = (IMoveStrategy) clazz.newInstance();
			return strategy;
		}
		catch (ClassNotFoundException e) {
			//System.out.println("The given strategy was not given, returning explore strategy");
			IMoveStrategy strategy = new ExploreStrategy();
			return strategy;
		}
	}
	
//	public IMoveStrategy getCompositeStrategy() {
//		
//		return null;
//	}
	
//	public CompositeStrategy createCompositeStrategy() {
//		CompositeStrategy composite = new CompositeStrategy();
//		composite.add(new CollectKeyStrategy());
//		
//		return composite;
//	}
	
}
