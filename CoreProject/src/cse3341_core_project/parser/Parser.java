package cse3341_core_project.parser;
import cse3341_core_project.error.*;
import cse3341_core_project.tokenizer.*;

public class Parser {
	
	Tokenizer tokenizer;
	ParseTree parseTree;
	
	public Parser(Tokenizer t) {
		this.tokenizer = t;
		this.parseTree = new ParseTree();
	}
	
	public ParseTree coreProgram() throws ParseException{
		parseTree.setNT(NonTerminalKind.PROG);
		parseTree.setAltNo(1);
		if (tokenizer.getTokenKind()!=TokenKind.PROGRAM) throw new ParseException("'program'", tokenizer);
		tokenizer.skipToken();
		parseTree.createLeftBranch();
		parseTree.createMiddleBranch();
		parseTree.goDownLeftBranch();
		parseDeclSeq();
		parseTree.goUp();
		if (tokenizer.getTokenKind()!=TokenKind.BEGIN) throw new ParseException("'begin'", tokenizer);
		tokenizer.skipToken();
		parseTree.goDownMiddleBranch();
		parseStmtSeq();
		parseTree.goUp();
		if (tokenizer.getTokenKind()!=TokenKind.END) throw new ParseException("'end'", tokenizer);
		tokenizer.skipToken();
		return parseTree;
	}
	
	private void parseDeclSeq()  throws ParseException{
		parseTree.setNT(NonTerminalKind.DECL_SEQ);
		parseTree.createLeftBranch();		
		parseTree.goDownLeftBranch();
		parseDecl();
		parseTree.goUp();
		if (tokenizer.getTokenKind()!=TokenKind.BEGIN) {
			parseTree.setAltNo(2);
			parseTree.createMiddleBranch();
			parseTree.goDownMiddleBranch();
			parseDeclSeq();
			parseTree.goUp();
		}
		else {
			parseTree.setAltNo(1);
		}
	}
	
	private void parseStmtSeq() throws ParseException{
		parseTree.setNT(NonTerminalKind.STMT_SEQ);
		parseTree.setAltNo(1);
		parseTree.createLeftBranch();
		parseTree.goDownLeftBranch();
		parseStmt();
		parseTree.goUp();
		if ((tokenizer.getTokenKind() != TokenKind.END) && (tokenizer.getTokenKind() != TokenKind.ELSE)) {
			parseTree.setAltNo(2);
			parseTree.createMiddleBranch();
			parseTree.goDownMiddleBranch();
			parseStmtSeq();
			parseTree.goUp();
		}
	}
	
	private void parseDecl() throws ParseException{
		parseTree.setNT(NonTerminalKind.DECL);
		parseTree.setAltNo(1);	
		if (tokenizer.getTokenKind()!=TokenKind.INT) throw new ParseException("'int'", tokenizer);
		tokenizer.skipToken();
		parseTree.createLeftBranch();
		parseTree.goDownLeftBranch();
		parseIdList();
		parseTree.goUp();
		if (tokenizer.getTokenKind()!=TokenKind.SEMICOLON) throw new ParseException("';'", tokenizer);
		tokenizer.skipToken();
	}
	
	private void parseIdList() throws ParseException{
		parseTree.setNT(NonTerminalKind.ID_LIST);
		parseTree.setAltNo(1);
		parseTree.createLeftBranch();
		parseTree.goDownLeftBranch();
		parseId();
		parseTree.goUp();
		if (tokenizer.getTokenKind()==TokenKind.COMMA) {
			tokenizer.skipToken();
			parseTree.setAltNo(2);
			parseTree.createMiddleBranch();
			parseTree.goDownMiddleBranch();
			parseIdList();
			parseTree.goUp();
		}
	}
	
	private void parseStmt() throws ParseException{
		parseTree.setNT(NonTerminalKind.STMT);	
		parseTree.createLeftBranch();
		switch (tokenizer.getTokenKind()) {
		case IDENTIFIER:
			parseTree.setAltNo(1);
			parseTree.goDownLeftBranch();
			parseAss();
			break;
		case IF:
			parseTree.setAltNo(2);
			parseTree.goDownLeftBranch();
			parseIf();
			break;
		case WHILE:
			parseTree.setAltNo(3);
			parseTree.goDownLeftBranch();
			parseLoop();
			break;
		case READ:
			parseTree.setAltNo(4);
			parseTree.goDownLeftBranch();
			parseIn();
			break;
		case WRITE: 
			parseTree.setAltNo(5);
			parseTree.goDownLeftBranch();
			parseOut();
			break;
		default:
			throw new ParseException("<id>, 'if', 'while', 'read', 'write'", tokenizer);
		}
		parseTree.goUp();
	}
	
	private void parseAss() throws ParseException{	
		parseTree.setNT(NonTerminalKind.ASS);
		parseTree.setAltNo(1);
		parseTree.createLeftBranch();
		parseTree.createMiddleBranch();
		parseTree.goDownLeftBranch();
		parseId();
		parseTree.goUp();
		if (tokenizer.getTokenKind()!=TokenKind.ASSIGNMENT_OPERATOR) throw new ParseException("'='", tokenizer);
		tokenizer.skipToken();
		parseTree.goDownMiddleBranch();
		parseExp();
		parseTree.goUp();
		if (tokenizer.getTokenKind()!=TokenKind.SEMICOLON) throw new ParseException("';'", tokenizer);
		tokenizer.skipToken();
	}
	
	private void parseIf() throws ParseException{
		parseTree.setNT(NonTerminalKind.IF);
		parseTree.setAltNo(1);
		if (tokenizer.getTokenKind()!=TokenKind.IF) throw new ParseException("'if'", tokenizer);
		tokenizer.skipToken();
		parseTree.createLeftBranch();
		parseTree.createMiddleBranch();
		parseTree.goDownLeftBranch();
		parseCond();
		parseTree.goUp();
		if (tokenizer.getTokenKind()!=TokenKind.THEN) throw new ParseException("'then'", tokenizer);
		tokenizer.skipToken();
		parseTree.goDownMiddleBranch();
		parseStmtSeq();
		parseTree.goUp();
		if (tokenizer.getTokenKind()==TokenKind.ELSE) {
			tokenizer.skipToken();
			parseTree.setAltNo(2);
			parseTree.createRightBranch();
			parseTree.goDownRightBranch();
			parseStmtSeq();
			parseTree.goUp();
		}
		if (tokenizer.getTokenKind()!=TokenKind.END) throw new ParseException("'end'", tokenizer);
		tokenizer.skipToken();
		if (tokenizer.getTokenKind()!=TokenKind.SEMICOLON) throw new ParseException("';'", tokenizer);
		tokenizer.skipToken();
	}
	
	private void parseLoop() throws ParseException{
		parseTree.setNT(NonTerminalKind.LOOP);
		parseTree.setAltNo(1);	
		parseTree.createLeftBranch();
		parseTree.createMiddleBranch();
		if (tokenizer.getTokenKind()!=TokenKind.WHILE) throw new ParseException("'while'", tokenizer);
		tokenizer.skipToken();
		parseTree.goDownLeftBranch();
		parseCond();
		parseTree.goUp();
		if (tokenizer.getTokenKind()!=TokenKind.LOOP) throw new ParseException("'loop'", tokenizer);
		tokenizer.skipToken();
		parseTree.goDownMiddleBranch();
		parseStmtSeq();
		parseTree.goUp();
		if (tokenizer.getTokenKind()!=TokenKind.END) throw new ParseException("'end'", tokenizer);
		tokenizer.skipToken();
		if (tokenizer.getTokenKind()!=TokenKind.SEMICOLON) throw new ParseException("';'", tokenizer);
		tokenizer.skipToken();
	}

	private void parseIn() throws ParseException{
		parseTree.setNT(NonTerminalKind.IN);
		parseTree.setAltNo(1);		
		if (tokenizer.getTokenKind()!=TokenKind.READ) throw new ParseException("'read'", tokenizer);
		tokenizer.skipToken();
		parseTree.createLeftBranch();
		parseTree.goDownLeftBranch();
		parseIdList();
		parseTree.goUp();
		if (tokenizer.getTokenKind()!=TokenKind.SEMICOLON) throw new ParseException("';'", tokenizer);
		tokenizer.skipToken();
	}

	private void parseOut() throws ParseException{
		parseTree.setNT(NonTerminalKind.OUT);
		parseTree.setAltNo(1);		
		if (tokenizer.getTokenKind()!=TokenKind.WRITE) throw new ParseException("'write'", tokenizer);
		tokenizer.skipToken();
		parseTree.createLeftBranch();
		parseTree.goDownLeftBranch();
		parseIdList();
		parseTree.goUp();
		if (tokenizer.getTokenKind()!=TokenKind.SEMICOLON) throw new ParseException("';'", tokenizer);
		tokenizer.skipToken();
	}
	
	private void parseCond() throws ParseException{
		parseTree.setNT(NonTerminalKind.COND);	
		parseTree.createLeftBranch();
		switch (tokenizer.getTokenKind()) {
		case OPEN_PAREN:
			parseTree.setAltNo(1);
			parseTree.goDownLeftBranch();
			parseComp();
			parseTree.goUp();
			break;
		case BANG:
			parseTree.setAltNo(2);
			parseTree.goDownLeftBranch();
			tokenizer.skipToken();
			parseCond();
			parseTree.goUp();
			break;
		case OPEN_BRACKET:
			tokenizer.skipToken();
			parseTree.goDownLeftBranch();
			parseCond();
			parseTree.goUp();
			if (tokenizer.getTokenKind()!=TokenKind.AND_OPERATOR) {
				parseTree.setAltNo(3);
				tokenizer.skipToken();
			}
			else if (tokenizer.getTokenKind()!=TokenKind.OR_OPERATOR) {
				parseTree.setAltNo(4);
				tokenizer.skipToken();
			}
			else {
				throw new ParseException("'&&', '|'", tokenizer);
			}
			parseTree.createMiddleBranch();
			parseTree.goDownMiddleBranch();
			parseCond();
			parseTree.goUp();
			if (tokenizer.getTokenKind()!=TokenKind.CLOSE_BRACKET) throw new ParseException("']'", tokenizer);
			tokenizer.skipToken();
			break;
		default:
			throw new ParseException("'(', '!', '['", tokenizer);
		}
		
	}

	private void parseComp() throws ParseException{
		parseTree.setNT(NonTerminalKind.COMP);
		parseTree.setAltNo(1);		
		if (tokenizer.getTokenKind()!=TokenKind.OPEN_PAREN) throw new ParseException("'('", tokenizer);
		tokenizer.skipToken();
		parseTree.createLeftBranch();
		parseTree.createMiddleBranch();
		parseTree.createRightBranch();
		parseTree.goDownLeftBranch();
		parseOp();
		parseTree.goUp();
		parseTree.goDownMiddleBranch();
		parseCompOp();
		parseTree.goUp();
		parseTree.goDownRightBranch();
		parseOp();
		parseTree.goUp();
		if (tokenizer.getTokenKind()!=TokenKind.CLOSE_PAREN) throw new ParseException("')'", tokenizer);
		tokenizer.skipToken();
	}
	
	private void parseExp() throws ParseException{
		parseTree.setNT(NonTerminalKind.EXP);
		parseTree.setAltNo(1);	
		parseTree.createLeftBranch();
		parseTree.goDownLeftBranch();
		parseTrm();
		parseTree.goUp();
		if (tokenizer.getTokenKind()==TokenKind.PLUS) {
			tokenizer.skipToken();
			parseTree.setAltNo(2);
			parseTree.createMiddleBranch();
			parseTree.goDownMiddleBranch();
			parseExp();
			parseTree.goUp();
		}
		else if (tokenizer.getTokenKind()==TokenKind.MINUS) {
			tokenizer.skipToken();
			parseTree.setAltNo(3);
			parseTree.createMiddleBranch();
			parseTree.goDownMiddleBranch();
			parseExp();
			parseTree.goUp();
		}
	}
	
	private void parseTrm() throws ParseException{
		parseTree.setNT(NonTerminalKind.TRM);
		parseTree.setAltNo(1);	
		parseTree.createLeftBranch();
		parseTree.goDownLeftBranch();
		parseOp();
		parseTree.goUp();
		if (tokenizer.getTokenKind()==TokenKind.ASTERISK) {
			tokenizer.skipToken();
			parseTree.setAltNo(2);
			parseTree.createMiddleBranch();
			parseTree.goDownMiddleBranch();
			parseTrm();
			parseTree.goUp();
		}
	}
	
	private void parseOp() throws ParseException{
		parseTree.setNT(NonTerminalKind.OP);	
		parseTree.createLeftBranch();
		switch (tokenizer.getTokenKind()) {
		case INTEGER_CONSTANT:
			parseTree.setAltNo(1);
			parseTree.goDownLeftBranch();
			parseNo();
			break;
		case IDENTIFIER:
			parseTree.setAltNo(2);
			parseTree.goDownLeftBranch();
			parseId();
			break;
		case OPEN_PAREN:
			if (tokenizer.getTokenKind()!=TokenKind.OPEN_PAREN) throw new ParseException("'('", tokenizer);
			tokenizer.skipToken();
			parseTree.goDownLeftBranch();
			parseExp();
			if (tokenizer.getTokenKind()!=TokenKind.CLOSE_PAREN) throw new ParseException("')'", tokenizer);
			tokenizer.skipToken();
			break;
		default:
			throw new ParseException("<no>, <id>, '('", tokenizer);
		}	
		parseTree.goUp();
	}
	
	private void parseCompOp() throws ParseException{
		parseTree.setNT(NonTerminalKind.COMP_OP);
		switch (tokenizer.getTokenKind()) {
		case NOT_EQ_TEST:
			parseTree.setAltNo(1);
			break;
		case EQUALITY_TEST:
			parseTree.setAltNo(2);
			break;
		case LESS_THAN:
			parseTree.setAltNo(3);
			break;
		case GREATER_THAN:
			parseTree.setAltNo(4);
			break;
		case LESS_THAN_OR_EQUAL_TO:
			parseTree.setAltNo(5);
			break;
		case GREATER_THAN_OR_EQUAL_TO:
			parseTree.setAltNo(6);
			break;
		default:
			throw new ParseException("'!=', '==', '<', '>', '<=', '>='", tokenizer);
		}
		tokenizer.skipToken();
	}
	
	private void parseId() throws ParseException{
		parseTree.setNT(NonTerminalKind.ID);
		parseTree.setCurrentIdName(tokenizer.getTokenVal());
		tokenizer.skipToken();
	}
	
	private void parseNo() throws ParseException{
		parseTree.setNT(NonTerminalKind.NO);
		parseTree.setCurrentIntVal(Integer.parseInt(tokenizer.getTokenVal()));
		tokenizer.skipToken();
	}
	
}
