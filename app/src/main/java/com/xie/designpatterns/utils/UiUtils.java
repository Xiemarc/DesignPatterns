package com.xie.designpatterns.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.xie.designpatterns.MAplication;

public class UiUtils {
	/**
	 * ��ȡ���ַ����� 
	 * @param tabNames  �ַ������id
	 */
	public static String[] getStringArray(int tabNames) {
		return getResource().getStringArray(tabNames);
	}

	public static Resources getResource() {
		return MAplication.getApplication().getResources();
	}
	public static Context getContext(){
		return MAplication.getApplication();
	}
	/** dipת��px */
	public static int dip2px(int dip) {
		final float scale = getResource().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/** pxת��dip */

	public static int px2dip(int px) {
		final float scale = getResource().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}
	/**
	 * ��Runnable �����ύ�����߳�����
	 * @param runnable
	 */
	public static void runOnUiThread(Runnable runnable) {
		// �����߳�����
		if(android.os.Process.myTid()==MAplication.getMainTid()){
			runnable.run();
		}else{
			//��ȡhandler  
			MAplication.getHandler().post(runnable);
		}
	}

	public static View inflate(int id) {
		return View.inflate(getContext(), id, null);
	}


	public static int getDimens(int homePictureHeight) {
		return (int) getResource().getDimension(homePictureHeight);
	}
	/**
	 * �ӳ�ִ�� ����
	 * @param run   ����
	 * @param time  �ӳٵ�ʱ��
	 */
	public static void postDelayed(Runnable run, int time) {
		MAplication.getHandler().postDelayed(run, time); // ����Runable�����run����
	}
	/**
	 * ȡ������
	 * @param auToRunTask
	 */
	public static void cancel(Runnable auToRunTask) {
		MAplication.getHandler().removeCallbacks(auToRunTask);
	}

}
