package com.zzh.o2o.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zzh.o2o.BaseTest;
import com.zzh.o2o.entity.ShopCategory;

public class ShopCategoryServiceTest extends BaseTest {

	@Autowired
	ShopCategoryService shopCategoryService;

	@Test
	public void testQueryShopCategory() {
		ShopCategory shopCategory = new ShopCategory();
		List<ShopCategory> shopCategories = shopCategoryService.getShopCategoryList(shopCategory);
		Assert.assertEquals(2, shopCategories.size());
		for (ShopCategory shopCategory2 : shopCategories) {
			System.out.println(shopCategory2);
		}
	}
}
