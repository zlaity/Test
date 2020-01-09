package com.zzh.o2o.util;

import javax.servlet.http.HttpServletRequest;

//负责处理http参数
public class HttpServletRequestUtil {
	// 根据key取出它的值
	public static int getInt(HttpServletRequest request, String key) {
		// 提取key 将它转换成整型
		try {
			return Integer.decode(request.getParameter(key));
		} catch (Exception e) {
			// TODO: handle exception
			return -1;
		}
	}

	public static long getLong(HttpServletRequest request, String key) {
		// 提取key 将它转换成long类型
		try {
			return Long.valueOf(request.getParameter(key));
		} catch (Exception e) {
			// TODO: handle exception
			return -1;
		}
	}

	public static Double getDouble(HttpServletRequest request, String name) {

		try {
			return Double.valueOf(request.getParameter(name));
		} catch (Exception e) {
			return -1d;
		}
	}

	public static Boolean getBoolean(HttpServletRequest request, String name) {

		try {
			return Boolean.valueOf(request.getParameter(name));
		} catch (Exception e) {
			return false;
		}
	}

	public static String getString(HttpServletRequest request, String name) {
		try {
			String result = request.getParameter(name);
			if (result != null) {
				result = result.trim();
			}
			if ("".equals(result))
				result = null;
			return result;
		} catch (Exception e) {
			return null;
		}

	}
}
