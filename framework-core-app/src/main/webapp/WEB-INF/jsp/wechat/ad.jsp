<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="../commons/jsp-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>广告</title>
<%@include file="../commons/commons-head.jsp"%>
<script type="text/javascript"
	src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
<b:weixin name="shareConfig"></b:weixin>
</head>
<body>
	<span>${shareConfig.ticket }</span>
	<span>${shareConfig.signature }</span>
	<span>${shareConfig.noncestr }</span>
	<span>${shareConfig.timestamp }</span>
	<span>${shareConfig.appid }</span>
	<script type="text/javascript">
		$(function() {
			wx.config({
				debug: false,
				appId: '${shareConfig.appid }',
				timestamp: '${shareConfig.timestamp }',
				nonceStr: '${shareConfig.noncestr }',
				signature: '${shareConfig.signature }',
				jsApiList: [
					'checkJsApi',
					'onMenuShareTimeline',
					'onMenuShareAppMessage',
					'onMenuShareQQ',
					'onMenuShareWeibo',
					'hideMenuItems',
					'showMenuItems',
					'hideAllNonBaseMenuItem',
					'showAllNonBaseMenuItem',
					'hideOptionMenu',
					'showOptionMenu',
					'closeWindow'
				]
			});
			var url = 'http://www.baidu.com',
				desc = "汇聚爱心，与爱同行",
				title = "中脉慈善捐助",
				imgUrl = "http://m.szbeacon.com/o2o/wechat/templates/default/mai/img/payment_btn.png";
			wx.ready(function () {
				wx.showOptionMenu();
				wx.onMenuShareAppMessage({
					title: title,
					desc: desc,
					link: url,
					imgUrl: imgUrl,
					success: function (res) {
						alert("分享成功");
					}
				});
				wx.onMenuShareTimeline({
					title: title,
					link: url,
					imgUrl: imgUrl,
					success: function (res) {
						alert("分享成功");
					}
				});
				wx.hideMenuItems({
					menuList: ['menuItem:copyUrl', 'menuItem:share:email', 'menuItem:openWithSafari', 'menuItem:share:QZone', 'menuItem:share:qq']
				});
			});
		});
	</script>
</body>
</html>