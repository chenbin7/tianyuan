package logic.car;

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
@WebServlet("/updateIntention")
public class updateIntention extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public updateIntention() {
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
		System.out.println("doPost  updateIntention");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String intentId = request.getParameter("intentId");
		String countStr = request.getParameter("bookCount");
		int count = Integer.parseInt(countStr);
		doUpdateIntent(intentId, count, response);
	}
	
	private void doUpdateIntent(String intentId, int count, HttpServletResponse response) {
		System.out.println("doGetAllIntents X");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "update intentbook set count=? where id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, count);		
			statement.setString(2, intentId);
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
