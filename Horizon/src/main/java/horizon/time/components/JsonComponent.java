package horizon.time.components;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Component
public class JsonComponent {
	private Gson gson;
	private JsonObject object;

	public JsonComponent() {
		this.gson = new GsonBuilder().setPrettyPrinting().create();
		object = new JsonObject();
	}

	public void add(String key, String value) {
		object.addProperty(key, value);
	}

	public void addMessageSuccess(String key, String value) {
		object.addProperty("status", "success");
		object.addProperty(key, value);
	}

	public void addMessageFailure(String key, String value) {
		object.addProperty("status", "failure");
		object.addProperty(key, value);
	}

	public void remove(String key) {
		object.remove(key);
	}

	public String getJson() {
		return gson.toJson(object);
	}
}
