import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Tafl extends JFrame{
	public static final long serialVersionUID = 1L;
	boolean selected=false, whiteTurn=true;
	int posX=0, posY=0;
	int selX, selY;
	int mX=0,mY=0;
	boolean mouseIsDown,up,down,left,right,space,showPow, gameOver=false;
	int sizeX=800, sizeY=800;
	int gridSpace=(sizeY-100)/9;
	int pieceRad = gridSpace/2;
	int pieceSpace=pieceRad/2;
	char board[][] = new char[9][9];
	public Tafl(){

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		setResizable( false );

		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				board[i][j]='e';
			}
		}

		board[4][4]='k';
		board[4][2]='w';
		board[3][4]='w';
		board[2][4]='w';
		board[4][5]='w';
		board[4][3]='w';
		board[6][4]='w';
		board[5][4]='w';
		board[4][6]='w';
		board[0][3]='b';
		board[0][4]='b';
		board[0][5]='b';
		board[1][4]='b';
		board[8][3]='b';
		board[8][4]='b';
		board[8][5]='b';
		board[7][4]='b';
		board[3][0]='b';
		board[4][0]='b';
		board[5][0]='b';
		board[4][1]='b';
		board[3][8]='b';
		board[4][8]='b';
		board[5][8]='b';
		board[4][7]='b';

		JPanel drawing = new JPanel(){
			public static final long serialVersionUID = 1L;
			public void paint(Graphics g){
				p("painted\n");
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //anti alias to make lines smooth
				AffineTransform at = g2.getTransform();

				for (int i=0; i<9; i++) {
					for (int j=0; j<9; j++) {
						g.setColor(Color.white);
						if((i==0&&j==0)||(i==0&&j==8)||(i==8&&j==0)||(i==8&&j==8)||(i==4&&j==4)){
							g.setColor(Color.red);
						}
						g.fillRect(50+gridSpace*i,50+gridSpace*j,gridSpace,gridSpace);
						g.setColor(Color.black);
						g.drawRect(50+gridSpace*i,50+gridSpace*j,gridSpace,gridSpace);
						if(board[i][j]=='b'){
							g.setColor(Color.black);
							g.fillOval(50+gridSpace*i+pieceSpace,50+gridSpace*j+pieceSpace,pieceRad,pieceRad);
						}else if(board[i][j]=='w'){
							g.setColor(Color.black);
							g.drawOval(50+gridSpace*i+pieceSpace,50+gridSpace*j+pieceSpace,pieceRad,pieceRad);
						}else if(board[i][j]=='k'){
							g.setColor(Color.pink);
							g.fillOval(50+gridSpace*i+pieceSpace,50+gridSpace*j+pieceSpace,pieceRad,pieceRad);
						}
					}
				}
				if(selected){
					g.setColor(Color.blue);
					g.fillOval(50+gridSpace*selX+pieceSpace+(pieceSpace/2),50+gridSpace*selY+pieceSpace+(pieceSpace/2),pieceRad/2,pieceRad/2);
				}
			}
		};

		addKeyListener(new KeyListener(){ //NOT THIS BUT FRAME
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==87){
					up=true;
				}else if(e.getKeyCode()==83){
					down=true;
				}else if(e.getKeyCode()==65){
					left=true;
				}else if(e.getKeyCode()==68){
					right=true;
				}else if(e.getKeyCode()==32){
					space=true;
				}

			}
			public void keyReleased(KeyEvent e){
				if(e.getKeyCode()==87){
					up=false;
				}else if(e.getKeyCode()==83){
					down=false;
				}else if(e.getKeyCode()==65){
					left=false;
				}else if(e.getKeyCode()==68){
					right=false;
				}else if(e.getKeyCode()==32){
					space=false;
				}
			}
			public void keyTyped(KeyEvent e){}
		});

		drawing.addMouseListener(new MouseListener(){
			public void mousePressed(MouseEvent e) {
				mouseIsDown=true;
			}
			public void mouseReleased(MouseEvent e) {
				mouseIsDown=false;
				int tempX=(mX-50)/gridSpace;
				int tempY=(mY-50)/gridSpace;
				if(tempX<=8||tempX>=0||tempY<=8||tempY>=0){
					posX=tempX;
					posY=tempY;
					if(e.getButton()==1){
						if(board[posX][posY]=='w'||board[posX][posY]=='k'||board[posX][posY]=='b'){
							selX=posX;
							selY=posY;
							selected=true;
						}else if(board[posX][posY]=='e'){
							boolean safe=false;
							if(posX==selX){
								safe=checkPath(posY,selY,false);
							}else if(posY==selY){
								safe=checkPath(posX,selX,true);
							}
							if(safe){
								if(board[selX][selY]=='w'){
									board[posX][posY]='w';
								}else if(board[selX][selY]=='k'){
									board[posX][posY]='k';
								}else if(board[selX][selY]=='b'){
									board[posX][posY]='b';
								}
								board[selX][selY]='e';
								selected=false;
							}
						}
					}else{
						board[posX][posY]='e';
					}
					p("selX: "+selX+" selY: "+selY+"\n");
					repaint();
				}
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		});
		drawing.addMouseMotionListener(new MouseMotionListener(){
			public void mouseMoved(MouseEvent e) {
				mX=e.getX();
				mY=e.getY();
			}
			public void mouseDragged(MouseEvent e) {
				mX=e.getX();
				mY=e.getY();
			}
		});
		add("Center", drawing);
		this.setSize(sizeX,sizeY);
		repaint();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE); //exit application when x is clicked
	}
	public boolean checkPath(int pos, int sel, boolean x){
		for (int i=(pos<sel?pos:sel)+1; i<(pos<sel?sel:pos); i++) {
			if((!x&&board[posX][i]!='e')||(x&&board[i][posY]!='e')){
				return false;
			}
		}
		return true;
	}

	public static void p(Object o){System.out.print(o);}
	public static int r(int x){return (int)(Math.random()*x);}
	public static void main(String args[]){new Tafl();}
}
