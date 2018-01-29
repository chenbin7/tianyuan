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
		System.out.println("doPost");
		String userid = request.getParameter("userId");
		System.out.println("userid:"+userid);
		if(!CheckUtil.checkParamsNotNull(1, userid)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}
		if(!hasAccount(userid)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_USER_NO_REG).toString());
		} else {			
			doGetInfo(userid, response);
		}
	}
	
	private void doGetInfo(String userid,HttpServletResponse response) {
		System.out.println("doRegister");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "select * from user where id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);		
			statement.setObject(1, userid);
	        ResultSet set = statement.executeQuery();
	        if(set.next()) {
	        	JSONObject json = new JSONObject(); 
	        	json.put("userId", userid);
                json.put("userName", set.getString("name"));
                json.put("userHeadPic", set.getString("picture"));
                json.put("telephone", set.getString("phone"));
                System.out.println("result = "+json);
                response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.SUCC, json));
	        } else {
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_COMMON));
			}
	        //todo
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private boolean hasAccount(String userid) {
		Connection connection = JdbcUtil.getConnect();
		String sql = "select * from user where id = "+userid;
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
