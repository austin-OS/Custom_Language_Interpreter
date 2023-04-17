package cse3341_core_project.tokenizer;

import java.util.Scanner;

public class Tokenizer {
	
	// Inner class for tokenizing input
	class OTLA_Tokenizer {

		// Scanner object used for tokenization
	    private final Scanner tokenScanner;
	    
	    // Holds the next token to be returned by the tokenizer
	    private String oneTokenLookAhead;

	    // constructor that initializes the OTLA_Tokenizer object with a Scanner object
	    public OTLA_Tokenizer(Scanner currentScanner) {
	        this.tokenScanner = currentScanner;
	        // checks if the scanner has a next token and assign it to nextToken if it does, otherwise set nextToken to null
	        this.oneTokenLookAhead = currentScanner.hasNext() ? currentScanner.next() : null;
	    }

	    // method that checks if there is another token to be returned by the tokenizer
	    public boolean hasNext() {
	        return oneTokenLookAhead != null;
	    }

	    // method that returns the next token and updates the value of nextToken
	    public String next() {
	    	// set current to the value of nextToken
	        String current = oneTokenLookAhead;
	        // check if the scanner has a next token and assign it to nextToken if it does, otherwise set nextToken to null
	        oneTokenLookAhead = tokenScanner.hasNext() ? tokenScanner.next() : null;
	        return current;
	    }
	    
	    // method that skips the characters in the input string s that match the characters in the current nextToken
	    public void skip(String s) {
	    	while(!oneTokenLookAhead.isEmpty() && !s.isEmpty() && (lookAhead().charAt(0)==s.charAt(0))) {
	    		oneTokenLookAhead = oneTokenLookAhead.substring(1,oneTokenLookAhead.length());
	    		s = s.substring(1,s.length());
	    	}
	    	if (oneTokenLookAhead.isEmpty()) {
	    		oneTokenLookAhead = tokenScanner.hasNext()? tokenScanner.next() : null;
	    	}
	    }
	    
	    // method that returns the nextToken without updating its value
	    public String lookAhead() {
	        return oneTokenLookAhead;
	    }
	}
	
	OTLA_Tokenizer inputTokens;
	private TokenKind Top_Token_Kind = null; 
	private String Top_Token = ""; 
	private static boolean isUpperCase(char c) { return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(String.valueOf(c));}
	private static boolean isLowerCase(char c) { return "abcdefghijklmnopqrstuvwxyz".contains(String.valueOf(c));}
	private static boolean isDigit(char c) { return "0123456789".contains(String.valueOf(c));}
	private static boolean isAnotherAcceptableCharacter(char c) { return ";,=![]&|()+-*<>".contains(String.valueOf(c));}
	
	public Tokenizer(Scanner inputScanner) {
		inputTokens = new OTLA_Tokenizer(inputScanner);
	}

	private enum State{
		READY_FOR_FIRST_CHAR_OF_NEXT_TOKEN, 
		GATHER_UC, 
		FINISH_ID, 
		GATHER_LC, 
		GATHER_DIGITS, 
		GOT_AN_EQ, 
		GOT_A_BANG, 
		GOT_AN_AND, 
		GOT_A_BAR, 
		GOT_A_GREATER_THAN, 
		GOT_A_LESS_THAN, 
		FINISHED;
	}

	private void getToken() {
    	State currentState = State.READY_FOR_FIRST_CHAR_OF_NEXT_TOKEN; 
    	Top_Token_Kind = null; 
    	Top_Token = ""; 
    	char nextchar = ' ';
    	int pos = 0;

    	while(currentState != State.FINISHED) {
        	if (inputTokens.hasNext()) {
        		nextchar = pos >= inputTokens.lookAhead().length()? ' ' : inputTokens.lookAhead().trim().charAt(pos++);
        		Top_Token += nextchar;
        	}
        	else {
        		Top_Token_Kind = TokenKind.EOF;	
        		currentState = State.FINISHED;
        	}
	    	switch(currentState){
	    	case READY_FOR_FIRST_CHAR_OF_NEXT_TOKEN:
	    		if (isUpperCase(nextchar)) currentState = State.GATHER_UC;
	    		
	    		else if (isLowerCase(nextchar)) currentState = State.GATHER_LC;
	    		
	    		else if (isDigit(nextchar)) currentState = State.GATHER_DIGITS;
	    		
	    		else if (isAnotherAcceptableCharacter(nextchar)) {
	    			switch(nextchar) {
	    				case ';':
	    					Top_Token_Kind = TokenKind.SEMICOLON;
	    					currentState = State.FINISHED;
	    					break;
	    				case '[':
	    					Top_Token_Kind = TokenKind.OPEN_BRACKET;
	    					currentState = State.FINISHED;
	    					break;
	    				case ']':
	    					Top_Token_Kind = TokenKind.CLOSE_BRACKET;
	    					currentState = State.FINISHED;
	    					break;
	    				case ',':
	    					Top_Token_Kind = TokenKind.COMMA;
	    					currentState = State.FINISHED;
	    					break;
	    				case '(':
	    					Top_Token_Kind = TokenKind.OPEN_PAREN;
	    					currentState = State.FINISHED;
	    					break;
	    				case ')':
	    					Top_Token_Kind = TokenKind.CLOSE_PAREN;
	    					currentState = State.FINISHED;
	    					break;
	    				case '+':
	    					Top_Token_Kind = TokenKind.PLUS;
	    					currentState = State.FINISHED;
	    					break;
	    				case '-':
	    					Top_Token_Kind = TokenKind.MINUS;
	    					currentState = State.FINISHED;
	    					break;
	    				case '*':
	    					Top_Token_Kind = TokenKind.ASTERISK;
	    					currentState = State.FINISHED;
	    					break;
	    				case '=':
	    					currentState = State.GOT_AN_EQ;
	    					break;
	    				case '!':
	    					currentState = State.GOT_A_BANG;
	    					break;
	    				case '&':
	    					currentState = State.GOT_AN_AND;
	    					break;
	    				case '|':
	    					currentState = State.GOT_A_BAR;
	    					break;
	    				case '>':
	    					currentState = State.GOT_A_GREATER_THAN;
	    					break;
	    				case '<':
	    					currentState = State.GOT_A_LESS_THAN;
	    					break;
		    			}
	    		}
	    		else {
	    			Top_Token_Kind = TokenKind.ERROR;
	    			currentState = State.FINISHED;
	    		}
	    		break;
	    	case GATHER_UC:
	    		if (isUpperCase(nextchar)) ;
	    		else if(isDigit(nextchar)) currentState=State.FINISH_ID;
	    		else if(isLowerCase(nextchar)) {
	    			Top_Token_Kind = TokenKind.ERROR;
	    			currentState = State.FINISHED;
	    		}
	    		else {
	    	    	Top_Token = Top_Token.substring(0,Top_Token.length()-1);
	    			Top_Token_Kind=TokenKind.IDENTIFIER;
	    			currentState = State.FINISHED;
	    		}
	    		break;
			case FINISH_ID:
	    		if (isDigit(nextchar)) ;
	    		else if(isLowerCase(nextchar)||isUpperCase(nextchar)) {
	    			Top_Token_Kind = TokenKind.ERROR;
	    			currentState = State.FINISHED;
	    		}
	    		else {
	    			Top_Token = Top_Token.substring(0,Top_Token.length()-1);
	    			Top_Token_Kind=TokenKind.IDENTIFIER;
	    			currentState = State.FINISHED;
	    		}
	    		break;
			case GATHER_LC:
	    		if (isLowerCase(nextchar)) ;
	    		else if(isDigit(nextchar)||isUpperCase(nextchar)) {
	    			Top_Token_Kind = TokenKind.ERROR;
	    			currentState = State.FINISHED;
	    		}
	    		else {
	    			Top_Token = Top_Token.substring(0,Top_Token.length()-1);
	    			Top_Token_Kind = null;
	    			currentState = State.FINISHED;
	    		}
	    		break;
			case GATHER_DIGITS:
	    		if (isDigit(nextchar)) ;
	    		else if(isLowerCase(nextchar)||isUpperCase(nextchar)) {
	    			Top_Token_Kind = TokenKind.ERROR;
	    			currentState = State.FINISHED;
	    		}
	    		else {
	    			Top_Token = Top_Token.substring(0,Top_Token.length()-1);
	    			Top_Token_Kind=TokenKind.INTEGER_CONSTANT;
	    			currentState = State.FINISHED;
	    		}
	    		break;
			case GOT_AN_EQ:
				if (nextchar=='=') {
	    			Top_Token_Kind=TokenKind.EQUALITY_TEST;
	    			currentState = State.FINISHED;
				}
				else {
					Top_Token = Top_Token.substring(0,Top_Token.length()-1);
	    			Top_Token_Kind=TokenKind.ASSIGNMENT_OPERATOR;
	    			currentState = State.FINISHED;
				}
				break;
			case GOT_A_BANG:
				if (nextchar=='=') {
	    			Top_Token_Kind=TokenKind.NOT_EQ_TEST;
	    			currentState = State.FINISHED;
				}
				else {
					Top_Token = Top_Token.substring(0,Top_Token.length()-1);
	    			Top_Token_Kind=TokenKind.BANG;
	    			currentState = State.FINISHED;
				}
				break;
			case GOT_A_GREATER_THAN:
				if (nextchar=='=') {
					Top_Token_Kind=TokenKind.GREATER_THAN_OR_EQUAL_TO;
					currentState = State.FINISHED;
				}
				else {
					Top_Token = Top_Token.substring(0,Top_Token.length()-1);
					Top_Token_Kind=TokenKind.GREATER_THAN;
					currentState = State.FINISHED;
				}
				break;
			case GOT_A_LESS_THAN:
				if (nextchar=='=') {
					Top_Token_Kind=TokenKind.LESS_THAN_OR_EQUAL_TO;
					currentState = State.FINISHED;
				}
				else {
					Top_Token = Top_Token.substring(0,Top_Token.length()-1);
					Top_Token_Kind=TokenKind.LESS_THAN;
					currentState = State.FINISHED;
				}
				break;
			case GOT_AN_AND:
				if (nextchar=='&') {
	    			Top_Token_Kind=TokenKind.AND_OPERATOR;
	    			currentState = State.FINISHED;
				}
				else {
	    			Top_Token_Kind=TokenKind.ERROR;
	    			currentState = State.FINISHED;
				}
				break;
			case GOT_A_BAR:
				if (nextchar=='|') {
	    			Top_Token_Kind=TokenKind.OR_OPERATOR;
	    			currentState = State.FINISHED;
				}
				else {
	    			Top_Token_Kind=TokenKind.ERROR;
	    			currentState = State.FINISHED;
				}
				break;
			default:
				break;
	    	}
    	}
    	

    	
    	if (Top_Token_Kind==null) {
    		switch(Top_Token) {
    		case "program":
    			Top_Token_Kind=TokenKind.PROGRAM;
    			break;
    		case "begin":
    			Top_Token_Kind=TokenKind.BEGIN;
    			break;
    		case "end":
    			Top_Token_Kind=TokenKind.END;
    			break;
    		case "int":
    			Top_Token_Kind=TokenKind.INT;
    			break;
    		case "if":
    			Top_Token_Kind=TokenKind.IF;
    			break;
    		case "then":
    			Top_Token_Kind=TokenKind.THEN;
    			break;
    		case "else":
    			Top_Token_Kind=TokenKind.ELSE;
    			break;
    		case "while":
    			Top_Token_Kind=TokenKind.WHILE;
    			break;
    		case "loop":
    			Top_Token_Kind=TokenKind.LOOP;
    			break;
    		case "read":
    			Top_Token_Kind=TokenKind.READ;
    			break;
    		case "write":
    			Top_Token_Kind=TokenKind.WRITE;
    			break;
    		default:
    			Top_Token_Kind=TokenKind.ERROR;
    			break;
    		}
    	}
    }
    
    public void skipToken() {
    	getToken();
    	inputTokens.skip(Top_Token);
    }

    public TokenKind getTokenKind() {
    	this.getToken();
    	return this.Top_Token_Kind;
    }

    public String getTokenVal() {
    	this.getToken();
    	return this.Top_Token;
    }
}
