package com.zzh.o2o.util;

/**
 * @ClassName: PageCalculator
 * @Description: 将前台使用的pageIndex 转换为 dao层使用的 rowIndex
 */
public class PageCalculator {

	public static int calculateRowIndex(int pageIndex, int pageSize) {
		return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
	}
}
