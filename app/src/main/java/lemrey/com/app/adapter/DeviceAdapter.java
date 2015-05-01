package lemrey.com.app.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import lemrey.com.app.R;
import lemrey.com.app.device.Device;
import lemrey.com.app.device.DeviceRegister;
import lemrey.com.app.device.Feature;
import lemrey.com.app.device.FeatureType;

/**
 * Created by lemrey on 3/7/2015.
 */


public class DeviceAdapter extends BaseExpandableListAdapter {

	private final Activity mActivity;
	private final List<Device> mDevices;
	private final LayoutInflater mLayoutInflater;

	public DeviceAdapter(Activity activity) {
		mActivity = activity;
		mDevices = DeviceRegister.devices();
		mLayoutInflater = activity.getLayoutInflater();
	}

	@Override
	public int getGroupCount() {
		return mDevices.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mDevices.get(groupPosition).features().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mDevices.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		final Device dev = mDevices.get(groupPosition);
		return dev.features().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
	                         View view, ViewGroup parent) {
		//if (view == null) {
		//view = mLayoutInflater.inflate(R.layout.list_objects_entry, parent, false);
		// Won't update if we check for null??
		view = mLayoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		//}
		//final TextView labelName = (TextView) view.findViewById(R.id.labelName);
		final TextView labelName = (TextView) view.findViewById(android.R.id.text1);
		final Device device = mDevices.get(groupPosition);
		labelName.setText(device.name());

		final Drawable img;
		switch (device.status()) {
			case CONNECTED:
				img = mActivity.getResources().getDrawable(R.drawable.ic_connected);
				break;
			case CONNECTING:
				img = mActivity.getResources().getDrawable(R.drawable.ic_connecting);
				break;
			default:
				img = mActivity.getResources().getDrawable(R.drawable.ic_disconnected);
				break;
		}

		img.setBounds(0, 0, 64, 64);
		labelName.setTextSize(22);
		labelName.setPadding(88, 0, 0, 0);
		labelName.setCompoundDrawablesRelative(null, null, img, null);
		return view;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
	                         boolean isLastChild, View view, ViewGroup parent) {
		if (view == null) {
			//view = mLayoutInflater.inflate(R.layout.list_object_details, parent, false);
			view = mLayoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		}

		final Feature child = mDevices.get(groupPosition).features().get(childPosition);
		//final TextView label = (TextView) view.findViewById(R.id.textView1);
		final TextView label = (TextView) view.findViewById(android.R.id.text1);

		final String name = child.name;
		final Drawable iconDirection;
		final Drawable iconData;

		if (child.type.equals(FeatureType.EVENT)) {
			iconDirection = mActivity.getResources().getDrawable(R.drawable.ic_event);
		} else {
			iconDirection = mActivity.getResources().getDrawable(R.drawable.ic_cmd);
		}

		switch (child.paramType) {
			default:
				iconData = mActivity.getResources().getDrawable(R.drawable.ic_exclamation);
				break;
			case NUMBER:
				iconData = mActivity.getResources().getDrawable(R.drawable.ic_pi);
				break;
			case TEXT:
				iconData = mActivity.getResources().getDrawable(R.drawable.ic_message);
				break;
		}
		iconDirection.setBounds(0, 0, 48, 48);
		iconData.setBounds(0, 0, 48, 48);

		label.setText(name);
		label.setCompoundDrawablePadding(16);
		label.setCompoundDrawablesRelative(iconDirection, null, iconData, null);
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}

