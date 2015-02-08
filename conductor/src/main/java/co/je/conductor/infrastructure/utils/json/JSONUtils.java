package co.je.conductor.infrastructure.utils.json;

import java.util.HashMap;
import java.util.Map;

public class JSONUtils {

    public static Map<String, Object> createKeyValueStringJson(String key, Object value) {
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        
        return map;
    }
}