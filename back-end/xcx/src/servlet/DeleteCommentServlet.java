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
 * Servlet implementation class DeleteCommentServlet
 */
@WebServlet("/DeleteCommentServlet")
public class DeleteCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteCommentServlet() {
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
		
		String comment_id;
		
		String requestHeader = request.getHeader("user-agent");
		
		if(sBuilder != null && !sBuilder.toString().equals(""))
		{
			JSONObject jsonObject = JSONObject.fromObject(sBuilder.toString());
			
			comment_id = jsonObject.get("commentid").toString();
		}
		else
		{
			comment_id = request.getParameter("commentid").trim();
		}
		
		IRegularCommentDao dao = new RegularCommentDao();
		int result = dao.deleteComment(Integer.parseInt(comment_id));
		
		JSONObject j=new JSONObject();
		if(result == 1)
			j.put("status", "200");
		else
			j.put("status", "500");
		
		response.getWriter().write(j.toString()); 
		dao.close();
	}

}
