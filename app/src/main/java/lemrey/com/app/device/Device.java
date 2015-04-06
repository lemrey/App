package lemrey.com.app.device;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Device {

	private static final String TAG = "Device";
	/**
	 * The MAC address of the remote device.
	 */
	public final String mAddress;
	private final String mBluetoothName;
	private final Set<Feature> mFeatures;
	private String mSmartName = null;

	/**
	 * The device connection status.
	 */
	private ConnectionStatus mStatus;
	public Device(String deviceName, String deviceAddress) {
		mBluetoothName = deviceName;
		mAddress = deviceAddress;
		mStatus = ConnectionStatus.DISCONNECTED;
		mFeatures = new HashSet<>();
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

	public List<Feature> events() {
		final ArrayList<Feature> events = new ArrayList<>();
		for (Feature ft : mFeatures) {
			if (ft.type.equals(FeatureType.EVENT)) events.add(ft);
		}
		return events;
	}

	public List<Feature> commands() {
		final ArrayList<Feature> cmds = new ArrayList<>();
		for (Feature ft : mFeatures) {
			if (ft.type.equals(FeatureType.COMMAND)) cmds.add(ft);
		}
		return cmds;
	}

	public List<Feature> features() {
		return new ArrayList<>(mFeatures);
	}

	@SafeVarargs
	public final void addFeatures(List<Feature> ... features ) {
		for (List<Feature> list : features) {
			mFeatures.addAll(list);
		}
	}

	public void addEvents(List<Feature> events) {
		mFeatures.addAll(events);
	}

	public void addCommands(List<Feature> cmds) {
		mFeatures.addAll(cmds);
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
		return mAddress.hashCode();
	}

	public static enum ConnectionStatus {
		DISCONNECTED,
		CONNECTING,
		CONNECTED
	}
}
