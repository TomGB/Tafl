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

		Moves selectedMove = getBestMove(tafl.mainBoard);

		tafl.myGUI.repaint();
		tafl.update(selectedMove.startX,selectedMove.startY);
		tafl.myGUI.repaint();
		tafl.update(selectedMove.endX,selectedMove.endY);

		hilightX = selectedMove.endX;
		hilightY = selectedMove.endY;
		tafl.myGUI.repaint();

		p("AI Happy");
	}

	public Moves getBestMove(Board testBoard){
		possibleMoves = new ArrayList<Moves>();
		possibleMoves = getPossibleMoves(testBoard, 'w');

		for (int i=0; i<possibleMoves.size(); i++) {
			Board tempBoard = new Board(tafl.boardWidth, tafl.boardHeight, testBoard.pieces);
			tafl.simulateMove(possibleMoves.get(i),tempBoard);
			possibleMoves.get(i).score = minmax(tempBoard, 2, 'b');
			p("ai algo returns: "+possibleMoves.get(i).score);
		}

		possibleMoves = sortMovesBasedOnScore(possibleMoves);

		int bestScore = possibleMoves.get(0).score;

		int numberToSelectFrom = 0;

		while(possibleMoves.get(numberToSelectFrom).score == bestScore && numberToSelectFrom < possibleMoves.size()-1){
			numberToSelectFrom++;
		}

		Moves bestMove = possibleMoves.get(r(numberToSelectFrom));

		p("chosen: "+bestMove.score);

		return bestMove;

	}

	//	function minimax(node, depth, maximizingPlayer)
	//	    if depth = 0 or node is a terminal node
	//	        return the heuristic value of node

	//	    if maximizingPlayer
	//	        bestValue := −∞
	//	        for each child of node
	//	            v := minimax(child, depth − 1, FALSE)
	//	            bestValue := max(bestValue, v)
	//	        return bestValue

	//	    else    (* minimizing player *)
	//	        bestValue := +∞
	//	        for each child of node
	//	            v := minimax(child, depth − 1, TRUE)
	//	            bestValue := min(bestValue, v)
	//	        return bestValue

	public int minmax(Board testBoard, int depth, char player){
		if(depth == 0){
			// p(evaluateBoard(testBoard));
			return evaluateBoard(testBoard);
		}
		if(player == 'w'){
			int bestValue = -999;

			ArrayList<Moves> possibleMoves = new ArrayList<Moves>();
			possibleMoves = getPossibleMoves(testBoard, 'w');
			for (int i=0; i<possibleMoves.size(); i++) {
				Board tempBoard = new Board(tafl.boardWidth, tafl.boardHeight, testBoard.pieces);
				tafl.simulateMove(possibleMoves.get(i),tempBoard);

				int thisMoveScore = minmax(tempBoard, depth - 1, 'b');
				if (thisMoveScore > bestValue) {
					bestValue = thisMoveScore;
				}
				// possibleMoves.get(i).score = getWorstBlackMove(tempBoard).score ;
			}

			// p("best white score: "+bestValue);

			return bestValue;

		}else{
			int bestValue = 999;

			ArrayList<Moves> possibleMoves = new ArrayList<Moves>();
			possibleMoves = getPossibleMoves(testBoard, 'b');
			for (int i=0; i<possibleMoves.size(); i++) {
				Board tempBoard = new Board(tafl.boardWidth, tafl.boardHeight, testBoard.pieces);
				tafl.simulateMove(possibleMoves.get(i),tempBoard);

				int thisMoveScore = minmax(tempBoard, depth - 1, 'w');
				if (thisMoveScore < bestValue) {
					bestValue = thisMoveScore;
				}
				// possibleMoves.get(i).score = getWorstBlackMove(tempBoard).score ;
			}

			// p("best black score: "+bestValue);

			return bestValue;
		}
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

	public int evaluateBoard(Board inputBoard){
		int score = 0;
		Board testBoard = new Board(tafl.boardWidth, tafl.boardHeight, inputBoard.pieces);

		if(testBoard.whiteHasWon){
			score = 999;
			// p("this move won the game");
		}else if(testBoard.blackHasWon){
			score = -999;
		}else{

			// find king piece
			
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
				score = 90;
			}else if(escapes == 1){
				score = 50;
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

				score+=numWhite-numBlack;
			}
		}
		// p("checking number of pieces left done \n\n");

		return score;

		// p("selecting move");
	}

	public ArrayList<Moves> sortMovesBasedOnScore(ArrayList<Moves> moveList){
		Collections.sort(moveList, new Comparator<Moves>() {
			@Override public int compare(Moves m1, Moves m2) {
				return m2.score - m1.score; // Ascending
			}
		});
		return moveList;
	}

	public static void s(int x){try{Thread.sleep(x);}catch(Exception e){}}
	public static void p(Object o){System.out.println(o);}
	public static int r(int x){return (int)(Math.random()*x);}
}