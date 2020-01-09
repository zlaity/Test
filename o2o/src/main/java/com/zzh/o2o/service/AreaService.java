package com.zzh.o2o.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zzh.o2o.entity.Area;

public interface AreaService {

	/**
	 * 
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	List<Area> getAreaList() throws JsonParseException, JsonMappingException, IOException;

	/*
	 * AreaExecution addArea(Area area);
	 * 
	 * AreaExecution modifyArea(Area area);
	 * 
	 * AreaExecution removeArea(long areaId);
	 * 
	 * AreaExecution removeAreaList(List<Long> areaIdList);
	 */
}
