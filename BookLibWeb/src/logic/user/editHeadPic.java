package logic.user;

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
import net.sf.json.JSONObject;

/**
 * Servlet implementation class register
 */

public class editHeadPic extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public editHeadPic() {
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
		System.out.println("doPost editHeadPic");
		String userid = request.getParameter("userId");
		String fileUri = request.getParameter("fileUri");
		System.out.println("userid:"+userid+"   "+fileUri);
		if(!CheckUtil.checkParamsNotNull(2, userid, fileUri)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}
		updateFileUri(userid, fileUri, response);
	}
	
	private void updateFileUri(String userid, String fileUri, HttpServletResponse response) {
		System.out.println("updateFileUri");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "update user set header=? where id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, fileUri);		
			statement.setObject(2, userid);
	        int result = statement.executeUpdate();
	        System.out.println("result = "+result);
	        if(result > 0) {
	        	JSONObject jsonObject = new JSONObject();
	        	jsonObject.put("headPicUrl", fileUri);
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.SUCC,jsonObject).toString());
	        } else {
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_COMMON).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
