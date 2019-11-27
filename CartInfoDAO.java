package com.internousdev.pumpkin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.pumpkin.dto.CartInfoDTO;
import com.internousdev.pumpkin.util.DBConnector;

public class CartInfoDAO {

	// カート画面に現在のカート情報を表示
	public List<CartInfoDTO> getCartInfoDTOList(String userId) {

		// 宣言と初期化文
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		List<CartInfoDTO> cartInfoList = new ArrayList<CartInfoDTO>();

		String sql ="SELECT"
					+ " ci.id,"
					+ " ci.user_id,"
					+ " ci.product_id,"
					+ " ci.product_count,"
					+ " pi.price,"
					+ " pi.product_name,"
					+ " pi.product_name_kana,"
					+ " pi.image_file_path,"
					+ " pi.image_file_name,"
					+ " pi.release_date,"
					+ " pi.release_company,"
					+ " pi.status,"
					+ " (ci.product_count * pi.price) as subtotal,"
					+ " ci.regist_date,"
					+ " ci.update_date"
					+ " FROM cart_info as ci "
					+ " LEFT JOIN product_info as pi"
					+ " ON ci.product_id = pi.product_id"
					+ " WHERE ci.user_id=? "
					+ " ORDER BY update_date desc,"
					+ " regist_date desc";

		// 処理文
		// テーブルからユーザーIDに基づいたカート情報を取得
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);// 結合するテーブル
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CartInfoDTO dto = new CartInfoDTO();
				dto.setId(rs.getInt("ci.id"));
				dto.setUserId(rs.getString("ci.user_id"));
				dto.setProductId(rs.getInt("ci.product_id"));
				dto.setProductCount(rs.getInt("ci.product_count"));
				dto.setPrice(rs.getInt("pi.price"));
				dto.setProductName(rs.getString("pi.product_name"));
				dto.setProductNameKana(rs.getString("pi.product_name_kana"));
				dto.setImageFilePath(rs.getString("pi.image_file_path"));
				dto.setImageFileName(rs.getString("pi.image_file_name"));
				dto.setReleaseDate(rs.getDate("pi.release_date"));
				dto.setReleaseCompany(rs.getString("pi.release_company"));
				dto.setStatus(rs.getString("pi.status"));
				dto.setSubtotal(rs.getInt("subtotal"));
				cartInfoList.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cartInfoList;
	}

	// カート情報の選択削除
	public int CartDelete(String productId, String userId) {

		// 宣言文と初期化文
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		PreparedStatement ps;
		int result = 0;

		String sql = "DELETE"
					+ " FROM cart_info"
					+ " WHERE product_id =?"
					+ " AND user_id = ?";

		// 処理文
		// DB接続してカート情報を削除
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, productId);
			ps.setString(2, userId);
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 購入したときにカート内からユーザーIDの一致する情報をすべて削除する
	public int deleteAll(String userId) {

		// 宣言文と初期化文
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		PreparedStatement ps;
		int result = 0;
		String sql = "DELETE"
					+ " FROM cart_info"
					+ " WHERE user_id = ?";

		// 処理文
		// DB接続してカート情報を削除
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 同じ商品のデータがすでにDBに存在しない場合に新規登録する機能
	public int regist(String user_id, int product_id, int produce_count) {

		// 宣言文と初期化文
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		PreparedStatement ps;
		int count = 0;

		String sql = "INSERT INTO cart_info "
					+ "(user_id,"
					+ " product_id,"
					+ " product_count,"
					+ " regist_date,"
					+ " update_date)"
					+ " VALUES (?,?,?,now(),now())";

		// 処理文
		// DB接続してカート情報を取得
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, user_id);
			ps.setInt(2, product_id);
			ps.setInt(3, produce_count);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	// カート内の合計金額を計算する
	public long getTotalPrice(String userId) {

		// 初期化文と宣言文
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		long totalPrice = 0;
		String sql = "SELECT"
					+ " SUM(product_count * price)"
					+ " FROM cart_info"
					+ " JOIN product_info"
					+ " ON cart_info.product_id = product_info.product_id"
					+ " WHERE user_id = ? ";

		// 処理文
		// DB接続をして商品の数と値段をかけた数値を取得
		try {
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				totalPrice = resultSet.getInt("SUM(product_count * price)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return totalPrice;
	}

	// 仮ユーザーIDから正規IDへ商品の移し変え
	public int linkToUserId(String tempUserId, String userId, int productId) {

		// 宣言文と初期化文
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int count = 0;
		String sql = "update cart_info SET user_id = ?,"
					+ " update_date = now()"
					+ " WHERE user_id = ?"
					+ " AND product_id = ?";

		// 処理文
		// DB接続をして、仮ユーザーIDと紐づいた商品情報をユーザーIDに紐付ける
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, tempUserId);
			ps.setInt(3, productId);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	// カート情報の更新（同じ商品の追加をする機能）
	public int updateProductCount(String userId, int productId, int productCount) {

		// 宣言文と初期化文
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int result = 0;
		String sql = "UPDATE cart_info"
					+ " SET product_count = (product_count + ?),"
					+ " update_date = now()"
					+ " WHERE user_id=?"
					+ " AND product_id = ?";

		// 処理文
		// DB接続して、新しくカートに追加する商品の情報を取得
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, productCount);
			ps.setString(2, userId);
			ps.setInt(3, productId);
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// ユーザーIDが商品カートにあるのか確認
	public boolean isExistsCartInfo(String userId, int productId) {

		// 宣言文と初期化文
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		boolean result = false;
		String sql = "SELECT"
					+ " COUNT(id)"
					+ " FROM cart_info"
					+ " WHERE user_id = ?"
					+ " AND product_id=?";

		// 処理文
		// DBに接続して追加しようとしている商品データがすでに存在するか確認する
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				if (rs.getInt("COUNT(id)") > 0) {
					result = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
