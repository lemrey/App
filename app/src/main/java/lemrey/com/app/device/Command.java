package lemrey.com.app.device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lemrey on 3/7/2015.
 */
public class Command {
	public final String mName;
	public Command(String name) {
		mName = name;
	}

	public String name() {
		return mName;
	}

	public static List<Command> fromJSONArray(JSONArray array) {
		ArrayList<Command> commands = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			Command command = new Command(array.optJSONObject(i).optString("name"));
			commands.add(command);
		}
		return commands;
	}

	@Override
	public String toString() {
		JSONObject jsonHeader = new JSONObject();
		JSONObject jsonBody = new JSONObject();
		try {
			jsonBody.putOpt("name", mName);
			jsonHeader.put("msg","cmd");
			jsonHeader.put("command", jsonBody);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonHeader.toString();
	}
}
