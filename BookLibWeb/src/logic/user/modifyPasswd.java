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

public class modifyPasswd extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public modifyPasswd() {
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
		System.out.println("doPost  modifyPasswd");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String userid = request.getParameter("userId");
		String oldPasswd = request.getParameter("oldPasswd");
		String passwd = request.getParameter("newPasswd");
		System.out.println("userid:"+userid+"  oldPasswd:"+oldPasswd);
		if(!CheckUtil.checkParamsNotNull(3, userid, oldPasswd, passwd)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}
		if(!hasAccount(userid)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_USER_NO_REG).toString());
		} else {			
			doModify(userid, oldPasswd, passwd, response);
		}
	}
	
	private void doModify(String userid, String oldpasswd , String passwd, HttpServletResponse response) {
		System.out.println("doModify");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "update user set passwd=? where id=? and passwd=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, passwd);		
			statement.setObject(2, userid);
			statement.setObject(3, oldpasswd);
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
