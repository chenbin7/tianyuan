package logic;

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

public class register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public register() {
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
		System.out.println("doPost register");
		String sms = request.getParameter("smsCode");
		String phone = request.getParameter("telephone");
		String passwd = request.getParameter("passwd");
		System.out.println("sms:"+sms+"  phone:"+phone);
		if(!CheckUtil.checkParamsNotNull(3, sms, phone, passwd)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}
		if(!checkSms(sms)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_COMMON).toString());
			return;
		}
		if(hasAccount(phone)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_ACCOUNT_EXIT).toString());
		} else {			
			doRegister(phone, passwd, response);
		}
	}
	
	private void doRegister(String phone, String passwd, HttpServletResponse response) {
		System.out.println("doRegister");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "insert into user(id,name,phone, passwd) VALUES(?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
	        
			statement.setString(1, UUID.getID());
			statement.setString(2, phone);		
			statement.setObject(3, phone);
			statement.setObject(4, passwd);
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

	private boolean checkSms(String sms) {
		System.out.println("checkSms:"+sms);
		if(sms.equals("1234")) {
			return true;
		}
		return false;
	}
	
	
	private boolean hasAccount(String phone) {
		Connection connection = JdbcUtil.getConnect();
		String sql = "select * from user where phone = "+phone;
		try {
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
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
