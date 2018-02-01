package logic.car;

import java.io.IOException;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.mysql.jdbc.Connection;

import common.CheckUtil;
import common.UUID;
import jdbc.JdbcUtil;

/**
 * Servlet implementation class addBook
 */
@WebServlet("/addIntentBook")
public class addIntentBook extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */

    public addIntentBook() {
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
		System.out.println("doPost  addIntentBook");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String userId = request.getParameter("userId");
        String bookId = request.getParameter("bookId");
		System.out.println("userid:"+userId+"  "+bookId);
		if(!CheckUtil.checkParamsNotNull(2, userId, bookId)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}		
		doAddIntentBook(userId, bookId, response);
	}
	
	private void doAddIntentBook(String userId, String bookId, HttpServletResponse response) {
		System.out.println("doAddIntentBook");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "insert into intentbook(id,userid,bookid,count) VALUES(?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			String intentId = UUID.getID();
			statement.setString(1, intentId);		
			statement.setString(2, userId);
			statement.setObject(3, bookId);
			statement.setObject(4, 1);
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
