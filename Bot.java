import java.io.PrintWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Bot {

	Tafl tafl;
	ArrayList<Moves> possibleMoves = new ArrayList<Moves>();
	int hilightX, hilightY;

	public Bot(Tafl _tafl){
		tafl = _tafl;
	}

	public void takeTurn(){
		p("AI's Turn");

		//for each piece on the tafl.mainBoard
		//for each possible move that the piece can make
		//record to a list of possible moves
		//
		// AI logic
		// 
		// 	for each move white can make,
		//		for each move black can make,
		// 			find most optimal move for black,
		// 			and take that move, then use that score, pass it up the tree for white
		// 	order white moves into most optimal based on the optimal black moves.

		Moves selectedMove = getBestWhiteMove(tafl.mainBoard);

		tafl.myGUI.repaint();
		tafl.update(selectedMove.startX,selectedMove.startY);
		tafl.myGUI.repaint();
		tafl.update(selectedMove.endX,selectedMove.endY);

		hilightX = selectedMove.endX;
		hilightY = selectedMove.endY;
		tafl.myGUI.repaint();

		p("AI Happy");
	}

	public Moves getBestWhiteMove(Board testBoard){
		possibleMoves = new ArrayList<Moves>();

		possibleMoves = getPossibleMoves(testBoard, 'w');

		for (int i=0; i<possibleMoves.size(); i++) {
			Board tempBoard = new Board(tafl.boardWidth, tafl.boardHeight, testBoard.pieces);
			tafl.simulateMove(possibleMoves.get(i),tempBoard);
			possibleMoves.get(i).score = getWorstBlackMove(tempBoard).score ;
		}

		Collections.sort(possibleMoves, new Comparator<Moves>() {
	        @Override public int compare(Moves m1, Moves m2) {
	            return m2.score - m1.score; // Ascending
	        }

	    });

		int bestScore = possibleMoves.get(0).score;

		int numberToSelectFrom = 0;

		while(possibleMoves.get(numberToSelectFrom).score == bestScore && numberToSelectFrom < possibleMoves.size()){
			numberToSelectFrom++;
		}

		return possibleMoves.get(r(numberToSelectFrom));

	}

	public Moves getWorstBlackMove(Board tempBoard){
		ArrayList<Moves> possibleReturnMoves = new ArrayList<Moves>();
		possibleReturnMoves = getPossibleMoves(tempBoard, 'b');

		possibleReturnMoves = evaluateMoves(possibleReturnMoves, tempBoard);

		Collections.sort(possibleReturnMoves, new Comparator<Moves>() {
	        @Override public int compare(Moves m1, Moves m2) {
	            return m2.score - m1.score; // Ascending
	        }
	    });

	    Moves worstMove = possibleReturnMoves.get(possibleReturnMoves.size()-1);

	    return worstMove;
	}

	public ArrayList<Moves> getPossibleMoves(Board testBoard, char colour){

		ArrayList<Moves> tempMoves = new ArrayList<Moves>();

		for (int i=0; i<tafl.boardWidth; i++) {
			for (int j=0; j<tafl.boardHeight; j++) {
				if((colour == 'w' && testBoard.isWhite(i,j)) || (colour == 'b' && testBoard.isBlack(i,j))){
					char tempPiece = testBoard.get(i,j);
					for (int k=0; k<tafl.boardWidth; k++) {
						for (int l=0; l<tafl.boardHeight; l++) {
							if(testBoard.validMove(i, j, k, l, tempPiece)){ //move piece
								tempMoves.add(new Moves(i,j,k,l, tempPiece));
							}
						}
					}
				}
			}
		}

		return tempMoves;
	}

	public ArrayList<Moves> evaluateMoves(ArrayList<Moves> movesToEvaluate, Board inputBoard){
		for (int moveNum=0; moveNum<movesToEvaluate.size(); moveNum++) {
			Board testBoard = new Board(tafl.boardWidth, tafl.boardHeight, inputBoard.pieces);
			Moves thisMove = movesToEvaluate.get(moveNum);
			tafl.simulateMove(thisMove, testBoard);

			if(testBoard.checkWhiteWin()){
				thisMove.score = 100;
				// p("this move won the game");
			}else if(testBoard.checkBlackWin()){
				thisMove.score = -100;
			}else{

				// find king piece
				// 
				
				int king_x = 0;
				int king_y = 0;

				for (int i=0; i<8; i++) {
					for (int j=0; j<8; j++) {
						if(testBoard.get(i,j)=='k'){
							king_x = i;
							king_y = j;
							i = 9;
							j = 9;
						}
					}
				}

				// p("checking king movements");
				int escapes = 0;
				if(testBoard.validMove(king_x,king_y,0,0,'k')){
					escapes+=1;
				}
				if(testBoard.validMove(king_x,king_y,0,tafl.boardHeight-1,'k')){
					escapes+=1;
				}
				if(testBoard.validMove(king_x,king_y,tafl.boardWidth-1,0,'k')){
					escapes+=1;
				}
				if(testBoard.validMove(king_x,king_y,tafl.boardWidth-1,tafl.boardHeight-1,'k')){
					escapes+=1;
				}

				// p("escapes: "+escapes);

				if(escapes>1){
					thisMove.score = 90;
				}else if(escapes == 1){
					thisMove.score = 50;
				}
				// p("king movements done\n\n");
			}

			for (int i=0; i<tafl.boardWidth; i++) {
				for (int j=0; j<tafl.boardHeight; j++) {
					int numWhite = 0;
					int numBlack = 0;
					if(testBoard.isWhite(i,j)){
						numWhite++;
					}else if(testBoard.isBlack(i,j)){
						numBlack++;
					}
					// p("num White: "+numWhite);
					// p("num Black: "+numBlack);

					thisMove.score+=numWhite-numBlack;
				}
			}
			// p("checking number of pieces left done \n\n");

		}

		return movesToEvaluate;

		// p("selecting move");
	}

	public static void s(int x){try{Thread.sleep(x);}catch(Exception e){}}
	public static void p(Object o){System.out.println(o);}
	public static int r(int x){return (int)(Math.random()*x);}
}