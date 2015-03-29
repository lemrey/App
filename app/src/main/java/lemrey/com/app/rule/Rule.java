package lemrey.com.app.rule;

import lemrey.com.app.device.Command;
import lemrey.com.app.device.Event;

/**
 * Created by lemrey on 3/8/2015.
 */
public class Rule {

	public final String srcAddr;
	public final String destAddr;
	public final Event event;
	public final Command cmd;

	public Rule(String srcAddr, Event event, String destAddr, Command cmd) {
		this.srcAddr = srcAddr;
		this.event = event;
		this.destAddr = destAddr;
		this.cmd = cmd;
	}
}
