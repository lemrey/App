package lemrey.com.app.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import lemrey.com.app.DeviceRegister;
import lemrey.com.app.R;
import lemrey.com.app.adapter.DeviceDetailsAdapter;
import lemrey.com.app.service.ConnectionService;


public class ObjectManagerActivity extends ActionBarActivity {

	private static final String TAG = "ObjectManagerActivity";
	private final DeviceStatus mDeviceCallbacks = new DeviceStatus();
	private LocalBroadcastManager mBroadcastManager;
	private DeviceDetailsAdapter mListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_object_manager);
		// The broadcast manager which will receive connection callbacks from the ConnectionService
		mBroadcastManager = LocalBroadcastManager.getInstance(this);
		mListAdapter = new DeviceDetailsAdapter(this);
		ExpandableListView mListView = (ExpandableListView) findViewById(R.id.listObjects);
		mListView.setAdapter(mListAdapter);
		mListView.setEmptyView(findViewById(R.id.empty));
		// Start the connection service
		if (!ConnectionService.isRunning()) {
			startService(new Intent(this, ConnectionService.class));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_objects, menu);
		if (DeviceRegister.devices().size() > 0)
			menu.findItem(R.id.menu_add_rule).setVisible(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {
			case R.id.menu_find: {
				startActivity(new Intent(this, ScannerActivity.class));
				return true;
			}
			case R.id.menu_add_rule: {
				startActivity(new Intent(this, RuleManagerActivity.class));
				return true;
			}
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mBroadcastManager.unregisterReceiver(mDeviceCallbacks);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mListAdapter.notifyDataSetChanged();
		invalidateOptionsMenu();
		mBroadcastManager.registerReceiver(mDeviceCallbacks, new IntentFilter("bing"));
	}

	private class DeviceStatus extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("bing")) {
				Log.d(TAG, intent.getStringExtra("device") + " connected");
				mListAdapter.notifyDataSetChanged();
				// Do stuff - maybe update my view based on the changed DB contents
			}
		}
	}
}
