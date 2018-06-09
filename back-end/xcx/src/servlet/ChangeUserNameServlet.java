package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.IRegularUserDao;
import dao.impl.RegularUserDao;
import net.sf.json.JSONObject;
import po.User;

/**
 * Servlet implementation class ChangeUserNameServlet
 */
@WebServlet("/ChangeUserNameServlet")
public class ChangeUserNameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeUserNameServlet() {
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
		
		String username;
		String userid;
		
		String requestHeader = request.getHeader("user-agent");
		
		if(sBuilder != null && !sBuilder.toString().equals(""))
		{
			JSONObject jsonObject = JSONObject.fromObject(sBuilder.toString());
			
			username = jsonObject.get("username").toString();
			userid = jsonObject.get("userid").toString();
		}
		else
		{
			username = request.getParameter("username").trim();
			userid = request.getParameter("userid").trim();
		}
		
		IRegularUserDao dao = new RegularUserDao();
		int result = dao.modifyUserName(username, Integer.parseInt(userid));

		JSONObject j=new JSONObject();
		if(result == 1)
			j.put("status", "200");
		else if(result == -2) // 无法修改
			j.put("status", "405");
		else if(result == -3) // 用户不存在
			j.put("status", "404");
		else //未知错误
			j.put("status", "500");
		
		response.getWriter().write(j.toString()); 
		dao.close();
	}

}
