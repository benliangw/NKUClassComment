package dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sun.xml.internal.fastinfoset.algorithm.FloatEncodingAlgorithm;

import dao.IRegularClassDao;
import db.ConnectionManager;
import db.DBUtils;
import po.Class_basic;
import po.Class_more;
import po.Comment;

public class RegularClassDao implements IRegularClassDao{
	private ConnectionManager connectionManager;
	private DBUtils dbUtils;
	private Connection conn;

	public RegularClassDao() {
		super();
		this.connectionManager = new ConnectionManager();
		this.dbUtils = new DBUtils();
		this.conn = connectionManager.openConnection();
	}
	
	@Override
	public void close() {
		connectionManager.closeConnection(conn);
	}

	@Override
	public List<Class_basic> GetAllClasses(int startpos) {
		String strSQL = "select class_id, class_name, class_teacher, class_dept from nku.class_info limit ?, 10;";
		Object[] params = new Object[] { startpos };
		ResultSet resultSet = this.dbUtils.execQuery(conn, strSQL, params);
		
		List<Class_basic> ResultSet = new ArrayList<>();
		
		try {
			while(resultSet.next())
			{
				Class_basic cb = new Class_basic();
				
				int class_id = resultSet.getInt(1);
				String class_name = resultSet.getString(2);
				String class_teacher = resultSet.getString(3);
				String class_dept = resultSet.getString(4);
				
				cb.setClass_id(class_id);
				cb.setClass_name(class_name);
				cb.setClass_teacher(class_teacher);
				cb.setClass_dept(class_dept);
				
				ResultSet.add(cb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ResultSet;
	}

	@Override
	public Class_more GetClassMoreByID(int class_id) {
		String strSQL = "select score_mean, score_max, score_min, score_above90, score_fail, people_num from nku.class_detail where class_id = ?;";
		Object[] params = new Object[] { class_id };
		ResultSet resultSet = this.dbUtils.execQuery(conn, strSQL, params);
		
		Class_more cm = new Class_more();
		
		try {
			if(resultSet.next())
			{
				double score_mean = resultSet.getDouble(1);
				double score_max = resultSet.getDouble(2);
				double score_min = resultSet.getDouble(3);
				int score_above90 = resultSet.getInt(4);
				int score_fail = resultSet.getInt(5);
				int people_num = resultSet.getInt(6);
				
				cm.setClass_id(class_id);
				cm.setNum_above90(score_above90);
				cm.setNum_failed(score_fail);
				cm.setNum_total(people_num);
				cm.setScore_max(score_max);
				cm.setScore_mean(score_mean);
				cm.setScore_min(score_min);
				
				return cm;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class_basic GetClassBasicByID(int class_id) {
		String strSQL = "select class_name, class_teacher, class_dept from nku.class_info where class_id = ?;";
		Object[] params = new Object[] { class_id };
		ResultSet resultSet = this.dbUtils.execQuery(conn, strSQL, params);
		
		Class_basic cb = new Class_basic();
		
		try {
			if(resultSet.next())
			{
				String class_name = resultSet.getString(1);
				String class_teacher = resultSet.getString(2);
				String class_dept = resultSet.getString(3);
				
				cb.setClass_dept(class_dept);
				cb.setClass_id(class_id);
				cb.setClass_name(class_name);
				cb.setClass_teacher(class_teacher);
				
				return cb;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public double GetScoreOfClass(int class_id) {
		String strSQL = "select avg(comment_score) from nku.comment where comment_class_id = ? and comment_deleted = 0;";
		Object[] params = new Object[] { class_id };
		ResultSet resultSet = this.dbUtils.execQuery(conn, strSQL, params);
		
		try {
			if(resultSet.next())
			{
				double result = resultSet.getDouble(1);
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public List<Class_basic> SearchClasses(String keyword, int startpos) {
		String key_name = "%";
		String key_teacher = "%";
		
		String key_name_init;
		String key_teacher_init;
		
		if(keyword.contains(" "))
		{
			String[] allword = keyword.split(" +");
			key_name_init = allword[0];
			key_teacher_init = allword[1];
			
			for(int i = 0; i < key_name_init.length(); i++)
			{
				key_name += key_name_init.substring(i, i+1);
				key_name += "%";
			}
			
			for(int i = 0; i < key_teacher_init.length(); i++)
			{
				key_teacher += key_teacher_init.substring(i, i+1);
				key_teacher += "%";
			}
		}
		else
		{
			for(int i = 0; i < keyword.length(); i++)
			{
				key_name += keyword.substring(i, i+1);
				key_name += "%";
			}
		}
		
		boolean flag = false;
		
		String preSQL = "select class_id from nku.class_info where class_name like ? and class_teacher like ?;";
		Object[] preparams = new Object[] { key_name, key_teacher };
		ResultSet preResult = this.dbUtils.execQuery(conn, preSQL, preparams);
		try {
			if(preResult.next())
			{
				flag = true;
			}
			else
			{
				; //do nothing
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		String strSQL = "select class_id, class_name, class_teacher, class_dept from nku.class_info where class_name like ? and class_teacher like ? limit ?, 10;";
		Object[] params = new Object[] { key_name, key_teacher, startpos };
		ResultSet resultSet = this.dbUtils.execQuery(conn, strSQL, params);
		
		List<Class_basic> ResultSet = new ArrayList<>();
		
		try {
			while(resultSet.next())
			{
				Class_basic cb = new Class_basic();
				
				int class_id = resultSet.getInt(1);
				String class_name = resultSet.getString(2);
				String class_teacher = resultSet.getString(3);
				String class_dept = resultSet.getString(4);
				
				cb.setClass_id(class_id);
				cb.setClass_name(class_name);
				cb.setClass_teacher(class_teacher);
				cb.setClass_dept(class_dept);
				
				ResultSet.add(cb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		key_name = "%";
		key_teacher = "%";
		
		if(keyword.contains(" "))
		{
			String[] allword = keyword.split(" +");
			key_teacher_init = allword[0];
			key_name_init = allword[1];
			
			for(int i = 0; i < key_name_init.length(); i++)
			{
				key_name += key_name_init.substring(i, i+1);
				key_name += "%";
			}
			
			for(int i = 0; i < key_teacher_init.length(); i++)
			{
				key_teacher += key_teacher_init.substring(i, i+1);
				key_teacher += "%";
			}
		}
		else
		{
			for(int i = 0; i < keyword.length(); i++)
			{
				key_teacher += keyword.substring(i, i+1);
				key_teacher += "%";
			}
		}
		
		preSQL = "select class_id from nku.class_info where class_name like ? and class_teacher like ?;";
		preparams = new Object[] { key_name, key_teacher };
		preResult = this.dbUtils.execQuery(conn, preSQL, preparams);
		try {
			if(preResult.next())
			{
				flag = true;
			}
			else
			{
				; //do nothing
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		strSQL = "select class_id, class_name, class_teacher, class_dept from nku.class_info where class_name like ? and class_teacher like ? limit ?, 10;";
		params = new Object[] { key_name, key_teacher, startpos };
		resultSet = this.dbUtils.execQuery(conn, strSQL, params);
		
		try {
			while(resultSet.next())
			{
				Class_basic cb = new Class_basic();
				
				int class_id = resultSet.getInt(1);
				String class_name = resultSet.getString(2);
				String class_teacher = resultSet.getString(3);
				String class_dept = resultSet.getString(4);
				
				cb.setClass_id(class_id);
				cb.setClass_name(class_name);
				cb.setClass_teacher(class_teacher);
				cb.setClass_dept(class_dept);
				
				ResultSet.add(cb);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(flag == false)
		{
			System.out.println("here!");
			Class_basic cBasic = new Class_basic();
			cBasic.setClass_id(-1);
			ResultSet.add(cBasic);
			return ResultSet;
		}
		System.out.println("------");
		System.out.println(ResultSet.size());
		return ResultSet;
	}

	@Override
	public int InsertClassBasic(Class_basic cb) throws SQLException {
		String strSQL = "insert into nku.class_info(class_name, class_teacher, class_dept) values(?,?,?);";
		Object[] params = new Object[] { cb.getClass_name(), cb.getClass_teacher(), cb.getClass_dept()};
		int resultSet = this.dbUtils.execOthers(conn, strSQL, params);
		if (resultSet != -1) 
		{
			int classid = 0;
			strSQL = "select LAST_INSERT_ID()";
			ResultSet rs = this.dbUtils.execQuery(conn, strSQL);
			if (rs.next()) {
				classid = (rs.getInt(1));
			}
			else {
				return -1;
			}
			
			strSQL = "insert into class_detail(class_id) values (?)";
			params = new Object[] {classid};
			resultSet = this.dbUtils.execOthers(conn, strSQL, params);
			if(resultSet == -1)
				return -1;
			return 1;
		}
		return -1;
	}
	
	@Override
	public int InsertClassMore(Class_basic cb, Class_more cm) throws SQLException {
		String strSQL = "insert into nku.class_info(class_name, class_teacher, class_dept) values(?,?,?);";
		Object[] params = new Object[] { cb.getClass_name(), cb.getClass_teacher(), cb.getClass_dept()};
		int resultSet = this.dbUtils.execOthers(conn, strSQL, params);
		if (resultSet != -1) 
		{
			int classid = 0;
			strSQL = "select LAST_INSERT_ID()";
			ResultSet rs = this.dbUtils.execQuery(conn, strSQL);
			if (rs.next()) {
				classid = (rs.getInt(1));
			}
			else {
				return -1;
			}
			
			strSQL = "insert into class_detail(class_id, score_mean, score_max, score_min, score_above90, score_fail, people_num) values (?,?,?,?,?,?,?)";
			params = new Object[] {classid, cm.getScore_mean(), cm.getScore_max(), cm.getScore_min(), cm.getNum_above90(), cm.getNum_failed(), cm.getNum_total()};
			resultSet = this.dbUtils.execOthers(conn, strSQL, params);
			if(resultSet == -1)
				return -1;
			return 1;
		}
		return -1;
	}
}
