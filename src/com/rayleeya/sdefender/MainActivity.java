package com.rayleeya.sdefender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import android.app.Activity;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {

	private static final String TAG = "MainActivity";
	
	private Button mBtn;
	private TextView mTv;
	private ListView mList;
	private MyListAdapter mAdapter;
	
	private H mH;
	private SDCleaner mCleaner;
	
	private class H extends Handler {
		public void handleMessage(Message msg) {
			if (MainActivity.this.isFinishing() || MainActivity.this.isDestroyed()) {
				return;
			}
			
			int what = msg.what;
			switch (what) {
				case SDCleaner.MSG_SCAN :
					List<File> files = (List<File>)msg.obj;
					mAdapter.changeData(files);
					break;
			}
		}
	};
	
	public class MyListAdapter extends BaseAdapter {

		private List<File> mFiles = new ArrayList<File>();
		
		@Override
		public int getCount() {
			return mFiles.size();
		}

		@Override
		public Object getItem(int position) {
			return mFiles.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			
			return null;
		}
		
		public void changeData(List<File> files) {
			if (files == null) {
				throw new NullPointerException("files cannot be null.");
			}
			mFiles = files;
			//TODO: Notify
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTv = (TextView) findViewById(R.id.tv);
		mBtn = (Button) findViewById(R.id.btn);
		mBtn.setOnClickListener(this);
		mList = (ListView) findViewById(R.id.list);
		mAdapter = new MyListAdapter();
		mList.setAdapter(mAdapter);
		
		mH = new H();
		
		HandlerThread thread = new HandlerThread("SDCleaner");
		thread.start();
		mCleaner = new SDCleaner(thread.getLooper(), mH);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == mBtn) {
//			mCleaner.sendEmptyMessage(SDCleaner.MSG_SCAN);
			try {
				android.os.Debug.dumpHprofData("sdcard/data/dump.hprof");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
