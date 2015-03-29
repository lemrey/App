package lemrey.com.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import lemrey.com.app.R;
import lemrey.com.app.device.Event;

/**
 * Created by lemrey on 3/29/2015.
 */
public class DeviceFeatureAdapter extends ArrayAdapter<Event> {

	private final List<Event> mFeatures;
	private final LayoutInflater mLayoutInflater;

	public DeviceFeatureAdapter(Context context, int resource, List<Event> features) {
		super(context, resource, features);
		mFeatures = features;
		mLayoutInflater = (LayoutInflater)context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = mLayoutInflater.inflate(R.layout.list_objects_entry2, parent, false);
		TextView label = (TextView) rowView.findViewById(R.id.labelName);
		//ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		label.setText(mFeatures.get(position).name());
		// change the icon for Windows and iPhone
		//String s = values[position];
		/*if (s.startsWith("iPhone")) {
			imageView.setImageResource(R.drawable.no);
		} else {
			imageView.setImageResource(R.drawable.ok);
		}*/

		return rowView;
	}
}
