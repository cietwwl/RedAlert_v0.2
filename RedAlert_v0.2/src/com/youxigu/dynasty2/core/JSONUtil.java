package com.youxigu.dynasty2.core;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.ObjectMapper;

import com.youxigu.dynasty2.util.BaseException;

public class JSONUtil {

	private static ObjectMapper mapper = null;
	static {
		mapper = new ObjectMapper();
		mapper.setVisibilityChecker(mapper.getSerializationConfig()
				.getDefaultVisibilityChecker().withFieldVisibility(
						JsonAutoDetect.Visibility.ANY).withGetterVisibility(
						JsonAutoDetect.Visibility.NONE).withSetterVisibility(
						JsonAutoDetect.Visibility.NONE).withIsGetterVisibility(
						JsonAutoDetect.Visibility.NONE).withCreatorVisibility(
						JsonAutoDetect.Visibility.NONE));
		mapper.getSerializationConfig().setDateFormat(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	public static String toJson(Object value) {
		try {

			return mapper.writeValueAsString(value);
		} catch (Exception e) {
			throw new BaseException(e);
		}
	}

	public static <T> T toJavaBean(String json, Class<T> targetCls) {
		try {

			return mapper.readValue(json, targetCls);
		} catch (Exception e) {
			throw new BaseException(e);
		}
	}

	public static JsonNode getJsonNode(String json) {
		try {

			return mapper.readTree(json);
		} catch (Exception e) {
			throw new BaseException(e);
		}

	}

	public static void main(String[] args) {

		String[] aaa = new String[] { "aaa", "bbb", "ccc" };
		String json = (JSONUtil.toJson(aaa));

		JsonNode jsonObj = JSONUtil.getJsonNode(json);

		List<String> datas = new ArrayList<String>();
		Iterator<JsonNode> lit1 = jsonObj.getElements();
		while (lit1.hasNext()) {
			JsonNode node = lit1.next();
			datas.add(node.getTextValue());
		}


		Map<String, Object> map = new HashMap<String, Object>(5);
		map.put("stringField", "123213");
		map.put("intField", 1);
		map.put("timestamp", new Timestamp(System.currentTimeMillis()));
		map.put("dateField", new Date());

		String aa = "{\"ret\":0,\"msg\":\"\",\"list\":[{\"name\":\"wangweihua\",\"ip\":\"192.168.0.46\",\"port\":8739,\"status\":2,\"flag\":1,\"full\":1}]}";
		JsonNode node = JSONUtil.getJsonNode(aa);
		Iterator<JsonNode> lit = node.getElements();
		while (lit.hasNext()) {
			JsonNode node1 = lit.next();

			System.out.println(node1);

			Iterator<JsonNode> lit2 = node1.getElements();
			while (lit2.hasNext()) {
				JsonNode node2 = lit2.next();
				System.out.println(node2);

				Iterator<JsonNode> lit3 = node2.getElements();
				while (lit3.hasNext()) {
					JsonNode node3 = lit3.next();
					System.out.println(node3);
				}

			}
		}

		// System.out.println(node.getPath("ret"));
		// System.out.println(node.getPath("list"));
		// System.out.println(node.getPath("list").getElements());
		System.out.println(JSONUtil.toJson(map));

	}
	// mapper.writeValueAsString(value)
}
