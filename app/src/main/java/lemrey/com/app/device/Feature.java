package lemrey.com.app.device;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lemrey on 4/3/2015.
 */
public class Feature {

	public final String name;
	public final FeatureType type;
	public final FeatureParam paramType;

	public Feature(String name, FeatureType type, FeatureParam param) {
		this.name = name;
		this.type = type;
		this.paramType = param;
	}

	/**
	 * Parses features as events from a JSONArray
	 * @param array a JSONArray of features
	 * @return a List of event features
	 */
	public static List<Feature> parseEvents(JSONArray array) {
		return parseArray(FeatureType.EVENT, array);
	}

	public static List<Feature> parseCommands(JSONArray array) {
		return parseArray(FeatureType.COMMAND, array);
	}

	private static List<Feature> parseArray(FeatureType type, JSONArray array) {
		final ArrayList<Feature> features = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			final JSONObject jsonObject = array.optJSONObject(i);
			final String name = jsonObject.optString("name");
			final FeatureParam param = FeatureParam.values()[jsonObject.optInt("type")];
			final Feature ft = new Feature(name, type, param);
			features.add(ft);
		}
		return features;
	}
}
