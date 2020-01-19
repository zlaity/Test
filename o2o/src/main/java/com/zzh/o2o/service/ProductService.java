package com.zzh.o2o.service;

import java.io.InputStream;
import java.util.List;

import com.zzh.o2o.dto.ImageHolder;
import com.zzh.o2o.dto.ProductExecution;
import com.zzh.o2o.entity.Product;
import com.zzh.o2o.exceptions.ProductOperationException;

public interface ProductService {
	/**
	 * @Title: addProductDep 废弃的方法
	 * @Description: 新增商品 。 因为无法从InputStream中获取文件的名称，所以需要指定文件名
	 *               需要传入的参数太多，我们将InputStream 和 ImgName封装到一个实体类中，便于调用。
	 *               及早进行优化整合，避免后续改造成本太大
	 * 
	 * @param product           商品信息
	 * @param prodImgIns        商品缩略图输入流
	 * @param prodImgName       商品缩略图名称
	 * @param prodImgDetailIns  商品详情图片的输入流
	 * @param prodImgDetailName 商品详情图片的名称
	 * @return
	 * @throws ProductOperationException
	 * @return: ProductExecution
	 */

	@Deprecated
	ProductExecution addProductDep(Product product, InputStream prodImgIns, String prodImgName,
			List<InputStream> prodImgDetailInsList, List<String> prodImgDetailNameList)
			throws ProductOperationException;

	/**
	 * @Title: addProduct
	 * @Description: 重构后的addProduct
	 * 
	 * @param product           产品信息
	 * @param imageHolder       产品缩略图的封装信息
	 * @param prodImgDetailList 产品详情图片的封装信息
	 * @return
	 * @throws ProductOperationException
	 * @return: ProductExecution
	 */
	ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> prodImgDetailList)
			throws ProductOperationException;

	/*
	 * 1. 如用户上传了缩略图，则将原有的缩略图删除（磁盘上删除），并更新tb_product表的img_addr字段，否则不做任何处理。
	 *
	 * 2. 如果用户上传了新的商品详情图片，则将原有的属于该productId下的全部的商品详情图删除（磁盘上删除），
	 * 同时删除productId对应的tb_product_img中的全部数据。
	 *
	 * 3. 更新tb_product的信息
	 */

	/**
	 * @Title: queryProductById
	 * @Description: 根据productId查询product
	 * @param productId
	 * @return: Product
	 */
	Product queryProductById(long productId);

	/**
	 * @Title: modifyProduct
	 * @Description: TODO
	 * @param product           产品信息
	 * @param imageHolder       产品缩略图的封装信息
	 * @param prodImgDetailList 产品详情图片的封装信息
	 * @throws ProductOperationException
	 * @return: ProductExecution
	 */
	ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> prodImgDetailList)
			throws ProductOperationException;

	/**
	 * @Title: queryProductionList
	 * 
	 * @Description: 查询
	 * 
	 * @param productCondition
	 * @param pageIndex        前端页面 只有第几页 第几页 定义为pageIndex
	 * @param pageSize         一页中展示的行数
	 * @throws ProductOperationException
	 * 
	 * @return: ProductExecution
	 */
	ProductExecution queryProductionList(Product productCondition, int pageIndex, int pageSize)
			throws ProductOperationException;

}
