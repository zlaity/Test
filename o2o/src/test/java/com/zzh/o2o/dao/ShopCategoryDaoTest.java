package com.zzh.o2o.dao;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.zzh.o2o.BaseTest;
import com.zzh.o2o.entity.ShopCategory;

public class ShopCategoryDaoTest extends BaseTest {
	@Autowired
	private ShopCategoryDao shopCategoryDao;

	@Test
	public void testQueryShopCategory() throws Exception {
		// shopCategoryCondition 不为null的情况，查询parent_id is not null 的数据
		ShopCategory sc = new ShopCategory();
		List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategoryList(sc);
		assertEquals(2, shopCategoryList.size());
		
		ShopCategory testCategory = new ShopCategory();
		ShopCategory parentCategory = new ShopCategory();
		parentCategory.setShopCategoryId(1L);
		testCategory.setParent(parentCategory);
		shopCategoryList = shopCategoryDao.queryShopCategoryList(sc);
		assertEquals(2, shopCategoryList.size());
		System.out.println(shopCategoryList.get(0).getShopCategoryName());
	}

}
