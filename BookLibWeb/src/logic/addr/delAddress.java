package logic.addr;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;

import common.CheckUtil;
import jdbc.JdbcUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class getAddressList
 */
@WebServlet("/delAddress")
public class delAddress extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public delAddress() {
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
		System.out.println("doPost delAddress");
		String userid = request.getParameter("userId");
		String addrid = request.getParameter("addressId");
		System.out.println("userid:"+userid+"   addrid:"+addrid);
		if(!CheckUtil.checkParamsNotNull(2, userid, addrid)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}		
		doDelAddress(userid, addrid, response);
	}
	
	private void doDelAddress(String userid,String addrid, HttpServletResponse response) {
		System.out.println("doDelAddress");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "delete from addr where userid=? and id=?";
			PreparedStatement statement = connection.prepareStatement(sql);		
			statement.setObject(1, userid);
			statement.setObject(2, addrid);
	        boolean result = statement.execute();
	        if(result) {
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.SUCC));
	        } else {
				response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_COMMON));
			}
	        //todo
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
