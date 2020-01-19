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
import com.zzh.o2o.entity.ProductCategory;


/**
 * 
 * @ClassName: ProductCategoryTest
 * 
 * @Description: Junit 4.11里增加了指定测试方法执行顺序的特性 .
 * 
 *               测试类的执行顺序可通过对测试类添加注解@FixMethodOrder(value) 来指定,其中value 为执行顺序
 * 
 *               三种执行顺序可供选择：
 * 
 *               默认（MethodSorters.DEFAULT）,
 *               默认顺序由方法名hashcode值来决定，如果hash值大小一致，则按名字的字典顺序确定
 *               由于hashcode的生成和操作系统相关
 *               (以native修饰），所以对于不同操作系统，可能会出现不一样的执行顺序，在某一操作系统上，多次执行的顺序不变
 * 
 *               按方法名（ MethodSorters.NAME_ASCENDING）【推荐】,
 *               按方法名称的进行排序，由于是按字符的字典顺序，所以以这种方式指定执行顺序会始终保持一致；
 *               不过这种方式需要对测试方法有一定的命名规则，如 测试方法均以testNNN开头（NNN表示测试方法序列号 001-999）
 * 
 *               JVM（MethodSorters.JVM）
 *               按JVM返回的方法名的顺序执行，此种方式下测试方法的执行顺序是不可预测的，即每次运行的顺序可能都不一样
 */

//按照方法的名字顺序去执行
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest {

	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Test
	public void testB_QueryShopCategoryList() {
		long shopId = 6;
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		System.out.println("自定义类别数目：" + productCategoryList.size());
	}

	@Test
	@Ignore
	public void testA_BatchInsertProductCategory() {

		ProductCategory productCategory1 = new ProductCategory();
		productCategory1.setProductCategoryName("ProductCategoryTest1");
		productCategory1.setPriority(300);
		productCategory1.setCreateTime(new Date());
		productCategory1.setShopId(10L);

		ProductCategory productCategory2 = new ProductCategory();
		productCategory2.setProductCategoryName("ProductCategoryTest2");
		productCategory2.setPriority(600);
		productCategory2.setCreateTime(new Date());
		productCategory2.setShopId(10L);

		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		productCategoryList.add(productCategory1);
		productCategoryList.add(productCategory2);

		int effectNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
		assertEquals(2, effectNum);

	}

	@Test
	@Ignore
	public void testC_DeleteProductCategory() {
		// 查询出来shopId=5的商铺下面全部的商品目录
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(10L);
		// 遍历循环删除
		for (ProductCategory productCategory : productCategoryList) {
			if ("product1".equals(productCategory.getProductCategoryName())
					|| "product2".equals(productCategory.getProductCategoryName())) {
				int effectNum = productCategoryDao.deleteProductCategory(productCategory.getProductCategoryId(), 6L);
				assertEquals(1, effectNum);
			}
		}
	}
}