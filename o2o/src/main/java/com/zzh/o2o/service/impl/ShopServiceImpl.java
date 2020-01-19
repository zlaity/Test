package com.zzh.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zzh.o2o.dao.ShopDao;
import com.zzh.o2o.dto.ImageHolder;
import com.zzh.o2o.dto.ShopExecution;
import com.zzh.o2o.entity.Shop;
import com.zzh.o2o.enums.ShopStateEnum;
import com.zzh.o2o.exceptions.ShopOperationException;
import com.zzh.o2o.service.ShopService;
import com.zzh.o2o.util.ImageUtils;
import com.zzh.o2o.util.PageCalculator;
import com.zzh.o2o.util.PathUtils;

@Service
public class ShopServiceImpl implements ShopService {

	/*
	 * 注册商铺service层操作
	 */
	@Autowired
	private ShopDao shopDao;

	@Override
	@Transactional // 说明是事务操作，错误的话要回滚，只有抛出异常时才能回滚
	// Shop shop, InputStream shopImgInputStream,String fileName
	public ShopExecution addShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException {
		// 检查传入的参数是否合法
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		} // 还可以判断其他是否为空 比如店铺的种类
		try {
			// 代表未上架 审核中 都不可改变
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
				// shopImgInputStream
				if (imageHolder.getIns() != null) {
					// 存储图片
					try {
						// shopImgInputStream,fileName
						addShopImg(shop, imageHolder);
					} catch (Exception e) {
						// e.printStackTrace();
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
		// 审核中 提交成功 返回整个店铺的信息 包括新增店铺的信息 以及图片
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}

	// InputStream shopImgInputStream, String fileName
	private void addShopImg(Shop shop, ImageHolder imageHolder) {
		// 获取shop图片目录的相对值路径 TUDO
		// String dest = PathUtils.getShopImagePath(shop.getShopId());
		String imgPath = PathUtils.getShopImagePath(shop.getShopId());
		// 存储图片 返回相应的值路径
		// shopImgInputStream, fileName, dest
		String shopImgAddr = ImageUtils.generateThumbnails(imageHolder, imgPath);
		// 返回更新
		shop.setShopImg(shopImgAddr);
	}

	/*
	 * 根据店铺id获取店铺信息
	 */
	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		} else {
			try {
				// 1. 判断是否需要处理图片
				if (imageHolder.getIns() != null && imageHolder.getFileName() != null
						&& !"".equals(imageHolder.getFileName())) {
					// 1.1 删除掉旧的图片
					// 查询入参shop对应数据库表中的shopImg路径
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if (tempShop != null) {
						// 删除就的缩略图
						ImageUtils.deleteStorePath(tempShop.getShopImg());
					}
					// 1.2 用新的图片生成缩略图
					addShopImg(shop, imageHolder);
				}

				// 2. 更新店铺信息
				// 2.1 更新一些必要属性
				shop.setLastEditTime(new Date());
				// 2.2 更新店铺
				int effectedNum = shopDao.updateShop(shop);
				if (effectedNum <= 0) {
					throw new ShopOperationException(ShopStateEnum.INNER_ERROR.getStateInfo());
				}
				return new ShopExecution(ShopStateEnum.SUCCESS, shop);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ShopOperationException("modify shop error:" + e.getMessage());
			}
		}
	}

	/**
	 * 商铺列表
	 *
	 */
	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) throws ShopOperationException {
		// 前台页面插入的pageIndex（第几页）， 而dao层是使用 rowIndex （第几行） ，所以需要转换一下
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = new ArrayList<Shop>();
		ShopExecution se = new ShopExecution();
		// 查询带有分页的shopList
		shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		// 查询符合条件的shop总数
		int count = shopDao.queryShopCount(shopCondition);
		// 将shopList和 count设置到se中，返回给控制层
		if (shopList != null) {
			se.setShopList(shopList);
			se.setCount(count);
		} else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

}
