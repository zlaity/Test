package com.zzh.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.zzh.o2o.BaseTest;
import com.zzh.o2o.entity.Product;
import com.zzh.o2o.entity.ProductCategory;
import com.zzh.o2o.entity.Shop;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest extends BaseTest {

	@Autowired
	private ProductDao productDao;

	@Test
	@Ignore
	public void testA_InsertProdcut() {

		// 注意表中的外键关系，确保这些数据在对应的表中的存在
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(3L);

		// 注意表中的外键关系，确保这些数据在对应的表中的存在
		Shop shop = new Shop();
		shop.setShopId(6L);
		// 初始化三个商品实例并添加进shopId为1的店铺 商品类别也为1
		Product product = new Product();
		product.setProductName("test_product");
		product.setProductDesc("product desc");
		product.setImgAddr("/aaa/bbb");
		product.setNormalPrice("10");
		product.setPromotionPrice("8");
		product.setPriority(66);
		product.setCreateTime(new Date());
		product.setLastEditTime(new Date());
		product.setEnableStatus(1);
		product.setProductCategory(productCategory);
		product.setShop(shop);

		int effectNum = productDao.insertProduct(product);
		assertEquals(1, effectNum);
	}

	@Test
	@Ignore
	public void testB_UpdateProduct() {

		// 注意表中的外键关系，确保这些数据在对应的表中的存在
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(3L);

		// 注意表中的外键关系，确保这些数据在对应的表中的存在
		Shop shop = new Shop();
		shop.setShopId(6L);

		Product product = new Product();
		product.setProductName("modifyProduct");
		product.setProductDesc("modifyProduct desc");
		product.setImgAddr("/mmm/ddd");
		product.setNormalPrice("350");
		product.setPromotionPrice("300");
		product.setPriority(66);
		product.setLastEditTime(new Date());
		product.setEnableStatus(1);

		product.setProductCategory(productCategory);
		product.setShop(shop);

		// 设置productId
		 product.setProductId(16L);

		int effectNum = productDao.updateProduct(product);
		assertEquals(1, effectNum);

	}

	@Test
	@Ignore
	public void testC_SelectProductListAndCount() {
		int rowIndex = 1;
		int pageSize = 2;
		List<Product> productList = new ArrayList<Product>();
		int effectNum = 0;

		Shop shop = new Shop();
		shop.setShopId(6L);

		Product productCondition = new Product();
		productCondition.setShop(shop);

		productList = productDao.selectProductList(productCondition, rowIndex, pageSize);
		assertEquals(2, productList.size());

		effectNum = productDao.selectCountProduct(productCondition);
		assertEquals(6, effectNum);

		System.out.println("==========================================");

		Shop shop2 = new Shop();
		shop2.setShopId(6L);

		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(3L);

		Product productCondition2 = new Product();
		productCondition2.setShop(shop2);
		productCondition2.setProductCategory(productCategory);
		productCondition2.setProductName("test");

		productList = productDao.selectProductList(productCondition2, rowIndex, pageSize);
		assertEquals(2, productList.size());

		effectNum = productDao.selectCountProduct(productCondition2);
		assertEquals(5, effectNum);
	}

	@Test
	@Ignore
	public void testUpdateProductCategory2Null() {
		long productCategoryId = 3L;
		long shopId = 6L;
		int effectNum = productDao.updateProductCategory2Null(productCategoryId, shopId);
		assertEquals(1, effectNum);

		productCategoryId = 36L;
		effectNum = productDao.updateProductCategory2Null(productCategoryId, shopId);
		assertEquals(6, effectNum);

	}
}
