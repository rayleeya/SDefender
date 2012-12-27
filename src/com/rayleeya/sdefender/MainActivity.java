package com.rayleeya.sdefender;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {

	private static final String TAG = "MainActivity";

	private boolean mShowDotFile = false;
	
	
	private Button mBtn;
	private TextView mTv;
	private ListView mList;
	private MyListAdapter mAdapter;
	
	private H mH;
	private SDCleaner mCleaner;
	
	private class H extends Handler {
		public void handleMessage(Message msg) {
			if (MainActivity.this.isFinishing()) {
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
	
	class MyListAdapter extends BaseAdapter {

		private int mItemRes;
		private ListView mList;
		private LayoutInflater mInflater;
		private List<File> mFiles = new ArrayList<File>();
		
		private DateFormat mFormat;
		
		MyListAdapter(ListView list) {
			mList = list;
			mInflater = getLayoutInflater();
			
			mFormat = DateFormat.getDateInstance();
		}
		
		void setItemRes(int res) {
			mItemRes = res;
		}
		
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
			View view = null;
			if (convertView != null) {
				view = convertView;
			} else {
				view = mInflater.inflate(mItemRes, null);
			}
			bindView(position, view);
			return view;
		}
		
		private void bindView(int position, View view) {
			File file = mFiles.get(position);
			boolean isFolder = file.isDirectory();
			String name = file.getName();
			long lastmodified = file.lastModified();
			
			ImageView i = (ImageView) view.findViewById(R.id.item_icon);
			TextView n = (TextView) view.findViewById(R.id.item_name);
			TextView l = (TextView) view.findViewById(R.id.item_lastmodified);
			
			if (isFolder) i.setImageResource(R.drawable.folder);
			n.setText(name);
			l.setText(mFormat.format(new Date(lastmodified)));
		}
		
		public void changeData(List<File> files) {
			if (files == null) {
				throw new NullPointerException("files cannot be null.");
			}
			mFiles = files;
			mList.invalidateViews();
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
		mAdapter = new MyListAdapter(mList);
		mAdapter.setItemRes(R.layout.list_item);
		mList.setAdapter(mAdapter);
		
		mH = new H();
		
		HandlerThread thread = new HandlerThread("SDCleaner");
		thread.start();
		mCleaner = new SDCleaner(thread.getLooper(), mH);
	}

	@Override
	protected void onStart() {
		super.onStart();
		listFiles();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
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
			
		}
	}

	private void listFiles() {
		SDCleaner.Request req = new SDCleaner.Request();
		req.mShowDotFile = mShowDotFile;
		
		Message msg = mCleaner.obtainMessage(SDCleaner.MSG_SCAN, req);
		msg.sendToTarget();
	}
	
}
