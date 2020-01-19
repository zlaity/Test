package com.zzh.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zzh.o2o.BaseTest;
import com.zzh.o2o.dto.ImageHolder;
import com.zzh.o2o.dto.ProductExecution;
import com.zzh.o2o.entity.Product;
import com.zzh.o2o.entity.ProductCategory;
import com.zzh.o2o.entity.Shop;
import com.zzh.o2o.enums.ProductStateEnum;

public class ProductServiceTest extends BaseTest {

	@Autowired
	private ProductService productService;

	@Test
	@Ignore
	public void testAddProduct() throws Exception {

		// 注意表中的外键关系，确保这些数据在对应的表中的存在
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(16L);

		// 注意表中的外键关系，确保这些数据在对应的表中的存在
		Shop shop = new Shop();
		shop.setShopId(6L);

		// 构造Product
		Product product = new Product();
		product.setProductName("test_product");
		product.setProductDesc("product desc");

		product.setNormalPrice("10");
		product.setPromotionPrice("8");
		product.setPriority(66);
		product.setCreateTime(new Date());
		product.setLastEditTime(new Date());
		product.setEnableStatus(1);
		product.setProductCategory(productCategory);
		product.setShop(shop);

		// 构造 商品图片 创建缩略图的文件流
		File productFile = new File("E:/o2o/photos/product/timg.jpg");
		InputStream ins = new FileInputStream(productFile);
		ImageHolder imageHolder = new ImageHolder(ins, productFile.getName());

		// 构造商品详情图片 创建两个商品详情图文件流并将他们添加到详情图列表中
		List<ImageHolder> prodImgDetailList = new ArrayList<ImageHolder>();

		File productDetailFile1 = new File("E:/o2o/photos/product1/timg1.jpg");
		InputStream ins1 = new FileInputStream(productDetailFile1);
		ImageHolder imageHolder1 = new ImageHolder(ins1, productDetailFile1.getName());

		File productDetailFile2 = new File("E:/o2o/photos/product1/timg2.jpg");
		InputStream ins2 = new FileInputStream(productDetailFile2);
		ImageHolder imageHolder2 = new ImageHolder(ins2, productDetailFile2.getName());

		prodImgDetailList.add(imageHolder1);
		prodImgDetailList.add(imageHolder2);

		// 调用服务 添加商品并验证
		ProductExecution pe = productService.addProduct(product, imageHolder, prodImgDetailList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
	}

	@Test
	@Ignore
	public void testModifyProduct() throws Exception {

		// 注意表中的外键关系，确保这些数据在对应的表中的存在
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(16L);

		// 注意表中的外键关系，确保这些数据在对应的表中的存在
		Shop shop = new Shop();
		shop.setShopId(6L);

		// 构造Product
		Product product = new Product();
		product.setProductName("offical_product");
		product.setProductDesc("product offical desc");

		product.setNormalPrice("100");
		product.setPromotionPrice("80");
		product.setPriority(66);
		product.setLastEditTime(new Date());
		product.setProductCategory(productCategory);
		product.setShop(shop);

		product.setProductId(19L);
		// 构造 商品图片
		File productFile = new File("E:/o2o/photos/product/timg.jpg");
		InputStream ins = new FileInputStream(productFile);
		ImageHolder imageHolder = new ImageHolder(ins, productFile.getName());

		// 构造商品详情图片
		List<ImageHolder> prodImgDetailList = new ArrayList<ImageHolder>();

		File productDetailFile1 = new File("E:/o2o/photos/product1/timg1.jpg");
		InputStream ins1 = new FileInputStream(productDetailFile1);
		ImageHolder imageHolder1 = new ImageHolder(ins1, productDetailFile1.getName());

		File productDetailFile2 = new File("E:/o2o/photos/product1/timg2.jpg");
		InputStream ins2 = new FileInputStream(productDetailFile2);
		ImageHolder imageHolder2 = new ImageHolder(ins2, productDetailFile2.getName());

		prodImgDetailList.add(imageHolder1);
		prodImgDetailList.add(imageHolder2);

		// 调用服务
		ProductExecution pe = productService.modifyProduct(product, imageHolder, prodImgDetailList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());

	}

	@Test
	@Ignore
	public void testQueryProductListAndCount() {
		// 库表中符合如下筛选条件的记录为5条
		// select * from tb_product a where a.product_category_id = 36 and
		// a.shop_id = 5 and a.product_name like '%test%';

		// 从第1页开始取，每页取3条
		int pageIndex = 1;
		int pageSize = 3;

		Shop shop2 = new Shop();
		shop2.setShopId(5L);

		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(36L);

		Product productCondition = new Product();
		productCondition.setShop(shop2);
		productCondition.setProductCategory(productCategory);
		productCondition.setProductName("test");

		ProductExecution productExecution = productService.queryProductionList(productCondition, pageIndex, pageSize);
		// 操作成功的状态为1
		assertEquals(1, productExecution.getState());
		assertEquals(3, productExecution.getProductList().size());
		assertEquals(5, productExecution.getCount());

		// 从第2页开始取，每页依然取3条
		pageIndex = 2;
		productExecution = productService.queryProductionList(productCondition, pageIndex, pageSize);
		assertEquals(1, productExecution.getState());
		assertEquals(2, productExecution.getProductList().size());
		assertEquals(5, productExecution.getCount());

	}

}
