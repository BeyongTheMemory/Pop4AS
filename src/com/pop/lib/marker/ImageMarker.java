
package com.pop.lib.marker;

import com.pop.lib.MixContextInterface;
import com.pop.lib.MixStateInterface;
import com.pop.lib.MixUtils;
import com.pop.lib.gui.PaintScreen;
import com.pop.lib.gui.ScreenLine;
import com.pop.lib.marker.Marker;
import com.pop.lib.marker.PluginMarker;
import com.pop.lib.marker.draw.DrawCommand;
import com.pop.lib.marker.draw.DrawImage;
import com.pop.lib.marker.draw.DrawTextBox;
import com.pop.lib.marker.draw.ParcelableProperty;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * @author A.Egal
 * An custom marker that shows a bitmap image in the augmented reality browser
 */
public class ImageMarker extends PluginMarker{

	public static final int MAX_OBJECTS = 20;
	private Bitmap image; 
	private ScreenLine pPt = new ScreenLine();
	public static final int OSM_URL_MAX_OBJECTS = 5;
    public String type;
//    public static final String geren = "geren";
//    public static final String gonggong = "gonggong";
//    public static final String jindian = "jindian";
//    public static final String shangye = "shangye";
    private long popid;
	public ImageMarker(int id, String title, double latitude, double longitude,
			double altitude, String URL, int type, int color) {
		super(id, title, latitude, longitude, altitude, URL, type, color);
		this.popid = id;
	}

	@Override
	public int getMaxObjects() {
		return MAX_OBJECTS;
	}
	
	@Override
	public DrawCommand[] remoteDraw(){
		DrawCommand[] dCommands = new DrawCommand[2];
		//dCommands[0] = new DrawImage(isVisible, signMarker, image);
		dCommands[0] = new DrawImage(isVisible, signMarker, image);
		//不再绘制下面的框了
		//dCommands[1] = new DrawTextBox(isVisible, distance, title, underline, textBlock, txtLab, signMarker);
		return dCommands;
	}

	@Override
	public void setExtras(String name, ParcelableProperty parcelableProperty) {
		if(name.equals("bitmap")){
			image = (Bitmap)parcelableProperty.getObject();
		}
	}
	
	public Bitmap getBitmap(){
		return image;
	}
	
	public void setBitmap(Bitmap image){
		double scale = this.getDistance();
		//根据距离缩放,实际图片会除以这个值
	
		if(scale < 100){
			scale =1.25;
     	}
		else if(scale < 200){
			scale = 1.33;
		}
		else if(scale < 300){
			scale = 1.43;
		}
		else if(scale < 400){
			scale = 1.53;
		}
		else if(scale < 500){
			scale = 1.66;
		}
		else if(scale < 600){
			scale = 2;
		}
		else if(scale < 700){
			scale = 2.22;
		}
		else if(scale < 800){
			scale = 2.5;
		}
		else if(scale < 900){
			scale = 2.9;
		}
		else{
			scale = 3.3;
		}
		
		Bitmap bitmap = Bitmap.createScaledBitmap(image, (int)(image.getWidth()/scale), (int)(image.getHeight()/scale), true);
		this.image = bitmap;
	}

	@Override
	public void draw(PaintScreen dw) {
		DrawCommand[] drs = remoteDraw();
		drs[0].draw(dw);
		//这是下面的矿
		//drs[1].draw(dw);
	}

	@Override
	public boolean fClick(float x, float y, MixContextInterface ctx,
			MixStateInterface state) {
		boolean evtHandled = false;
		if (isClickValid(x, y)) {
			evtHandled = true;
		}
		
		return evtHandled;
	
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public int compareTo(Marker another) {
		Marker leftPm = this;
		Marker rightPm = another;

		return Double.compare(leftPm.getDistance(), rightPm.getDistance());
	}
//	private boolean isClickValid(float x, float y) {
//        //����Ƿ���Ч
//		
//		//�����ǲ���Ծ��������ʾAR��ͼ�����ǲ���Ҫ������ĵ��
//
//		if (!isActive() && !this.isVisible)
//			return false;
//
//		float currentAngle = MixUtils.getAngle(cMarker.x, cMarker.y,
//				signMarker.x, signMarker.y);
//		Log.v("angle","angle"+currentAngle);
//		//TODO adapt the following to the variable radius!  
//		pPt.x = x - signMarker.x;
//		pPt.y = y - signMarker.y;
//		pPt.rotate((float) Math.toRadians(-(currentAngle + 90)));
//		pPt.x += txtLab.getX();
//		pPt.y += txtLab.getY();
//
//		float objX = txtLab.getX() - txtLab.getWidth() / 2;
//		float objY = txtLab.getY() - txtLab.getHeight() / 2;
//		float objW = txtLab.getWidth();
//		float objH = txtLab.getHeight();
//
//		if (pPt.x > objX && pPt.x < objX + objW && pPt.y > objY
//				&& pPt.y < objY + objH) {
//			return true;
//		} else {
//			return false;
//		}
//	}
	//检查点击事件的有效性,碰撞可以根据这个来做
	private boolean isClickValid(float x, float y) {
//		if(!isVisible&&!isActive()){
//			return false;
//		}
		if(!isVisible){
			return false;
		}
		float left = signMarker.x - (getBitmap().getWidth()/2);
		float top = signMarker.y - (getBitmap().getHeight() / 2);
		float right = left + getBitmap().getWidth();
		float bottom = top+getBitmap().getHeight();
		if(x > left && x < right && y > top && y < bottom){
			return true;
		}
		else{
			return false;
		}
	}

	public long getPopid() {
		return popid;
	}

	public void setPopid(long popid) {
		this.popid = popid;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}