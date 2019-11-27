package com.internousdev.pumpkin.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.pumpkin.dao.CartInfoDAO;
import com.internousdev.pumpkin.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

//カートボタンが押されたらカート画面へ遷移
public class CartAction extends ActionSupport implements SessionAware{
	private long totalPrice;
	private List<CartInfoDTO> cartInfoDTOList;
	private Map<String, Object> session;

	public String execute() {

		//宣言と初期化文
		String userId = null;
		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		String tempLogined = String.valueOf(session.get("logined"));
        int logined = "null".equals(tempLogined)? 0 : Integer.parseInt(tempLogined);

		//処理文
        //セッションアウトエラー処理
		if(!session.containsKey("tempUserId") && !session.containsKey("userId")) {
			return "sessionTimeout";
		}
		if(logined == 1) {
			userId = session.get("userId").toString();
		} else {
			userId = String.valueOf(session.get("tempUserId"));
		}

		//カート情報の取得
		cartInfoDTOList = cartInfoDAO.getCartInfoDTOList(userId);
		//合計金額の取得
		totalPrice = cartInfoDAO.getTotalPrice(userId);

		return SUCCESS;
	}

	//getter・setter
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

	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
