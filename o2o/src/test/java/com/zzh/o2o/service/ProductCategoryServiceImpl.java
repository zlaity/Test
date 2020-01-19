package com.zzh.o2o.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zzh.o2o.dto.ProductCategoryExecution;

public class ProductCategoryServiceImpl {
	@Autowired
	private ProductCategoryService productCategoryService;
	@Test
	public void testDeleteProductCategory() {

		ProductCategoryExecution productCategoryExecution = productCategoryService.deleteProductCategory(26, 5);
		assertEquals(1, productCategoryExecution.getState());
		ProductCategoryExecution productCategoryExecution2 = productCategoryService.deleteProductCategory(27, 5);
		assertEquals(1, productCategoryExecution2.getState());
	}


}
