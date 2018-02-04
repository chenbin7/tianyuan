package logic.user;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;

import common.CheckUtil;
import common.UUID;
import jdbc.JdbcUtil;

/**
 * Servlet implementation class register
 */

public class modifyInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public modifyInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("doGet");
		response.getWriter().append("none");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("doPost  modifyInfo");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String userid = request.getParameter("userId");
		String phone = request.getParameter("phone");
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		System.out.println("userid:"+userid+"  phone:"+phone+"  "+name+"  "+sex);
		if(!CheckUtil.checkParamsNotNull(4, userid, phone, name, sex)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}
		if(!hasAccount(userid)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_USER_NO_REG).toString());
		} else {			
			doModify(userid, phone, name, sex, response);
		}
	}
	
	private void doModify(String userid, String phone, String name, String sex, HttpServletResponse response) {
		System.out.println("doModify");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "update user set name=?, phone=?, sex=? where id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);		
			statement.setObject(2, phone);
			statement.setObject(3, sex);
			statement.setObject(4, userid);
	        int result = statement.executeUpdate();
	        System.out.println("result = "+result);
	        if(result > 0) {
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.SUCC).toString());
	        } else {
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_COMMON).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean hasAccount(String userid) {
		Connection connection = JdbcUtil.getConnect();
		String sql = "select * from user where id=?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, userid);		
			ResultSet set = statement.executeQuery();
			if(set != null && set.next()) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

}
