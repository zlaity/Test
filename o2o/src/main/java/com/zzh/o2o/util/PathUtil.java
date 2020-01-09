package com.zzh.o2o.util;
/*
 * 
 * 根据不同的操作系统,获取图片的存放路径。
 * 
 *	一般情况下都是将图片存放到专门的图片服务器或者主机上的其他目录。
 *	而存放的目录路径，都是以配置项的形式存放在数据库配置表中或者配置文件中。
 *	为了简单起见，直接将路径硬编码在代码中。
 *
 * 图片存储的根路径
 * 
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//提供两类的路径
//第一类是依据执行环境的不同 返回项目的根路径（图片存放的路径）
public class PathUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(PathUtil.class);

	// 路径分隔符. 在 类UNIX系统上为 '/',在 windows 系统上，它为 '\'
	private static String seperator = System.getProperty("file.seperator");

	// 调用静态方法的工具类
	public static String getImgBasePath() {
		// 获取操作系统的名称
		String os = System.getProperty("os.name");
		logger.debug("os.name: {}", os);
		String basePath = "";
		// 	
		if (os.toLowerCase().startsWith("win")) {
			basePath = "E:/o2o/photos";
		} else {
			// 别的系统保存在这个目录
			basePath = "/home/o2o/photos";
		}
		// 替换路径
		basePath = basePath.replace("/", seperator);
		logger.debug("basePath: {}", basePath);
		return basePath;
	}

	// 第二类方法：依据不同的业务 返回项目图片的子路径
	/*
	 * 获取特定商铺的图片的路径，根据shopId来区分，配置到数据库表或者配置文件中
	 * 约定每个店铺下图片分别存储在对应店铺id下
	 * 图片存储的相对路径 图片最终发存储的位置需要加上getShopImagPath方法返回的basepath
	 * 数据库中的tb_shop中的shop_img字段存储的是该相对路径 即该方法的返回值
	 */
	public static String getShopImagPath(long shopId) {
		String imagePath = "upload/item/shop/" + shopId + "/";
		logger.debug("shopImgPath: {}", imagePath);
		return imagePath.replace("/", seperator);

	}
	
	
}
