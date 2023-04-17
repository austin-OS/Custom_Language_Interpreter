# CSE 3341 Core Project (Tokenizer, Parser, Executor and Interpreter)

Austin Greer 4/5/2023

## Compiling:

First, in a terminal, use the 'cd' command to navigate to the directory where this project is stored.
For example, if the project is stored in a desktop folder, use the command 'cd desktop/coreproject'.
Then, run the command 'javac -d bin src/cse3341_core_project/\*.java -cp src'
This will compile the source code into executable Java programs in a new folder titled 'bin'.

## Running:

First, in a terminal use the 'cd' command to navigate to the 'bin' directory.
For example, if the project is stored in a desktop folder, use the command 'cd desktop/coreproject/bin'.
Then, to run the compiled program, use the command 'java cse3341_core_project/Interpreter PROGRAM VALUES' with "PROGRAM" and "VALUES" replaced by the appropriate pathnamess a program text file and a values text file.
Also, you can use the command 'java cse3341_core_project/Interpreter PROGRAM VALUES print' to pretty print the program, if wanted.

## Testing:

I've included a compiled folder titled "core" for use with testing.
This folder has already been compiled using the previous steps but also includes a folder titled 'test' with example test files.
There are two different test files for test programs and two different test value files for each program.
The two different example programs were provided by Dr. Neelam Soundarajan on Piazza.

To test, first navigate to the 'core' directory by using the command 'cd coreproject/core'
Then, use this list of commands to test:

- java cse3341_core_project/Interpreter cse3341_core_project/test/testprogram1 cse3341_core_project/test/testvalues1a print
- java cse3341_core_project/Interpreter cse3341_core_project/test/testprogram1 cse3341_core_project/test/testvalues1b print
- java cse3341_core_project/Interpreter cse3341_core_project/test/testprogram2 cse3341_core_project/test/testvalues2a print
- java cse3341_core_project/Interpreter cse3341_core_project/test/testprogram2 cse3341_core_project/test/testvalues2b print
