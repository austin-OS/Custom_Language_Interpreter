package cse3341_core_project.error;

@SuppressWarnings("serial")
public class ExecutorException extends Exception {
	public ExecutorException(String errorMessage) {
		super ("\nRuntime Error: " + errorMessage);
	}	
}
