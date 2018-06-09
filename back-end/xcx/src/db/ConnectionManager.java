package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class ConnectionManager {
	
	public static Connection coon;

	public Connection openConnection() {
		try {
			System.out.println("[ConnectionManager]：打开数据库连接对象……" + new Date());
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			return DriverManager.getConnection("jdbc:mysql://" + DBConfig.IP
					+ ":" + DBConfig.PORT + "/" + DBConfig.DBNAME+"?characterEncoding=utf8",
					DBConfig.ACCOUNT, DBConfig.PASSWORD);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("[ConnectionManager]：打开数据库连接对象失败……" + new Date());
			return null;
		}
	}

	public void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				System.out.println("[ConnectionManager]：关闭数据库连接对象……"
						+ new Date());
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("[ConnectionManager]：关闭数据库连接对象失败……"
						+ new Date());
			}
		}
	}
}
