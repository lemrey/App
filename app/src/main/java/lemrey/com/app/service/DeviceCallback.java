package lemrey.com.app.service;

public interface DeviceCallback {
	void onDeviceConnected(String deviceAddress);
	void onDeviceDisconnected(String deviceAddress);
	void onPayloadReceived(String deviceAddress);
}
