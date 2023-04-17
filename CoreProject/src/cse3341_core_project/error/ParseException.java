package cse3341_core_project.error;

import cse3341_core_project.tokenizer.*;

@SuppressWarnings("serial")
public class ParseException extends Exception {
	public ParseException(String expected, Tokenizer t) {
		super ("\nParsing Error: Expected: " + expected + " / Unexpected: " + t.getTokenKind().toString());
	}	
}
