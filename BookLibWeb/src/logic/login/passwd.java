package logic.login;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;

import common.CheckUtil;
import common.UUID;
import jdbc.JdbcUtil;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class passwd
 */

public class passwd extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public passwd() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("none");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("doPost  passwd");
		String phone = request.getParameter("telephone");
        String passwd = request.getParameter("passwd");
		System.out.println("telephone:"+phone+"   passwd:"+passwd);
		if(!CheckUtil.checkParamsNotNull(2, phone, passwd)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}		
		doLogin(phone, passwd, response);
	}
	
	private void doLogin(String phone, String passwd, HttpServletResponse response) {
		System.out.println("doLogin");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "Select * from user where phone=? and passwd=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, phone);
			statement.setString(2, passwd);		
	        ResultSet set = statement.executeQuery();
	        System.out.println("result = "+set);
	        if(set.next()) {
	        	String id = set.getString("id");
	        	JSONObject json = new JSONObject();
	        	json.put("id", id);
	        	json.put("token", UUID.getID());
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.SUCC, json).toString());
	        } else {
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_COMMON).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
