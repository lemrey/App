package lemrey.com.app.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lemrey.com.app.R;
import lemrey.com.app.device.Device;
import lemrey.com.app.device.Feature;
import lemrey.com.app.device.FeatureType;

/**
 * Created by lemrey on 3/7/2015.
 */


public class DeviceDetailsAdapter extends BaseExpandableListAdapter {

	private final Activity mActivity;
	private final List<Device> mDevices;
	private final LayoutInflater mLayoutInflater;

	public DeviceDetailsAdapter(Activity activity, Collection<Device> devices) {
		mActivity = activity;
		mDevices = new ArrayList<>(devices);
		mLayoutInflater = activity.getLayoutInflater();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		Device dev = mDevices.get(groupPosition);
		return dev.features().get(childPosition);
		//return groups.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
	                         boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.list_object_details, null);
		}
		final String name;
		//final Object child = getChild(groupPosition, childPosition);
		Feature child = mDevices.get(groupPosition).features().get(childPosition);
		final TextView label = (TextView) convertView.findViewById(R.id.textView1);
		final Drawable iconDirection;
		final Drawable iconData;

		name = child.name;
		Log.d("adapterzzz", name + " is a " + child.type);
		if (child.type.equals(FeatureType.EVENT)) {
			iconDirection = mActivity.getResources().getDrawable(R.drawable.ic_event);
		} else {
			iconDirection = mActivity.getResources().getDrawable(R.drawable.ic_cmd);
		}
		switch (child.paramType) {
			default:
				iconData = mActivity.getResources().getDrawable(R.drawable.ic_bubble);
				break;
			case NUMBER:
				iconData = mActivity.getResources().getDrawable(R.drawable.ic_dice);
				break;
			case TEXT:
				iconData = mActivity.getResources().getDrawable(R.drawable.ic_paperplane);
				break;
		}

		iconDirection.setBounds(0, 0, 64, 64);
		iconData.setBounds(0, 0, 64, 64);


		label.setText(name);

		/*convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mActivity, children,
						Toast.LENGTH_SHORT).show();
			}
		});*/


		label.setCompoundDrawablesRelative(iconDirection, null, iconData, null);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		//return groups.get(groupPosition).children.size();
		return mDevices.get(groupPosition).features().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mDevices.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mDevices.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
	                         View convertView, ViewGroup parent) {

		View rowView = mLayoutInflater.inflate(R.layout.list_objects_entry, parent, false);
		TextView labelName = (TextView) rowView.findViewById(R.id.labelName);

		// Set text and icon
		Device device = mDevices.get(groupPosition);
		labelName.setText(device.name());

		Drawable img = null;
		switch (device.status()) {
			case DISCONNECTED:
				img = mActivity.getResources().getDrawable(R.drawable.ic_disconnected);
				break;
			case CONNECTING:
				img = mActivity.getResources().getDrawable(R.drawable.ic_connecting);
				break;
			case CONNECTED:
				img = mActivity.getResources().getDrawable(R.drawable.ic_connected);
				break;
		}

		img.setBounds(0, 0, 64, 64);
		labelName.setCompoundDrawablesRelative(null, null, img, null);

		return rowView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}

