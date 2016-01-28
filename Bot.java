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

		possibleMoves = new ArrayList<Moves>();

		//for each piece on the tafl.mainBoard
		//for each possible move that the piece can make
		//record to a list of possible moves

		

		p("Possible Moves: "+possibleMoves.size());

		// need to seperate this for loop into a generic algorithem that works for black and white to rate the board
		// 
		
		Board tempBoard = new Board(tafl.boardWidth, tafl.boardHeight, tafl.mainBoard.pieces);

		possibleMoves = getPossibleMoves(tafl.mainBoard);

		possibleMoves = evaluateMoves(possibleMoves, tempBoard);

		Collections.sort(possibleMoves, new Comparator<Moves>() {
	        @Override public int compare(Moves m1, Moves m2) {
	            return m2.score - m1.score; // Ascending
	        }

	    });

		int bestScore = possibleMoves.get(0).score;

		int numberToSelectFrom;

		for (int i=0; i<possibleMoves.size(); i++) {
			p("Score: "+possibleMoves.get(i).score);
		}

		for (numberToSelectFrom = 0; numberToSelectFrom < possibleMoves.size() && possibleMoves.get(numberToSelectFrom).score == bestScore; numberToSelectFrom++) {

		}

		Moves selectedMove = possibleMoves.get(r(numberToSelectFrom));

		// p("selecting move done \n\n");

		tafl.myGUI.repaint();
		// p("before sleep");
		// s(500);
		// p("after sleep");
		tafl.update(selectedMove.startX,selectedMove.startY);
		tafl.myGUI.repaint();
		// s(500);
		tafl.update(selectedMove.endX,selectedMove.endY);

		hilightX = selectedMove.endX;
		hilightY = selectedMove.endY;
		tafl.myGUI.repaint();

		p("AI Happy");
	}

	public ArrayList<Moves> getPossibleMoves(Board testBoard){

		ArrayList<Moves> tempMoves = new ArrayList<Moves>();

		for (int i=0; i<tafl.boardWidth; i++) {
			for (int j=0; j<tafl.boardHeight; j++) {
				if(testBoard.isWhite(i,j)){
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

	public ArrayList<Moves> evaluateMoves(ArrayList<Moves> evaluateMoves, Board testBoard){
		for (int moveNum=0; moveNum<evaluateMoves.size(); moveNum++) {
			Moves thisMove = evaluateMoves.get(moveNum);
			tafl.simulateMove(thisMove, testBoard);

			if(thisMove.piece=='k'&&testBoard.checkWin()){
				thisMove.score = 100;
				// p("this move won the game");
			}else if(thisMove.piece=='k'){
				// p("checking king movements");
				int escapes = 0;
				if(testBoard.validMove(thisMove.endX,thisMove.endY,0,0,'k')){
					escapes+=1;
				}
				if(testBoard.validMove(thisMove.endX,thisMove.endY,0,tafl.boardHeight-1,'k')){
					escapes+=1;
				}
				if(testBoard.validMove(thisMove.endX,thisMove.endY,tafl.boardWidth-1,0,'k')){
					escapes+=1;
				}
				if(testBoard.validMove(thisMove.endX,thisMove.endY,tafl.boardWidth-1,tafl.boardHeight-1,'k')){
					escapes+=1;
				}

				p("escapes: "+escapes);

				if(escapes>1){
					thisMove.score = 90;
				}else if(escapes == 1){
					thisMove.score = 50;
				}
				// p("king movements done\n\n");
			}

			// p("checking enemies around king");
			for (int i=0; i<tafl.boardWidth; i++) {
				for (int j=0; j<tafl.boardHeight; j++) {
					if(testBoard.isKing(i,j)){
						int enemiesAround = 0;
						if(testBoard.isBlack(i-1,j)){
							enemiesAround++;
						}
						if(testBoard.isBlack(i+1,j)){
							enemiesAround++;
						}
						if(testBoard.isBlack(i,j-1)){
							enemiesAround++;
						}
						if(testBoard.isBlack(i,j+1)){
							enemiesAround++;
						}

						//one enemy around is OK
						enemiesAround--;
						if(enemiesAround>0){
							thisMove.score-= 50*enemiesAround;
						}
					}
				}
			}
			// p("checking enemies around king done\n\n");

			// p("checking number of pieces left");

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

		return evaluateMoves;

		// p("selecting move");
	}

	public static void s(int x){try{Thread.sleep(x);}catch(Exception e){}}
	public static void p(Object o){System.out.println(o);}
	public static int r(int x){return (int)(Math.random()*x);}
}