package logic.car;

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
@WebServlet("/deteleBookFormCar")
public class deteleBookFormCar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public deteleBookFormCar() {
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
		System.out.println("doPost deteleBookFormCar");
		String intentId = request.getParameter("intentId");
		System.out.println("intentId:"+intentId);
		if(!CheckUtil.checkParamsNotNull(1, intentId)) {
			response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.ERR_PARAM).toString());
			return;
		}		
		doDelFravite(intentId, response);
	}
	
	private void doDelFravite(String intentId, HttpServletResponse response) {
		System.out.println("doDelFravite");
		try {
			Connection connection = (Connection) JdbcUtil.getConnect();	
			String sql = "delete from intentbook where id=?";
			PreparedStatement statement = connection.prepareStatement(sql);		
			statement.setObject(1, intentId);
	        statement.execute();
	        response.getWriter().append(CheckUtil.getResponseBody(CheckUtil.SUCC));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
