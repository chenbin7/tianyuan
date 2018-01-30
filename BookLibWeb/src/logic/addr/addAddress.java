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
import common.UUID;
import jdbc.JdbcUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class getAddressList
 */
@WebServlet("/addAddress")
public class addAddress extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addAddress() {
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
		System.out.println("doPost  addAddress");
		String userId = request.getParameter("userId");
        String address = request.getParameter("address");
        String communityName = request.getParameter("communityName");
        String detail = request.getParameter("detail");
        String fullAddress = request.getParameter("fullAddress");
		System.out.println("userid:"+userId+"   addrid:"+addressId+"  :"+address+"  "+communityName+"  "+detail+"  "+fullAddress);
		if(!CheckUtil.checkParamsNotNull(5, userId, address, communityName, detail, fullAddress)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}		
		doAddAddress(userId, address, communityName, detail, fullAddress, response);
	}
	
	private void doAddAddress(String userid, String address, String communityName, String detail, String fullAddress, HttpServletResponse response) {
		System.out.println("doAddAddress");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "insert into addr(id,userid,address, communityname, addrdetail, fulladdr) VALUES(?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			String addressId = UUID.getID();
			statement.setString(1, addressId);
			statement.setString(2, userid);		
			statement.setObject(3, address);
			statement.setObject(4, communityName);
			statement.setObject(5, detail);
			statement.setObject(6, fullAddress);
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