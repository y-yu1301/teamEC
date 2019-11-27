<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/button.css">
<link rel="stylesheet" href="./css/commonMessage.css">
<link rel="stylesheet" href="./css/header_h1.css">
<link rel="stylesheet" href="./css/tableform.css">
<link rel="stylesheet" href="./css/pumpkinimg.css">
<title>カート画面</title>
</head>
<body>
	<script src="./js/cart.js"></script>
	<jsp:include page="header.jsp" />
	<div class="contents">
		<h1>カート画面</h1>
		<s:if test="cartInfoDTOList != null && cartInfoDTOList.size()>0">
			<s:form action="DeleteCartAction">
				<table class="cart-table">
					<thead>
						<tr>
							<th>#</th>
							<th>商品名</th>
							<th>商品名ふりがな</th>
							<th>商品画像</th>
							<th>値段</th>
							<th>発売会社名</th>
							<th>発売年月日</th>
							<th>購入個数</th>
							<th>合計金額</th>
						</tr>
					</thead>
					<tbody>
						<s:iterator value="cartInfoDTOList">
							<tr>
								<td><input type="checkbox" class="checkList"
									name="checkList" value='<s:property value="productId" />'
									onchange="checkflg(this.checked);"></td>
								<td><s:property value="productName" /></td>
								<td><s:property value="productNameKana" /></td>
								<td><img
									src='<s:property value ="imageFilePath"/>/<s:property value="imageFileName"/>'>
								<td><s:property value="price" /><span>円</span></td>
								<td><s:property value="releaseCompany" /></td>
								<td><s:property value="releaseDate" /></td>
								<td><s:property value="productCount" /></td>
								<td><s:property value="subtotal" />円</td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
				<div class="cartTotalPrice">
						<p>カート合計金額:<s:property value="totalPrice" />円</p>
				</div>
				<!-- カート内の商品を削除 -->
				<div class="btn_position_cart_delete">
					<s:submit value="削除" id="checkListdelete" disabled="true"
						class="btn" />
				</div>
			</s:form>
			<!-- 決済確認画面へ遷移（ログイン時） -->
			<s:if test="#session.logined == 1">
				<div class="btn_position_cart_settlement">
					<s:form action="SettlementConfirmAction">
						<s:hidden name="Flg" value="1" />
						<s:submit value="決済" class="btn" />
					</s:form>
				</div>
			</s:if>
			<!-- ログイン画面へ遷移（未ログイン時） -->
			<s:else>
				<div class="btn_position_cart_settlement">
					<s:form action="GoLoginAction">
						<s:hidden name="cartFlag" value="1" />
						<s:submit value="決済" class="btn" />
					</s:form>
				</div>
			</s:else>
		</s:if>
		<s:else>
			<h3 class="info">カート情報がありません。</h3>
		</s:else>
	</div>
</body>
</html>