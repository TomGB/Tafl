import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import java.awt.image.*;

class TextBoxController{
  TextBox reset,load,save,rules,undo,blackWinText,whiteWinText,whiteTurnText,blackTurnText;
  Tafl tafl;
  int textHeight = 40;
  int textWidth = 200;
  int rulesTextOffsetX = 80;
  int rulesTextOffsetY = 70;
  int sizeX=800, sizeY=800;
  Image undoimg;
  String rulesText = "";

  public TextBoxController(Tafl _tafl){
    tafl = _tafl;
    loadRulesText("../assets/rules.txt");
    loadUndoImage("../assets/undo.png");

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

  public void checkButtons(int x, int y){
    if(!tafl.whiteWin && !tafl.blackWin && undo.inside(x,y)){
      p("undo clicked");
      tafl.undo();
    }else if(rules.inside(x,y)){
      p("display rules");
      tafl.rules();
    }else if(!tafl.rules&&save.inside(x,y)){
      p("save game");
      tafl.save();
    }else if(!tafl.rules&&load.inside(x,y)){
      p("load game");
      tafl.load();
    }else if(!tafl.rules&&reset.inside(x,y)){
      p("reset game");
      tafl.reset();
    }
  }

  public void loadUndoImage(String path){
    try{
      undoimg = ImageIO.read(new File(path));
    }catch(IOException e){
      p("error reading undo image");
    }
  }

  public void drawRulesText(Graphics g){
    g.setColor(new Color(255,255,255,210));
    g.fillRect(60,60,sizeX-120,sizeY-120);
    g.setColor(Color.black);
    g.drawRect(60,60,sizeX-120,sizeY-120);
    for (String line : rulesText.split("\n")){
          g.drawString(line, rulesTextOffsetX, rulesTextOffsetY += g.getFontMetrics().getHeight());
    }
    rulesTextOffsetY = 70;
  }

  public void loadRulesText(String path){
    try(BufferedReader br = new BufferedReader(new FileReader(path))) {
      String line;
      while ((line = br.readLine()) != null) {
        rulesText += line+"\n";
      }
    }catch(IOException e){
      p("error reading rules.txt");
    }
  }

  public static void p(Object o){System.out.println(o);}
  public static int r(int x){return (int)(Math.random()*x);}

}
