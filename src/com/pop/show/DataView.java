
package com.pop.show;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


//import com.pop.activity.BusnisePopActivity;
import com.pop.activity.PopInfoActivity;
import com.pop.data.DataHandler;
import com.pop.enume.PopTypeEnum;
import com.pop.gui.RadarPoints;
import com.pop.lib.MixUtils;
import com.pop.lib.gui.PaintScreen;
import com.pop.lib.gui.ScreenLine;
import com.pop.lib.marker.ImageMarker;
import com.pop.lib.marker.Marker;
import com.pop.lib.render.Camera;
import com.pop.lib.render.MixVector;
import com.pop.mgr.downloader.DownloadMgrImpl;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.widget.Toast;

/**
 * This class is able to update the markers and the radar. It also handles some
 * user events�
 * @author daniele
 * 
 */
public class DataView {

	/** current context */
	private MixContext mixContext;
	/** is the view Inited? */
	private boolean isInit;

	/** width and height of the view */
	private int width, height;

	/**
	 * _NOT_ the android camera, the class that takes care of the transformation
	 */
	private Camera cam;

	private MixState state = new MixState();

	/** The view can be "frozen" for debug purposes */
	private boolean frozen;
	boolean sureF = true;//避免重复点击
	/** how many times to re-attempt download */
	private int retry;

	private Location curFix;
	private DataHandler dataHandler = new DataHandler();
	private float radius = 1;//显示的距离,默认1KM

	/** timer to refresh the browser */
	private Timer refresh = null;
	private final long refreshDelay = 300 * 1000; //

	private boolean isLauncherStarted;

	private ArrayList<UIEvent> uiEvents = new ArrayList<UIEvent>();

	private RadarPoints radarPoints = new RadarPoints();
	private ScreenLine lrl = new ScreenLine();
	private ScreenLine rrl = new ScreenLine();
	//private float rx = 10, ry = 20;
	private float rx = 0, ry = 40;//绘制雷达的坐标,rx由宽度计算,这里设置没用
	private float addX = 0, addY = 0;
	
	private List<Marker> markers;
	private Executor executor = Executors.newSingleThreadExecutor();
	boolean tag =true;
	//保存要显示的泡泡的队列,避免同屏显示过多和重叠
	private Map<Long,ImageMarker> showMarkers = new HashMap<>();
	/**
	 * Constructor
	 */
	public DataView(MixContext ctx) {
		this.mixContext = ctx;
	}

	public MixContext getContext() {
		return mixContext;
	}
    
	public Location getCurFix(){
		return curFix;
	}
	 
	public boolean isLauncherStarted() {
		return isLauncherStarted;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public DataHandler getDataHandler() {
		return dataHandler;
	}

	public boolean isDetailsView() {
		return state.isDetailsView();
	}

	public void setDetailsView(boolean detailsView) {
		state.setDetailsView(detailsView);
	}

	public void doStart() {
		state.nextLStatus = MixState.NOT_STARTED;
		mixContext.getLocationFinder().setLocationAtLastDownload(curFix);
	}

	public boolean isInited() {
		return isInit;
	}
    
	public void init(int widthInit, int heightInit) {
		try {
			width = widthInit;
			height = heightInit;
            rx = width - 2*radarPoints.RADIUS;
			cam = new Camera(width, height, true);
			cam.setViewAngle(Camera.DEFAULT_VIEW_ANGLE);

			lrl.set(0, -RadarPoints.RADIUS);
			lrl.rotate(Camera.DEFAULT_VIEW_ANGLE / 2);
			lrl.add(rx + RadarPoints.RADIUS, ry + RadarPoints.RADIUS);
			rrl.set(0, -RadarPoints.RADIUS);
			rrl.rotate(-Camera.DEFAULT_VIEW_ANGLE / 2);
			rrl.add(rx + RadarPoints.RADIUS, ry + RadarPoints.RADIUS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		frozen = false;
		isInit = true;
	}

	


//	public void requestData(DataSource datasource, double lat, double lon, double alt, float radius, String locale) {
//		DownloadRequest request = new DownloadRequest();
//		request.params = datasource.createRequestParams(lat, lon, alt, radius, locale);
//		request.source = datasource;
//		
//		mixContext.getDownloadManager().submitJob(request);
//		state.nextLStatus = MixState.PROCESSING;
//	}
    
	public void draw(PaintScreen dw) {
		mixContext.getRM(cam.transform);
		curFix = mixContext.getLocationFinder().getCurrentLocation();
		//AppContext appContext = (AppContext) getApplicationContext();
		state.calcPitchBearing(cam.transform);

		// Load Layer
		if (state.nextLStatus == MixState.NOT_STARTED && !frozen) {
			//loadDrawLayer();
			markers = new ArrayList<Marker>();
		}
		else if (state.nextLStatus == MixState.PROCESSING) {//刷新泡泡
		if(markers.size() == 0 ||tag){//没有新泡泡,只重绘
			tag = false;
			DownloadMgrImpl dm = (DownloadMgrImpl) mixContext.getDownloadManager();
			 executor.execute(dm);
			while(dm.isGetPopResult()){//监测线程完成 这里可以用信号量或者其他东西改写,这样忙等没意义
			}
		
				retry = 0;
				state.nextLStatus = MixState.DONE;
				dataHandler = new DataHandler();
				markers.addAll(dm.getMarkers());
				dataHandler.addMarkers(markers);
				dataHandler.onLocationChanged(curFix);
				if (refresh == null) {
					refresh = new Timer(false);
					Date date = new Date(System.currentTimeMillis()
							+ refreshDelay);
					refresh.schedule(new TimerTask() {
						@Override
						public void run() {
							callRefreshToast();
							refresh();
						}
					}, date, refreshDelay);
				}
			}
		}

		// Update markers
		dataHandler.updateActivationStatus(mixContext);
		for (int i = dataHandler.getMarkerCount() - 1; i >= 0; i--) {//重绘制
			ImageMarker ma = (ImageMarker)dataHandler.getMarker(i);
			// if (ma.isActive() && (ma.getDistance() / 1000f < radius || ma
			// instanceof NavigationMarker || ma instanceof SocialMarker)) {
			if (ma.isActive() && (ma.getDistance() / 1000f < radius)) {

				// To increase performance don't recalculate position vector
				// for every marker on every draw call, instead do this only
				// after onLocationChanged and after downloading new marker
				// if (!frozen)
				// ma.update(curFix);

				if (!frozen) {
					ma.calcPaint(cam, addX, addY);
				}

				if(showMarkers.get(ma.getPopid()) == null) {
					tryAddShowPop(ma);
				}
				//ma.draw(dw);//此时已确定了点坐标和图片大小
			}else {
				showMarkers.remove(ma.getPopid());
			}
		}
		Iterator iter = showMarkers.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			ImageMarker pop = (ImageMarker) entry.getValue();
			pop.draw(dw);
		}
		// Draw Radar
		//因为平移后点坐标难以计算而且只显示1km的内容,暂时不进行地图绘制
		//drawRadar(dw);

		// Get next event
		UIEvent evt = null;
		synchronized (uiEvents) {
			if (uiEvents.size() > 0) {
				evt = uiEvents.get(0);
				uiEvents.remove(0);
			}
		}
		if (evt != null) {
				handleClickEvent((ClickEvent) evt);
		}
		state.nextLStatus = MixState.PROCESSING;
	}


	private void tryAddShowPop(ImageMarker ma){
		if(adjust(ma)){
			showMarkers.put(ma.getPopid(),ma);
		}
	}

	private boolean adjust(ImageMarker pop){
		MixVector popSignMarker = pop.getSignMarker();
		if(popSignMarker.getRealX() > width || popSignMarker.getRealY() > height || popSignMarker.getRealX() < 0 || popSignMarker.offsetY <0){
			return false;
		}
		Iterator iter = showMarkers.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			ImageMarker cptPop = (ImageMarker) entry.getValue();
			MixVector cptSignMarker = cptPop.getSignMarker();
			Bitmap popBitmap = pop.getBitmap();
			Bitmap cptPopBitmap = cptPop.getBitmap();
			if(isCrash(popSignMarker.getRealX(),popSignMarker.getRealY(),cptSignMarker.getRealX(),cptSignMarker.getRealY(),popBitmap.getWidth(),popBitmap.getHeight(),cptPopBitmap.getWidth(),cptPopBitmap.getHeight())){//会碰撞，需调整
				if((popBitmap.getWidth()+cptPopBitmap.getWidth())/2 - Math.abs(popSignMarker.getRealX() - cptSignMarker.getRealX()) > 1 ){//水平碰撞
					popSignMarker.offsetX = (popBitmap.getWidth()+cptPopBitmap.getWidth())/2 + cptSignMarker.getRealX() - popSignMarker.x;//右移
					if(popSignMarker.offsetX < 0){
						return false;
					}
				}
				if((popBitmap.getHeight()+cptPopBitmap.getHeight())/2 - Math.abs(popSignMarker.getRealY() - cptSignMarker.getRealY()) > 1){//竖直碰撞
					popSignMarker.offsetY = (popBitmap.getHeight()+cptPopBitmap.getHeight())/2 + cptSignMarker.getRealY() - popSignMarker.y;//下移
					if(popSignMarker.offsetY < 0){
						return false;
					}
				}
				//重新比较
				return adjust(pop);
			}
		}
		return true;
	}

	private boolean isCrash(float x1,float y1,float x2,float y2,double width1,double height1,double width2,double height2){
		return Math.sqrt(Math.pow((width1+width2)/2,2)+Math.pow((height1+height2)/2,2)) - Math.sqrt(Math.pow((x1 - x2),2) + Math.pow((y1 - y2),2)) > 1  ;
	}

//	private boolean adjustLocation(ImageMarker ma){//这种方式有bug,会导致已经存在的泡泡突然消失,移除的判断也有问题
//		MixVector signMarker = ma.getSignMarker();
//		if(signMarker.x+signMarker.offsetX > width || signMarker.y+signMarker.offsetY > height){//超出范围
//			return false;
//		}else {
//			Bitmap bitmap = ma.getBitmap();
//			for (ImageMarker imageMarker : showMarkers) {
//				boolean isAdjust = false;
//				//判断是否会碰撞
//				MixVector anotherMarker = imageMarker.getSignMarker();
//				Bitmap anotherBitmap = imageMarker.getBitmap();
//				//水平方向
//				float xDistance = (signMarker.x+signMarker.offsetX)-(anotherMarker.x+anotherMarker.offsetX);
//				float xBitmapDistance = bitmap.getWidth()/2 +anotherBitmap.getWidth()/2;
//				if(xBitmapDistance < Math.abs(xDistance)){//水平距离不够,右移.
//					if(xDistance < 0){//碰撞且在已有图形的左边,不加入到屏幕显示
//						return false;
//					}
//					float offsetX = xBitmapDistance +(anotherMarker.x+anotherMarker.offsetX) - signMarker.x;
//					signMarker.offsetX = offsetX;
//					isAdjust = true;
//				}
//				//垂直方向
//				float yDistance = (signMarker.y+signMarker.offsetY)-(anotherMarker.y+anotherMarker.offsetY);
//				float yBitmapDistance = bitmap.getHeight()/2 +anotherBitmap.getHeight()/2;
//				if(yBitmapDistance< Math.abs(yDistance)){//垂直距离不够,下移.
//					if(yDistance < 0){//碰撞且在已有图形的上边,不加入到屏幕显示
//						return false;
//					}
//
//					float offsetY = yBitmapDistance +(anotherMarker.y+anotherMarker.offsetY) - signMarker.y;
//					signMarker.offsetY = offsetY;
//					isAdjust = true;
//				}
//				if(isAdjust){
//					boolean adjustresult = adjustLocation(ma);//在次遍历
//					if(!adjustresult){
//						return false;
//					}
//				}
//			}
//
//			return true;
//		}
//	}


	/**
	 * Handles drawing radar and direction.
	 * @param dw screen that radar will be drawn to
	 *  暂时不进行地图的绘制了
	 */
	private void drawRadar(PaintScreen dw) {
		String dirTxt = "";
		int bearing = (int) state.getCurBearing();
		int range = (int) (state.getCurBearing() / (360f / 16f));
		// TODO: get strings from the values xml file
		if (range == 15 || range == 0)
			dirTxt = "N";
		else if (range == 1 || range == 2)
			dirTxt = "NE";
		else if (range == 3 || range == 4)
			dirTxt = "E";
		else if (range == 5 || range == 6)
			dirTxt = "SE";
		else if (range == 7 || range == 8)
			dirTxt = "S";
		else if (range == 9 || range == 10)
			dirTxt = "SW";
		else if (range == 11 || range == 12)
			dirTxt = "W";
		else if (range == 13 || range == 14)
			dirTxt = "NW";

		radarPoints.view = this;
		dw.paintObj(radarPoints, rx, ry, -state.getCurBearing(), 1);
		dw.setFill(false);
		dw.setColor(Color.argb(255, 166, 202, 240));
		
		dw.paintLine(lrl.x, lrl.y, rx + RadarPoints.RADIUS, ry
				+ RadarPoints.RADIUS);
		dw.paintLine(rrl.x, rrl.y, rx + RadarPoints.RADIUS, ry
				+ RadarPoints.RADIUS);
		dw.setColor(Color.rgb(255, 255, 255));
		dw.setFontSize(12);
		radarText(dw, MixUtils.formatDist(radius * 1000), rx
				+ RadarPoints.RADIUS, ry + RadarPoints.RADIUS * 2 - 10, false);
		
		radarText(dw, "" + bearing + ((char) 176) + " " + dirTxt, rx
				+ RadarPoints.RADIUS, ry - 5, true);
	}



	boolean handleClickEvent(ClickEvent evt) {
		boolean evtHandled = false;

		// Handle event
//		if (state.nextLStatus == MixState.DONE) {
			// the following will traverse the markers in ascending order (by
			// distance) the first marker that
			// matches triggers the event.
			//TODO handle collection of markers. (what if user wants the one at the back)
			for (int i = 0; i < dataHandler.getMarkerCount() && !evtHandled; i++) {//遍历查看被点击的泡泡
				ImageMarker pm = (ImageMarker) dataHandler.getMarker(i);
				evtHandled = pm.fClick(evt.x, evt.y, mixContext, state);
				if(evtHandled){
					if(pm.getType().equals(PopTypeEnum.WORDS)){
						if(sureF){
							sureF = false;
						 SharedPreferences mySharedPreferences = this.getContext().getSharedPreferences("pop", 
								  Activity.MODE_PRIVATE); 
						  SharedPreferences.Editor editor = mySharedPreferences.edit(); 
						  editor.putLong("id",pm.getPopid());
						  editor.commit(); 
//					 Intent intent = new Intent();
//	   			     intent.setClass(this.getContext(),getPopActivity.class);
	   			          Intent intent = new Intent(this.getContext(), PopInfoActivity.class);
	   			          this.getContext().startActivity(intent);  
	   			          this.getContext().startActivity(intent);
	   			          sureF = true;
						}
					}
					else{

//						 Intent intent = new Intent();
//		   			     intent.setClass(this.getContext(),BusnisePopActivity.class);
//		   			      this.getContext().startActivity(intent);
					}
				}
			}
//		}
		return evtHandled;
	}
    
	private void radarText(PaintScreen dw, String txt, float x, float y, boolean bg) {
		float padw = 4, padh = 2;
		float w = dw.getTextWidth(txt) + padw * 2;
		float h = dw.getTextAsc() + dw.getTextDesc() + padh * 2;
		if (bg) {
			dw.setColor(Color.rgb(0, 0, 0));
			dw.setFill(true);
			dw.paintRect(x - w / 2, y - h / 2, w, h);
			dw.setColor(Color.rgb(255, 255, 255));
			dw.setFill(false);
			dw.paintRect(x - w / 2, y - h / 2, w, h);
		}
		dw.paintText(padw + x - w / 2, padh + dw.getTextAsc() + y - h / 2, txt,
				false);
	}
    
	public void clickEvent(float x, float y) {
		synchronized (uiEvents) {
			uiEvents.add(new ClickEvent(x, y));
		}
	}

	public void keyEvent(int keyCode) {
		synchronized (uiEvents) {
			uiEvents.add(new KeyEvent(keyCode));
		}
	}

	public void clearEvents() {
		synchronized (uiEvents) {
			uiEvents.clear();
		}
	}

	public void cancelRefreshTimer() {
		if (refresh != null) {
			refresh.cancel();
		}
	}
	

	public void refresh(){
		state.nextLStatus = MixState.NOT_STARTED;
	}
	
	private void callRefreshToast(){
		mixContext.getActualMixView().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(
						mixContext,
						"dataview425行",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}

class UIEvent {
	public static final int CLICK = 0;
	public static final int KEY = 1;

	public int type;
}

class ClickEvent extends UIEvent {
	public float x, y;

	public ClickEvent(float x, float y) {
		this.type = CLICK;
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}

class KeyEvent extends UIEvent {
	public int keyCode;

	public KeyEvent(int keyCode) {
		this.type = KEY;
		this.keyCode = keyCode;
	}

	@Override
	public String toString() {
		return "(" + keyCode + ")";
	}
}
