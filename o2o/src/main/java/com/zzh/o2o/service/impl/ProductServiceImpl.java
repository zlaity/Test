package com.zzh.o2o.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzh.o2o.dao.ProductDao;
import com.zzh.o2o.dao.ProductImgDao;
import com.zzh.o2o.dto.ImageHolder;
import com.zzh.o2o.dto.ProductExecution;
import com.zzh.o2o.entity.Product;
import com.zzh.o2o.entity.ProductImg;
import com.zzh.o2o.enums.ProductStateEnum;
import com.zzh.o2o.exceptions.ProductOperationException;
import com.zzh.o2o.service.ProductService;
import com.zzh.o2o.util.ImageUtils;
import com.zzh.o2o.util.PageCalculator;
import com.zzh.o2o.util.PathUtils;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductDao productDao;
	@Autowired
	ProductImgDao productImgDao;

	// 废弃不使用的方法
	@Deprecated
	@Override
	public ProductExecution addProductDep(Product product, InputStream prodImgIns, String prodImgName,
			List<InputStream> prodImgDetailInsList, List<String> prodImgDetailNameList)
			throws ProductOperationException {
		// 废弃的方法
		return null;
	}

	/**
	 * 注意事务控制@Transactional
	 * 
	 * 步骤如下：
	 * 
	 * 1.处理商品的缩略图，获取相对路径，为了调用dao层的时候写入 tb_product中的 img_addr字段有值
	 * 
	 * 2.写入tb_product ，获取product_id
	 * 
	 * 3.结合product_id 批量处理商品详情图片
	 * 
	 * 4.将商品详情图片 批量更新到 tb_proudct_img表
	 * 
	 */
	@Override
	@Transactional
	public ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> prodImgDetailList)
			throws ProductOperationException {
		// 空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null
				&& product.getProductCategory().getProductCategoryId() != null) {
			// 设置默认的属性 1 展示
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			// 默认为上架状态
			product.setEnableStatus(1);
			// 若商品的缩略图不为空则添加
			// 如果文件的输入流和文件名不为空，添加文件到特定目录，并且将相对路径设置给product,这样product就有了ImgAddr，为下一步的插入tb_product提供了数据来源
			if (imageHolder != null) {
				addProductImg(product, imageHolder);
			}
			try {
				// 写入tb_product
				int effectNum = productDao.insertProduct(product);
				if (effectNum <= 0) {
					throw new ProductOperationException("商品创建失败");
				}
				// 如果添加商品成功，继续处理商品详情图片，并写入tb_product_img
				if (prodImgDetailList != null && prodImgDetailList.size() > 0) {
					addProductDetailImgs(product, prodImgDetailList);
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch (Exception e) {
				throw new ProductOperationException("商品创建失败：" + e.getMessage());
			}

		} else {
			return new ProductExecution(ProductStateEnum.NULL_PARAMETER);
		}
	}

	/**
	 * @Title: addProductImg
	 * @Description: 将商品的缩略图写到特定的shopId目录，并将imgAddr属性设置给product
	 * @param product
	 * @param imageHolder
	 * @return: void
	 */
	private void addProductImg(Product product, ImageHolder imageHolder) {
		// 根据shopId获取图片存储的相对路径
		String relativePath = PathUtils.getShopImagePath(product.getShop().getShopId());
		// 添加图片到指定的目录
		String relativeAddr = ImageUtils.generateThumbnails(imageHolder, relativePath);
		// 将relativeAddr设置给product
		product.setImgAddr(relativeAddr);
	}

	/**
	 * @Title: addProductDetailImgs
	 * @Description: 批量添加图片，处理商品详情图片，并写入tb_product_img
	 * @param product
	 * @param prodImgDetailList
	 * @return: void
	 */
	private void addProductDetailImgs(Product product, List<ImageHolder> prodImgDetailList) {
		// 获取图片存储路径，这里直接存放在相应店铺文件夹底下
		String relativePath = PathUtils.getShopImagePath(product.getShop().getShopId());
		// 生成图片详情的图片,大一些，并且不添加水印，所以另外写了一个方法，基本和generateThumbnails相似
		List<String> imgAddrList = ImageUtils.generateNormalImgs(prodImgDetailList, relativePath);
		// 遍历图片一次去处理，并添加进productImg实体类中
		if (imgAddrList != null && imgAddrList.size() > 0) {
			List<ProductImg> productImgList = new ArrayList<ProductImg>();
			for (String imgAddr : imgAddrList) {
				ProductImg productImg = new ProductImg();
				productImg.setImgAddr(imgAddr);
				productImg.setProductId(product.getProductId());
				productImg.setCreateTime(new Date());
				productImgList.add(productImg);
			}
			// 如果确实是有图片需要添加的，就执行批量添加操作
			try {
				int effectedNum = productImgDao.batchInsertProductImg(productImgList);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建商品详情图片失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建商品详情图片失败:" + e.toString());
			}
		}
	}

	/**
	 * 商品编辑 注意事务控制@Transactional
	 */
	@Override
	@Transactional
	public ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> prodImgDetailList)
			throws ProductOperationException {
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null && product.getShop()
				.getShopId() != null/* product.getProductCategory().getProductCategoryId() != null */) {
			// 设置默认的属性
			product.setLastEditTime(new Date());

			// Step1. 处理缩略图
			if (imageHolder != null) {
				Product tempProduct = productDao.selectProductById(product.getProductId());
				// 1.1 删除旧的缩略图
				if (tempProduct.getImgAddr() != null) {
					ImageUtils.deleteStorePath(tempProduct.getImgAddr());
				}
				// 1.2 添加新的缩略图
				addProductImg(product, imageHolder);
			}

			// Step2. 处理商品详情

			// 如果添加商品成功，继续处理商品详情图片，并写入tb_product_img
			if (prodImgDetailList != null && prodImgDetailList.size() > 0) {
				// 2.1 删除库表中productId对应的tb_product_img的信息
				deleteProductImgs(product.getProductId());
				// 2.2 处理商品详情图片，并写入tb_product_img
				addProductDetailImgs(product, prodImgDetailList);
			}
			try {
				// Step3.更新tb_product
				int effectNum = productDao.updateProduct(product);
				if (effectNum <= 0) {
					throw new ProductOperationException("商品更新失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch (Exception e) {
				throw new ProductOperationException("商品更新失败：" + e.getMessage());
			}

		} else {
			return new ProductExecution(ProductStateEnum.NULL_PARAMETER);
		}
	}

	private void deleteProductImgs(Long productId) {
		// 获取该商铺下对应的productImg信息
		List<ProductImg> productImgList = productImgDao.selectProductImgList(productId);
		// 遍历删除该目录下的全部文件
		for (ProductImg productImg : productImgList) {
			ImageUtils.deleteStorePath(productImg.getImgAddr());
		}
		// 删除tb_product_img中该productId对应的记录
		productImgDao.deleteProductImgById(productId);

	}

	@Override
	public Product queryProductById(long productId) {
		return productDao.selectProductById(productId);
	}

	@Override
	public ProductExecution queryProductionList(Product productCondition, int pageIndex, int pageSize)
			throws ProductOperationException {
		List<Product> productList = null;
		int count = 0;
		try {
			// 将pageIndex 转换为Dao层识别的rowIndex
			int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
			// 调用Dao层获取productList和总量
			productList = productDao.selectProductList(productCondition, rowIndex, pageSize);
			count = productDao.selectCountProduct(productCondition);
		} catch (Exception e) {
			e.printStackTrace();
			new ProductExecution(ProductStateEnum.INNER_ERROR);
		}
		return new ProductExecution(ProductStateEnum.SUCCESS, productList, count);
	}

}
