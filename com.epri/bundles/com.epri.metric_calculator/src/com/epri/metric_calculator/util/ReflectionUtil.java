package com.epri.metric_calculator.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Reflection util
 * 
 * Reflection is a program technique that analyzes information of a class
 * through an object.
 * 
 * @author JoWookJae
 *
 */
public class ReflectionUtil {

	/**
	 * Invoke getter
	 * 
	 * @param instance
	 *            Target instance
	 * @param fieldType
	 *            Field type
	 * @param fieldName
	 *            Field name
	 * @return
	 */
	public static Object invokeGetter(Object instance, Class<?> fieldType, String fieldName) {
		Method getter = getter(instance.getClass(), fieldType, fieldName);

		try {
			return getter.invoke(instance);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get getther method
	 * 
	 * @param clazz
	 *            Target class
	 * @param fieldType
	 *            Field type
	 * @param fieldName
	 *            Field name
	 * @return
	 */
	public static Method getter(Class<?> clazz, Class<?> fieldType, String fieldName) {
		String fstChar = fieldName.substring(0, 1);
		String rest = fieldName.substring(1, fieldName.length());
		String getterName = null;
		if (fieldType == boolean.class) {
			getterName = "is" + fstChar.toUpperCase() + rest;
		} else {
			getterName = "get" + fstChar.toUpperCase() + rest;
		}

		try {
			return clazz.getMethod(getterName);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get setter method
	 * 
	 * @param clazz
	 *            Target class
	 * @param fieldName
	 *            Field name
	 * @param fieldType
	 *            Field types
	 * @return
	 */
	public static Method setter(Class<?> clazz, String fieldName, Class<?> fieldType) {
		String fstChar = fieldName.substring(0, 1);
		String rest = fieldName.substring(1, fieldName.length());
		String setterName = "set" + fstChar.toUpperCase() + rest;

		try {
			return clazz.getMethod(setterName, fieldType);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		return null;
	}
}
