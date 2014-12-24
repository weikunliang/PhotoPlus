package edu.cmu.weikunl.photoplus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class FourOne extends ActionBarActivity {
	
	FourOneView v;
	private static int RESULT_LOAD_IMAGE = 1;
	
	// three boolean variables to check which collage piece the user clicked
	private boolean isTop = false;
	private boolean isBotLeft = false;
	private boolean isBotRight = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fourone);
		v = (FourOneView)findViewById(R.id.custView);
	}
	
	// the button pressed for the top piece which replaces the current image with an uploaded image
	public void btnPressedTop(View view){
		isTop = true;
		isBotLeft = false;
		isBotRight = false;
		v = (FourOneView)findViewById(R.id.custView);
		((View)v).buildDrawingCache();
		Intent i = new Intent(
				Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				 
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}
	
	// the button pressed for the bottom left piece which replaces the current image with an uploaded image
	public void btnPressedBotLeft(View view){
		isTop = false;
		isBotLeft = true;
		isBotRight = false;
		v = (FourOneView)findViewById(R.id.custView);
		((View)v).buildDrawingCache();
		Intent i = new Intent(
				Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				 
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}
	
	// the button pressed for the bottom middle piece which replaces the current image with an uploaded image
	public void btnPressedBotMid(View view){
		isTop = false;
		isBotLeft = false;
		isBotRight = false;
		Intent i = new Intent(
				Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				 
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}
	
	// the button pressed for the bottom right piece which replaces the current image with an uploaded image
	public void btnPressedBotRight(View view){
		isTop = false;
		isBotLeft = false;
		isBotRight = true;
		Intent i = new Intent(
				Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				 
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}
	
	// the save button which saves the current image in the gallery
	public void btnPressedSave(View view){
		v = (FourOneView)findViewById(R.id.custView);
		// refreshes the drawing cache
		if(!v.isDrawingCacheEnabled()){
			v.setDrawingCacheEnabled(true);
		}else{
			v.destroyDrawingCache();
			v.setDrawingCacheEnabled(true);
		}
		Bitmap b = v.getDrawingCache(); // the current image on the page
		
		File mediaStorageDir; // new file which stores the image
		
		// checks to see if there is external storage
		if(Environment.getExternalStorageState() != null){
			// makes a new folder in the external storage called MyCameraApp to store the image
			mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
		              Environment.DIRECTORY_PICTURES), "MyCameraApp");
		} else { // if no external storage found, then create MyCameraApp in local directory
			mediaStorageDir = new File(Environment.getDataDirectory(), "MyCameraApp");
		}
		if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return;
	        }
	    }
		
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); // the current date and time
	    File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg"); // the file name as current date and time
		try { // exports the image to the gallery
			b.compress(CompressFormat.JPEG, 95, new FileOutputStream(mediaFile));
			Toast.makeText(this, "Image saved successfuly!", Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
		}
		MediaScannerConnection.scanFile(this, new String[] { mediaFile.getPath() }, null, null);
	}
	
	// responsible for uploading the image
	@SuppressLint("NewApi") @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // uploads the image from the gallery
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            
            // gets the size of the screen 
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;
            
            // the bitmap uploaded
            Bitmap b = BitmapFactory.decodeFile(picturePath);
            
            // scale it to fit the screen
            
            double newW = (double)screenWidth;
            double scale = newW/b.getWidth(); // assuming width > height
            double newH = scale * b.getHeight();
            
            // if height > width
            if(newH > screenHeight){
            	newH = (double)screenHeight;
            	scale = newH/b.getHeight();
            	newW = scale * b.getWidth();
            }

            Bitmap resizedBitmap = getResizedBitmap(b, (int)(newH*0.9), (int)(newW*0.9));
            
            // to figure out which piece of the collage the bitmap should go to
            if(isTop){
            	v.setBitmapTop(resizedBitmap);
            } else if (!isTop && !isBotLeft && !isBotRight){
            	v.setBitmapBotMid(resizedBitmap);
            } else if (!isTop && isBotLeft && !isBotRight){
            	v.setBitmapBotLeft(resizedBitmap);
            } else if (!isTop && !isBotLeft && isBotRight){
            	v.setBitmapBotRight(resizedBitmap);
            }
        }
    }
	
	// the function that resizes the bitmap for us given the bitmap and its new dimensions
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {	
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		 
		// CREATE A MATRIX FOR THE MANIPULATION	 
		Matrix matrix = new Matrix();	 
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);
		 
		// RECREATE THE NEW BITMAP 
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		 
		return resizedBitmap;
		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.four_one, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
