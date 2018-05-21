package mycontroller;
/* 
 * factory produces strategies for MyAIController
 * 
 */
public class MyStrategyFactory {
	protected static MyStrategyFactory instance;

	public static MyStrategyFactory getInstance() {
		return instance;
	}
	
	public IMoveStrategy getMoveStrategy(String strategyName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String classname = strategyName;
		IMoveStrategy strategy = (IMoveStrategy)Class.forName(classname).newInstance();
		return strategy;
	}
	
	public IMoveStrategy getCompositeStrategy() {
		
		return null;
	}
	
	public IMoveStrategy createCompositeStrategy() {

		return (IMoveStrategy) new CompositeStrategy();
	}
	
}
