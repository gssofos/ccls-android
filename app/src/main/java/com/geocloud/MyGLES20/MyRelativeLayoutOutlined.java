package com.geocloud.MyGLES20;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class MyRelativeLayoutOutlined extends RelativeLayout{

	public MyRelativeLayoutOutlined(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setWillNotDraw(false) ;
	}
	
	
	public MyRelativeLayoutOutlined(Context context, AttributeSet attrs) {
		super(context);
		// TODO Auto-generated constructor stub
		setWillNotDraw(false) ;
	}
	
	
	 @Override
	    protected void onDraw(Canvas canvas) {
	        /*
	        Paint fillPaint = new Paint();
	        fillPaint.setARGB(255, 0, 255, 0);
	        fillPaint.setStyle(Paint.Style.FILL);
	        canvas.drawPaint(fillPaint) ;
	        */

	        Paint strokePaint = new Paint();
	        strokePaint.setARGB(255, 255, 255, 255);
	        strokePaint.setStyle(Paint.Style.STROKE);
	        strokePaint.setStrokeWidth(1);  
	        Rect r = canvas.getClipBounds() ;
	        Rect outline = new Rect( 1,1,r.right-1, r.bottom-1) ;
	        canvas.drawRect(outline, strokePaint) ;
	    }
	 

}
