package lemrey.com.app.rule;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import lemrey.com.app.device.Command;
import lemrey.com.app.device.Event;

/**
 * Created by lemrey on 3/8/2015.
 */
public class RuleBook implements Iterable<Rule> {

	private static final ArrayList<Rule> mRules = new ArrayList<>();

	public static void addRule(Rule rule) {
		if (!mRules.contains(rule)) {
			mRules.add(rule);
		}
	}

	@Override
	public Iterator<Rule> iterator() {
		return mRules.iterator();
	}

	public static Command match(String srcAddr, Event event) {
		for (Rule rule : mRules) {
			//Log.d("RuleBook", "trying to match with " + rule.event.mName + "add " + rule.srcAddr);
			if (rule.event.mName.equals(event.mName)
					&& rule.srcAddr.equals(srcAddr)) {
				return rule.cmd;
			}
		}
		return null;
	}
}
