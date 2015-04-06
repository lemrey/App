package lemrey.com.app.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.ExpandableListView;
import android.widget.ListView;

import lemrey.com.app.DeviceRegister;
import lemrey.com.app.R;
import lemrey.com.app.adapter.DeviceDetailsAdapter;
import lemrey.com.app.service.ConnectionService;


public class ObjectManagerActivity extends ActionBarActivity /*implements DeviceCallback*/ {

	private static final String TAG = "ObjectManagerActivity";
	private final DeviceStatus mDeviceCallbacks = new DeviceStatus();
	private LocalBroadcastManager mBroadcastManager;
	private ExpandableListView mListView;
	private DeviceDetailsAdapter mListAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_object_manager);
		setTitle("Objects");

		mBroadcastManager = LocalBroadcastManager.getInstance(this);
		mListView = (ExpandableListView) findViewById(R.id.listObjects);
		mListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
		mListAdapter = new DeviceDetailsAdapter(this, DeviceRegister.devices());
		mListView.setAdapter(mListAdapter);
		// Start the connection service
		Intent intent = new Intent(this, ConnectionService.class);
		startService(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mBroadcastManager.registerReceiver(mDeviceCallbacks, new IntentFilter("bing"));
	}

	@Override
	protected void onPause() {
		super.onPause();
		mBroadcastManager.unregisterReceiver(mDeviceCallbacks);
	}

	private class DeviceStatus extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("bing")) {
				//Log.d(TAG, "connected");
				mListAdapter.notifyDataSetChanged();
				// Do stuff - maybe update my view based on the changed DB contents
			}
		}
	}
}
