package cse3341_core_project.parser;

import java.util.ArrayList;
import java.util.List;

public class ParseTree {
	// The root node of the parse tree
	private Node rootNode;
	// The current node of the parse tree
	private Node currentNode;	
	// This class represents a node of the parse tree	
	public class Node {
		// Constructor initializes a list of child nodes
		public Node() {
			this.children = new ArrayList <Node>();			
		}
		// The parent node of this node
		private Node parent;
		// The list of child nodes of this node
		private List<Node> children;
		// The non-terminal symbol associated with this node
		private NonTerminalKind nt;
		// The alternative number associated with this node
        private int alt;
        // The integer value associated with this node
        private int idVal;
        // The identifier name associated with this node
        private String idName;
    }
	
	// Constructor initializes the root node and sets the current node to the root node
	public ParseTree() {
		Node n = new Node();
		n.parent=null;
		
		this.rootNode=n;
		this.currentNode=this.rootNode;
	}
	// Sets the current node to the root node
	public void goAllTheWayBackUp() {
		this.currentNode=this.rootNode;
	}
	// Returns the non-terminal symbol associated with the current node
	public NonTerminalKind currentNTNo() {
		return this.currentNode.nt;
	}
	// Returns the alternative number associated with the current node
	public int currentAlternative() {
		return this.currentNode.alt;
	}
	// Sets the current node to the left child of the current node
	public void goDownLeftBranch() {
		this.currentNode=this.currentNode.children.get(0);
	}
	// Creates a new left child node of the current node
	public void createLeftBranch() {
		Node n = new Node();
		n.parent=this.currentNode;
		this.currentNode.children.add(0,n);
	}
	// Sets the current node to the right child of the current node
	public void goDownRightBranch() {
		this.currentNode=this.currentNode.children.get(2);
	}
	// Creates a new right child node of the current node
	public void createRightBranch() {
		Node n = new Node();
		n.parent=this.currentNode;
		this.currentNode.children.add(2,n);
	}
	// Sets the current node to the middle child of the current node
	public void goDownMiddleBranch() {
		this.currentNode=this.currentNode.children.get(1);
	}
	// Creates a new middle child node of the current node
	public void createMiddleBranch() {
		Node n = new Node();
		n.parent=this.currentNode;
		this.currentNode.children.add(1,n);
	} 
	// Sets the current node to the parent of the current node
	public void goUp() {
		this.currentNode=this.currentNode.parent;
	}
	// Returns the integer value associated with the current node
	public int getCurrentIntVal() {
		return this.currentNode.idVal;
	}
	// Sets the integer value associated with the current node
	public void setCurrentIntVal(int x) {
		this.currentNode.idVal=x;
	}
	// Returns the identifier name associated with the current node
	public String getCurrentIdName() {
		return this.currentNode.idName;
	}
	// Set the identifier name of the current node to the specified value.
	public void setCurrentIdName(String x) {
		this.currentNode.idName = x;
	}
	// Set the non-terminal kind of the current node to the specified value.
	public void setNT(NonTerminalKind nt) {
		this.currentNode.nt=nt;
	}
	// Set the alternative number of the current node to the specified value.
	public void setAltNo(int i) {
		this.currentNode.alt=i;
	}
}
