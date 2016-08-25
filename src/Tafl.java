import java.io.PrintWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class Tafl {
	boolean selected=false, whiteTurn=false, blackWin=false, whiteWin=false;
	int selX, selY;
	boolean gameOver=false, rules=false;
	int boardWidth = 9;
	int boardHeight = 9;
	Board mainBoard = new Board(boardWidth,boardHeight);
	UserInteraction myGUI;
	int numMoves = 0;
	Bot myBot = new Bot(this);

	boolean useAI;

	public Tafl(Boolean useAI){
		mainBoard.clear();
		mainBoard.setUp();
		this.useAI = useAI;
		myGUI = new UserInteraction(this);
	}

	public void reset(){
		whiteWin=false;
		blackWin=false;
		mainBoard.clear();
		mainBoard.setUp();
		whiteTurn=false;
		myGUI.repaint();
		numMoves = 0;
	}

	public void selectLocation(int posX, int posY){ //if left button clicked
		if(rules){ // clicked when viewing rules
			rules=false;
		}else{
			if(whiteWin||blackWin){ //reset if game has ended
				reset();
			}else{
				// if selecting a new piece
				if(isOwnPiece(posX, posY)){
					selectPiece(posX, posY);
				}else if(selected){
					movePiece(posX, posY);
				}
			}
		}
	}

	public boolean isOwnPiece(int posX, int posY){
		return (!whiteTurn&&mainBoard.isBlack(posX,posY))||(whiteTurn&&mainBoard.isWhite(posX,posY));
	}

	public void selectPiece(int posX, int posY){
		selX=posX;
		selY=posY;
		selected=true;
	}

	public void movePiece(int posX, int posY){
		char original = mainBoard.get(selX,selY);
		if(mainBoard.validMove(selX, selY, posX, posY, original)){ //move piece
			mainBoard.turnNum++;
			mainBoard.saveHistory();
			whiteTurn = !whiteTurn;
			mainBoard.set(posX,posY,original);
			mainBoard.set(selX,selY,'e');
			selected = false;
			mainBoard.takePieces(posX, posY, original);
			mainBoard.setWinLose();
			blackWin = mainBoard.blackHasWon;
			whiteWin = mainBoard.whiteHasWon;

			if(useAI){
				ActionListener listener = new ActionListener(){
					public void actionPerformed(ActionEvent event){
						if(!blackWin&&!whiteWin&&whiteTurn){
							myBot.takeTurn();
						}
					}
				};
				Timer displayTimer = new Timer(60, listener);
				displayTimer.start();
			}

			numMoves++;
			p("moveNumber: "+numMoves);
		}
	}

	public void simulateMove(Moves thisMove, Board tempBoard){ //if left button clicked
		tempBoard.set(thisMove.endX,thisMove.endY,thisMove.piece);
		tempBoard.set(thisMove.startX,thisMove.startY,'e');
		tempBoard.takePieces(thisMove.endX,thisMove.endY,thisMove.piece);
		tempBoard.setWinLose();
	}

	public void undo(){
		if(mainBoard.turnNum>0){
			mainBoard.loadHistory();
			whiteTurn = !whiteTurn;
			if(useAI){
				mainBoard.loadHistory();
				whiteTurn = !whiteTurn;
			}
			myGUI.repaint();
		}
	}

	public void rules(){
		rules=!rules;
		myGUI.repaint();
	}

	public void save(){
		try{
			PrintWriter savefile= new PrintWriter(new File("taflsave.txt"));
			savefile.println("tafl save");
			savefile.println(whiteTurn?"White's Turn":"Black's Turn");
			for (int j=0; j<boardHeight; j++) {
				for (int i=0; i<boardWidth; i++) {
					savefile.print(mainBoard.get(i,j));
				}
			}
			savefile.flush();
			p("save done");
		}catch(Exception e){
			p("error saving");
		}
	}

	public void load(){
		try(BufferedReader br = new BufferedReader(new FileReader("taflsave.txt"))) {
			br.readLine();
			String turn = br.readLine();
      String line = br.readLine();
      for (int i=0; i<line.length(); i++) {
      	mainBoard.set(i%boardWidth, i/boardHeight, line.charAt(i));
			}
			p(turn);
			whiteTurn = (turn.equals("White's Turn"));
			myGUI.repaint();
   		}catch(Exception e){
   			p("error reading file");
   		}
	}
	public static void s(int x){try{Thread.sleep(x);}catch(Exception e){}}
	public static void p(Object o){System.out.println(o);}
	public static int r(int x){return (int)(Math.random()*x);}
	public static void main(String args[]){
		boolean useAI;
		if(args.length==0){
			useAI = false;
		}else{
			useAI = true;
		}
		new Tafl(useAI);
	}
}
