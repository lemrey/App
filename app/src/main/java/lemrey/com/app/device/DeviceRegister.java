package lemrey.com.app.device;

import java.util.ArrayList;
import java.util.List;

public class DeviceRegister {

	private final static List<Device> mDevices = new ArrayList<>();

	public static List<Device> devices() {
		return mDevices;
	}

	public static void addDevice(Device device) {
		mDevices.add(device);
	}

	public static boolean deviceExists(String devAddr) {
		boolean ret = false;
		for (Device dev : mDevices) {
			if (dev.mAddress.equals(devAddr)) {
				ret = true;
			}
		}
		return ret;
	}

	public static Device get(int i) {
		return mDevices.get(i);
	}

	public static void addDevice(String deviceName, String deviceAddress) {
		Device device = new Device(deviceName, deviceAddress);
		// Set doesn't allow duplicates
		mDevices.add(device);
	}

	public static Device device(String address) {
		for (Device d : mDevices) {
			// We are comparing the device address, since we override equals
			//noinspection EqualsBetweenInconvertibleTypes
			if (d.equals(address)) {
				return d;
			}
		}
		return null;
	}

	public static List<Feature> eventsForDeviceId(int i) {
		return mDevices.get(i).events();
	}

	public static List<Feature> cmdsForDeviceId(int i) {
		return mDevices.get(i).commands();
	}
}
