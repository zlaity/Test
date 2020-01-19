package com.zzh.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzh.o2o.dao.ProductCategoryDao;
import com.zzh.o2o.dao.ProductDao;
import com.zzh.o2o.dto.ProductCategoryExecution;
import com.zzh.o2o.entity.ProductCategory;
import com.zzh.o2o.enums.ProductCategoryStateEnum;
import com.zzh.o2o.exceptions.ProductCategoryOperationException;
import com.zzh.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Autowired
	private ProductDao productDao;

	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	/**
	 * 使用@Transactional控制事务
	 */
	@Override
	@Transactional
	public ProductCategoryExecution addProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		// 非空判断
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				// 批量增加ProductCategory
				int effectNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
				if (effectNum > 0) {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS, productCategoryList,
							effectNum);
				} else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new ProductCategoryOperationException("batchAddProductCategory Error:" + e.getMessage());
			}
		} else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}

	/**
	 * 需要先将该商品目录下的商品的类别Id置为空，然后再删除该商品目录， 因此需要事务控制@Transactional
	 */
	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		// 解除tb_product里的商品与该productcategoryId的关联
		// 第一步 需要先将该商品目录下的商品的类别Id置为空
		try {
			int effectNum = productDao.updateProductCategory2Null(productCategoryId, shopId);
			if (effectNum < 0) {
				throw new ProductCategoryOperationException("商品类别更新失败");
			}
		} catch (Exception e) {
			throw new ProductCategoryOperationException(e.getMessage());
		}
		// 删除该productcategoryId
		// 第二步 删除该商品目录
		try {
			int effectNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if (effectNum > 0) {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			} else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
			}
		} catch (Exception e) {
			throw new ProductCategoryOperationException(e.getMessage());
		}
	}

}
