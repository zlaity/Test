package com.zzh.o2o.service;

import java.util.List;

import com.zzh.o2o.dto.ProductCategoryExecution;
import com.zzh.o2o.entity.ProductCategory;
import com.zzh.o2o.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {

	/**
	 * 查询指定店铺下所有商品类别的信息
	 * 
	 * @param shopId
	 * @return List<ProductCategory>
	 */
	List<ProductCategory> getProductCategoryList(long shopId);

	/**
	 * @Title: addProductCategory
	 * @Description: 批量插入ProductCategory
	 * @param productCategoryList
	 * @throws ProductCategoryOperationException
	 * @return: ProductCategoryExecution
	 */
	ProductCategoryExecution addProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException;

	/**
	 * @Title: deleteProductCategory
	 * @Description: TODO 需要先将该商品目录下的商品的类别Id置为空，然后再删除该商品目录， 因此需要事务控制
	 * @param productCategoryId
	 * @param shopId
	 * @throws ProductCategoryOperationException
	 * @return: ProductCategoryExecution
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException;

}
