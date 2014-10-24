package ${package}.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONParameterArray {
	private List<Map<String, Object>> array;
	
	public JSONParameterArray() {
		super();
		this.array = new ArrayList<Map<String, Object>>();
	}

	public static JSONParameterArray valueOf(String json) {
		JSONParameterArray root = new JSONParameterArray();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonFactory f = mapper.getFactory();
			//JsonNode actualObj = mapper.readTree(json);
			JsonParser jp = f.createParser(json);

			jp.nextToken(); // retorna START_ARRAY
			while (jp.nextToken() == JsonToken.START_OBJECT) {
				root.getArray().add(mapper.readValue(jp, Map.class));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return root;
	}

	public List<Map<String, Object>> getArray() {
		return array;
	}

	public void setArray(List<Map<String, Object>> array) {
		this.array = array;
	}

	
}
