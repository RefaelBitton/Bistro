package entities;

//strategy pattern
//using interface because there are some controllers that using same method(handle)
@FunctionalInterface
public interface RequestHandler {
	String handle(Request r);
}
