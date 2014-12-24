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


public class FourTwoView extends View {
	
	public static final int NOT_SELECTED = -1;
	// initializes the original bitmaps
	public Bitmap imgTopLeft = BitmapFactory.decodeResource(getResources(), R.drawable.purple);
	public Bitmap imgTopRight = BitmapFactory.decodeResource(getResources(), R.drawable.pink);
	public Bitmap imgBotLeft = BitmapFactory.decodeResource(getResources(), R.drawable.blue);
	public Bitmap imgBotRight = BitmapFactory.decodeResource(getResources(), R.drawable.yellow);

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
	float rectW = (squareSize-(3*offset))/2;
	
	// initializes the different pieces
	Piece topleft = new Piece(startX+offset, startY+offset,startX+offset, startY+offset, rectW, rectH, getBitmapTopLeft());
	Piece topright = new Piece(startX+(2*offset)+rectW, startY+offset,startX+(2*offset)+rectW, startY+offset, rectW, rectH, getBitmapTopRight());
	Piece botleft = new Piece(startX+offset, startY+offset+rectH+offset, startX+offset, startY+offset+rectH+offset, rectW, rectH, getBitmapBotLeft());
	Piece botright = new Piece(startX+(2*offset)+rectW, startY+offset+rectH+offset, startX+(2*offset)+rectW, startY+offset+rectH+offset, rectW, rectH, getBitmapBotRight());
	
	// responsible for storing which piece was moved and the point it was dragged on
	protected Piece _dragFocus = null;
	protected float _grabPointX = 0.0f;
	protected float _grabPointY = 0.0f;
	
	// responsible for making sure the dragged piece stays in the box
	protected float leftX = 0;
	protected float leftY = 0;
	protected float rightX = 0;
	protected float rightY = 0;
	
	
	public FourTwoView(Context context) {
		this(context, null);
	}
	

	public FourTwoView(Context context, AttributeSet attr) {
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
	
	// sets the bottom left bitmap
	public void setBitmapBotLeft(Bitmap b){
		imgBotLeft = b;
		botleft.setBitmap(b);
		invalidate();
	}
	
	// sets the bottom right bitmap
	public void setBitmapBotRight(Bitmap b){
		imgBotRight = b;
		botright.setBitmap(b);
		invalidate();
	}
	
	public Bitmap getBitmapTopLeft(){
		return imgTopLeft;
	}
	
	public Bitmap getBitmapTopRight(){
		return imgTopRight;
	}
	
	public Bitmap getBitmapBotLeft(){
		return imgBotLeft;
	}
	
	public Bitmap getBitmapBotRight(){
		return imgBotRight;
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
		botleft.draw(canvas);
		botright.draw(canvas);
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
		} else if(x >= botleft.getXClip() && x <= botleft.getXClip()+botleft.getW() && y >= botleft.getYClip() && y <= botleft.getYClip()+botleft.getH()){
			focus = botleft;
		} else if(x >= topright.getXClip() && x <= topright.getXClip()+topright.getW() && y >= topright.getYClip() && y <= topright.getYClip()+topright.getH()){
			focus = topright;
		} else if(x >= botright.getXClip() && x <= botright.getXClip()+botright.getW() && y >= botright.getYClip() && y <= botright.getYClip()+botright.getH()){
			focus = botright;
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
