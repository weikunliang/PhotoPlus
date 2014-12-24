package edu.cmu.weikunl.photoplus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ThreeOneView extends View {
	public static final int NOT_SELECTED = -1;
	// initializes the original bitmaps
	public Bitmap imgTopLeft = BitmapFactory.decodeResource(getResources(), R.drawable.yellow);
	public Bitmap imgTopRight = BitmapFactory.decodeResource(getResources(), R.drawable.blue);
	public Bitmap imgBot = BitmapFactory.decodeResource(getResources(), R.drawable.pink);

	// gets the size of the screen 
    int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
    int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
    
    // calculates the starting x and y points
    int squareSize = (int)(0.85*screenWidth);
    float startX = ((float)(screenWidth-squareSize))/2;
    float startY = ((float)(screenHeight-squareSize))/2;
    int offset = 5;
    
    // the rectangle width and height
	float rectH = (squareSize-(3*offset))/2;
	float rectW = squareSize-(2*offset);
	
	// initializes the different pieces
	Piece topleft = new Piece(startX+offset, startY+offset,startX+offset, startY+offset, rectW/2, rectH, getBitmapTopLeft());
	Piece topright = new Piece(startX+(2*offset)+(rectW/2), startY+offset,startX+offset+rectW/2, startY+offset, rectW/2, rectH, getBitmapTopRight());
	Piece bot = new Piece(startX+offset, startY+offset+rectH+offset, startX+offset, startY+offset+rectH+offset, rectW, rectH, getBitmapBot());
	
	// responsible for storing which piece was moved and the point it was dragged on
	protected Piece _dragFocus = null;
	protected float _grabPointX = 0.0f;
	protected float _grabPointY = 0.0f;
	
	// responsible for making sure the dragged piece stays in the box
	protected float leftX = 0;
	protected float leftY = 0;
	protected float rightX = 0;
	protected float rightY = 0;
	
	
	public ThreeOneView(Context context) {
		this(context, null);
	}
	

	public ThreeOneView(Context context, AttributeSet attr) {
		super(context, attr);
		// TODO Auto-generated constructor stub
	}
	
	// sets the top left bitmap
	public void setBitmapTopLeft(Bitmap b){
		imgTopLeft = b;
		topleft.setBitmap(b);
		invalidate();
	}
	
	// sets the top right bitmap
	public void setBitmapTopRight(Bitmap b){
		imgTopRight = b;
		topright.setBitmap(b);
		invalidate();
	}
	
	// sets the bottom bitmap
	public void setBitmapBot(Bitmap b){
		imgBot = b;
		bot.setBitmap(b);
		invalidate();
	}
	
	public Bitmap getBitmapTopLeft(){
		return imgTopLeft;
	}
	
	public Bitmap getBitmapTopRight(){
		return imgTopRight;
	}
	
	public Bitmap getBitmapBot(){
		return imgBot;
	}
	
	// called whenever the image is dragged
	public void requestDragFocus(Piece p, float grabX, float grabY) {
		if (p != null) {
			_dragFocus = p; // the piece that is dragged
			_grabPointX = grabX; // the x point which it was grabbed at
			_grabPointY = grabY; // the y point which it was grabbed at
			
			// the boundaries to make sure it stays in the square
			leftX = _dragFocus.getXClip();
			leftY = _dragFocus.getYClip();
			rightX = _dragFocus.getXClip()+_dragFocus.getW() - (_dragFocus.getBitmap().getWidth());
			rightY = _dragFocus.getYClip()+_dragFocus.getH() - (_dragFocus.getBitmap().getHeight());
		}
	}
	
	public void releaseDragFocus() {_dragFocus = null;}
	
	@Override
	protected void onDraw(Canvas canvas){
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		canvas.drawRect(new RectF(startX, startY, startX+squareSize, startY+squareSize), paint);
		
		// draws all the pieces
		bot.draw(canvas);
		topleft.draw(canvas);
		topright.draw(canvas);
	}
	
	public boolean onTouchEvent(MotionEvent event){
		float x = event.getX(); // the x coordinate of the touch
		float y = event.getY(); // the y coordinate of the touch
		Piece focus = null;
		
		// check to see which piece was touched
		if(x >= topleft.getXClip() && x <= topleft.getXClip()+topleft.getW() && y >= topleft.getYClip() && y <= topleft.getYClip()+topleft.getH()){
			focus = topleft;
		} else if(x >= bot.getXClip() && x <= bot.getXClip()+bot.getW() && y >= bot.getYClip() && y <= bot.getYClip()+bot.getH()){
			focus = bot;
		} else if (x >= topright.getXClip() && x <= topright.getXClip()+topright.getW() && y >= topright.getYClip() && y <= topright.getYClip()+topright.getH()){
			focus = topright;
		}
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(focus != null){
					requestDragFocus(focus, focus.getX()-x, focus.getY()-y);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(_dragFocus != null){
					float xdif = _grabPointX;
					float ydif = _grabPointY;
					
					// redraws the x and y and makes sure that they are within boundaries
					if((x+xdif<=leftX) && (x+xdif>=rightX)){
						_dragFocus.setX(x+xdif);
						invalidate();
					} 
					if((y+ydif<=leftY) && (y+ydif>=rightY)){
						_dragFocus.setY(y+ydif);
						invalidate();
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				releaseDragFocus();
				break;
			case MotionEvent.ACTION_CANCEL:
				break;
				
		}
		return true;
	}
}
