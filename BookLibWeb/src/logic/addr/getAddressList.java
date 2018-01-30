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
@WebServlet("/getAddressList")
public class getAddressList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getAddressList() {
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
		System.out.println("doPost  getAddressList");
		String userid = request.getParameter("userId");
		System.out.println("userid:"+userid);
		if(!CheckUtil.checkParamsNotNull(1, userid)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}		
		doGetAddress(userid, response);
	}
	
	private void doGetAddress(String userid,HttpServletResponse response) {
		System.out.println("doGetAddress");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "select * from addr where userid = ?";
			PreparedStatement statement = connection.prepareStatement(sql);		
			statement.setObject(1, userid);
	        ResultSet set = statement.executeQuery();
	        JSONArray ja = new JSONArray();
	        while(set.next()) {
	        	JSONObject json = new JSONObject(); 
	        	json.put("addressId", set.getString("id"));
                json.put("address", set.getString("address"));
                json.put("communityName", set.getString("communityname"));
                json.put("detail", set.getString("addrdetail"));
                json.put("fullAddress", set.getString("fulladdr"));
                System.out.println("result = "+json.toString());
                ja.add(json);
	        } 
	        response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.SUCC, ja));
	        //todo
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
