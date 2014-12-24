package edu.cmu.weikunl.photoplus;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View {
	ArrayList<Path> allPath = new ArrayList(); // the array list which stores all the path drawn on the screen
	ArrayList<Paint> allPaint = new ArrayList(); // the array list which stores all the paint corresponding to each path
	Path drawPath = new Path(); // the current path that draws on the screen
	Paint drawPaint = new Paint(); // the paint for the path
	Canvas drawCanvas = new Canvas(); // the paint for the canvas
	// current color of the paint
	int currColor = Color.BLACK;
	Bitmap img = null;
	
	// gets the size of the screen 
    int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
    int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
	
	public PaintView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public PaintView(Context context, AttributeSet attr) {
		super(context, attr);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		if(img != null){ // draws the bitmap
			int xpos = (screenWidth-img.getWidth())/2;
			int ypos = (screenHeight-img.getHeight())/2;
			canvas.drawBitmap(img, xpos, ypos, new Paint());
		}
		for(int i=0; i<allPath.size(); i++){ // draws all the path in the array list
			canvas.drawPath(allPath.get(i), allPaint.get(i));
		}
	}
	
	// sets the color of the paint for the path
	public void setColor(int color){
		invalidate();
		drawPaint.setColor(color);
		currColor = color;
	}
	
	public void setBitmap(Bitmap b){
		img = b;
	}
	
	public boolean onTouchEvent(MotionEvent event){
		float x = event.getX();
		float y = event.getY();
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// sets the properties of the paint 
				drawPaint.setAntiAlias(true);
				drawPaint.setStrokeWidth(20);
				drawPaint.setStyle(Paint.Style.STROKE);
				drawPaint.setStrokeJoin(Paint.Join.ROUND);
				drawPaint.setStrokeCap(Paint.Cap.ROUND);
				
				// move the draw path the where the screen was pressed and add the path
				// and color to their respective arraylists
				drawPath.moveTo(x, y);
				allPath.add(drawPath);
				allPaint.add(drawPaint);
				break;
			case MotionEvent.ACTION_MOVE:
				// moves the path to x, y
				drawPath.lineTo(x, y);
				break;
			case MotionEvent.ACTION_UP:
				// initializes a new path and paint to be stored in the array list
				drawPath = new Path();
				drawPaint = new Paint();
				drawPaint.setColor(currColor);
				break;
			case MotionEvent.ACTION_CANCEL:
				break;
				
		}
		invalidate();
		return true;
	}

}
