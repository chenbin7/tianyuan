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
 * Servlet implementation class getAllBooks
 */
@WebServlet("/getAllBooks")
public class getAllBooks extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getAllBooks() {
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
		System.out.println("doPost  getAllBooks");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String url = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+"//BookLibWeb/";
		System.out.println(url);
		doGetAllBooks(url, response);
	}
	
	private void doGetAllBooks(String url, HttpServletResponse response) {
		System.out.println("doGetAllBooks X");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "select * from book";
			Statement statement = connection.prepareStatement(sql);		
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
                json.put("picture", url+set.getString("picture"));
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