package logic.car;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.Locale;
import java.util.function.LongToDoubleFunction;

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
@WebServlet("/listIntents")
public class listIntents extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public listIntents() {
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
		System.out.println("doPost  listIntents");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String userid = request.getParameter("userid");
		System.out.println("userid:"+userid);
		String url = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+"//BookLibWeb/";
		System.out.println(url);
		doGetAllIntents(userid, url, response);
	}
	
	private void doGetAllIntents(String userId, String url, HttpServletResponse response) {
		System.out.println("doGetAllIntents X");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "select book.id, book.userid, book.name as bname, book.descriptor, book.price, book.sellsum, book.storesum, book.addtime, book.picture, intentbook.id as intentid, intentbook.count, intentbook.orderid, type.name as tname from book, intentbook, type where book.typeid = type.id and book.id in (select bookid from intentbook where userid=?)";
			PreparedStatement statement = connection.prepareStatement(sql);		
			statement.setString(1, userId);
	        ResultSet set = statement.executeQuery();
	        System.out.println("result X= "+set);
	        JSONArray jsonArray = new JSONArray();
	        while(set.next()) {
	        	try {
					String orderId = set.getString("orderid");
					if(orderId != null && orderId.length() > 0) {
						System.out.println("orderid:"+orderId);
						continue;
					}
	        	} catch (Exception e) {
					// TODO: handle exception
				}
	        	JSONObject json = new JSONObject(); 
	        	json.put("id", set.getString("id"));
	        	json.put("userid", set.getString("userid"));
	        	json.put("type", set.getString("tname"));
                json.put("name", set.getString("bname"));
                json.put("descriptor", set.getString("descriptor"));
                json.put("price", set.getInt("price"));
                json.put("sellsum", set.getInt("sellsum"));
                json.put("storesum", set.getInt("storesum"));
                json.put("addtime", set.getLong("addtime"));
                json.put("picture", url+set.getString("picture"));
                json.put("intentId", set.getString("intentid"));
                json.put("count", set.getInt("count"));
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
