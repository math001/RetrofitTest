package json;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;

/**
 * json 工具类
 * 
 * @author xiangming
 * 
 */
public class Jackson {

	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		// 空string转成空对象
		mapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.setSerializationInclusion(Inclusion.NON_NULL);
	}

	/**
	 * 对象转json字符串
	 * 
	 * @param object
	 *            对象
	 * @return
	 */
	public static String toJson(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * json字符串转JSONNode
	 * 
	 * @param json
	 * @return
	 */
	public static JsonNode toJsonNode(String json) {
		if (json == null) {
			return null;
		}
		try {
			return mapper.readTree(json);
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * json字符串转对象
	 * 
	 * @param json
	 *            字符串
	 * @param type
	 *            对象类型
	 * @return
	 */
	public static <T> T toObject(String json, Class<T> type) {
		if (json == null) {
			return null;
		}
		try {
			return mapper.readValue(json, type);
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * JsonNode转对象
	 * 
	 * @param node
	 *            json节点
	 * @param type
	 *            对象类型
	 * @return
	 */
	public static <T> T toObject(JsonNode node, Class<T> type) {
		if (node == null) {
			return null;
		}
		try {
			return mapper.readValue(node, type);
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * json字符串转对象列表
	 * 
	 * @param json
	 *            字符串
	 * @param type
	 *            List<Bean>中Bean的类型
	 * @return
	 */
	public static <T> T toList(String json, Class<T> type) {
		if (json == null) {
			return null;
		}
		try {
			return mapper.readValue(json, getCollectionType(ArrayList.class, type));
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * JsonNode转对象列表
	 * 
	 * @param node
	 *            json节点
	 * @param type
	 *            List<Bean>中Bean的类型
	 * @return
	 */
	public static <T> T toList(JsonNode node, Class<T> type) {
		if (node == null) {
			return null;
		}
		try {
			return mapper.readValue(node, getCollectionType(ArrayList.class, type));
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 获取泛型的Collection Type
	 * 
	 * @param collectionClass
	 *            泛型的Collection
	 * @param elementClasses
	 *            元素类
	 * @return java 类型
	 */
	private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}
}
