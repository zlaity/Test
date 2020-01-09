package com.zzh.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zzh.o2o.BaseTest;
import com.zzh.o2o.entity.Area;
import com.zzh.o2o.entity.PersonInfo;
import com.zzh.o2o.entity.Shop;
import com.zzh.o2o.entity.ShopCategory;


//注入dao需要继承BaseTest
public class ShopDaoTest extends BaseTest {

	@Autowired
	private ShopDao shopDao;
	
	@Test
	@org.junit.Ignore
	public void testInsertShop() throws Exception {
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory sc = new ShopCategory();
		owner.setUserId(1L);	//与数据库中表的值相对应
		area.setAreaId(3);
		sc.setShopCategoryId(2L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(sc);
		shop.setShopName("测试店铺5");
		shop.setShopDesc("mytest1");
		shop.setShopAddr("testaddr1");
		shop.setPhone("1234656789");
		shop.setShopImg("test1");
		shop.setCreateTime(new Date());
		shop.setLastEditTime(new Date());
		shop.setEnableStatus(1);
		shop.setAdvice("审核中");
		int effectedNum = shopDao.insertShop(shop);
		assertEquals(1, effectedNum);
	}
	
	//更新店铺描述以及地址
	@Test
	public void testUpdateShop() throws Exception {
		Shop shop = new Shop();
		shop.setShopId(5L);
		shop.setShopDesc("测试");
		shop.setShopAddr("测试地址");
		shop.setLastEditTime(new Date());
		int effectedNum = shopDao.updateShop(shop);
		assertEquals(1, effectedNum);
	}


}
