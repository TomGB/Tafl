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
	int textHeight = 40;
	int textWidth = 200;
	int frameX = 80;
	int frameY = 70;
	Font f = new Font("Dialog", Font.PLAIN, 16);

	String rulesText = "Black is attacking and goes first, white is defending it's King (the pink piece in\nthe middle).\n\nTo win black must surround the King on 4 sides or white must reach one of the\nfour corners with its King.\n\nPieces may move any distance in a strait line,horisontally or vertically,\nunless another piece blocks its path.\n\nOnly the King may sit in the red spaces.\n\nTo take an enemy piece, on your turn suround it on opposite sides with\ntwo pieces.\n\nThe red spaces count as enemy pieces when it is not your turn and can be\nused in capturing either side, unless the King is on it in which case it counts as\nwhite only.";

	Image undoimg;

	public UserInteraction(Tafl _tafl){

		setTitle("Tafl");

		tafl = _tafl;

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

		try{
			undoimg = ImageIO.read(new File("assets/undo.png"));
		}catch(IOException e){
			p("error reading image");
		}

		setResizable( false );

		JPanel drawing = new JPanel(){
			public static final long serialVersionUID = 1L;
			public void paint(Graphics g){
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g.setFont(f);
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

				/*=========================================================================
				===================   Need to create a text box class   ===================
				=========================================================================*/


				if(tafl.blackWin){
					g.setColor(new Color(255,255,255,200));
					g.fillRect((sizeX-textWidth)/2,(sizeY-textHeight)/2,textWidth,textHeight);
					g.setColor(Color.black);
					g.drawRect((sizeX-textWidth)/2,(sizeY-textHeight)/2,textWidth,textHeight);
					g.drawString("Black has Won",(sizeX-textWidth)/2+30,(sizeY)/2+6);
				}else if(tafl.whiteWin){
					g.setColor(new Color(255,255,255,200));
					g.fillRect((sizeX-textWidth)/2,(sizeY-textHeight)/2,textWidth,textHeight);
					g.setColor(Color.black);
					g.drawRect((sizeX-textWidth)/2,(sizeY-textHeight)/2,textWidth,textHeight);
					g.drawString("White has Won",(sizeX-textWidth)/2+30,(sizeY)/2+6);
				}else if(tafl.rules){
					g.setColor(new Color(255,255,255,210));
					g.fillRect(60,60,sizeX-120,sizeY-120);
					g.setColor(Color.black);
					g.drawRect(60,60,sizeX-120,sizeY-120);
					for (String line : rulesText.split("\n")){
	        			g.drawString(line, frameX, frameY += g.getFontMetrics().getHeight());
					}
					frameY = 70;
					g.setColor(new Color(0,0,0,50));
					g.fillRect(130,10,textWidth,textHeight);
					g.setColor(Color.black);
					g.drawRect(130,10,textWidth,textHeight);
					if(tafl.whiteTurn){
						g.drawString("White's Turn",130+30,30+6);
					}else{
						g.drawString("Black's Turn",130+30,30+6);
					}
					g.setColor(new Color(0,0,0,50));
					g.fillRect(70,10,50,textHeight);
					g.setColor(Color.black);
					g.drawRect(70,10,50,textHeight);
					g.drawImage(undoimg,74,12, null);

					g.setColor(new Color(0,0,0,50));
					g.fillRect(550,10,80,textHeight);
					g.setColor(Color.black);
					g.drawRect(550,10,80,textHeight);
					g.drawString("Save",550+20,30+6);

					g.setColor(new Color(0,0,0,50));
					g.fillRect(460,10,80,textHeight);
					g.setColor(Color.black);
					g.drawRect(460,10,80,textHeight);
					g.drawString("Load",460+20,30+6);

					g.setColor(new Color(0,0,0,50));
					g.fillRect(370,10,80,textHeight);
					g.setColor(Color.black);
					g.drawRect(370,10,80,textHeight);
					g.drawString("Reset",370+20,30+6);
				}else{
					g.setColor(new Color(255,255,255,200));
					g.fillRect(130,10,textWidth,textHeight);
					g.setColor(Color.black);
					g.drawRect(130,10,textWidth,textHeight);
					if(tafl.whiteTurn){
						g.drawString("White's Turn",130+30,30+6);
					}else{
						g.drawString("Black's Turn",130+30,30+6);
					}
					g.setColor(new Color(255,255,255,200));
					g.fillRect(70,10,50,textHeight);
					g.setColor(Color.black);
					g.drawRect(70,10,50,textHeight);
					g.drawImage(undoimg,74,12, null);

					g.setColor(new Color(255,255,255,200));
					g.fillRect(550,10,80,textHeight);
					g.setColor(Color.black);
					g.drawRect(550,10,80,textHeight);
					g.drawString("Save",550+20,30+6);

					g.setColor(new Color(255,255,255,200));
					g.fillRect(460,10,80,textHeight);
					g.setColor(Color.black);
					g.drawRect(460,10,80,textHeight);
					g.drawString("Load",460+20,30+6);

					g.setColor(new Color(255,255,255,200));
					g.fillRect(370,10,80,textHeight);
					g.setColor(Color.black);
					g.drawRect(370,10,80,textHeight);
					g.drawString("Reset",370+20,30+6);

					if(tafl.selected){
						g.setColor(Color.blue);
						g.drawRect(50+gridSpace*tafl.selX+selectSpacing,50+gridSpace*tafl.selY+selectSpacing,gridSpace-selectSpacing*2,gridSpace-selectSpacing*2);
					}
				}

				g.setColor(new Color(255,255,255,200));
				g.fillRect(640,10,80,textHeight);
				g.setColor(Color.black);
				g.drawRect(640,10,80,textHeight);
				g.drawString("Rules",640+20,30+6);
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
				float tempX=((float)(mX-50)/gridSpace);
				float tempY=((float)(mY-50)/gridSpace);
				if(tempX<9 && tempX>=0 && tempY<9 && tempY>=0){
					p("update call");
					tafl.update(e.getButton(),(int)tempX,(int)tempY);
				}else if(!tafl.whiteWin && !tafl.blackWin && mX>70 && mX<120 && mY>10 && mY<90){
					p("undo clicked");
					tafl.undo();
				}else if(mX>640&&mX<720&&mY>10&&mY<50){//these should be in a class or method not hard coded!!!
					p("display rules");
					tafl.rules();
				}else if(!tafl.rules&&mX>550&&mX<630&&mY>10&&mY<50){
					p("save game");
					tafl.save();
				}else if(!tafl.rules&&mX>460&&mX<540&&mY>10&&mY<50){
					p("load game");
					tafl.load();
				}else if(!tafl.rules&&mX>370&&mX<450&&mY>10&&mY<50){
					p("reset game");
					tafl.reset();
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
				if((!tafl.whiteWin && !tafl.blackWin&&mX>70&&mX<120&&mY>10&&mY<50)||(mX>640&&mX<720&&mY>10&&mY<50)||(!tafl.rules&&mX>550&&mX<630&&mY>10&&mY<50)||(!tafl.rules&&mX>460&&mX<540&&mY>10&&mY<50)||(!tafl.rules&&mX>370&&mX<450&&mY>10&&mY<50)){
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				}else{
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
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

	public static void p(Object o){System.out.println(o);}
	public static int r(int x){return (int)(Math.random()*x);}
}