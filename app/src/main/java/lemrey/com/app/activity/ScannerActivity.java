package lemrey.com.app.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.Set;

import lemrey.com.app.DeviceRegister;
import lemrey.com.app.R;


public class ScannerActivity extends ActionBarActivity {

	private final String TAG = "ScannerActivity";

	private BluetoothAdapter mBluetoothAdapter;
	private ArrayAdapter<String> mListAdapter;
	private SwipeRefreshLayout mSwipeRefreshLayout;

	/**
	 * The BroadcastReceiver that listens for discovered devices and changes the title
	 * when discovery is finished
	 */
	private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// If it's already paired or listed, skip it
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
					String item = device.getName() + "\n" + device.getAddress();
					// Avoid duplicates
					if (mListAdapter.getPosition(item) == -1) {
						mListAdapter.add(item);
					}
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				setTitle(R.string.select_device);
				mSwipeRefreshLayout.setRefreshing(false);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup the window
		setContentView(R.layout.activity_scanner);
		setTitle("Swipe down to scan");
		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(broadcastReceiver, filter);
		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(broadcastReceiver, filter);

		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				Log.v(TAG, "onRefresh called from SwipeRefreshLayout");
				doDiscovery();
			}
		});

		mListAdapter = new ArrayAdapter<>(this,
				android.R.layout.simple_list_item_multiple_choice);

		ListView listView = (ListView) findViewById(R.id.listDevices);
		listView.setOnItemClickListener(onItemClickListener);
		listView.setAdapter(mListAdapter);

		// Get the local Bluetooth adapter and paired devices
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices, add each one to the list
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				mListAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_scanner, menu);
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
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	// The on-click listener for all devices in the list
	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			CheckedTextView item = (CheckedTextView) v;
			item.setChecked(item.isChecked());
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
		// Make sure we're not doing discovery anymore
		if (mBluetoothAdapter != null) {
			mBluetoothAdapter.cancelDiscovery();
		}
		unregisterReceiver(broadcastReceiver);
	}

	/**
	 * Start device discover with the BluetoothAdapter
	 */
	private void doDiscovery() {
		setTitle(R.string.scanning);
		// If we're already discovering, stop it
		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
		}
		mBluetoothAdapter.startDiscovery();
	}

	public void onButtonAddClicked(View v) {
		//DeviceRegister deviceRegister = new DeviceRegister();
		ListView list = (ListView) findViewById(R.id.listDevices);
		SparseBooleanArray checkedItemPositions = list.getCheckedItemPositions();

		for (int i = 0; i < checkedItemPositions.size(); i++) {
			if (checkedItemPositions.valueAt(i)) {
				// Get the row entry text
				String text = (String) list.getItemAtPosition(checkedItemPositions.keyAt(i));
				// Get the device name
				String deviceName = text.substring(0, text.length() - 18);
				// Get the MAC address (last 17 chars)
				String deviceAddr = text.substring(text.length() - 17);
				// Register the device
				DeviceRegister.addDevice(deviceName, deviceAddr);
			}
		}

		Intent intent;
		intent = new Intent(getApplicationContext(), ObjectManagerActivity.class);
		startActivity(intent);
	}

}
