package lemrey.com.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import lemrey.com.app.R;
import lemrey.com.app.device.Feature;

/**
 * Created by lemrey on 4/3/2015.
 */
public class DeviceFeatureAdapter extends ArrayAdapter<Feature> {

	private final Context mContext;
	private final List<Feature> mFeatures;
	private final LayoutInflater mLayoutInflater;

	public DeviceFeatureAdapter(Context context, int resource, List<Feature> features) {
		super(context, resource, features);
		mContext = context;
		mFeatures = features;
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			//view = mLayoutInflater.inflate(R.layout.list_objects_entry2, parent, false);
			view = mLayoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		}
		//final TextView label = (TextView) view.findViewById(R.id.labelName);
		final TextView label = (TextView) view.findViewById(android.R.id.text1);
		final Feature ft = mFeatures.get(position);
		final Drawable icon;
		switch (ft.paramType) {
			case NUMBER:
				icon = mContext.getResources().getDrawable(R.drawable.ic_pi);
				break;
			case TEXT:
				icon = mContext.getResources().getDrawable(R.drawable.ic_paperplane);
				break;
			default:
				icon = mContext.getResources().getDrawable(R.drawable.ic_signal);
				break;
		}
		mContext.getResources().getDrawable(R.drawable.ic_action_new);
		icon.setBounds(0, 0, 64, 64);

		label.setText(ft.name);
		//label.setTextSize(16);
		label.setCompoundDrawablesRelative(null, null, icon, null);

		return view;
	}
}