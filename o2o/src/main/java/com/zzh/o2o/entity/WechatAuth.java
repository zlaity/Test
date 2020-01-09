package com.zzh.o2o.entity;

import java.util.Date;

public class WechatAuth {	//微信账号信息实体类  通过用户id将本地账号和微信账号绑定
	
	private Long wechatAuthid;
	private String openId;
	private Date createTime;
	private PersonInfo personInfo;//创建用户的实体类
	
	public Long getWechatAuthid() {
		return wechatAuthid;
	}
	public void setWechatAuthid(Long wechatAuthid) {
		this.wechatAuthid = wechatAuthid;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
	

}
