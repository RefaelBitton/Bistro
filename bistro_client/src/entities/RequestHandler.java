package entities;

@FunctionalInterface
public interface RequestHandler {
	String handle(Request r);
}
