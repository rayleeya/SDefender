package com.rayleeya.sdefender;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class SDCleaner extends Handler {

	private static final String TAG = "SDCleaner";

	public static final int MSG_SCAN = 1;
	public static final int MSG_CLEAN = 2;
	public static final int MSG_ERR_NOTEXISTS = 3;
	public static final int MSG_ERR_CANTREAD = 4;
	public static final int MSG_ERR_UNKNOWN = 5;
	
	
	private Handler mTarget;
	
	public SDCleaner(Looper looper, Handler target) {
		super(looper);
		if (looper == null || target == null)
			new NullPointerException("Arguments must not be null : [" + looper + ", " + target + "]");
		mTarget = target;
	}
	
	@Override
	public void handleMessage(Message msg) {
		int what = msg.what;
		switch (what) {
			case MSG_SCAN :
				Request r = (Request)msg.obj;
				doScan(r);
				break;
			
			case MSG_CLEAN :
				doClean();
				break;
		}
	}
	
	public static class Request {
		public boolean mShowDotFile;
	}
	
	private boolean isSecurity(File src) {
		if (!src.exists()) {
			mTarget.sendEmptyMessage(MSG_ERR_NOTEXISTS);
			return false;
		}
		
		if (!src.canRead()) {
			mTarget.sendEmptyMessage(MSG_ERR_CANTREAD);
			return false;
		}
		
		return true;
	}
	
	private void doScan(Request r) {
		boolean showDotFile = r.mShowDotFile;
		
		File src = new File(SDConfig.PATH_SD);
		if (!isSecurity(src)) return;
		List<File> files = SDUtils.getSubFiles(src, SDUtils.COMPARATOR_ALPHABET, showDotFile);
		
		Message msg = mTarget.obtainMessage(MSG_SCAN, files);
		msg.sendToTarget();
	}
	
	private void doClean() {
		File src = new File(SDConfig.PATH_SD);
		if (!isSecurity(src)) return;		
	}

}
