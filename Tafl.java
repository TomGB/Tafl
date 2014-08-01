class Tafl {
	boolean selected=false, whiteTurn=false, blackWin=false, whiteWin=false;
	int selX, selY;
	boolean gameOver=false;
	int boardWidth = 9;
	int boardHeight = 9;
	Board board = new Board(boardWidth,boardHeight);

	UserInteraction myGUI;

	public Tafl(){
		board.clear();
		board.setUp();
		myGUI = new UserInteraction(this);

	}

	public void update(int button, int posX, int posY){ //if left button clicked
		if(whiteWin||blackWin){ //reset if game has ended
			whiteTurn=false;
			whiteWin=false;
			blackWin=false;
			board.clear();
			board.setUp();
		}
		
		if(button==1){
			if((!whiteTurn&&board.isBlack(posX,posY))||(whiteTurn&&board.isWhite(posX,posY))){
				selX=posX;
				selY=posY;
				selected=true;
			}else if(selected){
				char original = board.get(selX,selY);
				if(board.validMove(selX, selY, posX, posY, original)){ //move piece
					whiteTurn = !whiteTurn;
					board.set(posX,posY,original);
					board.set(selX,selY,'e');
					selected=false;
					board.takePieces(posX, posY, original);
					if(board.checkKing()){
						blackWin=true;
						for (int i=0; i<board.width; i++) {
							for (int j=0; j<board.height; j++) {
								if(board.get(i,j)=='k'){
									board.set(i,j,'e');
								}
							}
						}
					}
					if(board.checkWin()){
						whiteWin=true;
					}
				}
			}
		}
		myGUI.repaint();
	}
	public static void p(Object o){System.out.print(o);}
	public static int r(int x){return (int)(Math.random()*x);}
	public static void main(String args[]){new Tafl();}
}
