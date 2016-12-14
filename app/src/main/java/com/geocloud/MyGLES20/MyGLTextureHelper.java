package com.geocloud.MyGLES20;


	import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

	public class MyGLTextureHelper {

		public static int loadTexture(final Context context, final int resourceId)
		{
			final int[] textureHandle = new int[1];

			GLES20.glGenTextures(1, textureHandle, 0);

			if (textureHandle[0] != 0)
			{
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inScaled = false;	// No pre-scaling

				// Read in the resource
				final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

				// Bind to the texture in OpenGL
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

				// Set filtering
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

				// Load the bitmap into the bound texture.
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

				// Recycle the bitmap, since its data has been loaded into OpenGL.
				bitmap.recycle();						
			}

			if (textureHandle[0] == 0)
			{
				
				throw new RuntimeException("Error loading texture.");
			}
			//Log.i(" textureHandle[0]", textureHandle[0]+"");
			return textureHandle[0];
		}
		
		
		
		
		
		
		public static int loadTextureFromString(final Context context, String name)
		{
			final int[] textureHandle = new int[1];

			GLES20.glGenTextures(1, textureHandle, 0);

			if (textureHandle[0] != 0)
			{
				final BitmapFactory.Options options = new BitmapFactory.Options();
				
				options.inScaled = false;	// No pre-scaling

				// Read in the resource
				// Create an empty, mutable bitmap
				final Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_4444);
				// get a canvas to paint over the bitmap
				Canvas canvas = new Canvas(bitmap);
				bitmap.eraseColor(0);
				
			
				
				// Draw the text
				Paint textPaint = new Paint();
				textPaint.setTextSize(32);
				textPaint.setAntiAlias(true);
				textPaint.setARGB(0xff, 0xff, 0xff, 0xff);
				// draw the text centered
				canvas.drawText(name, 16,112, textPaint);
				
				
				//final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

				// Bind to the texture in OpenGL
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

				// Set filtering
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

				// Load the bitmap into the bound texture.
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

				// Recycle the bitmap, since its data has been loaded into OpenGL.
				bitmap.recycle();						
			}

			if (textureHandle[0] == 0)
			{
				
				throw new RuntimeException("Error loading texture.");
			}
			//	Log.i(" textureHandle[0]String : " + name, textureHandle[0]+"");
			return textureHandle[0];
		}
		
	
		
		
		
		
		public static int loadTextureFromBitmap(final Context context, final Bitmap bitmap)
		{
			final int[] textureHandle = new int[1];

			GLES20.glGenTextures(1, textureHandle, 0);

			if (textureHandle[0] != 0)
			{
				

				// Bind to the texture in OpenGL
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

				// Set filtering
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

				// Load the bitmap into the bound texture.
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

				// Recycle the bitmap, since its data has been loaded into OpenGL.
				//bitmap.recycle();						
			}

			if (textureHandle[0] == 0)
			{
				
				throw new RuntimeException("Error loading texture.");
			}
			//Log.i(" textureHandle[0]Bitmap", textureHandle[0]+"");
			return textureHandle[0];
		}
		
		
		
		public static int loadTextureFromFile(final Context context, final String filename)
		{
			 final int[] textureHandle = new int[1];
			     GLES20.glGenTextures(1, textureHandle, 0);
		        if (textureHandle[0] != 0)
		        {
		        	File imagefile = new File(filename);
		        	Bitmap bitmap = BitmapFactory.decodeFile( imagefile.getAbsolutePath());
		        	
		        	if(bitmap==null){
		        		imagefile.delete();
		        	}else{
		        		
		        	
		        	
						// Bind to the texture in OpenGL
						GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
		
						// Set filtering
						GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
						GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		
						// Load the bitmap into the bound texture.
						GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		
						// Recycle the bitmap, since its data has been loaded into OpenGL.
						bitmap.recycle();	
		        	}
			}

			if (textureHandle[0] == 0)
			{	
				
				throw new RuntimeException("Error loading texture.");
			}
			//Log.i(" textureHandle[0]File", textureHandle[0]+"");
			
			return textureHandle[0];
		}
		
		
	}