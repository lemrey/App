package lemrey.com.app.rule;

import lemrey.com.app.device.Feature;

/**
 * Created by lemrey on 3/8/2015.
 */
public class Rule {

	public final String srcAddr;
	public final String destAddr;
	public final Feature event;
	public final Feature cmd;

	private boolean mIsEnabled = true;

	public Rule(String srcAddr, Feature event, String destAddr, Feature cmd) {
		this.srcAddr = srcAddr;
		this.destAddr = destAddr;
		this.event = event;
		this.cmd = cmd;
	}

	public boolean isEnabled() {
		return mIsEnabled;
	}

	public void setEnabled(boolean enabled) {
		mIsEnabled = enabled;
	}
}
