package cse3341_core_project.executor;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import cse3341_core_project.error.ExecutorException;
import cse3341_core_project.parser.ParseTree;

public class Executor {
	// Declaring variables
	private ParseTree currentParseTree;
	private PrintStream outputStream;
	private Map<String,RuntimeVariable> runtimeVariables;
	private Scanner inputFile;
	
	// Inner class to define Runtime Variables
	private class RuntimeVariable {
		private int val;
		private boolean defined;
		
		public RuntimeVariable() {
			this.defined=false;
		}

		public boolean defined() {
			return this.defined;
		}
		public int get() {
			return this.val;
		}
		public void set(int num) {
			this.val = num;
			this.defined = true;
		}
		
	}
	// Constructor method
	public Executor(ParseTree currentParseTree, Scanner inputScan) { 
		this(System.out, currentParseTree, inputScan); 
	}
	// Overloaded constructor method
	public Executor(PrintStream out, ParseTree fullyInitializedParseTree, Scanner inputFile) {
		this.outputStream = out;
		this.currentParseTree = fullyInitializedParseTree;
		currentParseTree.goAllTheWayBackUp();
		this.inputFile = inputFile;
		this.runtimeVariables = new HashMap<String,RuntimeVariable>();
	}
	// Method to execute core program
	public void executeCoreProgram() throws ExecutorException {
		currentParseTree.goDownLeftBranch();
		executeDeclSeq();
		currentParseTree.goUp();		
		currentParseTree.goDownMiddleBranch();
		executeStmtSeq();
		currentParseTree.goUp();
	}
	
	private void executeDeclSeq() throws ExecutorException {
		currentParseTree.goDownLeftBranch();
		executeDecl();
		currentParseTree.goUp();
		if (currentParseTree.currentAlternative()==2) {
			currentParseTree.goDownMiddleBranch();
			executeDeclSeq();
			currentParseTree.goUp();
		}
	}
	
	private void executeStmtSeq() throws ExecutorException {
		currentParseTree.goDownLeftBranch();
		executeStmt();
		currentParseTree.goUp();
		
		if (currentParseTree.currentAlternative() == 2) {
			currentParseTree.goDownMiddleBranch();
			executeStmtSeq();
			currentParseTree.goUp();
		}
	}
	
	private void executeDecl() throws ExecutorException {
		currentParseTree.goDownLeftBranch();
		List<String> list = executeIdList();
		currentParseTree.goUp();
		
		for (String id : list) {
			if (runtimeVariables.containsKey(id)) throw new ExecutorException("Variable name: "+id+" has been declared already.");		
			runtimeVariables.put(id, new RuntimeVariable() );
		}
	}
	
	private List<String> executeIdList() {
		List<String> list = new ArrayList<String>();
		
		currentParseTree.goDownLeftBranch();
		list.add(executeId());
		currentParseTree.goUp();
		
		if (currentParseTree.currentAlternative()==2) {
			currentParseTree.goDownMiddleBranch();
			list.addAll(executeIdList());
			currentParseTree.goUp();
		}
		
		return list;
	}
	
	private void executeStmt() throws ExecutorException {
		switch(currentParseTree.currentAlternative()) {
		case 1:
			currentParseTree.goDownLeftBranch();
			executeAss();
			currentParseTree.goUp();
			break;
		case 2:
			currentParseTree.goDownLeftBranch();
			executeIf();
			currentParseTree.goUp();
			break;
		case 3:
			currentParseTree.goDownLeftBranch();
			executeLoop();
			currentParseTree.goUp();
			break;
		case 4:
			currentParseTree.goDownLeftBranch();
			executeIn();
			currentParseTree.goUp();
			break;
		case 5:
			currentParseTree.goDownLeftBranch();
			executeOut();
			currentParseTree.goUp();
			break;
		}
	}
	
	private void executeAss() throws ExecutorException {
		
		currentParseTree.goDownLeftBranch();
		String id = currentParseTree.getCurrentIdName();
		currentParseTree.goUp();
		
		currentParseTree.goDownMiddleBranch();
		int val = executeExp(); 
		currentParseTree.goUp();
		
		if (id == null) throw new ExecutorException("no ID to assign to!");
		if (!runtimeVariables.containsKey(id)) throw new ExecutorException("ID ' " + id + " ' has not been declared!");
		
		runtimeVariables.get(id).set(val);
	}
	
	private void executeIf() throws ExecutorException {
		currentParseTree.goDownLeftBranch();
		boolean cond = executeCond();
		currentParseTree.goUp();
		
		if (cond) {
			currentParseTree.goDownMiddleBranch();
			executeStmtSeq();
			currentParseTree.goUp();
		}
		else if (currentParseTree.currentAlternative()==2) {
			currentParseTree.goDownRightBranch();
			executeStmtSeq();
			currentParseTree.goUp();
		}
	}
	
	private void executeLoop() throws ExecutorException {
		currentParseTree.goDownLeftBranch();
		boolean cond = executeCond();
		currentParseTree.goUp();
		
		while (cond) {
			currentParseTree.goDownMiddleBranch();
			executeStmtSeq();
			currentParseTree.goUp();
			
			currentParseTree.goDownLeftBranch();
			cond = executeCond();
			currentParseTree.goUp();
		}
	}
	
	private void executeIn() throws ExecutorException {
		currentParseTree.goDownLeftBranch();
		List<String> list = executeIdList();
		currentParseTree.goUp();
		
		for (String id : list) {
			if (!inputFile.hasNextInt()) throw new ExecutorException("input file is out of int's to read!");
			if (!runtimeVariables.containsKey(id)) throw new ExecutorException("can't assign value to " + id + " - it's undeclared!");
			runtimeVariables.get(id).set(inputFile.nextInt());
		}
	}
	
	private void executeOut() throws ExecutorException {
		currentParseTree.goDownLeftBranch();
		List<String> list = executeIdList();
		currentParseTree.goUp();
		
		for (String id : list) {
			if (!runtimeVariables.containsKey(id)) throw new ExecutorException("can't write value of " + id + " - it's undeclared!");
			if (!runtimeVariables.get(id).defined()) throw new ExecutorException("can't write value of " + id + " - it's undefined!");
			outputStream.println(id+" = "+runtimeVariables.get(id).get());
		}
	}
	
	private boolean executeCond() throws ExecutorException {
		boolean result = false, c1,c2;
		
		switch (currentParseTree.currentAlternative()) {
		case 1:
			currentParseTree.goDownLeftBranch();
			result = executeComp();
			currentParseTree.goUp();
			break;
		case 2:
			currentParseTree.goDownLeftBranch();
			result = !executeCond();
			currentParseTree.goUp();
			break;
		case 3:
			currentParseTree.goDownLeftBranch();
			c1 = executeCond();
			currentParseTree.goUp();
			
			currentParseTree.goDownMiddleBranch();
			c2 = executeCond();
			currentParseTree.goUp();
			
			result = c1 && c2;
			
			break;
		case 4:
			currentParseTree.goDownLeftBranch();
			c1 = executeCond();
			currentParseTree.goUp();
			
			currentParseTree.goDownMiddleBranch();
			c2 = executeCond();
			currentParseTree.goUp();
			
			result = c1 || c2;
			
			break;
		}
		
		return result;
	
	}

	private boolean executeComp() throws ExecutorException {
		boolean result = false;
		
		currentParseTree.goDownLeftBranch();
		int op1 = executeOp();
		currentParseTree.goUp();
		
		currentParseTree.goDownRightBranch();
		int op2 = executeOp();
		currentParseTree.goUp();
		
		currentParseTree.goDownMiddleBranch();
		
		switch (currentParseTree.currentAlternative()) {
		case 1:
			result = op1 != op2;
			break;
		case 2:
			result = op1 == op2;
			break;
		case 3:
			result = op1 < op2;
			break;
		case 4:
			result = op1 > op2;
			break;
		case 5:
			result = op1 <= op2;
			break;
		case 6:
			result = op1 >= op2;
			break;
		}
		
		currentParseTree.goUp();
		return result;
	
	}
	
	private int executeExp() throws ExecutorException {
		currentParseTree.goDownLeftBranch();
		int result = executeTrm();
		currentParseTree.goUp();
		
		switch (currentParseTree.currentAlternative()){
		case 2:
			currentParseTree.goDownMiddleBranch();
			result += executeExp();
			currentParseTree.goUp();
			break;
		case 3:
			currentParseTree.goDownMiddleBranch();
			result -= executeExp();
			currentParseTree.goUp();
			break;
		}
		return result;
	}
	
	private int executeTrm() throws ExecutorException {
		currentParseTree.goDownLeftBranch();
		int result = executeOp();
		currentParseTree.goUp();
		
		if (currentParseTree.currentAlternative()==2) {
			currentParseTree.goDownMiddleBranch();
			result *= executeTrm();
			currentParseTree.goUp();
		}
		
		return result;
	}
	
	private int executeOp() throws ExecutorException {
		int result = 0;
		String id;
		
		switch(currentParseTree.currentAlternative()) {
		case 1:
			currentParseTree.goDownLeftBranch();
			result=executeNo();
			currentParseTree.goUp();
			break;
		case 2:
			currentParseTree.goDownLeftBranch();
			id = executeId();
			currentParseTree.goUp();
			if (!runtimeVariables.containsKey(id)) throw new ExecutorException(id+" is undeclared!");
			if (!runtimeVariables.get(id).defined()) throw new ExecutorException(id+" is undefined!");
			result = runtimeVariables.get(id).get();
			break;
		case 3:
			currentParseTree.goDownLeftBranch();
			result = executeExp();
			currentParseTree.goUp();
			break;
		}
		
		return result;
	}
	
	private String executeId() {
		return currentParseTree.getCurrentIdName();
	}
	
	private int executeNo() {
		return currentParseTree.getCurrentIntVal();
	}
}
