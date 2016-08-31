import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

class TextBox {

	int posX, posY, width, height;
	String text;
	Image image;
	final Color light = new Color(255,255,255,200);
	final Color dark = new Color(0,0,0,40);
	boolean active = true;
	boolean visible = true;

	public TextBox(int _posX, int _posY, int _width, int _height, String _text){
		posX = _posX;
		posY = _posY;
		width = _width;
		height = _height;
		text = _text;
	}
	public TextBox(int _posX, int _posY, int _width, int _height, Image _image){
		posX = _posX;
		posY = _posY;
		width = _width;
		height = _height;
		image = _image;
	}
	public void draw(Graphics g){
		if(visible){
			g.setColor(active?light:dark);
			g.fillRect(posX,posY,width,height);
			g.setColor(Color.black);
			g.drawRect(posX,posY,width,height);
			if(text!=null){
				g.drawString(text,posX+20,posY+26);
			}else{
				g.drawImage(image,74,12, null);
			}
		}else{
			p("invisible" + text);
		}
	}

	public void setActive(boolean input){
		active = input;
	}

	public void setVisible(boolean input){
		visible = input;
	}

	/**
			deactivate when in active
	*/

	public boolean inside(int x, int y){
		return (x>posX&&x<posX+width&&y>posY&&y<posY+height && active && visible);
	}

	public static void p(Object o){System.out.println(o);}
	public static int r(int x){return (int)(Math.random()*x);}
}
