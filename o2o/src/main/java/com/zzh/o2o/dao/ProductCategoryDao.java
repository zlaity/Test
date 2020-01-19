package com.zzh.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zzh.o2o.entity.ProductCategory;

public interface ProductCategoryDao {

	/**
	 * 通过shop id 查询店铺商品的类别
	 * 
	 * @param shopId
	 * @return List<ProductCategory>
	 */
	List<ProductCategory> queryProductCategoryList(long shopId);

	/**
	 * 批量新增商品类别
	 * 
	 * @param productCategoryList
	 * @return
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);

	/**
	 * @Title: deleteProductCategory
	 * @Description: 删除特定shop下的productCategory
	 * @param productCategoryId
	 * @param shopId
	 * @return: int
	 */
	int deleteProductCategory(@Param("productCategoryId") Long productCategoryId, @Param("shopId") Long shopId);

}
