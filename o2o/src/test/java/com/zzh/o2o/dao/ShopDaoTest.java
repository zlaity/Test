package com.zzh.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
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

	/**
	 * 测试店铺列表
	 * 
	 */
	@Test
	@Ignore
	public void testQueryShopListAndCount() {

		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		PersonInfo personInfo = new PersonInfo();
		List<Shop> shopList = null;

		/**
		 * 可输入的查询条件： 1.商铺名（要求模糊查询） 2.区域Id 3.商铺状态 4.商铺类别 5.owner
		 * 
		 * 
		 * 首先按照单个条件进行单元测试，然后组合测试
		 **/

		// 1.商铺名（要求模糊查询）
		Shop shopCondition = new Shop();
		shopCondition.setShopName("咖啡");

		// 1.1 数据库中只有3条数据符合 ，我们分页条件 取出5条，即全部取出 验证rowIndex 和 pageSize
		shopList = shopDao.queryShopList(shopCondition, 0, 5);
		assertEquals(3, shopList.size());

		int count1 = shopDao.queryShopCount(shopCondition);
		assertEquals(3, count1);

		// 1.2 数据库中只有3条数据符合 ，我们分页条件 取出2条，即取出前两条 验证rowIndex 和 pageSize
		shopList = shopDao.queryShopList(shopCondition, 0, 2);
		assertEquals(2, shopList.size());

		// 总数依然是3条
		int count2 = shopDao.queryShopCount(shopCondition);
		assertEquals(3, count2);

		// 为了不影响测试， 新实例化出来一个Shop

		// 2.区域Id 库表中符合条件的记录有10条 areaId=1 10条 areaId=2 3条
		Shop shopCondition2 = new Shop();
		area.setAreaId(1);
		shopCondition2.setArea(area);
		shopList = shopDao.queryShopList(shopCondition2, 0, 99);
		assertEquals(10, shopList.size());

		area.setAreaId(2);
		shopCondition2.setArea(area);
		shopList = shopDao.queryShopList(shopCondition2, 0, 99);
		assertEquals(3, shopList.size());

		// 3.商铺状态 EnableStatus=0 12条 EnableStatus=1 1条
		Shop shopCondition3 = new Shop();
		shopCondition3.setEnableStatus(0);
		shopList = shopDao.queryShopList(shopCondition3, 0, 99);
		assertEquals(12, shopList.size());

		shopCondition3.setEnableStatus(1);
		shopList = shopDao.queryShopList(shopCondition3, 0, 99);
		assertEquals(1, shopList.size());

		// 4.商铺类别
		// shop_category_id = 1 9条数据
		// shop_category_id = 2 3条数据
		// shop_category_id = 3 1条数据
		Shop shopCondition4 = new Shop();

		shopCategory.setShopCategoryId(1L);
		shopCondition4.setShopCategory(shopCategory);
		shopList = shopDao.queryShopList(shopCondition4, 0, 99);
		assertEquals(9, shopList.size());

		shopCategory.setShopCategoryId(2L);
		shopCondition4.setShopCategory(shopCategory);
		shopList = shopDao.queryShopList(shopCondition4, 0, 99);
		assertEquals(3, shopList.size());

		shopCategory.setShopCategoryId(3L);
		shopCondition4.setShopCategory(shopCategory);
		shopList = shopDao.queryShopList(shopCondition4, 0, 99);
		assertEquals(1, shopList.size());

		// 5.owner_id=1 13条 其余0条
		Shop shopCondition5 = new Shop();
		personInfo.setUserId(1L);
		shopCondition5.setOwner(personInfo);
		shopList = shopDao.queryShopList(shopCondition5, 0, 99);
		assertEquals(13, shopList.size());

		personInfo.setUserId(877L);
		shopCondition5.setOwner(personInfo);
		shopList = shopDao.queryShopList(shopCondition5, 0, 99);
		assertEquals(0, shopList.size());

		// 组合场景不全面，仅列几个

		// 组合场景 owner_id =1 shop_name like %咖啡%
		Shop shopCondition6 = new Shop();
		personInfo.setUserId(1L);
		shopCondition6.setOwner(personInfo);
		shopCondition6.setShopName("咖啡");
		shopList = shopDao.queryShopList(shopCondition6, 0, 99);
		assertEquals(3, shopList.size());

		int count6 = shopDao.queryShopCount(shopCondition6);
		assertEquals(3, count6);

		// 组合场景 area_id =1 shop_name like %咖啡% owner_id=1
		Shop shopCondition7 = new Shop();
		personInfo.setUserId(1L);
		area.setAreaId(1);
		shopCondition7.setOwner(personInfo);
		shopCondition7.setArea(area);
		shopCondition7.setShopName("咖啡");
		shopList = shopDao.queryShopList(shopCondition7, 0, 99);
		assertEquals(2, shopList.size());

		int count7 = shopDao.queryShopCount(shopCondition7);
		assertEquals(2, count7);
	}

	// 测试根据id查询
	@Test
	@org.junit.Ignore
	public void queryByShopId() {
		long shopId = 143;
		Shop shop = shopDao.queryByShopId(shopId);
		System.out.println("areaId:" + shop.getArea().getAreaId());
		System.out.println("areaName:" + shop.getArea().getAreaName());
	}

	// 测试添加店铺
	@Test
	@org.junit.Ignore
	public void testInsertShop() throws Exception {
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
		shop.setShopName("测试店铺6");
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

	// 更新店铺描述以及地址
	@Test
	@org.junit.Ignore
	public void testUpdateShop() throws Exception {
		Shop shop = new Shop();
		shop.setShopId(5L);
		shop.setShopDesc("测试");
		shop.setShopAddr("测试地址");
		shop.setLastEditTime(new Date());
		int effectedNum = shopDao.updateShop(shop);
		assertEquals(1, effectedNum);
	}

	@Test
	public void testSelectShopListAndCount2() {

		Shop shopCondition = new Shop();
		// 模拟
		ShopCategory childShopCategory = new ShopCategory();
		ShopCategory parentShopCategory = new ShopCategory();
		// 设置父类的shopCategoryId
		parentShopCategory.setShopCategoryId(12L);
		// 设置父子关系
		childShopCategory.setParent(parentShopCategory);

		// 设置目录
		shopCondition.setShopCategory(childShopCategory);

		List<Shop> shoplist = shopDao.queryShopList(shopCondition, 0, 8);
		int count = shopDao.queryShopCount(shopCondition);
		assertEquals(8, shoplist.size());
		assertEquals(8, count);
		for (Shop shop : shoplist) {
			System.out.println(shop.toString());
		}
	}

}
