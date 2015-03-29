package lemrey.com.app.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lemrey.com.app.R;
import lemrey.com.app.device.Device;
import lemrey.com.app.device.Event;

/**
 * Created by lemrey on 3/7/2015.
 */


public class DeviceDetailsAdapter extends BaseExpandableListAdapter {

	//private final SparseArray<Group> groups;
	private final List<Device> mDevices;
	public Activity activity;
	private LayoutInflater inflater;

	public DeviceDetailsAdapter(Activity act, Collection<Device> devices) {
		activity = act;
		mDevices = new ArrayList<>(devices);
		//mDevices = devices;
		//mDevices = DeviceRegister.devices();
		inflater = act.getLayoutInflater();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		Device dev = mDevices.get(groupPosition);
		if (childPosition < dev.events().size())
			return dev.events().get(childPosition);
		else
			return dev.commands().get(childPosition-dev.events().size());
		//return groups.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
	                         boolean isLastChild, View convertView, ViewGroup parent) {
		final String children = ((Event)getChild(groupPosition, childPosition)).mName;
		TextView text = null;
		Drawable imgType = activity.getResources().getDrawable(R.drawable.gear);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_object_details, null);
		}
		text = (TextView) convertView.findViewById(R.id.textView1);
		text.setText(children);

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(activity, children,
						Toast.LENGTH_SHORT).show();
			}
		});

		imgType.setBounds(0, 0, 64, 64);
		text.setCompoundDrawablesRelative(imgType, null, null, null);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		//return groups.get(groupPosition).children.size();
		return mDevices.get(groupPosition).events().size();
				//+ mDevices.get(groupPosition).commands().size();
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
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
	                         View convertView, ViewGroup parent) {

		View rowView = inflater.inflate(R.layout.list_objects_entry, parent, false);
		TextView labelName = (TextView) rowView.findViewById(R.id.labelName);

		// Set text and icon
		Device device = mDevices.get(groupPosition);
		labelName.setText(device.name());

		Drawable img = null;
		switch (device.status()) {
			case DISCONNECTED:
				img = activity.getResources().getDrawable(R.drawable.ic_disconnected);
				break;
			case CONNECTING:
				img = activity.getResources().getDrawable(R.drawable.ic_connecting);
				break;
			case CONNECTED:
				img = activity.getResources().getDrawable(R.drawable.ic_connected);
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

