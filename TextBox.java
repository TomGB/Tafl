class TextBox {

	int posX, posY, width, height;
	String text;

	TextBox(int _posX, int _posY, int _width, int _height, String _text){
		posX = _posX;
		posY = _posY;
		width = _width;
		height = _height;
		text = _text;
	}

	TextBox(int _posX, int _posY, int _height, String _text){
		posX = _posX;
		posY = _posY;
		height = _height;
		text = _text;
		width = ;
	}
}