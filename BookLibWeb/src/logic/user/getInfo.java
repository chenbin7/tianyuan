package logic.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;

import common.CheckUtil;
import common.UUID;
import jdbc.JdbcUtil;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class register
 */

public class getInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("doGet");
		response.getWriter().append("none");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("doPost  getInfo");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String userid = request.getParameter("userId");
		System.out.println("userid:" + userid);
		if (!CheckUtil.checkParamsNotNull(1, userid)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}
		String url = "http://" + request.getLocalAddr() + ":" + request.getLocalPort() + "//BookLibWeb/";
		System.out.println(url);
		doGetInfo(userid, url, response);
	}

	private void doGetInfo(String userid, String url, HttpServletResponse response) {
		System.out.println("doGetInfo");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();
			String sql = "select * from user where id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setObject(1, userid);
			ResultSet set = statement.executeQuery();
			System.out.println("result = " + set);
			if (set.next()) {
				JSONObject json = new JSONObject();
				json.put("userId", userid);
				json.put("userName", set.getString("name"));
				try {
					String head = set.getString("header");
					if (head != null && head.length() > 0) {
						String header = url + head;
						json.put("userHeadPic", header);
					}
				} catch (Exception e) {
					System.out.println("no picture");
				}
				json.put("sex", set.getString("sex"));
				json.put("telephone", set.getString("phone"));
				System.out.println("result = " + json);
				response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.SUCC, json));
			} else {
				response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_COMMON));
			}
			// todo
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
