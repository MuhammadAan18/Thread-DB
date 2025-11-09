package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	private static final String url = "jdbc:mysql://localhost:3306/billiardaan";
	private static final String user = "root";
	private static final String pass = "";
	
	public static Connection getConnection () {
		try{
			return DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}