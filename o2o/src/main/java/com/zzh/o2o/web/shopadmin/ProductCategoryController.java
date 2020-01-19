package com.zzh.o2o.web.shopadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzh.o2o.dto.ProductCategoryExecution;
import com.zzh.o2o.dto.Result;
import com.zzh.o2o.entity.ProductCategory;
import com.zzh.o2o.entity.Shop;
import com.zzh.o2o.enums.ProductCategoryStateEnum;
import com.zzh.o2o.exceptions.ProductCategoryOperationException;
import com.zzh.o2o.service.ProductCategoryService;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryController {

	@Autowired
	private ProductCategoryService productCategoryService;

	@RequestMapping(value = "/getproductcategorybyshopId", method = RequestMethod.GET)
	@ResponseBody
	private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
		// 取出本次会话中currentShop的值
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		List<ProductCategory> list = null;
		if (currentShop != null && currentShop.getShopId() > 0) {
			// 返回属于商铺列表的id
			list = productCategoryService.getProductCategoryList(currentShop.getShopId());
			return new Result<List<ProductCategory>>(true, list);
		} else {
			// 证明对商铺的类别没有访问权限
			ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
			return new Result<List<ProductCategory>>(false, ps.getState(), ps.getStateInfo());
		}
	}

	/**
	 * @Title: addProductCategory
	 * @Description: 添加商铺目录 ，使用@RequestBody接收前端传递过来的productCategoryList
	 * @param productCategoryList
	 * @param request
	 * @return: Map<String,Object>
	 */
	@RequestMapping(value = "/addproductcategory", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addProductCategory(@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (productCategoryList != null && productCategoryList.size() > 0) {
			// 从session中获取shop的信息
			Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
			if (currentShop != null && currentShop.getShopId() != null) {
				// 为ProductCategory设置shopId
				for (ProductCategory productCategory : productCategoryList) {
					productCategory.setShopId(currentShop.getShopId());
				}
				try {
					// 批量插入
					ProductCategoryExecution pce = productCategoryService.addProductCategory(productCategoryList);
					if (pce.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
						modelMap.put("success", true);
						// 同时也将新增成功的数量返回给前台
						modelMap.put("effectNum", pce.getCount());
					} else {
						modelMap.put("success", false);
						modelMap.put("errMsg", pce.getStateInfo());
					}
				} catch (ProductCategoryOperationException e) {
					e.printStackTrace();
					modelMap.put("success", false);
					modelMap.put("errMsg", e.getMessage());
					return modelMap;
				}
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", ProductCategoryStateEnum.NULL_SHOP.getStateInfo());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "至少输入一个店铺目录信息");
		}
		return modelMap;
	}

	/**
	 * 
	 * 
	 * @Title: remooveProductCategory
	 * 
	 * @Description: 删除商品目录
	 * 
	 * @param productCategoryId
	 * @param request
	 * 
	 * @return: Map<String,Object>
	 */
	@RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> remooveProductCategory(Long productCategoryId, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (productCategoryId != null && productCategoryId > 0) {
			// 从session中获取shop的信息
			Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
			if (currentShop != null && currentShop.getShopId() != null) {
				try {
					// 删除
					Long shopId = currentShop.getShopId();
					ProductCategoryExecution pce = productCategoryService.deleteProductCategory(productCategoryId,
							shopId);
					if (pce.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
						modelMap.put("success", true);
					} else {
						modelMap.put("success", false);
						modelMap.put("errMsg", pce.getStateInfo());
					}
				} catch (ProductCategoryOperationException e) {
					e.printStackTrace();
					modelMap.put("success", false);
					modelMap.put("errMsg", e.getMessage());
					return modelMap;
				}
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", ProductCategoryStateEnum.NULL_SHOP.getStateInfo());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请选择商品类别");
		}
		return modelMap;
	}

}
