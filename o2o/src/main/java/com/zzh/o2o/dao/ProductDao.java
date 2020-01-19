package com.zzh.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zzh.o2o.entity.Product;

public interface ProductDao {

	/**
	 * @Title: insertProduct
	 * @Description: 增加商品
	 * @param product
	 * @return: int
	 */
	int insertProduct(Product product);

	/**
	 * @Title: selectProductById
	 * @Description: 根据productId查询product
	 * @param productId
	 * @return: Product
	 */
	Product selectProductById(long productId);

	/**
	 * @Title: updateProduct
	 * @Description: 修改商品
	 * @param product
	 * @return: int
	 */
	int updateProduct(Product product);

	/**
	 * @Title: selectProductList
	 * @Description: 支持分页功能的查询product
	 *               需要支持根据商品名称（支持模糊查询）、商品状态、shopId、商品类别的查询及组合查询
	 * @param productCondition
	 * @param rowIndex         从第几行开始取
	 * @param pageSize         返回多少行数据（页面上的数据量）
	 *                         比如 rowIndex为1,pageSize为5 即为 从第一行开始取，取5行数据
	 * @return: List<Product>
	 */
	List<Product> selectProductList(@Param("productCondition") Product productCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	/**
	 * @Title: selectCountProduct
	 * @Description: 按照条件查询 符合前台传入的条件的商品的总数
	 * @param productCondition
	 * @return
	 * @return: int
	 */
	int selectCountProduct(@Param("productCondition") Product productCondition);

	/**
	 * @Title: updateProductCategory2Null
	 * @Description: 删除productCategory的时候，需要先将tb_product中的该productCategoryId置为null
	 * @param productCategoryId
	 * @param shopId
	 * @return: int
	 */
	int updateProductCategory2Null(@Param("productCategoryId") long productCategoryId, @Param("shopId") long shopId);

	/*
	 * 说到商品编辑:
	 *		首先肯定要根据productId查到对应Product相关的信息,既然这里是Dao层的开发，所以需要在Dao层需要开发一个 selectProductById 方法
	 *		商品信息有商品缩略图和详情图片，这里我们先约定好：如果用户传入了新的商品缩略图和详情图片，就将原有的商品缩略图和详情图片删除掉。
	 * 
	 * 商品缩略图的地址存放在tb_product的img_addr字段，所以只需要更新改表即可。 所以对应Dao层应该有个方法updateProduct
	 *		图片缩略图还涉及磁盘上的文件的删除，需要根据productId获取到Product ,然后获取Product的imgAddr属性，复用selectProductById 解渴
	 *		详情图片的地址存放在tb_product_img中，根据product_id可以查找到对应商品下的全部详情图片，所以对应Dao层应该有个方法deleteProductImgById
	 *		图片详情还涉及磁盘上的文件的删除，需要根据productId获取到List<ProductImg> ,然后遍历该list，获取集合中每个ProductImg的imgAddr地址，所以还需要有个selectProductImgList方法
	 */
	
}
