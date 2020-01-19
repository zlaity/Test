package com.zzh.o2o.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	Logger logger = LoggerFactory.getLogger(AreaController.class);

	@Autowired
	private AreaService areaService;

	@RequestMapping(value = "/listarea", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listArea() {

		logger.info("-----begin getAreas------");
		Long beginTimeLong = System.currentTimeMillis();
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
			logger.error("exception happpens , desc [{}] ", e.getMessage());
		}
		Long endTimeLong = System.currentTimeMillis();
		logger.debug("cost [{}ms]", endTimeLong - beginTimeLong);
		logger.info("-----end getAreas------");
		return modelMap;
	}
}
