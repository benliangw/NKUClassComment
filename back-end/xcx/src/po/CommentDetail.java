package po;

import java.util.Date;

public class CommentDetail {
	public int comment_id;
	public int comment_class_id;
	public String comment_class_name;
	public String comment_class_dept;
	public String comment_class_teacher;
	public int comment_user;
	public String comment_user_name;
	public boolean user_isadmin;
	public Date comment_time;
	public String comment_content;
	public int comment_score;
	public boolean comment_deleted;
	public boolean comment_annoy;
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public int getComment_class_id() {
		return comment_class_id;
	}
	public void setComment_class_id(int comment_class_id) {
		this.comment_class_id = comment_class_id;
	}
	public String getComment_class_name() {
		return comment_class_name;
	}
	public void setComment_class_name(String comment_class_name) {
		this.comment_class_name = comment_class_name;
	}
	public String getComment_class_dept() {
		return comment_class_dept;
	}
	public void setComment_class_dept(String comment_class_dept) {
		this.comment_class_dept = comment_class_dept;
	}
	public String getComment_class_teacher() {
		return comment_class_teacher;
	}
	public void setComment_class_teacher(String comment_class_teacher) {
		this.comment_class_teacher = comment_class_teacher;
	}
	public int getComment_user() {
		return comment_user;
	}
	public void setComment_user(int comment_user) {
		this.comment_user = comment_user;
	}
	public Date getComment_time() {
		return comment_time;
	}
	public void setComment_time(Date comment_time) {
		this.comment_time = comment_time;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public int getComment_score() {
		return comment_score;
	}
	public void setComment_score(int comment_score) {
		this.comment_score = comment_score;
	}
	public boolean isComment_deleted() {
		return comment_deleted;
	}
	public void setComment_deleted(boolean comment_deleted) {
		this.comment_deleted = comment_deleted;
	}
	public boolean isComment_annoy() {
		return comment_annoy;
	}
	public void setComment_annoy(boolean comment_annoy) {
		this.comment_annoy = comment_annoy;
	}
	public String getComment_user_name() {
		return comment_user_name;
	}
	public void setComment_user_name(String comment_user_name) {
		this.comment_user_name = comment_user_name;
	}
	public boolean isUser_isadmin() {
		return user_isadmin;
	}
	public void setUser_isadmin(boolean user_isadmin) {
		this.user_isadmin = user_isadmin;
	}
}
