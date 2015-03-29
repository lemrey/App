package lemrey.com.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lemrey.com.app.device.Device;

	public class DeviceRegister implements Iterable<Device> {

	private final static List<Device> mDevices = new ArrayList<>();

	public static List<Device> devices() {
		return mDevices;
	}

	public static void addDevice(Device device) {
		// Set doesn't allow duplicates
		mDevices.add(device);
	}

	public static void addDevice(String deviceName, String deviceAddress) {
		Device device = new Device(deviceName, deviceAddress);
		// Set doesn't allow duplicates
		mDevices.add(device);
	}

	public static Device device(String address) {
		for (Device d : mDevices) {
			// We are comparing the device address
			if (d.equals(address)) {
				return d;
			}
		}
		return null;
	}

	@Override
	public Iterator<Device> iterator() {
		return mDevices.iterator();
	}
}
