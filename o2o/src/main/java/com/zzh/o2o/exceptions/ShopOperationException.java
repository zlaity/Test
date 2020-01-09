package com.zzh.o2o.exceptions;

/*
 * 封装业务的异常
 * 对于shopService层是具有事务的性质的，
 * 如果出错会回滚，但是只有抛出运行时错误，才回滚，
 * 所以我们新建一个运行时错误类继承自RuntimeExcuption
 */
public class ShopOperationException extends RuntimeException {


	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -6032690204603750960L;

	// 接收错误的信息
	public ShopOperationException(String msg) {
		// 构造函数
		super(msg);
	}

}
