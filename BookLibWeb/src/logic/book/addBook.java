package logic.book;

import java.io.IOException;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;

import common.CheckUtil;
import common.UUID;
import jdbc.JdbcUtil;

/**
 * Servlet implementation class addBook
 */
@WebServlet("/addBook")
public class addBook extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addBook() {
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
		System.out.println("doPost  addBook");
		String userId = request.getParameter("userId");
        String type = request.getParameter("bookType");
        String name = request.getParameter("bookName");
        String priceStr = request.getParameter("bookPrice");
        String sellSum = request.getParameter("sellSum");
        String desc = request.getParameter("bookDesc");
        String uri = request.getParameter("bookUri");
		System.out.println("userid:"+userId+"  "+type+"  :"+name+"  "+priceStr+"  "+desc+"  "+uri);
		if(!CheckUtil.checkParamsNotNull(6, userId, type, name, priceStr, desc, uri)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}		
		int price = Integer.parseInt(priceStr);
		int sell = Integer.parseInt(sellSum);
		doAddAddress(userId, type, name, price, sell, desc, uri,response);
	}
	
	private void doAddAddress(String userId, String type, String name, int price, int sell, String desc, String uri, HttpServletResponse response) {
		System.out.println("doAddBook");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "insert into book(id,userid,typeid,name,descriptor,price,sellsum,storesum,addtime,picture) VALUES(?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			String bookId = UUID.getID();
			statement.setString(1, bookId);
			statement.setString(2, userId);		
			statement.setObject(3, type);
			statement.setObject(4, name);
			statement.setObject(5, desc);
			statement.setObject(6, price);
			statement.setObject(7, sell);
			statement.setObject(8, 0);
			statement.setObject(9, System.currentTimeMillis());
			statement.setObject(10, uri);
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
