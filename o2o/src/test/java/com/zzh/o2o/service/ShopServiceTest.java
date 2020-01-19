package com.zzh.o2o.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.zzh.o2o.BaseTest;
import com.zzh.o2o.dto.ImageHolder;
import com.zzh.o2o.dto.ShopExecution;
import com.zzh.o2o.entity.Area;
import com.zzh.o2o.entity.PersonInfo;
import com.zzh.o2o.entity.Shop;
import com.zzh.o2o.entity.ShopCategory;
import com.zzh.o2o.enums.ShopStateEnum;
/*
 * 测试
 * base负责初始化spring容器
 * 注解Autowired 初始化时将service注入到spring中
 */

public class ShopServiceTest extends BaseTest {
	@Autowired
	private ShopService shopService;

	// 修改店铺
	@Test
	@Ignore
	public void testModifyShop() {
		Shop shop = new Shop();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();

		shop.setShopId(21L);

		area.setAreaId(4);
		shopCategory.setShopCategoryId(2L);

		shop.setArea(area);
		shop.setShopCategory(shopCategory);

		shop.setShopName("Modify咖啡店");
		shop.setShopDesc("Modif的咖啡店");
		shop.setShopAddr("Modify-三食堂");
		shop.setPhone("123456");
		shop.setPriority(78);

		File shopFile = new File("E:/o2o/photos/time.jpg");

		ShopExecution se = null;
		InputStream ins = null;
		try {
			ins = new FileInputStream(shopFile);
			ImageHolder imageHolder = new ImageHolder(ins, shopFile.getName());
			se = shopService.modifyShop(shop, imageHolder);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		assertEquals(ShopStateEnum.SUCCESS.getState(), se.getState());
	}

	@Test
	@Ignore
	public void testAddShop() throws FileNotFoundException {
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory sc = new ShopCategory();
		owner.setUserId(1L); // 与数据库中表的值相对应
		area.setAreaId(3);
		sc.setShopCategoryId(1L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(sc);
		shop.setShopName("测试店铺5");
		shop.setShopDesc("mytest5");
		shop.setShopAddr("mytest5");
		shop.setPhone("mytest5");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		File shopImg = new File("E:/o2o/photos/timg.jpg");

		ShopExecution se = null;
		InputStream ins = null;
		try {
			ins = new FileInputStream(shopImg);
			ImageHolder imageHolder = new ImageHolder(ins, shopImg.getName());
			se = shopService.addShop(shop, imageHolder);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
		/*
		 * InputStream is = new FileInputStream(shopImg); ShopExecution se =
		 * shopService.addShop(shop, is,shopImg.getName());
		 * assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
		 */
	}
	
	@Test
	public void testGetShopList() {

		Shop shopCondition = new Shop();
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);

		shopCondition.setOwner(personInfo);
		shopCondition.setShopName("咖啡");

		// 符合 shop_name like '%咖啡%' 且 owner_id =1 有3条数据，

		// 第二个参数 和 第三个参数 从pageIndex=1 第一页取数据，取2条 pageSize=2
		ShopExecution se = shopService.getShopList(shopCondition, 1, 2);
		// 按照tb_shop中的数据筛选 符合条件的数据3条， 从第一页开始取2条，se.getShopList().size() 应该有2条数据，
		assertNotNull(se);
		assertEquals(2, se.getShopList().size());
		assertEquals(3, se.getCount());

		// 按照tb_shop中的数据筛选 符合条件的数据3条， 从第2页开始取2条，se.getShopList().size()
		// 应该只有1条数据，总数仍为3
		se = shopService.getShopList(shopCondition, 2, 2);
		assertNotNull(se);
		assertEquals(1, se.getShopList().size());
		assertEquals(3, se.getCount());
	}



}
