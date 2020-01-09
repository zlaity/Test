package com.zzh.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zzh.o2o.entity.ShopCategory;

public interface ShopCategoryDao {
	// 根据传入的参数 获取相关的店铺类别
	//List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);

	List<ShopCategory> queryShopCategoryList(@Param("shopCategoryCondition") ShopCategory shopCategory);
}
	