	package com.zzh.o2o.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzh.o2o.entity.Area;
import com.zzh.o2o.service.AreaService;

@Controller
@RequestMapping("/superadmin")
public class AreaController {
	/* Logger logger = LoggerFactory.getLogger(AreaController.class); */

	@Autowired
	private AreaService areaService;

	@RequestMapping(value = "/listarea", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listArea() {
		/*
		 * logger.info("==start=="); long startTime = System.currentTimeMillis();
		 */
		// modemap用来存放方法的返回值 key唯一不重复
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取service层返回的区域列表
		List<Area> list = new ArrayList<Area>();
		try {
			list = areaService.getAreaList();
			modelMap.put("rows", list);
			modelMap.put("total", list.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		/*
		 * logger.error("tset error!"); long endTime = System.currentTimeMillis();
		 * logger.debug("costTime:[{}ms]",endTime-startTime); logger.info("==end==");
		 */
		return modelMap;
	}
}
