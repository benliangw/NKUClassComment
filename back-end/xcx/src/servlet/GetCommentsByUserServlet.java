package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.IRegularCommentDao;
import dao.impl.RegularCommentDao;
import net.sf.json.JSONObject;
import po.Comment;
import po.CommentDetail;

/**
 * Servlet implementation class GetCommentsByUserServlet
 */
@WebServlet("/GetCommentsByUserServlet")
public class GetCommentsByUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCommentsByUserServlet() {
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
		
		String userid;
		String cursor; // 游标，从第几个开始
		String requestHeader = request.getHeader("user-agent");
		
		if(sBuilder != null && !sBuilder.toString().equals(""))
		{
			JSONObject jsonObject = JSONObject.fromObject(sBuilder.toString());
			
			userid = jsonObject.get("userid").toString();
			cursor = jsonObject.get("cursor").toString();
		}
		else
		{
			userid= request.getParameter("userid").trim();
			cursor = request.getParameter("cursor").trim();
		}
		
		IRegularCommentDao dao = new RegularCommentDao();
		List<CommentDetail> result = null;
		try {
			result = dao.getCommentByUser(Integer.parseInt(userid), Integer.parseInt(cursor));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONObject j=new JSONObject();
		if(result.size() == 0) //该用户没有评论
			j.put("status", "100");
		else
		{
			j.put("status", "200");
			j.put("comment", result);
		}
		response.getWriter().write(j.toString()); 
		dao.close();
	}

}
