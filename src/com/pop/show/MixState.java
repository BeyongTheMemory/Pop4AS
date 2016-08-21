
package com.pop.show;


import com.pop.lib.MixContextInterface;
import com.pop.lib.MixStateInterface;
import com.pop.lib.MixUtils;
import com.pop.lib.render.Matrix;
import com.pop.lib.render.MixVector;

/**
 * This class calculates the bearing and pitch out of the angles
 */
public class MixState implements MixStateInterface{

	public static int NOT_STARTED = 0; 
	public static int PROCESSING = 1; 
	public static int READY = 2; 
	public static int DONE = 3; 
	int nextLStatus = MixState.NOT_STARTED;
	String downloadId;

	private float curBearing;
	private float curPitch;

	private boolean detailsView;
    

	public float getCurBearing() {
		return curBearing;
	}

	public float getCurPitch() {
		return curPitch;
	}
	
	public boolean isDetailsView() {
		return detailsView;
	}
	
	public void setDetailsView(boolean detailsView) {
		this.detailsView = detailsView;
	}
    
	public void calcPitchBearing(Matrix rotationM) {
		MixVector looking = new MixVector();
		rotationM.transpose();
		looking.set(1, 0, 0);
		looking.prod(rotationM);
		this.curBearing = (int) (MixUtils.getAngle(0, 0, looking.x, looking.z)  + 360 ) % 360 ;//���㵱ǰ��ת��ֵ
		rotationM.transpose();
		looking.set(0, 1, 0);
		looking.prod(rotationM);
		this.curPitch = -MixUtils.getAngle(0, 0, looking.y, looking.z);
	}

	@Override
	public boolean handleEvent(MixContextInterface ctx, String onPress) {
		// TODO �Զ����ɵķ������
		return false;
	}
}
