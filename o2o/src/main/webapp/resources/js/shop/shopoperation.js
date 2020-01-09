/*
 * 
 * 
 */
//返回区域 店铺类别相关信息
$(function() {
	var initUrl = '/o2o/shopadmin/getshopinitinfo';
	var registerShopUrl = '/o2o/shopadmin/registershop';
	// 调试信息 表示加载在执行
	alert(initUrl);
	// 获取需要的信息
	getShopInitInfo();
	function getShopInitInfo() {
		$.getJSON(initUrl, function(data) {
			if (data.success) {
				var tempHtml = '';
				var tempAreaHtml = '';
				data.shopCategoryList.map(function(item, index) {
					tempHtml += '<option data-id="' + item.shopCategoryId
							+ '">' + item.shopCategoryName + '</option>';
				});
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
				});
				// 获取html中对应标签的id 赋值
				$('#shop-category').html(tempHtml);
				// $('#shop-category').removeAttr('disabled');
				$('#area').html(tempAreaHtml);
			}
		});

		$('#submit').click(
				function() {
					var shop = {};
					// 获取页面的值
					shop.shopName = $('#shop-name').val();
					shop.shopAddr = $('#shop-addr').val();
					shop.phone = $('#shop-phone').val();
					shop.shopDesc = $('#shop-desc').val();

					shop.shopCategory = {
						shopCategoryId : $('#shop-category').find('option')
								.not(function() {
									return !this.selected;
								}).data('id')
					};
					shop.area = {
						areaId : $('#area').find('option').not(function() {
							return !this.selected;
						}).data('id')
					};
					// 图片
					var shopImg = $("#shop-img")[0].files[0];
					// 接收数据
					var formData = new FormData();
					// 和后端约定好，利用shopImg和shopStr接收图片信息和shop信息
					formData.append('shopImg', shopImg);
					// 转成json格式，后端接收到后将json转为实体类
					formData.append('shopStr', JSON.stringify(shop));

					var verifyCodeActual = $('#j_kaptcha').val();
					if (!verifyCodeActual) {
						$.toast('请输入验证码！');
						return;
					}

					// 将数据封装到formData发送到后台
					formData.append("verifyCodeActual", verifyCodeActual);
					$.ajax({
						url : registerShopUrl,
						type : 'POST',
						// contentType: "application/x-www-form-urlencoded;
						// charset=utf-8",
						data : formData,
						contentType : false,
						processData : false,
						cache : false,
						success : function(data) {
							if (data.success) {
								if (data.success) {
									$.toast('提示信息:' + data.errMsg);
								} else {
									$.toast('提示信息:' + data.errMsg);
								}
								// 点击提交后 不管成功失败都更换验证码，防止重复提交
								$('#kaptcha_img').click();
							}
						}
					});
				});

	}

});
