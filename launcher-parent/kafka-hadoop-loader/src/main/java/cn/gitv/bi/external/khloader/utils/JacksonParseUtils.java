package cn.gitv.bi.external.khloader.utils;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kang on 2016/12/23.
 */
public class JacksonParseUtils {
    private ObjectMapper jsonMapper = null;

    public JacksonParseUtils() {
        jsonMapper = new ObjectMapper(new JsonFactory());
    }

    public Map<String, Object> parseJsonAsMap(String data) {
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
        };
        try {
            return jsonMapper.readValue(data, typeRef);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }
}
