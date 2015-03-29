package lemrey.com.app.device;

/**
 * Created by lemrey on 4/2/2015.
 */
public enum FeatureParam {
	NONE("none"),
	NUMBER("number"),
	TEXT("text");

	private final String text;
	private FeatureParam(String text) {
		this.text = text;
	}

	/*public static FeatureParam fromData(String data) {
		if (data != null) {
			for (FeatureParam b : FeatureParam.values()) {
				if (data.equalsIgnoreCase(b.text)) {
					return b;
				}
			}
		}
		return null;
	}*/
}
