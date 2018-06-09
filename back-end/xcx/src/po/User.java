package po;

public class User {
	public int userId; //学号
	public String username; //用户名
	public String password; //密码
	public boolean canmodify; //是否能修改用户名
	public boolean isadmin; //是否是管理员
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isCanmodify() {
		return canmodify;
	}
	public void setCanmodify(boolean canmodify) {
		this.canmodify = canmodify;
	}
	public boolean isIsadmin() {
		return isadmin;
	}
	public void setIsadmin(boolean isadmin) {
		this.isadmin = isadmin;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", canmodify="
				+ canmodify + ", isadmin=" + isadmin + "]";
	}
}
