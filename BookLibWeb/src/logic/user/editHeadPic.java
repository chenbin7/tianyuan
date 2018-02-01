package logic.user;

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
import net.sf.json.JSONObject;

/**
 * Servlet implementation class register
 */
@WebServlet("/editHeadPic")
@MultipartConfig(location = "C://BookLibWeb/header")
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
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		String dir = request.getServletContext().getRealPath("/");
		String filepath = "header/header_"+UUID.getID()+".jpg";
		String path = dir+filepath;
		Part part = request.getPart("photo");
		part.write(path);
		String userid = request.getParameter("userId");
		System.out.println("userid:"+userid+"   "+path);
		if(!CheckUtil.checkParamsNotNull(2, userid, filepath)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}
		String url = request.getLocalAddr()+":"+request.getLocalPort()+"//BookLibWeb/";
		System.out.println(url);
		updateFileUri(userid, filepath, url, response);
		
	}
	
	private void updateFileUri(String userid, String path, String url, HttpServletResponse response) {
		System.out.println("updateFileUri");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "update user set header=? where id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, path);		
			statement.setObject(2, userid);
	        int result = statement.executeUpdate();
	        System.out.println("result = "+result);
	        if(result > 0) {
	        	String headerUrl = url+path;
	        	System.out.println("headerUrl = "+headerUrl);
	        	JSONObject jsonObject = new JSONObject();
	        	jsonObject.put("headPicUrl", headerUrl);
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.SUCC,jsonObject).toString());
	        } else {
	        	response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_COMMON).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
