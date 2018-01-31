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
import javax.servlet.annotation.WebServlet;
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
 * Servlet implementation class booksByType
 */
@WebServlet("/booksByType")
public class booksByType extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public booksByType() {
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
		System.out.println("doPost  booksByType");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String typeid = request.getParameter("typeid");
		System.out.println("typeid:"+typeid);
		if(!CheckUtil.checkParamsNotNull(1, typeid)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}	
		doGetBooksByType(typeid, response);
	}
	
	private void doGetBooksByType(String typeId, HttpServletResponse response) {
		System.out.println("doGetBooksByType X");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "select * from book where typeid=?";
			PreparedStatement statement = connection.prepareStatement(sql);		
	        statement.setObject(1, typeId);
			ResultSet set = statement.executeQuery(sql);
	        System.out.println("result = "+set);
	        JSONArray jsonArray = new JSONArray();
	        while(set.next()) {
	        	JSONObject json = new JSONObject(); 
	        	json.put("id", set.getString("id"));
	        	json.put("userid", set.getString("userid"));
	        	json.put("typeid", set.getString("typeid"));
                json.put("name", set.getString("name"));
                json.put("descriptor", set.getString("descriptor"));
                json.put("price", set.getInt("price"));
                json.put("sellsum", set.getInt("sellsum"));
                json.put("storesum", set.getInt("storesum"));
                json.put("addtime", set.getLong("addtime"));
                json.put("picture", set.getString("picture"));
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
