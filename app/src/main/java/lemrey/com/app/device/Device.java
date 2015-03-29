package lemrey.com.app.device;

import java.util.ArrayList;
import java.util.List;

public class Device {

	private static final String TAG = "Device";

	public static enum ConnectionStatus {
		DISCONNECTED,
		CONNECTING,
		CONNECTED
	}

	/**
	 * The MAC address of the remote device.
	 */
	public final String mAddress;
	private final String mBluetoothName;
	private String mSmartName = null;

	//private int mRSSI;

	/**
	 * The device connection status.
	 */
	private ConnectionStatus mStatus;
	private List<Event> mEvents;
	private List<Command> mCommands;


	public Device(String deviceName, String deviceAddress) {
		mBluetoothName = deviceName;
		mAddress = deviceAddress;
		mStatus = ConnectionStatus.DISCONNECTED;
		mEvents = new ArrayList<>();
		mCommands = new ArrayList<>();
	}

	public String name() {
		if (mSmartName != null)
			return mSmartName;
		else
			return mBluetoothName;
	}

	public void setSmartName(String name) {
		mSmartName = name;
	}

	public ConnectionStatus status() {
		return mStatus;
	}

	public boolean isConnected() {
		return mStatus.equals(ConnectionStatus.CONNECTED);
	}

	public void setStatus(ConnectionStatus status) {
		mStatus = status;
	}

	public void setConnected() {
		mStatus = ConnectionStatus.CONNECTED;
	}

	public void setConnecting() {
		mStatus = ConnectionStatus.CONNECTING;
	}

	public void setDisconnected() {
		mStatus = ConnectionStatus.DISCONNECTED;
	}

	public List<Event> events() {
		return mEvents;
	}

	public List<Command> commands() {
		return mCommands;
	}

	public void addEvents(List<Event> events) {
		mEvents.addAll(events);
	}

	public void addCommands(List<Command> cmds) {
		mCommands.addAll(cmds);
	}

	/**
	 * Returns true if the object is a {@link String} equal to this device address,
	 * or if the object is a {@link Device} with the same address as this one.
	 */
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		if (obj instanceof Device) {
			if (mAddress.equalsIgnoreCase(((Device) obj).mAddress)) {
				isEqual = true;
			}
		} else if (obj instanceof String) {
			if (mAddress.equalsIgnoreCase((String) obj)) {
				isEqual = true;
			}
		}
		return isEqual;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 89 * hash + (this.mAddress != null ? this.mAddress.hashCode() : 0);
		return hash;
	}
}
