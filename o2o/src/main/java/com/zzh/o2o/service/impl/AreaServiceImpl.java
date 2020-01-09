package com.zzh.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzh.o2o.dao.AreaDao;
import com.zzh.o2o.entity.Area;
import com.zzh.o2o.service.AreaService;
/*
 * 别的类有調用AreaService接口  springIOC会动态将impl注入接口中  
 * 
 */
@Service
public class AreaServiceImpl implements AreaService {
	
	@Autowired
	private AreaDao areaDao;
	@Override
	public List<Area> getAreaList(){
		
		return areaDao.queryArea(); //从数据库中取出列表
		
	}

}
