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
 * Servlet implementation class getAllComments
 */
@WebServlet("/getAllComments")
public class getAllComments extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getAllComments() {
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
		System.out.println("doPost  getAllComments");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String bookId = request.getParameter("bookId");
		System.out.println("bookId:"+bookId);
		if(!CheckUtil.checkParamsNotNull(1, bookId)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}		
		doGetAllComments(bookId, response);
	}
	
	private void doGetAllComments(String bookId, HttpServletResponse response) {
		System.out.println("getAllComments X");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "select user.name, comment.userid, comment.writetime, comment.comment from user, comment where comment.bookid=? and comment.userid=user.id";
			Statement statement = connection.prepareStatement(sql);		
	        ResultSet set = statement.executeQuery(sql);
	        System.out.println("result = "+set);
	        JSONArray jsonArray = new JSONArray();
	        while(set.next()) {
	        	JSONObject json = new JSONObject(); 
	        	json.put("time", set.getLong("writetime"));
	        	json.put("userid", set.getString("userid"));
	        	json.put("username", set.getString("name"));
                json.put("message", set.getString("comment"));
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
