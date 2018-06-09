package dao;

import java.text.ParseException;
import java.util.List;

import po.Comment;
import po.CommentDetail;
import po.User;

public interface IRegularCommentDao {
	public abstract int insertComment(Comment comment);
	public abstract List<CommentDetail> getCommentByUser(int userid, int startpos) throws ParseException;
	public abstract List<CommentDetail> getCommentByClass(int classid, int startpos) throws ParseException;
	public abstract CommentDetail getCommentByCommentId(int commentid) throws ParseException;
	public abstract CommentDetail getCommentByClassUser(int userid, int classid) throws ParseException;
	public abstract int updateComment(Comment comment);
	public abstract int deleteComment(int commentid);
	public abstract void close();
}
