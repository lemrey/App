package lemrey.com.app.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;

import lemrey.com.app.DeviceRegister;
import lemrey.com.app.R;
import lemrey.com.app.adapter.DeviceDetailsAdapter;
import lemrey.com.app.device.Device;
import lemrey.com.app.service.ConnectionService;
import lemrey.com.app.service.DeviceCallback;


public class ObjectManagerActivity extends ActionBarActivity implements DeviceCallback {

	private static final String TAG = "ObjectManagerActivity";

	private ConnectionService mConnectionService;
	private boolean mIsBoundToService = false;

	//private DeviceRegister mDeviceRegister;
	private DeviceDetailsAdapter mListAdapter;

	private ExpandableListView mListView;

	private ServiceConnection mLocalServiceConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "Attached to service");
			mIsBoundToService = true;
			mConnectionService = ((ConnectionService.EventServiceBinder) service).getService();
			//initiateConnections();

			ArrayList<Device> m = new ArrayList<>();
			m.addAll(DeviceRegister.devices());
			mListAdapter = new DeviceDetailsAdapter(ObjectManagerActivity.this,	m);
			mListView.setAdapter(mListAdapter);

			mConnectionService.startConnections(ObjectManagerActivity.this);
		}

		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "Detached from service");
		}

	};

	private void initiateConnections() {
		Log.d(TAG, "Initiating connections");

		/*for (Device device : mDeviceRegister) {
			if (device.status() != Device.ConnectionStatus.CONNECTED) {
				mConnectionService.startConnection(device.mAddress, this);
				device.setConnecting();
				mListAdapter.notifyDataSetChanged();
			}
		}*/
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_object_manager);

		setTitle("Objects");

		//mDeviceRegister = new DeviceRegister();
		//mListAdapter = new DeviceAdapter(this);
		//mListAdapter = new DeviceDetailsAdapter(this, mConnectionService.devices());

		//mListAdapter.setNotifyOnChange(true);

		mListView = (ExpandableListView) findViewById(R.id.listObjects);
		mListView.setChoiceMode(ListView.CHOICE_MODE_NONE);

		/*RuleBook.addRule(new Rule("00:06:66:61:DB:DD", new Event("button on"),
				"00:06:66:61:DB:DD", new Command("led toggle")));*/

}

	@Override
	protected void onResume() {
		super.onResume();
		//Log.d(TAG, "On Resume");
		if (!mIsBoundToService) {
			bindLocalService();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//Log.d(TAG, "On Destroy");
		if (mIsBoundToService) {
			mIsBoundToService = false;
			unbindService(mLocalServiceConnection);
		}
	}

	private void bindLocalService() {
		if (!ConnectionService.isRunning()) {
			startService(new Intent(this, ConnectionService.class));
		} else {
			Log.d(TAG, "Service running, just bind");
		}
		bindService(new Intent(this, ConnectionService.class),
				mLocalServiceConnection, BIND_AUTO_CREATE);
	}

	public void onDeviceConnected(String deviceAddress) {
		Log.d(TAG, "OnDeviceConnected");
		mListAdapter.notifyDataSetChanged();

	}

	public void onDeviceDisconnected(String deviceAddress) {
		Log.d(TAG, "OnDeviceDisconnected");
		mListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPayloadReceived(String deviceAddress) {
		Log.d(TAG, "OnPayloadReceived");
		mListAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_object_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			mConnectionService.writeSomething("{\"msg\":\"cmd\", \"command\": {\"name\":\"led toggle\"}}");
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
