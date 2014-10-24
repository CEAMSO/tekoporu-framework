package ${package}.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JSONParameterObject {
	private Map<String, Object> object;
	
	public JSONParameterObject() {
		super();
		this.object = new HashMap<String, Object>();
	}

	public static JSONParameterObject valueOf(String json) {
		JSONParameterObject root = new JSONParameterObject();
		ObjectMapper mapper = new ObjectMapper();


        try {
			root.setObject((Map<String, Object>) mapper.readValue(json, new TypeReference<Map<String,Object>>(){}));
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return root;
	}

	public Map<String, Object> getObject() {
		return object;
	}

	public void setObject(Map<String, Object> object) {
		this.object = object;
	}
		
}
