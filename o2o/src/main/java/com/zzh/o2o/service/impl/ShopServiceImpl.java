package com.zzh.o2o.service.impl;

import java.io.InputStream;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zzh.o2o.dao.ShopDao;
import com.zzh.o2o.dto.ShopExecution;
import com.zzh.o2o.entity.Shop;
import com.zzh.o2o.enums.ShopStateEnum;
import com.zzh.o2o.exceptions.ShopOperationException;
import com.zzh.o2o.service.ShopService;
import com.zzh.o2o.util.ImageUtil;
import com.zzh.o2o.util.ImagesUtil;
import com.zzh.o2o.util.PathUtil;
import com.zzh.o2o.util.PathsUtil;

@Service
public class ShopServiceImpl implements ShopService {
	
	/*
	 * 注册商铺service层操作
	 */
	@Autowired
	private ShopDao shopDao;

	@Override
	@Transactional		//说明是事务操作，错误的话要回滚，只有抛出异常时才能回滚
	public ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName) throws ShopOperationException {
		// 检查传入的参数是否合法
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		} // 还可以判断其他是否为空 比如店铺的种类
		try {
			// 代表未上架 审核中  都不可改变
			shop.setEnableStatus(0);
			// 创建时间
			shop.setCreateTime(new Date());
			// 编辑后的时间
			shop.setLastEditTime(new Date());
			// 插入记录
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				throw new ShopOperationException("店铺创建失败");
			} else {
				if (shopImgInputStream != null) {
					// 存储图片
					try {
						addShopImg(shop, shopImgInputStream,fileName);
					} catch (Exception e) {
						//e.printStackTrace();
						throw new ShopOperationException("addShopImg error:" + e.getMessage());
					}
					// 更新店铺的图片地址
					effectedNum = shopDao.updateShop(shop);
					if (effectedNum <= 0) {
						throw new ShopOperationException("更新图片地址失败");
					}

				}
			}
		} catch (Exception e) {
			throw new ShopOperationException("addShop error:" + e.getMessage());
		}
		//审核中 提交成功 返回整个店铺的信息  包括新增店铺的信息 以及图片
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}

	private void addShopImg(Shop shop, InputStream shopImgInputStream,String fileName) {
		// 获取shop图片目录的相对值路径
		String dest = PathsUtil.getShopImgPath(shop.getShopId());
		// 存储图片 返回相应的值路径
		String shopImgAddr = ImagesUtil.generateThumbnail(shopImgInputStream,fileName,dest);
		// 返回更新
		shop.setShopImg(shopImgAddr);
	}

}
