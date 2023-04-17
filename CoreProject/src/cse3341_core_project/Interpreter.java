package cse3341_core_project;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import cse3341_core_project.error.*;
import cse3341_core_project.executor.*;
import cse3341_core_project.parser.*;
import cse3341_core_project.printer.*;
import cse3341_core_project.tokenizer.*;

public class Interpreter {
	public static void main(String [] args) {	
		// Set a boolean variable to determine whether pretty print should be used or not
		boolean printChoice = args[args.length-1].equals("print");		
		// Try to open input files and execute the program
		try {
			// Create a scanner object for the program input file
	    	Scanner inputProgramFile = new Scanner ( Paths.get(args[0]) );
	    	// Create a scanner object for the file input
	    	Scanner inputValuesFile = new Scanner ( Paths.get(args[1]) );
	    	// Create a tokenizer object for the program input
	        Tokenizer tokenizer = new Tokenizer ( inputProgramFile );
	        // Create a parser object for the tokenizer
	        Parser parser = new Parser ( tokenizer );
	        // Create a parse tree using the parser
	        ParseTree parseTree = parser.coreProgram();
	        // Create a printer object to print the parse tree to standard output
	        Printer printer = new Printer ( System.out, parseTree );
	        // If pretty print was specified, print the parse tree
	        if (printChoice) printer.printCoreProgram();
	        // Create an executor object to execute the parse tree with the file input
	        Executor executor = new Executor (System.out, parseTree, inputValuesFile );
	        // Execute the parse tree
	        executor.executeCoreProgram();	        
		}
		// Catch any IO exceptions that occur and print an error message
		catch(IOException error) {
			System.err.println("ERROR. Unable to open file: " + error.getMessage());
		}
		// Catch any parse exceptions that occur and print an error message and stack trace
		catch (ParseException error){			
			System.err.println(error.getMessage());
			error.printStackTrace();			
		// Catch any executor exceptions that occur and print an error message and stack trace
		} catch (ExecutorException error) {			
			System.err.println(error.getMessage());
			error.printStackTrace();			
		}
	}
}
