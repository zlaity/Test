$(function() {
	// 获取此店铺下商品列表的URL
	var listUrl = '/o2o/shopadmin/getproductlist?pageIndex=1&pageSize=9999';
	// 商品下架的URL
	var changeStuatusURL = '/o2o/shopadmin/changestatus';
	// 获取此店铺下的商品列表
	getList();

	function getList() {
		// 从后台获取此店铺的商品列表
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var productList = data.productList;
				var tempHtml = '';
				// 遍历每条商品信息，拼接成一行显示，列信息包括：
				// 商品名称、优先级、上|下架(含productId),编辑按钮
				// 预览(含productId)
				productList.map(function(item, index) {
					var textOp = "下架";
					var contraryStatus = 0;
					if (item.enableStatus == 0) {
						textOp = "上架";
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					// 拼接每件商品的行信息
					tempHtml += '' + '<div class="row row-product">'
							+ '<div class="col-33">'
							+ item.productName
							+ '</div>'
							+ '<div class="col-33">'
							+ item.priority
							+ '</div>'
							+ '<div class="col-33">'
							+ '<a href="#" class="edit" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">编辑</a>'
							+ '<a href="#" class="status" data-id="'
							+ item.productId
							+ '" data-status="'
							+ contraryStatus
							+ '">'
							+ textOp
							+ '</a>'
							+ '<a href="#" class="preview" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">预览</a>'
							+ '</div>'
							+ '</div>';
				});
				// 将拼接好的信息赋值进html控件中
				$('.product-wrap').html(tempHtml);
			}
		});
	}

	/**
	 * 下架操作
	 */
	function changeStatus(id, enableStatus) {
		var product = {};
		product.productId = id;
		product.enableStatus = enableStatus;
		$.confirm('确定么?', function() {
			$.ajax({
				url : changeStuatusURL,
				type : 'POST',
				data : {
					productStr : JSON.stringify(product)
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.toast(data.errMsg);
						getList();
					} else {
						$.toast(data.errMsg);
					}
				}
			});
		});
	}

	// 将class为product-wrap里面的a标签绑定上点击的事件
	$('.product-wrap')
			.on(
					'click',
					'a',
					function(e) {
						var target = $(e.currentTarget);
						if (target.hasClass('edit')) {
							// 如果有class edit则点击就进入店铺信息编辑界面,并带有productId参数
							window.location.href = '/o2o/shopadmin/productoperation?productId='
									+ e.currentTarget.dataset.id;
						} else if (target.hasClass('status')) {
							// 如果有class status则调用后台功能上|下架相关商品,并带有productId参数
							changeStatus(e.currentTarget.dataset.id,
									e.currentTarget.dataset.status);
						} else if (target.hasClass('preview')) {
							// 如果有class preview则去前台展示该商品详情页预览商品情况,并带有productId参数
							window.location.href = '/o2o/frontend/productdetail?productId='
									+ e.currentTarget.dataset.id;
						}
					});

	$('#new').click(function() {
		window.location.href = '/o2o/shopadmin/productoperation';
	});
});