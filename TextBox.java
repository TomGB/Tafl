import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

class TextBox {

	int posX, posY, width, height;
	String text;
	Color light = new Color(255,255,255,200);
	Color dark = new Color(0,0,0,40);

	public TextBox(int _posX, int _posY, int _width, int _height, String _text){
		posX = _posX;
		posY = _posY;
		width = _width;
		height = _height;
		text = _text;
	}
	public void draw(Boolean state, Graphics g){
		g.setColor(state?light:dark);
		g.fillRect(posX,posY,width,height);
		g.setColor(Color.black);
		g.drawRect(posX,posY,width,height);
		g.drawString(text,posX+20,posY+26);
	}
	public boolean inside(int x, int y){
		return (x>posX&&x<posX+width&&y>posY&&y<posY+height);
	}
}