import java.io.PrintWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

class Moves {

	int startX, startY, endX, endY;
	char piece;
	int score=0;
	
	public Moves(int startX, int startY, int endX, int endY, char piece){
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.piece = piece;
	}

	public static void p(Object o){System.out.println(o);}
	public static int r(int x){return (int)(Math.random()*x);}
	public static void main(String args[]){new Tafl();}
}
