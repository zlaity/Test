package com.zzh.o2o.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	private static String bathPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	// 设置时间的格式
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	// 随机数对象
	private static final Random r = new Random();

	// 处理缩略图 即门面照和商品的的小图 用来处理用户传递过来的文件流
	// CommonsMultipartFile为spring自带的文件处理对象 接收图片存储路径targetAddr
	public static String generateThumbnail(InputStream thumbnailInputStream, String fileName, String targetAddr) {
		// 系统随机生成不重名的文件
		String realFileName = getRandomFileName();
		// 文件的扩展名 jpg png 一起工程新文件的名字
		String extension = getFileExtension(fileName);
		// 创建新目录
		makeDirPath(targetAddr);
		// 获取相对路径
		String relativeAddr = targetAddr + realFileName + extension;
		// 文件组成：
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			// 创建缩略图 加水印
			Thumbnails.of(thumbnailInputStream).size(200, 200)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(bathPath + "/watermark.jpg")), 0.25f)
					.outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}

	/*
	 * 创建目标路径所涉及到的目录，即/home/work/zzh/xxx.jpg 那么home work zzh 这三个文件都要自动创建
	 * 
	 */
	private static void makeDirPath(String targetAddr) {
		String reFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(reFileParentPath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	/*
	 * 生成随机文件名 当前年月日小时分钟秒钟 + 五位随机数
	 * 
	 */
	public static String getRandomFileName() {
		// 获取随机的五位数
		int rannum = r.nextInt(89999) + 10000;
		String nowTimeStr = sDateFormat.format(new Date());
		return nowTimeStr + rannum;
	}

	/*
	 * 获取输入文件流的扩展名
	 * 
	 */
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	public static void main(String[] args) throws IOException {
		Thumbnails.of(new File("E:/o2o/photos/timg.jpg")).size(200, 200)
				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(bathPath + "/watermark.jpg")), 0.25f)
				.outputQuality(0.8f).toFile("E:/o2o/photos/timgnew.jpg");
	}

}
