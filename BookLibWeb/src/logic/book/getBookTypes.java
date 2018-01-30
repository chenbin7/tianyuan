package logic.book;

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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class register
 */

public class getBookTypes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getBookTypes() {
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
		System.out.println("doPost  getBookTypes");
		String userid = request.getParameter("userId");
		System.out.println("userid:"+userid);
		doGetTypes(response);
	}
	
	private void doGetTypes(HttpServletResponse response) {
		System.out.println("doGetTypes X");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "select * from type";
			Statement statement = connection.prepareStatement(sql);		
	        ResultSet set = statement.executeQuery(sql);
	        System.out.println("result = "+set);
	        JSONArray jsonArray = new JSONArray();
	        while(set.next()) {
	        	JSONObject json = new JSONObject(); 
	        	json.put("id", set.getString("id"));
                json.put("name", set.getString("name"));
               jsonArray.add(json);
                System.out.println("result = "+json.toString());
	        } 
	        if(jsonArray.isEmpty()) {
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_COMMON));
	        } else {				
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.SUCC, jsonArray));
			}
	        //todo
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
