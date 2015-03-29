package lemrey.com.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lemrey.com.app.device.Device;
import lemrey.com.app.device.Feature;

public class DeviceRegister implements Iterable<Device> {

	private final static List<Device> mDevices = new ArrayList<>();

	public static List<Device> devices() {
		return mDevices;
	}

	public static void addDevice(Device device) {
		// Set doesn't allow duplicates
		mDevices.add(device);
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
			// We are comparing the device address
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

	@Override
	public Iterator<Device> iterator() {
		return mDevices.iterator();
	}
}
