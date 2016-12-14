package com.geocloud.MyGLES20;

import android.widget.RelativeLayout;

public class MyRelativeLayoutParams extends RelativeLayout.LayoutParams {

	public MyRelativeLayoutParams(int w, int h,int left, int top) {
		super(w, h);
		this.topMargin=top;
		this.leftMargin=left;
		// TODO Auto-generated constructor stub
	}

}
