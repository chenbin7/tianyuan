package logic.book;

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
 * Servlet implementation class deleteFrvaite
 */
@WebServlet("/deleteFrvaite")
public class deleteFrvaite extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public deleteFrvaite() {
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
		System.out.println("doPost deleteFrvaite");
		String fraviteId = request.getParameter("fraviteId");
		System.out.println("fraviteId:"+fraviteId);
		if(!CheckUtil.checkParamsNotNull(1, fraviteId)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}		
		doDelFravite(fraviteId, response);
	}
	
	private void doDelFravite(String fraviteId, HttpServletResponse response) {
		System.out.println("doDelFravite");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "delete from favorite where id=?";
			PreparedStatement statement = connection.prepareStatement(sql);		
			statement.setObject(1, fraviteId);
	        statement.execute();
	        response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.SUCC));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
