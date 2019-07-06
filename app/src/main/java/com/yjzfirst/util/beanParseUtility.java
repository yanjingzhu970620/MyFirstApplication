package com.yjzfirst.util;

import com.google.gson.JsonObject;
import com.yjzfirst.bean.ReportProductBean;

import org.json.JSONObject;
import org.w3c.dom.Element;

import java.lang.reflect.Field;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @{# XmlParseUtility.java Create on 2015年4月12日 下午5:33:47
 * @description
 */
public class beanParseUtility {

	public static <T> T parse(JSONObject element, Class<T> clazz) {
		try {
			T t = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (element.has(field.getName()))
				field.set(t, element.getString(field.getName()));
//				System.out.println(" return:::"+field.getName()+"  "+element.getString(field.getName()));
			}
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T MergeBean(JSONObject elementmain, JSONObject elementtag, Class<T> clazz) {
		try {
			T t = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
					if (!elementtag.has(field.getName())||elementtag.getString(field.getName()).equals("null")
							|| elementtag.getString(field.getName()).equals("")) {
						if (elementmain.has(field.getName()))
							field.set(t, elementmain.getString(field.getName()));
//						System.out.println(" MergeBean:::"+field.getName()+"  "+elementmain.getString(field.getName()));
					} else {
						if (elementtag.has(field.getName()))
							field.set(t, elementtag.getString(field.getName()));
//						System.out.println(" MergeBean:::"+field.getName()+"  "+elementtag.getString(field.getName()));
					}

//				System.out.println(" return:::"+field.getName()+"  "+element.getString(field.getName()));
			}
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
