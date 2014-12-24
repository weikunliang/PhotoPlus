package edu.cmu.weikunl.photoplus;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

// the class which contains each of the pieces which forms the collage
public class Piece {
	
	// the starting x and y position of the image
	private float xpos;
	private float ypos;
	
	// the starting x and y position of the clip (fixed)
	private float xclip;
	private float yclip;
	
	// width and height of the image
	private float width;
	private float height;
	
	// the current image shown
	private Bitmap img;

	
	public Piece(float x, float y, float xc, float yc, float w, float h, Bitmap image){
		xpos = x;
		ypos = y;
		xclip = xc;
		yclip = yc;
		width = w;
		height = h;
		img = image;
	}
	
	public float getW(){
		return width;
	}
	
	public float getH(){
		return height;
	}
	
	public float getX(){
		return xpos;
	}
	
	public float getY(){
		return ypos;
	}
	
	public float getXClip(){
		return xclip;
	}
	
	public float getYClip(){
		return yclip;
	}
	
	public void setX(float x){
		xpos = x;
	}
	
	public void setY(float y){
		ypos = y;
	}
	
	public Bitmap getBitmap(){
		return img;
	}
	
	public void setBitmap(Bitmap b){
		img = b;
	}
	
	public void draw(Canvas onCanvas){	
		Paint p = new Paint();
		
		Path path = new Path();
		RectF clip = new RectF(xclip, yclip, xclip+width, yclip+height);
		path.addRect(clip, Path.Direction.CW);
		onCanvas.clipPath(path, Region.Op.REPLACE);
		onCanvas.drawBitmap(img, xpos, ypos, p);
	}

}
