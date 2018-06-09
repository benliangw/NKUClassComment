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
import dao.impl.RegularCommentDao;
import net.sf.json.JSONObject;
import po.Comment;

/**
 * Servlet implementation class ModifyCommentServlet
 */
@WebServlet("/ModifyCommentServlet")
public class ModifyCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyCommentServlet() {
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
		
		String comment_content;
		String comment_score;
		String comment_annoy;
		String comment_id;
		
		String requestHeader = request.getHeader("user-agent");
		
		if(sBuilder != null && !sBuilder.toString().equals(""))
		{
			JSONObject jsonObject = JSONObject.fromObject(sBuilder.toString());
			
			comment_content = jsonObject.get("content").toString();
			comment_score = jsonObject.get("score").toString();
			comment_annoy = jsonObject.get("isannoy").toString();
			comment_id = jsonObject.get("commentid").toString();
		}
		else
		{
			comment_content = request.getParameter("content").trim();
			comment_score = request.getParameter("score").trim();
			comment_annoy = request.getParameter("isannoy").trim();
			comment_id = request.getParameter("commentid").trim();
		}
		
		
		Comment c = new Comment();
		c.setComment_annoy(Boolean.parseBoolean(comment_annoy));
		c.setComment_content(comment_content);
		c.setComment_deleted(false);
		c.setComment_score(Integer.parseInt(comment_score));
		c.setComment_id(Integer.parseInt(comment_id));
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //TODO:这里注意一下，如果修改评论，则发布时间按照修改之后的计算
		String nowTime = sdf.format(date);
		try {
			Date comment_time = sdf.parse(nowTime);
			c.setComment_time(comment_time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		

		IRegularCommentDao dao = new RegularCommentDao();
		int result = dao.updateComment(c);
		
		JSONObject j=new JSONObject();
		if(result == 1)
			j.put("status", "200");
		else
			j.put("status", "500");
		
		response.getWriter().write(j.toString()); 
		dao.close();
	}

}
