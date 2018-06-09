package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public int execOthers(final Connection conn, final String strSQL, final Object...params){
		try {
			System.out.println("[DBUtils]: SQL> " + strSQL);
			for (int i = 0; i < params.length; i++) {
				System.out.println("[DBUtils]: params> " + params[i]+"\t");
			}
			this.preparedStatement = conn.prepareStatement(strSQL);
			for (int i = 0; i < params.length; i++) {
				preparedStatement.setObject(i+1, params[i]);
			}
			int affectedRows = preparedStatement.executeUpdate();
			return affectedRows;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}	
		
	}
	
	public ResultSet execQuery(final Connection conn, final String strSQL, final Object...params){
		try {
			System.out.println("[DBUtils]: SQL> " + strSQL);
			for (int i = 0; i < params.length; i++) {
				System.out.println("[DBUtils]: params> " + params[i]+"\t");
			}
			this.preparedStatement = conn.prepareStatement(strSQL);
			for (int i = 0; i < params.length; i++) {
				preparedStatement.setObject(i+1, params[i]);
			}
			this.resultSet = this.preparedStatement.executeQuery();
			return this.resultSet;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
		
	}
	
	public void execViewHandle(final Connection conn, final String strSQL, final Object...params){
		try {
			System.out.println("[DBUtils]: SQL> " + strSQL);
			for (int i = 0; i < params.length; i++) {
				System.out.println("[DBUtils]: params> " + params[i]+"\t");
			}
			this.preparedStatement = conn.prepareStatement(strSQL);
			for (int i = 0; i < params.length; i++) {
				this.preparedStatement.setObject(i+1, params[i]);
			}
			this.preparedStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
}
