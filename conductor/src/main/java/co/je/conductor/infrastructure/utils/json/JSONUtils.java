package co.je.conductor.infrastructure.utils.json;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtils {
	
	public static Gson getGsonAbleToParseDateTime() {

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter());

		return gsonBuilder.create();
	}
}