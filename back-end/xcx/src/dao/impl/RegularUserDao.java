package dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.IRegularUserDao;
import db.ConnectionManager;
import db.DBUtils;
import po.User;

public class RegularUserDao implements IRegularUserDao {
	private ConnectionManager connectionManager;
	private DBUtils dbUtils;
	private Connection conn;
	
	public RegularUserDao() {
		super();
		this.connectionManager = new ConnectionManager();
		this.dbUtils = new DBUtils();
		this.conn = connectionManager.openConnection();
	}
	
	@Override
	public int insertUser(User user) {
		String strSQL = "insert into nku.user_info(userid, username, password, canmodify) values(?,?,?,1)";
		Object[] params = new Object[] { user.getUserId(), user.getUsername(), user.getPassword()};
		int resultSet = this.dbUtils.execOthers(conn, strSQL, params);
		if (resultSet != -1) 
		{
			return 1;
		}
		return -1;
	}

	@Override
	public User login(User u) {
		String strSQL = "select userid, username, password, canmodify, isadmin FROM nku.user_info where userid=?";
		Object[] params = new Object[] {u.getUserId()};
		ResultSet resultSet = this.dbUtils.execQuery(conn, strSQL, params);
		try {
			if(resultSet.next())
			{
				User user = new User();
				user.setUserId(resultSet.getInt(1));
				user.setUsername(resultSet.getString(2));
				user.setPassword(resultSet.getString(3));
				user.setCanmodify(resultSet.getBoolean(4));
				user.setIsadmin(resultSet.getBoolean(5));
				if(u.getPassword().compareTo(user.getPassword()) == 0)
				{
					return user;
				}
				else
				{
					return null;
				}
			}
			else
			{
				User user = new User();
				user.setUserId(-1);
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void close() {
		connectionManager.closeConnection(conn);
	}

	@Override
	public int modifyUserName(String username, int userid) {
		String strSQL = "select canmodify from nku.user_info where userid=?;";
		Object[] params = new Object[] {userid};
		ResultSet resultSet = this.dbUtils.execQuery(conn, strSQL, params);
		try {
			if(resultSet.next())
			{
				boolean canmodify = resultSet.getBoolean(1);
				if(!canmodify)
				{
					return -2;
				}
				strSQL = "update nku.user_info set username=?, canmodify=false where userid=?;";
				params = new Object[] { username, userid };
				int res = this.dbUtils.execOthers(conn, strSQL, params);
				if(res != -1)
					return 1;
				return -1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -3;
	}

	@Override
	public int privateChangePassword(int userid, String password) {
		String strSQL = "update nku.user_info set password=? where userid=?;";
		Object[] params = new Object[] { password, userid };
		int resultSet = this.dbUtils.execOthers(conn, strSQL, params);
		if(resultSet != -1)
			return 1;
		return -1;
	}
}
