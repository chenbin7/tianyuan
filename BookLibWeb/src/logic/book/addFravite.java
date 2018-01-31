package logic.book;

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

public class addFravite extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addFravite() {
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
		System.out.println("doPost addFravite");
		String userId = request.getParameter("userId");
		String bookId = request.getParameter("bookId");
		System.out.println("userId:"+userId+"  bookId:"+bookId);
		if(!CheckUtil.checkParamsNotNull(2, userId, bookId)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}
					
		doAddFavrite(userId, bookId, response);
		
	}
	
	private void doAddFavrite(String userId, String bookId, HttpServletResponse response) {
		System.out.println("doAddFavrite");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "insert into favorite(id,userid,bookid) VALUES(?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
	        
			statement.setString(1, UUID.getID());
			statement.setString(2, userId);		
			statement.setObject(3, bookId);
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


}
