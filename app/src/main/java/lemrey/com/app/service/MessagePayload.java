package lemrey.com.app.service;

import java.util.List;

import lemrey.com.app.device.Command;
import lemrey.com.app.device.Event;

/**
 * Created by lemrey on 3/7/2015.
 */
public final class MessagePayload {

	private List<Event> mEvents;
	private List<Command> mCommands;

	public MessagePayload(List<Event> events, List<Command> commands) {
		this.mEvents = events;
		this.mCommands = commands;
	}

	public List<Event> sensors() {
		return mEvents;
	}

	public List<Command> actuators() {
		return mCommands;
	}
}
