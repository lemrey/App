package lemrey.com.app.rule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

	//TODO: currently one match per rule only!!
	public static Rule match(String srcAddr, String eventName) {
		for (Rule rule : mRules) {
			//Log.d("RuleBook", "trying to match with " + rule.event.name + "add " + rule.srcAddr);
			if (rule.event.name.equals(eventName)
					&& rule.srcAddr.equals(srcAddr)) {
				return rule;
			}
		}
		return null;
	}

	/*public static Rule match(String srcAddr, Event event) {
		for (Rule rule : mRules) {
			//Log.d("RuleBook", "trying to match with " + rule.event.name + "add " + rule.srcAddr);
			if (rule.event.name.equals(event.name)
					&& rule.srcAddr.equals(srcAddr)) {
				return rule;
			}
		}
		return null;
	}*/

	public static List<Rule> rules() {
		return mRules;
	}

	@Override
	public Iterator<Rule> iterator() {
		return mRules.iterator();
	}
}
