package dao;

import java.sql.SQLException;
import java.util.List;

import po.Class_basic;
import po.Class_more;

public interface IRegularClassDao {
	public abstract void close();
	public abstract int InsertClassBasic(Class_basic cb) throws SQLException;
	public abstract int InsertClassMore(Class_basic cb, Class_more cm) throws SQLException;
	public abstract List<Class_basic> GetAllClasses(int startpos);
	public abstract List<Class_basic> SearchClasses(String keyword, int startpos);
	public abstract Class_more GetClassMoreByID(int class_id);
	public abstract Class_basic GetClassBasicByID(int class_id);
	public double GetScoreOfClass(int class_id);
}
