package dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.IRegularCommentDao;
import db.ConnectionManager;
import db.DBUtils;
import po.Comment;
import po.CommentDetail;

public class RegularCommentDao implements IRegularCommentDao{
	private ConnectionManager connectionManager;
	private DBUtils dbUtils;
	private Connection conn;
	
	public RegularCommentDao() {
		super();
		this.connectionManager = new ConnectionManager();
		this.dbUtils = new DBUtils();
		this.conn = connectionManager.openConnection();
	}

	@Override
	public int insertComment(Comment comment) {
		String strSQL = "insert into nku.comment(comment_class_id, comment_user, comment_time, comment_content, comment_score, comment_annoy) values(?,?,?,?,?,?)";
		Object[] params = new Object[] { comment.getComment_class_id(), comment.getComment_user(), comment.getComment_time(), comment.getComment_content(), comment.getComment_score(), comment.isComment_annoy()};
		int resultSet = this.dbUtils.execOthers(conn, strSQL, params);
		if (resultSet != -1) 
		{
			return 1;
		}
		return -1;
	}

	@Override
	public void close() {
		connectionManager.closeConnection(conn);
	}

	@Override
	public List<CommentDetail> getCommentByUser(int userid, int startpos) throws ParseException {
		String strSQL = "select comment_id, comment_class_id, comment_time, comment_content, comment_score, comment_annoy from nku.comment where comment_user = ? and comment_deleted = 0 limit " + startpos + ", 10;";
		Object[] params = new Object[] { userid };
		ResultSet resultSet = this.dbUtils.execQuery(conn, strSQL, params);
		
		List<CommentDetail> ResultSet = new ArrayList<>();
		
		try {
			while(resultSet.next())
			{
				CommentDetail cd = new CommentDetail();
				
				int comment_id = resultSet.getInt(1);
				int comment_class_id = resultSet.getInt(2);
				String comment_time_s = resultSet.getString(3);
				String comment_content = resultSet.getString(4);
				int comment_score = resultSet.getInt(5);
				boolean isannoy = resultSet.getBoolean(6);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date comment_time = sdf.parse(comment_time_s);
				
				
				cd.setComment_id(comment_id);
				cd.setComment_time(comment_time);
				cd.setComment_content(comment_content);
				cd.setComment_class_id(comment_class_id);
				cd.setComment_deleted(false);
				cd.setComment_score(comment_score);
				cd.setComment_user(userid);
				cd.setComment_annoy(isannoy);
				
				strSQL = "select class_name, class_teacher, class_dept from nku.class_info where class_id = ? ";
				params = new Object[] { comment_class_id };
				ResultSet resultSet2 = this.dbUtils.execQuery(conn, strSQL, params);
				if(resultSet2.next())
				{
					String comment_class_name = resultSet2.getString(1);
					String comment_class_teacher = resultSet2.getString(2);
					String comment_class_dept = resultSet2.getString(3);
					
					cd.setComment_class_name(comment_class_name);
					cd.setComment_class_dept(comment_class_dept);
					cd.setComment_class_teacher(comment_class_teacher);
				}
				
				strSQL = "select username, isadmin from nku.user_info where userid = ? ";
				params = new Object[] { userid };
				resultSet2 = this.dbUtils.execQuery(conn, strSQL, params);
				if(resultSet2.next())
				{
					String username = resultSet2.getString(1);
					boolean isadmin = resultSet2.getBoolean(2);
					
					cd.setComment_user_name(username);
					cd.setUser_isadmin(isadmin);
				}
				
				ResultSet.add(cd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ResultSet;
	}
	
	@Override
	public List<CommentDetail> getCommentByClass(int classid, int startpos) throws ParseException {
		String strSQL = "select comment_id, comment_user, comment_time, comment_content, comment_score, comment_annoy from nku.comment where comment_class_id = ? and comment_deleted = 0 limit " + startpos + ", 10;";
		Object[] params = new Object[] { classid };
		ResultSet resultSet = this.dbUtils.execQuery(conn, strSQL, params);
		
		List<CommentDetail> ResultSet = new ArrayList<>();
		
		try {
			while(resultSet.next())
			{
				CommentDetail cd = new CommentDetail();
				
				int comment_id = resultSet.getInt(1);
				int comment_user = resultSet.getInt(2);
				String comment_time_s = resultSet.getString(3);
				String comment_content = resultSet.getString(4);
				int comment_score = resultSet.getInt(5);
				boolean isannoy = resultSet.getBoolean(6);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date comment_time = sdf.parse(comment_time_s);
				
				cd.setComment_id(comment_id);
				cd.setComment_time(comment_time);
				cd.setComment_content(comment_content);
				cd.setComment_class_id(classid);
				cd.setComment_deleted(false);
				cd.setComment_score(comment_score);
				cd.setComment_user(comment_user);
				cd.setComment_annoy(isannoy);
				
				strSQL = "select class_name, class_teacher, class_dept from nku.class_info where class_id = ? ";
				params = new Object[] { classid };
				ResultSet resultSet2 = this.dbUtils.execQuery(conn, strSQL, params);
				if(resultSet2.next())
				{
					String comment_class_name = resultSet2.getString(1);
					String comment_class_teacher = resultSet2.getString(2);
					String comment_class_dept = resultSet2.getString(3);
					
					cd.setComment_class_name(comment_class_name);
					cd.setComment_class_dept(comment_class_dept);
					cd.setComment_class_teacher(comment_class_teacher);
				}
				
				strSQL = "select username, isadmin from nku.user_info where userid = ? ";
				params = new Object[] { comment_user };
				resultSet2 = this.dbUtils.execQuery(conn, strSQL, params);
				if(resultSet2.next())
				{
					String username = resultSet2.getString(1);
					boolean isadmin = resultSet2.getBoolean(2);
					
					cd.setComment_user_name(username);
					cd.setUser_isadmin(isadmin);
				}
				
				ResultSet.add(cd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ResultSet;
	}

	@Override
	public CommentDetail getCommentByCommentId(int commentid) throws ParseException {
		String strSQL = "select comment_id, comment_class_id, comment_time, comment_content, comment_score, comment_annoy, comment_user from nku.comment where comment_id = ?;";
		Object[] params = new Object[] { commentid };
		ResultSet resultSet = this.dbUtils.execQuery(conn, strSQL, params);
		
		try {
			if(resultSet.next())
			{
				CommentDetail cd = new CommentDetail();
				
				int comment_id = resultSet.getInt(1);
				int comment_class_id = resultSet.getInt(2);
				String comment_time_s = resultSet.getString(3);
				String comment_content = resultSet.getString(4);
				int comment_score = resultSet.getInt(5);
				boolean isannoy = resultSet.getBoolean(6);
				int comment_userid = resultSet.getInt(7);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date comment_time = sdf.parse(comment_time_s);
				
				
				cd.setComment_id(comment_id);
				cd.setComment_time(comment_time);
				cd.setComment_content(comment_content);
				cd.setComment_class_id(comment_class_id);
				cd.setComment_deleted(false);
				cd.setComment_score(comment_score);
				cd.setComment_user(comment_userid);
				cd.setComment_annoy(isannoy);
				
				strSQL = "select class_name, class_teacher, class_dept from nku.class_info where class_id = ? ";
				params = new Object[] { comment_class_id };
				ResultSet resultSet2 = this.dbUtils.execQuery(conn, strSQL, params);
				if(resultSet2.next())
				{
					String comment_class_name = resultSet2.getString(1);
					String comment_class_teacher = resultSet2.getString(2);
					String comment_class_dept = resultSet2.getString(3);
					
					cd.setComment_class_name(comment_class_name);
					cd.setComment_class_dept(comment_class_dept);
					cd.setComment_class_teacher(comment_class_teacher);
				}
				
				strSQL = "select username, isadmin from nku.user_info where userid = ? ";
				params = new Object[] { comment_userid };
				resultSet2 = this.dbUtils.execQuery(conn, strSQL, params);
				if(resultSet2.next())
				{
					String username = resultSet2.getString(1);
					boolean isadmin = resultSet2.getBoolean(2);
					
					cd.setComment_user_name(username);
					cd.setUser_isadmin(isadmin);
				}
				
				return cd;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateComment(Comment comment) {
		String strSQL = "update nku.comment set comment_time=?, comment_content=?, comment_score=?, comment_annoy=? where comment_id=?;";
		Object[] params = new Object[] { comment.getComment_time(), comment.getComment_content(), comment.getComment_score(), comment.isComment_annoy(), comment.getComment_id() };
		int resultSet = this.dbUtils.execOthers(conn, strSQL, params);
		if(resultSet != -1)
			return 1;
		return -1;
	}

	@Override
	public int deleteComment(int commentid) {
		String strSQL = "update nku.comment set comment_deleted=true where comment_id=?;";
		Object[] params = new Object[] { commentid };
		int resultSet = this.dbUtils.execOthers(conn, strSQL, params);
		if(resultSet != -1)
			return 1;
		return -1;
	}

	@Override
	public CommentDetail getCommentByClassUser(int userid, int classid) throws ParseException {
		String strSQL = "select comment_id, comment_user, comment_time, comment_content, comment_score, comment_annoy from nku.comment where comment_class_id = ? and comment_user = ? and comment_deleted = 0;";
		Object[] params = new Object[] { classid, userid };
		ResultSet resultSet = this.dbUtils.execQuery(conn, strSQL, params);
		
		try {
			if(resultSet.next())
			{
				CommentDetail cd = new CommentDetail();
				
				int comment_id = resultSet.getInt(1);
				int comment_user = resultSet.getInt(2);
				String comment_time_s = resultSet.getString(3);
				String comment_content = resultSet.getString(4);
				int comment_score = resultSet.getInt(5);
				boolean isannoy = resultSet.getBoolean(6);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date comment_time = sdf.parse(comment_time_s);
				
				cd.setComment_id(comment_id);
				cd.setComment_time(comment_time);
				cd.setComment_content(comment_content);
				cd.setComment_class_id(classid);
				cd.setComment_deleted(false);
				cd.setComment_score(comment_score);
				cd.setComment_user(comment_user);
				cd.setComment_annoy(isannoy);
				
				strSQL = "select class_name, class_teacher, class_dept from nku.class_info where class_id = ? ";
				params = new Object[] { classid };
				ResultSet resultSet2 = this.dbUtils.execQuery(conn, strSQL, params);
				if(resultSet2.next())
				{
					String comment_class_name = resultSet2.getString(1);
					String comment_class_teacher = resultSet2.getString(2);
					String comment_class_dept = resultSet2.getString(3);
					
					cd.setComment_class_name(comment_class_name);
					cd.setComment_class_dept(comment_class_dept);
					cd.setComment_class_teacher(comment_class_teacher);
				}
				
				strSQL = "select username, isadmin from nku.user_info where userid = ? ";
				params = new Object[] { comment_user };
				resultSet2 = this.dbUtils.execQuery(conn, strSQL, params);
				if(resultSet2.next())
				{
					String username = resultSet2.getString(1);
					boolean isadmin = resultSet2.getBoolean(2);
					
					cd.setComment_user_name(username);
					cd.setUser_isadmin(isadmin);
				}
				
				return cd;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
