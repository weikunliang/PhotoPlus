package edu.cmu.weikunl.photoplus;
import edu.cmu.weikunl.photoplus.R;
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


public class ThreeTwoView extends View {
	public static final int NOT_SELECTED = -1;
	// initializes the original bitmaps
	public Bitmap imgLeft = BitmapFactory.decodeResource(getResources(), R.drawable.pink);
	public Bitmap imgRightTop = BitmapFactory.decodeResource(getResources(), R.drawable.yellow);
	public Bitmap imgRightBot = BitmapFactory.decodeResource(getResources(), R.drawable.blue);

	// gets the size of the screen 
    int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
    int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
    
    // calculates the starting x and y points
    int squareSize = (int)(0.85*screenWidth);
    float startX = ((float)(screenWidth-squareSize))/2;
    float startY = ((float)(screenHeight-squareSize))/2;
    int offset = 5;
    
    // the rectangle width and height
	float rectW = (squareSize-(3*offset))/2;
	float rectH = squareSize-(2*offset);
	
	// initializes the different pieces
	Piece left = new Piece(startX+offset, startY+offset,startX+offset, startY+offset, rectW, rectH, getBitmapLeft());
	Piece righttop = new Piece(startX+rectW+(2*offset), startY+offset, startX+rectW+(2*offset), startY+offset, rectW, rectH/2, getBitmapRightTop());
	Piece rightbot = new Piece(startX+rectW+(2*offset), startY+(2*offset)+rectH/2, startX+rectW+(2*offset), startY+(2*offset)+rectH/2, rectW, rectH/2, getBitmapRightBot());
	
	// responsible for storing which piece was moved and the point it was dragged on
	protected Piece _dragFocus = null;
	protected float _grabPointX = 0.0f;
	protected float _grabPointY = 0.0f;
	
	// responsible for making sure the dragged piece stays in the box
	protected float leftX = 0;
	protected float leftY = 0;
	protected float rightX = 0;
	protected float rightY = 0;
	
	
	public ThreeTwoView(Context context) {
		this(context, null);
	}
	

	public ThreeTwoView(Context context, AttributeSet attr) {
		super(context, attr);
		// TODO Auto-generated constructor stub
	}
	
	// sets the left bitmap
	public void setBitmapLeft(Bitmap b){
		imgLeft = b;
		left.setBitmap(b);
		invalidate();
	}
	
	// sets the right top bitmap
	public void setBitmapRightTop(Bitmap b){
		imgRightTop = b;
		righttop.setBitmap(b);
		invalidate();
	}
	
	// sets the right bottom bitmap
	public void setBitmapRightBot(Bitmap b){
		imgRightBot = b;
		rightbot.setBitmap(b);
		invalidate();
	}
	
	public Bitmap getBitmapLeft(){
		return imgLeft;
	}
	
	public Bitmap getBitmapRightTop(){
		return imgRightTop;
	}
	
	public Bitmap getBitmapRightBot(){
		return imgRightBot;
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
		left.draw(canvas);
		righttop.draw(canvas);
		rightbot.draw(canvas);
	}
	
	public boolean onTouchEvent(MotionEvent event){
		float x = event.getX(); // the x coordinate of the touch
		float y = event.getY(); // the y coordinate of the touch
		Piece focus = null;
		
		// check to see which piece was touched
		if(x >= left.getXClip() && x <= left.getXClip()+left.getW() && y >= left.getYClip() && y <= left.getYClip()+left.getH()){
			focus = left;
		} else if(x >= righttop.getXClip() && x <= righttop.getXClip()+righttop.getW() && y >= righttop.getYClip() && y <= righttop.getYClip()+righttop.getH()){
			focus = righttop;
		} else if(x >= rightbot.getXClip() && x <= rightbot.getXClip()+rightbot.getW() && y >= rightbot.getYClip() && y <= rightbot.getYClip()+rightbot.getH()){
			focus = rightbot;
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
