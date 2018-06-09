package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.IRegularCommentDao;
import dao.IRegularUserDao;
import dao.impl.RegularCommentDao;
import dao.impl.RegularUserDao;
import net.sf.json.JSONObject;
import po.Comment;
import po.User;

/**
 * Servlet implementation class AddCommentServlet
 */
@WebServlet("/AddCommentServlet")
public class AddCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCommentServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		StringBuilder sBuilder = new StringBuilder();
		BufferedReader bReader = request.getReader();
		try {
			String line;
			while((line = bReader.readLine()) != null)
			{
				sBuilder.append(line).append('\n');
			}
		} finally {
			bReader.close();
		}
		
		String classid;
		String userid;
		String comment_content;
		String comment_score;
		String comment_annoy;
		
		String requestHeader = request.getHeader("user-agent");
		
		if(sBuilder != null && !sBuilder.toString().equals(""))
		{
			JSONObject jsonObject = JSONObject.fromObject(sBuilder.toString());
			
			classid = jsonObject.get("classid").toString();
			userid = jsonObject.get("userid").toString();
			comment_content = jsonObject.get("content").toString();
			comment_score = jsonObject.get("score").toString();
			comment_annoy = jsonObject.get("isannoy").toString();
		}
		else
		{
			classid = request.getParameter("classid").trim();
			userid = request.getParameter("userid").trim();
			comment_content = request.getParameter("content").trim();
			comment_score = request.getParameter("score").trim();
			comment_annoy = request.getParameter("isannoy").trim();
		}
		
		
		Comment c = new Comment();
		c.setComment_annoy(Boolean.parseBoolean(comment_annoy));
		c.setComment_class_id(Integer.parseInt(classid));
		c.setComment_content(comment_content);
		c.setComment_deleted(false);
		c.setComment_score(Integer.parseInt(comment_score));
		c.setComment_user(Integer.parseInt(userid));
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = sdf.format(date);
		try {
			Date comment_time = sdf.parse(nowTime);
			c.setComment_time(comment_time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		

		IRegularCommentDao dao = new RegularCommentDao();
		int insert_result = dao.insertComment(c);

		JSONObject j=new JSONObject();
		if(insert_result == 1)
			j.put("status", "200");
		else
			j.put("status", "500");
		
		response.getWriter().write(j.toString()); 
		dao.close();
	}

}
