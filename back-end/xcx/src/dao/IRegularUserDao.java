package dao;

import java.sql.SQLException;

import po.User;

public interface IRegularUserDao {
	public abstract int insertUser(User user);
	public abstract int modifyUserName(String username, int userid);
	public abstract User login(User user);
	public abstract int privateChangePassword(int userid, String password);
	public abstract void close();
}
