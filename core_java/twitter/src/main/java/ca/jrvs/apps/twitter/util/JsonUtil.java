package ca.jrvs.apps.twitter.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;

public class JsonUtil {

  /**
   * Convert java object to json string
   * @param object
   * @param prettyJson
   * @param includeNullValues
   * @return
   * @throws JsonProcessingException
   */
  public static String toJson(Object object, boolean prettyJson, boolean includeNullValues)
    throws JsonProcessingException {
    ObjectMapper m = new ObjectMapper();
    if(!includeNullValues) {
      m.setSerializationInclusion(Include.NON_NULL);
    }
    if(prettyJson) {
      m.enable(SerializationFeature.INDENT_OUTPUT);
    }
    //System.out.println("in tojson");
    //System.out.println(m.writeValueAsString(object));
    return m.writeValueAsString(object);
  }

  /**
   * Parse Json string to an object
   * @param json
   * @param <T>
   * @return
   * @throws IOException
   */
  public static <T> T toObjectFromJson(String json, Class clazz) throws IOException {
    ObjectMapper m = new ObjectMapper();
    m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    m.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);;

    return (T) m.readValue(json, clazz);
  }
}
