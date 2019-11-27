package com.internousdev.pumpkin.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.pumpkin.dao.CartInfoDAO;
import com.internousdev.pumpkin.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

//カート画面で削除ボタンを押したらチェックをつけたカート内の商品を削除
public class DeleteCartAction extends ActionSupport implements SessionAware{
	private long totalPrice;
	private List<CartInfoDTO> cartInfoDTOList;
	private String[] checkList;
	private Map<String, Object> session;

	public String execute() {
		//宣言文
		String result=ERROR;
		CartInfoDAO dao = new CartInfoDAO();
		int count = 0;
		String userId = null;
		String tempLogined = String.valueOf(session.get("logined"));
        int logined = "null".equals(tempLogined)? 0 : Integer.parseInt(tempLogined);

        //処理文
        //セッションタイムアウト処理
		if(!session.containsKey("tempUserId") && !session.containsKey("userId")) {
			return "sessionTimeout";
		}
		if(logined == 1) {
			userId = session.get("userId").toString();
		}else{
			userId = String.valueOf(session.get("tempUserId"));
		}

		//チェックリストの商品をカートから削除する処理
		for(String productId:checkList) {
			count += dao.CartDelete(productId, userId);
		}
		if(count == checkList.length) {
			cartInfoDTOList = dao.getCartInfoDTOList(userId);
			totalPrice = dao.getTotalPrice(userId);
			result=SUCCESS;
		}
		return result;
	}

	public long getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public List<CartInfoDTO> getCartInfoDTOList() {
		return cartInfoDTOList;
	}

	public void setCartInfoDTOList(List<CartInfoDTO> cartInfoDTOList) {
		this.cartInfoDTOList = cartInfoDTOList;
	}

	public String[] getCheckList() {
		return checkList;
	}

	public void setCheckList(String[] checkList) {
		this.checkList = checkList;
	}

	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

}
