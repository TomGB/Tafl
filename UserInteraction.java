import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

public class UserInteraction extends JFrame{
	public static final long serialVersionUID = 1L;

	int sizeX=800, sizeY=800;
	Tafl tafl;
	boolean mouseIsDown,up,down,left,right,space,showPow;
	int mX=0,mY=0;
	int gridSpace=(sizeY-100)/9;
	int pieceRad = gridSpace/2;
	int pieceSpace=pieceRad/2;

	public UserInteraction(Tafl _tafl){

		tafl = _tafl;

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		setResizable( false );

		JPanel drawing = new JPanel(){
			public static final long serialVersionUID = 1L;
			public void paint(Graphics g){
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //anti alias to make lines smooth
				AffineTransform at = g2.getTransform();

				for (int i=0; i<tafl.board.width; i++) {
					for (int j=0; j<tafl.board.height; j++) {
						g.setColor(Color.white);
						if((i==0&&j==0)||(i==0&&j==tafl.board.width-1)||(i==8&&j==0)||(i==8&&j==8)||(i==4&&j==4)){
							g.setColor(Color.red);
						}
						g.fillRect(50+gridSpace*i,50+gridSpace*j,gridSpace,gridSpace);
						g.setColor(Color.black);
						g.drawRect(50+gridSpace*i,50+gridSpace*j,gridSpace,gridSpace);
						char tempPiece = tafl.board.get(i,j);
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
				if(tafl.selected){
					g.setColor(Color.blue);
					g.fillOval(50+gridSpace*tafl.selX+pieceSpace+(pieceSpace/2),50+gridSpace*tafl.selY+pieceSpace+(pieceSpace/2),pieceRad/2,pieceRad/2);
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
				if(tempX<=8 && tempX>=0 && tempY<=8 && tempY>=0){
					tafl.update(e.getButton(),tempX,tempY);
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

	public static void p(Object o){System.out.print(o);}
	public static int r(int x){return (int)(Math.random()*x);}
}