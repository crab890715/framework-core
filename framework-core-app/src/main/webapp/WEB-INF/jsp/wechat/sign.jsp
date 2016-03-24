<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="../commons/jsp-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport"
	content="width=device-width,user-scalable=no, initial-scale=1">
<meta content="telephone=no,email=no" name="format-detection">
<title>摇一摇签到</title>
<%@include file="../commons/commons-head.jsp"%>
<script language="javascript" type="text/javascript"
	src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
<style type="text/css">
#bg {
	margin: 0px;
	padding: 0px;
	height: 100%;
	width: 100%;
	position: absolute;
	top: 0px;
	left: 0px;
	bottom: 0px;
	right: 0px;
}
</style>
</head>
<body>
	<div id="bg"></div>
	<div class="weui_dialog_alert" id="dialog2">
		<div class="weui_mask"></div>
		<div class="weui_dialog">
			<div class="weui_dialog_hd">
				<strong class="weui_dialog_title">签到</strong>
			</div>
			<div class="weui_dialog_bd">
				<div class="weui_cells weui_cells_form">
					<div class="weui_cell">
						<div class="weui_cell_hd">
							<label class="weui_label">姓名</label>
						</div>
						<div class="weui_cell_bd weui_cell_primary">
							<input class="weui_input" type="text" placeholder="请输入姓名" />
						</div>
					</div>

					<div class="weui_cell">
						<div class="weui_cell_hd">
							<label class="weui_label">手机号</label>
						</div>
						<div class="weui_cell_bd weui_cell_primary">
							<input class="weui_input" type="tel" placeholder="请输入手机号码" />
						</div>
					</div>
				</div>
			</div>
			<div class="weui_dialog_ft">
				<a href="javascript:;" class="weui_btn_dialog primary">确定</a>
			</div>
		</div>
	</div>
</body>
</html>