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
		List<ShopCategory> categoryList = shopCategoryDao.queryShopCategoryList(sc);
		assertEquals(2, categoryList.size());

		// shopCategoryCondition.parent 不为null的情况

		// 查询parent=1的店铺目录
		ShopCategory child = new ShopCategory();
		ShopCategory parent = new ShopCategory();
		parent.setShopCategoryId(1L);
		child.setParent(parent);

		categoryList = shopCategoryDao.queryShopCategoryList(child);
		assertEquals(2, categoryList.size());
		for (ShopCategory shopCategory2 : categoryList) {
			System.out.println(shopCategory2);
		}

		// 查询 parent is null 的情况
		categoryList = shopCategoryDao.queryShopCategoryList(null);
		assertEquals(1, categoryList.size());
		System.out.println(categoryList.get(0));
	}

}
