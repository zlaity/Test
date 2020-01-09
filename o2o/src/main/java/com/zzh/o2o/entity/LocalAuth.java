package com.zzh.o2o.entity;

import java.util.Date;

public class LocalAuth {	//本地账号实体类  通过用户id将本地账号和微信账号绑定

	private Long localAuth;
	private String username;
	private String password;
	private Date createtime;
	private Date lastEditTime;
	private PersonInfo personInfo;

	public Long getLocalAuth() {
		return localAuth;
	}

	public void setLocalAuth(Long localAuth) {
		this.localAuth = localAuth;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getLastEditTime() {
		return lastEditTime;
	}

	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public PersonInfo getPersonInfo() {
		return personInfo;
	}

	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}

}
