package com.zzh.o2o.service;

import java.util.List;

import com.zzh.o2o.entity.HeadLine;

public interface HeadLineService {
	/**
	 * 
	 * @Title: queryHeadLineList
	 * 
	 * @Description: 查询headLine
	 * 
	 * @param headLineConditon
	 * 
	 * @return: List<HeadLine>
	 */
	List<HeadLine> queryHeadLineList(HeadLine headLineConditon);
}

