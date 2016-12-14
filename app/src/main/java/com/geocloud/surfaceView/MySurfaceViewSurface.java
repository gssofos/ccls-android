package com.geocloud.surfaceView;

import com.example.test2.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceViewSurface  extends SurfaceView {
	private Bitmap 			bmp;
	private Bitmap 			bmp2;
    private SurfaceHolder 	holder;
    private MyApp_S			app;
    private int				level	=	17;
    
    private float 			startpx;
    private float 			startpy;
    private float 			lastpx	=	0;
    private float 			lastpy	=	0;
    
    private float 			lastl	=	23.709f;
    private float 			lastf	=	37.912f;
    private float 			startl	=	23.709f;
    private float 			startf	=	37.912f;
    private float 			bima	=	0;
    
    private float			l1;
    private float			f1;
    private int			startIndX;
    private int			startIndY;
    private float 			left=0;
    private float 			top=0;
    private TileGrid		grid;
    
	public MySurfaceViewSurface(Context context, MyApp_S app) {
		
		super(context);
		holder 			= 	getHolder();
		this.app		=	app;
		this.grid		=	new TileGrid(app);
		
		holder.addCallback(new SurfaceHolder.Callback() {
			 
             @Override
             public void surfaceDestroyed(SurfaceHolder holder) {
             }

             @Override
             public void surfaceCreated(SurfaceHolder holder) {
                    Canvas c = holder.lockCanvas(null);
                    onDraw(c);
                    holder.unlockCanvasAndPost(c);
             }

             @Override
             public void surfaceChanged(SurfaceHolder holder, int format,
                           int width, int height) {
             }
      });
			final float[] res 	= 	 app.ktima.getOriginFromLonLat(lastl,lastf,level);
			this.l1=res[0];
			this.f1=res[1];
			this.startIndX=(int) res[2];
			this.startIndY=(int) res[3];
			this.bima		=	app.ktima.get_bima(this.level) ;
			
			
			this.grid.add(lastl,lastf,level);
			//bmp = BitmapFactory.decodeResource(getResources(), R.drawable.football);
			//bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.football);
			// TODO Auto-generated constructor stub
	}


	
	@Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
            	left=lastpx+x-startpx;
            	top=lastpy+y-startpy;
            	break;
            case MotionEvent.ACTION_DOWN:
            	startpx=x;
            	startpy=y;
            	break;
            case MotionEvent.ACTION_UP:
            	//mRenderer.setLastXY();
            	
            	lastpx=left;
            	lastpy=top;
               
            	lastl = startl - lastpx*bima/512;
            	lastf = startf + lastpy*bima/512;
                
            	
            	final float[] res 	= 	 app.ktima.getOriginFromLonLat(lastl,lastf,level);
    			this.l1=res[0];
    			this.f1=res[1];
    			
    			Log.i("l1,f1",l1 + "," + f1);
    			
    			if(!grid.itemExists(l1, f1)){
    				Log.i("l1,f1",l1 + "," + f1);
    				this.grid.add(lastl,lastf,level);
    			}
    			
    			if(!grid.itemExists(l1+bima, f1)){
    				Log.i("l1,f1",l1 + "," + f1);
    				this.grid.add(lastl+bima,lastf,level);
    			}
    			
    			
    			if(!grid.itemExists(l1, f1+bima)){
    				Log.i("l1,f1",l1 + "," + f1);
    				this.grid.add(lastl,lastf+bima,level);
    			}
    			if(!grid.itemExists(l1+bima, f1+bima)){
    				Log.i("l1,f1",l1 + "," + f1);
    				this.grid.add(lastl+bima,lastf+bima,level);
    			}
    			
    			
            	break;
            default:
            		//mPreviousX = x;
                   // mPreviousY = y;	
            		
            		
        }

        Canvas c = holder.lockCanvas(null);
        onDraw(c);
        holder.unlockCanvasAndPost(c);
        return true;
    }
     
     
     @Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);
		
		int i =0;
		for(i=0;i<=grid.item.size()-1;i++){
			Tile tmp	=	grid.item.get(i);
			
			if(tmp.loaded){
				
				canvas.drawBitmap(tmp.bmp, left-(startIndX-tmp.indX)*512, top+(startIndY-tmp.indY)*512, null);
			}
		}
		  
	}
     
     
    
     
}
