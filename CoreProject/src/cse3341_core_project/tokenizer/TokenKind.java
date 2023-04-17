package cse3341_core_project.tokenizer;

public enum TokenKind {

	PROGRAM(1),
	BEGIN(2),
	END(3),
	INT(4),
	IF(5),
	THEN(6),
	ELSE(7),
	WHILE(8),
	LOOP(9),
	READ(10),
	WRITE(11),
    SEMICOLON(12),
    COMMA(13),
    ASSIGNMENT_OPERATOR(14),
    BANG(15),
    OPEN_BRACKET(16),
    CLOSE_BRACKET(17),
    AND_OPERATOR(18),
    OR_OPERATOR(19),
    OPEN_PAREN(20),
    CLOSE_PAREN(21),
    PLUS(22),
    MINUS(23),
    ASTERISK(24),
    NOT_EQ_TEST(25),
    EQUALITY_TEST(26),
    GREATER_THAN(28),
    LESS_THAN(27),
    GREATER_THAN_OR_EQUAL_TO(30),
    LESS_THAN_OR_EQUAL_TO(29),
    INTEGER_CONSTANT(31),
    IDENTIFIER(32),
    EOF(33),
    ERROR(34);

    private int testDriverTokenNumber;

    private TokenKind(int number) {
        this.testDriverTokenNumber = number;
    }

    public int testDriverTokenNumber() {
        return this.testDriverTokenNumber;
    }
}
