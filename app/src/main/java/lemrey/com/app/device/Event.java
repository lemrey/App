package lemrey.com.app.device;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Event {

	public final String mName;
	public Event(String name) {
		mName = name;
	}

	public String name() {
		return mName;
	}

	public static List<Event> fromJSONArray(JSONArray array) {
		ArrayList<Event> events = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			Event event = new Event(array.optJSONObject(i).optString("name"));
			events.add(event);
		}
		return events;
	}
}
