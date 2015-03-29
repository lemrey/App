package lemrey.com.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import lemrey.com.app.DeviceRegister;
import lemrey.com.app.R;
import lemrey.com.app.rule.Rule;

/**
 * Created by lemrey on 3/29/2015.
 */
public class RuleAdapter extends BaseExpandableListAdapter {

	private final List<Rule> mRules;
	private final LayoutInflater mLayoutInflater;

	public RuleAdapter(Context context, List<Rule> rules) {
		mRules = rules;
		mLayoutInflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getGroupCount() {
		return mRules.size();
	}

	@Override
	public int getChildrenCount(int i) {
		return 2;
	}

	@Override
	public Object getGroup(int i) {
		return mRules.get(i);
	}

	@Override
	public Object getChild(int i, int i2) {
		return i2 == 0 ? mRules.get(i).event : mRules.get(i).cmd;
	}

	@Override
	public long getGroupId(int i) {
		return 0;
	}

	@Override
	public long getChildId(int i, int i2) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
		if (view == null) {
			view = mLayoutInflater.inflate(R.layout.list_rule_entry, null);
		}
		final String ruleName = mRules.get(i).event.name;
		final TextView labelRuleName = (TextView) view.findViewById(R.id.labelName);
		labelRuleName.setText(ruleName);
		return view;
	}

	@Override
	public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
		if (view == null) {
			view = mLayoutInflater.inflate(R.layout.list_rule_details, null);
		}
		final String childName;
		final Rule rule = mRules.get(i);
		if (i2 == 0) {
			childName = "when " + DeviceRegister.device(rule.srcAddr).name() + "->" + rule.event.name;
		} else {
			childName = "then " + DeviceRegister.device(rule.destAddr).name() + "->" + rule.cmd.name;
		}
		final TextView labelRuleName = (TextView) view.findViewById(R.id.labelName);
		labelRuleName.setText(childName);
		return view;
	}

	@Override
	public boolean isChildSelectable(int i, int i2) {
		return false;
	}
}
