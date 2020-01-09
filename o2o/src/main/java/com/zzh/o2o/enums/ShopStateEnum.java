package com.zzh.o2o.enums;

/*
 * enum构造器 私有化 
 * 因为不希望有其他可以改变enum值 check、offline等
 * 只能通过内部的私有构造器去修改
 * 
 */
public enum ShopStateEnum {
	// 刚创建时的状态
	CHECK(0, "审核中"), OFFLINE(-1, "非法店铺"), SUCCESS(1, "操作成功"), PASS(2, "通过认证"), INNER_ERROR(-1001, "内部系统错误"),
	NULL_SHOPID(-1002, "ShopId为空"),NULL_SHOP(-1003,"shop信息为空");

	private String stateInfo;
	private int state;

	private ShopStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/*
	 * 依据传入的state返回相应的enum值
	 * 
	 */
	public static ShopStateEnum stateOf(int state) {
		for (ShopStateEnum stateEnum : values()) {
			if (stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public int getState() {
		return state;
	}

}
