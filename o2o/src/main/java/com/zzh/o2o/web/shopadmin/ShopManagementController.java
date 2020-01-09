package com.zzh.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzh.o2o.dto.ShopExecution;
import com.zzh.o2o.entity.Area;
import com.zzh.o2o.entity.PersonInfo;
import com.zzh.o2o.entity.Shop;
import com.zzh.o2o.entity.ShopCategory;
import com.zzh.o2o.enums.ShopStateEnum;
import com.zzh.o2o.exceptions.ShopOperationException;
import com.zzh.o2o.service.AreaService;
import com.zzh.o2o.service.ShopCategoryService;
import com.zzh.o2o.service.ShopService;
import com.zzh.o2o.util.CodeUtil;
import com.zzh.o2o.util.HttpServletRequestUtil;

/*
 * 实现店铺管理的相关逻辑
 */
@Controller
//指定访问路径 
@RequestMapping("/shopadmin")
public class ShopManagementController {

	@Autowired
	private ShopService shopService;

	@Autowired
	private ShopCategoryService shopCategoryService;

	@Autowired
	private AreaService areaService;

	@RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 接收shopcategory和区域的相关信息
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			// 返回success shopCategoryList areaList,前端通过
			// data.success来判断从而展示shopCategoryList和areaList的数据
			modelMap.put("success", true);
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	// 接收前端传进来的参数 代表客户端的请求
	// http请求头中的所有信息都封装在这个对象中 提供的方法可以获得用户请求的所有信息
	@RequestMapping(value = "/registershop", method = RequestMethod.POST) // 因为是表单 所以是post
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		// 定义hashmap类型返回值
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 判断验证码
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}

		// 1. 接收前端并转换相应的参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			// TODO: handle exception
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		// 接收前端传递过来的文件 接收到shopImg中
		// Spring MVC中的图片存在CommonsMultipartFile中
		CommonsMultipartFile shopImg = null;
		// 从request的本次会话中的上线文中获取图片的相关内容
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			// shopImg是和前端约定好的变量名
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			// 将错误信息返回给前台
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}

		// 2. 注册店铺
		if (shop != null && shopImg != null) {
			// Session TODO
			// 店主persionInfo的信息，肯定要登录才能注册店铺。
			// 所以这部分信息我们从session中获取，尽量不依赖前端,这里暂时时不具备条件，
			PersonInfo owner = new PersonInfo();
			owner.setUserId(1L);
			shop.setOwner(owner);
			// 注册店铺

			// se = shopService.addShop(shop, shopImg); 改造前的调用方式
			// 这个时候，我们从前端获取到的shopImg是CommonsMultipartFile类型的，如何将CommonsMultipartFile转换为file.
			// 网上也有将CommonsMultipartFile转换为File的方法，并通过maxInMemorySize的设置尽量不产生临时文件
			// 这里我们换个思路,因为CommonsMultipartFile可以获取InputStream,Thumbnailator又可以直接处理输入流
			// 因为InputStream中我们无法得到文件的名称，而thumbnail中需要根据文件名来获取扩展名，所以还要再加一个参数String类型的fileName
			// 既然第二个和第三个参数都是通过shopImg获取的，为什么不直接传入一个shopImg呢？
			// 主要是为了service层单元测测试的方便，因为service层很难实例化出一个CommonsMultipartFile类型的实例
			ShopExecution se = null;
			try {
				se = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					modelMap.put("errMsg", "注册成功");
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException | IOException e) {
				e.printStackTrace();
				modelMap.put("success", false);
				modelMap.put("errMsg", "addShop Error");
			}
		} else {
			// 将错误信息返回给前台
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
		}
		// 3. 返回结果
		return modelMap;
	}

	/*
	 * private static void inputStreamToFile(InputStream ins, File file) {
	 * OutputStream os = null; try { os = new FileOutputStream(file); int bytesRead
	 * = 0; byte[] buffer = new byte[1024]; while ((bytesRead = ins.read(buffer)) !=
	 * -1) { os.write(buffer, 0, bytesRead); } } catch (Exception e) { throw new
	 * RuntimeException("调用inputStreamToFile产生异常：" + e.getMessage()); } finally {
	 * try { if (os != null) { os.close(); } if (ins != null) { ins.close(); } }
	 * catch (IOException e) { throw new
	 * RuntimeException("inputStreamToFile关闭io产生异常：" + e.getMessage()); } } }
	 */

}
