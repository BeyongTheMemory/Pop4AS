
package com.pop.mgr.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pop.enume.ClientCode;
import com.pop.enume.PopModelEnume;
import com.pop.enume.PopTypeEnum;
import com.pop.model.PopDto;
import com.pop.request.GetPopRequest;
import com.pop.response.pop.PopResponse;
import com.pop.show.MixContext;
import com.pop.lib.marker.Marker;
import com.pop.lib.reality.PhysicalPlace;
import com.pop.lib.marker.ImageMarker;
import com.pop.util.CollectionUtil;
import com.pop.util.UrlUtil;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 */
public class DownloadMgrImpl implements Runnable, DownloadManager {


    private MixContext ctx;
    private DownloadManagerState state = DownloadManagerState.Confused;
    //private LinkedBlockingQueue<ManagedDownloadRequest> todoList = new LinkedBlockingQueue<ManagedDownloadRequest>();//ʹ���̰߳�ȫ����������
    private ConcurrentHashMap<String, DownloadResult> doneList = new ConcurrentHashMap<String, DownloadResult>();//�̰߳�ȫ��hashMap
    private List<Marker> markers;
    private boolean tag = true;
    private boolean getPopResult = false;
    double olatitude;
    double olongitude;
    double altitude;

    public DownloadMgrImpl(MixContext ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("Mix Context IS NULL");
        }
        this.ctx = ctx;
        state = DownloadManagerState.OffLine;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mixare.mgr.downloader.DownloadManager#run()
     */
    public void run() {
        if (tag) {
            state = DownloadManagerState.OnLine;
            olatitude = ctx.getActualMixView().getDataView().getCurFix().getLatitude();
            olongitude = ctx.getActualMixView().getDataView().getCurFix().getLongitude();
            getPopResult = false;
            altitude = ctx.getActualMixView().getDataView().getCurFix().getAltitude();
            SharedPreferences mySharedPreferences = ctx.getActualMixView().getSharedPreferences("loacation",
                    Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("latitude", olatitude + "");
            editor.putString("longitude", olongitude + "");
            editor.putString("altitude", altitude + "");
            editor.commit();
            //发起请求获取pop数据
            RequestParams params = new RequestParams(UrlUtil.getPop());
            params.setAsJsonContent(true);
            GetPopRequest getPopRequest = new GetPopRequest(olatitude, olongitude);
            params.setBodyContent(JSON.toJSONString(getPopRequest));
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    PopResponse popResponse = JSONObject.parseObject(result, PopResponse.class);
                    if (popResponse.getResult() == ClientCode.SUCCESS) {
                        //获取pop信息
                        List<PopDto> popDtos = popResponse.getPopDtoList();
                        if (!CollectionUtil.isEmpty(popDtos)) {
                            markers = new ArrayList<>(popDtos.size());
                            for (PopDto popDto : popDtos) {
                                ImageMarker imge = new ImageMarker(popDto.getId(), null, popDto.getLatitude(), popDto.getLongitude(), popDto.getAltitude(), null, 1, -1);
                                imge.setDistance(PhysicalPlace.distanceBetween(olatitude, olongitude, imge.getLatitude(), imge.getLongitude()));
                                imge.setBitmap(BitmapFactory.decodeResource(ctx.getActualMixView().getResources(), PopModelEnume.getImg(popDto.getModel())));
                                imge.setType(PopTypeEnum.WORDS + "");
                                markers.add(imge);
                            }
                        }
                        getPopResult = true;

                    } else {
                        Toast.makeText(x.app(), popResponse.getErrorMsg(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinished() {
                }
            });


            tag = false;
        }
    }

	
	/*
     * (non-Javadoc)
	 * ����������������
	 * @see org.mixare.mgr.downloader.DownloadManager#purgeLists()
	 */
//	public synchronized void resetActivity() {
//		todoList.clear();
//		doneList.clear();
//	}

	/*
     * (non-Javadoc)
	 *��URL���䵽������Եõ���Ӧ��Ϣ��URL��ID
	 * @see
	 * org.mixare.mgr.downloader.DownloadManager#submitJob(org.mixare.mgr.downloader
	 * .DownloadRequest)
	 */


    /*
     * (non-Javadoc)
     * �õ����һ�����ؽ��
     * @see
     * org.mixare.mgr.downloader.DownloadManager#getReqResult(java.lang.String)
     */
    public DownloadResult getReqResult(String jobId) {
        DownloadResult result = doneList.get(jobId);
        doneList.remove(jobId);
        return result;
    }

    /*
     * (non-Javadoc)
     * @see org.mixare.mgr.downloader.DownloadManager#getNextResult()
     */
    public synchronized DownloadResult getNextResult() {
        DownloadResult result = null;
        if (!doneList.isEmpty()) {
            String nextId = doneList.keySet().iterator().next();
            result = doneList.get(nextId);
            doneList.remove(nextId);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mixare.mgr.downloader.DownloadManager#getResultSize()
     */
    public int getResultSize() {
        return doneList.size();
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mixare.mgr.downloader.DownloadManager#isDone()
	 */
//	public Boolean isDone() {
//		return todoList.isEmpty();
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mixare.mgr.downloader.DownloadManager#goOnline()
	 */


    public void switchOff() {
        tag = false;
    }

    @Override
    public DownloadManagerState getState() {
        return state;
    }

    public List<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(List<Marker> markers) {
        this.markers = markers;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }


    public boolean isGetPopResult() {
        return getPopResult;
    }
}
