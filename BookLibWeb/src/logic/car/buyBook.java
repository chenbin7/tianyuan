package logic.car;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.function.LongToDoubleFunction;

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
@WebServlet("/buyBook")
public class buyBook extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */

    public buyBook() {
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
		System.out.println("doPost  buyBook");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String intentIds = request.getParameter("intentIds");
		String userId = request.getParameter("userId");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String addrId = request.getParameter("addrId");
        String price = request.getParameter("price");
		System.out.println("intentIds:"+intentIds+"  "+userId+"  "+addrId+"   "+price);
		if(!CheckUtil.checkParamsNotNull(6, intentIds, userId,name,phone, addrId, price)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}	
		int totalPrice = Integer.parseInt(price);
		doAddOrderBook(intentIds, userId,name,phone, addrId, totalPrice, response);
	}
	
	private void doAddOrderBook(String intentIds, String userId,String name,String phone, String addrId, int price,HttpServletResponse response) {
		System.out.println("buyBook");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "insert into orderbook(id,userid,name,phone,addrid,ordertime,totalprice) VALUES(?,?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			String orderId = UUID.getID();
			statement.setString(1, orderId);		
			statement.setString(2, userId);
			statement.setString(3, name);
			statement.setString(4, phone);
			statement.setString(5, addrId);
			statement.setLong(6, System.currentTimeMillis());
			statement.setInt(7, price);
	        int result = statement.executeUpdate();
	        System.out.println("result = "+result);
	        if(result > 0) {
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.SUCC).toString());
	        	updateIntentOrder(orderId, intentIds);
	        } else {
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_COMMON).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateIntentOrder(String orderId, String intentIds) {
		String[] ids = intentIds.split(",");
		for (int i = 0; i < ids.length; i++) {
			updateIntentBook(orderId, ids[i]);
		}
	}
	
	private void updateIntentBook(String orderId, String intentId) {
		System.out.println("updateIntentBook   "+orderId+"    "+intentId);
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "update intentbook set orderid=? where id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, orderId);		
			statement.setString(2, intentId);
	        int result = statement.executeUpdate();
	        System.out.println("result = "+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
