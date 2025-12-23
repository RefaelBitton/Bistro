package entities;

//strategy pattern
//using interface because there are some controllers that using same method(handle)
/**
 * an interface that each method of the database connector implements to allow
 * for the strategy pattern 
 * */
@FunctionalInterface
public interface RequestHandler {
	String handle(Request r);
}
