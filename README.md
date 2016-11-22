# checkers
This program plays the game checkers (or draughts), and tries to win or at least not lose as often as possible (A.I. course KTH project).

It is written in Java, and based on the generalized 3D tic-tac-toe project.

# Description

The board is square, containing 8x8 square cells. Cells are alternatively colored white and green, with the lower left corner colored green. Cells connected by a corner share the same color. Only green cells are used in the game.

The game starts with red (or black) pieces in the top three rows, and white pieces in the bottom three rows. The red player controls the red pieces and the white player controls the white pieces.

There are two kinds of pieces: normal pieces, and kings. Initially, all pieces are normal, but any piece that reaches the last row will become a king.

The players move in turns, alternately. Red player first. In their own turn, a player can move one of their own pieces. All movements are performed diagonally, which means that since all pieces start in green cells, they will always stay in green cells.

Normal pieces can only move forwards (downwards for red pieces, upwards for white pieces). Kings can move in any diagonal direction.

You have one second to make a move.

There are two types of moves:

Normal move

Pieces can be moved diagonally forward, to an adjacent empty square in the next row. Kings can also move backward, to an empty square in the previous row. 
Jumping

If the adjacent square in the next row is occupied by an opponent’s piece, and the square immediately and directly opposite that square is empty, the piece can “jump” over the opponent’s piece. Normal pieces can only jump forward, while kings can also jump backward. The piece that is jumped over is captured and removed from the board. It is possible to perform multiple jumps in the same turn, if when the piece lands, there is another immediate piece that can be jumped, even if the jump is in a different direction. 

Jumping is mandatory: whenever it is possible to perform a jump, it is not possible to perform a normal move. When more than one jump or multiple jump is available, the player can choose which piece to jump with, and which sequence of jumps to perform. It is not necessary to perform the multiple jump that captures the most pieces. However, it is mandatory to perform all the jumps in the chosen sequence: the moved piece cannot end in a position where another jump would be possible.

If a piece moves into the last row, that piece is “crowned” and becomes a king. The piece that becomes a king after a jump, cannot immediately jump backward over another piece.

# Input

A game state which consists of a board, whose turn it is, what the last move was, and how many moves there are left until draw if no captures are made.

# Output

The agent program outputs the next game state in the same format, i.e. what the board looks like after applying your turn, which player is next, what your move was, and how many moves left there are until draw. This is taken care of by the skeleton.

# To play in a single terminal

Assume that “agentX” is your C++ agent, which is in the current directory together with another agent called “checkers”. First, you have to set up a pipe for them to communicate with:

mkfifo pipe

which you do once only.

Now you can make your agents play with

./agentX init verbose < pipe | ./checkers > pipe

The agent that is started with "init" will play as white (exactly one of them have to issue "init").

If you want to test a Java client with one of these you would do

java Main init verbose < pipe | ./agentX > pipe

# Testing in two terminals

To play in two different terminals (so that we can tell who does what in an easier way) you would have to create two pipes with

mkfifo pipe1 pipe2

which you do only once.

In Terminal 1:

./checkers init verbose < pipe1 > pipe2

In Terminal 2:

java Main verbose > pipe1 < pipe2

If you run in two different terminals or directories, make sure that you use the same named pipes for both agents. That is, if agent1 writes to pipe1 (">") then agent2 must read ("<") from the same file. You can specify the full path of the pipe to make sure that you use the same pipes.

Note that if you work on for example computers that use the AFS filesystem, you cannot create named pipes like above. However, you can create your named pipes in the /tmp directory.

mkfifo /tmp/mypipe

and then replace pipe above with /tmp/mypipe

# Windows users

If you want to run your code in Windows with the above technique, you can use Cygwin.

C++

If you want to be able to compile your C++ code in cygwin you should obtain the gcc-g++ package. You can find it easy with the search function on the screen that lets you select additional packages during the installation.

Java

Install the JDK on your Windows machine. In cygwin you need add the path to the java executables with something like

export PATH=\$PATH:/cygdrive/c/Program Files/Java/jdk1.7.0\_40/bin/

# Advanced use

You can run your agent directly in the terminal without any pipe, for example, for debugging/testing purposes. Start the agent

./agent verbose

and then paste in a board state message in the terminal and press ENTER. When you run an agent in verbose mode you see the board state message together with the graphical representation. This what you should pass into your agent. You can also do this by putting such a message into a file (make sure to have an end of line) and do

java Main verbose < file.txt

The start state is represented by the following string:

rrrrrrrrrrrr........wwwwwwwwwwww -1 r 50

where the first 32 characters describe the board state, -1 says that the game just begun, r says that it is RED’s turn and 50 says that it is at least 50 moves until draw.
