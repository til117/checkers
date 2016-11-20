import java.util.*;

public class Player {
    
    public static final int milliseconds=1000000;
    int elementCount;
    int depth;
    
    /**
     * Performs a move
     *
     * @param pState
     *            the current state of the board
     * @param pDue
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState pState, final Deadline pDue) {

        Vector<GameState> lNextStates = new Vector<GameState>();
        pState.findPossibleMoves(lNextStates);

        if (lNextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(pState, new Move());
        }
        else if(lNextStates.size()==1){
            return lNextStates.elementAt(0);
        }
        
        //variables for setting depth
        depth=0;
        elementCount=1;
        ArrayList<GameState> originalBranch=new ArrayList<GameState>();
        originalBranch.add(pState);
        setDepth(originalBranch, pDue);
        
        //get best index
        int bestIndex=getScore(pState,depth);
        
        return lNextStates.elementAt(bestIndex);
        
        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */
        //Random random = new Random();
        //return lNextStates.elementAt(random.nextInt(lNextStates.size()));
    }
    
    public int getScore(GameState pState, int movesLeft){
        
        Vector<GameState> lNextStates=new Vector<GameState>();
        pState.findPossibleMoves(lNextStates);
        
        int bestScore= Integer.MIN_VALUE;
        int bestIndex=-1;
        int pColor= pState.getNextPlayer();
        int index=0;
        
        for (GameState cState : lNextStates){
            
            int score=0;
  
            //check of the game is finished
            if(cState.isEOG()){
                
                //if the color is the same as the one who won, the score is inf
                if((pColor== Constants.CELL_RED && cState.isRedWin())||(pColor== Constants.CELL_WHITE && cState.isWhiteWin())){
                    score=1000;
                    //score=Integer.MAX_VALUE;
                    bestScore=score;
                    bestIndex=index;
                    break;
                }
                
                //if the color is differetn as the one who won, the score is a small value
                else if((pColor==Constants.CELL_RED && cState.isWhiteWin())||(pColor==Constants.CELL_WHITE && cState.isRedWin())){
                    //score=Integer.MIN_VALUE;
                    score=-1000;
                }
                
                //if its a draw, the score is zero
                else{
                    score=0;
                }
            }
            
            //if the game is not finished
            else{
                
                if(movesLeft>0 || cState.getMove().isJump()){
                    score= -getScore(cState,movesLeft -1);
                }
                
                //score if there are no possible moves
                else{
                    int redScore=0;
                    int whiteScore=0;
                    
                    for(int i=0;i<cState.NUMBER_OF_SQUARES;i++){
                        int stateSquare=cState.get(i);
                        int squareRow=cState.cellToRow(i);
                        int squareCol=cState.cellToCol(i);
                        
                        //check if the cell is red
                        if((stateSquare & Constants.CELL_RED) != 0){
                            
                            redScore +=10;
                            
                            //check if the cell is a king
                            if((stateSquare & Constants.CELL_KING) != 0){
                                redScore +=5;
                            }
                            
                            //check if you are in a white row
                            redScore += (7-squareRow);
                            
                            //you get extra points if you go to the other side ie kings cell
                            if(i>=28 && i<=31){
                                redScore +=1;
                            }
                            
                            //extra points for been in the middle columns
                            if(squareCol>=2 && squareCol <=5){
                                redScore +=1;
                            }
                        }
                        
                        //check if the cell is white
                        else if((stateSquare & Constants.CELL_WHITE) != 0){
                            
                            whiteScore +=10;
                            
                            //check if the cell is a king
                            if((stateSquare & Constants.CELL_KING) != 0){
                                whiteScore +=5;
                            }
                            
                            //check if you are in a red row
                            whiteScore += (squareRow);
                            
                            //you get extra points if you go to the other side ie kings cell
                            if(i>=0 && i<=3){
                                whiteScore +=1;
                            }
                            
                            //extra points for been in the middle columns
                            if(squareCol>=2 && squareCol <=5){
                                whiteScore +=1;
                            }                
                        }
                    }
                    
                    //get the final score
                    if(pColor == Constants.CELL_RED){
                        score += redScore-whiteScore;
                    }
                    
                    else if(pColor == Constants.CELL_WHITE){
                        score += whiteScore-redScore;
                    }
                }
            }
            
            if(score >= bestScore){
                bestScore=score;
                bestIndex=index;
            }
            
            index++;
        }
        
        if(movesLeft == depth){
            return bestIndex;
        }
        else{
            return bestScore;
        }
    }
  
    
//Used for setting time limit  
    private boolean enoughTime(Deadline pDue){
        double timePerElement=0.01;
        boolean decision= (timePerElement*elementCount*milliseconds) < (pDue.timeUntil());
        return decision;
    }
    
    public void setDepth(final ArrayList<GameState> stateBranch, Deadline pDue){
        
        ArrayList<GameState> nextBranch =new ArrayList<GameState>();
        
        for(GameState parentState : stateBranch){
            
            if(!parentState.isEOG()){
                Vector<GameState> lNextStates = new Vector<GameState>();
                parentState.findPossibleMoves(lNextStates);
                elementCount += lNextStates.size();
                nextBranch.addAll(lNextStates);
            }
            
            if(!enoughTime(pDue)){
                break;
            }
        }
        
        if(enoughTime(pDue) && !nextBranch.isEmpty()){
            depth++;
            setDepth(nextBranch, pDue);
        }
    } 
}
