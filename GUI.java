import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

public class GUI extends JFrame{
	public static final long serialVersionUID = 1L;

	public GUI(){

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		setResizable( false );

		JPanel drawing = new JPanel(){
			public static final long serialVersionUID = 1L;
			public void paint(Graphics g){
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
						char tempPiece = board.get(i,j);
						if(tempPiece=='b'){
							g.setColor(Color.black);
							g.fillOval(50+gridSpace*i+pieceSpace,50+gridSpace*j+pieceSpace,pieceRad,pieceRad);
						}else if(tempPiece=='w'){
							g.setColor(Color.black);
							g.drawOval(50+gridSpace*i+pieceSpace,50+gridSpace*j+pieceSpace,pieceRad,pieceRad);
						}else if(tempPiece=='k'){
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
				update(e.getButton());
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

	public void update(int button){ //if left button clicked
		int tempX=(mX-50)/gridSpace;
		int tempY=(mY-50)/gridSpace;
		if(tempX<=8 && tempX>=0 && tempY<=8 && tempY>=0){
			posX=tempX;
			posY=tempY;
			if(button==1){
				if(board.isPiece(posX,posY)){
					selX=posX;
					selY=posY;
					selected=true;
				}else if(selected){
					char original = board.get(selX,selY);

					if(board.validMove(selX, selY, posX, posY, original)){ //move piece
						board.set(posX,posY,original);
						board.set(selX,selY,'e');
						selected=false;
						board.takePieces(posX, posY, original);
						if(board.checkKing()){
							p("black has won\n");
						}
						if(board.checkWin()){
							p("white has won\n");
						}
					}
				}
			}else{
				board.set(posX,posY,'e');
			}
			repaint();
		}
	}

	public static void p(Object o){System.out.print(o);}
	public static int r(int x){return (int)(Math.random()*x);}
	public static void main(String args[]){new GUI();}
}