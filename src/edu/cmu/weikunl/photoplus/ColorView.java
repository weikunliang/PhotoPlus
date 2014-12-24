package edu.cmu.weikunl.photoplus;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

// This class is responsible for drawing the color square above the 
// palette to show the current color of the paint. 
public class ColorView extends View {
	
	// the paint responsible for drawing the color square 
	Paint p = new Paint();
	// the paint that draws the white outline
	Paint outline = new Paint();
	
	public ColorView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ColorView(Context context, AttributeSet attr) {
		super(context, attr);
		// TODO Auto-generated constructor stub
	}
	
	// called whenever the color is changed (usually after button pressed)
	public void setColor(int color){
		p.setColor(color);
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		float rx = 20;
		float ry = 20;
		outline.setColor(Color.WHITE);
		canvas.drawRoundRect (new RectF(0, 0, 100, 100), rx, ry, outline);
		canvas.drawRoundRect (new RectF(5, 5, 95, 95), rx, ry, p);
	}
}
