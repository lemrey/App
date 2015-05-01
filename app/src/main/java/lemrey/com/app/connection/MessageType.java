package lemrey.com.app.connection;

/**
 * Created by lemrey on 3/7/2015.
 */
public enum MessageType {
	PONG("pong"),
	EVENT("event");

	private String text;
	private MessageType(String text) {
		this.text = text;
	}

	public static MessageType fromData(String data) {
		if (data != null) {
			for (MessageType b : MessageType.values()) {
				if (data.equalsIgnoreCase(b.text)) {
					return b;
				}
			}
		}
		return null;
	}
}

