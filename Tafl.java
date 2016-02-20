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

	public Tafl(){
		mainBoard.clear();
		mainBoard.setUp();
		myGUI = new UserInteraction(this);
	}

	public void reset(){
		mainBoard.clear();
		mainBoard.setUp();
		whiteTurn=false;
		myGUI.repaint();
		numMoves = 0;
	}

	public void update(int posX, int posY){ //if left button clicked
		if(rules){
			rules=false;
		}else{
			if(whiteWin||blackWin){ //reset if game has ended
				whiteTurn=false;
				whiteWin=false;
				blackWin=false;
				reset();
			}
			if((!whiteTurn&&mainBoard.isBlack(posX,posY))||(whiteTurn&&mainBoard.isWhite(posX,posY))){
				selX=posX;
				selY=posY;
				selected=true;
			}else if(selected){
				char original = mainBoard.get(selX,selY);
				if(mainBoard.validMove(selX, selY, posX, posY, original)){ //move piece
					mainBoard.turnNum++;
					mainBoard.saveHistory();
					whiteTurn = !whiteTurn;
					mainBoard.set(posX,posY,original);
					mainBoard.set(selX,selY,'e');
					selected=false;
					mainBoard.takePieces(posX, posY, original);
					mainBoard.setWinLose();
					blackWin=mainBoard.blackHasWon;
					whiteWin=mainBoard.whiteHasWon;

					ActionListener listener = new ActionListener(){
						public void actionPerformed(ActionEvent event){
							if(!blackWin&&!whiteWin&&whiteTurn){
								myBot.takeTurn();
							}
						}
					};
					Timer displayTimer = new Timer(60, listener);
					displayTimer.start();

					numMoves++;
					p("moveNumber: "+numMoves);
				}
			}
		}
		myGUI.repaint();
	}

	public void simulateMove(Moves thisMove, Board tempBoard){ //if left button clicked
		tempBoard.set(thisMove.endX,thisMove.endY,thisMove.piece);
		tempBoard.set(thisMove.startX,thisMove.startY,'e');
		tempBoard.takePieces(thisMove.endX,thisMove.endY,thisMove.piece);
		tempBoard.setWinLose();
	}

	public void undo(){
		if(mainBoard.turnNum>0){
			whiteTurn = !whiteTurn;
			mainBoard.loadHistory();
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
	public static void main(String args[]){new Tafl();}
}
