package jdbc;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import com.mysql.jdbc.Connection;

public class jdbcdemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			String url = "jdbc:mysql://localhost:3306/booklib";
			Connection connection = (Connection) DriverManager.getConnection(url,"root","root");
			Statement statement = connection.createStatement();
			String sql1 = "insert into type(id,name) VALUES('"+UUID.randomUUID().toString()+"','历史')";
			String sql2 = "insert into type(id,name) VALUES('"+UUID.randomUUID().toString()+"','IT')";
			String sql3 = "insert into type(id,name) VALUES('"+UUID.randomUUID().toString()+"','经管')";
			String sql4 = "insert into type(id,name) VALUES('"+UUID.randomUUID().toString()+"','语言')";
			String sql5 = "insert into type(id,name) VALUES('"+UUID.randomUUID().toString()+"','物理')";
			String sql6 = "insert into type(id,name) VALUES('"+UUID.randomUUID().toString()+"','哲学')";
			String sql7 = "insert into type(id,name) VALUES('"+UUID.randomUUID().toString()+"','思政')";
			String sql8 = "insert into type(id,name) VALUES('"+UUID.randomUUID().toString()+"','小说')";
			String sql9 = "insert into type(id,name) VALUES('"+UUID.randomUUID().toString()+"','艺术')";
			statement.executeUpdate(sql1);
			statement.executeUpdate(sql2);
			statement.executeUpdate(sql3);
			statement.executeUpdate(sql4);
			statement.executeUpdate(sql5);
			statement.executeUpdate(sql6);
			statement.executeUpdate(sql7);
			statement.executeUpdate(sql8);
			statement.executeUpdate(sql9);
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
