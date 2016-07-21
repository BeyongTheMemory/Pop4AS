
package com.pop.show;


import com.pop.activity.MainActivity;
import com.pop.lib.MixContextInterface;
import com.pop.lib.render.Matrix;
import com.pop.mgr.downloader.DownloadManager;
import com.pop.mgr.downloader.DownloadManagerFactory;
import com.pop.mgr.location.LocationFinder;
import com.pop.mgr.location.LocationFinderFactory;
//import org.mixare.mgr.webcontent.WebContentManager;
//import org.mixare.mgr.webcontent.WebContentManagerFactory;




import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class MixContext extends ContextWrapper implements MixContextInterface{

	// TAG for logging
	public static final String TAG = "Mixare";

	private MainActivity mixView;

	private Matrix rotationM = new Matrix();//��ת����

	/** Responsible for all download */
	private DownloadManager downloadManager;

	/** Responsible for all location tasks */
	private LocationFinder locationFinder;

	/** Responsible for data Source Management */
	//private DataSourceManager dataSourceManager;

	/** Responsible for Web Content */
	//private WebContentManager webContentManager;
  
	
	public MixContext(MainActivity appCtx) {
		super(appCtx);
		mixView = appCtx;

		// TODO: RE-ORDER THIS SEQUENCE... IS NECESSARY?

		getLocationFinder().switchOn();//�򿪶�λ
		getLocationFinder().findLocation();//��ʼ��λ,�����õ����һ�ζ�λʱ��λ����Ϣ
	}

	public String getStartUrl() {
		Intent intent = ((Activity) getActualMixView()).getIntent();
		if (intent.getAction() != null
				&& intent.getAction().equals(Intent.ACTION_VIEW)) {
			return intent.getData().toString();
		} else {
			return "";
		}
	}
    
	public void getRM(Matrix dest) {
		synchronized (rotationM) {
			dest.set(rotationM);
		}
	}




	public void doResume(MainActivity mixView) {
		setActualMixView(mixView);
	}

	public void updateSmoothRotation(Matrix smoothR) {
		synchronized (rotationM) {
			rotationM.set(smoothR);
		}
	}

//	public DataSourceManager getDataSourceManager() {
//		if (this.dataSourceManager == null) {
//			dataSourceManager = DataSourceManagerFactory
//					.makeDataSourceManager(this);
//		}
//		return dataSourceManager;
//	}

	public LocationFinder getLocationFinder() {
		if (this.locationFinder == null) {
			locationFinder = LocationFinderFactory.makeLocationFinder(this);
		}
		return locationFinder;
	}

	public DownloadManager getDownloadManager() {
		if (this.downloadManager == null) {
			downloadManager = DownloadManagerFactory.makeDownloadManager(this);
			getLocationFinder().setDownloadManager(downloadManager);
		}
		return downloadManager;
	}

	
	public MainActivity getActualMixView() {
		synchronized (mixView) {
			return this.mixView;
		}
	}

	private void setActualMixView(MainActivity mv) {
		synchronized (mixView) {
			this.mixView = mv;
		}
	}
	public ContentResolver getContentResolver() {
		ContentResolver out = super.getContentResolver();
		if (super.getContentResolver() == null) {
			out = getActualMixView().getContentResolver();
		}
		return out;
	}
	
	/**
	 * Toast POPUP notification
	 * @param string message
	 */
	public void doPopUp(final String string){
       Toast.makeText(this,string,Toast.LENGTH_LONG).show();
	}

	/**
	 * Toast POPUP notification
	 * 
	 * @param connectionGpsDialogText
	 */
	public void doPopUp(int RidOfString) {
        doPopUp(this.getString(RidOfString));
	}

	@Override
	public void loadMixViewWebPage(String url) throws Exception {

	}
}
