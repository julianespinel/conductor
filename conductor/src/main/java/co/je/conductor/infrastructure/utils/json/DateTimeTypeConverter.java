package co.je.conductor.infrastructure.utils.json;

import java.lang.reflect.Type;
import java.util.Date;

import org.joda.time.DateTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateTimeTypeConverter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
	
	public JsonElement serialize(DateTime src, Type srcType, JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}

	public DateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		
		try {
			
			return new DateTime(json.getAsString());
			
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
			
			// May be it came in formatted as a java.util.Date, so try that
			Date date = context.deserialize(json, Date.class);
			return new DateTime(date);
		}
	}
}