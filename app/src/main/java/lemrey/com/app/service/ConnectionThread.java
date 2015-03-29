package lemrey.com.app.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by lemrey on 3/9/2015.
 */
public final class ConnectionThread extends Thread {

	private final String TAG = "ConnectionThread";

	private final byte STX = (byte) 2;
	private final byte ETX = (byte) 3;
	private final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	public final String mAddress;
	private BluetoothSocket mBluetoothSocket;
	private InputStream mInputStream;
	private OutputStream mOutputStream;

	// The handler of the thread that started us
	private final Handler mParentHandler;


	public ConnectionThread(String address, Handler callback) {
		setName(address);
		mAddress = address;
		mParentHandler = callback;
	}

	private void connect(String address) throws IOException {
		Log.d(TAG, "Connecting to " + address);

		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			throw new IOException("Bluetooth is disabled");
		}

		mBluetoothAdapter.cancelDiscovery();
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		mBluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);

		try {
			mBluetoothSocket.connect();
			Log.d(TAG, "Connection established");
			// Invoke connection callback
			mParentHandler.obtainMessage(0, mAddress).sendToTarget();
		} catch (IOException ex) {
			String error = "Service discovery failed";
			if (ex.getMessage().equals(error)) {
				try {
					Log.d(TAG, "Caught a recoverable error: retrying");
					Thread.sleep(10000);
					Log.d(TAG, "Retrying now");
					mBluetoothSocket.connect();
				} catch (IOException ex1) {
					throw ex1;
				} catch (InterruptedException ex1) {
					throw new IOException();
				}
			}
		}
	}

	public void write(String msg) {
		byte[] data;
		try {
			data = msg.getBytes("UTF8");
		} catch (UnsupportedEncodingException ex) {
			Log.d(TAG, ex.getMessage());
			data = msg.getBytes();
		}
		try {
			mOutputStream.write(STX);
			mOutputStream.write(data);
			mOutputStream.write(ETX);
			Log.d(TAG, "Writing " + msg + " to " + mAddress);
			//ConsoleActivity.logSent(Event.prettyPrint(msg));
			//mOutputStream.flush();
		} catch (IOException ex) {
			Log.d(TAG, "Couldn't write bytes: " + ex.getMessage());
		}
	}

	@Override
	public void run() {
		Log.d(TAG, "Thread " + mAddress + " running");

		int readBufferPos = 0;
		boolean isReading = false;
		byte[] readBuffer = new byte[1024];

		int loopCount = 0;

		try {
			// TODO: we are calling the callback in another function
			connect(mAddress);
			mInputStream = mBluetoothSocket.getInputStream();
			mOutputStream = mBluetoothSocket.getOutputStream();
			sendPing();
		} catch (IOException ex) {
			Log.d(TAG, "Failed to connect: " + ex.getMessage());
			interrupt();
		}

		while (!isInterrupted()) {
			loopCount++;
			try {
				// read() is blocking!
				int character = mInputStream.read();
				if (character == STX) {
					isReading = true;
				}
				if (isReading && character != STX) {
					if (character != ETX && character != -1) {
						if (readBufferPos < 1023) {
							readBuffer[readBufferPos++] = (byte) character;
						} else {
							Log.d(TAG, "readBuffer overflow");
						}
					} else {
						String data = new String(readBuffer, 0, readBufferPos, "US-ASCII");
						//mDeviceCallback.onDataReceived(data);
						//Log.d(TAG, "Received " + data);
						//parseData(data);
						Message msg = mParentHandler.obtainMessage(2, mAddress);
						Bundle bundle = new Bundle();
						bundle.putString("data", data);
						msg.setData(bundle);
						msg.sendToTarget();

						// Reset buffer
						isReading = false;
						readBufferPos = 0;
						readBuffer = new byte[1024]; // Is this really okay?
					}
				}
			} catch (IOException ex) {
				Log.d(TAG, "Unable to read from socket: " + ex.getMessage());
				interrupt();
			}
		}

		interrupt();
		Log.d(TAG, "Thread stopped, exiting.");
		Log.d(TAG, "Executed " + loopCount + " times");

		// Cleanup
		if (mBluetoothSocket != null) {
			try {
				mBluetoothSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.d(TAG, "Failed to clean up socket.");
			}
		}

		// Invoke connection callback
		mParentHandler.obtainMessage(1, mAddress).sendToTarget();
	}

	private void sendPing() {
		write("{\"msg\":\"ping\"}");
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o instanceof ConnectionThread) {
			if (mAddress.equals((((ConnectionThread) o).mAddress))) {
				ret = true;
			}
		} else {
			/**
			 * This is especially handy since we often check for a connection (Thread) based
			 * on a device address (String)
			 */
			if (o instanceof String) {
				if (mAddress.equals(o)) {
					ret = true;
				}
			}
		}
		return ret;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + (this.mAddress != null ? this.mAddress.hashCode() : 0);
		return hash;
	}

}
