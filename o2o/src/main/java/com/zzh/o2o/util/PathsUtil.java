package com.zzh.o2o.util;

public class PathsUtil {
	private static String seperator = System.getProperty("file.seperator");

	public static String getImgBasePath() {
		String os = System.getProperty("os.name");
		String basePath = "";
		if (os.toLowerCase().startsWith("win")) {
			basePath = "E:/o2o/photos/";
		} else {
			basePath = "/home/o2o/photos/";
		}
		basePath = basePath.replace("/", seperator);
		return basePath;
	}

	public static String getShopImgPath(long shopId) {
		String imagePath = "/upload/item/shop/" + shopId + "/";
		return imagePath.replace("/", seperator);
	}
}
