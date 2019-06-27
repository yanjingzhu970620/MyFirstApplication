package com.yjzfirst.util;

import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.w3c.dom.Element;

import java.lang.reflect.Field;

/**
 * @{# XmlParseUtility.java Create on 2015年4月12日 下午5:33:47
 * 
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 *
 */
public class beanParseUtility {

	public static <T> T parse(JSONObject element, Class<T> clazz) {
		try {
			T t = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				field.set(t, element.getString(field.getName()));
//				System.out.println(" return:::"+field.getName()+"  "+element.getString(field.getName()));
			}
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
