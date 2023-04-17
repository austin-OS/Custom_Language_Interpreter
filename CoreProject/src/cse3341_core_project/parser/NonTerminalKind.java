package cse3341_core_project.parser;

//This code declares an enumerated type called "NonTerminalKind".
//Each value in the enumerated type represents a specific kind of non-terminal symbol that might appear in the grammar.
enum NonTerminalKind {
	PROG, // Represents the starting symbol of the grammar (e.g., "program")
	DECL_SEQ, // Represents a sequence of variable declarations (e.g., "int x, y, z;")
	STMT_SEQ, // Represents a sequence of statements (e.g., "x = 1; y = x + 2;")
	DECL, // Represents a single variable declaration (e.g., "int x;")
	ID_LIST, // Represents a list of identifiers (e.g., "x, y, z")
	STMT, // Represents a single statement (e.g., "x = 1;")
	ASS, // Represents an assignment statement (e.g., "x = 1;")
	IF, // Represents an if statement (e.g., "if x > y then x = 1 else y = 1;")
	LOOP, // Represents a loop statement (e.g., "while x < 10 do x = x + 1;")
	IN, // Represents an input statement (e.g., "read x;")
	OUT, // Represents an output statement (e.g., "write x;")
	COND, // Represents a boolean expression (e.g., "x > y")
	COMP, // Represents a comparison operation (e.g., ">")
	EXP, // Represents a mathematical expression (e.g., "x + y")
	TRM, // Represents a term in a mathematical expression (e.g., "x")
	OP, // Represents a mathematical operation (e.g., "+")
	COMP_OP, // Represents a comparison operation (e.g., ">", "<", etc.)
	ID, // Represents an identifier (e.g., "x")
	LET, // Represents the keyword "let"
	NO, // Represents the keyword "no"
	DIGIT; // Represents a digit (0-9)   
}
