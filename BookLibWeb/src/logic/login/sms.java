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
import common.Const;
import common.UUID;
import jdbc.JdbcUtil;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class sms
 */
public class sms extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public sms() {
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
		System.out.println("doPost  sms");
		String phone = request.getParameter("telephone");
        String sms = request.getParameter("smsCode");
		System.out.println("telephone:"+phone+"   passwd:"+sms);
		if(!CheckUtil.checkParamsNotNull(2, phone, sms)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}		
		doLogin(phone, sms, response);
		Const.init(request);
	}
	
	private void doLogin(String phone, String sms, HttpServletResponse response) {
		System.out.println("doLogin");
		try {
			if(!sms.equals("1234")) {
				response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_COMMON).toString());
				return;
			}
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "Select * from user where phone=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, phone);	
	        ResultSet set = statement.executeQuery();
	        System.out.println("result = "+set);
	        if(set.next()) {
	        	String id = set.getString("id");
	        	JSONObject json = new JSONObject();
	        	json.put("id", id);
	        	json.put("token", UUID.getID());
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.SUCC,json).toString());
	        } else {
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_COMMON).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
