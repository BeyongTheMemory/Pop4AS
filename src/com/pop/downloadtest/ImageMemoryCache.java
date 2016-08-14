package com.pop.downloadtest;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.app.ActivityManager;

public class ImageMemoryCache {


	private static final int SOFT_CACHE_SIZE = 15;
	private static LruCache<String, Bitmap> mLruCache;
	private static LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache;

	public ImageMemoryCache(Context context) {
        int memClass = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();  
		int cacheSize = 1024 * 1024 * memClass / 4;
		mLruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				if (value != null)
					return value.getRowBytes() * value.getHeight();
				else
					return 0;
			}

			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				if (oldValue != null)
					// Ӳ���û�����������ʱ�򣬻����LRU�㷨�����û�б�ʹ�õ�ͼƬת��������û���
					mSoftCache.put(key, new SoftReference<Bitmap>(oldValue));
			}
		};
		mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(
				SOFT_CACHE_SIZE, 0.75f, true) {
			private static final long serialVersionUID = 6040103833179403725L;

			@Override
			protected boolean removeEldestEntry(
					Entry<String, SoftReference<Bitmap>> eldest) {
				if (size() > SOFT_CACHE_SIZE) {
					return true;
				}
				return false;
			}
		};
	}

	/**
	 * �ӻ����л�ȡͼƬ
	 */
	public Bitmap getBitmapFromCache(String imageName) {
		Bitmap bitmap;
		// �ȴ�Ӳ���û����л�ȡ
		synchronized (mLruCache) {
			bitmap = mLruCache.get(imageName);
			if (bitmap != null) {
				// ����ҵ��Ļ�����Ԫ���Ƶ�LinkedHashMap����ǰ�棬�Ӷ���֤��LRU�㷨�������ɾ��
				mLruCache.remove(imageName);
				mLruCache.put(imageName, bitmap);
				return bitmap;
			}
		}
		
		// ���Ӳ���û������Ҳ������������û�������
		synchronized (mSoftCache) {
			SoftReference<Bitmap> bitmapReference = mSoftCache.get(imageName);
			if (bitmapReference != null) {
				bitmap = bitmapReference.get();
				if (bitmap != null) {
					// ��ͼƬ�ƻ�Ӳ����
					mLruCache.put(imageName, bitmap);
					mSoftCache.remove(imageName);
					return bitmap;
				} else {
					mSoftCache.remove(imageName);
				}
			}
		}
		return null;
	}

	/**
	 * ���ͼƬ������
	 */
	public void addBitmapToCache(String imageName, Bitmap bitmap) {
		if (bitmap != null) {
			synchronized (mLruCache) {
				mLruCache.put(imageName, bitmap);
			}
		}
		System.out.println(mLruCache.size());
	}

	public void clearCache() {
		mSoftCache.clear();
	}
}
