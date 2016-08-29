import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import java.awt.image.*;

public class UserInteraction extends JFrame{
	public static final long serialVersionUID = 1L;

	int sizeX=800, sizeY=800;
	Tafl tafl;
	boolean mouseIsDown,up,down,left,right,space,showPow;
	int mX=0,mY=0;
	int gridSpace=(sizeY-100)/9;
	int pieceRad = gridSpace/2;
	int pieceSpace=pieceRad/2;
	int selectSpacing=4;
	Font f = new Font("Dialog", Font.PLAIN, 16);
	TextBoxController tbc;

	public UserInteraction(Tafl _tafl){
		tafl = _tafl;
		setTitle("Tafl");
		setResizable( false );
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		tbc = new TextBoxController(tafl);

		JPanel drawing = new JPanel(){
			public static final long serialVersionUID = 1L;
			public void paint(Graphics g){
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				AffineTransform at = g2.getTransform();
				g.setFont(f);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //anti alias to make lines smooth

				drawBoardAndPieces(g);

				if(tafl.blackWin){
					tbc.blackWinText.draw(true,g);
				}else if(tafl.whiteWin){
					tbc.whiteWinText.draw(true, g);
				}else if(tafl.rules){
					tbc.drawRulesText(g);
					tbc.drawButtons(false, g);
				}else{
					tbc.drawButtons(true, g);

					if(tafl.selected){
						g.setColor(Color.blue);
						g.drawRect(50+gridSpace*tafl.selX+selectSpacing,50+gridSpace*tafl.selY+selectSpacing,gridSpace-selectSpacing*2,gridSpace-selectSpacing*2);
					}
				}

				// g.setColor(Color.black);

				// for (int i=0; i<tafl.possibleMoves.size(); i++) {
				// 	Moves temp = tafl.possibleMoves.get(i);
				// 	g.fillRect(50+gridSpace*temp.endX+selectSpacing,50+gridSpace*temp.endY+selectSpacing,gridSpace-selectSpacing*2,gridSpace-selectSpacing*2);
				// }
			}
		};

		addKeyListener(new KeyListener(){ //NOT THIS BUT FRAME
			public void keyPressed(KeyEvent e)	{	setKey(true,e.getKeyCode());	}
			public void keyReleased(KeyEvent e)	{	setKey(false,e.getKeyCode());	}
			public void keyTyped(KeyEvent e){}
		});
		drawing.addMouseListener(new MouseListener(){
			public void mousePressed(MouseEvent e) {setMouse(true);}
			public void mouseReleased(MouseEvent e) {setMouse(false);}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		});
		drawing.addMouseMotionListener(new MouseMotionListener(){
			public void mouseMoved(MouseEvent e) {setMouse(e.getX(),e.getY());}
			public void mouseDragged(MouseEvent e) {setMouse(e.getX(),e.getY());}
		});

		add("Center", drawing);
		this.setSize(sizeX,sizeY);
		repaint();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE); //exit application when x is clicked
	}
	public void setKey(boolean state, int key){
			 if(key==87){	up=state;}
		else if(key==83){	down=state;}
		else if(key==65){	left=state;}
		else if(key==68){	right=state;}
		else if(key==32){	space=state;}
	}
	public void setMouse(int _mX, int _mY){
		mX=_mX;
		mY=_mY;
		if(!tafl.whiteWin && !tafl.blackWin
			&& (tbc.undo.inside(mX,mY)||tbc.save.inside(mX,mY)
			||tbc.load.inside(mX,mY)||tbc.reset.inside(mX,mY)
			||tbc.rules.inside(mX,mY))){
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}else{
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void drawBoardAndPieces(Graphics g){
		for (int i=0; i<tafl.mainBoard.width; i++) {
			for (int j=0; j<tafl.mainBoard.height; j++) {
				g.setColor(Color.white);
				if((i==0&&j==0)||(i==0&&j==8)||(i==8&&j==0)||(i==8&&j==8)||(i==4&&j==4)){
					g.setColor(Color.red);
				}
				g.fillRect(50+gridSpace*i,50+gridSpace*j,gridSpace,gridSpace);
				g.setColor(Color.black);
				g.drawRect(50+gridSpace*i,50+gridSpace*j,gridSpace,gridSpace);
				drawPiece(g, i, j);
			}
		}
	}

	public void drawPiece(Graphics g, int i, int j){
		char tempPiece = tafl.mainBoard.get(i,j);

		int x = 50+gridSpace*i+pieceSpace;
		int y = 50+gridSpace*j+pieceSpace;
		if(tempPiece=='b'){
			g.setColor(Color.black);
			g.fillOval(x, y, pieceRad, pieceRad);
		}else if(tempPiece=='w'){
			g.setColor(Color.black);
			g.drawOval(x, y, pieceRad, pieceRad);
		}else if(tempPiece=='k'){
			g.setColor(Color.pink);
			g.fillOval(x, y, pieceRad, pieceRad);
		}
	}

	public void setMouse(boolean state){
		if(state){
			mouseIsDown=true;
		}else{
			mouseIsDown=false;
			float tempX=((float)(mX-50)/gridSpace);
			float tempY=((float)(mY-50)/gridSpace);
			if(tempX<9 && tempX>=0 && tempY<9 && tempY>=0){
				p("board location select call");
				tafl.selectLocation((int)tempX,(int)tempY);
				tafl.myGUI.repaint();
			}else{
				tbc.checkButtons(mX, mY);
			}
		}
	}

	public static void p(Object o){System.out.println(o);}
	public static int r(int x){return (int)(Math.random()*x);}
}
