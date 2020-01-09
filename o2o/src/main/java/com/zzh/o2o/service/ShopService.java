package com.zzh.o2o.service;


import java.io.InputStream;
import com.zzh.o2o.dto.ShopExecution;
import com.zzh.o2o.entity.Shop;
import com.zzh.o2o.exceptions.ShopOperationException;

public interface ShopService {
	///存储店铺图片  传入filename获取文件名字 inputstream没有办法获取名字
	ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName) throws ShopOperationException;
	
}
