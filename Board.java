class Board {
	char pieces[][];
	int width, height;

	public Board(int _width, int _height){
		width = _width;
		height = _height;
		pieces = new char[width][height];
	}
	public void clear(){
		for (int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				pieces[i][j]='e';
			}
		}
	}
	public void setUp(){
		pieces[4][4]='k';
		pieces[4][2]='w';
		pieces[3][4]='w';
		pieces[2][4]='w';
		pieces[4][5]='w';
		pieces[4][3]='w';
		pieces[6][4]='w';
		pieces[5][4]='w';
		pieces[4][6]='w';
		pieces[0][3]='b';
		pieces[0][4]='b';
		pieces[0][5]='b';
		pieces[1][4]='b';
		pieces[8][3]='b';
		pieces[8][4]='b';
		pieces[8][5]='b';
		pieces[7][4]='b';
		pieces[3][0]='b';
		pieces[4][0]='b';
		pieces[5][0]='b';
		pieces[4][1]='b';
		pieces[3][8]='b';
		pieces[4][8]='b';
		pieces[5][8]='b';
		pieces[4][7]='b';
	}
	public char get(int x,int y){
		if(x>=0&&x<width&&y>=0&&y<height){
			return pieces[x][y];
		}else{
			return 'o';
		}
	}
	public void set(int x, int y, char type){
		pieces[x][y]=type;
	}
	public boolean isPiece(int x, int y){
		char tempPiece = pieces[x][y];
		return (tempPiece=='w'||tempPiece=='k'||tempPiece=='b');
	}
	public boolean validMove(int x1, int y1, int x2, int y2, char piece){
		if(x1==x2){ //piece moved in y axis
			int low = (y1<y2?y1:y2);
			int high = (y1>y2?y1:y2);
			for (int i=low+1; i<high; i++) {
				if(get(x1,i)!='e'){
					return false;
				}
			}
		}else if(y1==y2){ //if piece moved in x axis
			int low = (x1<x2?x1:x2);
			int high = (x1>x2?x1:x2);
			for (int i=low+1; i<high; i++) {
				if(get(i,y1)!='e'){
					return false;
				}
			}
		}else{ //if attempted to move diagonally
			return false;
		}
		if(piece!='k'&&isKingSpace(x2,y2)){
			return false;
		}
		return true;
	}
	public boolean isKingSpace(int x, int y){
		return ((x==0&&y==0)||(x==0&&y==height-1)||(x==width-1&&y==0)||(x==width-1&&y==height-1)||(x==width/2&&y==height/2));
	}
	public void takePieces(int x, int y, char piece){
		if(y<width-2&&isEnemyPawn(x, y+1, piece)&&isFriend(x, y+2, piece)){
			set(x, y+1, 'e');
		}
		if(x<width-2&&isEnemyPawn(x+1, y, piece)&&isFriend(x+2, y, piece)){
			set(x+1, y, 'e');
		}
		if(y>1&&isEnemyPawn(x, y-1, piece)&&isFriend(x, y-2, piece)){
			set(x, y-1, 'e');
		}
		if(x>1&&isEnemyPawn(x-1, y, piece)&&isFriend(x-2, y, piece)){
			set(x-1, y, 'e');
		}
	}
	public boolean isEnemyPawn(int x, int y, char piece){
		return ((get(x,y)=='b'&&piece=='w'||piece=='k'))||(get(x,y)=='w'&&piece=='b');
	}
	public boolean isFriend(int x, int y, char piece){
		return ((get(x,y)=='b'&&piece=='b')||((get(x,y)=='w'||get(x,y)=='k')&&(piece=='w'||piece=='k')));
	}
	public boolean checkKing(){
		for (int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				if(get(i,j)=='k'){
					return(
						(get(i-1,j)=='o'||get(i-1,j)=='b'||isKingSpace(i-1,j)) &&
						(get(i,j-1)=='o'||get(i,j-1)=='b'||isKingSpace(i,j-1)) &&
						(get(i+1,j)=='o'||get(i+1,j)=='b'||isKingSpace(i+1,j)) &&
						(get(i,j+1)=='o'||get(i,j+1)=='b'||isKingSpace(i,j+1)));
				}
			}
		}
		return true;
	}
	public boolean checkWin(){
		for (int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				if(get(i,j)=='k'){
					return(isKingSpace(i,j));
				}
			}
		}
		return false;
	}
}