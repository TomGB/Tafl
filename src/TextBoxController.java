import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import java.awt.image.*;

class TextBoxController{
  TextBox reset,load,save,rules,undo,blackWinText,whiteWinText,whiteTurnText,blackTurnText;

  public TextBoxController(){
    reset = new TextBox(370,10,80,40,"Reset");
    load = new TextBox(460,10,80,40,"Load");
    save = new TextBox(550,10,80,40,"Save");
    rules = new TextBox(640,10,80,40,"Rules");
    undo = new TextBox(70,10,50,40,undoimg);
    blackWinText = new TextBox((sizeX-textWidth)/2,(sizeY-textHeight)/2,textWidth,textHeight,"Black has Won");
    whiteWinText = new TextBox((sizeX-textWidth)/2,(sizeY-textHeight)/2,textWidth,textHeight,"White has Won");
    whiteTurnText = new TextBox(130,10,textWidth,textHeight,"White's Turn");
    blackTurnText = new TextBox(130,10,textWidth,textHeight,"Black's Turn");
  }


	public void drawButtons(Boolean active, Graphics g){
		if(tafl.whiteTurn){
			whiteTurnText.draw(active,g);
		}else{
			blackTurnText.draw(active,g);
		}
		undo.draw(active,g);
		save.draw(active,g);
		load.draw(active,g);
		reset.draw(active,g);
		rules.draw(true,g);
	}

}
