package lemrey.com.app.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import lemrey.com.app.R;
import lemrey.com.app.rule.Rule;

/**
 * Created by lemrey on 5/1/2015.
 */
public class RuleAdapter extends ArrayAdapter<Rule> {

	private final List<Rule> mRules;
	private final LayoutInflater mLayoutInflater;

	public RuleAdapter(Context context, int resource, List<Rule> objects) {
		super(context, resource, objects);
		mRules = objects;
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = mLayoutInflater.inflate(R.layout.list_rule_entry, parent, false);
		}
		final TextView label = (TextView) view.findViewById(R.id.labelName);
		final Switch switchEnabled = (Switch) view.findViewById(R.id.switchEnabled);
		final Rule rule = mRules.get(position);

		label.setText("if \t\t\t" + rule.event.name + "\nthen\t" + rule.cmd.name);
		label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		switchEnabled.setChecked(rule.isEnabled());

		return view;
	}
}

