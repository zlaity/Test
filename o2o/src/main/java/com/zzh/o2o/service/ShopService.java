package com.zzh.o2o.service;


import com.zzh.o2o.dto.ImageHolder;
import com.zzh.o2o.dto.ShopExecution;
import com.zzh.o2o.entity.Shop;
import com.zzh.o2o.exceptions.ShopOperationException;

public interface ShopService {
	/// 存储店铺图片 传入filename获取文件名字 inputstream没有办法获取名字
	// Shop shop, InputStream shopImgInputStream,String fileName
	ShopExecution addShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException;

	/*
	 * 通过店铺id获取店铺消息
	 */
	Shop getByShopId(long shopId);

	/*
	 * 更新店铺消息，包括对图片的处理
	 */
	ShopExecution modifyShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException;
	
	/**
	 * @Title: getShopList
	 * @Description: 获取商铺列表. 在这一个方法中同样的会调用查询总数的DAO层方法，封装到ShopExecution中
	 * @param shopCondition
	 * @param pageIndex 前端页面 只有第几页 第几页 定义为pageIndex
	 * @param pageSize  展示的行数
	 * @throws ShopOperationException
	 * @return: ShopExecution
	 */
	ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) throws ShopOperationException;;
}
