package com.zzh.o2o.dao;

import org.apache.ibatis.annotations.Param;

import com.zzh.o2o.entity.LocalAuth;

public interface LocalAuthDao {

	/**
	 * 
	 * 
	 * @Title: queryLocalByUserNameAndPwd
	 * 
	 * @Description: 根据用户名和密码查询用户
	 * 
	 * @param userName
	 * @param password
	 * 
	 * @return: LocalAuth
	 */
	LocalAuth queryLocalByUserNameAndPwd(@Param("userName") String userName, @Param("password") String password);
}

