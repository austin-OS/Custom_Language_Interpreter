package cse3341_core_project.printer;

import java.io.*;
import cse3341_core_project.parser.ParseTree;

//A printer for the parse tree that outputs the tree in a pretty format.
public class Printer {
	
	// The output stream to print to.
	PrettyPrintStream outputStream;
	
	// The parse tree to print
	ParseTree currentParseTree;
	
	// An internal class representing a pretty print stream that wraps a standard output stream.
	private class PrettyPrintStream{
		
		// The current size of the indentation.
		private int currentIndentationSize;
		
		// The print stream to output to.
		private PrintStream printStream;
		
		// Whether or not to add a newline before printing the next line.
		private boolean newLine;
		
		// Get the indentation string to be added to the start of the line.
		public PrettyPrintStream(PrintStream ps) {
			this.printStream=ps;
			currentIndentationSize = 0;
			this.newLine=true;
		}
		
		// Print a line with the specified string, adding the current indentation if needed.
		private String indentStr(){
			String s = "";
			for (int i = 0;i<(currentIndentationSize*5);i++) {
				s+=" ";
			}
			return s;
		}
		
		// Print a line with the specified string, adding the current indentation if needed.
		public void println(String str) {
			String prefix = this.newLine? indentStr() : "";
			printStream.print(prefix + prettify(str) + "\n");
			this.newLine=true;
		}
		
		// Convert a string to a pretty string by adding indentation to each line.
		private String prettify(String s) {
			while (s.charAt(s.length()-1)=='\n') {
				s=s.substring(0, s.length()-1);
			}
			return s.replace("\n", "\n"+indentStr());
		}
		
		// Print a string, adding the current indentation if needed.
		public void print(String s) {
			if (s.charAt(s.length()-1)=='\n') {
				println(s);
			}
			else {		
				String prefix = this.newLine? indentStr() : "";
				printStream.print(prefix + prettify(s));
				this.newLine=false;
			}
		}
		
		// Increase the current indentation.
		public void increaseIndent() {
			this.currentIndentationSize++;
		}
		
		// Decrease the current indentation.
		public void decreaseIndent() {
			this.currentIndentationSize--;
		}
	}
	
	// Create a new printer for the specified parse tree, using standard output as the output stream.
	public Printer(ParseTree p) { 
		this(System.out, p); 
	}
	
	// Create a new printer for the specified parse tree, using the specified output stream.
	public Printer(PrintStream out, ParseTree fullyInitializedParseTree) {
		this.outputStream = new PrettyPrintStream(out);
		this.currentParseTree = fullyInitializedParseTree;
	}
	
	// Print the core program node of the parse tree.
	public void printCoreProgram() {
		outputStream.println("program");
		outputStream.increaseIndent();
		currentParseTree.goDownLeftBranch();
		printDeclSeq();
		currentParseTree.goUp();
		outputStream.decreaseIndent();
		outputStream.println("begin");
		outputStream.increaseIndent();
		currentParseTree.goDownMiddleBranch();
		printStmtSeq();
		currentParseTree.goUp();
		outputStream.decreaseIndent();
		outputStream.println("end");		
	}
	
	private void printDeclSeq() {
		currentParseTree.goDownLeftBranch();
		printDecl();
		currentParseTree.goUp();
		if (currentParseTree.currentAlternative()==2) {
			currentParseTree.goDownMiddleBranch();
			printDeclSeq();
			currentParseTree.goUp();
		}
	}

	private void printStmtSeq() {
		currentParseTree.goDownLeftBranch();
		printStmt();
		currentParseTree.goUp();
		if (currentParseTree.currentAlternative()==2) {
			currentParseTree.goDownMiddleBranch();
			printStmtSeq();
			currentParseTree.goUp();
		}
	}
	
	private void printDecl() {
		outputStream.print("int ");
		currentParseTree.goDownLeftBranch();
		printIdList();
		currentParseTree.goUp();
		outputStream.println(";");
	}
	
	private void printIdList() {
		currentParseTree.goDownLeftBranch();
		printId();
		currentParseTree.goUp();
		if (currentParseTree.currentAlternative()==2) {
			outputStream.print(", ");
			currentParseTree.goDownMiddleBranch();
			printIdList();
			currentParseTree.goUp();
		}
		
	}
	
	private void printStmt() {
		switch (currentParseTree.currentAlternative()) {
		case 1:
			currentParseTree.goDownLeftBranch();
			printAss();
			break;
		case 2:
			currentParseTree.goDownLeftBranch();
			printIf();
			break;
		case 3:
			currentParseTree.goDownLeftBranch();
			printLoop();
			break;
		case 4:
			currentParseTree.goDownLeftBranch();
			printIn();
			break;
		case 5:
			currentParseTree.goDownLeftBranch();
			printOut();
			break;
		}
		currentParseTree.goUp();
	}
	
	private void printAss() {
		currentParseTree.goDownLeftBranch();
		printId();
		currentParseTree.goUp();
		outputStream.print(" = ");
		currentParseTree.goDownMiddleBranch();
		printExp();
		currentParseTree.goUp();
		outputStream.println(";");
	}
	
	private void printIf() {
		outputStream.print("if ");
		currentParseTree.goDownLeftBranch();
		printCond();
		currentParseTree.goUp();
		outputStream.println(" then");
		outputStream.increaseIndent();
		currentParseTree.goDownMiddleBranch();
		printStmtSeq();
		currentParseTree.goUp();
		outputStream.decreaseIndent();
		
		if (currentParseTree.currentAlternative()==2) {
			outputStream.println("else");
			outputStream.increaseIndent();
			currentParseTree.goDownRightBranch();
			printStmtSeq();
			currentParseTree.goUp();
			outputStream.decreaseIndent();
		}
		
		outputStream.println("end;");
	}
	
	private void printLoop() {
		outputStream.print("while ");
		currentParseTree.goDownLeftBranch();
		printCond();
		currentParseTree.goUp();
		outputStream.println(" loop");
		outputStream.increaseIndent();
		currentParseTree.goDownMiddleBranch();
		printStmtSeq();
		currentParseTree.goUp();
		outputStream.decreaseIndent();
		outputStream.println("end;");
	}
	
	private void printIn() {
		outputStream.print("read ");
		currentParseTree.goDownLeftBranch();
		printIdList();
		currentParseTree.goUp();
		outputStream.println(";");
	}
	
	private void printOut() {
		outputStream.print("write ");
		currentParseTree.goDownLeftBranch();
		printIdList();
		currentParseTree.goUp();
		outputStream.println(";");
	}
	
	private void printCond() {
		currentParseTree.goDownLeftBranch();
		switch (currentParseTree.currentAlternative()) {
		case 1:
			printComp();
			break;
		case 2:
			outputStream.print("!");
			printCond();
			break;
		case 3:
			outputStream.print("[");
			printCond();
			currentParseTree.goUp();
			outputStream.print(" && ");
			currentParseTree.goDownMiddleBranch();
			printCond();
			outputStream.print("]");
			break;
		case 4:
			outputStream.print("[");
			printCond();
			currentParseTree.goUp();
			outputStream.print(" || ");
			currentParseTree.goDownMiddleBranch();
			printCond();
			outputStream.print("]");
			break;
		}
		currentParseTree.goUp();
	}

	private void printComp() {
		outputStream.print("(");
		currentParseTree.goDownLeftBranch();
		printOp();
		currentParseTree.goUp();
		currentParseTree.goDownMiddleBranch();
		printCompOp();
		currentParseTree.goUp();
		currentParseTree.goDownRightBranch();
		printOp();
		currentParseTree.goUp();
		outputStream.print(")");
	}
	
	private void printExp() {
		currentParseTree.goDownLeftBranch();
		printTrm();
		currentParseTree.goUp();
		switch (currentParseTree.currentAlternative()) {
		case 2:
			outputStream.print(" + ");
			currentParseTree.goDownMiddleBranch();
			printExp();
			currentParseTree.goUp();			
			break;
		case 3:
			outputStream.print(" - ");		
			currentParseTree.goDownMiddleBranch();
			printExp();
			currentParseTree.goUp();			
			break;
		}
	}
	
	private void printTrm() {
		currentParseTree.goDownLeftBranch();
		printOp();
		currentParseTree.goUp();		
		if (currentParseTree.currentAlternative()==2) {
			outputStream.print(" * ");			
			currentParseTree.goDownMiddleBranch();
			printTrm();
			currentParseTree.goUp();
		}
	}
	
	private void printOp() {		
		switch (currentParseTree.currentAlternative()) {
		case 1:
			currentParseTree.goDownLeftBranch();
			printNo();
			break;
		case 2:
			currentParseTree.goDownLeftBranch();
			printId();
			break;
		case 3: 
			currentParseTree.goDownLeftBranch();
			outputStream.print("(");
			printExp();
			outputStream.print(")");
		}		
		currentParseTree.goUp();
	}
	
	private void printCompOp() {
		switch(currentParseTree.currentAlternative()) {
		case 1:
			outputStream.print(" != ");
			break;
		case 2:
			outputStream.print(" == ");
			break;
		case 3: 
			outputStream.print(" < ");
			break;
		case 4:
			outputStream.print(" > ");
			break;
		case 5:
			outputStream.print(" <= ");
			break;
		case 6:
			outputStream.print(" >= ");
			break;
		}
	}

	private void printId() {
		outputStream.print(currentParseTree.getCurrentIdName());
	}
	
	private void printNo() {
		outputStream.print(Integer.toString(currentParseTree.getCurrentIntVal()));
	}
}
