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
@WebServlet("/editAddress")
public class editAddress extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public editAddress() {
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
		System.out.println("doPost  editAddress");
		String addressId = request.getParameter("addressId");
        String address = request.getParameter("address");
        String communityName = request.getParameter("communityName");
        String detail = request.getParameter("detail");
        String fullAddress = request.getParameter("fullAddress");
		System.out.println("addressId:"+addressId+"   addrid:"+addressId+"  :"+address+"  "+communityName+"  "+detail+"  "+fullAddress);
		if(!CheckUtil.checkParamsNotNull(5, addressId, address, communityName, detail, fullAddress)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}		
		doEditAddress(addressId, address, communityName, detail, fullAddress, response);
	}
	
	private void doEditAddress(String addressId, String address, String communityName, String detail, String fullAddress, HttpServletResponse response) {
		System.out.println("doEditAddress");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "update addr set address=?,communityname=?,addrdetail=?,fulladdr=? where id=?";
			PreparedStatement statement = connection.prepareStatement(sql);	
			statement.setObject(1, address);
			statement.setObject(2, communityName);
			statement.setObject(3, detail);
			statement.setObject(4, fullAddress);
			statement.setObject(5, addressId);
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
