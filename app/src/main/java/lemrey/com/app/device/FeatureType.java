package lemrey.com.app.device;

/**
 * Created by lemrey on 4/3/2015.
 */
public enum FeatureType {
	EVENT("event"),
	COMMAND("command");

	private final String text;
	private FeatureType(String text) {
		this.text = text;
	}
}
